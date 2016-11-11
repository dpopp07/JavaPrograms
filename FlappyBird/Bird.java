import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

public class Bird {

	//instance varaibles

	private int speed;
	private int acceleration;
	private int xPos;
	private int yPos;
	private int height;
	private int width;
	private ImageIcon img;

	//constructor

	public Bird() {
		speed = -15;
		acceleration = 1;

		img = new ImageIcon("bird.png");
		height = img.getIconHeight();
		width = img.getIconWidth();

		xPos = 200;
		yPos = 300 - height/2;
	}

	//methods

	public void move() {
		speed += acceleration;
		yPos += speed;
	}

	public void jump() {
		speed = -15;
	}

	public void paint(Graphics2D brush) {
		brush.drawImage(img.getImage(), xPos, yPos, null);
	}

	public Rectangle2D.Double getBounds() {
		return new Rectangle2D.Double(xPos, yPos, width, height);
	}

	/*
	public void reset() {
		acceleration = 1;
		speed = 2;
	}
	*/
}
