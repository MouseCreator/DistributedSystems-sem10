package mouse.univ.color.gui;

import mouse.univ.color.ColoredVertex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class GraphVisualization {

    public BufferedImage visualize(GraphModel graphModel, ColorMap colorMap) {
        BufferedImage image = new BufferedImage(graphModel.getWidth(), graphModel.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = image.createGraphics();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, graphModel.getWidth(), graphModel.getHeight());

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));

            for (ColoredVertex vertex : graphModel.getColoredGraph().getAllVertices()) {
                Position from = graphModel.getPositionHashMap().get(vertex.getId());
                for (ColoredVertex neighbor : vertex.getNeighbors()) {
                    Position to = graphModel.getPositionHashMap().get(neighbor.getId());
                    g2.drawLine(from.getX(), from.getY(), to.getX(), to.getY());
                }

            }
            int radius = graphModel.getRadius();
            Set<ColoredVertex> vertices = graphModel.getColoredGraph().getAllVertices();
            for (ColoredVertex vertex : vertices) {
                Position position = graphModel.getPositionHashMap().get(vertex.getId());
                g2.setColor(colorMap.getColor(vertex.getColor()));
                g2.fillOval(
                        position.getX() - radius,
                        position.getY() - radius,
                        radius * 2,
                        radius * 2
                );
            }
        } finally {
            g2.dispose();
        }

        return image;
    }

    public void save(BufferedImage image, String filename) {
        try {
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image to " + filename, e);
        }
    }
}
