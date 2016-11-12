import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

public class Laser {

	private Rectangle2D.Double shape;
	int speed;
	int xPos;
	int yPos;
	int height;
	int width;
	boolean visible;

	public Laser(int x, int y) {

		speed = -2;
		xPos = x;
		yPos = y;
		height = 15;
		width = 5;
		visible = true;
		shape = new Rectangle2D.Double(xPos, yPos, width, height);
	}

	public void setVisible(boolean x) {
		visible = x;
	}

	public boolean getVisible() {
		return visible;
	}

	public void move() {
		if (visible) {
		yPos += speed;
		shape.setRect(xPos, yPos, width, height);
		}
	}

	public void paint(Graphics2D brush) {
		if (visible) {
			brush.setColor(Color.ORANGE);
			brush.draw(shape);
			brush.fill(shape);
		}
	}

	public Rectangle2D.Double getShape() {
		return shape;
	}
}