
package jasteroidblaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

class ImagePanel extends JComponent{                                            //Background image Component
    private BufferedImage image;                                                
    private int maxX = 1020;
    private int maxY = 700;
    
    public ImagePanel() {        
        try{
            image = ImageIO.read(this.getClass().getResource("/images/back.jpg"));            
        } catch (Exception e){
            System.out.println("Background image not found!");
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {                                 //Paint method for background
        g.drawImage(image, 0, 0,maxX,maxY, this);           
    }    
    
    public void setMax(int maxX, int maxY){                                     //Resize
        this.maxX = maxX;
        this.maxY = maxY;
    }
}

public class AsteroidGUI extends javax.swing.JFrame implements Runnable, KeyListener{

    boolean shotPushed = false;                                                 //Game variables
    boolean threadRun = true;
    boolean isEnd = false;     
    boolean isPaused = false;
    boolean ufoSpawned = false;
    boolean generated = false;  
    boolean upPushed = false;
    boolean rightPushed = false;
    boolean leftPushed = false;
    byte lifes;
    byte lifesMax;
    byte turnCount = 0;
    ImagePanel ip;                                                              // ip -> Background Panel      
    int shotAsteroids = 0;
    int score = 0;
    int xSize = 1020;
    int ySize = 700; 
    int keyCycle = 22;
    int turnCycle = 8;
    int shotcycle = 7;
    int chanceToUfo = 15;
    List<Meteor> meteors;
    List<Bullet> bullets;   
    List<Debris> debrises;
    SpaceShip sh;
    Thread update;                                                              //Game run thread
    UFO ufoShip;
    
    public AsteroidGUI(int diff) {                                              //Constructor
        if(diff==0){
            this.lifes = 10;
            this.chanceToUfo = 50;
        }
        else if(diff==1){
            this.lifes = 5;
            this.chanceToUfo = 20;
        }
        else{
            this.lifes = 2;
            this.chanceToUfo = 10;
        }
        lifesMax = lifes;        
        ip = new ImagePanel();
        this.setContentPane(ip);                                                //Set the background Panel as contentPane
        init();          
        this.revalidate();       
        this.repaint();
        addKeyListener(this);
        meteors = new ArrayList();        
        bullets = new ArrayList();     
        debrises = new ArrayList();
        update = new Thread(this, "update");
        update.start();
        //this.generate();
            
    }
    
    public void run(){                                                          //Update thread
        while(update!=null && threadRun){
            if(!isPaused){
                if(isEnd){
                    String[] buttons = { "Again", "Exit"};                      //Exit window
                    int returnValue = JOptionPane.showOptionDialog(null, "Score: " + this.score, "Game Over",
                    JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[0]);
                    if(returnValue == 0){
                        this.resetPanel();
                        isEnd = false;                        
                    } else {
                        threadRun = false;
                        this.dispose();
                    }
                } else {
                    this.writeOut();                                            //Run game
                }
            }
            this.waiting();
        }
    }
    
    private void waiting(){
       try{
            Thread.sleep(2);
        } catch (Exception e){
            System.out.println("Sleep exception!");
        }
    }
                                                                                    //KeyListener methods
    public void keyPressed(KeyEvent e){                                 //Pressed                
        int keyCode = e.getKeyCode();
        if((keyCode==38 || keyCode==87)&& !isEnd){                  //Up (Forward)         
            upPushed = true;
        }                    
        else if((keyCode==39 || keyCode==68)&& !isEnd){             //Right (Turn)
            rightPushed = true;
        }
        else if((keyCode==37 || keyCode==65)&& !isEnd){             //Left (Turn)
            leftPushed = true;
        }
        else if(keyCode==32 && !isEnd){
            if(!shotPushed) {
                Bullet b = new Bullet(xSize,ySize,(int)sh.posX+15,(int)sh.posY+15,sh.angle,true);
                bullets.add(b);
                ip.add(b);
                this.validate();
            }
        shotPushed = true;                        
        }
        else if(keyCode==80 && !isEnd){                             //Pause
            isPaused = !isPaused;
            sh.label = "Paused, press P!";
            ip.repaint();
        }
    }    
    
    public void keyReleased(KeyEvent e){                                //Released
        int keyCode = e.getKeyCode();
        if((keyCode==38 || keyCode==87)&& !isEnd){                    
            upPushed = false;
        }
        else if((keyCode==32) && !isEnd){
            shotPushed = false;
        }
        else if((keyCode==39 || keyCode==68)&& !isEnd){                    
            rightPushed = false;
            turnCount = 0;
        }
        else if((keyCode==37 || keyCode==65)&& !isEnd){                    
            leftPushed = false;
            turnCount = 0;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent ke) {
        
    }
    
    private void resetPanel(){                                                    //New game reset
        ip.removeAll();
        this.meteors.clear();
        this.bullets.clear();
        ufoShip = null;
        ufoSpawned = false;
        this.generated = false;
        this.score = 0;  
        this.lifes = this.lifesMax;
        this.init();
        this.gameRun();
       
    }
        
    private void writeOut(){                                                    //Main game run thread
        if(keyCycle == 0){                                                      //Control if button was pushed
            if(upPushed) sh.accelerate();            
            keyCycle = 22;
        } else keyCycle--;
        if(turnCycle == 0){
            byte toDeg = 1;
            if(leftPushed || rightPushed){
                if(turnCount > 5) toDeg = 5;
                if(leftPushed) sh.turn(true, toDeg);
                if(rightPushed) sh.turn(false, toDeg);
                turnCycle = 8;
                turnCount++;                
            }
        } else turnCycle--;                                 
        if(shotcycle == 0){
            if(shotPushed){
                Bullet b = new Bullet(xSize,ySize,(int)sh.posX+15,(int)sh.posY+15,sh.angle,true);
                bullets.add(b);
                ip.add(b);
                this.validate();
            }
            shotcycle = 70;
        } else shotcycle--;      
        if(this.lifes == 0) isEnd = true;                                       
         sh.move2();                                                            //Move spaceShip
         sh.label = "Score: " + score + " Life: " + lifes;         
        if(ufoShip!=null){                                                      //Move UFO object, and shot
            if(ufoShip.shotCycle == 0){
                ufoShip.shotCycle = 60;
                int[] locations;
                locations = ufoShip.shot();
                Bullet b = new Bullet(xSize, ySize, locations[0], locations[1], locations[2], false);                
                bullets.add(b);                
                ip.add(b);
            } else ufoShip.shotCycle--;
            if(ufoShip.move()){
                ip.remove(ufoShip);
                ufoShip = null;
                ufoSpawned = false;
                this.validate();
            } else {
                if(ufoShip.checkCollision((int)sh.posX, (int)sh.posY, sh.size)) this.resetShip();
            }
        }
        if(score%chanceToUfo==5 && !ufoSpawned){                                //Spawn UFO
            ufoShip = new UFO(xSize, ySize);                       
            ip.add(ufoShip);  
            this.validate();
            ufoShip.alive = true;
            ufoSpawned = true;           
        }        
        this.moveMeteors();
        this.moveBullets();
        this.shotCheck();           
        if(shotAsteroids==4){                                                   //Add new Meteor, if shot 4 small
            Meteor m = new Meteor(xSize,ySize,10,10, 3,-5);
            meteors.add(m);
            ip.add(m);
            this.validate();
            shotAsteroids = 0;
        }
        ip.repaint();                                                           //Call background repaint
        if(this.xSize != this.getWidth() || this.ySize != this.getHeight()){    //If resize the window...         
            ip.removeAll();
            if(this.getWidth() > xSize + 200 || this.getHeight() > ySize +200){
                Meteor m = new Meteor(xSize,ySize,10, 10, 3, -5);
                meteors.add(m);
                ip.add(m);
                this.validate();
            }
            this.xSize = this.getWidth();
            this.ySize = this.getHeight();
            ip.add(sh);
            this.validate();
            for(int i = 0; i < meteors.size();i++){
                ip.add(meteors.get(i));
                meteors.get(i).setMax(xSize, ySize);
                this.validate();
            }
           
            sh.setMax(this.getWidth(), this.getHeight());            
            ip.setMax(xSize, ySize);
        }                                                                       
    }
    
    public void gameRun(){
      this.generate();                                                          //Add the first 4 asteroid, called from main
    }
       
    private void generate(){                                                    //Add the first 4 asteroid
        if(!generated){            
            Meteor topleft = new Meteor(xSize,ySize,10,10, 3,-5);
            Meteor topright = new Meteor(xSize,ySize,450,10,3,-5);
            Meteor lowleft = new Meteor(xSize,ySize,10,450, 3,-5);
            Meteor lowright = new Meteor(xSize,ySize,450,450,3,-5);
            meteors.add(topleft);
            meteors.add(topright);
            meteors.add(lowleft);
            meteors.add(lowright);            
            ip.add(topleft);
            this.validate();
            this.repaint();
            ip.add(topright);
            this.validate();
            this.repaint();
            ip.add(lowleft);
            this.validate();
            this.repaint();
            ip.add(lowright);
            this.validate();
            this.repaint();            
            generated = true;
        }          
    }
      
    private void resetShip(){
        lifes--;
        addDebris(sh.posX, sh.posY,30);
        upPushed = false;
        leftPushed = false;
        rightPushed = false;
        shotPushed = false;
        for(int x = xSize/2; x < xSize; x+=50){
            boolean isGood = true;
            for(int i = 0; i < meteors.size();i++){
                 Meteor m = meteors.get(i);
                if(m.checkCollision(x, ySize/2, sh.size)) isGood = false;
            }
            if(isGood){
                sh.reset(x, ySize/2);                
                return;
            }
        }
        sh.reset(400, ySize/2);        
    }
        
    private void moveMeteors(){        
        for(int i = 0; i < meteors.size();i++){                                  //Move meteors                
                Meteor m = meteors.get(i);
                m.move();               
                if(m.checkCollision((int)sh.posX, (int)sh.posY, sh.size)){                     
                    this.resetShip();                    
                } 
                if(ufoShip != null){
                    if(m.checkCollision(ufoShip.posX, ufoShip.posY, ufoShip.size)){
                        addDebris(ufoShip.posX, ufoShip.posY, 50);
                        ufoShip.alive = false;
                        ip.remove(ufoShip);  
                        ufoShip = null;      
                        ufoSpawned = false;
                    }
                }
            }
    }
    
    private void moveBullets(){
        for(int i = 0; i < bullets.size();i++){                                 //Move bullets
           try{
                Bullet b = bullets.get(i);
                if(!b.move()){                
                    ip.remove(b);
                    bullets.remove(b);       
                    this.validate();
                } 
           } catch (java.lang.NullPointerException e){
               
           }            
        }      
        moveDebris();
    }
    
    private void moveDebris(){
        if(!debrises.isEmpty()){
            for(int i = 0; i < debrises.size();i++){
                Debris d = debrises.get(i);
                if(!d.move()){
                    ip.remove(d);
                    debrises.remove(d);
                }
            }
        }
    }
    
    private void shotCheck(){
    for(int i = 0; i< meteors.size();i++){                                      //Meteor get shot
        Meteor  m = meteors.get(i);
        for(int j = 0; j < bullets.size();j++){
            Bullet b = bullets.get(j);
            if(m.shot((int)b.posX, (int)b.posY)){                 
                if(m.type>1){
                    double direction1;
                    double direction2;
                    if(b.direction+0.8>6.28) direction1 = 1;
                    else direction1 = (int)b.direction+1;
                    if(b.direction-0.8<0.1) direction2 = 6;
                    else direction2 = (int)b.direction-1;
                    Meteor m2 = new Meteor(xSize,ySize,m.posX,m.posY,m.type-1, direction1);
                    Meteor m3 = new Meteor(xSize,ySize,m.posX,m.posY,m.type-1, direction2);
                    ip.add(m2);
                    this.validate();
                    ip.add(m3);    
                    this.validate();
                    ip.remove(m);
                    ip.remove(b);  
                    meteors.remove(i);                    
                    bullets.remove(j);                    
                    if(b.fromPlayer)score++;
                     meteors.add(m2);
                    meteors.add(m3);
               } else {             
                   addDebris(m.posX, m.posY, 10);
                   ip.remove(m);                   
                    ip.remove(b);
                    meteors.remove(i);                   
                    bullets.remove(j);                    
                    shotAsteroids++;
                    if(b.fromPlayer)score++;                   
                }          
               
            }
            if(b.fromPlayer && ufoShip != null){                                //Shot the UFO
                if(ufoShip.getShot((int)b.posX, (int)b.posY)){
                     addDebris(ufoShip.posX, ufoShip.posY, 50);
                    ip.remove(ufoShip);                    
                    bullets.remove(b);      
                    ip.remove(b);
                    score+=25;
                    ufoSpawned = false;
                    ufoShip = null;
                }
            } else {
                if(!b.fromPlayer && sh.getShot((int)b.posX, (int)b.posY)){      //We get shoted                    
                    this.resetShip();                      
                    bullets.remove(j);  
                    ip.remove(b);
                }
            }
          }
       }    
    }
    
    private void addDebris(double posX, double posY, int piece){
        for(int l = 0; l < piece; l++){
            double dir = ThreadLocalRandom.current().nextDouble(0, 7);
            Debris d = new Debris(xSize, ySize, posX, posY, dir);
            debrises.add(d);
            ip.add(d);
            this.validate();
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Asteroid Blaster");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 652, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 624, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void init(){
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(xSize, ySize);
        setMinimumSize(new Dimension(1020,700));
        ip.setBackground(Color.black);
        this.setLayout(new BorderLayout());
        this.setTitle("Asteroid Blaster");
        this.setResizable(true);         
        sh = new SpaceShip(xSize,ySize,30);               
        ip.add(sh);        
        this.validate();
        this.repaint();
       
    }

    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}



//class KeyBoard extends KeyEvent implements Runnable
