public class Complex {
   double real;
   double img;

   public Complex(double real, double img){
       this.real = real;
       this.img = img;
    }

   public static Complex add(Complex c1, Complex c2){

        double temp_real = c1.real + c2.real;
        double temp_img = c1.img + c2.img;

        return(new Complex(temp_real, temp_img));
     }

   public static Complex multiply(Complex c1, Complex c2){

      double temp_real = c1.real * c2.real - c1.img * c2.img;
      double temp_img = c1.real * c2.img + c1.img * c2.real;

      return(new Complex(temp_real, temp_img));
   }

   public static double module(Complex c1){

      double temp_module = Math.sqrt(Math.pow(c1.real, 2) + Math.pow(c1.img, 2));

      return(temp_module);
   }
}
