import java.awt.geom.Point2D;

public class Particle
{
    double x, y;

    public void Update(double dt, VectorFunction function)
    {
        Point2D.Double vec = function.Evaluate(x, y);
        x += vec.x * dt;
        y += vec.y * dt;
    }
}
