package a.star.project.GraphImplementation;

import java.util.ArrayList;

/**
 * Vertex Implementation Class
 * 
 * @author Amine Elkhalsi <aminekhalsi@hotmail.com>
 */

public class Vertex
{
    // The vertex name
    private String name;
    // The x coordinate
    private int x;
    // The y coordinate
    private int y;
    // List of the neighbors of the vertex
    private ArrayList<Vertex> neighbors;


    
    /*
     * Constructors
     */

    public Vertex()
    {
        this.name = "UnnamedVertex";
        this.x = 0;
        this.y = 0;
        this.neighbors = null;
    }
    
    public Vertex(String name, int x, int y)
    {
        this.name = name;
        this.x = x;
        this.y = y;
        this.neighbors = null;
    }
    
    public Vertex(String name, int x, int y, ArrayList<Vertex> neighbors)
    {
        this.name = name;
        this.x = x;
        this.y = y;
        this.neighbors = neighbors;
    }
    
    public Vertex(Vertex vertex)
    {
        this.name = vertex.getName();
        this.x = vertex.getX();
        this.y = vertex.getY();
        this.neighbors = vertex.getNeighbors();
    }
    
    
    
    /*
     * @Overrides
     */
    
    @Override
    public String toString()
    {
        return this.name + " " + this.getX() + " " + getY();
    }
    
    // Shows vertices without their coordinates
    public String toString2()
    {
        return this.name;
    }
    
    // Shows vertices with their neighbors (without coordinates)
    public String toString3()
    {
        String result = this.name + " : [";
        for (int i = 0; i < this.getNeighbors().size(); i++)
        {
            result += this.getNeighbors().get(i).getName();
            if (i != this.getNeighbors().size() - 1)
                result += ", ";
            else
                result += "]";
        }
        
        return result;
    }
    
    
    /*
     * Useful methods
     */
    
    public void addNeighbor(Vertex neighbor)
    {
        this.neighbors.add(new Vertex(neighbor));
    }
    
    
    
    /*
     * Getters and Setters
     */
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public ArrayList<Vertex> getNeighbors()
    {
        return neighbors;
    }

    public void setNeighbors(ArrayList<Vertex> neighbors)
    {
        this.neighbors = neighbors;
    }
}
