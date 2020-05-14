package dataset;

import core.Tensor;

public class DataElement {
	private Tensor input;
	private Tensor desired;
	public DataElement() {
		
	}
	
	public Tensor getDesired() {
		return desired;
	}
	public void setDesired(Tensor desired) {
		this.desired = desired;
	}
	public Tensor getInput() {
		return input;
	}
	public void setInput(Tensor input) {
		this.input = input;
	}
}
