package a.star.project;

import a.star.project.Visualization.Frame;
import java.io.IOException;

/**
 * Main Class Implementation
 * 
 * @author Amine Elkhalsi <aminekhalsi@hotmail.com>
 */

public class AStarProject
{
    public static void main(String[] args) throws IOException
    {
        // First argument is for the type of graph : Choice must be among {"graph1", "graph2", "chessboard", "maze"}. Any other choices will be treated as "graph1"
        // Second argument is for heuristic : Choice can be "euclidean" or "manhattan". Anything other value will make it work WITHOUT heuristic
        Frame f = new Frame("maze", "manhattan");
    }
}