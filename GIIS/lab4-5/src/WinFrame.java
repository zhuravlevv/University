import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class WinFrame {
    public static void main(String[] args) throws IOException {
        JFrame jf = new JFrame();
        JPanel jp = new JPanel();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jp.setLayout(new GridLayout(2,2,1,1));
        Game game = new Game("/home/vladislav/IdeaProjects/University/GIIS/lab4-5/src/resources");
        Integer sizeBlock = game.sizeBlock;
        Integer sizeGame = game.sizeGame;
        jf.setSize(sizeBlock*sizeGame,sizeBlock*sizeGame);
        BufferedImage img = game.img;
        JLabel jl = new JLabel();
        jl.setSize(sizeBlock,sizeBlock);
        BufferedImage[][] bb;
        Integer[][] x;
        Integer[][] y;
        x = new Integer[sizeGame][sizeGame];
        y = new Integer[sizeGame][sizeGame];
        bb = new BufferedImage[sizeGame][sizeGame];
        for (int i = 0; i < sizeGame; i++) {
            for (int j = 0; j < sizeGame; j++) {
                x[i][j] = 0;
                y[i][j] = 0;
            }
        }

        int width = img.getWidth() / sizeGame;
        int height = img.getHeight() / sizeGame;
        for (int i = 0; i < sizeGame; i++)
            for (int j = 0; j < sizeGame; j++) {
                bb[i][j] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                for (int k = 0 + i * height, q = 0; k < height + i * height; k++, q++) {
                    for (int l = 0 + j * width, w = 0; l < width + j * width; l++, w++) {
                        bb[i][j].setRGB(w, q, img.getRGB(l, k));
                    }
                }
                bb[i][j] = game.resize(bb[i][j], jl);

            }

        jf.add(new JPanel() {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(sizeBlock*sizeGame, sizeBlock*sizeGame);
            }

            boolean flag = false;

            Integer col;

            int r = 0;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                JLabel jl = new JLabel();
                jl.setPreferredSize(new Dimension(sizeBlock, sizeBlock));
                jl.setSize(new Dimension(sizeBlock, sizeBlock));
                if(r == 0)
                    col = sizeGame - 1;
                r += 1;
                Graphics2D g2 = (Graphics2D) g;
                FontRenderContext frc = g2.getFontRenderContext();
                Font font1 = new Font("Courier", Font.BOLD, 24);
                String str1 = new String("You Win!!! Congratulation!!!");
                TextLayout tl = new TextLayout(str1, font1, frc);
                g2.setColor(Color.gray);
                tl.draw(g2,  200,getHeight()/2);
                for (int i = 0; i < sizeGame; i++) {
                    Integer rrnd = new Random().nextInt(5);
                    for (int j = 0; j < sizeGame; j++) {
                        if (j >= col) {
                            y[i][j] += rrnd;
                            x[i][j] += rrnd;
                            AffineTransform af = AffineTransform.getTranslateInstance(i * sizeBlock, j * sizeBlock + y[i][j]);
                            af.rotate(Math.toRadians(r), sizeBlock / 2, sizeBlock / 2);
                            g2.drawImage(bb[i][j], af, null);
                        } else
                            g2.drawImage(bb[i][j], i * sizeBlock + x[i][j], j * sizeBlock + y[i][j], null);
                        if(y[0][col] > getHeight() - sizeBlock*3 && col >0)
                            col --;
                        if (y[0][col] > getHeight()+100)
                            flag = true;
                    }
                    if (!flag) {
                        repaint();
                    }
                }
            }

        });
        jf.pack();
        jf.setVisible(true);
    }
}