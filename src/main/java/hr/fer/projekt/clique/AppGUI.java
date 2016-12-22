package hr.fer.projekt.clique;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public class AppGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel jp;
    private JPanel checkPanel;

    private JPanel mainPanel;
    private JPanel centerPane;

    public AppGUI() {

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setTitle("Clique Finder");
        newPanel();
    }


    private void initGUI() {
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

    private void makeGrid(int size) {
        mainPanel.remove(centerPane);
        centerPane = new JPanel();
        centerPane.setLayout(new GridLayout(size, size));

        Component[][] grid = new Component[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new JCheckBox(i + "," + j);
                centerPane.add(grid[i][j]);
            }
        }

        mainPanel.add(centerPane, BorderLayout.CENTER);
        this.revalidate();
    }

    private void newPanel() {

        this.setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel topPane = new JPanel();
        topPane.setLayout(new GridLayout(1, 3));

        JTextField textField = new JTextField();
        topPane.add(textField);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            int size = Integer.valueOf(textField.getText());
            makeGrid(size);
        });

        JButton clearButton = new JButton("Clear");

        topPane.add(confirmButton);
        topPane.add(clearButton);

        centerPane = new JPanel();
        centerPane.setLayout(new BorderLayout());
        centerPane.add(new JTextArea("Hello world"), BorderLayout.CENTER);

        mainPanel.add(topPane, BorderLayout.PAGE_START);
        mainPanel.add(centerPane, BorderLayout.CENTER);

        this.add(mainPanel);
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
