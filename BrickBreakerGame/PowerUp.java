import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public abstract class PowerUp {
	
	protected int speed;
	protected int xPos;
	protected int yPos;
	protected int height;
	protected int width;
	protected ImageIcon img;
	protected boolean visible;

	public PowerUp() {

	}

	public void setVisible(boolean x) {
		visible = x;
	}

	public boolean getVisible() {
		return visible;
	}

	public void move(int i) {
		if (visible) {
			speed = i;
		}

		yPos += speed;
	}

	public void paint(Graphics2D brush) {
		if (visible) {
			brush.drawImage(img.getImage(), xPos, yPos, width, height, null);
		}
	}

	public Rectangle2D.Double getShape() {
		return new Rectangle2D.Double(xPos, yPos, width, height);
	}
}