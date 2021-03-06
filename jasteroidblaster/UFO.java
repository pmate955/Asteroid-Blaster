/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jasteroidblaster;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author Mate1
 */
public class UFO extends JComponent{
    
    boolean alive;
    Image icon;
    int maxX;
    int maxY;
    int posX;
    int posY;
    int size;
    int cycle;
    int shotCycle;
    int speed;
    boolean toLeft;
    
    public UFO(int maxX, int maxY){
        this.maxX = maxX;
        this.maxY = maxY;
        this.size = 40;
        this.posY = ThreadLocalRandom.current().nextInt(0, maxY-100);
        this.cycle = 5;
        this.shotCycle = 30;
        this.speed = 5;
        this.alive = false;
        try{
            icon = ImageIO.read(this.getClass().getResource("/images/UFO.png"));
        } catch (Exception e){
            System.out.println("UFO image not found");
        }        
        toLeft = ThreadLocalRandom.current().nextBoolean();
        if(!toLeft) this.posX = 0-size;
        else this.posX = maxX;
    }
    public void paintComponent(Graphics gr){
        super.paintComponent(gr);
        gr.drawImage(icon, posX, posY,size, size, this);
        gr.setColor(Color.red);
        gr.setFont(new Font("Times New Roman", Font.BOLD, 12));
        gr.drawString("UFO alert!", (int)posX+size, (int)posY+size);
        
    }
    public void reset(){
        this.posX = 0-size;
        this.posY = ThreadLocalRandom.current().nextInt(0, maxY);
    }
    
    
    
    public boolean move(){
        boolean isDead = false;
        if(!toLeft){
            if(cycle == 0) {
                if(posX+2+size < maxX-1) posX += 2;
                else isDead = true;
                cycle = speed;
            } else cycle--;
        } else {
            if(cycle == 0) {
                if(posX-2 >= 0) posX -= 2;
                else isDead = true;
                cycle = speed;
            } else cycle--;
        }
        repaint();
        return isDead;
    }
    
    public int[] shot(){                                        //0 - posX, 1 - posY, 2 - direction
        int[] positions = new int[3];
        int direction = ThreadLocalRandom.current().nextInt(1, 6);
        positions[2] = direction;
        if(direction == 1) {
            positions[0] = posX+(size/2);
            positions[1] = posY;
        } else if(direction == 2){
            positions[0] = posX+size;
            positions[1] = posY;
        } else if(direction == 3){
            positions[0] = posX+size;
            positions[1] = posY+(size/2);
        } else if(direction == 4){
            positions[0] = posX+size;
            positions[1] = posY+size;
        } else if(direction == 5){
            positions[0] = posX+(size/2);
            positions[1] = posY+size;
        } else if(direction == 6){
            positions[0] = posX;
            positions[1] = posY+size;
        } else if(direction == 7){
            positions[0] = posX;
            positions[1] = posY+(size/2);
        } else {
            positions[0] = posX;
            positions[1] = posY;
        }
        return positions;
    }
    
    public boolean getShot(int x, int y){
        if(x>posX && x < posX+ size){
            if(y>posY && y < posY + size) return true;
        }
        return false;
    }
    
    public void setMax(int maxX, int maxY){
        this.maxX = maxX;
        this.maxY = maxY;
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
}
