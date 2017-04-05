import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by alterG on 14.01.2017.
 */
public class FunctionGraph extends JPanel {
    int width;
    int height;
    int axisDividersAmount = 4;
    int scale = 70; // 70 px in 1 sm
    int userMouseX;
    int userMouseY;

    public void setToolsPanel(ToolsPanel toolsPanel) {
        this.toolsPanel = toolsPanel;
    }

    private ToolsPanel toolsPanel;
    ArrayList<Dot> dots;
    boolean syncFlag;
    //axis scale
    double maxY;
    double minY;
    double maxX;
    double minX;

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public FunctionGraph(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.width = width;
        this.height = height;
        this.setBackground(new Color(56, 54, 54));
        this.toolsPanel = toolsPanel;
        maxY = 1;
        minY = 0;
        maxX = 1;
        minX = 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graph) {
            Graphics2D g = (Graphics2D) graph;
            super.paintComponent(g);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.white);
            //draw axis
            g.drawLine(50, 30, 50, height - 30); //vertical
            g.drawLine(40, height - 40, width - 30, height - 40); //horizontal
            //draw horizontal arrow
            int[] dotX1 = {width - 40, width - 30, width - 40};
            int[] dotY1 = {height - 45, height - 40, height - 35};
            g.fillPolygon(dotX1, dotY1, dotX1.length);
            //draw vertical arrow
            int[] dotX2 = {45, 50, 55};
            int[] dotY2 = {40, 30, 40};
            g.fillPolygon(dotX2, dotY2, dotX2.length);
            //draw axis names
            g.drawString("x", width - 30, height - 25);
            g.drawString("y", 30, 30);
            //draw axis dividers
            for (int i = 1; i <= axisDividersAmount; i++) {
                g.drawLine(50 + i * (width - 120) / axisDividersAmount, height - 50, 50 + i * (width - 120) / axisDividersAmount, height - 30); //hori
                g.drawLine(40, height - 40 - i * (height - 120) / axisDividersAmount, 60, height - 40 - i * (height - 120) / axisDividersAmount); //vert
            }
            //draw axis scale
            double stepYAxis = (maxY - minY) / (axisDividersAmount);
            double stepXAxis = (maxX - minX) / (axisDividersAmount);
            for (int i = 0; i <= axisDividersAmount; i++) {
                g.drawString(String.format("%.1f", minX + stepXAxis * i), 40 + i * (width - 120) / axisDividersAmount, height - 10);
                g.drawString(String.format("%.2f", minY + stepYAxis * i), 10, height - 45 - i * (height - 120) / axisDividersAmount);
            }
        double stepXDraw = 0; //1px=stepXDraw X coords
        double stepYDraw = 0; //1px=stepXDraw Y coords

        if (toolsPanel.readyToDraw) {
            //draw original function
            g.setColor(Color.BLACK);
            stepXDraw = (maxX - minX) / (width - 120);
            stepYDraw = (maxY - minY) / (height - 120);
            int coordX, coordXNext, coordY, coordYNext;
            double currentY =0, currentX = 0, nextX = 0, nextY = 0;
            for (int i = 0; i < width - 80; i++) {
                currentX = minX + i * stepXDraw;
                nextX = currentX + stepXDraw;
                switch (toolsPanel.functionType) {
                    case sinx: {
                        currentY = Math.sin(currentX);
                        nextY = Math.sin(nextX);
                    } break;
                    case sqrtx: {
                        currentY = Math.sqrt(currentX);
                        nextY = Math.sqrt(nextX);
                    } break;
                    case x2: {
                        currentY = Math.pow(currentX,2);
                        nextY = Math.pow(nextX,2);
                    }
                }
                coordX = i;
                coordY = (int) ((currentY - minY) / stepYDraw);
                coordXNext = i + 1;
                coordYNext = (int) ((nextY - minY) / stepYDraw);
                g.drawLine(50 + coordX, height - 40 - coordY, 50 + coordXNext, height - 40 - coordYNext);
            }
            g.setColor(Color.white);
            //Draw interpolated function
            g.setColor(Color.blue);
            for (int i = 0; i < width - 80; i++) {
                currentX = minX + i * stepXDraw;
                currentY = toolsPanel.interpolationFunction.interpolate(currentX);
                nextX = currentX + stepXDraw;
                nextY = toolsPanel.interpolationFunction.interpolate(nextX);
                coordX = i;
                coordY = (int) ((currentY - minY) / stepYDraw);
                coordXNext = i + 1;
                coordYNext = (int) ((nextY - minY) / stepYDraw);
                g.drawLine(50 + coordX, height - 40 - coordY, 50 + coordXNext, height - 40 - coordYNext);
            }
            //draw splines
            g.setColor(Color.white);
            for (int i = 0; i < toolsPanel.dots.size(); i++) {
                g.fillOval((int) (50 - 4 + (toolsPanel.dots.get(i).getX() - minX) / stepXDraw), (int) (height - 40 - 4 - (toolsPanel.dots.get(i).getY() - minY) / stepYDraw), 8, 8);
            }
        }

            //Draw mouse coordinates
            g.drawLine(userMouseX, 0, userMouseX, height);
            g.drawLine(0, userMouseY, width, userMouseY);
            g.drawString(String.format("(%.2f; %.2f)", minX + (userMouseX - 50) * stepXDraw, minY + (height - 40 - userMouseY) * stepYDraw), userMouseX + 20, userMouseY - 20);
            //draw designation
            g.drawString("White - splines",630,30);
            g.drawString("Black - original function",630,45);
            g.drawString("Blue -interpolated function",630,60);

            syncFlag=false;

            this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {

                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    userMouseX = e.getX();
                    userMouseY = e.getY();
                    repaint();
                }
            });

           /* this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!syncFlag) {
                        double nowX =minX+(userMouseX-50)*stepXDraw;
                        double leftDx=(userMouseX-50)*stepXDraw;
                        double rightDx=maxX-minX-(userMouseX-50)*stepXDraw;
                        double maxDx=Math.max(Math.abs(leftDx),Math.abs(rightDx));
                        minX=nowX-(maxX-minX)/4;
                        maxX=nowX+(maxX-minX)/4;
                        System.out.println("-------------\nminX is "+minX);
                        System.out.println("maxX is "+maxX);
                        System.out.println("Dx is "+maxDx);
                        minY=Math.sin(minX);
                        maxY=Math.sin(maxX);
                        if (maxY<minY) {
                            double swapBuffer = maxY;
                            maxY=minY;
                            minY=maxY;
                        }
                        if (maxY-minY<0.01) maxY=minY+0.05;
                        syncFlag=true;
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {
                }

            });*/
        }
}