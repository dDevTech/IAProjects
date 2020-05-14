package layers;

import org.json.simple.JSONObject;

import core.Tensor;



public class InputLayer extends Layer {

	private double[] shape;

	public InputLayer(double... shape) {
		this.shape = shape;
	}
	
	@Override
	public void initialize(Tensor previouShape) {
		setOutputShape(new Tensor(shape));
	}
	
	@Override
	public Tensor feedForward(Tensor inputData) {
		this.output = inputData;
		this.input = inputData;
		return inputData;
	}

	@Override
	public Tensor backPropagation(Tensor errorSignal) {
		return null;
	}

	@Override
	public JSONObject toJSON() {
		return null;
	}

	public Tensor getInputData() {
		return output;
	}

	public void setInputData(Tensor inputData) {
		this.output = inputData;
	}

	

}
