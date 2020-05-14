package layers;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import core.Tensor;
import initializer.Initializer;
import optimization.Optimizer;

public abstract class LearningLayer extends Layer {

	private Tensor weights;
	private Tensor biases;

	// Accumulative
	private Tensor gradientWeights;
	private Tensor gradientBiases;

	protected Tensor errorSignal;

	protected Initializer weightsInitializer;
	protected Initializer biasesInitializer;

	public abstract void adjust(int minibatches,Optimizer optimizer, Tensor neuronsPreviousLayer);

	public LearningLayer(Initializer weights, Initializer biases) {
		this.weightsInitializer = weights;
		this.biasesInitializer = biases;
	}

	@Override
	public void initialize(Tensor previousShape) {

	}

	@Override
	public Tensor feedForward(Tensor t) {
		return null;
	}

	@Override
	public Tensor backPropagation(Tensor errorSignal) {
		return null;
	}

	@Override
	public JSONObject toJSON() {
		return null;
	}

	@Override
	public String toString() {
		return super.toString() + " Weights: " + getWeights().toString() + "\n Biases: " + getBiases().toString()
				+ "\n Error signal: " + errorSignal;
	}

	public Tensor getWeights() {
		return weights;
	}

	public void setWeights(Tensor weights) {
		this.weights = weights;
	}

	public Tensor getBiases() {
		return biases;
	}

	public void setBiases(Tensor biases) {
		this.biases = biases;
	}

	public void addGradientWeights(Tensor gradient) {
		if (getGradientWeights() == null) {
			setGradientWeights(gradient);
		} else {
			getGradientWeights().add(gradient);
		}
	}

	public void addGradientBiases(Tensor gradient) {
		if (getGradientBiases() == null) {
			setGradientBiases(gradient);
		} else {
			setGradientBiases(gradient);
			getGradientBiases().add(gradient);
		}

	}

	public void clearGradients() {
		setGradientBiases(null);
		setGradientWeights(null);
	}

	public Tensor getGradientBiases() {
		return gradientBiases;
	}

	public void setGradientBiases(Tensor gradientBiases) {
		this.gradientBiases = gradientBiases;
	}

	public Tensor getGradientWeights() {
		return gradientWeights;
	}

	public void setGradientWeights(Tensor gradientWeights) {
		this.gradientWeights = gradientWeights;
	}

}
