import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.imageio.ImageIO;
import java.awt.Color;

import java.awt.Graphics;

public class Mandelbrot{
    
    private static BufferedImage bImg;
    private static Graphics g;

    public static int check_convergence(Complex c, int iter) {
        
        Complex c0 = new Complex(0,0);

        for (int i = 1; i <= iter; i++) {
            c0 = Complex.add(Complex.multiply(c0, c0), c);

            if (Complex.module(c0) > 2) {
                return i;
            }
        }

        return iter;
    }

    private static BufferedImage draw(int width, int heigh, double[] e1, double[] e2, int iter) {

        bImg = new BufferedImage(width,heigh,BufferedImage.TYPE_INT_RGB);
        g = bImg.createGraphics();

        double dx = Math.abs(e2[0] - e1[0])/(width-1);
        double dy = Math.abs(e2[1] - e1[1])/(heigh-1);
        int color;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < heigh; y++) {
                
                int iterations = check_convergence(new Complex(e1[0] + x * dx, e1[1] + y * dy), iter);

                // if (iterations == iter) {
                //     color = 0x00000000;
                // } else {
                //     color = 0xFFFFFFFF;
                // }

                color = Color.HSBtoRGB(((float) iterations/iter), 0.8f, 1);
                bImg.setRGB(x, y, color);
            }
        }

        g.drawImage(bImg, 0, 0, null);
        
        g.dispose();

        return bImg;
    }

    private static void measure_time(int[] sizes, int reapeats){

        double[] e1 = {-2.1, -1.2};
        double[] e2 = {0.6, 1.2};

        int iter = 200;

        double time;
        
        File csvFile = new File("semestr3/Wdpr/zadania/projekty/output/Mandel_times.csv");

        try {
            PrintWriter pw = new PrintWriter(csvFile);
            for (int s = 0; s < sizes.length - 1; s++) { 
    
                for (int r = 0; r < reapeats; r++) {
    
                    long start = System.nanoTime();
                    BufferedImage bImg = draw(sizes[s], sizes[s], e1, e2, iter);
                    long stop = System.nanoTime();
    
                    time = (double)((stop - start) / 1000000.0);

                    pw.printf(Locale.US, "%d,%f\n", sizes[s], time);
                    System.out.println("Skonczono " + sizes[s]);
                }
            }
            pw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
        int width = 4096;
        int heigh = width;

        double[] e1 = {-2.1, -1.2};
        double[] e2 = {0.6, 1.2};

        int iter = 200;

        int reapeats = 10;
        int[] sizes = {32, 64, 128, 256, 512, 1024, 2048, 4096, 8192};
        measure_time(sizes, reapeats);

        // BufferedImage bImg = draw(width, heigh, e1, e2, iter);

        // File outputfile = new File("semestr3/Wdpr/zadania/projekty/output/Mandelbrot.png");
        
        // try {
        //     ImageIO.write(bImg, "png", outputfile);
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }
}
