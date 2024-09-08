import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Graphics;


public class GamePanel extends JPanel implements KeyListener {

    // Decalre variables
    public GameShape rightPaddle = new GameShape();
    public GameShape leftPaddle = new GameShape();
    public GameShape ball = new GameShape();
    boolean playerIsOnRight;
    int paddleY = 0;
    int opponentPaddleY;
    int ballDimension;
    int height;
    int w;
    int h;

    // Constructor
    public GamePanel(boolean playerIsOnRight) {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        this.playerIsOnRight = playerIsOnRight;
    }

    // GETTERS AND SETTERS
    public int getPaddleY() {
        return paddleY;
    }

    public void setOpponentPaddleY(int y) {
        this.opponentPaddleY = y;
    }

    public void setBallDimension(int ballDimension) {
        this.ballDimension = ballDimension;
    }

    public int getBallDimension() {
        return ballDimension;
    }

    // I have no use for this but I must override
    @Override
	public void keyTyped(KeyEvent e) {
	
	}

    // Override KeyListener's method with instruction on what to do when a key is pressed
	@Override
	public void keyPressed(KeyEvent e) {
        // Get window height
        height = getHeight();
        
        // If up key is pressed update paddleY
		if (KeyEvent.getKeyText(e.getKeyCode()).equals("Up")) {

			if ((height / 100) < 2) {
                // If window height becomes too small only change paddleY by 2
                paddleY -= 2;
            } else {
                // If change paddleY by 1/25th of window height
                paddleY -= height / 25;
            }

            // Don't let the paddle go out of the window
            if ((height / 6) + paddleY > height) {
                paddleY = height - (height / 6);
            } else if (paddleY < 0) {
                paddleY = 0;
            }
        
        // If down key is pressed update paddleY
		} else if (KeyEvent.getKeyText(e.getKeyCode()).equals("Down")) {

            if ((height / 100) < 2) {
                // If window height becomes too small only change paddleY by 2
                paddleY += 2;
            } else {
                // If change paddleY by 1/25th of window height
                paddleY += height / 25;
            }
            
            // Don't let the paddle go out of the window
            if ((height / 6) + paddleY > height) {
                paddleY = height - (height / 6);
            } else if (paddleY < 0) {
                paddleY = 0;
            }
        }
	}

    // I have no use for this but I must override
	@Override
	public void keyReleased(KeyEvent e) {

	}

    // Override paint function with code that paints the game
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Get width and height of window to scale everything
        w = getWidth();
        h = getHeight();

        // Set paddle position and dimensions
        if (playerIsOnRight) {
            rightPaddle.setShape(w - (w / 15), paddleY, w / 60, h / 6);
            leftPaddle.setShape(w / 15, opponentPaddleY, w / 60, h / 6);
        } else {
            rightPaddle.setShape(w - (w / 15), opponentPaddleY, w / 60, h / 6);
            leftPaddle.setShape(w / 15, paddleY, w / 60, h / 6);
        }
            
        // Set ball to appropriate size and position
        ball.setDimensions(ballDimension, ballDimension);
            
        // Draw everything
        leftPaddle.draw(g);
        rightPaddle.draw(g);
        ball.draw(g);
    }
}