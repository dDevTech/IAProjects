package function.activation;

import core.Tensor;
import function.Function;

public class ReLU extends ActivationFunction {

	@Override
	public Tensor activate(Tensor input) {

		return input.applyFunction(new Function() {

			@Override
			public double calculate(double value) {

				return value > 0 ? value : 0;
			}

		});
	}

	@Override
	public Tensor derivative(Tensor input, Tensor output) {

		return input.applyFunction(new Function() {

			@Override
			public double calculate(double value) {

				return value > 0 ? 1 : 0;
			}

		});
	}
}
