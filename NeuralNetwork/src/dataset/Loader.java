package dataset;

import java.io.IOException;

public abstract class Loader {
	public abstract void readData(int numberOfData, String dataFilePath, String labelFilePath);
	public abstract void onFinish(Dataset d);
}
