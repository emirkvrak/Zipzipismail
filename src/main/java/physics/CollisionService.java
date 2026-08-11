package physics;

import java.awt.Rectangle;

public final class CollisionService {

    private CollisionService() {
    }

    public static boolean overlaps(Rectangle first, Rectangle second) {
        return first.intersects(second);
    }

    public static boolean circleIntersects(Rectangle rectangle, Rectangle circleBounds) {
        double centerX = circleBounds.getCenterX();
        double centerY = circleBounds.getCenterY();
        double radius = Math.min(circleBounds.width, circleBounds.height) / 2.0;

        double closestX = clamp(centerX, rectangle.getMinX(), rectangle.getMaxX());
        double closestY = clamp(centerY, rectangle.getMinY(), rectangle.getMaxY());
        double distanceX = centerX - closestX;
        double distanceY = centerY - closestY;
        return distanceX * distanceX + distanceY * distanceY <= radius * radius;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
