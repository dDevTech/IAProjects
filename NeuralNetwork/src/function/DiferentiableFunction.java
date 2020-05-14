package function;

public class DiferentiableFunction {
	private Function activation;
	private Function derivative;

	public DiferentiableFunction(Function activation, Function derivative) {
		this.setActivation(activation);
		this.setDerivative(derivative);
		
	}
	public DiferentiableFunction() {
		
	}
	public Function getDerivative() {
		return derivative;
	}
	public void setDerivative(Function derivative) {
		this.derivative = derivative;
	}
	public Function getActivation() {
		return activation;
	}
	public void setActivation(Function activation) {
		this.activation = activation;
	}
}
