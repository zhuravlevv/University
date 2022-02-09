import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Window3 extends JFrame {
    private JLabel label1 = new JLabel("     Введите сообщение: ");
    private JLabel label2 = new JLabel("     Введите номер ошибочного символа: ");
    private JLabel label3 = new JLabel("     Введите номер ошибочного бита (1-8): ");
    private JTextField field1 = new JTextField("");
    private JTextField field2 = new JTextField("");
    private JTextField field3 = new JTextField("");
    private JButton button1 = new JButton("Запуск");

    public Window3() {
        super.setTitle("Window");
        this.setSize(700, 500);

        field1.setColumns(30);
        field2.setColumns(30);
        field3.setColumns(30);
        button1.addActionListener(this::buttonClick);
        this.getContentPane().setLayout(new FlowLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.getContentPane().add(label1);
        this.getContentPane().add(field1);
        this.getContentPane().add(label2);
        this.getContentPane().add(field2);
        this.getContentPane().add(label3);
        this.getContentPane().add(field3);
        this.getContentPane().add(button1);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void buttonClick(ActionEvent e) {
        String text = field1.getText();
        field1.setText("");

        Integer errorWordNumber = Integer.parseInt(field2.getText());
        errorWordNumber = (errorWordNumber - 1) * 2;
        field2.setText("");

        Integer errorBitNumber = Integer.parseInt(field3.getText()) - 1;
        if(errorBitNumber >= 4){
            errorWordNumber++;
            errorBitNumber%=4;
        }
        field3.setText("");

        int[][] words = Hamming3.parseText(text);

        String[] results = Hamming3.start(words, errorWordNumber, errorBitNumber);
        String message = results[0];
        String restoredMessage = results[1];

        JOptionPane.showMessageDialog(null, "Полученное сообщение: " + message + "\nВосстановленное сообщение: " + restoredMessage);

    }

    public static void main(String[] args) {
        new Window3();
    }
}