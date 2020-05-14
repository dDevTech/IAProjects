package neuralnetwork;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;

import core.Tensor;
import dataset.DataElement;
import dataset.Dataset;
import errors.Loss;
import function.DiferentiableFunction;
import function.activation.ActivationFunction;
import initializer.Initializer;
import layers.FullyConnectedLayer;
import layers.HiddenLayer;
import layers.InputLayer;
import layers.Layer;
import layers.LearningLayer;
import layers.OutputLayer;
import optimization.Optimizer;
import optimization.SGD;

public class NeuralNetwork {

	private OutputLayer outputLayer;
	private InputLayer inputLayer;
	public ArrayList<Layer> layers = new ArrayList<>();

	public NeuralNetwork(InputLayer input, OutputLayer out, ArrayList<Layer> layers) {
		this.layers = layers;
		this.inputLayer = input;
		this.outputLayer = out;
	}

	public NeuralNetwork(int inputNeurons, int outputNeurons, Initializer outputInitializerWeights,
			Initializer outputInitializerBiases, ActivationFunction activation, Loss errorFunction) {
		setInputLayer(new InputLayer(inputNeurons, 1));
		setOutputLayer(new OutputLayer(
				new FullyConnectedLayer(outputNeurons, outputInitializerWeights, outputInitializerBiases, activation),
				errorFunction));
	}

	public NeuralNetwork(Layer output, Loss errorFunction, double... inputShape) {
		if (output instanceof OutputLayer) {
			System.err.println("Output layer must be different from class output layer");
			return;
		}
		setInputLayer(new InputLayer(inputShape));
		setOutputLayer(new OutputLayer(output, errorFunction));
	}

	public void initialize() {
		double init = System.currentTimeMillis();
		synchronized (layers) {
			inputLayer.initialize(null);
			for (int i = 0; i < layers.size(); i++) {
				Layer l = layers.get(i);
				if (i == 0) {
					l.initialize(inputLayer.getOutputShape());
				} else {
					l.initialize(layers.get(i - 1).getOutputShape());
				}

			}
			outputLayer.initialize(layers.get(layers.size() - 1).getOutputShape());

		}
		System.out.println("Initialized in " + ((System.currentTimeMillis()) - init) / 1000d + "s");
	}

	public Tensor feedForward(Tensor data) {
		// double init=System.currentTimeMillis();
		Tensor dataElement = data.copy();

		synchronized (layers) {
			dataElement = inputLayer.feedForward(dataElement);

			if (Constants.DEBUG)
				System.out.println(inputLayer);
			for (Layer l : layers) {

				dataElement = l.feedForward(dataElement);
				if (Constants.DEBUG)
					System.out.println(l);

			}
			dataElement = outputLayer.feedForward(dataElement);
			if (Constants.DEBUG)
				System.out.println(outputLayer);

		}
		// System.out.println("Updated in "+((System.currentTimeMillis())-init)/1000d);

		return dataElement;
	}

	public void backPropagation(Tensor desired) {
		// double init=System.currentTimeMillis();
		Tensor errorSignal = getOutputLayer().backPropagation(desired);
		if (Constants.DEBUG)
			System.out.println(getOutputLayer());
		for (int i = layers.size() - 1; i >= 0; i--) {
			errorSignal = layers.get(i).backPropagation(errorSignal);

			if (Constants.DEBUG)
				System.out.println(layers.get(i));
		}
		// System.out.println("BackProp in "+((System.currentTimeMillis())-init)/1000d);
	}

	public void adjust(int minibatches,Optimizer optimizer) {
		for (int i = 0; i < layers.size(); i++) {

			if (layers.get(i) instanceof LearningLayer) {
				LearningLayer l = ((LearningLayer) layers.get(i));
				if (i == 0) {
					l.adjust(minibatches,optimizer, inputLayer.getOutput());

					l.clearGradients();

				} else {
					l.adjust(minibatches,optimizer, layers.get(i - 1).getOutput());

					l.clearGradients();
				}

			}

		}
		if (outputLayer.getLayer() instanceof LearningLayer) {

			((LearningLayer) outputLayer.getLayer()).adjust(minibatches,optimizer, layers.get(layers.size() - 1).getOutput());

			((LearningLayer) outputLayer.getLayer()).clearGradients();
		}
	}

	public double calculateError(Tensor desired) {
		return outputLayer.calculateError(desired);
	}

	public NetworkTask train(Dataset dataset, int epochs, int minibatches, Optimizer optimizer) {

		NetworkTask task = new NetworkTask() {

			@Override
			public void run() {

				int batches = 0;
				if (dataset.getData().size() % minibatches == 0) {
					batches = dataset.getData().size() / minibatches + 1;
				} else {
					batches = dataset.getData().size() / minibatches;
				}

				if (this.getMonitor() != null) {
					this.getMonitor().setTotalEpochs(epochs);
					this.getMonitor().setMinibatchSize(minibatches);
					this.getMonitor().setTotalBatches(batches);
				}

				for (int e = 0; e < epochs; e++) {
					if (this.getMonitor() != null)
						this.getMonitor().setEpoch(e);

					Collections.shuffle(dataset.getData());
					
					double sum = 0f;
					boolean done = false;
					for (int i = 0; i < batches; i++) {
						if (this.getMonitor() != null)
							this.getMonitor().setBatch(i);
						for (int j = 0; j < minibatches && !done; j++) {

							int pos = i * minibatches + j;
							if (pos < dataset.getData().size()) {
								DataElement elem = dataset.getData().get(pos);
								feedForward(elem.getInput());
								sum += calculateError(elem.getDesired());
								if (Constants.DEBUG)
									elem.getDesired().print();

								backPropagation(elem.getDesired());
								if (this.getMonitor() != null)
									this.getMonitor().onIteration();
							} else {
								done = true;
							}

						}
						adjust(minibatches,optimizer);

					}
					double errorEpoch = sum / (double) dataset.getData().size();
					if (this.getMonitor() != null)
						this.getMonitor().setError(errorEpoch);
					if (this.getMonitor() != null)
						this.getMonitor().onIteration();
				}
				if (this.getMonitor() != null)
					this.getMonitor().onFinish();

			}

		};
		return task;
	}

	public NetworkTask test(Dataset dataset) {
		
		NetworkTask task = new NetworkTask() {
			
			@Override
			public void run() {
				if (this.getMonitor() != null)
					this.getMonitor().setTotalBatches(dataset.getData().size());
				double sumError = 0d;
				for (DataElement elem : dataset.getData()) {
					if (this.getMonitor() != null)
						this.getMonitor().setInput(elem.getInput());
					if (this.getMonitor() != null)
						this.getMonitor().setDesired(elem.getDesired());
					feedForward(elem.getInput());
					if (this.getMonitor() != null)this.getMonitor().setIteration(dataset.getData().indexOf(elem));
					sumError += calculateError(elem.getDesired());
					if (this.getMonitor() != null)
						this.getMonitor().setOutput(outputLayer.getOutput());
					if (this.getMonitor() != null) {
						try {
							Thread.sleep(this.getMonitor().getTimeBetweenIteration());
						} catch (InterruptedException e) {

						}
					}
					if (this.getMonitor() != null)
						this.getMonitor().onIteration();
					
				}
				if (this.getMonitor() != null)
					this.getMonitor().onFinish();
				System.out.println("Average error: " + sumError / (double) dataset.getData().size());
				if (this.getMonitor() != null)
					this.getMonitor().onFinish();
			}
			
			
		};
		
		return task;
	}

	public void addLayer(Layer layer) {
		layers.add(layer);
	}

	public InputLayer getInputLayer() {
		return inputLayer;
	}

	public void setInputLayer(InputLayer inputLayer) {
		this.inputLayer = inputLayer;
	}

	public OutputLayer getOutputLayer() {
		return outputLayer;
	}

	public void setOutputLayer(OutputLayer outputLayer) {
		this.outputLayer = outputLayer;
	}
}
