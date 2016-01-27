/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jasteroidblaster;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author Mate1
 */
public class SpaceShip extends JComponent{
    
    boolean running = true;   
    boolean accelerated = false;
    Image up;    
    Image upF;
    int fireCycle = 20;                                                          //Blow time
    int maxX;                                                                    //Window size(width)
    int maxY;                                                                    //Window size(height);
    int posX;                                                                    //Spaceship position
    int posY;                                
    int[] directions = {10, 10, 10, 10, 10, 10, 10, 10};   
    int[] cycles = {0, 0, 0, 0, 0, 0, 0, 0};   
    int size;                                                                   //Size of spaceship in pixel
    int cycle;
    int newDirection;                                                    //1-up, 2 up right, 3 right, 4 down right, 5 down, 6 down left, 7 down, 8 uo left
    final int[] xDirs = { 0,  1, 1, 1, 0, -1, -1, -1};
    final int[] yDirs = {-1, -1, 0, 1, 1,  1,  0, -1};
    
    public SpaceShip(int maxX, int maxY, int size){                       //Constructor
        this.maxX = maxX;
        this.maxY = maxY;
        this.posX = maxX/2;
        this.posY = maxY/2;       
        this.size = size;
        this.cycle = 5;
        this.newDirection = 1;
        try{
            up = ImageIO.read(this.getClass().getResource("/images/r-1.gif"));
            upF = ImageIO.read(this.getClass().getResource("/images/r-1F.gif"));
        } catch(Exception e){
            System.out.println("SpaceShip picture not found!");
        }
    }    
    
    public void paintComponent(Graphics gr){                                    //Paint method
        Graphics2D g2d = (Graphics2D)gr.create();
        g2d.rotate(Math.toRadians(this.shipRotationDeg()), posX+15, posY+15); 
        
        if(accelerated){
            g2d.drawImage(upF, posX, posY, size, size, this);             
        } else g2d.drawImage(up, posX, posY,size, size, this);        
        g2d.dispose();
    }
    
    public void accelerate(){                                                   //Accelerate
        if(directions[newDirection-1]>2) directions[newDirection-1]-=1;        
        for(int i = 0; i < 8; i++) if(newDirection-1 != i && directions[i]<10){
            directions[i]++;
            cycles[i] = directions[i];
        }
        accelerated = true;
    }
    
    public void move2(){                                                        //Move ship
       
        for(int i = 0; i < 8; i++){
            if(directions[i]<10){
                if(cycles[i]==0){                     
                    cycles[i] = directions[i];
                    if(xDirs[i]==1){
                        if(posX+2<maxX-1) posX+=2;
                        else posX = 5;
                    } else if(xDirs[i]==-1){
                        if(posX-2>0) posX-=2;
                        else posX = maxX-5;
                    }
                    if(yDirs[i]==1){
                        if(posY+2<maxY-1) posY+=2;
                        else posY = 5;
                    } else if(yDirs[i]==-1){
                        if(posY-2>0) posY-=2;
                        else posY = maxY-5;
                    }
                } else cycles[i]--;
            }
        }
        if(fireCycle == 0){
            accelerated = false;
            fireCycle = 80;
        } else fireCycle--;       
        //repaint();
    }
    
    public void turn(boolean toLeft){                           //Rotate spaceship       
       if(toLeft){
            if(newDirection==1) newDirection = 8;
            else if(newDirection>1) newDirection--;
        } else {
            if(newDirection==8) newDirection = 1;
            else if(newDirection<8) newDirection++;
        }
    }
    
    public void reset(){
        for(int i = 0; i < 8; i++){
            directions[i] = 10;
            cycles[i] = 0;
        }
        this.newDirection = 1;
        this.posX = maxX/2;
        this.posY = maxY/2;
    } 
    
    public void reset(int x, int y){
        for(int i = 0; i < 8; i++){
            directions[i] = 10;
            cycles[i] = 0;
        }
        this.newDirection = 1;
        this.posX = x;
        this.posY = y;
    }
    
    
    private int shipRotationDeg(){
        if(newDirection == 1) return 0;
        else if(newDirection == 2) return 45;
        else if(newDirection == 3) return 90;
        else if(newDirection == 4) return 135;
        else if(newDirection == 5) return 180;
        else if(newDirection == 6) return 225;
        else if(newDirection == 7) return 270;
        else if(newDirection == 8) return 315;
        else return 0;
    }
    public void setMax(int maxX, int maxY){
        this.maxX = maxX;
        this.maxY = maxY;
        repaint();
    }
    
    public boolean getShot(int x, int y){
        if(x>posX && x < posX+ size){
            if(y>posY && y < posY + size) return true;
        }
        return false;
    }
}
