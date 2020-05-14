package errors;

import core.Tensor;
import function.Function;

public class MSE implements Loss {

	@Override
	public double calculateError(Tensor desired, Tensor output) {
		Tensor copy = desired.copy();
		copy.sub(output);
		Tensor out=copy.applyFunction(new Function() {

			@Override
			public double calculate(double value) {

				return value * value;

			}
		});
		return  out.sumUp()/output.shape().getComponents()[0].getValue();

	}

	@Override
	public Tensor derivative(Tensor desired, Tensor output) {
		Tensor copy=desired.copy();
		copy.sub(output);
		copy.mult(-2d);
		return copy;
	}
	
	

}
