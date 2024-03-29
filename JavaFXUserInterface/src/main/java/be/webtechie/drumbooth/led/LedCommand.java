package be.webtechie.drumbooth.led;

import javafx.scene.paint.Color;

/**
 * LedCommand as it is exchanged with the Arduino.
 */
public class LedCommand {
    private final LedEffect ledEffect;
    private final int speed;
    private final Color color1;
    private final Color color2;

    /**
     * Initialize a {@link LedCommand} with speed and colors.
     *
     * @param ledEffect {@link LedEffect}
     * @param speed The speed value
     * @param color1 {@link Color}
     * @param color2 {@link Color}
     */
    public LedCommand(LedEffect ledEffect, int speed, Color color1, Color color2) {
        this.ledEffect = ledEffect;
        this.speed = speed;
        this.color1 = color1;
        this.color2 = color2;
    }

    /**
     * Start-up and exit state for LED strips.
     *
     * @return Static LedEffect with dimmed color.
     */
    public static LedCommand getInitialState() {
        return new LedCommand(LedEffect.STATIC, 0, Color.color(0.1,0.1,0.1), null);
    }

    /**
     * Convert to a ":"-separated String to be exchanged with the Arduino.
     *
     * @return The command as ":"-separated String
     */
    public String toCommandString() {
        return this.ledEffect.getId() + ":"
                + speed + ":"
                + Math.round(color1.getRed() * 255) + ":"
                + Math.round(color1.getGreen() * 255) + ":"
                + Math.round(color1.getBlue() * 255) + ":"
                + Math.round(color2.getRed() * 255) + ":"
                + Math.round(color2.getGreen() * 255) + ":"
                + Math.round(color2.getBlue() * 255);
    }

    public LedEffect getLedEffect() {
        return ledEffect;
    }

    public int getSpeed() {
        return speed;
    }

    public Color getColor1() {
        return color1;
    }

    public Color getColor2() {
        return color2;
    }

    public String getColor1AsHexString() {
        return "#" + color1.toString().substring(2, 8);
    }

    public String getColor2AsHexString() {
        return "#" + color2.toString().substring(2, 8);
    }
}
