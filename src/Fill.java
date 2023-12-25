import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Fill
{

    public static void circle(Graphics2D g, Point2D.Double center, double radius)
    {
        Ellipse2D.Double circle = new Ellipse2D.Double(center.x - radius, center.y - radius, radius * 2, radius * 2);
        g.fill(circle);
    }

}
