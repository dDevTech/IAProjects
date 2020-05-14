package function.activation;

import core.Tensor;
import function.Function;

public class Logistic extends ActivationFunction {

	@Override
	public Tensor activate(Tensor tensor) {
		return tensor.applyFunction(new Function() {

			@Override
			public double calculate(double value) {

				return 1 / (1 + Math.pow(Math.E, -value));
			}

		});

	}

	@Override
	public Tensor derivative(Tensor input, Tensor output) {
		return output.applyFunction(new Function() {

			@Override
			public double calculate(double value) {

				return value * (1 - value);
			}

		});

	}

}
