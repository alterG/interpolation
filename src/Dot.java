/**
 * Created by alterG on 22.12.2016.
 */
public class Dot implements Comparable {
    private double x;
    private  double y;

    public Dot(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public int compareTo(Object o) {
        Dot tempDot = (Dot) o;
        double result = this.y-tempDot.y;
        if (result>0) return 1;
        else if(result==0) return 0;
             else return -1;
    }
}
