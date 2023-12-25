import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Draw
{

    public static void scalarField(Graphics2D g, Rectangle2D.Double bounds, Point2D.Double resolution, ScalarFunction function)
    {
        Color color = g.getColor();
        Color negative = Color.RED;
        double x = bounds.x;
        double y = bounds.y;
        double width = bounds.width;
        double height = bounds.height;

        double dx = width * resolution.x;
        double dy = height * resolution.y;

        for (double i = x; i < x + width; i += dx)
        {
            for (double j = y; j < y + height; j += dy)
            {
                double value = function.Evaluate(i, j);
                double length = Math.abs(value);

                if (length > 1)
                    value *= 1 / length;

                Color c = value < 0 ? negative : color;

                // fill a rectangle
                float r = c.getRed() / 255f;
                float g_ = c.getGreen() / 255f;
                float b = c.getBlue() / 255f;
                float a = (float) Math.abs(value);
                g.setColor(new Color(r, g_, b, a));
                Rectangle2D.Double rect = new Rectangle2D.Double(i, j, dx, dy);
                g.fill(rect);
            }
        }
        g.setColor(color);
    }

    public static void vectorField(Graphics2D g, Rectangle2D.Double bounds, Point2D.Double resolution, VectorFunction function)
    {
        double x = bounds.x;
        double y = bounds.y;
        double width = bounds.width;
        double height = bounds.height;

        double dx = width * resolution.x;
        double dy = height * resolution.y;

        for (double i = x; i < x + width; i += dx)
        {
            for (double j = y; j < y + height; j += dy)
            {
                Point2D.Double start = new Point2D.Double(i, j);
                Point2D.Double vec = function.Evaluate(i, j);

                double length = Point2D.Double.distance(0, 0, vec.x, vec.y);

                if (length > dx)
                {
                    vec.x *= dx / length;
                    vec.y *= dy / length;
                }

                arrow(g, start, vec, 0.1);
            }
        }
    }

    public static void circle(Graphics2D g, Point2D.Double center, double radius)
    {
        Ellipse2D.Double circle = new Ellipse2D.Double(center.x - radius, center.y - radius, radius * 2, radius * 2);
        g.draw(circle);
    }

    public static void vector(Graphics2D g, Point2D.Double origin, Point2D.Double vector)
    {
        Line2D.Double line = new Line2D.Double(origin.x, origin.y, origin.x + vector.x, origin.y + vector.y);
        g.draw(line);
    }

    public static void arrow(Graphics2D g, Point2D.Double origin, Point2D.Double vector, double arrowHeadSize)
    {
        Line2D.Double line = new Line2D.Double(origin.x, origin.y, origin.x + vector.x, origin.y + vector.y);
        g.draw(line);

        double angle = Math.atan2(vector.y, vector.x) + Math.PI;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        Point2D.Double arrowHeadPoint1 = new Point2D.Double(
            (line.x2 + (arrowHeadSize * cos) - (arrowHeadSize / 2.0 * sin)),
            (line.y2 + (arrowHeadSize * sin) + (arrowHeadSize / 2.0 * cos)));

        Point2D.Double arrowHeadPoint2 = new Point2D.Double(
            (line.x2 + (arrowHeadSize * cos) + (arrowHeadSize / 2.0 * sin)),
            (line.y2 + (arrowHeadSize * sin) - (arrowHeadSize / 2.0 * cos)));

        Line2D.Double arrowHead1 = new Line2D.Double(line.x2, line.y2, arrowHeadPoint1.x, arrowHeadPoint1.y);
        Line2D.Double arrowHead2 = new Line2D.Double(line.x2, line.y2, arrowHeadPoint2.x, arrowHeadPoint2.y);

        g.draw(arrowHead1);
        g.draw(arrowHead2);
    }
}
