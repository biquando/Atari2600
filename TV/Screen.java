package Atari2600.TV;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A container with an image on which to draw pixels.
 * Starts a loop when run() that updates the image.
 */
class Screen extends JPanel implements Runnable {

    private int xScale, yScale;
    private BufferedImage img;
    private volatile boolean running = false;

    public Screen(int xScale, int yScale) {
        this.xScale = xScale;
        this.yScale = yScale;
        img = new BufferedImage(160 * xScale, 262 * yScale, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            // uses less resources than repaint()
            paintImmediately(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
    }

    /**
     * Draws a pixel to the image, upscaled by the scale member.
     *
     * @param x default resolution x-coordinate [0–159]
     * @param y default resolution y-coordinate [0–191]
     * @param rgb integer color value to draw (ex. 0xFF0000 for red)
     */
    public void setPixel(int x, int y, int rgb) {
        for (int i = x * xScale; i < x * xScale + xScale; i++) {
            for (int j = y * yScale; j < y * yScale + yScale; j++) {
                img.setRGB(i, j, rgb);
            }
        }
    }

    /**
     * Stops the loop that's updating the canvas.
     */
    public void stop() {
        running = false;
    }
}
