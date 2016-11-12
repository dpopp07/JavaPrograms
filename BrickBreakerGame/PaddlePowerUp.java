import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

public class PaddlePowerUp  extends PowerUp {

	public PaddlePowerUp(int x, int y) {

		super();

		speed = 0;
		visible = false;
		xPos = x;
		yPos = y;
		height = 25;
		width = 30;
		img = new ImageIcon("images/arrow.png");
	}
}