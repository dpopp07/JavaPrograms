import java.awt.geom.*;

public class Brick extends ColorShape {
		private int xPos = 0;
		private int yPos = 0;
		private int width = 0;
		private int height = 0;
		private Rectangle2D.Double shape;

		// constructor
		public Brick(int x, int y, int w, int h) {
			super(new Rectangle2D.Double(x, y, w, h));

			//set brick x, y, width, and height
			xPos = x;
			yPos = y;
			width = w;
			height = h;

			//initialize shape --> shape = new Rectangle2D somethin somethin
			shape = new Rectangle2D.Double(xPos, yPos, width, height);
	
			// update shape
			shape.setRect(xPos, yPos, width, height);
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