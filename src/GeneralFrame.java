import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by alterG on 14.01.2017.
 */
public class GeneralFrame extends JFrame {

    public GeneralFrame(Interpolation function) {
        setSize(1160, 840);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Homework#3 Interpolation (cube splines method) Author: Igor Shchipanov P3202");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        FunctionGraph functionGraph = new FunctionGraph(800,800);
        ToolsPanel toolsPanel = new ToolsPanel(360,800, functionGraph);
        functionGraph.setToolsPanel(toolsPanel);
        add(functionGraph, BorderLayout.WEST);
        add(toolsPanel, BorderLayout.CENTER);
        setVisible(true);

    }

}
