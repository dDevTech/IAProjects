package function.activation;

import core.Tensor;

public abstract class ActivationFunction{
	public abstract Tensor activate(Tensor input);
	public abstract Tensor derivative(Tensor input,Tensor output);
}
