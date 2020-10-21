package Atari2600.TV;

import javax.swing.*;
import java.awt.*;

/**
 * Provides a window for the TIA to write pixels to.
 *
 * This class stores a 192x160 matrix of integer color values, and updates
 * the Screen whenever those values are changed by the TIA.
 */
public class CRT extends JFrame {

    private Screen screen;
    private int[][] pixels;
    private int scanline, cycle, currColor, frame;
    public boolean vsync, vblank;

    public CRT(int xScale, int yScale) {
        // Create the screen
        screen = new Screen(xScale, yScale);
        screen.setPreferredSize(new Dimension(160 * xScale, 262 * yScale));
        add(screen);

        pixels = new int[262][160];

        // Set up the window
        pack();
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Atari2600");
        setVisible(true);

        // Start the screen update loop on a new thread
        Thread screenThread = new Thread(screen, "crt-thread");
        screenThread.start();
    }

    /**
     * Increments the current position by one cycle (or moves to the next scanline).
     * If the current position is on the screen, the pixel is drawn.
     */
    public synchronized void step() {
        if (scanline < 262 && cycle > 67) {
            if (pixels[scanline][cycle - 68] != currColor) {
                if (vblank) currColor = 0;
                pixels[scanline][cycle - 68] = currColor;
                screen.setPixel(cycle - 68, scanline, currColor);
            }
        }

        if (++cycle == 228) {
            scanline++;
            cycle = 0;
        }
        if (vsync && scanline > 3) {
            scanline = 0;
            frame++;
        }
    }

    /**
     * Sets the color of the next pixel drawn.
     *
     * @param color int hash value (rgb) of the color to set
     */
    public void setColor(int color) {
        currColor = color;
    }

    public int getScanline() {
        return scanline;
    }

    public int getCycle() { return cycle; }

    public int getFrame() {
        return frame;
    }
}
