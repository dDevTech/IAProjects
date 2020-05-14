package layers;

import org.json.simple.JSONObject;

import core.Tensor;
import exceptions.DimensionalityMismatch;
import initializer.Initializer;
import optimization.Optimizer;

public class HiddenLayer extends LearningLayer {

	private int neurons;

	public HiddenLayer(int neurons, Initializer weights, Initializer biases) {
		super(weights, biases);
		this.neurons = neurons;
	}

	@Override
	public void initialize(Tensor previousShape) {
		if (previousShape.isVector()) {
			setWeights(new Tensor(weightsInitializer, neurons, (int)previousShape.getComponents()[0].getValue()));
			setBiases(new Tensor(biasesInitializer, neurons, 1));
			setOutputShape(new Tensor(new double[] {neurons, 1}));//shape [neurons,1] to use it dot product;
		
		} else {
			throw new DimensionalityMismatch("Previous layer must be flattened and have one dimension");
		}
		

	}

	@Override
	public Tensor feedForward(Tensor t) {
		input = t;
		
		if (t.isMatrix()) {
			Tensor result = getWeights().dot(input);
			result.add(getBiases());
			output = result;
			
			return result;
		}
		throw new DimensionalityMismatch("Mismatch dimensions");

	}

	@Override
	public Tensor backPropagation(Tensor errorSignal) {
		this.errorSignal=errorSignal;
		Tensor t = getWeights().getTranspose().dot(errorSignal);
		Tensor gradient=errorSignal.dot(input.getTranspose());
		addGradientWeights(gradient);
		addGradientBiases(errorSignal.copy());
		return t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSON() {
		JSONObject layer = new JSONObject();
		layer.put("layer", "HiddenLayer");
		layer.put("weights", getWeights().toJSON());
		layer.put("bias", getBiases().toJSON());
		layer.put("neurons", neurons);
		layer.put("weightInitializer", weightsInitializer.toJSON());
		layer.put("biasInitializer", biasesInitializer.toJSON());
		return layer;
	}

	@Override
	public void adjust(int minibatches,Optimizer optimizer,Tensor neuronsPreviousLayer) {
		optimizer.optimize(minibatches,getBiases(),getWeights(),getGradientWeights(),getGradientBiases());
		
	}

}
