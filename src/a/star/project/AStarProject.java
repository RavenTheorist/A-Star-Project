package a.star.project;

import a.star.project.Visualization.Frame;
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
        // First argument : Choice must be among {"graph1", "graph2", "chessboard", "maze"}. Any other choice will be treated as "graph1"
        // Second argument : Choice can be "euclidean". Anything else will make it work without heuristic
        Frame f = new Frame("d", "euclidean");
    }
}



































/* Another possible implementation of main() that asks the user for the graph type, musht also change the cunstructo of Panel
   public static void main(String[] args) throws IOException
    {
        Frame f;
        
        // Let the the user choose among a list containing all the known graph types
        String graphType = "";
        // While it's an invalid choice
        while (graphType.equals(""))
        {
            // Ask user and treat response
            System.out.print("Please select the graph type {\"graph1\", \"graph2\", \"chessboard\", \"maze\"} : ");
            Scanner sc = new Scanner(System.in);
            graphType = sc.nextLine();
            
            // If it's a know graph type, create frame
            if ((graphType.equals("graph1")) || (graphType.equals("graph2")) || (graphType.equals("chessboard")) || (graphType.equals("maze")))
                 f = new Frame(graphType);
            else
            {
                // Print error in case of a wrong choice
                System.out.println("Graph type \"" + graphType + "\" is unknown.");
                graphType = "";
            }
        }
    }
 */