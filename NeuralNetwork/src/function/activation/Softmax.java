package function.activation;

import core.Tensor;
import function.Function;

public class Softmax extends ActivationFunction{

	@Override
	public Tensor activate(Tensor input) {
		Tensor total=input.applyFunction(new Function() {
			
			@Override
			public double calculate(double value) {
				
				return Math.pow(Math.E, value);
			}
		});
		double sum=total.sumUp();
		total.mult(1/sum);
		return total;
	}

	@Override
	public Tensor derivative(Tensor input, Tensor output) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
