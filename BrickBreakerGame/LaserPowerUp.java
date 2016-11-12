import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

public class LaserPowerUp extends PowerUp {

	public LaserPowerUp(int x, int y) {

		super();

		speed = 0;
		visible = false;
		xPos = x;
		yPos = y;
		height = 25;
		width = 30;
		img = new ImageIcon("images/laserbeam.png");
	}
}