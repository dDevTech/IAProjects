package optimization;

import core.Tensor;
import neuralnetwork.Constants;

public class SGD extends Optimizer{

	@Override
	public void optimize(int minibatches,Tensor biases, Tensor weights, Tensor sumgradientWeights, Tensor sumgradientBiases) {
		super.optimize(minibatches,biases, weights, sumgradientWeights, sumgradientBiases);
	}
	
	

}
