package a.star.project.Visualization;

import java.awt.Color;
import javax.swing.JFrame;

/**
 * JFrame Implementation Class
 * 
 * @author Amine Elkhalsi <aminekhalsi@hotmail.com>
 */

public class Frame extends JFrame
{
    public Frame()
    {
        // Set frame parameters
        this.setTitle("A Star Visualization");
        Panel panel = new Panel();
        this.setSize(panel.getMaxXCoordinate() + 100, panel.getMaxYCoordinate() + 100);
        //this.setBackground(Color.black);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set panel parameters
        this.setContentPane(panel);
        
        this.setVisible(true);
    }
}
