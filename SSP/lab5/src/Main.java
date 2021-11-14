import javax.swing.*;
import java.awt.*;

public class Main {

    static JTable jTable;
    static Integer[][] data = new Integer[][]{{1, 2, 3}};
    static String[] columns = new String[]{"", "", ""};

    public static void createPanelUI(Container container) {

        jTable = new JTable(data, columns);
        jTable.setBounds(10, 20, 200, 200);
        JScrollPane scrollPane = new JScrollPane(jTable);
        JPanel jPanel = new JPanel();
        jPanel.setBounds(30, 40, 300, 300);
        jPanel.add(scrollPane);

        //Для управления расположением
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;

        JButton button;

        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        container.setLayout(new GridBagLayout());


        // По умолчанию натуральная высота, максимальная ширина
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        constraints.gridy = 0;  // нулевая ячейка таблицы по вертикали

        button = new JButton("Школа");

        constraints.gridx = 0;      // нулевая ячейка таблицы по горизонтали
        container.add(button, constraints);

        button = new JButton("Институт");
        constraints.gridx = 1;      // первая ячейка таблицы по горизонтали
        container.add(button, constraints);

        button = new JButton("Академия");
        constraints.gridx = 2;      // вторая ячейка таблицы по горизонтали
        container.add(button, constraints);

        button = new JButton("Высокая кнопка размером в 2 ячейки");
        constraints.ipady = 45;   // кнопка высокая
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;    // размер кнопки в две ячейки
        constraints.gridx = 0;    // нулевая ячейка по горизонтали
        constraints.gridy = 1;    // первая ячейка по вертикали
        container.add(button, constraints);

        button = new JButton("Семья");
        constraints.ipady = 0;    // установить первоначальный размер кнопки
        constraints.weighty = 1.0;  // установить отступ
        // установить кнопку в конец окна
        constraints.anchor = GridBagConstraints.PAGE_END;
        constraints.insets = new Insets(5, 0, 0, 0);  // граница ячейки по Y
        constraints.gridwidth = 2;    // размер кнопки в 2 ячейки
        constraints.gridx = 1;    // первая ячейка таблицы по горизонтали
        constraints.gridy = 2;    // вторая ячейка по вертикали
        container.add(button, constraints);
    }

    public static void main(String[] args) {
        // Создание окна
        JFrame frame = new JFrame("GridBagLayoutTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Определить панель содержания
        createPanelUI(frame.getContentPane());

        // Показать окно
        frame.pack();
        frame.setVisible(true);
    }
}

//public class Main {
//
//
//    public static void main(String[] args) {
//        f = new JFrame();
//        f.setSize(600, 600);

//        f.add(new JLabel("Количество строк"));
//        f.add(new JLabel("Количество столбцов"));
//        f.setVisible(true);
//    }
//}
