/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jasteroidblaster;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author Mate1
 */
public class Meteor extends JComponent implements Runnable{
    
    BufferedImage small;
    BufferedImage medium;
    BufferedImage large;
    double direction;
    int maxX;
    int maxY;
    double posX;
    double posY;
    int type;              //1-small, 2 medium, 3, large
    int speed;              // small - 3, medium 6, large 15
    int cycle;
    int size;
    Thread load;
    
    
    public Meteor(int maxX, int maxY, double posX, double posY, int type, double direction){
        this.maxX = maxX;
        this.maxY = maxY;
        this.posX = posX;
        this.posY = posY;
        this.type = type;
        if(type==1){
            speed = ThreadLocalRandom.current().nextInt(2, 5);
            size = 25;
        }
        else if(type==2){
            speed = ThreadLocalRandom.current().nextInt(5, 8);;
            size = 50;
        }
        else if(type==3){
            speed = ThreadLocalRandom.current().nextInt(10, 14);;
            size = 100;
        }                    
        if(direction == -5) this.direction = ThreadLocalRandom.current().nextDouble(0, 6);   
        else this.direction = direction;
        load = new Thread(this, "Load Meteor");
        load.start();
    }   
    
    public void run(){
        try{
            if(type==1) small = ImageIO.read(this.getClass().getResource("/images/m-1.gif"));
            else if(type==2) medium = ImageIO.read(this.getClass().getResource("/images/m-2.gif"));
            else large = ImageIO.read(this.getClass().getResource("/images/m-3.gif"));
        } catch (Exception e){
            System.out.println("Meteor images not found!");
        }
    }
    
    public void paintComponent(Graphics gr){ 
        super.paintComponent(gr);
        if(this.type==1) gr.drawImage(small, (int)posX, (int)posY,size, size, this);
        else if(this.type==2) gr.drawImage(medium, (int)posX, (int)posY,size, size, this);
        else if(this.type==3) gr.drawImage(large, (int)posX, (int)posY,size, size, this);            
    }
    
    public void move(){
        if(cycle==0){
            cycle=speed;
            double tempX = posX;
            double tempY = posY;
            tempX+=Math.sin(direction);
            tempY-=Math.cos(direction);
            if(tempX < 0) tempX = maxX-10;
            else if(tempX > maxX-10) tempX = 10;
            if(tempY < 0 ) tempY = maxY-10;
            else if(tempY > maxY-10) tempY = 10;
            posX = tempX;
            posY = tempY;            
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
