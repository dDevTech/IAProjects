package layers;

import org.json.simple.JSONObject;

import core.Tensor;
import function.DiferentiableFunction;
import function.activation.ActivationFunction;


public class ActivationLayer extends Layer {
	private ActivationFunction activation;

	public ActivationLayer(ActivationFunction activation) {
		this.setActivation(activation);

	}

	@Override
	public void initialize(Tensor previousShape) {
		setOutputShape(previousShape.copy());
	}

	@Override
	public Tensor feedForward(Tensor t) {
		setInput(t);
		output=getActivation().activate(t);
		return output;
	}

	@Override
	public Tensor backPropagation(Tensor errorSignal) {
		
		Tensor derivative=getActivation().derivative(input, output);
		derivative.mult(errorSignal);
		return derivative;
	}

	@Override
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	public ActivationFunction getActivation() {
		return activation;
	}

	public void setActivation(ActivationFunction activation) {
		this.activation = activation;
	}

}
