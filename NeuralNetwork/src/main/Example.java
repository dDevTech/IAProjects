package main;

import java.io.File;

import core.Tensor;
import initializer.Random;

public class Example {

	public static void main(String[] args) {
		//3D matrix
		Tensor t = new Tensor(new double[][][] {{{3,2},{4,1}},{{3,2},{4,1}}});
		
		System.out.println("Initial array");
		t.print();
		System.out.println("Shape");
		t.shape().print();
		
		System.out.println("Reshape 4,2");
		Tensor tensor=t.reshape(4,2);
		tensor.print();
		System.out.println("Shape");
		tensor.shape().print();
		
		System.out.println("Split 1,1,2");
		Tensor[]tensors=tensor.split(1,1,2);
		tensors[0].print();
		tensors[1].print();
		tensors[2].print();
		
		System.out.println("Squeeze");
		tensors[0].squeeze();
		tensors[1].squeeze();
		tensors[2].squeeze();
		
		tensors[0].print();
		tensors[1].print();
		tensors[2].print();
		
		System.out.println("Flatten [2] array");
		tensors[2].flatten().print();
		
		System.out.println("Create copy");
		Tensor k=tensors[2].copy();
		System.out.println("Mult 3");
		k.mult(3f);
		System.out.println("Reference doesnt change");
		tensors[2].print();
		System.out.println("Copy changes");
		k.print();
		
		System.out.println("Safe copy tensor");
		k.saveTensor(new File("tensor2"));
		System.out.println("Load tensor");
		Tensor load=Tensor.loadTensor(new File("tensor2"));
		load.print();
		
		System.out.println("Transpose initial tensor with 213");
		
		Tensor c=new Tensor(new double[][] {{4,5,3}});
		c.print();
		Tensor kk=c.getTranspose(2,1);
		kk.print();
		kk.shape().print();
		
//		System.out.println("Hard task");
//		Tensor m= new Tensor(new Random(),32,32,3);
//		for(int  i=0;i<10000;i++) {
//			m.getTranspose(2,1,3);
//		}
//		System.out.println("Done");

	}

}
