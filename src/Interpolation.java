import java.util.ArrayList;

/**
 * Created by alterG on 22.12.2016.
 */
public class Interpolation {

    ArrayList<Dot> dots;
    //koef of Interpolation functions
    double[] y;
    double[] x;
    double[] b;
    double[] c;
    double[] d;
    boolean readyToInterpolate;


    public Interpolation(ArrayList<Dot> dots) {
        this.dots=dots;
        y = new double[dots.size()];
        x = new double[dots.size()];
        b = new double[dots.size()];
        c = new double[dots.size()];
        d = new double[dots.size()];
        for (int i = 0; i < dots.size(); i++) {
            y[i]=dots.get(i).getY();
            x[i]=dots.get(i).getX();
        }
    }

    public void calcCoef() {
        double [] alpha = new double[dots.size()-1];
        double [] beta = new double[dots.size()-1];
        double [] h = new double[dots.size()];
        double [] A = new double[dots.size()-1];
        double [] B = new double[dots.size()-1];
        double [] C = new double[dots.size()-1];
        double [] F = new double[dots.size()-1];
       /*
        h - количество интервалов
        dots - количество точек
        */
        //initialization h
        for (int i = 1; i < dots.size(); i++) {
        h[i]=x[i]-x[i-1];
        }
        //initialization A
        for (int i = 1; i < dots.size()-1 ; i++) {
            A[i] = h[i];
        }
        //initialization B
        for (int i = 1; i < dots.size()-1 ; i++) {
            B[i] = h[i+1];
        }
        //initialization C
        for (int i = 1; i <dots.size()-1 ; i++) {
            C[i] = 2.0*(h[i]+h[i+1]);
        }
        //initialization F
        for (int i = 1; i <dots.size()-1 ; i++) {
            F[i] = 6.0*((y[i+1]-y[i])/h[i]-(y[i]-y[i-1])/h[i]);
        }
        //initialization alpha
        alpha[0]=0;
        for (int i = 1; i <dots.size()-1; i++) {
            alpha[i] = -B[i]/(A[i]*alpha[i-1]+C[i]);
        }
        //initialization beta
        beta[0]=0;
        for (int i = 1; i <dots.size()-1 ; i++) {
            beta[i] = (F[i]-A[i]*beta[i-1])/(A[i]*alpha[i-1]+C[i]);
        }

        //initializaton c
        c[0]=0;
        c[dots.size()-1]=0;
        for (int i = dots.size()-2; i > 0 ; --i) {
            c[i] = alpha[i]*c[i+1]+beta[i];
        }
        //initialization b and d
        for (int i = dots.size()-1; i >0 ; --i) {
            b[i] = h[i]*(2.0*c[i]+c[i-1])/6.0+(y[i]-y[i-1])/h[i];
            d[i] = (c[i]-c[i-1])/h[i];
        }
        readyToInterpolate = true;
    }
    public void printCoef() {
        for (double x : b) {
            System.out.println("b "+x);
        }
        for (double x : c) {
            System.out.println("c "+x);
        }
        for (double x : d) {
            System.out.println("d "+x);
        }
            }

    public double interpolate(double x) {
        int intervalNumber=0;
        if (!readyToInterpolate) calcCoef();
        for (int i = 0; i < dots.size()-1; i++) {
            if (x>=this.x[i]) intervalNumber++;
            else break;
        }
        double dx = x-this.x[intervalNumber];
//        if (intervalNumber!=0) intervalNumber--;
        return y[intervalNumber]+b[intervalNumber]*dx+c[intervalNumber]/2.0*Math.pow(dx,2)+d[intervalNumber]/6.0*Math.pow(dx,3);
    }


}
