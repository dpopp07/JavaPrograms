import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.ArrayList;
import java.io.*;

public class Breakout {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600,500);
        frame.setTitle("Breakout");
        frame.setResizable(false);
        frame.add(new GamePanel());
        frame.setVisible(true);
	}

	private static class GamePanel extends JPanel {
		
		Ball ball;
		Paddle paddle;
		Brick brick;
		BrickConfiguration bconfig;
		PaddlePowerUp powerUp1;
		LifePowerUp powerUp2;
		LaserPowerUp powerUp3;
		ArrayList<Laser> lasers;
		Timer timer;
		Timer powerUpTimer;
		boolean gameOver;
		boolean ballGone;
		boolean ballInPlay;
		boolean levelOneComplete;
		boolean levelTwoComplete;
		boolean levelThreeComplete;
		boolean levelFourComplete;
		boolean paddleElongated;
		boolean readyToShoot;
		boolean gamePaused;
		boolean gameStarted;
		int score;
		int highScore;
		int lives;
		int level;
		int blocksCounter;
		int lengthCounter;
		int waitToShootCounter;
		int shotCounter;

		ImageIcon background;

		public GamePanel() {
			super();

			// call initializeGameObjects()
			initializeGameObjects();
			
			// add PaddleMover as a keyListener
			addKeyListener(new PaddleMover());

			setFocusable(true);		
		}

		public void initializeGameObjects() {
			// instantiate ball, paddle, and brick configuration
			paddle = new Paddle();
			ball = new Ball();
			bconfig = new BrickConfiguration(1);
			powerUp1 = new PaddlePowerUp(bconfig.getBrick(bconfig.getYPowerUp1(), bconfig.getXPowerUp1()).getX(), 
				bconfig.getBrick(bconfig.getYPowerUp1(), bconfig.getXPowerUp1()).getY());

			powerUp2 = new LifePowerUp(bconfig.getBrick(bconfig.getYPowerUp2(), bconfig.getXPowerUp2()).getX(), 
				bconfig.getBrick(bconfig.getYPowerUp2(), bconfig.getXPowerUp2()).getY());

			powerUp3 = new LaserPowerUp(bconfig.getBrick(bconfig.getYPowerUp3(), bconfig.getXPowerUp3()).getX(), 
				bconfig.getBrick(bconfig.getYPowerUp3(), bconfig.getXPowerUp3()).getY());

			lasers = new ArrayList<Laser>();
			gameOver = false;
			ballGone = false;
			ballInPlay = true;
			allPowerUpsFalse();
			gamePaused = false;
			gameStarted = false;
			allLevelsFalse();
			score = 0;
			lives = 3;
			level = 1;
			blocksCounter = 0;
			lengthCounter = 0;
			waitToShootCounter = 0;
			shotCounter = 0;
			background = new ImageIcon("brickwall.png");

			// set up timer to run GameMotion() every 10ms
			timer = new Timer(10, new GameMotion());
			timer.start();

			powerUpTimer = new Timer(1000, new PowerUpLength());
			powerUpTimer.start();
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			g2.drawImage(background.getImage(), 0, 0, 600, 500, null);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("Comic sans", Font.BOLD, 20));
			g2.drawString("Score: " + score, 15, 460);
			g2.drawString("Lives: " + lives, 500, 460); //187, 215, 245, 
			g2.drawString("Level " + level, 260, 460);

			if (gameStarted == false) {
				g2.drawString("Destroy All The Bricks In Each Level To Win The Game.", 17, 160);
				g2.drawString("Fail To Hit The Ball, And You Will Lose A Life.", 50, 187);
				g2.drawString("As Well As All Power Ups.", 150, 215);
				g2.drawString("Press P At Any Point To Pause The Game.", 110, 245);
				g2.drawString("Press The Spacebar to Shoot Lasers When Applicable.", 30, 297);
				g2.drawString("You Only Get 15 Shots So Use Them Wisely.", 65, 325);
				g2.drawString("Press The Left Or Right Arrow Key To Begin.", 60, 380);
			}

			if (ball.getY() + ball.getHeight() >= 500) {
				lives -= 1;
				ballGone = true;
				allPowerUpsFalse();
				endLevel();

				if (lives == 0) {
					g2.setColor(Color.BLACK);
					g2.setFont(new Font("Serif", Font.PLAIN, 30));
    				g2.drawString("Game Over!", 230, 190);
    				g2.drawString("Your Final Score Was " + score, 150, 300);
    				g2.drawString("Press The Spacebar To Play Again!", 100, 245);
    				g2.drawString("High Score:" + highScore, 200, 355);
   					timer.stop();
   					gameOver = true;
   				}
   				else {
   					Rectangle2D.Double rect = new Rectangle2D.Double(165, 200, 280, 80);
   					g2.setPaint(Color.BLUE);
   					g2.fill(rect);
   					g2.draw(rect);
   					g2.setFont(new Font("Serif", Font.BOLD, 15));
   					g2.setColor(Color.WHITE);
   					g2.drawString("Lives left: " + lives, 260, 220);
   					g2.drawString("Press The Left Or Right Key To Continue", 170, 250);
   				}
   			}

   			if (blocksCounter == bconfig.getRows()*bconfig.getCols() && levelOneComplete == false) {
   				levelOneComplete = true;
   				g2.drawString("Easy Right? Press The Left Or Right Key To Move On.", 25, 297);
   				endLevel();
   			}

   			if (levelOneComplete && levelTwoComplete == false && blocksCounter == 2*bconfig.getRows()*bconfig.getCols()) {
   				levelTwoComplete = true;
   				g2.drawString("Not Too Shabby.", 215, 297);
   				g2.drawString("Press The Left Or Right Key To Keep Going", 70, 325);
   				endLevel();
   			}

   			if (levelOneComplete && levelTwoComplete && levelThreeComplete == false 
   				&& blocksCounter == 3*bconfig.getRows()*bconfig.getCols()) {

   				levelThreeComplete = true;
   				g2.drawString("Well Done. Now It Gets Harder.", 140, 297);
   				g2.drawString("Press Right Or Left To Continue.", 138, 350);

   				endLevel();
   			}

   			if (levelOneComplete && levelTwoComplete && levelThreeComplete && levelFourComplete == false 
   				&& blocksCounter == 4*bconfig.getRows()*bconfig.getCols()) {

   				levelFourComplete = true;
   				g2.drawString("Good Luck. Press Right Or Left To Begin.", 75, 297);
   				endLevel();
   			}

   			if (levelOneComplete && levelTwoComplete && levelThreeComplete && levelFourComplete
   				&& blocksCounter == 5*bconfig.getRows()*bconfig.getCols()) {
   				g2.drawString("You Have Won The Game, Congratulations.", 75, 297);
   			}

			// paint ball, paddle, and brick configuration
			
   			bconfig.paint(g2);
			paddle.paint(g2);
			ball.paint(g2);
			powerUp1.paint(g2);
			powerUp2.paint(g2);
			powerUp3.paint(g2);
			for (int i = 0; i < lasers.size(); i++) {
				lasers.get(i).paint(g2);
			}
		}
			
			

		private class GameMotion implements ActionListener {
			public GameMotion() {

			}

			public void actionPerformed(ActionEvent evt) {
				//move ball automatically
				if (level == 5) {
					ball.move(4);
					powerUp1.move(2);
					powerUp2.move(2);
					powerUp3.move(2);
				}
				else if (level == 4) {
					ball.move(3);
					powerUp1.move(1);
					powerUp2.move(1);
					powerUp3.move(1);
				}
				else {
					ball.move(2);
					powerUp1.move(1);
					powerUp2.move(1);
					powerUp3.move(1);
				}
			
				//move paddle according to key press
				paddle.move();

				for (int i = 0; i < lasers.size(); i++) {
					lasers.get(i).move();
				}

				elongatePaddle();

				//check if the ball hits the paddle or a brick
				checkForHit();
				
				//call repaint
				repaint();
			}
		}

		private class PowerUpLength implements ActionListener {
			public PowerUpLength() {

			}

			public void actionPerformed(ActionEvent evt) {
				waitToShootCounter += 1;
				if (waitToShootCounter > 1) {
					powerUpTimer.stop();
				}
			}
		}


		private class PaddleMover implements KeyListener {
			public void keyPressed(KeyEvent evt) {

				int key = evt.getKeyCode();

				// change paddle speeds for left and right key presses
					if (key == KeyEvent.VK_LEFT) {
						if (gameStarted == false) {
							ball.setXspeed(-1);
							ball.setYspeed(-1);
							gameStarted = true;
						}
						else {
							paddle.setSpeed(-5);
						}
					}

					if (key == KeyEvent.VK_RIGHT) {
						if (gameStarted == false) {
							ball.setXspeed(1);
							ball.setYspeed(-1);
							gameStarted = true;
						}
						else {
							paddle.setSpeed(5);
						}
					}

				if (gamePaused == false && ballInPlay) {
					if (key == KeyEvent.VK_P) {
						gamePaused = true;
						timer.stop();
					}
				} else if (gamePaused) {
					if (key == KeyEvent.VK_P) {
						gamePaused = false;
						timer.start();
					}
				}

					//******CHEAT CODES --- FOR INSTRUCTOR USE ONLY*****
				if (key == KeyEvent.VK_Q) {
						lives += 1;
					}
				if (key == KeyEvent.VK_2) {
						blocksCounter = 80;
					}
				if (key == KeyEvent.VK_3) {
						blocksCounter = 160;
					}
				if (key == KeyEvent.VK_4) {
						blocksCounter = 240;
					}
				if (key == KeyEvent.VK_5) {
						blocksCounter = 320;
					}

					//************************

				if (ballGone && gameOver == false) {
						if (key == KeyEvent.VK_RIGHT) {
							continuePlay(1);
						}
						if (key == KeyEvent.VK_LEFT) {
							continuePlay(-1);
						}
					}
				if (gameOver) {
						if (key == KeyEvent.VK_SPACE) {
							restart();
						}
					}
				if (levelOneComplete && levelTwoComplete == false && gameOver == false 
					&& ballGone == false && ballInPlay == false) {
						if (key == KeyEvent.VK_RIGHT) {
							levelTwo(1);
						}
						if (key == KeyEvent.VK_LEFT) {
							levelTwo(-1);
						}
					}
				if (levelOneComplete && levelTwoComplete && levelThreeComplete == false 
					&& gameOver == false && ballGone == false && ballInPlay == false) {
						if (key == KeyEvent.VK_RIGHT) {
							levelThree(1);
						}
						if (key == KeyEvent.VK_LEFT) {
							levelThree(-1);
						}
					}
				if (levelOneComplete && levelTwoComplete && levelThreeComplete && levelFourComplete == false 
					&& gameOver == false && ballGone == false && ballInPlay == false) {
						if (key == KeyEvent.VK_RIGHT) {
							levelFour(1);
						}
						if (key == KeyEvent.VK_LEFT) {
							levelFour(-1);
						}
					}
				if (levelOneComplete && levelTwoComplete && levelThreeComplete && levelFourComplete 
					&& gameOver == false && ballGone == false && ballInPlay == false) {
						if (key == KeyEvent.VK_RIGHT) {
							levelFive(1);
						}
						if (key == KeyEvent.VK_LEFT) {
							levelFive(-1);
						}
					}
				if (readyToShoot && ballInPlay && ballGone == false && waitToShootCounter > 0) {
					if (key == KeyEvent.VK_SPACE) {
							lasers.add(new Laser(paddle.getX() + paddle.getWidth()/2, paddle.getY()-20));
							shotCounter += 1;
							if (shotCounter > 14) {
								readyToShoot = false;
							}
						}
					}
				}


			public void keyReleased(KeyEvent evt) {

				int key = evt.getKeyCode();

				//set paddle speed to 0
				if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_LEFT) {
					paddle.setSpeed(0);
				}
			}

			public void keyTyped(KeyEvent evt) {}
		}

		public void restart() {

			initializeGameObjects();
		}

		public void continuePlay(int direction) {

			paddle = new Paddle();
			ball = new Ball();
			ball.setYspeed(-1);
			ball.setXspeed(direction);
			ballGone = false;
			gameOver = false;
			ballInPlay = true;
			allPowerUpsFalse();
			gamePaused = false;

			timer = new Timer(10, new GameMotion());
			timer.start();

			powerUpTimer = new Timer(1000, new PowerUpLength());
			powerUpTimer.start();
		}

		public void levelTwo(int direction) {

			paddle = new Paddle();
			ball = new Ball();
			ball.setYspeed(-1);
			ball.setXspeed(direction);

			bconfig = new BrickConfiguration(2);
			powerUp1 = new PaddlePowerUp(bconfig.getBrick(bconfig.getYPowerUp1(), bconfig.getXPowerUp1()).getX(), 
				bconfig.getBrick(bconfig.getYPowerUp1(), bconfig.getXPowerUp1()).getY());

			powerUp2 = new LifePowerUp(bconfig.getBrick(bconfig.getYPowerUp2(), bconfig.getXPowerUp2()).getX(), 
				bconfig.getBrick(bconfig.getYPowerUp2(), bconfig.getXPowerUp2()).getY());

			powerUp3 = new LaserPowerUp(bconfig.getBrick(bconfig.getYPowerUp3(), bconfig.getXPowerUp3()).getX(), 
				bconfig.getBrick(bconfig.getYPowerUp3(), bconfig.getXPowerUp3()).getY());

			powerUp1.setVisible(false);
			powerUp2.setVisible(false);
			powerUp3.setVisible(false);

			timer = new Timer(10, new GameMotion());
			timer.start();

			powerUpTimer = new Timer(1000, new PowerUpLength());
			powerUpTimer.start();

			ballInPlay = true;
			level = 2;
		}

		public void levelThree(int direction) {

			paddle = new Paddle();
			ball = new Ball();
			ball.setYspeed(-1);
			ball.setXspeed(direction);

			bconfig = new BrickConfiguration(3);
			powerUp1 = new PaddlePowerUp(bconfig.getBrick(bconfig.getYPowerUp1(), bconfig.getXPowerUp1()).getX(), 
				bconfig.getBrick(bconfig.getYPowerUp1(), bconfig.getXPowerUp1()).getY());

			powerUp2 = new LifePowerUp(bconfig.getBrick(bconfig.getYPowerUp2(), bconfig.getXPowerUp2()).getX(), 
				bconfig.getBrick(bconfig.getYPowerUp2(), bconfig.getXPowerUp2()).getY());

			powerUp3 = new LaserPowerUp(bconfig.getBrick(bconfig.getYPowerUp3(), bconfig.getXPowerUp3()).getX(), 
				bconfig.getBrick(bconfig.getYPowerUp3(), bconfig.getXPowerUp3()).getY());

			powerUp1.setVisible(false);
			powerUp2.setVisible(false);
			powerUp3.setVisible(false);

			timer = new Timer(10, new GameMotion());
			timer.start();

			powerUpTimer = new Timer(1000, new PowerUpLength());
			powerUpTimer.start();

			ballInPlay = true;
			level = 3;
		}

		public void levelFour(int direction) {

			paddle = new Paddle();
			ball = new Ball();
			ball.setYspeed(-1);
			ball.setXspeed(direction);

			bconfig = new BrickConfiguration(4);
			powerUp1 = new PaddlePowerUp(bconfig.getBrick(bconfig.getYPowerUp1(), bconfig.getXPowerUp1()).getX(), 
				bconfig.getBrick(bconfig.getYPowerUp1(), bconfig.getXPowerUp1()).getY());

			powerUp2 = new LifePowerUp(bconfig.getBrick(bconfig.getYPowerUp2(), bconfig.getXPowerUp2()).getX(), 
				bconfig.getBrick(bconfig.getYPowerUp2(), bconfig.getXPowerUp2()).getY());

			powerUp3 = new LaserPowerUp(bconfig.getBrick(bconfig.getYPowerUp3(), bconfig.getXPowerUp3()).getX(), 
				bconfig.getBrick(bconfig.getYPowerUp3(), bconfig.getXPowerUp3()).getY());

			powerUp1.setVisible(false);
			powerUp2.setVisible(false);
			powerUp3.setVisible(false);

			timer = new Timer(10, new GameMotion());
			timer.start();

			powerUpTimer = new Timer(1000, new PowerUpLength());
			powerUpTimer.start();

			ballInPlay = true;
			level = 4;
		}

		public void levelFive(int direction) {

			paddle = new Paddle();
			ball = new Ball();
			ball.setYspeed(-1);
			ball.setXspeed(direction);

			bconfig = new BrickConfiguration(5);
			powerUp1 = new PaddlePowerUp(bconfig.getBrick(bconfig.getYPowerUp1(), bconfig.getXPowerUp1()).getX(),
				bconfig.getBrick(bconfig.getYPowerUp1(), bconfig.getXPowerUp1()).getY());

			powerUp2 = new LifePowerUp(bconfig.getBrick(bconfig.getYPowerUp2(), bconfig.getXPowerUp2()).getX(), 
				bconfig.getBrick(bconfig.getYPowerUp2(), bconfig.getXPowerUp2()).getY());

			powerUp3 = new LaserPowerUp(bconfig.getBrick(bconfig.getYPowerUp3(), bconfig.getXPowerUp3()).getX(), 
				bconfig.getBrick(bconfig.getYPowerUp3(), bconfig.getXPowerUp3()).getY());

			powerUp1.setVisible(false);
			powerUp2.setVisible(false);
			powerUp3.setVisible(false);

			timer = new Timer(10, new GameMotion());
			timer.start();

			powerUpTimer = new Timer(1000, new PowerUpLength());
			powerUpTimer.start();

			ballInPlay = true;
			level = 5;

			lives += 1;
		}

		public void setHighScore(int n) {
				highScore = n;
		}

		public int getHighScore() {
				return highScore;
		}

		public void increaseLives() {
			if (powerUp2.getVisible()) {
				lives += 1;
			}
		}

		public void elongatePaddle() {
			if (paddleElongated) {

				powerUpTimer.start();
				paddle.setWidth(100);

			}
			else if (paddleElongated == false) {

				lengthCounter = 0;
				paddle.setWidth(50);
			}
			if (lengthCounter > 4) {
				paddleElongated = false;
			}
		}

		public void allPowerUpsFalse() {
			paddleElongated = false;
			readyToShoot = false;
			powerUp1.setVisible(false);
			powerUp2.setVisible(false);
			powerUp3.setVisible(false);
		}

		public void allLevelsFalse() {
			levelOneComplete = false;
			levelTwoComplete = false;
			levelThreeComplete = false;
			levelFourComplete = false;
		}

		public void endLevel() {
			for (int i = 0; i < lasers.size(); i++) {
				lasers.get(i).setVisible(false);
			}
			ballInPlay = false;
			lengthCounter = 0;
			waitToShootCounter = 0;
			timer.stop();
			powerUpTimer.stop();
		}

		public void checkForHit() {
			
			// change ball speed when ball hits paddle
			if (ball.getShape().intersects(paddle.getShape())) {
				int leftSide = paddle.getX();
				int middleLeft = paddle.getX() + (int)(paddle.getWidth()/3);
				int middleRight = paddle.getX() + (int)(2*paddle.getWidth()/3);
				int rightSide = paddle.getX() + paddle.getWidth();

				if ((ball.getX() >= leftSide) && (ball.getX() < middleLeft)) {
					// change ball speed
					ball.setYspeed(-1);
					ball.setXspeed(-1);
				}
				if ((ball.getX() >= middleLeft) && (ball.getX() <= middleRight)) {
					// change ball speed
					ball.setYspeed(-1);
				}
				if ((ball.getX() > middleRight) && (ball.getX() <= rightSide)) {
					// change ball speed
					ball.setYspeed(-1);
					ball.setXspeed(1);
				}
				if (paddleElongated) {
					lengthCounter += 1;
				}
			}

			if (powerUp1.getShape().intersects(paddle.getShape())) {
				powerUp1.setVisible(false);
				paddleElongated = true;		
			}

			if (powerUp2.getShape().intersects(paddle.getShape())) {
				increaseLives();
				powerUp2.setVisible(false);
			}

			if (powerUp3.getShape().intersects(paddle.getShape())) {
				powerUp3.setVisible(false);
				readyToShoot = true;
				shotCounter = 0;
			}

			// change ball speed when ball hits brick
			for (int i = 0; i < bconfig.getRows(); i++) {
				for (int j = 0; j < bconfig.getCols(); j++) {
					if (bconfig.exists(i,j)) {
						if (ball.getShape().intersects(bconfig.getBrick(i,j).getShape())) {
							Point ballLeft = new Point((int)ball.getShape().getX(), (int)(ball.getShape().getY() + ball.getShape().getHeight()/2));
							Point ballRight = new Point((int)(ball.getShape().getX() + ball.getShape().getWidth()), (int)(ball.getShape().getY() + ball.getShape().getHeight()/2));
							Point ballTop = new Point((int)(ball.getShape().getX() + ball.getShape().getWidth()/2), (int)ball.getShape().getY());
							Point ballBottom = new Point((int)(ball.getShape().getX() + ball.getShape().getWidth()/2), (int)(ball.getShape().getY() + ball.getShape().getHeight()));
							if (bconfig.getBrick(i,j).getShape().contains(ballLeft)) {
								// change ball speed
								ball.setXspeed(1);
							}
							else if(bconfig.getBrick(i,j).getShape().contains(ballRight)) {
								// change ball speed
								ball.setXspeed(-1);
							}
							if (bconfig.getBrick(i,j).getShape().contains(ballTop)) {
								// change ball speed
								ball.setYspeed(1);
							}
							else if (bconfig.getBrick(i,j).getShape().contains(ballBottom)) {
								// change ball speed
								ball.setYspeed(-1);
							}

							if (bconfig.getChangeValue(i, j) == 0) {
								bconfig.removeBrick(i, j);
								blocksCounter += 1;
							}

							if (bconfig.getChangeValue(i, j) > 0) {
								bconfig.setChangeValue(i, j);
							}

							if (i == bconfig.getYPowerUp1() && j == bconfig.getXPowerUp1()) {
								powerUp1.setVisible(true);
							}

							if (i == bconfig.getYPowerUp2() && j == bconfig.getXPowerUp2()) {
								powerUp2.setVisible(true);
							}

							if (i == bconfig.getYPowerUp3() && j == bconfig.getXPowerUp3()) {
								powerUp3.setVisible(true);
							}

							score += 10;
							if (score > getHighScore()) {
								setHighScore(score);
							}
						}
					}
				}
			}
			// check if lasers hit bricks
			for (int i = 0; i < bconfig.getRows(); i++) {
				for (int j = 0; j < bconfig.getCols(); j++) {
					for (int k = 0; k < lasers.size(); k++) {
						if (bconfig.exists(i,j)) {
							if (lasers.get(k).getShape().intersects(bconfig.getBrick(i, j).getShape())) {
								if (bconfig.getChangeValue(i, j) == 0 && lasers.get(k).getVisible()) {
									bconfig.removeBrick(i, j);
									blocksCounter += 1;
								}

								if (bconfig.getChangeValue(i, j) > 0 && lasers.get(k).getVisible()) {
									bconfig.setChangeValue(i, j);
								}

								if (i == bconfig.getYPowerUp1() && j == bconfig.getXPowerUp1()) {
									powerUp1.setVisible(true);
								}

								if (i == bconfig.getYPowerUp2() && j == bconfig.getXPowerUp2()) {
									powerUp2.setVisible(true);
								}

								lasers.get(k).setVisible(false);
							}
						}
					}
				}
			}
		}
	}
}