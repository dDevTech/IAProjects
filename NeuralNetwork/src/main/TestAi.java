package main;

import java.io.IOException;

import core.Tensor;
import dataset.Dataset;
import dataset.MINST;
import errors.MSE;
import function.activation.Logistic;
import initializer.Random;
import initializer.Zeros;
import layers.ActivationLayer;
import layers.FullyConnectedLayer;
import layers.HiddenLayer;
import layers.InputLayer;
import layers.OutputLayer;
import neuralnetwork.NeuralNetwork;
import optimization.SGD;

public class TestAi {

	public static void main(String[] args) {
//		NeuralNetwork net= new NeuralNetwork(10,10,new Random(-1,1),new Zeros(),new Logistic(),new MSE());
//		net.addLayer(new FullyConnectedLayer(728, new Random(-1,1), new Zeros(),new Logistic()));
//		net.addLayer(new FullyConnectedLayer(20, new Random(-1,1), new Zeros(),new Logistic()));
//		net.addLayer(new FullyConnectedLayer(20, new Random(-1,1), new Zeros(),new Logistic()));
//		net.initialize();
//		for(int i=0;i<100;i++) {
//			net.feedForward(new Tensor(new double[][] {{0.5},{0.3},{0.2},{0.1},{0.5},{0.5},{0.3},{0.2},{0.1},{0.5}}));
//			double e=net.calculateError(new Tensor(new double[][] {{0.1},{0.4},{0.5}}));
//			System.out.println("Error: "+e);
//			net.backPropagation(new Tensor(new double[][] {{0.5},{0.3},{0.2},{0.1},{0.5},{0.5},{0.3},{0.2},{0.1},{0.5}}));
//			net.adjust(new SGD());
//		}
//		net.feedForward(new Tensor(new double[][] {{0.5},{0.3},{0.2},{0.1},{0.5},{0.5},{0.3},{0.2},{0.1},{0.5}}));
//		net.getOutputLayer().getOutput().print();
//		double e=net.calculateError(new Tensor(new double[][] {{0.1},{0.4},{0.5}}));
//		System.out.println("Error: "+e);
//		System.out.println("Done!");

		NeuralNetwork n = new NeuralNetwork(784, 10, new Random(-1, 1), new Zeros(), new Logistic(), new MSE());
		n.addLayer(new FullyConnectedLayer(20, new Random(-1, 1), new Zeros(), new Logistic()));
		n.addLayer(new FullyConnectedLayer(20, new Random(-1, 1), new Zeros(), new Logistic()));
		MINST m = new MINST() {

			@Override
			public void onFinish(Dataset d) {
				n.initialize();
				n.train(d, 1, 32, new SGD());
				n.test(d);

			}
		};
		m.readData(60000, "datasets/train-images.idx3-ubyte", "datasets/train-labels.idx1-ubyte");

		// n.addLayer(new HiddenLayer(20, new Random(-1,1), new Zeros()));
		//// n.addLayer(new ActivationLayer(new Logistic()));
		// n.addLayer(new HiddenLayer(20, new Random(-1,1), new Zeros()));
		// n.addLayer(new ActivationLayer(new Logistic()));

	}

}
