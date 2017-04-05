import java.util.Comparator;

/**
 * Created by alterG on 15.01.2017.
 */
public class DotComparatorByY implements Comparator<Dot> {
    @Override
    public int compare(Dot o1, Dot o2) {
        double result = o1.getY()-o2.getY();
        if (result>0) return 1;
        else if(result==0) return 0;
        else return -1;
    }
}
