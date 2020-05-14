package project;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import layers.Layer;

import javax.swing.JPanel;
import javax.swing.DropMode;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NetworkDesigner {

	private JFrame frmNetworkArchitecture;
	private JTable table;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public NetworkDesigner() {
		initialize();
	}
	DefaultTableModel model;
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setFrame(new JFrame());
		getFrame().setBounds(100, 100, 740, 526);
		getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane();
		getFrame().getContentPane().add(scrollPane, BorderLayout.NORTH);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		model=new DefaultTableModel(
				new Object[][] {
					
				},
				new String[] {
					"Layer Type", "Activation", "Neurons", "Init weights","Init bias"
				}
			);
		table.setModel(model);
		
		 
		addLayer( 784, Type.InputLayer, Activation.Non);
		addLayer( 20, Type.FullyConnected, Activation.ReLU);
		addLayer( 10, Type.OutputLayer, Activation.Softmax);
		JPanel panel = new JPanel();
		getFrame().getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton create = new JButton("Create layer");
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LayerDesigner designer= new LayerDesigner();
				designer.setVisible(true);
			}
		});
		panel.add(create);
		
		JButton save = new JButton("Save network");
		save.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		panel.add(save);
		
		JButton remove = new JButton("Remove Layer\r\n");
		panel.add(remove);
		
		JButton settings = new JButton("Network settings");
		settings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				NetworkSettings settings = new NetworkSettings();
				settings.setVisible(true);
			}
		});
		panel.add(settings);
	}
	int id=0;
	public enum Type{FullyConnected,InputLayer,OutputLayer};
	public enum Activation{Logistic,Softmax,ReLU,LeakyRelu,Tanh,Non};
	public void addLayer(int neurons,Type layerType,Activation act) {
		model.addRow(new Object[] {layerType,act,neurons});
		id++;
	}

	public JFrame getFrame() {
		return frmNetworkArchitecture;
	}

	public void setFrame(JFrame frame) {
		this.frmNetworkArchitecture = frame;
		frmNetworkArchitecture.setResizable(false);
		frmNetworkArchitecture.setTitle("Network architecture");
		frame.setAlwaysOnTop(true);
	}

}
