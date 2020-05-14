package project;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;

public class NetworkSettings extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */


	/**
	 * Create the frame.
	 */
	public NetworkSettings() {
		setAlwaysOnTop(true);
		setTitle("Network settings");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 712, 786);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel contentPane_1 = new JPanel();
		contentPane_1.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.add(contentPane_1, BorderLayout.NORTH);
		contentPane_1.setLayout(new GridLayout(10, 1, 0, 20));
		
		JPanel panel_2 = new JPanel();
		contentPane_1.add(panel_2);
		
		JLabel lblNetworkSettings = new JLabel("Network settings");
		panel_2.add(lblNetworkSettings);
		
		JPanel panel = new JPanel();
		contentPane_1.add(panel);
		
		JLabel lblLearningRateBias = new JLabel("Learning rate bias (/100)");
		panel.add(lblLearningRateBias);
		
		JSlider slider = new JSlider();
		slider.setMinorTickSpacing(10);
		slider.setMajorTickSpacing(10);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		panel.add(slider);
		
		JPanel panel_2_1 = new JPanel();
		contentPane_1.add(panel_2_1);
		
		JLabel lblLearningRateWeights = new JLabel("Learning rate weights (/100)");
		panel_2_1.add(lblLearningRateWeights);
		
		JSlider slider_1 = new JSlider();
		slider_1.setMajorTickSpacing(10);
		slider_1.setPaintLabels(true);
		slider_1.setPaintTicks(true);
		panel_2_1.add(slider_1);
		
		JPanel panel_4 = new JPanel();
		contentPane_1.add(panel_4);
		
		JLabel lblDecay = new JLabel("Decay (/100)");
		panel_4.add(lblDecay);
		
		JSlider slider_2 = new JSlider();
		slider_2.setMajorTickSpacing(10);
		slider_2.setPaintLabels(true);
		slider_2.setPaintTicks(true);
		panel_4.add(slider_2);
		
		JPanel panel_1 = new JPanel();
		contentPane_1.add(panel_1);
		
		JLabel lblOptimizer = new JLabel("Optimizer");
		panel_1.add(lblOptimizer);
		
		JComboBox comboBox_1_2 = new JComboBox();
		comboBox_1_2.setModel(new DefaultComboBoxModel(new String[] {"SGD", "Momentum", "Adagrad"}));
		panel_1.add(comboBox_1_2);
		
		JPanel panel_1_1 = new JPanel();
		contentPane_1.add(panel_1_1);
		
		JLabel lblWeightInitializer = new JLabel("Cost function");
		panel_1_1.add(lblWeightInitializer);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"MSE", "Cross-Entropy"}));
		panel_1_1.add(comboBox_1);
		
		JPanel panel_1_1_1_1 = new JPanel();
		contentPane_1.add(panel_1_1_1_1);
		
		JLabel lblEpochs = new JLabel("Epochs");
		panel_1_1_1_1.add(lblEpochs);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(2, 1, 100, 1));
		panel_1_1_1_1.add(spinner_1);
		
		JPanel panel_1_1_1 = new JPanel();
		contentPane_1.add(panel_1_1_1);
		
		JLabel lblBiasInitializer = new JLabel("Batches");
		panel_1_1_1.add(lblBiasInitializer);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, 60000, 1));
		panel_1_1_1.add(spinner);
		
		JPanel panel_3 = new JPanel();
		contentPane_1.add(panel_3);
		
		JButton btnSaveLayer = new JButton("Save settings");
		panel_3.add(btnSaveLayer);
		
		JButton btnCancel = new JButton("Cancel");
		panel_3.add(btnCancel);
	}

}
