package neuralnetwork;

import core.Tensor;
import utils.ConsoleUtils;

public abstract class NetworkMonitor {
	private int epoch=0;
	private int batch=0;
	private int iteration=0;
	private int totalEpochs=0;
	private int totalBatches=0;
	private int minibatchSize=0;
	private int timeBetweenIteration=500;
	private double error;
	private boolean printStatus=true;
	private Tensor desired;
	private Tensor input;
	private Tensor output;
	
	public abstract void onFinish();
	public abstract void onIteration();
	public int getEpoch() {
		return epoch;
	}
	public void setEpoch(int epoch) {
		if(printStatus)
		System.out.println("Epoch: " + epoch + " / " + totalEpochs);
		this.epoch = epoch;
		
	}
	public int getBatch() {
		return batch;
	}
	public void setBatch(int batch) {
		this.batch = batch;
		if(printStatus)
		ConsoleUtils.progressPercentage(batch+1, totalBatches);
		
	}
	
	public int getIteration() {
		return iteration;
	}
	public void setIteration(int iteration) {
		
		
		this.iteration = iteration;
		
	}
	public int getTotalBatches() {
		return totalBatches;
	}
	public void setTotalBatches(int totalBatches) {
		if(printStatus)
		System.out.println("Batches: " + totalBatches);
		this.totalBatches = totalBatches;
	}
	public int getTotalEpochs() {
		return totalEpochs;
	}
	public void setTotalEpochs(int totalEpochs) {
		this.totalEpochs = totalEpochs;
	}
	public int getMinibatchSize() {
		return minibatchSize;
	}
	public void setMinibatchSize(int minibatchSize) {
		this.minibatchSize = minibatchSize;
	}
	public Tensor getInput() {
		return input;
	}
	public void setInput(Tensor input) {
		this.input = input;
		
	}
	public Tensor getOutput() {
		return output;
	}
	public void setOutput(Tensor output) {
		this.output = output;
		
	}
	public Tensor getDesired() {
		return desired;
	}
	public void setDesired(Tensor desired) {
		this.desired = desired;
	}
	public int getTimeBetweenIteration() {
		return timeBetweenIteration;
	}
	public void setTimeBetweenIteration(int timeBetweenIteration) {
		this.timeBetweenIteration = timeBetweenIteration;
	}
	public double getError() {
		return error;
	}
	public void setError(double error) {
		System.out.println("Error: " + error);
		this.error = error;
	}
	public boolean isPrintStatus() {
		return printStatus;
	}
	public void setPrintStatus(boolean printStatus) {
		this.printStatus = printStatus;
	}
	
}
