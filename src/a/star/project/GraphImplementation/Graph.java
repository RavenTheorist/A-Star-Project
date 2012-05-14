package a.star.project.GraphImplementation;

import java.io.*;
import java.util.ArrayList;

/**
 * Graph Implementation Class
 * 
 * @author Amine Elkhalsi <aminekhalsi@hotmail.com>
 */

public class Graph
{
    // Vertices' List
    private ArrayList<Vertex> vertices;
    // Edges' List
    private ArrayList<Edge> edges;
    // Number of vertices
    private int n;
    // Number of edges
    private int m;
    
    
    
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
                                vertices.add(new Vertex(vertexName, x, y));
                            }
                        }
                        
                        // For each line corresponding to an edge
                        for (int i = 0 ; (i < m) && (textFile != null) ; i++)
                        {
                            line = textFile.readLine();
                            if (!(line.equals("")))
                            {
                                s = line.split(" ");
                                
                                // We seek both vertices of the edge in the previously stored vertices array
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
                            }
                        }
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
