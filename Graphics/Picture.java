import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Graphics;

public class Picture extends JPanel{
    // Declare class members
    private BufferedImage picture;

    // Constructor
    public Picture(String fileName) {
        // Save image specified by fileName to picture variable
        try {
            picture = ImageIO.read(new File(fileName));
        } catch (Exception e) {
            // Print error if there is one
            System.out.println(e);
        }
    }

    // Override panel's paint function to keep drawing image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the image
        g.drawImage(picture, 0, 0, this);
    }
}