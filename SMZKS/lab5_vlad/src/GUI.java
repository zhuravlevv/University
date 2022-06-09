import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GUI implements Runnable {

    private DefaultTableModel model;
    private JFrame frame;
    private JTable table;
    private Map<String, String> data;
    private JTextField textFieldA = new JTextField();
    private JTextField textFieldB = new JTextField();
    private JTextField textFieldC = new JTextField();
    private JTextField textFieldD = new JTextField();

    static MainInterface main = new Main1();

    // Launch the application.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GUI());
    }

    public GUI() {
        String[] columnNames = {" Name", " Value "};
        this.model = new DefaultTableModel();

        for (String s : columnNames) {
            model.addColumn(s);
        }

        this.data = new LinkedHashMap<String, String>() {{
            put(" ", "");
            put("N1", main.getN1().toString());
            put("N2", main.getN2().toString());
            put("N3", main.getN3().toString());
            put("C1", main.getC1().toString());
            put("C2", main.getC2().toString());
            put("C3", main.getC3().toString());
            put("  ", "");
        }};
    }

    // Create the application.
    @Override
    public void run() {
        frame = new JFrame("Football GUI");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(createTablePanel(), BorderLayout.CENTER);
        frame.add(createButtonPanel(), BorderLayout.AFTER_LINE_ENDS);

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel();

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0d;

        JLabel labelA = new JLabel("A");
        panel.add(labelA, gbc);

        gbc.gridy++;
        textFieldA = new JTextField(16);
        panel.add(textFieldA, gbc);

        gbc.gridy++;
        JLabel labelB = new JLabel("B");
        panel.add(labelB, gbc);

        gbc.gridy++;
        textFieldB = new JTextField(16);
        panel.add(textFieldB, gbc);

        gbc.gridy++;
        JLabel labelc = new JLabel("C");
        panel.add(labelc, gbc);

        gbc.gridy++;
        textFieldC = new JTextField(16);
        panel.add(textFieldC, gbc);

        gbc.gridy++;
        JLabel labelD = new JLabel("D");
        panel.add(labelD, gbc);

        gbc.gridy++;
        textFieldD = new JTextField(16);
        panel.add(textFieldD, gbc);

        gbc.gridy++;
        JButton displayTeams = new JButton("Calculate M0, mi, ni");
        displayTeams.addActionListener(new CalculateListener());
        panel.add(displayTeams, gbc);

        gbc.gridy++;
        JButton clearTextFields = new JButton("Clear A, B, C, D");
        clearTextFields.addActionListener(new ClearTextFieldsListener());
        panel.add(clearTextFields, gbc);

        gbc.gridy++;
        JButton increaseNumberOfRaws = new JButton("Increase Number of Raws");
        increaseNumberOfRaws.addActionListener(new IncreaseNumberOfRaws());
        panel.add(increaseNumberOfRaws, gbc);

        gbc.gridy++;
        JButton clearGrid = new JButton("Clear Grid");
        clearGrid.addActionListener(new ClearGrid());
        panel.add(clearGrid, gbc);

        return panel;
    }

    public class CalculateListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

            main.start();

            int count = model.getRowCount();
            for (int i = 0; i < count; i++) {
                model.removeRow(0);
            }

            data.put("M0", String.valueOf(main.getM0()));
            data.put("m1", String.valueOf(main.getm1()));
            data.put("m2", String.valueOf(main.getm2()));
            data.put("m3", String.valueOf(main.getm3()));
            data.put("n1", main.getn1().toString());
            data.put("n2", main.getn2().toString());
            data.put("n3", main.getn3().toString());
            data.put("   ", " ");
            data.put("C1*n1*m1", main.getC1n1m1().toString());
            data.put("C2*n2*m2", main.getC2n2m2().toString());
            data.put("C3*n3*m3", main.getC3n3m3().toString());
            data.put("S", main.getS().toString());
            data.put("SModM0", main.getSModM0().toString());
            data.put("M", main.getM().toString());

            textFieldA.setText(main.getSModM0().toString());
            textFieldB.setText(String.valueOf(main.getE()));
            textFieldC.setText(main.getM0().toString());
            textFieldD.setText(main.getM().toString());

            for (Map.Entry<String, String> entry : data.entrySet()) {
                model.addRow(new Object[]{entry.getKey(), entry.getValue()});
            }
        }
    }

    public class ClearGrid implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            model.setNumRows(0);
        }
    }

    public class IncreaseNumberOfRaws implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            model.addRow(new Object[]{"", ""});
        }
    }

    public class ClearTextFieldsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

            textFieldA.setText("");
            textFieldB.setText("");
            textFieldC.setText("");
            textFieldD.setText("");

        }
    }

}