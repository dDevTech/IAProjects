package neuralnetwork;

public abstract class NetworkTask implements Runnable{	
	private NetworkMonitor monitor;

	public NetworkTask() {
		
	}
	public NetworkMonitor getMonitor() {
		return monitor;
	}
	public void setMonitor(NetworkMonitor monitor) {
		this.monitor = monitor;
	}
}
