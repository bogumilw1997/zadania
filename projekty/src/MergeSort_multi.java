import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class MergeSort_multi {
    
    public static ForkJoinPool fjp = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    public static class MS_action extends RecursiveAction {

        private int[] array;
        
        public MS_action(int[] array) {
            this.array = array;
        }

        @Override
        protected void compute() {

            int length = array.length;
            if (length < 2) 
                return;

            int middle = length / 2;

            int[] larray = new int[middle];
            int[] rarray = new int[length - middle];

            System.arraycopy(array, 0, larray, 0, middle);
            System.arraycopy(array, middle, rarray, 0, length - middle);

            invokeAll(new MS_action(larray), new MS_action(rarray));
            merge(larray, rarray);
        }

        // public void sort(int[] array) {
        //     ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        //     forkJoinPool.invoke(new MS_action(array));
        // }

        private void merge(int[] larray, int[] rarray) {
            int i = 0;
            int j = 0;
            int s = 0;

            while (i < larray.length && j < rarray.length) {
                if (larray[i] < rarray[j]) {
                    array[s] = larray[i];
                    s++;
                    i++;
                }
                else {
                    array[s] = rarray[j];
                    s++;
                    j++;
                }
            }
            while (i < larray.length) {
                array[s] = larray[i];
                s++;
                i++;
            }
            while (j < rarray.length) {
                array[s] = rarray[j];
                s++;
                j++;
            }
        }
    }

    public static int[] sort(int[] array, ForkJoinPool fjp) {

        int[] sorted_array = new int[array.length];

        System.arraycopy(array, 0, sorted_array, 0, array.length);

        fjp.invoke(new MS_action(sorted_array));
        return sorted_array;
    }


    public static void main(String[] args) {

        double time;
        Random rClass = new Random();

        File csvFile = new File("semestr3/Wdpr/zadania/projekty/output/MS_multi.csv");

        int base = 2;
        
        int min_exp = 3;
        int max_exp = 20;

        int reapeats = 10;

        try {
            PrintWriter pw = new PrintWriter(csvFile);

            for (int i = min_exp; i <= max_exp; i++) {

                int size = (int) Math.pow(base, i);
                int [] array = new int[size];
                
                array = rClass.ints().limit(array.length).toArray();
  
                for (int r = 0; r < reapeats; r++) {

                    long start = System.nanoTime();
                    int[] sorted_array = sort(array, fjp);

                    long stop = System.nanoTime();

                    time = (double)((stop - start) / 1000000.0);

                    pw.printf(Locale.US, "%d,%f\n", size, time);
                    System.out.println("Skonczono " + size);
                }
            }
            
            fjp.shutdown();
            try {
                fjp.awaitTermination(1, TimeUnit.DAYS);
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