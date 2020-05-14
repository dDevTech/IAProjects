package initializer;

import org.json.simple.JSONObject;

public interface Initializer {
	public double initialize();
	public JSONObject toJSON();
}
