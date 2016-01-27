/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jasteroidblaster;

import javax.swing.JOptionPane;

/**
 *
 * @author Mate1
 */
public class JAsteroidBlaster {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] buttons2 = { "Chill", "Normal", "Hard"};    
        int diff= JOptionPane.showOptionDialog(null, "Choose difficulty!", "Starting",
        JOptionPane.WARNING_MESSAGE, 0, null, buttons2, buttons2[1]);
        AsteroidGUI as = new AsteroidGUI(diff);
        as.setVisible(true);
        as.gameRun();
       
    }
    
}
