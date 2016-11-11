import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

public class Wall {

	private int xPos;
	private int yPos;
	private int speed;
	private int height;
	private int width;
	private int gap;
	private Rectangle2D.Double wallBottom;
	private Rectangle2D.Double wallTop;

	public Wall(int n) {
		gap = 180;
		width = 45;
		height = 500;
		speed = -5;
		xPos = n;
		yPos = (int)(Math.random()*400) + 100;
		wallBottom = new Rectangle2D.Double(xPos, yPos, width, height);
		wallTop = new Rectangle2D.Double(xPos, (yPos - gap - height), width, height);

	}

	public void move() {
		xPos += speed;

		if (xPos <= 0) {
			xPos = 800;
			yPos = (int)(Math.random()*400) + 100;
		}

		wallBottom.setRect(xPos, yPos, width, height);
    	wallTop.setRect(xPos, (yPos - gap - height), width, height);
	}

	public Rectangle2D.Double getBottomBounds() {
    	return wallBottom;
	}

	public Rectangle2D.Double getTopBounds() {
		return wallTop;
	}

	public void paint(Graphics2D brush) {
		brush.setPaint(Color.GREEN);
		brush.fill(getBottomBounds());
		brush.fill(getTopBounds());
		brush.draw(getBottomBounds());
		brush.draw(getTopBounds());
	}
}
