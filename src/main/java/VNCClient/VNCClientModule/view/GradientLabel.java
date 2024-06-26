package VNCClient.VNCClientModule.view;

import javax.swing.*;
import java.awt.*;

public class GradientLabel extends JLabel {
    private Color startColor;
    private Color endColor;

    public GradientLabel(String text, Color startColor, Color endColor) {
        super(text);
        this.startColor = startColor;
        this.endColor = endColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradientPaint = new GradientPaint(0, 0, startColor, getWidth(), 0, endColor);
        g2d.setPaint(gradientPaint);
        FontMetrics fm = g2d.getFontMetrics();
        int textHeight = fm.getHeight();
        int textAscent = fm.getAscent();
        int yCoordinate = (getHeight() - textHeight)/2 + textAscent;
        g2d.drawString(getText(), 0, yCoordinate);
        g2d.dispose();
    }
}
