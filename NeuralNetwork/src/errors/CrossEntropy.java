package errors;

import core.Tensor;
import function.Function;

public class CrossEntropy implements Loss{

	@Override
	public double calculateError(Tensor desired, Tensor output) {
		Tensor copy=desired.copy();
		Tensor log=output.applyFunction(new Function() {
			
			@Override
			public double calculate(double value) {
				
				return Math.log(value);
			}
		});
		copy.mult(log);
		return copy.sumUp()*-1;
	}

	@Override
	public Tensor derivative(Tensor desired, Tensor output) {
		Tensor copy=desired.copy();
		copy.div(output);
		copy.mult(-1);
		return copy;
	}

}
