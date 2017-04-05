import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by alterG on 15.01.2017.
 */
public class ToolsPanel extends JPanel {
    //size of Toolspanel
    int width, heigth;

    //panel where functions draw
    FunctionGraph functionGraph;

    //dots for interpolation
    ArrayList<Dot> dots;

    //type of function to interpolate
    FunctionType functionType;

    //range of getting dots to interpolation
    double startXRange;
    double endXRange;

    //model for JTextField (axis scale)
    double maxY;
    double minY;
    double maxX;
    double minX;

    // true if interpolation coefs is calculated
    boolean readyToDraw;

    //true if scale is freezed
    boolean scaleFreezed;


    Interpolation interpolationFunction;


    public ToolsPanel(int width, int heigth,FunctionGraph functionGraph) {
        this.setLayout(null);
        this.width=width;
        this.heigth=heigth;
        this.functionGraph=functionGraph;

        //function type picker
        JPanel functionPickerPanel = new JPanel(null);
        JLabel functionPicker = new JLabel("Choose function to interpolate:");
        JLabel dotsInterpolateAmount = new JLabel("Choose amount of dots to interpolate:");
        JLabel messageTypePickerLabel = new JLabel("");
        JPanel buttonGroupPanel = new JPanel(new GridLayout(1,0));
        ButtonGroup functionPickerButtonGroup = new ButtonGroup();
        JRadioButton sinxRadioButton = new JRadioButton("Sin(x)", true);
        JRadioButton x2RadioButton = new JRadioButton("X^2", false);
        JRadioButton sqrtxRadioButton = new JRadioButton("Sqrt(x)", false);
        sinxRadioButton.setActionCommand("Sin(x)");
        x2RadioButton.setActionCommand("x^2");
        sqrtxRadioButton.setActionCommand("Sqrt(x)");
        functionPickerButtonGroup.add(sinxRadioButton);
        functionPickerButtonGroup.add(x2RadioButton);
        functionPickerButtonGroup.add(sqrtxRadioButton);
        buttonGroupPanel.add(sinxRadioButton);
        buttonGroupPanel.add(x2RadioButton);
        buttonGroupPanel.add(sqrtxRadioButton);
        //JSlider for setting dots amount
        JSlider sliderDotsInterpolateAmount = new JSlider(JSlider.HORIZONTAL, 4,20,12);
        sliderDotsInterpolateAmount.setMajorTickSpacing(8);
        sliderDotsInterpolateAmount.setMinorTickSpacing(1);
        sliderDotsInterpolateAmount.setPaintTicks(true);
        sliderDotsInterpolateAmount.setPaintLabels(true);
        //Determination of X area to interpolate
        JLabel startXLabel = new JLabel("Interpolate function from x = ");
        JLabel endXLabel = new JLabel("to ");
        JTextField startX = new JTextField();
        JTextField endX = new JTextField();
        //JButton for interpolation
        JButton interpolateButton = new JButton("Interpolate");
        functionPicker.setBounds(20,20,240,20);
        buttonGroupPanel.setBounds(20, 50, 300,20);
        dotsInterpolateAmount.setBounds(20, 90, 240,20);
        sliderDotsInterpolateAmount.setBounds(20, 120, 300,50);
        startXLabel.setBounds(20, 180, 170, 20);
        endXLabel.setBounds(260, 180, 30, 20);
        startX.setBounds(200,180,50,20);
        endX.setBounds(280,180,50,20);
        interpolateButton.setBounds(20, 220, 180, 40);
        messageTypePickerLabel.setBounds(20, 270,300,20);
        functionPickerPanel.setBounds(0,0,400,300);
        functionPickerPanel.add(functionPicker);
        functionPickerPanel.add(buttonGroupPanel);
        functionPickerPanel.add(dotsInterpolateAmount);
        functionPickerPanel.add(sliderDotsInterpolateAmount);
        functionPickerPanel.add(startXLabel);
        functionPickerPanel.add(endXLabel);
        functionPickerPanel.add(startX);
        functionPickerPanel.add(endX);
        functionPickerPanel.add(interpolateButton);
        functionPickerPanel.add(messageTypePickerLabel);
        add(functionPickerPanel);

        //block scale
        JPanel scalePanel = new JPanel(null);
        JLabel scaleLabel = new JLabel("Choose area of graphic to draw:");
        JLabel startScaleXLabel = new JLabel("Min x:");
        JLabel endScaleXLabel = new JLabel("Max x:");
        JLabel startScaleYLabel = new JLabel("Min y:");
        JLabel endScaleYLabel = new JLabel("Max y:");
        JTextField startScaleX = new JTextField();
        JTextField endScaleX = new JTextField();
        JTextField startScaleY = new JTextField();
        JTextField endScaleY = new JTextField();
        JButton changeScaleButton = new JButton("Change scale");
        JButton resetScaleButton = new JButton("Reset scale");
        JLabel messageScaleLabel = new JLabel("");
        JRadioButton freezeScaleRadioButton = new JRadioButton("freeze scale", false);
        scaleLabel.setBounds(20, 20, 200, 20);
        startScaleXLabel.setBounds(20, 50, 50, 20);
        endScaleXLabel.setBounds(160, 50, 50, 20);
        startScaleYLabel.setBounds(20, 80, 50, 20);
        endScaleYLabel.setBounds(160, 80, 50, 20);
        startScaleX.setBounds(90,50,50,20);
        endScaleX.setBounds(230,50,50,20);
        startScaleY.setBounds(90,80,50,20);
        endScaleY.setBounds(230,80,50,20);
        changeScaleButton.setBounds(20,110,120,40);
        resetScaleButton.setBounds(160,110,120,40);
        messageScaleLabel.setBounds(20,180,380, 40);
        freezeScaleRadioButton.setBounds(20, 160, 120, 20);
        scalePanel.setBounds(0,320,400,200);
        scalePanel.add(scaleLabel);
        scalePanel.add(startScaleXLabel);
        scalePanel.add(endScaleXLabel);
        scalePanel.add(startScaleYLabel);
        scalePanel.add(endScaleYLabel);
        scalePanel.add(startScaleX);
        scalePanel.add(endScaleX);
        scalePanel.add(startScaleY);
        scalePanel.add(endScaleY);
        scalePanel.add(changeScaleButton);
        scalePanel.add(resetScaleButton);
        scalePanel.add(messageScaleLabel);
        scalePanel.add(freezeScaleRadioButton);
        add(scalePanel);

        //Panel for interpolation with new Dot
        JPanel changeDotPanel = new JPanel(null);
        JLabel titleChangeDotPanel = new JLabel("Change dot value");
        JLabel numberDotLabel = new JLabel("Choose dot index i = ");
        JTextField numberDot = new JTextField();
        JLabel currentDotLabel = new JLabel("Input dot index and press enter");
        JLabel newDotValueLabel = new JLabel("New value for y = ");
        JTextField newDotValue = new JTextField();
        JButton reInterpolateButton = new JButton("Replace");
        titleChangeDotPanel.setBounds(20,20,100,20);
        numberDotLabel.setBounds(20,55, 140,20);
        numberDot.setBounds(150, 55, 30,20);
        currentDotLabel.setBounds(20,85,300,20);
        newDotValueLabel.setBounds(20,115, 100, 20);
        newDotValue.setBounds(130,115,50,20);
        reInterpolateButton.setBounds(20,145,180,40);
        changeDotPanel.setBounds(0,560,400,200);
        changeDotPanel.add(newDotValue);
        changeDotPanel.add(newDotValueLabel);
        changeDotPanel.add(currentDotLabel);
        changeDotPanel.add(numberDot);
        changeDotPanel.add(numberDotLabel);
        changeDotPanel.add(titleChangeDotPanel);
        changeDotPanel.add(reInterpolateButton);
        add(changeDotPanel);
        repaint();


        // add action listeners
        changeScaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    minX=Double.parseDouble(startScaleX.getText());
                    maxX=Double.parseDouble(endScaleX.getText());
                    minY=Double.parseDouble(startScaleY.getText());
                    maxY=Double.parseDouble(endScaleY.getText());
                    if (minX>=maxX || minY>=maxY) throw new NumberFormatException();
                    functionGraph.setMinX(minX);
                    functionGraph.setMaxX(maxX);
                    functionGraph.setMinY(minY);
                    functionGraph.setMaxY(maxY);
                    messageScaleLabel.setText("");
                    functionGraph.repaint();
                } catch (Exception e1) {
                    messageScaleLabel.setText("You entered wrong coordinates. Please try again.");
                }

            }
        });
        resetScaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maxY = Collections.max(dots, new DotComparatorByY()).getY();
                minY = Collections.min(dots, new DotComparatorByY()).getY();
                maxX = Collections.max(dots, new DotComparatorByX()).getX();
                minX = Collections.min(dots, new DotComparatorByX()).getX();
                functionGraph.setMinX(minX);
                functionGraph.setMaxX(maxX);
                functionGraph.setMinY(minY);
                functionGraph.setMaxY(maxY);
                functionGraph.repaint();
            }
        });

        interpolateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            try {
            dots = new ArrayList<>();
            startXRange = Double.parseDouble(startX.getText());
            endXRange = Double.parseDouble(endX.getText());
            if (startXRange>=endXRange) throw new NumberFormatException();
            int dotsAmount = sliderDotsInterpolateAmount.getValue();
            functionType = FunctionType.getType(functionPickerButtonGroup.getSelection().getActionCommand());
            double xDotStep=(endXRange-startXRange)/(dotsAmount-1);
            switch (functionType) {
                case sinx: {
                    for (int i = 0; i < dotsAmount; i++) {
                        dots.add(new Dot(startXRange + xDotStep*i, Math.sin(startXRange + xDotStep*i)));
                    }
                } break;
                case x2:  {
                    for (int i = 0; i < dotsAmount; i++) {
                        dots.add(new Dot(startXRange + xDotStep*i, Math.pow(startXRange + xDotStep*i,2)));
                    }
                } break;
                case sqrtx: {
                    for (int i = 0; i < dotsAmount; i++) {
                        dots.add(new Dot(startXRange + xDotStep * i, Math.sqrt(startXRange + xDotStep * i)));
                    }
                }
            }
            interpolationFunction = new Interpolation(dots);
            interpolationFunction.calcCoef();
            if (!scaleFreezed) {
                maxY = Collections.max(dots, new DotComparatorByY()).getY();
                minY = Collections.min(dots, new DotComparatorByY()).getY();
                maxX = Collections.max(dots, new DotComparatorByX()).getX();
                minX = Collections.min(dots, new DotComparatorByX()).getX();
                functionGraph.setMinX(minX);
                functionGraph.setMaxX(maxX);
                functionGraph.setMinY(minY);
                functionGraph.setMaxY(maxY);
            }
            readyToDraw=true;
            numberDot.setText("");
            newDotValue.setText("");
            currentDotLabel.setText("Input dot index and press enter");
            messageTypePickerLabel.setText("");
            functionGraph.repaint();
        } catch (NumberFormatException e1) {
            e1.printStackTrace();
            messageTypePickerLabel.setText("You typed wrong X area. Try again.");
        }
        }
    });

        freezeScaleRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                scaleFreezed = freezeScaleRadioButton.isSelected();
            }
        });
        reInterpolateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int index = Integer.parseInt(numberDot.getText());
                    double newValue = Double.parseDouble(newDotValue.getText());
                    Dot dotReplacer = new Dot(dots.get(index).getX(), newValue);
                    dots.remove(index);
                    dots.add(index, dotReplacer);
                    currentDotLabel.setText("Dot replaced. Function is reinterpolated.");
                } catch (NumberFormatException e1) {}
                    interpolationFunction = new Interpolation(dots);
                    interpolationFunction.calcCoef();
                    numberDot.setText("");
                    newDotValue.setText("");
                    currentDotLabel.setText("Input dot index and press enter");
                    functionGraph.repaint();

            }
        });

        numberDot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int index = Integer.parseInt(numberDot.getText());
                    currentDotLabel.setText(String.format("Choosed dot (index %d) is x = %.2f and y = %.2f",index,dots.get(index).getX(),dots.get(index).getY()));
                } catch (NumberFormatException e1) {}
                            }
        });

    }

    enum FunctionType {
        sinx("Sin(x)"),
        x2("x^2"),
        sqrtx("Sqrt(x)");

        private String value;
        private FunctionType(String value) {this.value=value;}
        public static FunctionType getType(String value) {
            for (FunctionType x : FunctionType.values()) {
                if (x.value.equals(value)) return x;
            }
            return sinx;
        }

    }



}
