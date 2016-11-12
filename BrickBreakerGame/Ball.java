import java.awt.geom.*;
import java.awt.*;

public class Ball extends ColorShape {
	
	// location and size variables
	private int xPos;
	private int yPos;
	private int xSpeed;
	private int ySpeed;
	private static final int height = 10;
	private static final int width = 10;
	private static final Ellipse2D.Double shape = new Ellipse2D.Double(300 - width/2, 425+height, width, height);

	// constructor
	public Ball() {
		super(shape);

		// set ball color
		super.setFillColor(Color.BLUE);
		super.setBorderColor(Color.BLUE);
		
		// set initial values for x and y position and speed
		xPos = 300 - width/2;
		yPos = 420-height;
		xSpeed = 0;
		ySpeed = 0;
	}

	public void move(int i) {
		// move ball
		xPos += i*xSpeed;
		yPos += i*ySpeed;
		
		// detect if ball should bounce off an edge
		if (xPos <= 0) {
			setXspeed(1);
		}

		if (xPos + width >= 600) {
			setXspeed(-1);
		}

		if (yPos <= 0) {
			setYspeed(1);
		}

		if (yPos + height >= 500) {
			setYspeed(-1);
		}

		// update shape to new values
		shape.setFrame(xPos, yPos, width, height);
	}

	public void setXspeed(int newSpeed) {
		xSpeed = newSpeed;
	}

	public void setYspeed(int newSpeed) {
		ySpeed = newSpeed;
	}

	public int getX() {
		return xPos;
	}

	public int getY() {
		return yPos;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Ellipse2D.Double getShape() {
		return shape;
	}
}
