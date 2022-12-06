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

public class Mandel_pools_blocks {

    public static double[] e1 = {-2.1, -1.2};
    public static double[] e2 = {0.6, 1.2};

    public static int iter = 200;

    public static int max_block = Runtime.getRuntime().availableProcessors();

    public static int reapeats = 4;
    public static int[] sizes = {32, 64, 128, 256, 512, 1024, 2048, 4096, 8192};
    public static int[] block_sizes = {4, 8, 16, 32, 64, 128};

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

    public static BufferedImage draw(int width, int heigh, double[] e1, double[] e2, int iter, int blockx, int blocky, int block_size) {

        BufferedImage bImg = new BufferedImage(width,heigh,BufferedImage.TYPE_INT_RGB);
        Graphics g = bImg.createGraphics();

        double dx = Math.abs(e2[0] - e1[0])/(width-1);
        double dy = Math.abs(e2[1] - e1[1])/(heigh-1);
        int color;

        for (int x = block_size * blockx; x < block_size * (blockx + 1); x++) {
            for (int y = block_size * blocky; y < block_size * (blocky + 1); y++) {
                
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
        
        File csvFile = new File("semestr3/Wdpr/zadania/projekty/output/Mandel_pools_block_size_times.csv");
        ExecutorService ex = Executors.newFixedThreadPool(max_block);

        try {
            PrintWriter pw = new PrintWriter(csvFile);

            for (int s = 0; s < sizes.length - 3; s++) {

                for (int b = 0; b < block_sizes.length && block_sizes[b] <= sizes[s]; b++) { 

                    int blocksx = (int) (sizes[s] / block_sizes[b]);
                    int blocksy = blocksx;
                    int total_blocks = blocksx * blocksy;

                    //System.out.println("s = " + sizes[s] + " ,b = " + block_sizes[b] + " ,blocks = " + total_blocks + " " + blocksx);

                    for (int r = 0; r < reapeats; r++) {
                        
                        CountDownLatch latch = new CountDownLatch(total_blocks);
                        long start = System.nanoTime();   

                        for(int bx = 0; bx < blocksx; ++bx){
                            for(int by = 0; by < blocksy; ++by){
                                int s_ = s;
                                int b_ = b;
                                int bx_ = bx;
                                int by_ = by;

                                ex.execute(() -> {
                                    BufferedImage bImg = draw(sizes[s_], sizes[s_], e1, e2, iter, bx_, by_, block_sizes[b_]);
                                    latch.countDown();
                                });
                            }
                        }

                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        
                        long stop = System.nanoTime();
        
                        time = (double)((stop - start) / 1000000.0);

                        pw.printf(Locale.US, "%d,%d,%f\n", sizes[s], block_sizes[b], time);
                        System.out.println("Skonczono " + sizes[s]);
                    }
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
