import java.awt.*;

public class GameShape {
    // Variable representing shape shape
    public Rectangle shape = new Rectangle(0, 0, 0, 0);
    Rectangle prevShape = new Rectangle(0, 0, 0, 0);

    // Method to modify shape position and dimensions
    public void setShape(int x, int y, int width, int height) {
        // Save current shape before modification
        this.prevShape = this.shape;

        this.shape.x = x;
        this.shape.y = y;
        this.shape.width = width;
        this.shape.height = height;
    }

    // Method to modify shape position
    public void setPosition(int x, int y) {
        // Save current shape before modification
        this.prevShape = this.shape;

        this.shape.x = x;
        this.shape.y = y;
    }

    // Method to modify shape dimensions
    public void setDimensions(int width, int height) {
        // Save current shape before modification
        this.prevShape = this.shape;

        this.shape.width = width;
        this.shape.height = height;
    }

    public void draw(Graphics g) {
        // Erase previous drawing of the shape
        g.setColor(Color.BLACK);
        g.fillRect(prevShape.x, prevShape.y, prevShape.width, prevShape.height);

        // Draw shape at new position
        g.setColor(Color.WHITE);
        g.fillRect(shape.x, shape.y, shape.width, shape.height);
    }
    
}
