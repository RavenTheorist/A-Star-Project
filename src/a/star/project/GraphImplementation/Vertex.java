package a.star.project.GraphImplementation;

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


    
    /*
     * Constructors
     */

    public Vertex()
    {
        this.name = "UnnamedVertex";
        this.x = 0;
        this.y = 0;
    }
    
    public Vertex(String name, int x, int y)
    {
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    
    
    /*
     * @Overrides
     */
    
    @Override
    public String toString()
    {
        return this.name;
    }
    
    // Shows vertices with their coordinates
    public String toString2()
    {
        return this.name + " " + this.getX() + " " + getY();
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
    
}
