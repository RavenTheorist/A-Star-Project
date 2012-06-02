package a.star.project.GraphImplementation;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Graph Implementation Class
 * 
 * @author Amine Elkhalsi <aminekhalsi@hotmail.com>
 */

public final class Graph
{
    /*
     * Graph Attributes
     */
    
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
    // Source vertex of the A* minimal path seeking
    private Vertex source;
    // List of the terminal states of the A* minimal path seeking
    private ArrayList<Vertex> terminals;
    // The used A* minimal pathfinding heuristic attribute
    private String heuristic;
    
    
    
    /*
     * Constructors
     */
    
    // This constructor is used for the chessboard
    public Graph()
    {
        // Initialization
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
        
        // Number of lines
        int lines = -1;
        // Number of columns
        int cols = -1;
        
        // Ask for lines number
        while(lines == -1)
        {
            System.out.print("Please write the number of lines : ");
            Scanner sc = new Scanner(System.in);
            int temp = sc.nextInt();
            // There should be an interval as a limit for the number
            if ((temp >= 1) && (temp <= 100))
            {
                lines = temp;
            }
            else
                System.out.println("Incorrect Number of line !");
        }
        
        // Ask for the columns number
        while(cols == -1)
        {
            System.out.print("Please write the number of columns : ");
            Scanner sc = new Scanner(System.in);
            int temp = sc.nextInt();
            // There should be an interval as a limit for the number
            if ((temp >= 1) && (temp <= 100))
            {
                cols = temp;
            }
            else
                System.out.println("Incorrect Number of columns !");
        }
        
        // Updating the number of vertices
        this.n = (cols * lines);
        // Updating the number of edges
        this.m = (((cols-1) * lines) + ((lines-1) * cols));
        System.out.println("Calculated : " + (((cols-1) * lines) + ((lines-1) * cols)));
        
        // Constructing the chessboard
        // Start construction by vertices
        for (int l = 0 ; l < lines ; l++)
        {
            for (int c = 0 ; c < cols ; c++)
            {
                this.vertices.add(new Vertex(("l" + l + "c" + c), (c * 100), (l * 100)));
            }
        }
        
        // Edges construction
        // Start construction by the lines
        for (int i = 0 ; i < n-1 ; i++)
        {
            Vertex v1 = this.vertices.get(i);
            Vertex v2 = this.vertices.get(i+1);
            
            if (!(((i+1) % cols) == 0))
                this.edges.add(new Edge(v1, v2, 1));
        }
        
        // Constructing the columns
        for (int i = 0 ; i < cols ; i++)
        {
            for (int j = 1 ; j < lines ; j++)
            {
                Vertex v1 = this.vertices.get(i);
                Vertex v2 = this.vertices.get(j*cols+i);
                
                this.edges.add(new Edge(v1, v2, 1));
            }
        }
    }
    
    public Graph(ArrayList<Vertex> vertices, ArrayList<Edge> edges)
    {
        this.vertices = vertices;
        this.edges = edges;
        this.n = vertices.size();
        this.m = edges.size();
        this.minimalPath = new ArrayList<>();
    }
    
    public Graph(ArrayList<Vertex> vertices, ArrayList<Edge> edges, Vertex source, ArrayList<Vertex> terminals)
    {
        this.vertices = vertices;
        this.edges = edges;
        this.n = vertices.size();
        this.m = edges.size();
        this.minimalPath = new ArrayList<>();
        this.source = source;
        this.terminals = terminals;
    }
    
    public Graph(String fileName) throws IOException
    {
        // Extract the graph from the file
        Graph tempGraph = fileToGraph(fileName);
        
        this.vertices = tempGraph.getVertices();
        this.edges = tempGraph.getEdges();
        this.n = tempGraph.getN();
        this.m = tempGraph.getM();
        this.minimalPath = new ArrayList<>();
    }
    
    // Constructor used for the maze
    public Graph(String fileName, int a) throws IOException
    {
        // Extract the graph from the file
        Graph tempGraph = fileToMaze(fileName);
        
        // Update own attributes using the resulted tempGraph
        this.vertices = tempGraph.getVertices();
        this.edges = tempGraph.getEdges();
        this.n = tempGraph.getVertices().size();
        this.m = tempGraph.getEdges().size();
        this.minimalPath = new ArrayList<>();
        this.source = tempGraph.getSource();
        this.terminals = tempGraph.getTerminals();
    }
    
    public Graph(String fileName, String sourceName, String[] terminals) throws IOException
    {
        // Extract the graph from the file
        Graph tempGraph = fileToGraph(fileName);
        
        // Update own attributes using the resulted tempGraph
        this.vertices = tempGraph.getVertices();
        this.edges = tempGraph.getEdges();
        this.n = tempGraph.getVertices().size();
        this.m = tempGraph.getEdges().size();
        this.terminals = new ArrayList<>();
        this.minimalPath = new ArrayList<>();
        
        // Verify the existense of the source vertex
        for (int i = 0; i < this.vertices.size(); i++)
        {
            if(this.vertices.get(i).getName().equals(sourceName))
                this.source = this.vertices.get(i);
        }
        
        // If the source vertex exists
        if (this.source != null)
        {
            // Verify the existence of each terminal vertex
            for(int i = 0 ; i < terminals.length ; i++)
            {
                boolean terminalExists = false;
                for (int j = 0 ; j < this.vertices.size() ; j++)
                {
                    // If the current analyzed terminal vertex exists
                    if(this.vertices.get(j).getName().equals(terminals[i]))
                    {
                        // Take it from the vertices of the graph
                        this.terminals.add(this.vertices.get(j));
                        // Say it exists
                        terminalExists = true;
                    }
                }
                // Show error if the current terminal vertex doesn't exist
                if (!terminalExists)
                        System.err.println("Graph Constructor : \"One of the terminal vertices doesn't exist !\"");
            }
            
            // Everything's ok, we are now ready to lauch the A* Algorithm
            AStar(this.heuristic);
        }
        else
            System.err.println("Graph Constructor : \"The source vertex doesn't exist !\"");
        
        //this.minimalPath = AStar(this.vertices.get(0), this.getVertices().get(this.getVertices().size()-1));
        
    }
    
    public Graph(Graph graph)
    {
        this.vertices = graph.getVertices();
        this.edges = graph.getEdges();
        this.n = graph.getN();
        this.m = graph.getM();
        this.minimalPath = new ArrayList<>();
    }
    
    
    
    /*
     * A* Methods
     */
    
    public Vertex AStar(String h)
    {
        // Clearing minimalPath and terminals contents
        this.minimalPath = new ArrayList<>();
        this.terminals = new ArrayList<>();
        // Update the used heuristic
        switch (h.toLowerCase())
        {
            case "euclidean":
                this.heuristic = "euclidean";
                break;
            case "manhattan":
                this.heuristic = "manhattan";
                break;
            default:
                this.heuristic = h;
                break;
        }
        Vertex s = this.getSource();
        // The set of tentative nodes to be evaluated
        ArrayList<Vertex> Opened = new ArrayList<>();
        // Initially containing the start node
        Opened.add(s);
        // The set of the already evaluated nodes
        ArrayList<Vertex> Closed = new ArrayList<>();
        // Terminals list
        ArrayList<Vertex> terminalsList = this.getTerminals();
        // Distance of s is 0 since it's the starting point
        s.setG(0);
        switch (h.toLowerCase())
        {
            case "euclidean":
                s.setF(euclideanHeuristic(s));
                break;
            case "manhattan":
                s.setF(manhattanHeuristic(s));
                break;
        }
        
        // We make s the parent of its own self
        s.setParent(s);
        
        // While there is(are) remaining vertices in the Opened set
        while (!Opened.isEmpty())
        {
            int min = Integer.MAX_VALUE;
            Vertex x = s;
            
            // Extract the vertex x with the least f(x)
            for(int i = 0 ; i < Opened.size() ; i++)
            {
                if(Opened.get(i).getF() < min)
                {
                    min = Opened.get(i).getF();
                    x = Opened.get(i);
                }
            }
            
            // Add it to Closed vertices set
            Closed.add(x);
            // Remove it from the Opened vertices set
            Opened.remove(x);
            
            if(terminalsList.contains(x))
            {
                Vertex temp = x;
                // Save the current terminal vertex
                this.minimalPath.add(new Vertex(x));
                // While the parent vertex is different from the source
                while(!(temp.equals(s)))
                {
                    // Get the parent and save it
                    temp = temp.getParent();
                    this.minimalPath.add(new Vertex(temp));
                }
                // We'll also be able to draw the path with using x
                return x;
            }
            else
            {
                // Get neighbors
                ArrayList<Vertex> succ = this.seekNeighbors(x);
                // For each neighbor/successor
                for (int j = 0; j < succ.size(); j++ )
                {
                    if(((!(Closed.contains(succ.get(j)))) && (!(Opened.contains(succ.get(j))))) || ((succ.get(j).getG()) > (x.getG() + this.cost(x, succ.get(j)))))
                    {
                        // Get weight of the relating edge (x, succ)
                        succ.get(j).setG(x.getG() + this.cost(x, succ.get(j)));
                        // Carlculate F using the right heuristic
                        switch (h.toLowerCase())
                        {
                            case "euclidean":
                                succ.get(j).setF(succ.get(j).getG() + euclideanHeuristic(succ.get(j)));
                                break;
                            case "manhattan":
                                succ.get(j).setF(succ.get(j).getG() + manhattanHeuristic(succ.get(j)));
                                break;
                        }
                        
                         // Set the x as parent of j
                         succ.get(j).setParent(x);
                         // Finally adds the neighbor to the opened set of vertices
                         Opened.add(succ.get(j));
                    }

                }
            }
       }
       return s;
}
    
    public Vertex AStar(String h, Vertex source, ArrayList<Vertex> terminals)
    {
        // Clearing minimalPath and terminals contents
        this.minimalPath = new ArrayList<>();
        this.terminals = new ArrayList<>();
        
        // Update the graph attributes
        if (h != null)
        {
            switch (h)
            {
                case "euclidean":
                    this.heuristic = "euclidean";
                    break;
                case "manhattan":
                    this.heuristic = "manhattan";
                    break;
            }
        }
        else
            this.heuristic = h;
        this.source = source;
        this.terminals = terminals;
        
        Vertex s = source;
        // The set of tentative nodes to be evaluated
        ArrayList<Vertex> Opened = new ArrayList<>();
        // Initially containing the start node
        Opened.add(s);
        // The set of the already evaluated nodes
        ArrayList<Vertex> Closed = new ArrayList<>();
        // Terminals list
        ArrayList<Vertex> terminalsList = terminals;
        // Distance of s is 0 since it's the starting point
        s.setG(0);
        switch (h.toLowerCase())
        {
            case "euclidean":
                s.setF(euclideanHeuristic(s));
                break;
            case "manhattan":
                s.setF(manhattanHeuristic(s));
                break;
        }
        
        // We make s the parent of its own self
        s.setParent(s);
        
        // While there is(are) remaining vertices in the Opened set
        while (!Opened.isEmpty())
        {
            int min = Integer.MAX_VALUE;
            Vertex x = s;
            
            // Extract the vertex x with the least f(x)
            for(int i = 0 ; i < Opened.size() ; i++)
            {
                if(Opened.get(i).getF() < min)
                {
                    min = Opened.get(i).getF();
                    x = Opened.get(i);
                }
            }
            
            // Add it to Closed vertices set
            Closed.add(x);
            // Remove it from the Opened vertices set
            Opened.remove(x);
            
            if(terminalsList.contains(x))
            {
                Vertex temp = x;
                // Save the current terminal vertex
                this.minimalPath.add(new Vertex(x));
                // While the parent vertex is different from the source
                while(!(temp.equals(s)))
                {
                    // Get the parent and save it
                    temp = temp.getParent();
                    this.minimalPath.add(new Vertex(temp));
                }
                // We'll also be able to draw the path with using x
                return x;
            }
            else
            {
                // Get neighbors
                ArrayList<Vertex> succ = this.seekNeighbors(x);
                // For each neighbor/successor
                for (int j = 0; j < succ.size(); j++ )
                {
                    if(((!(Closed.contains(succ.get(j)))) && (!(Opened.contains(succ.get(j))))) || ((succ.get(j).getG()) > (x.getG() + this.cost(x, succ.get(j)))))
                    {
                        // Get weight of the relating edge (x, succ)
                        succ.get(j).setG(x.getG() + this.cost(x, succ.get(j)));
                        // Carlculate F using the right heuristic
                        switch (h.toLowerCase())
                        {
                            case "euclidean":
                                succ.get(j).setF(succ.get(j).getG() + euclideanHeuristic(succ.get(j)));
                                break;
                            case "manhattan":
                                succ.get(j).setF(succ.get(j).getG() + manhattanHeuristic(succ.get(j)));
                                break;
                        }
                         // Set the x as parent of j
                         succ.get(j).setParent(x);
                         // Finally adds the neighbor to the opened set of vertices
                         Opened.add(succ.get(j));
                    }

                }
            }
       }
       return s;
}
    
    // This method look for an edge that relates x and y then return its weight
    public int cost(Vertex x, Vertex y)
    {
        // Make an edge (x, y) with the two given vertices x and y
        Edge a = new Edge(x,y, -1);

        if(this.verifyEdgeExistence(a))
        {
            // Sweep all the edges from the edges list of the graph
            for (int i = 0;i< this.edges.size();i++)
            {
                if((this.edges.get(i).getFirstVertex() == a.getFirstVertex())&&(this.edges.get(i).getSecondVertex() == a.getSecondVertex()))
                {
                    return  this.edges.get(i).getWeight();
                }

                if((this.edges.get(i).getSecondVertex() == a.getFirstVertex())&&(this.edges.get(i).getFirstVertex() == a.getSecondVertex()))
                {
                    return this.edges.get(i).getWeight();
                }
            }
        }
        else
        {
            // Else, print the error
            System.out.println("These two vertices aren't related by an edge.");
            return -1;
        }
        // If anything else happens... (is that "wtf" you just said? Don't blame ME ! I know it's not possible -> but it's simply Java and its JRE)
        return -1;
    }
    
    // This method verifies the existency of an edge among the edges list of the graph
    public boolean verifyEdgeExistence(Edge edge)
    {
        // For each existing edge
        for (int i = 0 ; i < edges.size() ; i++)
        {
            // Compare it to the edge (edge.getFirstVertex(), edge.getSecondVertex())
            if((this.edges.get(i).getFirstVertex() == edge.getFirstVertex())&&(this.edges.get(i).getSecondVertex() == edge.getSecondVertex()))
            {
                // If the same, than return true (it exists)
                return true;
            }

            if((this.edges.get(i).getSecondVertex() == edge.getFirstVertex())&&(this.edges.get(i).getFirstVertex() == edge.getSecondVertex()))
            {
                // If the same in one way or another, than return true (it exists)
                return true;
            }
        }
        // Else return false (doesn't exist)
        return false;
    }
    
    // This method, as its name implies, returns a set of all the neighbors of a given vertex
    public ArrayList<Vertex> seekNeighbors(Vertex vertex)
    {
        // The set succ will contain all the neighbors of vertex
        ArrayList<Vertex> succ = new ArrayList<>();
        if(this.vertices.contains(vertex))
        {
            if(vertex.getParent() != null)
            {
                // We seek for the neighbors by sweeping the edges
                for(int i = 0 ; i < this.edges.size() ; i++)
                {
                    // If there is an edge that contains the two vertices
                    if(this.edges.get(i).getFirstVertex() == vertex)
                    {
                        if(this.edges.get(i).getSecondVertex() != vertex.getParent())
                        {
                            // If exists, add it to the set
                            succ.add(this.edges.get(i).getSecondVertex());
                        }
                    }

                    if(this.edges.get(i).getSecondVertex() == vertex)
                    {
                        if(this.edges.get(i).getFirstVertex() != vertex.getParent())
                        {
                            // If exists, add it to the set
                            succ.add(this.edges.get(i).getFirstVertex());
                        }
                    }

                }
                // Return the set
                return succ;
            }
            else
            {
                // Print the error
                System.out.println("The actual vertex doesn't have any parent, no parents no successors");
                succ = null;
                return succ;
            }
        }
        else
        {
            // Print the error
            System.out.println("The actual vertex dosn't exist, so obviously it does not have any successors.");
            succ = null;
            return succ;
        }
    }

    // This is my first tested heuristic
    public int euclideanHeuristic(Vertex x)
    {
        int min = Integer.MAX_VALUE;
        int distance;
        // For each terminal vertex
        for(int i = 0 ; i < this.getTerminals().size() ; i++)
        {
         // Calculating distance using pythagore
         distance = (int) Math.sqrt(Math.pow(((this.getTerminals().get(i).getX()) - (x.getX())), 2) + Math.pow(((this.getTerminals().get(i).getY()) - (x.getY())), 2));
         // If the distance from this terminal vertex is the smallest
         if (distance < min)
         {
             // Take it as the minimal distance to return
             min = distance;
         }
        }
        return min;
    }
    
    // Manhatten heuristic, another possible heuristic
    public int manhattanHeuristic(Vertex x)
    {
        int min = Integer.MAX_VALUE;
        int distance;
        // For each terminal vertex
        for(int i = 0 ; i < this.terminals.size() ; i++)
        {
         // Calculate the distance = the distances on X axins + the distance of the Y axis
         distance = (int) Math.abs((this.terminals.get(i).getX()) - (x.getX())) + Math.abs((this.terminals.get(i).getY()) - (x.getY())) ;
         // If the distance from the terminal vertex is the smallest
         if (distance < min)
         {
             // Take it as the minimal distance to return
             min = distance;
         }
        }
        return min;
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
		try
                {
                        // Opening the text file
			textFile = new BufferedReader(new FileReader(new File(file)));
			if (textFile == null)
                        {
				throw new FileNotFoundException("File not found : " + file);
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
    
    // This method opens a text file than transform its content to a graph
    public static Graph fileToMaze(String fileName) throws IOException
    {
		String line;
		String file = fileName;
                ArrayList<Vertex> vertices = new ArrayList<>();
                ArrayList<Edge> edges = new ArrayList<>();
                Vertex source = new Vertex();
                ArrayList<Vertex> terminals = new ArrayList<>();

		BufferedReader textFile;
		try {
                        // Opening the text file
			textFile = new BufferedReader(new FileReader(new File(file)));
			if (textFile == null)
                        {
				throw new FileNotFoundException("File not found : " + file);
			}
                        
                        // Put a mark in order to be able to reset the cursor up to this point
                        int BUFFER_SIZE = 1000;
                        textFile.mark(BUFFER_SIZE);
                        
                        // Read first line
                        line = textFile.readLine();
                        // Count the number of columns
                        String s[] = line.split("-");
                        int cols = s.length - 1;
                        
                        // Count the number of lines
                        int lines = 0;
                        // Count also the number of lines in the text file except the first one
                        int textLines = 0;
                        while ((line = textFile.readLine()) != null)
                        {
                            textLines++;
                            if (line.charAt(0) == '|')
                                lines++;
                        }
                        
                        // Construction of vertices
                        for (int l = 0 ; l < lines ; l++)
                        {
                            for (int c = 0 ; c < cols ; c++)
                            {
                                vertices.add(new Vertex(("l" + l + "c" + c), (c * 100), (l * 100)));
                            }
                        }
                        
                        // Rewind the stream back to the mark
                        textFile.reset();
                        
                        // Read first line to ignore it
                        textFile.readLine();
                        
                        int loopCounter = 0;
                        int loopCounter2 = 0;
                        // Construction of edges, source vertex and terminal vertices
                        for (int i = 0 ; i < textLines - 1 ; i++)
                        {
                            // For each read line
                            line = textFile.readLine();
                            // If it's about vertical edges
                            if (line.charAt(0) == '|')
                            {
                                /* Firstly, we treat the content of every square */
                                // We need a counter that is specific the above condition
                                
                                // columnCounter indicate the column of the current vertex
                                int columnCounter = 0;
                                // Sweep all the columns, jumping from square to square and directly into the middle of each square
                                for (int j = 2 ; j < line.length() ; j += 4)
                                {
                                    // If the square contains an 's'
                                    if (line.charAt(j) == 's')
                                    {
                                        // It means it's a source, so we save it
                                        for (int k = 0; k < vertices.size(); k++)
                                        {
                                            if (vertices.get(k).getName().equals("l"+loopCounter+"c"+columnCounter))
                                                source = vertices.get(k);
                                        }
                                    }
                                    // If the square contains a 't'
                                    else if(line.charAt(j) == 't')
                                    {
                                        // It means it's a terminal vertex, so we save it
                                        Vertex terminal = new Vertex();
                                        for (int k = 0; k < vertices.size(); k++)
                                        {
                                            if (vertices.get(k).getName().equals("l"+loopCounter+"c"+columnCounter))
                                                terminals.add(vertices.get(k));
                                        }
                                    }
                                    columnCounter++;
                                }
                                
                                /* Secondly, we treat the edges */
                                // re-initializing columnCounter to zero
                                // Sweep all the columns, jumping from edge to edge
                                columnCounter = 0;
                                for (int j = 4 ; j < line.length() ; j += 4)
                                {
                                    // If there's a ' '
                                    if ((line.charAt(j) == ' ') && (j != line.length()-1))
                                    {
                                        // It means it's an edge, so we save it
                                        Vertex v1 = new Vertex();
                                        Vertex v2 = new Vertex();
                                        for (int k = 0; k < vertices.size(); k++)
                                        {
                                            // Construct the first vertex
                                            if (vertices.get(k).getName().equals("l"+loopCounter+"c"+columnCounter))
                                                v1 = vertices.get(k);
                                            // Construct the second vertex
                                            if (vertices.get(k).getName().equals("l"+loopCounter+"c"+(columnCounter+1)))
                                                v2 = vertices.get(k);
                                        }
                                        // Construct the edge
                                        Edge e = new Edge(v1, v2, 1);
                                        edges.add(e);
                                    }
                                    columnCounter++;
                                }
                                loopCounter++;
                            }
                            
                            /* Finally, we treat the horizontal edges */
                            else if (line.charAt(0) == '+') 
                            {
                                // We need a specific counter for the columns
                                int columnCounter = 0;
                                for (int j = 2 ; j < line.length() ; j += 4)
                                {
                                    // If there's a ' '
                                    if (line.charAt(j) == ' ')
                                    {
                                        // It means it's an edge, so we save it
                                        Vertex v1 = new Vertex();
                                        Vertex v2 = new Vertex();
                                        for (int k = 0; k < vertices.size(); k++)
                                        {
                                            // Construct the first vertex
                                            if (vertices.get(k).getName().equals("l"+loopCounter2+"c"+columnCounter))
                                                v1 = vertices.get(k);
                                            // Construct the second vertex
                                            if (vertices.get(k).getName().equals("l"+(loopCounter2+1)+"c"+(columnCounter)))
                                                v2 = vertices.get(k);
                                        }
                                        // Construct the edge
                                        Edge e = new Edge(v1, v2, 1);
                                        edges.add(e);
                                    }
                                    columnCounter++;
                                }
                                loopCounter2++;
                            }
                            //System.out.println(line);
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
                Graph tempGraph = new Graph(vertices, edges, source, terminals);
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

    public ArrayList<Vertex> getMinimalPath()
    {
        return minimalPath;
    }

    public void setMinimalPath(ArrayList<Vertex> minimalPath)
    {
        this.minimalPath = minimalPath;
    }

    public Vertex getSource()
    {
        return source;
    }

    public void setSource(Vertex source)
    {
        this.source = source;
    }

    public ArrayList<Vertex> getTerminals()
    {
        return terminals;
    }

    public void setTerminals(ArrayList<Vertex> terminals)
    {
        this.terminals = terminals;
    }

    public String getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(String heuristic) {
        this.heuristic = heuristic;
    }
}
