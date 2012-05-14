package a.star.project;

/**
 * Edge Implementation Class
 * 
 * @author Amine Elkhalsi <aminekhalsi@hotmail.com>
 */

public class Edge
{
    // The first vertex name
    private Vertex firstVertex;
    // The second vertex name
    private Vertex secondVertex;
    // The edge's weight
    private int weight;
    
    
    /*
     * Constructors
     */
    
    public Edge(Vertex firstVertex, Vertex secondVertex, int weight)
    {
        this.firstVertex = firstVertex;
        this.secondVertex = secondVertex;
        this.weight = weight;
    }
    
    public Edge()
    {
        this.firstVertex = new Vertex();
        this.secondVertex = new Vertex();
        this.weight = 0;
    }
    
    
    
    /*
     * @Overrides
     */

    // Without printing vertices' coordinates
    @Override
    public String toString()
    {
        return "(" + this.firstVertex.toString() + "," + this.secondVertex.toString() + ") : " + this.weight;
    }
    
    // With vertices' coordinates
    public String toString2()
    {
        return "(" + this.firstVertex.toString2() + "," + this.secondVertex.toString2() + ") : " + this.weight;
    }
    
    
    
    /*
     * Getters and Setters
     */

    public Vertex getFirstVertex()
    {
        return firstVertex;
    }

    public void setFirstVertex(Vertex firstVertex)
    {
        this.firstVertex = firstVertex;
    }

    public Vertex getSecondVertex()
    {
        return secondVertex;
    }

    public void setSecondVertex(Vertex secondVertex)
    {
        this.secondVertex = secondVertex;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
    }
}