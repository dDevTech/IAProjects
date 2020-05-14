package errors;

import core.Tensor;


public interface Loss {
	
	public double calculateError(Tensor desired,Tensor output);
	public Tensor derivative(Tensor desired,Tensor output);
	
}
