package function.activation;

import core.Tensor;
import function.Function;

public class LeakyRelu extends ActivationFunction{
	private double steep;

	public LeakyRelu(double steep) {
		this.steep = steep;
		
	}
	@Override
	public Tensor activate(Tensor input) {

		return input.applyFunction(new Function() {

			@Override
			public double calculate(double value) {

				return value > 0 ? value*steep : 0;
			}

		});
	}

	@Override
	public Tensor derivative(Tensor input, Tensor output) {

		return input.applyFunction(new Function() {

			@Override
			public double calculate(double value) {

				return value > 0 ? steep : 0;
			}

		});
	}

}
