import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class MergeSort_single {

        private static void mergeSort (int[] array) {

            int length = array.length;
            if (length < 2) 
                return;

            int middle = length / 2;

            int[] larray = new int[middle];
            int[] rarray = new int[length - middle];

            System.arraycopy(array, 0, larray, 0, middle);
            System.arraycopy(array, middle, rarray, 0, length - middle);

            mergeSort(larray);
            mergeSort(rarray);
            merge(larray, rarray, array);
        }

        private static void merge(int[] larray, int[] rarray, int[] array) {
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

        private static int[] sort(int[] array) {

            int[] sorted_array = new int[array.length];

            System.arraycopy(array, 0, sorted_array, 0, array.length);

            mergeSort(sorted_array);
            return sorted_array;
        }


    public static void main(String[] args) {

        double time;
        Random rClass = new Random();

        File csvFile = new File("semestr3/Wdpr/zadania/projekty/output/MS_single.csv");

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
                    int[] sorted_array = sort(array);
                    long stop = System.nanoTime();

                    // System.out.println(Arrays.toString(sorted_array));
                    // System.out.println(Arrays.toString(array));

                    time = (double)((stop - start) / 1000000.0);

                    pw.printf(Locale.US, "%d,%f\n", size, time);
                    System.out.println("Skonczono " + size);
                }
            }
            pw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}