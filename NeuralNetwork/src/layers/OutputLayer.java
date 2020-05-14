package layers;

import org.json.simple.JSONObject;

import core.Tensor;
import errors.CrossEntropy;
import errors.Loss;
import function.DiferentiableFunction;
import function.activation.Softmax;
import initializer.Initializer;

public class OutputLayer extends Layer {
	private Layer layer;
	private Loss errorFunction;

	public OutputLayer(Layer l, Loss errorFunction) {
		setLayer(l);
		this.errorFunction = errorFunction;
	}

	@Override
	public void initialize(Tensor previousShape) {
		getLayer().initialize(previousShape);
		setOutputShape(getLayer().getOutputShape());

	}

	@Override
	public Tensor feedForward(Tensor t) {
		input = t;
		return output = getLayer().feedForward(t);
	}

	@Override
	public Tensor backPropagation(Tensor errorSignal) {
		Tensor signal = null;
		if (errorFunction instanceof CrossEntropy)
			if (getLayer() instanceof FullyConnectedLayer) {
				if (((FullyConnectedLayer) getLayer()).getActivationLayer().getActivation() instanceof Softmax) {
					signal = output.copy();
					signal.sub(errorSignal);
					return ((FullyConnectedLayer) getLayer()).getConnectedLayer().backPropagation(signal);
				}
			}
		signal = errorFunction.derivative(errorSignal, output);
		return getLayer().backPropagation(signal);

	}

	@Override
	public String toString() {

		return "Output layer {\n" + getLayer().toString() + "\n" + super.toString() + "}\n";
	}

	public double calculateError(Tensor desired) {
		if (desired.shape().equals(output.shape())) {
			return errorFunction.calculateError(desired, output);
		}
		System.err.println("Desired tensor must have the same shape as the output shape of output layer");
		return 0d;

	}

	@Override
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	public Layer getLayer() {
		return layer;
	}

	public void setLayer(Layer layer) {
		this.layer = layer;
	}

}
