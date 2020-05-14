package layers;

import org.json.simple.JSONObject;

import core.Tensor;
import function.DiferentiableFunction;
import function.activation.ActivationFunction;
import initializer.Initializer;
import optimization.Optimizer;

public class FullyConnectedLayer extends LearningLayer {
	private HiddenLayer connectedLayer;
	private ActivationLayer activationLayer;

	public FullyConnectedLayer(int neurons, Initializer weightsInit, Initializer biasInit,
			ActivationFunction activation) {
		super(weightsInit, biasInit);
		setConnectedLayer(new HiddenLayer(neurons, weightsInit, biasInit));
		setActivationLayer(new ActivationLayer(activation));
	}

	@Override
	public void initialize(Tensor previousOutput) {
		getConnectedLayer().initialize(previousOutput);
		getActivationLayer().initialize(getConnectedLayer().getOutputShape());
		setOutputShape(getActivationLayer().getOutputShape());
	}

	
	@Override
	public String toString() {
		
		return "Printin Fully Connected Layer {\n"+getConnectedLayer().toString()+"\n"+getActivationLayer().toString()+"}\n";
	}

	@Override
	public Tensor feedForward(Tensor t) {
		input = t;
		Tensor outputTensor = getActivationLayer().feedForward(getConnectedLayer().feedForward(t));
		output = outputTensor;
		return outputTensor;
	}

	@Override
	public Tensor backPropagation(Tensor errorSignal) {
		this.errorSignal = errorSignal;
		Tensor backPropActivation = (getActivationLayer().backPropagation(errorSignal));
		Tensor backPropConnected = getConnectedLayer().backPropagation(backPropActivation);
		setGradientWeights(getConnectedLayer().getGradientWeights());
		setGradientBiases(getConnectedLayer().getGradientBiases());
		return backPropConnected;
	}

	@Override
	public void adjust(int minibatches,Optimizer optimizer, Tensor neuronsPreviousLayer) {
	
		getConnectedLayer().adjust(minibatches,optimizer, neuronsPreviousLayer);

	}

	@Override
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	public ActivationLayer getActivationLayer() {
		return activationLayer;
	}

	public void setActivationLayer(ActivationLayer activationLayer) {
		this.activationLayer = activationLayer;
	}

	public HiddenLayer getConnectedLayer() {
		return connectedLayer;
	}

	public void setConnectedLayer(HiddenLayer connectedLayer) {
		this.connectedLayer = connectedLayer;
	}

}
