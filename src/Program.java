import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Program extends JPanel
{
    float scale = 50f;
    ArrayList<Particle> particles = new ArrayList<>();
    double u = 1.0;
    UniformFlow uniform = new UniformFlow(u);
    ArrayList<VectorFunction> functions = new ArrayList<>()
    {{
        add(uniform);

        Color[] colors = new Color[]
        {
            Color.RED, Color.GREEN
        };

        for (int i = 0; i < 10; i++)
        {
            Color color = colors[i % colors.length];
            double x = Math.random() * 16 - 8;
            double y = Math.random() * 16 - 8;
            double r = Math.random() * 0.5 + 0.5;

            add(new PotentialFlowOverCylinder(x, y, u, r, color));
        }
    }};

    VectorFunction combined = (x, y) ->
    {
        Point2D.Double sum = new Point2D.Double(0, 0);
        for (VectorFunction f : functions)
        {
            Point2D.Double vec = f.Evaluate(x, y);
            sum.x += vec.x;
            sum.y += vec.y;
        }
        return sum;
    };

    public static void main(String[] args)
    {
        Program panel = new Program();
        panel.setBackground(Color.BLACK);

        JFrame frame = new JFrame("Vector Field");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.add(panel);
        frame.setVisible(true);

        // Update loop
        while (true)
        {
            // Update particles
            for (Particle p : panel.particles)
                for (VectorFunction f : panel.functions)
                    p.Update(0.01, f);

            // Add particles
            if (Math.random() < 0.1)
            {
                int n = (int) (Math.random() * 10 + 1);
                for (int i = 0; i < n; i++)
                {
                    Particle p = new Particle();
                    p.x = Math.random() * 20 - 30;
                    p.y = Math.random() * 20 - 10;
                    panel.particles.add(p);
                }
            }

            // Remove particles
            for (int i = panel.particles.size() - 1; i >= 0; i--)
                if (panel.particles.get(i).x > 50)
                    panel.particles.remove(i);

            panel.repaint();

            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        // Prepare the graphics context
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(getWidth() / 2, getHeight() / 2);
        g2.scale(scale, scale);
        g2.rotate(Math.PI / 2);

        // Draw the combined flow field (arrows in a grid)
        g2.setStroke(new BasicStroke(1.5f / scale));
        g2.setColor(new Color(1f, 1f, 1f, 0.25f));
        Draw.vectorField
            (
                g2,
                new Rectangle2D.Double(-10, -10, 20, 20),
                new Point2D.Double(0.02, 0.02),
                combined
            );

        // Draw particles
        g2.setColor(Color.WHITE);
        for (Particle p : particles)
            Fill.circle(g2, new Point2D.Double(p.x, p.y), scale * 0.001);

        // Draw functions that are Drawable
        for (VectorFunction f : functions)
            if (f instanceof Drawable)
                ((Drawable) f).draw(g2);
    }
}
