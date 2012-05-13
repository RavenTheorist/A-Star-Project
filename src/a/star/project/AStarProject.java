package a.star.project;

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
    }
}