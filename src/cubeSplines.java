import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by alterG on 22.12.2016.
 */
public class cubeSplines {
    public static void main(String[] args) {
        ArrayList<Dot> dots = new ArrayList<>();
        GeneralFrame jframe = new GeneralFrame(new Interpolation(dots));
    }
}
