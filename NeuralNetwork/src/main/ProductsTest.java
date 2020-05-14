package main;

import core.Tensor;
import function.activation.Logistic;

public class ProductsTest {
	public static void main(String[]args) {
		Tensor t= new Tensor(new double[][][] {{{3,2}}});
		Tensor t3= new Tensor(new double[][]{{5,6},{4,2}});
		Tensor t2= new Tensor(new double[][]{{1,5},{3,4}});
		
		System.out.println(t.shape());
		t2.print();
		t3.print();
		t2.shape().print();
		Tensor k=t2.tensorProduct(t3);
		k.print();
		k.shape().print();
		
		Tensor tk=new Logistic().activate(t2);
	
		System.out.println(t3.isMatrix());
		 Tensor m= new Tensor();
		 m.print();
		 System.out.println(m.shape());
		
	}
}
