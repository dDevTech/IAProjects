package layers;

import org.json.simple.JSONObject;

import core.Tensor;

public abstract class Layer {
	public static enum Content {BASIC, DETAILED};
	public static Content contentLevel= Content.DETAILED;
	protected Tensor input=null;
	protected Tensor output=null;
	private Tensor outputShape=null;
	public abstract void initialize(Tensor previousShape);
	
	public abstract Tensor feedForward(Tensor t);
	public abstract Tensor backPropagation(Tensor errorSignal);
	public abstract JSONObject toJSON();
	public Tensor getOutput() {
		return output;
	}
	public void setOutput(Tensor output) {
		this.output = output;
	}
	public Tensor getInput() {
		return input;
	}
	public void setInput(Tensor input) {
		this.input = input;
	}

	public Tensor getOutputShape() {
		return outputShape;
	}

	public void setOutputShape(Tensor outputShape) {
		this.outputShape = outputShape;
	}

	@Override
	public String toString() {
		if(output!=null) {
			if(contentLevel == Content.BASIC) {
				return ("Printing layer: "+super.getClass())+"\n Output: "+output.toString()+"\n";
			}else if( contentLevel == Content.DETAILED) {
				return ("Printing layer: "+super.getClass())+"\n Input: "+input.toString()+"\n Output: "+output.toString()+"\n";
			}
			
		}
		System.err.println("Couldnt print layer because it doesnt contain a value yet");
		return null;
		
	}
	
	
	
	
}
