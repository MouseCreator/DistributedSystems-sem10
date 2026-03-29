package mouse.univ.color;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class ColoredVertex {
    private int id;
    private int color;
    private final List<ColoredVertex> neighbors;

    public void addNeighbor(ColoredVertex v) {
        if (neighbors.contains(v)) {
            return;
        }
        this.neighbors.add(v);
        v.neighbors.add(this);
    }

    public ColoredVertex(int id, int color) {
        this.id = id;
        this.color = color;
        neighbors = new ArrayList<>();
    }

    public List<ColoredVertex> getNeighbors() {
        return new ArrayList<>(neighbors);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ColoredVertex that = (ColoredVertex) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
