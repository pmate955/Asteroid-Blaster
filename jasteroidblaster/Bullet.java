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
    int direction;
    int maxX;
    int maxY;
    int posX;
    int posY;
    
    public Bullet(int maxX, int maxY, int posX, int posY, int direction, boolean fromPlayer){
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
        gr.fillRect(posX, posY, 5, 5);        
    }
    
    public boolean move(){                                          //False: over bullet
            if(direction==1){                       //Up
                if(posY-1>=0) posY--;
                else return false;
            } else if(direction==2){                //Up Right
                if(posX+1<maxX-2) posX++;
                else return false;
                if(posY-1>=0) posY--;
                else return false;
            } else if(direction==3){                //Right
                if(posX+1<maxX-2) posX++;
                else return false;
            } else if(direction==4){                //Down Right
                if(posX+1<maxX-2) posX++;
                else return false;
                if(posY+1<maxY-2) posY++;
                else return false;
            } else if(direction==5){                //Down
                if(posY+1<maxY-2) posY++;
                else return false;          
            } else if(direction==6){                //Down Left
                if(posY+1<maxY-2) posY++;
                else  return false;
                if(posX-1>=0) posX--;
                else return false;
            } else if(direction==7){                //Left
                if(posX-1>=0) posX--;
                else  return false;
            } else if(direction==8){                //Up left
                if(posX-1>=0) posX--;
                else  return false;
                if(posY-1>=0) posY--;
                else  return false;
            }
            //repaint();            
            return true;
    }

}
