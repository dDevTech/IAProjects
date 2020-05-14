package initializer;

import org.json.simple.JSONObject;

import core.Dimensionality;

public class Random implements Initializer{
	double leftLimit=0;
	double rightLimit=1;
	java.util.Random r= new java.util.Random();;
	public Random() {
		
	}
	public Random(double leftLimit,double rightLimit) {
		
		if(rightLimit-leftLimit>0) {
			this.leftLimit=leftLimit;
			this.rightLimit=rightLimit;
		}else {
			throw new Dimensionality("Right limit must be larger than left limit");
		}
		
	}
	@Override
	public double initialize() {
		return leftLimit+r.nextGaussian()*(rightLimit-leftLimit);
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSON() {
		JSONObject obj= new JSONObject();
		obj.put("Initializer", "Random");
		obj.put("RightLimit", rightLimit);
		obj.put("LeftLimit", leftLimit);
		return obj;
	}
	
}
