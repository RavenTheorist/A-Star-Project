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
public final class Panel extends JPanel
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
            graph = new Graph("graph2.txt");
        }
        catch (IOException ex)
        {
            Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Calculating max and min for both X and Y coordinates of all vertices in order to adapt the JFrame's size
        this.maxXCoordinate = graph.getMaxXCoordinate();
        this.maxYCoordinate = graph.getMaxYCoordinate();
        
        int minXCoordinate = graph.getMinXCoordinate();
        int minYCoordinate = graph.getMinYCoordinate();
        
        // If the smallest vertex at the x coordinates is too far
        if (minXCoordinate > 50)
        {
            // Bring all the graph vertices closer at the x axis
            graph = Graph.bringVerticesCloserBy_X(graph, minXCoordinate - 50);
        }
        
        // If the smallest vertex at the y coordinates is too far
        if (minYCoordinate > 50)
        {
            // Bring all the graph vertices closer at the y axis
            graph = Graph.bringVerticesCloserBy_Y(graph, minYCoordinate - 50);
        }
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
        
        
        // Edges Visualization
        for (int i = 0; i < getGraph().getM(); i++)
        {
            // Extract the coordinates of both of the vertices that composes the edge
            int firstVertex_X = getGraph().getEdges().get(i).getFirstVertex().getX();
            int firstVertex_Y = getGraph().getEdges().get(i).getFirstVertex().getY();
            int secondVertex_X = getGraph().getEdges().get(i).getSecondVertex().getX();
            int secondVertex_Y = getGraph().getEdges().get(i).getSecondVertex().getY();
            
            
            
            // Draw a line between the two vertices
            g.setColor(Color.blue);
            g.drawLine(firstVertex_X + 25, firstVertex_Y + 25, secondVertex_X + 25, secondVertex_Y + 25);
            
            // Draw a rectangle at the middle of each edge
            g.setColor(Color.white);
            g.fillRect(((firstVertex_X - secondVertex_X) / 2) - 7, ((firstVertex_Y - secondVertex_Y) / 2) - 7, 14, 14);
            g.setColor(Color.red);
            g.drawRect(((firstVertex_X - secondVertex_X) / 2) - 7, ((firstVertex_Y - secondVertex_Y) / 2) - 7, 14, 14);
            
            /*// Print the name of the vertex at a position that is relative to the vertex coordinates (right in the center of the circle)
            g2d.setColor(Color.red);
            g2d.drawString(graph.getVertices().get(i).getName(), x + 14, y + 32);*/
        }
        
        // Vertices Visualization
        for (int i = 0; i < getGraph().getN(); i++)
        {
            // Extract vertice coordinates
            int x = getGraph().getVertices().get(i).getX();
            int y = getGraph().getVertices().get(i).getY();
            
            // Draw a circle at the extracted coordinates
            g.setColor(Color.gray);
            if ((i == 0) || (i == getGraph().getN() - 1))
                g.setColor(Color.pink);
            g.fillOval(x, y, 50, 50);
            g.setColor(Color.black);
            g.drawOval(x, y, 50, 50);
            
            // Print the name of the vertex at a position that is relative to the vertex coordinates (right in the center of the circle)
            g2d.setColor(Color.white);
            g2d.drawString(getGraph().getVertices().get(i).getName(), x + 14, y + 32);
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

    public Graph getGraph()
    {
        return graph;
    }

    public void setGraph(Graph graph)
    {
        this.graph = graph;
    }
}