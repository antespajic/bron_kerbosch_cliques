package hr.fer.projekt.clique;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

public class AppGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel jp;
	private JPanel checkPanel;
	
	
	public AppGUI() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setForeground(Color.WHITE);
		initGUI();
	}
	

	private void initGUI() {
		setTitle("Clique Finder");
		jp = new JPanel(new BorderLayout());
		jp.setForeground(Color.WHITE);
		add(jp);
		
		ButtonGroup bg = new ButtonGroup();
		JPanel radioPanel = new JPanel(new GridLayout(1, 0));
		
		for (int i = 3; i <= 8; i++) {
			JRadioButton btn = new JRadioButton(String.valueOf(i));
			btn.setActionCommand(String.valueOf(i));
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					displayMatrix(e.getActionCommand());
				}
			});
			bg.add(btn);
			radioPanel.add(btn);
		}

		jp.add(radioPanel, BorderLayout.NORTH);
	}

	private void displayMatrix(String actionCommand) {
		int size = Integer.parseInt(actionCommand);
		checkPanel = new JPanel(new GridLayout(size, size));
		checkPanel.removeAll();
		jp.remove(checkPanel);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				JCheckBox cb = new JCheckBox();
				checkPanel.add(cb);
			}
		}
		jp.add(checkPanel, BorderLayout.CENTER);
		jp.validate();
		jp.repaint();
		repaint();
	}

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeLater(() -> new AppGUI().setVisible(true));
	}
}
