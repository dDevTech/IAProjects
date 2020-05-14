package main;

import core.Tensor;
import initializer.Random;
import initializer.Zeros;

public class TensorTestNew {

	public static void main(String[] args) {
		long k= System.currentTimeMillis();
		Tensor t = new Tensor();
		t.generateTensor(new int[] {728,20},new Random());
		t.shape().print();
		System.out.println((System.currentTimeMillis()-k)/1000d);
		long k2= System.currentTimeMillis();
		
		Tensor t2 = new Tensor();
		t2.generateTensor(0,new int[] {728,20},new Random());
		t2.shape().print();
		System.out.println((System.currentTimeMillis()-k2)/1000d);
		

	}

}
