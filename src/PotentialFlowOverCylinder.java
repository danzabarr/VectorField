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
        x -= this.x;
        y -= this.y;

        double x2 = x * x;
        double y2 = y * y;
        double r2 = r * r;

        double vx = u * (1.0 - r2 * (x2 - y2) / (x2 + y2) / (x2 + y2));
        double vy = u * (-2.0 * r2 * x * y / (x2 + y2) / (x2 + y2));

        // Equation for potential flow over a cylinder:
        //    (1 - r^2 (x^2 - y^2) / (x^2 + y^2)^2, -2 r^2 x y / (x^2 + y^2)^2)
        //   mathbf V(x,y)=U\,\left(\frac{1 - R^2 (x^2 - y^2)}{(x^2 + y^2)^2}\,\mathbf i+\frac{-2R^2xy}{(x^2 + y^2)^2})\,\mathbf j\right)

        // We also need to subtract the uniform flow from the cylinder flow.
        // This is because the uniform flow will be added to the combined flow of the field.
        return new Point2D.Double(vx - u, vy);
    };
}
