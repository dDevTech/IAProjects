package main;

import java.io.File;
import java.util.Arrays;

import org.json.simple.JSONArray;

import core.Tensor;
import initializer.Random;

public class Main {

	public static void main(String[] args) {
		Tensor tensor= new Tensor(new double[]{1.0d,2.0d,2.0d});
		Tensor tensor2= new Tensor(new double[][][]{{{3,2},{5,7},{3,2}}});
		Tensor tensor3= new Tensor(tensor);
		Tensor tensor4= new Tensor(new double[][][]{{{0.1d}},{{0.1d}}});
		Tensor tensor5= new Tensor(tensor4);
		Tensor tensor6= new Tensor(new double[]{3,5,6,3,2});
		Tensor tensor7= new Tensor();
		Tensor tensor8= new Tensor(new double[][][]{{{0.5d}},{{0.4d}}});
		Tensor tensor9= new Tensor(9d);
		Tensor tensor10= new Tensor(9d);
		Tensor tensor11= new Tensor( new Random(),new int[] {1,5,5});
		Tensor tensor12= new Tensor(new double[][] {{5f,3f},{3F,2f},{3F,2f},{4f,6f},{3f,1f}}) ;
		Tensor tensor13= new Tensor(new double[][][] {{{5f,3f},{3F,2f},{3F,2f}},{{7f,4f},{5F,4f},{4F,4f}}});
		Tensor tensor14= new Tensor(new double[][] {{5f,3f},{3F,2f}}) ;
		Tensor tensor15= new Tensor(new double[][] {{5f,3f},{3F,2f},{3f,1f}}) ;
		Tensor tensor16= new Tensor(new double[][] {{5f},{3f}}) ;
		tensor16.print();
		Tensor ttt=tensor16.getTranspose(2,1);
		ttt.print();
		ttt.saveTensor(new File("tensor1.json"));
		Tensor ttt2=Tensor.loadTensor(new File("tensor1.json"));
		ttt2.print();
		Tensor c=tensor14.join(tensor15);
		c.print();
		Tensor k=tensor13.reshape(1,2,2,3);
		k.shape().print();
		
		k.print();
		
		
		Tensor[]tensors=tensor12.split(0,1,2,1,1);
		for(Tensor t : tensors) {
			t.squeeze();
			System.out.println(t);
		}
		
		
		
		
		tensor12.print();
		
		tensor11.print();
		
		tensor9.mult(tensor10);
		tensor9.squeeze();
		tensor9.print();
		
		tensor4.print();
		tensor8.print();
		tensor4.mult(8.0d);
		tensor4.squeeze();
		tensor4.print();
		tensor4.set(5d, 0);
		tensor4.print();
		System.out.println("Get tensor");
		Tensor t0=tensor4.get(0);
		t0.print();
		System.out.println("Result");
		
	
		tensor4.shape().print();
		tensor4.squeeze();
		tensor4.print();
		System.out.println(tensor4.rank());
		
		//Tensor 1 test
		tensor.print();
		System.out.println("Shape");
		tensor.shape().print();
		
		tensor.set(10f, 2);
		tensor.print();
	
		
		//Tensor 2 test
		tensor2.print();
		tensor2.set(10f, 0,0,1);
		
		tensor2.get(0,1).print();
		tensor2.print();
		tensor2.set(new Tensor(new double[]{8.0d,6.0d}) ,0,0);
		tensor2.print();
		tensor2.squeeze();
		tensor2.print();
		//tensor2.getShape().printTensor();
	}

}
