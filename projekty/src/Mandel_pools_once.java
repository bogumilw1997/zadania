import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.awt.Color;

import java.awt.Graphics;

public class Mandel_pools_once {

    public static double[] e1 = {-2.1, -1.2};
    public static double[] e2 = {0.6, 1.2};

    public static int iter = 200;

    public static int max_block = Runtime.getRuntime().availableProcessors();

    public static int reapeats = 10;
    public static int[] sizes = {32, 64, 128, 256, 512, 1024, 2048, 4096, 8192};

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

    public static BufferedImage draw(int width, int heigh, double[] e1, double[] e2, int iter, int block, int max_block) {

        BufferedImage bImg = new BufferedImage(width,heigh,BufferedImage.TYPE_INT_RGB);
        Graphics g = bImg.createGraphics();

        double dx = Math.abs(e2[0] - e1[0])/(width-1);
        double dy = Math.abs(e2[1] - e1[1])/(heigh-1);
        int color;

        for (int x = 0; x < width; x++) {
            for (int y = Math.round(heigh * block / max_block); y < Math.round(heigh * (block + 1) / max_block); y++) {
                
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

    public static void main(String[] args) {
        
        double time;
        
        File csvFile = new File("semestr3/Wdpr/zadania/projekty/output/Mandel_pools_create_once_times.csv");
        ExecutorService ex = Executors.newFixedThreadPool(max_block);
        
        try {
            PrintWriter pw = new PrintWriter(csvFile);

            for (int s = 0; s < sizes.length - 1; s++) { 
    
                for (int r = 0; r < reapeats; r++) {
                    
                    CountDownLatch latch = new CountDownLatch(max_block);
                    long start = System.nanoTime();   

                    for(int i = 0; i < max_block; ++i){
                        int s_ = s;
                        int i_ = i;
                        ex.execute(() -> {
                            BufferedImage bImg = draw(sizes[s_], sizes[s_], e1, e2, iter, i_, max_block);
                            latch.countDown();
                        });
                    }

                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                    long stop = System.nanoTime();
    
                    time = (double)((stop - start) / 1000000.0);

                    pw.printf(Locale.US, "%d,%f\n", sizes[s], time);
                    System.out.println("Skonczono " + sizes[s]);
                }
            }
            ex.shutdown();
            try {
                ex.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            pw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
