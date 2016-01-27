/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jasteroidblaster;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author Mate1
 */
public class Meteor extends JComponent{
    
    BufferedImage small;
    BufferedImage medium;
    BufferedImage large;
    int direction;
    int maxX;
    int maxY;
    int posX;
    int posY;
    int type;              //1-small, 2 medium, 3, large
    int speed;              // small - 3, medium 6, large 15
    int cycle;
    int size;
    
    public Meteor(int maxX, int maxY, int posX, int posY, int type, int direction){
        this.maxX = maxX;
        this.maxY = maxY;
        this.posX = posX;
        this.posY = posY;
        this.type = type;
        if(type==1){
            speed = 3;
            size = 25;
        }
        else if(type==2){
            speed = 6;
            size = 50;
        }
        else if(type==3){
            speed = 12;
            size = 100;
        }
        try{
            small = ImageIO.read(this.getClass().getResource("/images/m-1.gif"));
            medium = ImageIO.read(this.getClass().getResource("/images/m-2.gif"));
            large = ImageIO.read(this.getClass().getResource("/images/m-3.gif"));
        } catch (Exception e){
            System.out.println("MEteor images not found!");
        }
        if(direction == 0) this.direction = ThreadLocalRandom.current().nextInt(1, 9);   
        else this.direction = direction;
    }   
    
    public void paintComponent(Graphics gr){ 
       
        Graphics2D g2d = (Graphics2D)gr.create();
        if(this.type==1) g2d.drawImage(small, posX, posY,size, size, this);
        else if(this.type==2) g2d.drawImage(medium, posX, posY,size, size, this);
        else if(this.type==3) g2d.drawImage(large, posX, posY,size, size, this);
        //g2d.dispose();        
    }
    
    public void move(){
        if(cycle==0){
            cycle=speed;
            if(direction==1){                       //Up
                if(posY-1>=0) posY--;
                else posY=maxY-2;
            } else if(direction==2){                //Up Right
                if(posX+1<maxX-2) posX++;
                else posX = 2;
                if(posY-1>=0) posY--;
                else posY = maxY-2;
            } else if(direction==3){                //Right
                if(posX+1<maxX-2) posX++;
                else posX=2;
            } else if(direction==4){                //Down Right
                if(posX+1<maxX-2) posX++;
                else posX=2;
                if(posY+1<maxY-2) posY++;
                else posY=2;
            } else if(direction==5){                //Down
                if(posY+1<maxY-2) posY++;
                else posY=2;                
            } else if(direction==6){                //Down Left
                if(posY+1<maxY-2) posY++;
                else posY = 2;
                if(posX-1>=0) posX--;
                else posX = maxX-2;
            } else if(direction==7){                //Left
                if(posX-1>=0) posX--;
                else posX = maxX-2;
            } else if(direction==8){                //Up left
                if(posX-1>=0) posX--;
                else posX = maxX-2;
                if(posY-1>=0) posY--;
                else posY = maxY-2;
            }
             //repaint();
        } else cycle--;
       
    }
    
    public boolean checkCollision(int x, int y, int spSize){                //Collision w/ spaceship
        boolean dead = false;
        if(x>posX && x < posX+size){                            //left corners
            if(y>posY && y < posY+size){
                dead = true;
            }
            if(y+spSize > posY && y + spSize < posY + size) dead = true;
            
        }   
        if(x+5>posX && x+5 < posX+size){                        //Right corners
            if(y>posY && y < posY+size){
                dead = true;
            }
            if(y+spSize > posY && y + spSize < posY + size) dead = true;
        }
        if(x>posX-spSize  && x < posX+ size && y>posY && y<posY+size) dead = true;
        return dead;
    }
    
    public boolean shot(int x, int y){                                          //Shot by bullet
        boolean getShot = false;
        if(x>posX && x < posX+size){
            if(y>posY && y < posY+size) getShot = true;
        }
        return getShot;
    }    
    
    public void setMax(int maxX, int maxY){
        this.maxX = maxX;
        this.maxY = maxY;
    }
}
