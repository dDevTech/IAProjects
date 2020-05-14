package initializer;

import org.json.simple.JSONObject;

public class Zeros implements Initializer {

	@Override
	public double initialize() {
		
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSON() {
		JSONObject obj= new JSONObject();
		obj.put("Initializer", "Zeros");
		return obj;
	}

}
