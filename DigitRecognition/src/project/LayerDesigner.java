package project;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

public class LayerDesigner extends JFrame {

	private JPanel contentPane;



	/**
	 * Create the frame.
	 */
	public LayerDesigner() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Create a fully connected layer");
		setResizable(false);
		setAlwaysOnTop(true);
		setBounds(100, 100, 507, 483);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(8, 1, 0, 20));
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);
		
		JLabel lblFullyConnectedLayer = new JLabel("Fully Connected Layer");
		panel_2.add(lblFullyConnectedLayer);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		
		JLabel lblNewLabel = new JLabel("Neurons:     ");
		panel.add(lblNewLabel);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(20, 1, 2000, 1));
		panel.add(spinner);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		
		JLabel lblActivationLayer = new JLabel("Activation Layer: ");
		panel_1.add(lblActivationLayer);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Logistic", "ReLU", "LeakyReLU", "Tanh", "Softmax"}));
		panel_1.add(comboBox);
		
		JPanel panel_1_1 = new JPanel();
		contentPane.add(panel_1_1);
		
		JLabel lblWeightInitializer = new JLabel("Weight Initializer");
		panel_1_1.add(lblWeightInitializer);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"Random [-1,1]", "Zeros", "He_Initializer"}));
		panel_1_1.add(comboBox_1);
		
		JPanel panel_1_1_1 = new JPanel();
		contentPane.add(panel_1_1_1);
		
		JLabel lblBiasInitializer = new JLabel("Bias Initializer");
		panel_1_1_1.add(lblBiasInitializer);
		
		JComboBox comboBox_1_1 = new JComboBox();
		comboBox_1_1.setModel(new DefaultComboBoxModel(new String[] {"Random [-1,1]", "Zeros", "He_Initializer"}));
		panel_1_1_1.add(comboBox_1_1);
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3);
		
		JButton btnSaveLayer = new JButton("Save layer");
		panel_3.add(btnSaveLayer);
		
		JButton btnCancel = new JButton("Cancel");
		panel_3.add(btnCancel);
	}

}
