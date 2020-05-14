package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import function.Function;
import initializer.Initializer;
import initializer.Random;
import initializer.Zeros;

public class Tensor {
	protected Tensor[] components;
	protected boolean storeValue = false;
	private double value = 0;
	private Tensor shape = null;

	/**
	 * Exclusively for shapes
	 * 
	 * @param shape
	 */
	private Tensor(double[] shape) {
		arrayToTensor(this, shape);

	}

	/**
	 * Create a tensor with an array object
	 */
	public Tensor(Object array) {
		arrayToTensor(this, array);

	}

	/**
	 * Create a tensor with a custom shape and a initializer to initialize it
	 * 
	 * @param shape       the shape
	 * @param initializer initializer
	 */
	public Tensor(Initializer initializer, int... shape) {
		generateTensor(0, shape, initializer);

	}

	/**
	 * Set a new shape to the tensor and set every value with a initializer of the
	 * tensor
	 * 
	 * @param shape       shape
	 * @param initializer initialize algorithm
	 */
	public void generateTensor(int k, int[] shape, Initializer initializer) {

		if (k == shape.length) {
			value = initializer.initialize();
			storeValue = true;
		} else {
			int s = shape[k];
			components = new Tensor[s];
			k++;
			for (int i = 0; i < s; i++) {
				components[i] = new Tensor();
				components[i].generateTensor(k, shape, initializer);
			}
		}
	}

	public void generateTensor(int[] shape, Initializer initializer) {

		if (shape.length == 0) {
			value = initializer.initialize();
			storeValue = true;
		} else {

			components = new Tensor[shape[0]];
			for (int i = 0; i < shape[0]; i++) {

				components[i] = new Tensor();
				components[i].generateTensor(Arrays.copyOfRange(shape, 1, shape.length), initializer);

			}
		}
	}

	/**
	 * Create a tensor with some tensor components
	 * 
	 * @param components subtensors
	 */
	public Tensor(Tensor[] components) {
		this.setComponents(components);

	}

	/**
	 * Create a tensor with rank 0
	 * 
	 * @param value the real value
	 */
	public Tensor(double value) {
		components = new Tensor[0];
		storeValue = true;
		this.value = value;

	}

	/**
	 * Create a copy of a tensor
	 * 
	 * @param tensor the tensor to clone
	 */
	public Tensor(Tensor tensor) {
		Tensor copy = tensor.copy();
		components = copy.components;
		storeValue = copy.storeValue;
		value = copy.value;

	}

	/**
	 * Create an empty tensor
	 */
	public Tensor() {
		components = new Tensor[0];

	}

	/**
	 * Copy an instance of tensor
	 * 
	 * @return the cloned tensor
	 */
	public Tensor copy() {
		Tensor copy = new Tensor();
		if (components != null) {
			copy.components = new Tensor[components.length];
			for (int i = 0; i < components.length; i++) {
				copy.components[i] = components[i].copy();
			}
		}
		copy.value = value;
		copy.storeValue = storeValue;

		return copy;
	}

	private void arrayToTensor(Tensor context, Object array) {
		try {
			int length = Array.getLength(array);
			context.components = new Tensor[length];
			if (Array.getLength(array) == 0) {
				return;
			}
			if (Array.get(array, 0).getClass().isArray()) {
				for (int i = 0; i < length; i++) {
					Tensor t = new Tensor();

					context.components[i] = t;

					arrayToTensor(t, Array.get(array, i));
				}
			} else {

				for (int i = 0; i < length; i++) {
					Tensor t = new Tensor((double) (Array.get(array, i)));
					context.components[i] = t;

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);

		}
	}

	public void set(double value, int... positions) {
		if (rank() == positions.length) {
			setRecursive(value, positions);

			return;
		}
		try {
			throw new Exception(
					"The number of positions doesn't match with the rank of the tensor RANK: " + shape().rank());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean setRecursive(double value, int... positions) {
		if (!storeValue) {
			return components[positions[0]].setRecursive(value, Arrays.copyOfRange(positions, 1, positions.length));
		} else if (positions.length == 0) {
			this.value = value;
			this.storeValue = true;
			return true;
		}
		return false;

	}

	public void set(Tensor tensor, int... positions) {

		if (rank() >= positions.length) {
			setRecursive(tensor, positions);
			return;
		}
		try {
			throw new Exception(
					"The number of positions doesn't match with the rank of the tensor RANK: " + shape().rank());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean setRecursive(Tensor tensor, int... positions) {

		if (positions.length > 1) {
			return components[positions[0]].setRecursive(tensor, Arrays.copyOfRange(positions, 1, positions.length));
		} else {
			components[positions[0]] = tensor;
		}
		shape = null;
		return false;

	}

	/**
	 * Return the tensor associated to a position. The tensor returned is a
	 * reference of the current tensor. Be careful if you want to use it create a
	 * copy of it Example: [[3,2][5,2],[3,1]] get(0) -> [3,2] get(2) -> [3,1]
	 * get(1,1) -> 2
	 * 
	 * @param positions Ordered positions of the tensor
	 * @return the tensor associated to that position.
	 */
	public Tensor get(int... positions) {
		if (rank() >= positions.length) {
			return getRecursive(0, positions);
		}
		throw new Dimensionality(
				"The number of positions doesn't match with the rank of the tensor RANK: " + shape().rank());

	}

	private Tensor getRecursive(int k, int... positions) {
		if (positions.length - k != 0) {
			return components[positions[k]].getRecursive(k + 1, positions);
		} else {

			return this;
		}

	}

	/**
	 * Removed unused dimensions of the tensor [[0.1]] - > 0.1 [[[2.3]],[[3]]] ->
	 * [[2.3],[3]]
	 */
	public void squeeze() {
		squeezeRecursive();

	}

	private void squeezeRecursive() {

		if (components.length == 1) {
			if (components[0].storeValue) {
				value = components[0].value;
				storeValue = true;
			} else {
				components = components[0].components;
				squeezeRecursive();
			}

		} else {
			for (int i = 0; i < components.length; i++) {
				components[i].squeezeRecursive();
			}
		}
		shape = null;

	}

	/**
	 * Sum to tensors. The sum will be stored in the tensor who call the function.
	 * If you don't want to replace it create a copy of the tensor before PRE: Both
	 * tensors must have the same shape
	 * 
	 * @param tensor Tensor to sum
	 */
	public void add(Tensor tensor) {
		if (storeValue && tensor.storeValue) {
			value = tensor.value + value;
		} else {
			if (tensor.shape().equals(this.shape())) {
				for (int i = 0; i < components.length; i++) {
					components[i].add(tensor.components[i]);
				}
			} else {
				throw new Dimensionality("Dimensions don't match");

			}
		}

	}

	/**
	 * Substract to tensors. The substraction will be stored in the tensor who call
	 * the function. If you don't want to replace it create a copy of the tensor
	 * before PRE: Both tensors must have the same shape
	 * 
	 * @param tensor Tensor to subtract
	 */
	public void sub(Tensor tensor) {
		try {
			if (storeValue) {

				value = value - tensor.value;
			} else {
				if (tensor.shape().equals(this.shape())) {
					for (int i = 0; i < components.length; i++) {
						components[i].sub(tensor.components[i]);
					}
				} else {
					throw new Dimensionality("Dimensions don't match");

				}
			}
		} catch (Dimensionality e) {
			e.printStackTrace();
		}

	}

	/**
	 * Known as hadamard product. Multiply to tensors each component by each
	 * component. The multiplication will be stored in the tensor who call the
	 * function. If you don't want to replace it create a copy of the tensor before
	 * PRE: Both tensors must have the same shape
	 * 
	 * @param tensor Tensor to use hadamard product
	 */
	public void mult(Tensor tensor) {
		try {
			if (storeValue) {

				value *= tensor.value;
			} else {
				if (tensor.shape().equals(this.shape())) {
					for (int i = 0; i < components.length; i++) {
						components[i].mult(tensor.components[i]);
					}
				} else {
					throw new Dimensionality("Dimensions don't match");

				}
			}
		} catch (Dimensionality e) {
			e.printStackTrace();
		}

	}

	/**
	 * Multiply a vector by a number
	 * 
	 * @param scalar the scalar to multiply every component
	 */
	public void mult(double scalar) {

		if (storeValue) {

			value = scalar * value;
		} else {

			for (int i = 0; i < components.length; i++) {
				components[i].mult(scalar);
			}

		}

	}

	/**
	 * Divide one tensor to another. The division will be stored in the tensor who
	 * call the function. If you don't want to replace it create a copy of the
	 * tensor before PRE: Both tensors must have the same shape
	 * 
	 * @param tensor Divisor tensor
	 */
	public void div(Tensor tensor) {
		try {
			if (storeValue) {
				if (tensor.value != 0) {
					value /= tensor.value;
				} else {
					throw new ArithmeticException("A divisor is 0. You cannot divide by zero");
				}

			} else {
				if (tensor.shape().equals(this.shape())) {
					for (int i = 0; i < components.length; i++) {
						components[i].div(tensor.components[i]);
					}
				} else {
					throw new Dimensionality("Dimensions don't match");

				}
			}
		} catch (Dimensionality e) {
			e.printStackTrace();
		}

	}

	/**
	 * Tensorial product
	 * 
	 * @param tensor tensor to do tensorial product
	 * @return the resulted tensor
	 */
	public Tensor tensorProduct(Tensor tensor) {
		if (storeValue) {
			throw new Dimensionality("Rank must be more than zero");
		}
		Tensor out = tensorProductAlg(null, 0, this.copy(), tensor);

		return out;
	}

	private Tensor tensorProductAlg(Tensor previous, int pos, Tensor newTensor, Tensor tensor) {

		if (storeValue) {

			previous.components[pos] = tensor.copy();
			previous.components[pos].mult(value);

		} else {
			for (int i = 0; i < components.length; i++) {
				newTensor.components[i].tensorProductAlg(newTensor, i, newTensor.components[i], tensor);
			}
		}
		shape = null;
		return newTensor;

	}
	// To do restore shape only to services and constructors that modifiy it

	/**
	 * Typical tensor product. If they vectors remember to uprank() to matrix vector
	 * it to 2 dimensions
	 * 
	 * @param tensor tensor to do dot
	 * @return the dot product with both tensors
	 */
	public Tensor dot(Tensor tensor) {
		return dot(tensor, 1, 2);
	}

	/**
	 * Dot product to any dimension. It will do the dot product in the dimensions
	 * you specify
	 * 
	 * @param tensor     tensor to do dot product
	 * @param dimension1 dim1
	 * @param dimension2 dim2
	 * @return the result tensor of dot product
	 */
	public Tensor dot(Tensor tensor, int dimension1, int dimension2) {
		if (rank() < 2 || tensor.rank() < 2) {
			throw new Dimensionality("Tensor must have a rank greater or equal than 2. if not uprank() them");
		}
		dimension1 -= 1;
		dimension2 -= 1;
		if (rank() != tensor.rank())
			throw new Dimensionality("Tensors must have the same rank");
		int[] desiredShape = new int[shape().components.length];

		if (shape().components[dimension2].value == tensor.shape().components[dimension1].value) {
			for (int i = 0; i < shape().components.length; i++) {
				if (i != dimension1 && i != dimension2) {
					if (shape().components[i].value != tensor.shape().components[i].value) {
						throw new Dimensionality("Shapes that are not the orientations must have the same dimension");
					}
					desiredShape[i] = (int) shape().components[i].value;

				} else {
					if (i == dimension2) {
						desiredShape[i] = (int) tensor.shape().components[dimension2].value;
					}
					if (i == dimension1) {
						desiredShape[i] = (int) shape().components[dimension1].value;
					}
				}
			}
			if (rank() == 2) {
				return dotMatrix(tensor, (int) shape().components[dimension2].value,
						new Tensor(new Zeros(), desiredShape), dimension1, dimension2, new int[2]);
			}
			return dotAlg(tensor, (int) shape().components[dimension2].value, 0, new Tensor(new Zeros(), desiredShape),
					0, new int[shape().components.length], dimension1, dimension2);

		} else {
			throw new Dimensionality(
					"Shape in position orientation2 must be the same as the parameter tensor shape in position orientation1");
		}

	}

	private Tensor dotAlg(Tensor tensor, int sizeProductVector, int iteratedPos, Tensor output, int loop,
			int[] positions, int dimension1, int dimension2) {
		if (iteratedPos == dimension1 || iteratedPos == dimension2) {
			if (iteratedPos < output.rank() - 1) {
				dotAlg(tensor, sizeProductVector, iteratedPos + 1, output, loop, positions, dimension1, dimension2);
			}

			return output;
		}

		int thisLoop = loop;

		int size = (int) output.shape().components[iteratedPos].value;

		for (int i = 0; i < size; i++) {

			positions[iteratedPos] = i;

			if (thisLoop == shape().components.length - 3) {
				dotMatrix(tensor, sizeProductVector, output, dimension1, dimension2, positions);

			} else {
				dotAlg(tensor, sizeProductVector, iteratedPos + 1, output, loop + 1, positions, dimension1, dimension2);
			}

		}
		positions[loop] = 0;
		return output;
	}

	private Tensor dotMatrix(Tensor tensor, int sizeProductVector, Tensor output, int dimension1, int dimension2,
			int[] positions) {
		// Matrix multiplication simple
		for (int j = 0; j < (int) output.shape().components[dimension1].value; j++) {

			for (int k = 0; k < (int) output.shape().components[dimension2].value; k++) {
				float sum = 0;
				for (int l = 0; l < sizeProductVector; l++) {
					positions[dimension1] = l;
					positions[dimension2] = k;
					double v1 = tensor.get(positions).value;
					positions[dimension1] = j;
					positions[dimension2] = l;
					double v2 = get(positions).value;
					sum += v1 * v2;
				}
				positions[dimension1] = j;
				positions[dimension2] = k;
				output.set(sum, positions);

			}
		}
		return output;
	}

	/**
	 * Split a tensor in the first dimension PRE: Sum of the splits must be the same
	 * as the first dimension shape EXAMPLE: [[4,1][3,2][5,2]] split(2,1)->
	 * [[4,1],[3,2]] & [[5,2]]
	 * 
	 * @param splits an array with the size of every split
	 * @return an array with the splited tensors
	 */
	public Tensor[] split(int... splits) {
		if (rank() == 0)
			throw new Dimensionality("Rank must be more or equal than 1");
		int sum = 0;
		for (int s : splits) {
			sum += s;
		}

		if (shape().components[0].value == sum) {
			Tensor[] tensors = new Tensor[splits.length];
			int c = 0;
			for (int j = 0; j < splits.length; j++) {
				Tensor[] subtensors = new Tensor[splits[j]];

				for (int k = 0; k < splits[j]; k++) {
					subtensors[k] = get(c).copy();
					c++;
				}
				tensors[j] = new Tensor(subtensors);
			}
			return tensors;

		}
		throw new Dimensionality("The size of the shape in dim 1 must be the same as the sum of the splits");
	}

	/**
	 * Creates a new tensor with a specified shape PRE the multiplication of the new
	 * shape dimensions must be the same as the old shape
	 * 
	 * @param newShape the shape to organize the tensor
	 * @return a new tensor with the new shape
	 */
	public Tensor reshape(int... newShape) {
		int currentShape = 1;
		for (int dimension : newShape) {
			currentShape *= dimension;
		}
		int desiredShape = 1;
		for (Tensor tensor : shape().components) {
			desiredShape *= tensor.value;
		}
		if (currentShape == desiredShape) {
			int[] positionsNewShape = new int[newShape.length];
			return reshapeRecursive(flatten(), 0, newShape, positionsNewShape, new Counter());

		}
		throw new Dimensionality("Shape mismatch");

	}

	/**
	 * Convert any tensor to a vector
	 * 
	 * @return the flattened vector
	 */
	public Tensor flatten() {
		int[] positionsTensor = new int[shape().components.length];
		ArrayList<Tensor> vector = flattenRecursive(0, positionsTensor);
		Tensor[] comps = new Tensor[vector.size()];
		for (int i = 0; i < vector.size(); i++) {
			comps[i] = vector.get(i);
		}
		return new Tensor(comps);
	}

	private ArrayList<Tensor> flattenRecursive(int loop, int[] positions) {
		int thisLoop = loop;
		int size = (int) shape().components[loop].value;
		ArrayList<Tensor> flattenTensors = new ArrayList<Tensor>();
		for (int i = 0; i < size; i++) {
			positions[loop] = i;
			if (thisLoop == shape().components.length - 1) {
				flattenTensors.add(new Tensor(get(positions).copy()));
				// System.out.println(Arrays.toString(positions));

			} else {
				flattenTensors.addAll(flattenRecursive(loop + 1, positions));
			}
		}

		positions[loop] = 0;
		return flattenTensors;
	}

	private Tensor reshapeRecursive(Tensor vectorValues, int loop, int[] newShape, int[] positions, Counter counter) {

		int thisLoop = loop;
		Tensor[] components = new Tensor[newShape[thisLoop]];

		for (int i = 0; i < newShape[thisLoop]; i++) {
			positions[loop] = i;
			if (thisLoop == newShape.length - 1) {
				components[i] = new Tensor(vectorValues.components[counter.getCounter()].copy());
				counter.increment();
			} else {
				components[i] = reshapeRecursive(vectorValues, loop + 1, newShape, positions, counter);
			}
		}

		positions[loop] = 0;
		return new Tensor(components);
	}

	/**
	 * Concatenates two tensors PRE: They must have the same shape in every
	 * dimension except the first.
	 * 
	 * @param tensor tensor to join with
	 * @return the tensor concatenation
	 */
	public Tensor join(Tensor tensor) {
		boolean validOperation = true;
		if (rank() == tensor.rank()) {

			for (int i = 1; i < tensor.shape().components.length && validOperation; i++) {
				if (tensor.shape().components[i].value != shape().components[i].value)
					validOperation = false;
			}
		} else {
			validOperation = false;
		}
		if (validOperation) {
			Tensor t = new Tensor(this);
			Tensor newComps[] = new Tensor[components.length + tensor.components.length];
			for (int i = 0; i < components.length; i++) {
				newComps[i] = components[i].copy();
			}
			for (int i = 0; i < tensor.components.length; i++) {
				newComps[i + components.length] = tensor.components[i].copy();
			}
			t.setComponents(newComps);

			return t;
		} else {
			throw new Dimensionality("Every dimension except the first must have the same shape");
		}
	}

	/**
	 * Return the transposed matrix with the order specified PRE: dimensionSwaps
	 * length must be the same as the rank of the tensor
	 * 
	 * In matrix to transpose it parameter will be 2,1 The default order in a 3D
	 * matrix is 123. To swap columns 1 and 2 the parameter will be 2,1,3.
	 * 
	 * The array must have different integers. It can't be 112 or 223
	 * 
	 * @param dimensionSwaps swap order
	 * @return the transposed tensor
	 */
	public Tensor getTranspose(int... dimensionSwaps) {
		if (dimensionSwaps.length != rank())
			throw new Dimensionality("Size of parameter must be the same as rank");
		int[] shape = new int[dimensionSwaps.length];
		for (int i = 0; i < shape.length; i++) {
			shape[i] = (int) shape().components[dimensionSwaps[i] - 1].value;
		}
		Tensor t = new Tensor(new Zeros(), shape);
		return transposeRecursive(t, 0, new int[shape().components.length], dimensionSwaps);
	}

	/**
	 * Regular transpose operation. If the rank of the tensor is two it will matrix
	 * transpose
	 * 
	 * @return transposed matrix
	 */
	public Tensor getTranspose() {
		if (rank() >= 2) {
			int[] positions = new int[rank()];
			positions[0] = 2;
			positions[1] = 1;
			for (int i = 2; i < rank(); i++) {
				positions[i] = i + 1;
			}
			return getTranspose(positions);
		} else {
			throw new Dimensionality("Must be at least rank 2");
		}

	}

	private Tensor transposeRecursive(Tensor transposed, int loop, int[] positions, int... swapOrder) {
		int thisLoop = loop;
		int size = (int) shape().components[loop].value;
		for (int i = 0; i < size; i++) {
			positions[loop] = i;
			if (thisLoop == shape().components.length - 1) {
				transposed.set(new Tensor(get(positions).value), swapPositions(positions, swapOrder));
			} else {
				transposeRecursive(transposed, loop + 1, positions, swapOrder);
			}
		}
		positions[loop] = 0;
		return transposed;
	}

	private int[] swapPositions(int[] positions, int... swapOrder) {
		int[] swaped = new int[positions.length];
		for (int i = 0; i < positions.length; i++) {
			swaped[i] = positions[swapOrder[i] - 1];
		}
		return swaped;
	}

	/**
	 * Apply a certain function to every component in tensor
	 * 
	 * @param function
	 * @return the tensor with the function applied
	 */
	public Tensor applyFunction(Function function) {
		Tensor t;
		if (storeValue) {
			t = new Tensor(function.calculate(value));
		} else {
			Tensor[] newComps = new Tensor[components.length];
			for (int i = 0; i < components.length; i++) {
				newComps[i] = components[i].applyFunction(function);
			}
			t = new Tensor(newComps);
		}
		return t;
	}

	/**
	 * Increase a new dimension of size one to the tensor
	 * 
	 * @return the increased dimension tensor
	 */
	public Tensor uprank() {
		Tensor copy = this.copy();
		return new Tensor(new Tensor[] { copy });
	}

	/**
	 * Return the sum of every component of the tensor
	 * 
	 * @return the sum
	 */
	public double sumUp() {
		if (storeValue) {
			return value;
		} else {
			double sum = 0;
			for (int i = 0; i < components.length; i++) {
				sum += components[i].sumUp();
			}
			return sum;
		}
	}

	/**
	 * Multiplies sum all the square of each component
	 * 
	 * @return the result
	 */
	public double squareSum() {
		if (storeValue) {
			return value;
		} else {
			double total = 0;
			for (int i = 0; i < components.length; i++) {
				double v = components[i].squareSum();
				total += v * v;
			}
			return total;
		}
	}

	public int calcRank() {
		if (storeValue) {
			return 0;
		} else {
			int rank = 0;
			for (int i = 0; i < components.length && rank == 0; i++) {
				rank = components[i].calcRank();

			}
			return rank + 1;
		}

	}

	private double[] calcShape() {
		if (storeValue) {
			return new double[] {};
		}
		if (components.length == 0) {
			return new double[] { 0 };
		}

		return (double[]) (concatenate(new double[] { (double) (components.length) }, components[0].calcShape()));

	}

	/**
	 * Service that concatenates two arrays of doubles
	 * 
	 * @param a first array
	 * @param b second array
	 * @return array concatenated
	 */
	private double[] concatenate(double[] a, double[] b) {
		double[] c = new double[a.length + b.length];
		for (int i = 0; i < a.length; i++) {
			c[i] = a[i];
		}
		for (int i = 0; i < b.length; i++) {
			c[i + a.length] = b[i];
		}
		return c;
	}

	private static int roundDecimalsPrint = 4;

	private static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	/**
	 * Only used for developers. Not use by default
	 */
	protected void calculateShapeOfEveryTensor() {
		shape = new Tensor(calcShape());
		calculateShapeOfEverySubTensor();
	}

	protected void calculateShapeOfEverySubTensor() {
		for (int i = 0; i < components.length; i++) {
			components[i].shape = new Tensor(components[i].calcShape());
			components[i].calculateShapeOfEverySubTensor();
		}
	}

	/**
	 * Set the components of the current tensor. Remember to use method
	 * calcluateRankShapeOfEveryTensor to update
	 * 
	 * @param components array of tensor will be the components
	 */
	public void setComponents(Tensor[] components) {
		this.components = components;
	}

	/**
	 * Print a tensor in the console
	 */
	public void print() {

		System.out.println(this);
		System.out.println();

	}

	/**
	 * Check if two tensors are equal. They will be equal if the have the same
	 * values in the same positions of the tensor
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tensor) {
			Tensor tensor = (Tensor) obj;
			if (tensor.components == null || this.components == null) {
				return tensor.value == this.value && this.storeValue && tensor.storeValue;
			}
			if (tensor.components.length == this.components.length) {
				for (int i = 0; i < tensor.components.length; i++) {
					if (!tensor.components[i].equals(this.components[i])) {
						return false;
					}
				}
				return true;
			}
			return false;
		}
		return false;

	}

	private String string(boolean last) {
		String s = "";
		if (storeValue) {
			if (last) {
				s += Double.toString(Tensor.round(value, getRoundDecimalsPrint()));
			} else {
				s += Double.toString(Tensor.round(value, getRoundDecimalsPrint())) + ", ";
			}

		} else {
			s += ("[");
			for (int i = 0; i < components.length; i++) {
				s += components[i].string(i == components.length - 1);
			}
			s += ("]");
		}
		return s;
	}

	/**
	 * Convert the tensor to a string similar to JSON
	 */
	@Override
	public String toString() {
		if (storeValue) {
			return string(true);
		} else {
			return string(false);
		}

	}

	/**
	 * Return the value if there is a value inside the tensor (must be rank 0)
	 * 
	 * @return the value
	 */
	public double getValue() {
		if (storeValue) {
			return value;
		}
		throw new Dimensionality("The tensor doens't contain a value");

	}

	/**
	 * Return the rank (number of dimensions) of the tensor
	 * 
	 * @return rank int
	 */
	public int rank() {
		if (shape == null) {
			shape = new Tensor(calcShape());
		}
		return shape.components.length;

	}

	/**
	 * Return a tensor with the shape. Its length will be the rank
	 * 
	 * @return the tensor with the shape of every dimension
	 */
	public Tensor shape() {
		if (shape == null) {
			shape = new Tensor(calcShape());
		}
		return shape;

	}

	/**
	 * Return the components of the current tensor
	 * 
	 * @return array of tensor that are the components
	 */
	public Tensor[] getComponents() {
		return components;
	}

	public boolean isVector() {
		return rank() == 1;
	}

	public boolean isMatrix() {
		return rank() == 2;
	}

	public boolean is3DMatrix() {
		return rank() == 3;
	}

	/**
	 * If tensors has n dimensions
	 * 
	 * @param n
	 * @return
	 */
	public boolean isNDimension(int n) {
		return rank() == n;
	}

	/**
	 * If the tensor is a real number
	 * 
	 * @return
	 */
	public boolean isNumber() {
		return rank() == 0;
	}

	/**
	 * If the tensor is empty and doesn't contain any value or subtensors
	 * 
	 * @return empty or not
	 */
	public boolean isEmpty() {
		return components.length == 0 && !storeValue;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("storeValue", storeValue);
		obj.put("value", value);
		JSONArray array = new JSONArray();
		for (int i = 0; i < components.length; i++) {
			array.add(components[i].toJSON());
		}
		obj.put("subtensors", array);
		return obj;
	}

	/**
	 * Safe the tensor in a specified file in json format
	 * 
	 * @param f File to store the tensor RECOMMENDED .json
	 * @return if the operation was successful
	 */
	public boolean saveTensor(File f) {
		JSONObject obj = toJSON();
		StringWriter out = new StringWriter();
		try {
			obj.writeJSONString(out);
			String jsonText = out.toString();

			f.createNewFile();
			FileWriter fw = new FileWriter(f);
			fw.write(jsonText);
			fw.close();
			return true;
		} catch (IOException e) {

			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes" })
	protected static Tensor jsonToTensor(JSONObject tensor) {
		ArrayList<Tensor> tensorsArray = new ArrayList<Tensor>();
		Tensor t = new Tensor();

		Object o = tensor.get("storeValue");
		if (o != null) {
			t.storeValue = (boolean) o;
		}
		Object o2 = tensor.get("value");
		if (o2 != null) {
			t.value = (double) o2;
		}
		Iterator itr1 = ((JSONArray) tensor.get("subtensors")).iterator();
		while (itr1.hasNext()) {
			JSONObject object = (JSONObject) itr1.next();
			tensorsArray.add(jsonToTensor(object));
		}
		Tensor[] tensors = new Tensor[tensorsArray.size()];
		for (int i = 0; i < tensorsArray.size(); i++) {
			tensors[i] = tensorsArray.get(i);
		}
		t.setComponents(tensors);
		return t;

	}

	/**
	 * Load a tensor from a file in a json format
	 * 
	 * @param f The file to load the tensor
	 * @return the tensor
	 */
	public static Tensor loadTensor(File f) {
		try {
			Object obj = new JSONParser().parse(new FileReader(f));
			JSONObject tensor = (JSONObject) obj;
			Tensor t = jsonToTensor(tensor);

			return t;
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return null;
	}

	public static int getRoundDecimalsPrint() {
		return roundDecimalsPrint;
	}

	public static void setRoundDecimalsPrint(int roundDecimalsPrint) {
		Tensor.roundDecimalsPrint = roundDecimalsPrint;
	}

}
