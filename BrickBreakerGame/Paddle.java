import java.awt.*;
import java.awt.geom.*;

public class Paddle extends ColorShape{
	
	// location and size variables
	private static int speed;
	private static int xPos;
	private static final int yPos = 425;
	private static int width = 50;
	private static final int height = 8;

	private static final Rectangle2D.Double shape = new Rectangle2D.Double(xPos,yPos,width,height);

	public Paddle() {
		super(shape);

		// set paddle color
		setFillColor(Color.BLACK);
		setBorderColor(Color.BLACK);

		// set paddle position and speed
		speed = 0;
		xPos = 300 - width/2;
	}

	public void move() {
		// move paddle
		xPos += speed;
		
		// stop the paddle from moving at the edges
		if (xPos + width >= 600) {
			xPos = 600 - width;
		}
		if (xPos <= 0) {
			xPos = 0;
		}
		
		// update shape
		shape.setRect(xPos, yPos, width, height);
	}

	public void setSpeed(int newSpeed) {
		speed = newSpeed;
	}

	public void setWidth(int newWidth) {
		width = newWidth;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getX() {
		return xPos;
	}

	public int getY() {
		return yPos;
	}

	public Rectangle2D.Double getShape() {
		return shape;
	}

}