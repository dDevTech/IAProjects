package main;

import core.Tensor;
import function.activation.Logistic;

public class TensorsTutorial {
	public static void main(String[]args) {
		double[][]matrix = new double[][] {{2,5,2},{1,2,3}};
		
		Tensor t= new Tensor(matrix);
		t.print();
		t.shape().print();
	
		Tensor t3=t.reshape(6,1);
		t3.squeeze();
		t3.shape().print();
		t3.print();
	}
}
