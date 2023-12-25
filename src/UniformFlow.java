import java.awt.geom.Point2D;

public class UniformFlow implements VectorFunction
{
    public double u;

    public UniformFlow(double u)
    {
        this.u = u;
    }

    public Point2D.Double Evaluate(double x, double y)
    {
        return new Point2D.Double(u, 0.0);
    };
}
