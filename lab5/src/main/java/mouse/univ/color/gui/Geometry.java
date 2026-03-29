package mouse.univ.color.gui;

public class Geometry {
    public static double distancePoints(Position p1, Position p2) {
        double dx = Math.abs(p1.getX() - p2.getX());
        double dy = Math.abs(p1.getY() - p2.getY());
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static class LineParams {
        double a;
        double b;
        double c;
        public LineParams(Position s1, Position s2) {
            double x1 = s1.getX();
            double x2 = s2.getX();

            double y1 = s1.getY();
            double y2 = s2.getY();
            a = y2 - y1;
            b = x1 - x2;
            c = y1 * (x2 - x1) - (y2 - y1) * x1;
        }
    }

    public static double distancePointToSegment(Position point, Position s1, Position s2) {
        double px = point.getX();
        double py = point.getY();
        double x1 = s1.getX();
        double y1 = s1.getY();
        double x2 = s2.getX();
        double y2 = s2.getY();

        double dx = x2 - x1;
        double dy = y2 - y1;

        if (dx == 0 && dy == 0) {
            return Math.sqrt((px - x1) * (px - x1) + (py - y1) * (py - y1));
        }

        double t = ((px - x1) * dx + (py - y1) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));

        double closestX = x1 + t * dx;
        double closestY = y1 + t * dy;

        double distX = px - closestX;
        double distY = py - closestY;

        return Math.sqrt(distX * distX + distY * distY);
    }

    public static boolean segmentIntersect(Position a1, Position a2, Position b1, Position b2) {
        LineParams l1 = new LineParams(a1, a2);
        LineParams l2 = new LineParams(b1, b2);
        double denominator = (l1.a * l2.b - l2.a * l1.b);
        if (Math.abs(denominator) < 0.00001) {
            return false;
        }
        double interceptX = (l1.b * l2.c - l2.b * l1.c) / denominator;
        double interceptY = (l1.c * l2.a - l2.c * l1.a) / denominator;

        boolean withinA = withinSegment(interceptX, interceptY, a1, a2);
        boolean withinB = withinSegment(interceptX, interceptY, b1, b2);

        return withinA && withinB;
    }

    private static boolean withinSegment(double interceptX, double interceptY, Position s1, Position s2) {
        int minX = Math.min(s1.getX(), s2.getX());
        int maxX = Math.max(s1.getX(), s2.getX());

        int minY = Math.min(s1.getY(), s2.getY());
        int maxY = Math.max(s1.getY(), s2.getY());

        return withinCoordsUnstrict(minX, interceptX, maxX) && withinCoordsUnstrict(minY, interceptY, maxY);
    }

    private static boolean withinCoordsUnstrict(int low, double target, int high) {
        return target - low > 0.25 && high - target > 0.25;
    }
}
