import java.awt.*;
import java.awt.geom.*;

public abstract class ColorShape{
	protected RectangularShape shape;
	private Color fillColor;
	private Color borderColor;

	public ColorShape(RectangularShape s) {
		shape = s;
	}

	public void setLocation(double x, double y) {
		shape.setFrame(x, y, shape.getWidth(), shape.getHeight());
	}

	public void setSize(double w, double h) {
		shape.setFrame(shape.getX(), shape.getY(), w, h);
	}

	public void setFillColor(Color color) {
		fillColor = color;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setBorderColor(Color color) {
		borderColor = color;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void paint(Graphics2D brush) {
		brush.setColor(borderColor);
		brush.draw(shape);
		brush.setColor(fillColor);
		brush.fill(shape);
	}

}