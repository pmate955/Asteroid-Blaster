/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jasteroidblaster;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 *
 * @author Mate1
 */
public class Bullet extends JComponent{
    
    boolean fromPlayer;   
    double direction;
    int maxX;
    int maxY;
    double posX;
    double posY;
    
    public Bullet(int maxX, int maxY, double posX, double posY, double direction, boolean fromPlayer){
        this.fromPlayer = fromPlayer;
        this.maxX = maxX;
        this.maxY = maxY;
        this.posX = posX;
        this.posY = posY;
        this.direction = direction;
    }
    
    public void paintComponent(Graphics gr){        
        super.paintComponent(gr);        
        gr.setColor(Color.white);
        gr.fillRect((int)posX, (int)posY, 5, 5);        
    }
    
    public boolean move(){                                          //False: over bullet
            double tempX = posX;
            double tempY = posY;
            tempX+=Math.sin(direction);
            tempY-=Math.cos(direction);
            if(tempX < 0 || tempX > maxX) return false;
            else if(tempY < 0 || tempY > maxY) return false;
            else {
                posX = tempX;
                posY = tempY;
                return true;
            }
    }
        

}
