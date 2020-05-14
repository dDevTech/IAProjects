package optimization;

import core.Tensor;

public abstract class Optimizer {
	private double learningRateWeights=0.01;
	private double learningRateBiases=0.02;
	private double decay=0;
	public double getLearningRateBiases() {
		return learningRateBiases;
	}
	public void setLearningRateBiases(double learningRateBiases) {
		this.learningRateBiases = learningRateBiases;
	}
	public double getLearningRateWeights() {
		return learningRateWeights;
	}
	public void setLearningRateWeights(double learningRateWeights) {
		this.learningRateWeights = learningRateWeights;
	}
	
	public void optimize(int minibatches,Tensor biases,Tensor weights,Tensor sumgradientWeights,Tensor sumgradientBiases) {
	
		sumgradientWeights.mult(getLearningRateWeights()/(double)minibatches);
		Tensor copy=weights.copy();
		copy.mult(getDecay()*getLearningRateWeights());
		sumgradientWeights.add(copy);
		weights.sub(sumgradientWeights);
		
		sumgradientBiases.mult(getLearningRateBiases()/(double)minibatches);
		Tensor copyBias=biases.copy();
		copyBias.mult(getDecay()*getLearningRateBiases());
		sumgradientBiases.add(copyBias);
		biases.sub(sumgradientBiases);
		
	
	}
	public double getDecay() {
		return decay;
	}
	public void setDecay(double decay) {
		this.decay = decay;
	}
}
