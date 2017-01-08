package hr.fer.projekt.clique;

import hr.fer.projekt.clique.algorithm.BronKerbosch;
import hr.fer.projekt.clique.input.GraphLoader;
import hr.fer.projekt.clique.input.exception.GraphLoaderException;
import hr.fer.projekt.clique.output.OutputEnvironment;
import hr.fer.projekt.clique.output.implementation.GUIOutputEnvironment;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GUI application which calculates maximal and maximum cliques
 * for undirected String graph provided through textual file or user interface
 * using Bron-Kerbosch algorithm, while outputting algorithm steps. Application
 * supports variations of original algorithm - utilization of degeneracy
 * ordering and utilization of pivot environment, which can be set up through
 * appropriate checkboxes in user interface.
 */
public class ApplicationGUI extends JFrame {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default app window title.
     */
    private static final String WINDOW_TITLE = "Bron-Kerbosch Clique Finder";

    /**
     * Default window width.
     */
    private static final int WINDOW_WIDTH = 1200;

    /**
     * Default window height.
     */
    private static final int WINDOW_HEIGHT = 800;

    /**
     * File name for export file with computation results.
     */
    private static final String OUTPUT_FILE_NAME = "bron_kerbosch_ouput.txt";

    /**
     * Center text displayed to user on application startup
     * and in other situations when no graph definition
     * is provided.
     */
    private String defaultCenterText;

    /**
     * Text pane on which default center text or full path
     * to textual file with graph definition is displayed.
     */
    private JTextPane centerTextPane;

    /**
     * Central panel of application's GUI.
     */
    private JPanel mainPanel;

    /**
     * Central pane positioned in mainPanel
     * {@link JPanel} component.
     */
    private JPanel centerPane;

    /**
     * Text area component user for output of algorithm
     * steps during graph traversal.
     */
    private JTextArea outputArea;

    /**
     * Signals whether graph definition is given through
     * GUI or textual file.
     */
    private boolean guiInput;

    /**
     * Number of vertices for graph defined through GUI.
     */
    private Integer numberOfVertices;

    /**
     * Grid of checkboxes for graph definition through GUI.
     */
    private List<JCheckBox> grid;

    /**
     * Text field for input of number of vertices given
     * through GUI.
     */
    private JTextField textField;

    /**
     * Confirms current selection for number of vertices
     * and triggers display of vertex matrix for edge
     * definition.
     */
    private JButton confirmButton;

    /**
     * Triggers calculation on graph defined through GUI
     * or file definition.
     */
    private JButton calculateButton;

    /**
     * Clears all current selections in app window.
     */
    private JButton clearButton;

    /**
     * Graph on which calculation will be performed.
     */
    private Graph<String, DefaultEdge> graph;

    /**
     * Output environment given to {@link BronKerbosch} algorithm
     * class for algorithm step output to GUI.
     */
    private OutputEnvironment<String> outputEnvironment;

    /**
     * Checkbox which holds boolean value for usage of
     * algorithm variant with degeneracy ordering.
     */
    private JCheckBox degeneracy;

    /**
     * Checkbox which holds boolean value for usage of
     * algorithm variant with pivot environment computation.
     */
    private JCheckBox pivot;

    /**
     * Private constructor receives no arguments, initiates
     * graphical user interface and configures needed output
     * environment for algorithm steps.
     */
    private ApplicationGUI() {
        initGui();
        outputEnvironment = new GUIOutputEnvironment(outputArea);
    }

    /**
     * Constructs application menu bar which offers
     * ability to import graph and export algorithm results,
     * as well as help with how to properly construct textual
     * file with graph definition.
     */
    private void makeMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");

        Action importAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileFilter(new FileNameExtensionFilter(
                        "Graph definitions only",
                        GraphLoader.SUPPORTED_EXTENSION)
                );
                int result = fileChooser.showDialog(null, null);

                if (result != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                File file = fileChooser.getSelectedFile();

                try {
                    graph = GraphLoader.getGraphLoader().loadStringGraph(file.toPath());
                } catch (GraphLoaderException e) {
                    JOptionPane.showMessageDialog(
                            ApplicationGUI.this,
                            "Could not load graph definition from given file. Please check " +
                                    "file structure and try again.",
                            "Graph load failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                guiInput = false;
                configureCenterPane("Graph definition successfully loaded from:\n"
                        + file.getAbsolutePath());

                textField.setEditable(false);
                confirmButton.setEnabled(false);
                calculateButton.setEnabled(true);
                clearButton.setEnabled(true);
                outputArea.setText("");
            }
        };

        importAction.putValue(Action.NAME, "Import");
        importAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);

        Action exportAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (outputArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            ApplicationGUI.this,
                            "No algorithm run was detected.",
                            "Nothing to export",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    return;
                }

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);

                int result = fileChooser.showDialog(null, null);
                if (result != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                System.out.println(fileChooser.getCurrentDirectory());
                System.out.println(fileChooser.getSelectedFile());

                File directory = fileChooser.getSelectedFile();
                File outputFile = new File(directory, OUTPUT_FILE_NAME);

                List<String> lines = Arrays.asList(outputArea.getText().split("\\n"));

                try {
                    Files.write(outputFile.toPath(), lines);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(
                            ApplicationGUI.this,
                            "Unrecoverable error occurred during output export.",
                            "Fatal error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        exportAction.putValue(Action.NAME, "Export");
        exportAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);

        Action graphDefinitionAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        };

        graphDefinitionAction.putValue(Action.NAME, "Graph definition file");
        graphDefinitionAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        fileMenu.add(importAction);
        fileMenu.add(exportAction);

        helpMenu.add(graphDefinitionAction);

        this.setJMenuBar(menuBar);
    }

    /**
     * Configures center pane with text to be displayed
     * by {@link JTextPane} component positioned in
     * center of center pane component.
     *
     * @param text text to be displayed by text pane
     */
    private void configureCenterPane(String text) {
        if (centerPane != null) {
            mainPanel.remove(centerPane);
        }
        centerPane = new JPanel();
        centerPane.setLayout(new BorderLayout());

        centerTextPane = new JTextPane();
        centerTextPane.setText(text);
        centerTextPane.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        centerTextPane.setEditable(false);

        StyledDocument doc = centerTextPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        centerPane.add(centerTextPane, BorderLayout.CENTER);
        mainPanel.add(centerPane, BorderLayout.CENTER);
    }

    /**
     * Action performed when confirm button is pressed.
     */
    private void confirmAction() {
        try {
            numberOfVertices = Integer.valueOf(textField.getText());
        } catch (NumberFormatException x) {
            JOptionPane.showMessageDialog(
                    ApplicationGUI.this,
                    "Input needs to be number in between 3 and 15.",
                    "Invalid input",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (numberOfVertices == null || numberOfVertices < 3 || numberOfVertices > 15) {
            JOptionPane.showMessageDialog(
                    ApplicationGUI.this,
                    "Input needs to be number in between 3 and 15.",
                    "Invalid input",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        makeGrid();
        guiInput = true;

        textField.setEditable(false);
        confirmButton.setEnabled(false);
        calculateButton.setEnabled(true);
        clearButton.setEnabled(true);
    }

    /**
     * Action performed when calculate button is pressed.
     */
    private void calculateAction() {
        if (guiInput) {
            List<String> edges = grid.stream()
                    .filter(cb -> cb.isSelected())
                    .map(cb -> cb.getName().replaceAll(",", "-"))
                    .collect(Collectors.toList());

            graph = new SimpleGraph<>(DefaultEdge.class);
            for (int i = 1; i <= numberOfVertices; i++) {
                graph.addVertex(String.valueOf(i));
            }

            for (String string : edges) {
                String[] def = string.split("-");
                graph.addEdge(def[0], def[1]);
            }
        }

        outputArea.setText("");

        new BronKerbosch<>(
                graph,
                degeneracy.isSelected(),
                pivot.isSelected(),
                outputEnvironment
        ).performTraversal();

        this.revalidate();
    }

    /**
     * Action performed when clear button is pressed.
     */
    private void clearAction() {
        textField.setText("");
        textField.setEditable(true);

        confirmButton.setEnabled(true);
        calculateButton.setEnabled(false);
        clearButton.setEnabled(false);

        outputArea.setText("");

        degeneracy.setSelected(false);
        pivot.setSelected(false);

        grid = null;
        graph = null;
        configureCenterPane(defaultCenterText);
    }

    /**
     * Constructs grid of {@link JCheckBox} elements which
     * form matrix and simulate graph on which user can
     * define connections - edges between vertices.
     */
    private void makeGrid() {
        mainPanel.remove(centerPane);

        centerPane = new JPanel();
        centerPane.setLayout(new GridLayout(numberOfVertices, numberOfVertices));

        grid = new ArrayList<>();

        for (int i = 1; i <= numberOfVertices; i++) {
            for (int j = 1; j <= numberOfVertices; j++) {
                JCheckBox cb = new JCheckBox(i + "," + j);
                cb.setName(i + "," + j);
                cb.setEnabled(!(i == j));
                int x = i - 1;
                int y = j - 1;
                cb.addActionListener(l -> grid.get(numberOfVertices * x + y).setEnabled(!cb.isSelected()));
                grid.add(cb);
                centerPane.add(cb);
            }
        }

        mainPanel.add(centerPane, BorderLayout.CENTER);
        this.revalidate();
    }

    /**
     * Initiates graphical user interface.
     */
    private void initGui() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle(WINDOW_TITLE);
        setLocationRelativeTo(null);

        makeMenu();

        StringBuilder sb = new StringBuilder();
        sb.append("Computation needs graph which can be loaded:\n")
                .append("1) Through selection of number of vertices and edges between them in displayed matrix\n")
                .append("2) Through textual file with graph definition\n")
                .append("First method is limited to graphs for up to 15 vertices.\n")
                .append("Second method supports graphs with any number of vertices.");
        defaultCenterText = sb.toString();

        setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel topPane = new JPanel();
        topPane.setLayout(new GridLayout(1, 4));

        // Text field used for input of number of vertices.
        textField = new JTextField();

        // Confirm button for grid display.
        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> confirmAction());

        // Clear button for program reset.
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearAction());

        // Calculate button for performing computation.
        calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(e -> calculateAction());

        topPane.add(textField);
        topPane.add(confirmButton);
        topPane.add(calculateButton);
        topPane.add(clearButton);

        mainPanel.add(topPane, BorderLayout.PAGE_START);

        JPanel botPane = new JPanel(new BorderLayout());
        JPanel choosePane = new JPanel(new GridLayout(1, 2));

        degeneracy = new JCheckBox("Degeneracy Ordering");
        pivot = new JCheckBox("Pivot Environment");

        choosePane.add(degeneracy);
        choosePane.add(pivot);

        outputArea = new JTextArea("");
        outputArea.setEditable(false);

        botPane.add(choosePane, BorderLayout.NORTH);
        botPane.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        mainPanel.add(botPane, BorderLayout.SOUTH);

        clearAction();

        this.add(mainPanel);
    }

    /**
     * Application entry point.
     *
     * @param args command line arguments are not utilized
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ApplicationGUI().setVisible(true));
    }
}
