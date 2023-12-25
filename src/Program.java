import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Program extends JPanel implements MouseMotionListener
{
    float scale = 50f;

    Point2D.Double mouse = new Point2D.Double(0, 0);
    ArrayList<Particle> particles = new ArrayList<>();
    double u = 1.0;
    UniformFlow uniform = new UniformFlow(u);
    ArrayList<VectorFunction> functions = new ArrayList<>() {{
        add(uniform);

        Color[] colors = new Color[] {
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
        JFrame frame = new JFrame("Vector Field");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        Program panel = new Program();
        panel.addMouseMotionListener(panel);
        panel.setBackground(Color.BLACK);
        frame.add(panel);
        frame.setVisible(true);

        // add 100 random particles

        while (true)
        {
            for (Particle p : panel.particles)
                for (VectorFunction f : panel.functions)
                    p.Update(0.01, f);

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

            for (int i = 0; i < panel.particles.size(); i++)
            {
                Particle p = panel.particles.get(i);
              if (p.x > 50)
              {
                  panel.particles.remove(i);
                    i--;
              }
            }

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
    public void mouseDragged(java.awt.event.MouseEvent e)
    {
        Point p = e.getPoint();
        mouse.x = (p.x - getWidth() / 2) / scale;
        mouse.y = (p.y - getHeight() / 2) / scale;
        repaint();
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e)
    {
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(getWidth() / 2, getHeight() / 2);
        g2.scale(scale, scale);
        g2.rotate(Math.PI / 2);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1.5f / scale));


        g2.setColor(new Color(1f, 1f, 1f, 0.25f));
        Draw.vectorField
        (
            g2,
            new Rectangle2D.Double(-10, -10, 20, 20),
            new Point2D.Double(0.02, 0.02),
            combined
        );

        g2.setColor(Color.WHITE);
        for (Particle p : particles)
            Fill.circle(g2, new Point2D.Double(p.x, p.y), scale * 0.001);

        for (VectorFunction f : functions)
            if (f instanceof Drawable)
                ((Drawable) f).draw(g2);
    }
}
