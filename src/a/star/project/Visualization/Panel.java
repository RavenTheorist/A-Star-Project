package a.star.project.Visualization;

import a.star.project.GraphImplementation.Edge;
import a.star.project.GraphImplementation.Graph;
import a.star.project.GraphImplementation.Vertex;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * Frame Main Content Panel Implementation
 * 
 * @author Amine Elkhalsi <aminekhalsi@hotmail.com>
 */
public final class Panel extends JPanel implements KeyListener
{
    /*
     * Panel Attributes
     */
    
    // The max X and Y vertices coordinates needs to be passed to the JFrame to make it adapt its size
    private int maxXCoordinate;
    private int maxYCoordinate;
    private String heuristicPanel;
    
    // The graph that we'll be working with
    private Graph graph = null;
    
    // Internat attribute
    boolean first = true;
    
    /*
     * Constructors
     */
    
    public Panel(String graphType, String heuristic)
    {
        heuristicPanel = heuristic;
        this.heuristicPanel = heuristic;
        // Add Key Listener to enable moving the graph using the arrow keys
        this.addKeyListener(this);
        
        // Creating graph form text file
        try
        {
            // Open the right type of graph occording to the content of the given graphType
            switch (graphType)
            {
                // Normal graphs type 1
                case "graph1":
                    graph = new Graph("graph1.txt");
                    break;
                // Normal graphs type 2
                case "graph2":
                    graph = new Graph("graph2.txt");
                    break;
                // Chessboard type chosen
                case "chessboard":
                    graph = new Graph();
                    break;
                // Maze type chosen
                case "maze":
                    graph = new Graph("labyrinthe.txt", 2);
                    break;
                // If anything else given, consider a normal graph
                default:
                    System.out.println("Graph type \"" + graphType + "\" is unknown. Considering normal graph type 1.");
                    graph = new Graph("graph1.txt");
                    break;
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.heuristicPanel = heuristic;
        
        // If it's a maze than we have to keep the source and terminal vertices taken from the file
        if (graphType.equals("maze"))
        {
            // First Call of A*
            graph.AStar(this.heuristicPanel, graph.getSource(), graph.getTerminals());
        }
        // If not, we specify the first created vertex as the source vertex and the last one as a terminal
        else
        {
            // Specifying terminals and source vertices
            ArrayList<Vertex> terminals = new ArrayList<>();
            terminals.add(this.graph.getVertices().get(graph.getVertices().size()-1));
            
            //Update own attributes by the constructed graph attributes
            this.graph.setHeuristic(heuristicPanel);
            this.graph.setSource(graph.getVertices().get(0));
            this.graph.setTerminals(terminals);
            
            // First Call of A*
            graph.AStar(this.heuristicPanel, graph.getVertices().get(0), terminals);
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
        first = false;
    }
    
    /*
     * @Overrides
     */
    
    // This is the paintComponent method that will be called each time a modification occurs in the panel
    @Override
    public void paintComponent(Graphics g)
    {
        // Initialize lists
        ArrayList<Edge> minimalPathEdges;
        minimalPathEdges = new ArrayList<>();
        
        // Panel's defaut parameters initialization
        g.setColor(Color.LIGHT_GRAY);
        // We cover the panel with a filled rectangle in each iteration, in order to avoid having trails, especially when we need to move the graph
        g.fillRect(0, 0, this.maxXCoordinate + 100, this.maxYCoordinate + 100);
        Graphics2D g2d = (Graphics2D)g;
        Font font = new Font("Arial Black", Font.PLAIN, 16);
        g2d.setFont(font);
        
        // We will calculate the total distance of the generated minimal path and store it in totalDistance
        int totalDistance = 0;
        
        // Take the minimalPath which is a set of vertices, and transform it into a set of related edges => minimalPathEdges
        for (int i = 0 ; i < this.getGraph().getMinimalPath().size() - 1 ; i++)
        {
            // Initialize two vertices, which will form the next edge
            Vertex firstV = this.graph.getMinimalPath().get(i);
            Vertex secondV = this.graph.getMinimalPath().get(i+1);
            // currentEdge is the edge under construction
            Edge currentEdge = new Edge();
            
            // We search an existing edge in the set of edges of the graph
            for (int j = 0 ; j < this.getGraph().getEdges().size() ; j++)
            {
                if ((this.graph.getEdges().get(j).getFirstVertex().getName().equals(firstV.getName())) && (this.graph.getEdges().get(j).getSecondVertex().getName().equals(secondV.getName())))
                {
                    // If exists, than pass it to currentEdge
                    currentEdge = new Edge(firstV, secondV, this.graph.getEdges().get(j).getWeight());
                }
                if ((this.graph.getEdges().get(j).getFirstVertex().getName().equals(secondV.getName())) && (this.graph.getEdges().get(j).getSecondVertex().getName().equals(firstV.getName())))
                {
                    // If exists in one way or another, than pass it to currentEdge
                    currentEdge = new Edge(firstV, secondV, this.graph.getEdges().get(j).getWeight());
                }
            }
            // Calculate the total distance of the minimal path
            totalDistance += currentEdge.getWeight();
            // Finally, add it to the minimalPathEdges set
            minimalPathEdges.add(currentEdge);
        }
        
        ///***** Calling AStar method with the appropriate parameters *****///
        if (first)
            graph.AStar(this.heuristicPanel, this.graph.getSource(), this.graph.getTerminals());
        
       
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
                
                // If the edge is part of the minimal path
                boolean isPartOfMinimalPathEdges = false;
                for (int k = 0; k < minimalPathEdges.size(); k++)
                {
                    if ((this.graph.getEdges().get(i).getFirstVertex().getName().equals(minimalPathEdges.get(k).getFirstVertex().getName())) && (this.graph.getEdges().get(i).getSecondVertex().getName().equals(minimalPathEdges.get(k).getSecondVertex().getName())))
                        isPartOfMinimalPathEdges = true;
                    if ((this.graph.getEdges().get(i).getFirstVertex().getName().equals(minimalPathEdges.get(k).getSecondVertex().getName())) && (this.graph.getEdges().get(i).getSecondVertex().getName().equals(minimalPathEdges.get(k).getFirstVertex().getName())))
                        isPartOfMinimalPathEdges = true;
                }
                
                // If the edge is part of the minimal path
                if (isPartOfMinimalPathEdges)
                {
                    // Draw a colored line between the two vertices
                    g.setColor(Color.red);
                    g.drawLine(firstVertex_X + 25, firstVertex_Y + 25, secondVertex_X + 25, secondVertex_Y + 25);
                }
            
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
            
            // If there is any source
            if(this.graph.getSource() != null)
            {
                // If it's a source or a terminal, than draw it in... pink ?
                if (this.graph.getVertices().get(i).getName().equals(this.graph.getSource().getName()))
                    g.setColor(Color.pink);
                // But! if it's a terminal vertex, then draw it in... orange ^^
                for (int j = 0 ; j < this.graph.getTerminals().size() ; j++)
                {
                    if(this.graph.getVertices().get(i).getName().equals(this.graph.getTerminals().get(j).getName()))
                        g.setColor(Color.ORANGE);
                }
            }
            
            g.fillOval(x, y, 50, 50);
            // ...with some circle decorations
            g.setColor(Color.blue);
            g.drawOval(x, y, 50, 50);
            // If the vertex is part of the minimal path
            for (int j = 0 ; j < this.graph.getMinimalPath().size() ; j++)
            {
                if (this.graph.getMinimalPath().get(j).getName().equals(this.graph.getVertices().get(i).getName()))
                {
                    g.setColor(Color.red);
                    g.drawOval(x, y, 50, 50);
                }
            }
            
            // Print the name of the vertex at a position that is relative to the vertex coordinates (right in the center of the circle)
            g2d.setColor(Color.white);
            g2d.drawString(this.graph.getVertices().get(i).getName(), x + 14, y + 32);
        }
        
        // Print the used heuristic
        
        g2d.setColor(Color.black);
        font = new Font("Arial", Font.BOLD, 16);
        g2d.setFont(font);
        
        String heuristicUsed = "Heuristic Used : ";
        switch (this.getHeuristicPanel())
        {
            case "euclidean":
                // If it's about an Euclidean heuristic, then print it
                heuristicUsed += this.getHeuristicPanel() + ".";
                break;
            case "manhattan":
                // If it's about a Manhattan heuristic, then print it
                heuristicUsed += this.getHeuristicPanel() + ".";
                break;
            default:
                // Else, print "None"
                heuristicUsed += "None.";
                break;
        }
        g2d.drawString(heuristicUsed, 5, 16);
        
        // Show source caption
        g.setColor(Color.black);
        g.drawRect(6, 20, 6, 6);
        g.setColor(Color.pink);
        g.fillRect(7, 21, 5, 5);
        font = new Font("Arial", Font.PLAIN, 10);
        g2d.setFont(font);
        g2d.setColor(Color.black);
        g2d.drawString("source vertex", 14, 26);
        
        // Show terminals caption
        g.setColor(Color.black);
        g.drawRect(6, 30, 6, 6);
        g.setColor(Color.orange);
        g.fillRect(7, 31, 5, 5);
        font = new Font("Arial", Font.PLAIN, 10);
        g2d.setFont(font);
        g2d.setColor(Color.black);
        g2d.drawString("terminal vertices", 14, 37);
        
        // Show minimal path caption
        g.setColor(Color.red);
        g.drawLine(6, 44, 10, 44);
        font = new Font("Arial", Font.PLAIN, 10);
        g2d.setFont(font);
        g2d.setColor(Color.black);
        g2d.drawString("minimal path", 14, 47);
        
        // If there is no possible path between the source and the terminal vertices
        if ((minimalPathEdges.isEmpty()) && (this.graph.getSource() != null))
        {
            font = new Font("Arial Black", Font.PLAIN, 16);
            g2d.setColor(Color.red);
            g2d.setFont(font);
            g2d.drawString("Warning : Path obstructed !", 7, 70);
        }
        else
        {
            font = new Font("Arial Black", Font.PLAIN, 16);
            Color c;
            c = Color.GREEN;
            c = c.darker();
            g2d.setColor(c);
            g2d.setFont(font);
            g2d.drawString("Total distance : " + totalDistance, 7, 70);
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
                this.graph.getVertices().get(i).setY(this.graph.getVertices().get(i).getY() - 5);
            }
            this.repaint();
        }
        
        // If the DOWN arrow key is pressed
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            for (int i = 0 ; i < this.graph.getN() ; i++)
            {
                this.graph.getVertices().get(i).setY(this.graph.getVertices().get(i).getY() + 5);
            }
            this.repaint();
        }
        
        // If the LEFT arrow key is pressed
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            for (int i = 0 ; i < this.graph.getN() ; i++)
            {
                this.graph.getVertices().get(i).setX(this.graph.getVertices().get(i).getX() - 5);
            }
            this.repaint();
        }
        
        // If the RIGHT arrow key is pressed
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            for (int i = 0 ; i < this.graph.getN() ; i++)
            {
                this.graph.getVertices().get(i).setX(this.graph.getVertices().get(i).getX() + 5);
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

    public String getHeuristicPanel() {
        return heuristicPanel;
    }

    public void setHeuristicPanel(String heuristicPanel) {
        this.heuristicPanel = heuristicPanel;
    }
}


/* Another implementation of the try block in the Panel constructor
     * try
        {
            // We will use this to verify the integers read from the user
            int readInteger = -1;
            // Open the right type of graph occording to the content of the given graphType
            switch (graphType)
            {
                // Normal graphs type 1
                case "graph1":
                    graph = new Graph("graph1.txt");
                    break;
                // Normal graphs type 2
                case "graph2":
                    graph = new Graph("graph2.txt");
                    break;
                // Chessboard type chosen
                case "chessboard":
                    graph = new Graph();
                    break;
                // Maze type chosen
                case "maze":
                    graph = new Graph("labyrinthe.txt", 2);
                    break;
            }
        }
     */