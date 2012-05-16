package a.star.project.Visualization;

import a.star.project.GraphImplementation.Vertex;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;

/**
 * JFrame Implementation Class
 * 
 * @author Amine Elkhalsi <aminekhalsi@hotmail.com>
 */

public class Frame extends JFrame
{
    // Content panel of the frame
    private Panel panel;
    
    /*
     * Constructors
     */
    
    public Frame(String graphType)
    {
        // Set frame parameters
        this.setTitle("A Star Visualization");
        this.panel = new Panel(graphType);
        this.setSize(panel.getMaxXCoordinate() + 100, panel.getMaxYCoordinate() + 100);
        this.setAlwaysOnTop(true);
        //this.setBackground(Color.black);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Enable the focus when pressing the arrow keys
        panel.setFocusable(true);
        panel.requestFocus();
        
        // Set panel parameters
        this.setContentPane(panel);
        
        // Set the frame visible
        this.setVisible(true);
        
        // Calling Main Loop
        go();
    }
    
    // Frame Main Loop
    private void go()
    {
        // Inifinite loop
        while(true)
        {
            // Ask for the new source for the pathfinding A* algorithm
            boolean exist = false;
            Vertex source = new Vertex();
            while (!exist)
            {
                // Seek for its existence
                System.out.print("Please select the source vertex : ");
                Scanner sc = new Scanner(System.in);
                String readString = sc.nextLine();
                for (int i = 0 ; i < this.panel.getGraph().getVertices().size() ; i++)
                {
                    if (this.panel.getGraph().getVertices().get(i).getName().equals(readString))
                    {
                        // Say it exists (exist = true), than save it
                        exist = true;
                        source = this.panel.getGraph().getVertices().get(i);
                    }
                }
                // Show error
                if(!exist)
                        System.out.println("This vertex doesn't exist !");
            }
            
            // Ask for a correct number that will correspond to the number of terminal vertices
            boolean correctNumber = false;
            int numberOfTerminalVertices = 0;
            while (!correctNumber)
            {
                System.out.print("Please write the number of terminal vertices : ");
                Scanner sc = new Scanner(System.in);
                int readInteger = sc.nextInt();
                
                // If it's a correct number
                if((readInteger < this.panel.getGraph().getVertices().size()) && (readInteger > 0))
                {
                    correctNumber = true;
                    numberOfTerminalVertices = readInteger;
                }
                // Show error
                else
                    System.out.println("Incorrect number, please select a number between 1 and " + (this.panel.getGraph().getVertices().size() - 1));
            }
            
            ArrayList<Vertex> terminals = new ArrayList<>();
            
            // Ask for each of the terminal vertices needed
            for (int i = 0 ; i < numberOfTerminalVertices ; i++)
            {
                boolean vertexExist = false;
                while (!vertexExist)
                {
                    // Seek for its existence
                    System.out.print("Please select the terminal vertex number " + (i+1) + " : ");
                    Scanner sc = new Scanner(System.in);
                    String readString = sc.nextLine();
                    for (int j = 0 ; j < this.panel.getGraph().getVertices().size() ; j++)
                    {
                        if (this.panel.getGraph().getVertices().get(j).getName().equals(readString))
                        {
                            // Say it exists (exist = true), than save it
                            vertexExist = true;
                            terminals.add(this.panel.getGraph().getVertices().get(j));
                        }
                    }
                        // Show error
                    if(!vertexExist)
                            System.out.println("This vertex doesn't exist !");
                }
            }
            
            // Ask for the heuristic
            System.out.print("Please choose a heuristic to use : ");
            Scanner sc = new Scanner(System.in);
            String heuristic = sc.nextLine();
            heuristic = heuristic.toLowerCase();
            if(!heuristic.equals("euclidean"))
                System.out.println("Unknown heuristic... continuing without heuristic.");
            
            this.panel.setHeuristicPanel(heuristic);
            this.panel.getGraph().setTerminals(terminals);
            this.panel.getGraph().setSource(source);
            this.panel.getGraph().setHeuristic(heuristic);
            this.panel.getGraph().AStar(heuristic, source, terminals);
            
            repaint();
        }
    }

    public Panel getPanel()
    {
        return panel;
    }

    public void setPanel(Panel panel)
    {
        this.panel = panel;
    }
}