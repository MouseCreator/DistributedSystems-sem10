package mouse.univ.color.gui;

import java.awt.*;

public class HVSColorMap implements ColorMap {

    @Override
    public Color getColor(int id) {
        int numHueSteps = 10;
        double hueStep = 0.3; // 0.0 -> 0.3 -> 0.6 -> 0.9 -> 0.2 -> 0.5 -> 0.8 -> 0.1 -> 0.4 -> 0.7
        int cycle = id / numHueSteps;
        float hue = (float) ((id * hueStep) % 1.0);

        float decrease = cycle * 0.05f;

        float saturation = Math.max(0f, 1f - decrease);
        float brightness = Math.max(0f, 1f - decrease);

        return Color.getHSBColor(hue, saturation, brightness);
    }
}
