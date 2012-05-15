package a.star.project.GraphImplementation;

import java.io.*;
import java.util.ArrayList;

/**
 * Graph Implementation Class
 * 
 * @author Amine Elkhalsi <aminekhalsi@hotmail.com>
 */

public final class Graph
{
    // Vertices' List
    private ArrayList<Vertex> vertices;
    // Edges' List
    private ArrayList<Edge> edges;
    // Number of vertices
    private int n;
    // Number of edges
    private int m;
    // Minimal Path (A*)
    private ArrayList<Vertex> minimalPath;
    
    
    
    /*
     * Constructors
     */
    
    public Graph(ArrayList<Vertex> vertices, ArrayList<Edge> edges)
    {
        this.vertices = vertices;
        this.edges = edges;
        this.n = vertices.size();
        this.m = edges.size();
    }
    
    public Graph(String fileName) throws IOException
    {
        // Extract the graph from the file
        Graph tempGraph = fileToGraph(fileName);
        
        this.vertices = tempGraph.getVertices();
        this.edges = tempGraph.getEdges();
        this.n = tempGraph.getN();
        this.m = tempGraph.getM();
        this.minimalPath = AStar(this.vertices.get(0), this.getVertices().get(this.getVertices().size()-1));
        
        System.out.println("Minimal Path :");
        
        for (int i = 0 ; i < this.minimalPath.size() ; i++)
        {
                System.out.println(this.minimalPath.get(i).getName());
                
        }
            
    }
    
    
    public Graph(Graph graph)
    {
        this.vertices = graph.getVertices();
        this.edges = graph.getEdges();
        this.n = graph.getN();
        this.m = graph.getM();
    }
    
    
    
    /*
     * A* Methods
     */
    
    public ArrayList<Vertex> AStar(Vertex source, Vertex target)
    {
        // Objects Initialization
        Vertex s = source;
        Vertex t = target;
        ArrayList<Vertex> Open;
        ArrayList<Vertex> Close;
        
        // Initialization
        Open = new ArrayList<>();
        Close = new ArrayList<>();
        Open.add(new Vertex(s));
        s.setG(0);
        s.setF(s.getH());
        System.out.println("g(s) : " + s.getF());
        
        while (!Open.isEmpty())
        {
            System.out.println("\nOpen : ");
            for (int i = 0 ; i < Open.size() ; i++)
            {
                System.out.println(Open.get(i).getName() + ",");
            }
            System.out.println("\nClose : ");
            for (int i = 0 ; i < Close.size() ; i++)
            {
                System.out.print(Close.get(i).getName() + ",");
            }
            System.out.println("\n");
            
            // Extract x the vertex with the minimal f
            int minimalF = Open.get(0).getG();
            Vertex x = Open.get(0);
            for (int i = 1 ; i < Open.size() ; i++)
            {
                if (minimalF > Open.get(i).getG())
                {
                    minimalF = Open.get(i).getG();
                    x = Open.get(i);
                }
            }
            
            for (int i = 1 ; i < Open.size() ; i++)
            {
                if (Open.get(i).getName().equals(x.getName()))
                    Open.remove(i);
            }
            
            //System.out.println("minimalV : " + x);
            
            Close.add(new Vertex(x));
            
            if (x.getName().equals(target.getName()))
            {
                return Close;
            }
            else
            {
                for (Vertex y : x.getNeighbors())
                {
                    boolean containsY = false;
                    for (int i = 0 ; i < Open.size() ; i++)
                    {
                        if (Open.get(i).getName().equals(y.getName()))
                            containsY = true;
                    }
                    for (int i = 0 ; i < Close.size() ; i++)
                    {
                        if (Close.get(i).getName().equals(y.getName()))
                            containsY = true;
                    }
                    
                    int costXY = 0;
                    for (int i = 0 ; i < this.getEdges().size() ; i++)
                    {
                        if (((this.getEdges().get(i).getFirstVertex().getName().equals(x.getName())) && (this.getEdges().get(i).getSecondVertex().getName().equals(y.getName()))) || ((this.getEdges().get(i).getFirstVertex().getName().equals(y.getName())) && (this.getEdges().get(i).getSecondVertex().getName().equals(x.getName()))))
                            costXY = this.getEdges().get(i).getWeight();
                        //System.out.println("COST : " + costXY);
                    }
                    if ((!containsY) || (y.getG() > (x.getG() + costXY)))
                    {
                        y.setG(x.getG() + costXY);
                        y.setF(y.getG());
                        y.setParent(new Vertex(x));
                        Open.add(y);
                    }
                }
            }
        }
        
        return null;
    }
    
    
    
    /*
     * Internal Methods
     */
    
    // This method opens a text file than transform its content to a graph
    public static Graph fileToGraph(String fileName) throws IOException
    {
		String line;
		String file = fileName;
                ArrayList<Vertex> vertices = new ArrayList<>();
                ArrayList<Edge> edges = new ArrayList<>();

		BufferedReader textFile;
		try {
                        // Opening the text file
			textFile = new BufferedReader(new FileReader(new File(file)));
			if (textFile == null)
                        {
				throw new FileNotFoundException("Fichier non trouvé : " + file);
			}
                        
                        // Read first line
                        line = textFile.readLine();
                        String s[] = line.split(" ");
                        
                        // Extract each of the corresponding vertices' and edges' numbers from the file
                        int n = Integer.valueOf(s[0]).intValue();
                        int m = Integer.valueOf(s[1]).intValue();
                        
                        // For each line corresponding to a vertex
                        for (int i = 0 ; (i < n) && (textFile != null) ; i++)
                        {
                            line = textFile.readLine();
                            if (!(line.equals("")))
                            {
                                s = line.split(" ");
                                String vertexName = s[0].substring(1, s[0].length() - 1);
                                int x = Integer.valueOf(s[1]).intValue();
                                int y = Integer.valueOf(s[2]).intValue();
                                
                                // Store the extracted vertex's informations
                                vertices.add(new Vertex(vertexName, x, y, new ArrayList<Vertex>()));
                            }
                        }
                        
                        // For each line corresponding to an edge
                        for (int i = 0 ; (i < m) && (textFile != null) ; i++)
                        {
                            line = textFile.readLine();
                            if (!(line.equals("")))
                            {
                                s = line.split(" ");
                                
                                // We seek for the two vertices of the edge in the previously stored vertices array
                                Vertex firstVertex = null, secondVertex = null;
                                for (int j = 1; j <= 2; j++)
                                {
                                    int k = 0;
                                    while ((!(s[j].equals(vertices.get(k).getName()))) && (k < n))
                                        k++;
                                    if (k >= n)
                                        System.err.println("Can't create edge (" + s[1] + "," + s[2] + ") : Missing vertex " + s[j]);
                                    else if (j == 1)
                                        firstVertex = vertices.get(k);
                                    else if (j == 2)
                                        secondVertex = vertices.get(k);
                                }
                                
                                // Extract the edge's weight
                                int weight = Integer.valueOf(s[3]).intValue();
                                
                                // Store the extracted edge's informations
                                edges.add(new Edge(firstVertex, secondVertex, weight));
                                
                                // Update the neighborhood list of each of the two vertices
                                firstVertex.addNeighbor(secondVertex);
                                secondVertex.addNeighbor(firstVertex);
                            }
                        }
                        // Close text file
                        textFile.close();
		}
                catch (FileNotFoundException e)
                {
			System.out.println(e.getMessage());
		}
                catch (IOException e)
                {
			System.out.println(e.getMessage());
		}
                
                // Store and return the resulted graph
                Graph tempGraph = new Graph(vertices, edges);
                return tempGraph;
    }
    
    // Bring all of the graph vertices closer at the x axis by given distance
    public static Graph bringVerticesCloserBy_X(Graph graph, int distance)
    {
        // First we make a copy of the sent graph
        Graph tempGraph = new Graph(graph);
        
        // For each vertex
        for (int i = 0 ; i < tempGraph.getN() ; i++)
        {
            // Make it closer by the given distance at the x axis
            tempGraph.getVertices().get(i).setX(tempGraph.getVertices().get(i).getX() - distance);
        }
        
        return tempGraph;
    }
    
    // Bring all of the graph vertices closer at the y axis by given distance
    public static Graph bringVerticesCloserBy_Y(Graph graph, int distance)
    {
        // First we make a copy of the sent graph
        Graph tempGraph = new Graph(graph);
        
        // For each vertex
        for (int i = 0 ; i < tempGraph.getN() ; i++)
        {
            // Make it closer by the given distance at the y axis
            tempGraph.getVertices().get(i).setY(tempGraph.getVertices().get(i).getY() - distance);
        }
        
        return tempGraph;
    }
    
    
    
    /*
     * @Overrides
     */
    
    // toString() shows edges without printing vertices' coordinates
    @Override
    public String toString()
    {
        String result;
        
        // Showing the number of vertices and edges
        result = "n = " + this.n + ", m = " + this.m + "\n";
        
        // Showing vertices' informations
        for (int i = 0 ; i < this.n ; i++)
        {
            result += this.vertices.get(i).toString2() + "\n";
        }
        
        // Showing edges' informations
        for (int i = 0 ; i < this.m ; i++)
        {
            result += this.edges.get(i).toString() + "\n";
        }
        
        return result;
    }
    
    // toString2() shows edges with the vertices' coordinates
    public String toString2()
    {
        String result;
        
        // Showing the number of vertices and edges
        result = "n = " + this.n + ", m = " + this.m + "\n";
        
        // Showing vertices' informations
        for (int i = 0 ; i < this.n ; i++)
        {
            result += this.vertices.get(i).toString2();
        }
        
        // Showing edges' informations
        for (int i = 0 ; i < this.m ; i++)
        {
            result += this.edges.get(i).toString2();
        }
        
        return result;
    }
    
    
    
    /*
     * Other useful methods
     */
    
    // Returns the coordiante x of the farthest vertex at the x axis
    public int getMaxXCoordinate()
    {
        int maxX = 0;
        
        for (int i = 0; i < this.n; i++)
        {
            if (this.vertices.get(i).getX() > maxX)
                maxX = this.vertices.get(i).getX();
        }
        
        return maxX;
    }
    
    // Returns the coordiante y of the farthest vertex at the y axis
    public int getMaxYCoordinate()
    {
        int maxY = 0;
        
        for (int i = 0; i < this.n; i++)
        {
            if (this.vertices.get(i).getY() > maxY)
                maxY = this.vertices.get(i).getY();
        }
        
        return maxY;
    }
    
    // Returns the smallest x coordiante of all vertices
    public int getMinXCoordinate()
    {
        int minX = this.vertices.get(0).getX();
        
        for (int i = 1; i < this.n; i++)
        {
            if (this.vertices.get(i).getX() < minX)
                minX = this.vertices.get(i).getX();
        }
        
        return minX;
    }
    
    // Returns the smallest y coordiante of all vertices
    public int getMinYCoordinate()
    {
        int minY = this.vertices.get(0).getY();
        
        for (int i = 1; i < this.n; i++)
        {
            if (this.vertices.get(i).getY() < minY)
                minY = this.vertices.get(i).getY();
        }
        
        return minY;
    }
    
    
    
    /*
     * Getters and Setters
     */

    public ArrayList<Vertex> getVertices()
    {
        return vertices;
    }

    public void setVertices(ArrayList<Vertex> vertices)
    {
        this.vertices = vertices;
    }

    public ArrayList<Edge> getEdges()
    {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges)
    {
        this.edges = edges;
    }

    public int getN()
    {
        return n;
    }

    public void setN(int n)
    {
        this.n = n;
    }

    public int getM()
    {
        return m;
    }

    public void setM(int m)
    {
        this.m = m;
    }
}
