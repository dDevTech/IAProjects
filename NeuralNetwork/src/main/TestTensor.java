package main;






import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import core.Dimensionality;
import core.Tensor;
import initializer.Zeros;

class TestTensor {
	Tensor vector= new Tensor(new double[]{2,5,3,6});
	Tensor matrix= new Tensor(new double[][]{{2,5},{3,6}});
	Tensor tensor= new Tensor(new double[][][]{{{3,5},{5,3}}});
	Tensor tensor2= new Tensor(new double[][][]{{{3,2},{5,3}}});
	Tensor tensor3= new Tensor(tensor);
	Tensor tensor4= new Tensor(2d);
	Tensor tensor5= new Tensor(tensor4);
	Tensor tensor6= new Tensor(new double[]{3,5,6,3,2});
	Tensor tensor7= new Tensor();
	Tensor tensor8= new Tensor(new double[][][]{{{0.5d}},{{0.4d}}});
	Tensor tensor9= new Tensor(9d);
	Tensor tensor10= new Tensor(9d);
	Tensor tensor11= new Tensor( new Zeros(),new int[] {1,5,5});
	Tensor tensor12= new Tensor(new double[][] {{5f,3f},{3F,2f},{3F,2f}});
	Tensor tensor13= new Tensor(new double[] {5f});
	Tensor tensor14= new Tensor(new double[] {});
	Tensor tensor15= new Tensor(new Tensor[] {new Tensor(2d),new Tensor(3d)});
	Tensor tensor16= new Tensor(new double[][][] {{{3d}}});
	Tensor tensor17= new Tensor(new double[][][] {{{3d,2d}}});
	Tensor tensor18= new Tensor(new double[][][]{{{3,2},{5,3}},{{5,3},{4,1}}});
	Tensor tensor19= new Tensor(new double[][][]{{{4,6},{-5,3}},{{1,4},{0,5}}});
	Tensor tensor20= new Tensor(new double[] {2f});
	@Test
	void constructor1() {
		assertEquals(tensor.toString(), "[[[3.0, 5.0][5.0, 3.0]]]", "");
	}
	@Test
	void constructor2() {
		assertEquals(tensor4.toString(), "2.0", "");
	}
	@Test
	void constructor3() {
		assertEquals(tensor11.toString(),"[[[0.0, 0.0, 0.0, 0.0, 0.0][0.0, 0.0, 0.0, 0.0, 0.0][0.0, 0.0, 0.0, 0.0, 0.0][0.0, 0.0, 0.0, 0.0, 0.0][0.0, 0.0, 0.0, 0.0, 0.0]]]");
	}
	@Test
	void constructor4() {
		assertEquals(tensor15.toString(), "[2.0, 3.0]", "");
	}
	@Test
	void specialCases1() {
		assertEquals(tensor4.toString(),"2.0");
		assertEquals(tensor4.shape().toString(),"[]","");
		assertEquals(tensor4.rank(),0,"");
	}
	@Test
	void specialCases2() {
		assertEquals(tensor13.toString(),"[5.0]");
		assertEquals(tensor13.shape().toString(),"[1.0]","");
		assertEquals(tensor13.rank(),1,"");
	}
	@Test
	void specialCases3() {
		assertEquals(tensor14.toString(),"[]");
		assertEquals(tensor14.shape().toString(),"[0.0]","");
		assertEquals(tensor14.rank(),1,"");
	}
	@Test
	void copyConstructor() {
		assertEquals(tensor3,tensor);
		assert(tensor3!=tensor);
	
	}
	@Test
	void copy() {
		assertEquals(tensor.copy(),tensor);
		assert(tensor.copy()!=tensor);
	
	}
	@Test
	void vector() {
		assertEquals(vector.toString(),"[2.0, 5.0, 3.0, 6.0]");
	}
	@Test
	void matrix() {
		assertEquals(matrix.toString(),"[[2.0, 5.0][3.0, 6.0]]");
	}
	@Test
	void matrixShape() {
		assertEquals(matrix.shape().toString(),"[2.0, 2.0]");
	}
	@Test
	void squeezeTest1() {
		Tensor copy= tensor12.copy();
		tensor12.squeeze();
		assertEquals(tensor12,copy);
	}
	@Test
	void squeezeTest2() {
		
		tensor16.squeeze();
		assertEquals(tensor16.toString(),"3.0");
		
		tensor17.squeeze();
		assertEquals(tensor17.toString(),"[3.0, 2.0]");
	}
	@Test
	void squeezeTest3() {
		//?
		tensor14.squeeze();
		assertEquals(tensor14.toString(),"[]");
		
		
	}
	@Test
	void squeezeTest5() {
		
		tensor.squeeze();
		assertEquals(tensor.toString(),"[[3.0, 5.0][5.0, 3.0]]");
		
		
	}
	@Test
	void squeezeTest6() {
		
		tensor18.squeeze();
		assertEquals(tensor18.toString(),"[[[3.0, 2.0][5.0, 3.0]][[5.0, 3.0][4.0, 1.0]]]");
		
		
	}
	@Test
	void add() {
		Tensor t=tensor18.copy();
		Tensor t2=tensor19.copy();
		tensor18.add(tensor19);
		assertEquals(t2,tensor18);
		assertEquals(t.toString(),"[[[3.0, 2.0][5.0, 3.0]][[5.0, 3.0][4.0, 1.0]]]");
		assertEquals(tensor18.toString(),"[[[7.0, 8.0][0.0, 6.0]][[6.0, 7.0][4.0, 6.0]]]");
	}
	
	@Test
	void addSpecial() {
		tensor9.add(tensor10);
		assertEquals(tensor9.toString(),"18.0");
		tensor13.add(tensor20);
		assertEquals(tensor13.toString(),"[7.0]");
		
	
	}
	
	@Test
	void addError() {
		
		assertThrows(
				Dimensionality.class,
	           () -> tensor9.add(tensor11),
	           "Dimension"
	    );
	  
		
		
		
	}
	@Test
	void add2Error() {
		
		assertThrows(
				  Exception.class,
		           () -> vector.add(matrix),
		           ""
		    );
		
	}
}
