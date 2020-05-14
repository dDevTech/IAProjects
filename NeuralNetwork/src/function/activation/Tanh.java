package function.activation;

import core.Tensor;
import function.Function;

public class Tanh extends ActivationFunction{
	@Override
	public Tensor activate(Tensor tensor) {
		return tensor.applyFunction(new Function() {

			@Override
			public double calculate(double value) {
				double v=Math.pow(Math.E,value);
				return (v-(1/v))/(v+(1/v));
			}

		});

	}

	@Override
	public Tensor derivative(Tensor input, Tensor output) {
		return output.applyFunction(new Function() {

			@Override
			public double calculate(double value) {

				return 1-value*value;
			}

		});

	}
}
