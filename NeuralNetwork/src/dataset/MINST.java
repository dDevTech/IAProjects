package dataset;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import core.Tensor;
import initializer.Zeros;
import utils.ConsoleUtils;

public abstract class MINST extends Loader {
	Dataset dataset;

	public void readData(int numberOfData, String dataFilePath, String labelFilePath){
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("Loading MNINST!");
					DataInputStream dataInputStream = new DataInputStream(
							new BufferedInputStream(new FileInputStream(dataFilePath)));
					int magicNumber = dataInputStream.readInt();
					int numberOfItems = dataInputStream.readInt();
					int nRows = dataInputStream.readInt();
					int nCols = dataInputStream.readInt();

					DataInputStream labelInputStream = new DataInputStream(
							new BufferedInputStream(new FileInputStream(labelFilePath)));
					int labelMagicNumber = labelInputStream.readInt();
					int numberOfLabels = labelInputStream.readInt();

					dataset = new Dataset();

					assert numberOfItems == numberOfLabels;
					int total = numberOfData;
					for (int i = 0; i < total; i++) {
						if (total / 1000 != 0) {
							if ((i + 1) % (total / 1000) == 0) {
								ConsoleUtils.progressPercentage(i + 1, total);
							}
						}

						DataElement data = new DataElement();
						double[] labels = new double[10];
						int num = labelInputStream.readUnsignedByte();
						for (int j = 0; j < labels.length; j++) {
							if (num == j) {
								labels[j] = 1;
							} else {
								labels[j] = 0;
							}
						}
						data.setDesired(new Tensor(labels).reshape(10, 1));

						Tensor[] components = new Tensor[nRows * nCols];
						int k = 0;
						for (int r = 0; r < nRows; r++) {
							for (int c = 0; c < nCols; c++) {
								Tensor t = new Tensor();
								double value = dataInputStream.readUnsignedByte() / 255d;

								Tensor tens = new Tensor(value);

								t.setComponents(new Tensor[] { tens });
								components[k] = t;
								k++;
							}
						}

						data.setInput(new Tensor(components));

						dataset.getData().add(data);
					}
					dataInputStream.close();
					labelInputStream.close();
					System.out.println("MNINST loaded successfully!");
					onFinish(dataset);
				} catch (IOException e) {
					System.out.println("Error reading file");
				}
			}
		});
		t.start();

	}

	
}
