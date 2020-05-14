package project;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import core.Tensor;
import dataset.Dataset;
import dataset.MINST;
import errors.CrossEntropy;
import errors.MSE;
import function.activation.Logistic;
import function.activation.ReLU;
import function.activation.Softmax;
import initializer.Random;
import initializer.Zeros;
import layers.FullyConnectedLayer;
import neuralnetwork.NetworkMonitor;
import neuralnetwork.NeuralNetwork;
import neuralnetwork.NetworkTask;
import optimization.SGD;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JSlider;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JLabel;
import javax.swing.border.MatteBorder;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JList;

public class Window {

	private JFrame frmDigitRecognition;
	private NeuralNetwork n;
	private NetworkMonitor monitorCurrent;
	private Tensor inputPaint;
	private Tensor outputPaint;
	private JPanel digit;
	private Thread current;
	JProgressBar progressBar_0;
	JProgressBar progressBar_1;
	JProgressBar progressBar_2;
	JProgressBar progressBar_3;
	JProgressBar progressBar_4;
	JProgressBar progressBar_5;
	JProgressBar progressBar_6;
	JProgressBar progressBar_7;
	JProgressBar progressBar_8;
	JProgressBar progressBar_9;
	JProgressBar progressBar = new JProgressBar();
	JLabel label;
	int total = 0;
	int guessed = 0;
	private JButton btnNewButton;
	private JLabel lblDigitRecognition;
	private JButton btnStep;
	private JButton initialize;
	private JProgressBar progressBar_10;
	private JButton btnStartTraining;
	private JButton btnInterruptTask;
	private JPanel panel_5;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		UIManager.put("ProgressBar.background", Color.BLACK);
		UIManager.put("ProgressBar.foreground", Color.black);
		UIManager.put("ProgressBar.selectionBackground", Color.black);
		UIManager.put("ProgressBar.selectionForeground", Color.black);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frmDigitRecognition.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void test() {
		progressBar_10.setString("Loading minst");
		Dataset d;
		btnStartTraining.setEnabled(false);
		btnStep.setEnabled(false);

		MINST m = new MINST() {
			int totalT = 0;
			int rightGuess = 0;

			@Override
			public void onFinish(Dataset d) {
				progressBar_10.setString(null);
				NetworkTask t = n.test(d);
				t.setMonitor(new NetworkMonitor() {

					@Override
					public void onIteration() {
						int progress = (t.getMonitor().getIteration());
						double total = (double) (t.getMonitor().getTotalBatches());

						inputPaint = t.getMonitor().getInput();
						digit.repaint();
						outputPaint = t.getMonitor().getOutput();

						Tensor desired = t.getMonitor().getDesired();
						double maxProb = -1;
						int pos = -1;
						double desiredProb = -1;
						int posD = -1;
						for (int i = 0; i < outputPaint.getComponents().length; i++) {
							if (outputPaint.get(i, 0).getValue() > maxProb) {
								maxProb = outputPaint.get(i, 0).getValue();
								pos = i;
							}
							if (desired.get(i, 0).getValue() > desiredProb) {
								desiredProb = desired.get(i, 0).getValue();
								posD = i;
							}
						}
						if (pos == posD) {
							rightGuess++;
						}
						totalT++;
						label.setText("Accuracy: " + ((rightGuess / (double) totalT) * 100) + "%");
						updateProbs();
						int value = (int) ((progress / total) * 100d);
						progressBar_10.setValue(value);
					}

					@Override
					public void onFinish() {
						btnStartTraining.setEnabled(true);
						btnStep.setEnabled(true);
						progressBar_10.setValue(100);
						progressBar_10.setString("Test successfully");

					}
				});
				monitorCurrent = t.getMonitor();
				Thread testProcess = new Thread(t);
				current = testProcess;
				testProcess.start();

			}
		};
		m.readData(10000, "datasets/train-images.idx3-ubyte", "datasets/train-labels.idx1-ubyte");

	}

	public void train() {
		progressBar.setString("Loading minst");

		btnStartTraining.setEnabled(false);
		btnStep.setEnabled(false);
		Dataset d;

		MINST m = new MINST() {

			@Override
			public void onFinish(Dataset d) {
				progressBar.setString(null);
				NetworkTask t = n.train(d, 2, 1, new SGD());
				t.setMonitor(new NetworkMonitor() {

					@Override
					public void onIteration() {
						int progress = (t.getMonitor().getEpoch() * t.getMonitor().getTotalBatches()
								+ t.getMonitor().getBatch());
						double total = (double) (t.getMonitor().getTotalEpochs() * t.getMonitor().getTotalBatches());

						int value = (int) ((progress / total) * 100d);
						progressBar.setValue(value);
					}

					@Override
					public void onFinish() {
						btnStartTraining.setEnabled(true);
						btnStep.setEnabled(true);
						progressBar.setValue(100);
						progressBar.setString("Trained successfully");

					}
				});
				Thread trainProcess = new Thread(t);
				current = trainProcess;
				trainProcess.start();

			}
		};
		m.readData(2000, "datasets/train-images.idx3-ubyte", "datasets/train-labels.idx1-ubyte");

	}

	public void updateProbs() {
		progressBar_0.setValue((int) (outputPaint.getComponents()[0].getComponents()[0].getValue() * 100d));
		progressBar_1.setValue((int) (outputPaint.getComponents()[1].getComponents()[0].getValue() * 100d));
		progressBar_2.setValue((int) (outputPaint.getComponents()[2].getComponents()[0].getValue() * 100d));
		progressBar_3.setValue((int) (outputPaint.getComponents()[3].getComponents()[0].getValue() * 100d));
		progressBar_4.setValue((int) (outputPaint.getComponents()[4].getComponents()[0].getValue() * 100d));
		progressBar_5.setValue((int) (outputPaint.getComponents()[5].getComponents()[0].getValue() * 100d));
		progressBar_6.setValue((int) (outputPaint.getComponents()[6].getComponents()[0].getValue() * 100d));
		progressBar_7.setValue((int) (outputPaint.getComponents()[7].getComponents()[0].getValue() * 100d));
		progressBar_8.setValue((int) (outputPaint.getComponents()[8].getComponents()[0].getValue() * 100d));
		progressBar_9.setValue((int) (outputPaint.getComponents()[9].getComponents()[0].getValue() * 100d));
	}

	/**
	 * Create the application.
	 */
	public Window() {
		n = new NeuralNetwork(784, 10, new Random(-1, 1), new Zeros(), new Softmax(), new CrossEntropy());
		n.addLayer(new FullyConnectedLayer(20, new Random(-1, 1), new Zeros(), new ReLU()));
		n.addLayer(new FullyConnectedLayer(20, new Random(-1, 1), new Zeros(), new ReLU()));

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDigitRecognition = new JFrame();
		frmDigitRecognition.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDigitRecognition.setTitle("Digit Recognition");
		frmDigitRecognition.setBounds(100, 100, 1065, 904);
		frmDigitRecognition.getContentPane().setLayout(new BorderLayout(0, 0));
		
		panel_5 = new JPanel();
		frmDigitRecognition.getContentPane().add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel_5.add(panel, BorderLayout.NORTH);

		btnStartTraining = new JButton("STEP 3 - TRAIN NETWORK");
		btnStartTraining.setEnabled(false);
		btnStartTraining.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnStartTraining.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				train();
			}
		});
		panel.setLayout(new GridLayout(0, 1, 10, 10));

		lblDigitRecognition = new JLabel("DIGIT RECOGNITION");
		lblDigitRecognition.setFont(new Font("Source Sans Pro Black", Font.BOLD, 27));
		lblDigitRecognition.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblDigitRecognition);

		btnNewButton = new JButton("STEP 1 - CUSTOMIZE NETWORK");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				NetworkDesigner designer = new NetworkDesigner();
				designer.getFrame().setVisible(true);
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panel.add(btnNewButton);

		initialize = new JButton("STEP 2 - INITIALIZE");
		initialize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnNewButton.setEnabled(false);
				btnStep.setEnabled(true);
				btnStartTraining.setEnabled(true);
				n.initialize();
			}
		});
		initialize.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panel.add(initialize);
		panel.add(btnStartTraining);
		progressBar.setBackground(new Color(0, 128, 0));
		progressBar.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 13));
		progressBar.setForeground(new Color(0, 128, 0));
		progressBar.setOpaque(true);
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(146, 30));

		panel.add(progressBar);

		btnStep = new JButton("STEP 4 - TEST NETWORK");
		btnStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				test();
			}
		});
		btnStep.setEnabled(false);
		btnStep.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panel.add(btnStep);

		progressBar_10 = new JProgressBar();
		progressBar_10.setStringPainted(true);
		progressBar_10.setPreferredSize(new Dimension(146, 30));
		progressBar_10.setOpaque(true);
		progressBar_10.setForeground(new Color(0, 128, 0));
		progressBar_10.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 13));
		progressBar_10.setBackground(new Color(0, 128, 0));
		panel.add(progressBar_10);

		JPanel panel_1 = new JPanel();
		panel_5.add(panel_1, BorderLayout.CENTER);
		panel_1.setBackground(new Color(0, 0, 0));
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.BLACK);
		panel_1.add(panel_2);
		panel_2.setLayout(null);

		digit = new JPanel() {
			public void paint(Graphics g) {
				super.paint(g);
				if (inputPaint != null) {
					BufferedImage bf = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);
					for (int i = 0; i < inputPaint.getComponents().length; i++) {
						double v = inputPaint.getComponents()[i].getComponents()[0].getValue();

						bf.setRGB(i % 28, i / 28, new Color((float) v, (float) v, (float) v).getRGB());
					}
					Image i = bf.getScaledInstance(200, 200, BufferedImage.SCALE_FAST);
					g.drawImage(i, 0, 0, null);
				}

			}
		};

		digit.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GRAY));
		digit.setBackground(Color.BLACK);
		digit.setSize(200, 200);
		digit.setLocation(new Point(30, 39));
		digit.setPreferredSize(new Dimension(200, 200));
		panel_2.add(digit);
		digit.setLayout(null);

		DrawPanel guess = new DrawPanel(n) {

			@Override
			public void onDraw(Tensor input, Tensor output) {
				outputPaint = output;
				inputPaint = input;
				updateProbs();
				digit.repaint();

			}
		};
		guess.setBackground(Color.BLACK);
		guess.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GRAY));
		guess.setLayout(null);
		guess.setPreferredSize(new Dimension(200, 200));
		guess.setLocation(new Point(30, 29));
		guess.setBounds(282, 39, 200, 200);
		panel_2.add(guess);

		label = new JLabel("Accuracy");
		label.setFont(new Font("Source Sans Pro Semibold", Font.PLAIN, 18));
		label.setForeground(Color.WHITE);
		label.setBounds(15, 232, 194, 68);
		panel_2.add(label);

		JButton Clear = new JButton("Clear");
		Clear.setBounds(292, 255, 103, 25);
		panel_2.add(Clear);
		Clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				guess.setPaint();
			}
		});
		Clear.setActionCommand("Test");

		JLabel lblCurrentDigit = new JLabel("Current digit");
		lblCurrentDigit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCurrentDigit.setForeground(Color.WHITE);
		lblCurrentDigit.setBounds(32, 13, 162, 16);
		panel_2.add(lblCurrentDigit);

		JLabel lblTestHandwrittenDigit = new JLabel("Test handwritten digit");
		lblTestHandwrittenDigit.setForeground(Color.WHITE);
		lblTestHandwrittenDigit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTestHandwrittenDigit.setBounds(282, 13, 162, 16);
		panel_2.add(lblTestHandwrittenDigit);

		JPanel panel_3 = new JPanel();
		panel_3.setOpaque(false);
		panel_1.add(panel_3);
		panel_3.setLayout(new GridLayout(10, 1, 0, 0));

		progressBar_0 = new JProgressBar();
		progressBar_0.setBackground(new Color(0, 0, 0));
		progressBar_0.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 13));
		progressBar_0.setForeground(new Color(30, 144, 255));
		progressBar_0.setStringPainted(true);
		panel_3.add(progressBar_0);

		progressBar_1 = new JProgressBar();
		progressBar_1.setBackground(new Color(0, 0, 0));
		progressBar_1.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 13));
		progressBar_1.setForeground(new Color(30, 144, 255));
		progressBar_1.setStringPainted(true);
		panel_3.add(progressBar_1);

		progressBar_2 = new JProgressBar();
		progressBar_2.setBackground(new Color(0, 0, 0));
		progressBar_2.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 13));
		progressBar_2.setForeground(new Color(30, 144, 255));
		progressBar_2.setStringPainted(true);
		panel_3.add(progressBar_2);

		progressBar_3 = new JProgressBar();
		progressBar_3.setBackground(new Color(0, 0, 0));
		progressBar_3.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 13));
		progressBar_3.setForeground(new Color(30, 144, 255));
		progressBar_3.setStringPainted(true);
		panel_3.add(progressBar_3);

		progressBar_4 = new JProgressBar();
		progressBar_4.setBackground(new Color(0, 0, 0));
		progressBar_4.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 13));
		progressBar_4.setForeground(new Color(30, 144, 255));
		progressBar_4.setStringPainted(true);
		panel_3.add(progressBar_4);

		progressBar_5 = new JProgressBar();
		progressBar_5.setBackground(new Color(0, 0, 0));
		progressBar_5.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 13));
		progressBar_5.setForeground(new Color(30, 144, 255));
		progressBar_5.setStringPainted(true);
		panel_3.add(progressBar_5);

		progressBar_6 = new JProgressBar();
		progressBar_6.setBackground(new Color(0, 0, 0));
		progressBar_6.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 13));
		progressBar_6.setForeground(new Color(30, 144, 255));
		progressBar_6.setStringPainted(true);
		panel_3.add(progressBar_6);

		progressBar_7 = new JProgressBar();
		progressBar_7.setBackground(new Color(0, 0, 0));
		progressBar_7.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 13));
		progressBar_7.setForeground(new Color(30, 144, 255));
		progressBar_7.setStringPainted(true);
		panel_3.add(progressBar_7);

		progressBar_8 = new JProgressBar();
		progressBar_8.setBackground(new Color(0, 0, 0));
		progressBar_8.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 13));
		progressBar_8.setForeground(new Color(30, 144, 255));
		progressBar_8.setStringPainted(true);
		panel_3.add(progressBar_8);

		progressBar_9 = new JProgressBar();
		progressBar_9.setBackground(new Color(0, 0, 0));
		progressBar_9.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 13));
		progressBar_9.setForeground(new Color(30, 144, 255));
		progressBar_9.setStringPainted(true);
		panel_3.add(progressBar_9);

		JPanel panel_4 = new JPanel();
		panel_5.add(panel_4, BorderLayout.SOUTH);

		JSlider Speed = new JSlider();
		Speed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (monitorCurrent != null) {
					monitorCurrent.setTimeBetweenIteration(Speed.getValue() * 30);
				}

			}
		});

		JLabel lblNewLabel = new JLabel("Testing speed: ");
		panel_4.add(lblNewLabel);
		panel_4.add(Speed);

		btnInterruptTask = new JButton("Interrupt Task");
		btnInterruptTask.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (current != null) {
					System.out.println("Interrupted");
					current.interrupt();
				}

			}
		});
		panel_4.add(btnInterruptTask);
	}
}
