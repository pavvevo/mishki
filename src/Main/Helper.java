package Main;

import java.awt.*;
import java.awt.font.GlyphVector;

public class Helper {

    public double lerp(double a, double b, double f) {
        return (a * (1.0 - f)) + (b * f);
    }

    public void drawTextOutine(Graphics2D g2d, String text, int x, int y, Color fillColor, Color outlineColor, int outlineWidth) {
        Color originalColor = g2d.getColor();
        Stroke originalStroke = g2d.getStroke();

        // create a glyph vector from your text
        GlyphVector glyphVector = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), text);
        // get the shape object
        Shape textShape = glyphVector.getOutline();

        g2d.translate(x, y);

        g2d.setColor(outlineColor);
        g2d.setStroke(new BasicStroke(outlineWidth));
        g2d.draw(textShape); // draw outline
        g2d.setColor(fillColor);
        g2d.fill(textShape);
        g2d.setColor(originalColor);
        g2d.setStroke(originalStroke);

        g2d.translate(-x, -y);
    }
}
