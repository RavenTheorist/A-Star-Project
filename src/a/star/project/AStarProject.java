package a.star.project;

import a.star.project.Visualization.Frame;
import a.star.project.GraphImplementation.Graph;
import java.io.IOException;

/**
 * Main Class
 * 
 * @author Amine Elkhalsi <aminekhalsi@hotmail.com>
 */

public class AStarProject
{
    public static void main(String[] args) throws IOException
    {
        Graph graph = new Graph("graph1.txt");
        System.out.println(graph.toString());
        Frame f = new Frame();
    }
}