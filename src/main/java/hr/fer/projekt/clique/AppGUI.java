package hr.fer.projekt.clique;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import hr.fer.projekt.clique.output.OutputEnvironment;
import hr.fer.projekt.clique.output.implementation.GUIOutputEnvironment;

public class AppGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private JButton calcButton;
	private JTextField textField; 
	
	private JPanel mainPanel;
	private JPanel centerPane;
	private JPanel topPane;

	private JTextArea outputArea;
	
	private JCheckBox degeneracy;
	private JCheckBox pivot;
	
	private Integer numberOfVertices;
	private List<JCheckBox> grid;
	
	public AppGUI() {

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600, 400);
		setTitle("Clique Finder");
		initGui();
	}

	private void makeGrid() {
		mainPanel.remove(centerPane);
		centerPane = new JPanel();
		centerPane.setLayout(new GridLayout(numberOfVertices, numberOfVertices));
		textField.setText("");
		grid = new ArrayList<>();
		for (int i = 1; i <= numberOfVertices; i++) {
			for (int j = 1; j <= numberOfVertices; j++) {
				JCheckBox cb = new JCheckBox(i + "," + j);
				cb.setName(i + "," + j);
				cb.setEnabled(!(i == j));
				grid.add(cb);
				centerPane.add(cb);
			}
		}
		calcButton = new JButton("Calculate");
		calcButton.addActionListener(e -> performCalculations());
		if (topPane.getComponentCount() == 3) {
			topPane.add(calcButton);
		}

		mainPanel.add(centerPane, BorderLayout.CENTER);
		this.revalidate();
	}

	private void performCalculations() {
		System.out.println("size: " + numberOfVertices  + " gridsize: " + grid.size());
		List<String> edges = grid.stream()
				.filter(cb -> cb.isSelected())
				.map(cb -> cb.getName().replaceAll(",", "-"))
				.collect(Collectors.toList());
		
		UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
		for (int i = 1; i <= numberOfVertices; i++) {
			graph.addVertex(String.valueOf(i));
		}

		for (String string : edges) {
			String[] def = string.split("-");
			graph.addEdge(def[0], def[1]);
		}

		OutputEnvironment<String> outputEnvironment = GUIOutputEnvironment.getInstance(outputArea);
		BronKerbosch<String, DefaultEdge> bronKerbosch = new BronKerbosch<>(graph, degeneracy.isSelected(), pivot.isSelected(), outputEnvironment);

		bronKerbosch.performTraversal();
		outputArea.append("Result: ");
		outputArea.append("\nMaximum cliques: " + outputEnvironment.getMaximumCliques() + " maximal cliques: "
				+ outputEnvironment.getMaximalCliques() + "\n");
	}

	private void initGui() {

		this.setLayout(new BorderLayout());

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		topPane = new JPanel();
		topPane.setLayout(new GridLayout(1, 4));

		textField = new JTextField();
		topPane.add(textField);

		JButton confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(e -> {
			numberOfVertices = null;
			try {
				numberOfVertices = Integer.valueOf(textField.getText());
			} catch (Exception x) {}
			if (numberOfVertices == null || numberOfVertices < 3) {
				JOptionPane.showMessageDialog(this, "Please insert a valid integer greater than 3");
				return;
			}
			makeGrid();
		});

		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(e -> {
			centerPane.removeAll();
			outputArea.setText("");
			this.revalidate();
			this.repaint();
		});
		
		topPane.add(confirmButton);
		topPane.add(clearButton);

		centerPane = new JPanel();
		centerPane.setLayout(new BorderLayout());
		
		JTextArea helloArea = new JTextArea("Insert number of vertices and select confirm, then choose edges in matrix");
		helloArea.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		helloArea.setEditable(false);
		helloArea.setLineWrap(true);
		centerPane.add(helloArea, BorderLayout.CENTER);

		mainPanel.add(topPane, BorderLayout.PAGE_START);
		mainPanel.add(centerPane, BorderLayout.CENTER);

		JPanel botPane = new JPanel(new BorderLayout());

		JPanel choosePane = new JPanel(new GridLayout(1, 2));
		degeneracy = new JCheckBox("Use Degeneracy Ordering");
		pivot = new JCheckBox("Use Pivot environment");
		
		choosePane.add(degeneracy);
		choosePane.add(pivot);
		
		outputArea = new JTextArea("");
		outputArea.setEditable(false);
		
		botPane.add(choosePane, BorderLayout.NORTH);
		botPane.add(outputArea, BorderLayout.CENTER);
		
		mainPanel.add(botPane, BorderLayout.SOUTH);

		this.add(mainPanel);
	}


	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeLater(() -> new AppGUI().setVisible(true));
	}
}
