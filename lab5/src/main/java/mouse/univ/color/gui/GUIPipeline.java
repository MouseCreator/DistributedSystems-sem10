package mouse.univ.color.gui;

import lombok.extern.log4j.Log4j2;
import mouse.univ.color.ColoredGraph;
import mouse.univ.color.Coloring;

import java.awt.image.BufferedImage;

@Log4j2
public class GUIPipeline {
    public static void run(int width, int height, int numVertices, Coloring coloring) {
        GraphModelGenerator graphModelGenerator = new GraphModelGenerator(width, height);
        GraphVisualization visualization = new GraphVisualization();
        ColorMap colorMap = new HVSColorMap();

        GraphModel initialModel = graphModelGenerator.generate(numVertices);
        BufferedImage beforeImage = visualization.visualize(initialModel, colorMap);
        visualization.save(beforeImage, "before.png");

        GraphModel afterModel = initialModel.duplicate();
        ColoredGraph minimized = coloring.minimize(afterModel.getColoredGraph());
        afterModel.setColoredGraph(minimized);
        int maxColor = minimized.maxColor();
        log.info("Total colors: {}", maxColor + 1);
        BufferedImage afterImage = visualization.visualize(afterModel, colorMap);
        visualization.save(afterImage, "after.png");
    }
}
