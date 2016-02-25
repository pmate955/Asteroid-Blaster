/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jasteroidblaster;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JComponent;

/**
 *
 * @author Mate1
 */
public class Debris extends JComponent{
    double direction;
    int maxX;
    int maxY;
    double posX;
    double posY;
    int showCycle;
    int cycle;
    int cycleMax;
    
    public Debris(int maxX, int maxY, double posX, double posY, double direction){
        this.maxX = maxX;
        this.maxY = maxY;
        this.posX = posX;
        this.posY = posY;
        this.direction = direction;
        cycleMax = ThreadLocalRandom.current().nextInt(1,5);
        showCycle = ThreadLocalRandom.current().nextInt(30,70);
        cycle = cycleMax;
    }
    
    public void paintComponent(Graphics gr){        
        super.paintComponent(gr);        
        gr.setColor(Color.white);
        gr.fillRect((int)posX, (int)posY, 2, 2);        
    }
    
    public boolean move(){                                          //False: delete the object
            if(cycle==0){
                cycle = cycleMax;
                double tempX = posX;
                double tempY = posY;
                showCycle--;
                tempX+=Math.sin(direction);
                tempY-=Math.cos(direction);
                if(tempX < 0 || tempX > maxX) return false;
                else if(tempY < 0 || tempY > maxY) return false;
                if(showCycle==0) return false;
                else {
                    posX = tempX;
                    posY = tempY;
                    return true;
                }
            } else cycle--;
        return true;
    }
}
