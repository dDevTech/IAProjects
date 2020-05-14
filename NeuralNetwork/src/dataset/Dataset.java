package dataset;

import java.util.ArrayList;

import core.Tensor;

public class Dataset {
	private ArrayList<DataElement>data;
	public Dataset() {
		data= new ArrayList<DataElement>();
	}
	public ArrayList<DataElement> getData() {
		return data;
	}
	public void setData(ArrayList<DataElement> data) {
		this.data = data;
	}
	
}
