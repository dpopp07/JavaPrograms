import java.awt.*;
import java.awt.geom.*;

public class BrickConfiguration {
	
	//location and size variables
	private static final int xStart = 40;
	private static final int yStart = 20;	
	private static final int numCols = 10;
	private static final int numRows = 8;
	private static final int brickHeight = 10;
	private static final int brickWidth = 45;
	private static int xPowerUp1;
	private static int yPowerUp1;
	private static int xPowerUp2;
	private static int yPowerUp2;
	private static int xPowerUp3;
	private static int yPowerUp3;

	// 2D array containing brick objects
	private static Brick[][] bricks = new Brick[numCols][numRows];

	// 2D array telling us whether the brick should be painted (has it been hit?)
	private static boolean[][] paintBricks = new boolean[numCols][numRows];
	private static int[][] changeBricks = new int[numCols][numRows];
	
	// constructor
	public BrickConfiguration(int level) {
		
		// create new bricks and store them in bricks array
		for (int i = 0; i < numCols; i++) {
			for (int j = 0; j < numRows; j++) {
				// initialize paintBricks[i][j]
				paintBricks[i][j] = true;

				//Paint different configs for different levels
				if (level == 1) {
					changeBricks[i][j] = 0;
				}
				if (level == 2) {
					if (j < numRows/2) {
						changeBricks[i][j] = 1;
					}
					else {
						changeBricks[i][j] = 0;
					}
				}
				if (level == 3) {
					if (j < numRows/4) {
						changeBricks[i][j] = 2;
					}
					else if (j < 3*(numRows/4) && j >= numRows/4) {
							changeBricks[i][j] = 1;
					}
					else {
						changeBricks[i][j] = 0;
					}
				}
				if (level == 4) {
					if (j < numRows/4) {
						changeBricks[i][j] = 3;
					}
					else if (j < numRows/2 && j >= numRows/4) {
						changeBricks[i][j] = 2;
					}
					else if (j < 3*(numRows/4) && j >= numRows/2) {
						changeBricks[i][j] = 1;
					}
					else {
						changeBricks[i][j] = 0;
					}
				}
				if (level == 5) {
					if (j < numRows/4) {
						changeBricks[i][j] = 4;
					}
					else if (j < 3*(numRows/4) && j >= numRows/4) {
							changeBricks[i][j] = 3;
					}
					else {
						changeBricks[i][j] = 2;
					}
				}

				// initialize bricks[i][j]
				bricks[i][j] = new Brick(xStart + 50*i, yStart + 15*j, brickWidth, brickHeight);
			}
		}

		//randomize power up blocks
		xPowerUp1 = (int)(Math.random()*numCols);
		yPowerUp1 = (int)(Math.random()*numRows);
		xPowerUp2 = (int)(Math.random()*numCols);
		yPowerUp2 = (int)(Math.random()*numRows);
		xPowerUp3 = (int)(Math.random()*numCols);
		yPowerUp3 = (int)(Math.random()*numRows);

		//make sure they arent the same blocks
		while (xPowerUp1 == xPowerUp2 && yPowerUp1 == yPowerUp2) {
			xPowerUp1 = (int)(Math.random()*numCols);
			yPowerUp1 = (int)(Math.random()*numRows);
		}
		while (xPowerUp1 == xPowerUp3 && yPowerUp1 == yPowerUp3) {
			xPowerUp3 = (int)(Math.random()*numCols);
			yPowerUp3 = (int)(Math.random()*numRows);
		}
		while (xPowerUp2 == xPowerUp3 && yPowerUp3 == yPowerUp2) {
			xPowerUp2 = (int)(Math.random()*numCols);
			yPowerUp2 = (int)(Math.random()*numRows);
		}
	}

	// paint the bricks array to the screen
	public void paint(Graphics2D brush) {
		for (int i = 0; i < numCols; i++) {
			for (int j = 0; j < numRows; j++) {
				// determine if brick should be painted
				if (paintBricks[i][j]) {
					if (i == xPowerUp1 && j == yPowerUp1 
						|| i == xPowerUp2 && j == yPowerUp2 
						|| i == xPowerUp3 && j == yPowerUp3) {
						changeBricks[i][j] = 0;
						paintBrick(bricks[i][j], brush, new Color(0, 200, 200));
					}
					else {
						if (changeBricks[i][j] == 0) {
							// if so, call paintBrick()
							paintBrick(bricks[i][j], brush, new Color(0, 200, 0));
						}
						if (changeBricks[i][j] == 1) {
							paintBrick(bricks[i][j], brush, new Color(0, 150, 0));
						}
						if (changeBricks[i][j] == 2) {
							paintBrick(bricks[i][j], brush, new Color(0, 100, 0));
						}
						if (changeBricks[i][j] == 3) {
							paintBrick(bricks[i][j], brush, new Color(0, 50, 0));
						}
						if (changeBricks[i][j] == 4) {
							paintBrick(bricks[i][j], brush, new Color(0, 50, 50));
						}
					}
				}
			}
		}
	}

	// paint an individual brick
	public void paintBrick(Brick brick, Graphics2D brush, Color color) {

			brush.setPaint(color);
			brick.setFillColor(color);
			brick.setBorderColor(color);
			brick.paint(brush);
	}

	public void removeBrick(int row, int col) {
		// update paintBricks array for destroyed brick
		paintBricks[col][row] = false;
	}

	public Brick getBrick(int row, int col) {
		return bricks[col][row];
	}

	public boolean exists(int row, int col) {
		return paintBricks[col][row];
	}

	public int getChangeValue(int row, int col) {
		return changeBricks[col][row];
	}

	public void setChangeValue(int row, int col) {
		changeBricks[col][row] -=1;
	}

	public int getRows() {
		return numRows;
	}

	public int getCols() {
		return numCols;
	}

	public int getXPowerUp1() {
		return xPowerUp1;
	}

	public int getYPowerUp1() {
		return yPowerUp1;
	}

	public int getXPowerUp2() {
		return xPowerUp2;
	}

	public int getYPowerUp2() {
		return yPowerUp2;
	}

	public int getXPowerUp3() {
		return xPowerUp3;
	}

	public int getYPowerUp3() {
		return yPowerUp3;
	}
}