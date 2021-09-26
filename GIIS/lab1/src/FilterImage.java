import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class FilterImage extends JFrame{
    private String imageName;
    private Double percentNoise;
    private BufferedImage bufImage = null;
    private Random rnd = new Random();
    private Integer sizeX;
    private Integer sizeY;
    private BufferedImage noisedImage;

    public FilterImage(String imageName, String sizeX, String sizeY, boolean flag) {
        this.imageName = imageName;
        this.sizeX = Integer.parseInt(sizeX);
        this.sizeY = Integer.parseInt(sizeY);

        setTitle("ImageTest");
        setSize(2000, 1020);
        percentNoise = 0.1;
        noiseImage();

            if (!flag)
                processingFilter(1);
            else {
                processingFilter(1);
                swap();
                processingFilter(1);
                swap();
            }

        JScrollPane scrollPane = new JScrollPane();
        JLabel l1 = new JLabel();
        l1.setSize(800,800);
        l1.setIcon(new ImageIcon(resize(noisedImage,l1)));
        JLabel l2 = new JLabel();
        l2.setSize(800,800);
        l2.setIcon(new ImageIcon(resize(bufImage,l2)));
        JPanel p = new JPanel();
        p.setLayout (new FlowLayout());
        p.add(l1);
        p.add(l2);
        scrollPane.setViewportView(p);
        add(scrollPane);
        pack();
    }
    private void swap(){
        int t = sizeX;
        sizeX = sizeY;
        sizeY = t;
    }
    private void noiseImage() {
        try {
            bufImage = ImageIO.read(new File(imageName));
            Integer height = bufImage.getHeight();
            Integer width = bufImage.getWidth();
            Color white = new Color(255, 255, 255);
            noisedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (rnd.nextDouble() < percentNoise) {
                        bufImage.setRGB(j, i, white.getRGB());
                    }
                    noisedImage.setRGB(j,i, bufImage.getRGB(j,i));
                }
            }

            saveImage(folder + "\\noise.jpg", bufImage);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "You haven't selected an image", "Error", JOptionPane.OK_OPTION);
        }
    }

    private void processingFilter(int index) {
        Integer height = bufImage.getHeight();
        Integer width = bufImage.getWidth();
        for (int i = 0; i < height - sizeY; i++)
            for (int j = 0; j < width - sizeX; j++)
                bufImage.setRGB(j + ((sizeX - 1) / 2), i + ((sizeY - 1) / 2), avgColor(i, j).getRGB());
        saveImage(folder + "\\process"+ sizeX+ "x" + sizeY +"_" + index + ".jpg", bufImage);
    }
    public BufferedImage resize(BufferedImage img, JLabel panel){
        Image tmp = img.getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_SMOOTH);
        img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return img;
    }

    private Color avgColor(int row, int col) {
        ArrayList<Integer> r = new ArrayList<>();
        ArrayList<Integer> g = new ArrayList<>();
        ArrayList<Integer> b = new ArrayList<>();
        for (int i = row; i < row + sizeY; i++)
            for (int j = col; j < col + sizeX; j++) {
                Color c = new Color(bufImage.getRGB(j, i));
                r.add(c.getRed());
                b.add(c.getBlue());
                g.add(c.getGreen());
            }
        Collections.sort(r);
        Collections.sort(g);
        Collections.sort(b);
        int size = sizeX * sizeY;
        return new Color(r.get((size - 1) / 2), g.get((size - 1) / 2), b.get((size - 1) / 2));
    }

    private void saveImage(String name, BufferedImage buf) {
        try {
            ImageIO.write(buf, "jpg", new File(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static  String folder ="";
    public static void main(String[] args) {
        JFrame mainWindow = new JFrame();
        mainWindow.setSize(450,120);
        mainWindow.setTitle("Filter Image");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        JTextField textField = new JTextField("D:\\lab1\\13.jpg",20);
        folder = "D:\\lab1";
        textField.setEnabled(false);
        JButton load = new JButton("Load image");
        load.addActionListener(e -> {
            JFileChooser c = new JFileChooser();
            c.setCurrentDirectory(new File("src//lab1"));
            int rVal = c.showOpenDialog(mainWindow);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                if (c.getSelectedFile().toString().contains(".jpg") || c.getSelectedFile().toString().contains(".png")) {
//                    JOptionPane.showMessageDialog(mainWindow, "Wait");
                    textField.setText(c.getSelectedFile().toString());
                    folder = c.getCurrentDirectory().toString();

                    JOptionPane.showMessageDialog(mainWindow, "Load success");

                } else if (rVal == JFileChooser.CANCEL_OPTION)
                    JOptionPane.showMessageDialog(mainWindow, "Load cancel",
                            "", JOptionPane.OK_OPTION);
                else
                    JOptionPane.showMessageDialog(mainWindow, "Pick image in jpg or png format",
                            "Error", JOptionPane.OK_OPTION);

            }
        });

        JTextField sizeXField = new JTextField("0",5);
        JTextField sizeYField = new JTextField("0",5);
        JCheckBox check = new JCheckBox("both");
        JButton start = new JButton("Start");

        mainPanel.add(new JLabel("Image Name: "));
        mainPanel.add(textField);
        mainPanel.add(load);
        mainPanel.add(new JLabel("X: "));
        mainPanel.add(sizeXField);
        mainPanel.add(new JLabel("Y: "));
        mainPanel.add(sizeYField);
        mainPanel.add(check);
        mainPanel.add(start);
        start.addActionListener(e -> EventQueue.invokeLater(() -> {
            FilterImage frame = new FilterImage(textField.getText(),sizeXField.getText(),sizeYField.getText(),check.isSelected());
            frame.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent ev){
                    frame.setVisible(false);
                    frame.dispose();
                }
            });
            frame.setVisible(true);
        }));

        mainWindow.add(mainPanel);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);
    }
}