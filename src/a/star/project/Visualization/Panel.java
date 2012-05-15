package a.star.project.Visualization;

import a.star.project.GraphImplementation.Graph;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Frame Main Content Panel Implementation
 * 
 * @author Amine Elkhalsi <aminekhalsi@hotmail.com>
 */
public final class Panel extends JPanel implements KeyListener
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
        // Add Key Listener to enable moving the graph using the arrow keys
        this.addKeyListener(this);
        
        // Creating graph form text file
        try
        {
            graph = new Graph("graph1.txt");
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
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, this.maxXCoordinate + 100, this.maxYCoordinate + 100);
        Graphics2D g2d = (Graphics2D)g;
        Font font = new Font("Arial Black", Font.PLAIN, 16);
        g2d.setFont(font);
        
        
        
        // Edges Visualization
        for (int i = 0; i < this.graph.getM(); i++)
        {
            // Extract the coordinates of both of the vertices that composes the edge
            int firstVertex_X = this.graph.getEdges().get(i).getFirstVertex().getX();
            int firstVertex_Y = this.graph.getEdges().get(i).getFirstVertex().getY();
            int secondVertex_X = this.graph.getEdges().get(i).getSecondVertex().getX();
            int secondVertex_Y = this.graph.getEdges().get(i).getSecondVertex().getY();
            
            
            
            // Draw a line between the two vertices
            g.setColor(Color.blue);
            g.drawLine(firstVertex_X + 25, firstVertex_Y + 25, secondVertex_X + 25, secondVertex_Y + 25);
            
            // Calculate the middle of the edge
            int middleX = (firstVertex_X + secondVertex_X) / 2;
            int middleY = (firstVertex_Y + secondVertex_Y) / 2;
            
            // Draw a rectangle at the middle of each edge
            g.setColor(Color.white);
            g.fillRect(middleX + 16, middleY + 16, 17, 17);
            g2d.setColor(Color.getHSBColor(163, 73, 164));
            g.setColor(Color.red);
            g.drawRect(middleX + 16, middleY + 16, 17, 17);
            
            // Print the weight of the edge right in the center of the previously created rectangle
            g2d.setColor(Color.getHSBColor(163, 73, 164));
            font = new Font("Arial Black", Font.ITALIC, 16);
            g2d.setFont(font);
            g2d.drawString(String.valueOf(this.graph.getEdges().get(i).getWeight()), middleX + 19, middleY + 31);
        }
        
        font = new Font("Arial Black", Font.PLAIN, 16);
        g2d.setFont(font);
        
        // Vertices Visualization
        for (int i = 0; i < this.graph.getN(); i++)
        {
            // Extract vertice coordinates
            int x = this.graph.getVertices().get(i).getX();
            int y = this.graph.getVertices().get(i).getY();
            
            // Draw a circle at the extracted coordinates
            g.setColor(Color.gray);
            if ((i == 0) || (i == this.graph.getN() - 1))
                g.setColor(Color.pink);
            g.fillOval(x, y, 50, 50);
            // ...with some circle decorations
            g.setColor(Color.blue);
            g.drawOval(x, y, 50, 50);
            
            // Print the name of the vertex at a position that is relative to the vertex coordinates (right in the center of the circle)
            g2d.setColor(Color.white);
            g2d.drawString(this.graph.getVertices().get(i).getName(), x + 14, y + 32);
        }
    }
    
    
    
    /*
     * @ Implementations' Overrides
     */
    
    @Override
    public void keyTyped(KeyEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        // If the UP arrow key is pressed
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            for (int i = 0 ; i < this.graph.getN() ; i++)
            {
                this.graph.getVertices().get(i).setY(this.graph.getVertices().get(i).getY() - 2);
            }
            this.repaint();
        }
        
        // If the DOWN arrow key is pressed
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            for (int i = 0 ; i < this.graph.getN() ; i++)
            {
                this.graph.getVertices().get(i).setY(this.graph.getVertices().get(i).getY() + 2);
            }
            this.repaint();
        }
        
        // If the LEFT arrow key is pressed
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            for (int i = 0 ; i < this.graph.getN() ; i++)
            {
                this.graph.getVertices().get(i).setX(this.graph.getVertices().get(i).getX() - 2);
            }
            this.repaint();
        }
        
        // If the RIGHT arrow key is pressed
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            for (int i = 0 ; i < this.graph.getN() ; i++)
            {
                this.graph.getVertices().get(i).setX(this.graph.getVertices().get(i).getX() + 2);
            }
            this.repaint();
        }
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
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