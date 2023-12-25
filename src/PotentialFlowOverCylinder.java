import java.awt.*;
import java.awt.geom.Point2D;

public class PotentialFlowOverCylinder implements VectorFunction, Drawable
{
    public double x, y;
    public double u;
    public double r;
    public Color color;

    public PotentialFlowOverCylinder(double x, double y, double u, double r, Color color)
    {
        this.x = x;
        this.y = y;
        this.u = u;
        this.r = r;
        this.color = color;
    }

    public void draw(Graphics2D g)
    {
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 128));
        Fill.circle(g, new Point2D.Double(x, y), r);

        g.setColor(color);
        Draw.circle(g, new Point2D.Double(x, y), r);
    }

    public Point2D.Double Evaluate(double x, double y)
    {
        // radius of cylinder
        x -= this.x;
        y -= this.y;

        double x2 = x * x;
        double y2 = y * y;



        double r2 = r * r;

        double vx = 1.0 - r2 * (x2 - y2) / (x2 + y2) / (x2 + y2);
        double vy = -2.0 * r2 * x * y / (x2 + y2) / (x2 + y2);


        return new Point2D.Double(vx * u - u, vy * u);
    };
}
