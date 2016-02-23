/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jasteroidblaster;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author Mate1
 */
public class SpaceShip extends JComponent{
    
    boolean running = true;   
    boolean accelerated = false;
    BufferedImage up;    
    BufferedImage upF;
    int speedPlus = 0;
    double speedAngle = 0;
    int fireCycle = 20;                                                         //Blow time
    int maxX;                                                                   //Window size(width)
    int maxY;                                                                   //Window size(height);
    double posX;                                                                //Spaceship position
    double posY;
    double angle;                                                               //Visual direction in radian
    int dir;                                                                    //Visual direction in degree    
    List<Integer> cycles = new ArrayList<Integer>();                            //Cycle integer for speed (temporary)
    List<Double> angles = new ArrayList<Double>();                              //Angles to move
    List<Integer> angleCycle = new ArrayList<Integer>();                        //Cycle integer for speed    
    int size;                                                                   //Size of spaceship in pixel                                                                       
    
    
    public SpaceShip(int maxX, int maxY, int size){                             //Constructor
        this.maxX = maxX;
        this.maxY = maxY;
        this.posX = maxX/2;
        this.posY = maxY/2;       
        this.size = size;                
        try{
            up = ImageIO.read(this.getClass().getResource("/images/r-1.gif"));
            upF = ImageIO.read(this.getClass().getResource("/images/r-1F.gif"));
        } catch(Exception e){
            System.out.println("SpaceShip picture not found!");
        }
    }    
    
    public void paintComponent(Graphics gr){                                    //Paint method
        Graphics2D g2d = (Graphics2D)gr.create();
        g2d.rotate(angle, posX+15, posY+15);         
        if(accelerated){
            g2d.drawImage(upF,(int) posX, (int) posY, size, size, this); 
            
        } else g2d.drawImage(up, (int)posX, (int)posY,size, size, this);        
        g2d.dispose();
    }
    
    public void accelerate(){                                                   //Accelerate      
         accelerated = true;
        if(angles.isEmpty()){
            angles.add(angle);
            angleCycle.add(7);
            cycles.add(7);
        } else {
            if(angles.contains(angle)){
                for(int i = 0; i < angles.size();i++){
                    int cyc = angleCycle.get(i);
                    if(angles.get(i)== angle){                        
                        if(cyc>2){                        
                            angleCycle.set(i, cyc-=1);                               
                        } else if(speedPlus < 3){
                            angles.add(angle);
                            angleCycle.add(7);
                            cycles.add(7);
                            speedAngle = angle;
                            speedPlus++;
                        }
                    } else if(cyc<10){
                        angleCycle.set(i, cyc+=1);
                    }
                    if(cyc+1==10){
                        if(angles.get(i)==speedAngle){
                            speedPlus = 0;
                        }
                        angles.remove(i);
                        angleCycle.remove(i);
                        cycles.remove(i);     
                        
                    }
                }
            } else {                
                 angles.add(angle);
                 angleCycle.add(7);
                 cycles.add(7);                 
            }
        }
    }
    
       
    public void move2(){                                                        //Move ship
        
        for(int i = 0; i < cycles.size();i++){           
            int cyc = cycles.get(i);
            if(cyc==0){
                cycles.set(i, angleCycle.get(i));
                double rad = angles.get(i);
                double tempX = posX;
                double tempY = posY;
                tempX+=Math.sin(rad);
                tempY-=Math.cos(rad);
                if(tempX < 5) tempX = maxX-18;
                else if(tempX > maxX-15) tempX = 5;
                if(tempY < 5 ) tempY = maxY-18;
                else if(tempY > maxY-15) tempY = 5;
                posX = tempX;
                posY = tempY;       
            } else{
                cycles.set(i, cyc-=1);                
            }
        }
        if(fireCycle == 0){
            accelerated = false;
            fireCycle = 90;
        } else fireCycle--;
       
    }
    
    public void turn(boolean toLeft){                                           //Rotate spaceship       
       if(toLeft){
            if(dir>5) dir-=5;
            else dir = 360;
        } else {
            if(dir < 355) dir +=5;
            else dir = 0;
        }
       angle = Math.toRadians(dir);
    }
    
    public void reset(){                                                        //Reset to center
        this.angle = 0;
        this.dir = 0;
        this.angles.clear();
        this.angleCycle.clear();
        this.cycles.clear();
        this.posX = maxX/2;
        this.posY = maxY/2;
        this.speedPlus = 0;
    } 
    
    public void reset(int x, int y){                                            //Reset ship to another location
        this.angle = 0;
        this.dir = 0;
        this.angles.clear();
        this.angleCycle.clear();
        this.cycles.clear();
        this.posX = x;
        this.posY = y;
        this.speedPlus = 0;
    }
    
    
    
    public void setMax(int maxX, int maxY){                                     //Unused now
        this.maxX = maxX;
        this.maxY = maxY;
        repaint();
    }
    
    public boolean getShot(int x, int y){                                       //True if get shot by bullet
        if(x>posX && x < posX+ size){
            if(y>posY && y < posY + size) return true;
        }
        return false;
    }
}
