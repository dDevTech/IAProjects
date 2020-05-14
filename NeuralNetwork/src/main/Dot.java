package main;

import core.Tensor;
import initializer.Random;
import initializer.Zeros;

public class Dot {

	public static void main(String[] args) {
		Tensor t= new Tensor(new double[][] {{3,2,5},{4,2,5}});
		Tensor t2= new Tensor(new double[][]{{3,2,5,4},{4,2,5,4},{3,2,5,4}});
		
		Tensor t3= new Tensor(new Zeros(),5,3,4);
		Tensor t4= new Tensor(new Zeros(),5,4,2);
		t.shape().print();
		t2.shape().print();
		Tensor k=t.dot(t2);
	
		k.print();
		k.shape().print();
		k.tensorProduct(t).print();
	}

}
