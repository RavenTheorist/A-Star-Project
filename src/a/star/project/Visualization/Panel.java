package a.star.project.Visualization;

import a.star.project.GraphImplementation.Graph;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Frame Main Content Panel Implementation
 * 
 * @author Amine Elkhalsi <aminekhalsi@hotmail.com>
 */
public class Panel extends JPanel
{
    // The max X and Y vertices coordinates needs to be passed to the JFrame to make it adapt its size
    private int maxXCoordinate;
    private int maxYCoordinate;
    
    // The graph that we'll be working with
    private Graph graph = null;
    
    
    
    /*
     * Constructors
     */
    
    public Panel()
    {
        try
        {
            graph = new Graph("graph1.txt");
        }
        catch (IOException ex)
        {
            Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Calculating max X and Y coordinates of all vertices in order to adapt the JFrame's size
        this.maxXCoordinate = graph.getMaxXCoordinate();
        this.maxYCoordinate = graph.getMaxYCoordinate();
    }
    
    
    
    /*
     * @Overrides
     */
    
    @Override
    public void paintComponent(Graphics g)
    {
        // Panel's defaut parameters initialization
        this.setBackground(Color.darkGray);
        Graphics2D g2d = (Graphics2D)g;
        Font font = new Font("Arial Black", Font.PLAIN, 16);
        g2d.setFont(font);
        
        
        // Vertices Visualization
        for (int i = 0; i < graph.getN(); i++)
        {
            // Extract vertice coordinates
            int x = graph.getVertices().get(i).getX();
            int y = graph.getVertices().get(i).getY();
            
            // Draw a circle at the extracted coordinates
            g.setColor(Color.black);
            g.fillOval(x, y, 50, 50);
            
            // Print the name of the vertex at a position that is relative to the vertex coordinates (right in the center of the circle)
            g2d.setColor(Color.red);
            g2d.drawString(graph.getVertices().get(i).getName(), x + 14, y + 32);
        }
    }
    
    
    
    /*
     * Getters and Setters
     */

    public int getMaxXCoordinate()
    {
        return maxXCoordinate;
    }

    public void setMaxXCoordinate(int maxXCoordinate)
    {
        this.maxXCoordinate = maxXCoordinate;
    }

    public int getMaxYCoordinate()
    {
        return maxYCoordinate;
    }

    public void setMaxYCoordinate(int maxYCoordinate)
    {
        this.maxYCoordinate = maxYCoordinate;
    }
    
    
}