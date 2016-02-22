
package jasteroidblaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

class ImagePanel extends JComponent {
    private BufferedImage image;
    public ImagePanel(BufferedImage image) {
        this.image = image;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0,1020,700, this);        
    }
}

public class AsteroidGUI extends javax.swing.JFrame implements Runnable{

    boolean canshot = true;
    boolean threadRun = true;
    boolean isEnd = false;     
    boolean isPaused = false;
    boolean addedLabel = false;
    boolean ufoSpawned = false;
    boolean generated = false;  
    boolean upPushed = false;
    boolean rightPushed = false;
    boolean leftPushed = false;
    byte lifes;
    byte lifesMax;
    ImagePanel ip;
    int addedLabelCount = 0;
    int shotAsteroids = 0;
    int score = 0;
    int xSize = 1020;
    int ySize = 700; 
    int keyCycle = 20;
    int turnCycle = 11;
    JLabel displayer = new JLabel();
    List<Meteor> meteors;
    List<Bullet> bullets;       
    SpaceShip sh;
    Thread update;
    UFO ufoShip;
    
    public AsteroidGUI(int diff) {                              
        if(diff==0) this.lifes = 10;
        else if(diff==1) this.lifes = 5;
        else this.lifes = 2;
        lifesMax = lifes;
        BufferedImage back = null;
        try{
            back = ImageIO.read(this.getClass().getResource("/images/back.jpg"));
            
        } catch (Exception e){
            System.out.println("Background image not found!");
        }
        ip = new ImagePanel(back);
        this.setContentPane(ip);
        init();             
        this.revalidate();       
        this.repaint();
        this.addKeyListener(new KeyAdapter() {       //Keyboard
                       
            public void keyPressed(KeyEvent e){
                
                     int keyCode = e.getKeyCode();
                    if((keyCode==38 || keyCode==87)&& !isEnd){
                        //sh.accelerate();
                        upPushed = true;
                    }
                    else if((keyCode==40 || keyCode==83) && !isEnd){

                    }
                    else if((keyCode==39 || keyCode==68)&& !isEnd){
                        //sh.turn(false);
                        rightPushed = true;
                    }
                    else if((keyCode==37 || keyCode==65)&& !isEnd){
                        //sh.turn(true);
                        leftPushed = true;
                    }
                    else if(keyCode==32 && !isEnd && canshot){
                        bullets.add(new Bullet(xSize,ySize,(int)sh.posX+15,(int)sh.posY+15,sh.angle,true));                       

                        addedLabel = true;
                        addedLabelCount++;

                    }
                    else if(keyCode==80 && !isEnd){
                        if(!isPaused) isPaused = true;
                        else isPaused = false;
                    }
                }
                

                
                
            
                
            public void keyReleased(KeyEvent e){
                int keyCode = e.getKeyCode();
                if((keyCode==38 || keyCode==87)&& !isEnd){
                    //sh.accelerate();
                    upPushed = false;
                }
                else if((keyCode==40 || keyCode==83) && !isEnd){
                  
                }
                else if((keyCode==39 || keyCode==68)&& !isEnd){
                    //sh.turn(false);
                    rightPushed = false;
                }
                else if((keyCode==37 || keyCode==65)&& !isEnd){
                    //sh.turn(true);
                    leftPushed = false;
                }
            }
        });
        meteors = new ArrayList();        
        bullets = new ArrayList();       
        update = new Thread(this, "update");
        update.start();
        //this.generate();
            
    }
    
    public void run(){
        while(update!=null && threadRun){
            if(!isPaused){
                if(isEnd){
                    String[] buttons = { "Again", "Exit"};      //Exit window
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
                    this.writeOut();
                    try{
                        Thread.sleep(2);
                    } catch (Exception e){
                        System.out.println("Sleep exception! Line: 140");
                    }
                }
            }
        }
    }
    
    private void resetPanel(){                                                    //New game reset
        this.getContentPane().removeAll();
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
        
    private void writeOut(){                                                        //Main game run
        if(keyCycle == 0){
            if(upPushed) sh.accelerate();            
            keyCycle = 20;
        } else keyCycle--;
        if(turnCycle == 0){
            if(leftPushed) sh.turn(true);
            if(rightPushed) sh.turn(false);
            turnCycle = 11;
        } else turnCycle--;
        displayer.setText("Score: " + this.score + " Lifes: " + this.lifes);       
        if(this.lifes == 0) isEnd = true;
         sh.move2(); 
        if(ufoShip!=null){
            if(ufoShip.shotCycle == 0){
                ufoShip.shotCycle = 60;
                int[] locations;
                locations = ufoShip.shot();
                Bullet b = new Bullet(xSize, ySize, locations[0], locations[1], locations[2], false);                
                bullets.add(b);                
                this.getContentPane().add(b);
            } else ufoShip.shotCycle--;
            if(ufoShip.move()){
                this.getContentPane().remove(ufoShip);
                ufoShip = null;
                ufoSpawned = false;
                this.validate();
            } else {
                if(ufoShip.checkCollision((int)sh.posX, (int)sh.posY, sh.size)) this.resetShip();
            }
        }
        if(score%15==5 && !ufoSpawned){
            ufoShip = new UFO(xSize, ySize);                       
            this.getContentPane().add(ufoShip);  
            this.validate();
            ufoShip.alive = true;
            ufoSpawned = true;           
        }
        
        if(addedLabel) this.updateLabels();
        this.moveMeteors();
        this.moveBullets();
        this.shotCheck();           
        if(shotAsteroids==4){
            Meteor m = new Meteor(xSize,ySize,10,10, 3,-5);
            meteors.add(m);
            this.getContentPane().add(m);
            this.validate();
            shotAsteroids = 0;
        }
        this.getContentPane().repaint();        
    }
    
    public void gameRun(){
      this.generate();        
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
            this.getContentPane().add(topleft);
            this.validate();
            this.repaint();
            this.getContentPane().add(topright);
            this.validate();
            this.repaint();
            this.getContentPane().add(lowleft);
            this.validate();
            this.repaint();
            this.getContentPane().add(lowright);
            this.validate();
            this.repaint();
            
            generated = true;
        }          
    }
    
    private void updateLabels(){                                                //Add new bullets to contentPane
        for(int i = bullets.size()-addedLabelCount; i < bullets.size(); i++) {
            this.getContentPane().add((bullets.get(i)), BorderLayout.CENTER); 
            this.validate();
        }
        addedLabelCount = 0;
        addedLabel = false;
    }
    
    private void resetShip(){
        lifes--;
        upPushed = false;
        leftPushed = false;
        rightPushed = false;
        for(int x = 500; x < xSize; x+=50){
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
        for(int i = 0; i < meteors.size();i++){                             //Move meteors                
                Meteor m = meteors.get(i);
                m.move();               
                if(m.checkCollision((int)sh.posX, (int)sh.posY, sh.size)){                    
                    this.resetShip();                    
                } 
                if(ufoShip != null){
                    if(m.checkCollision(ufoShip.posX, ufoShip.posY, ufoShip.size)){
                        ufoShip.alive = false;
                        this.getContentPane().remove(ufoShip);  
                        ufoShip = null;      
                        ufoSpawned = false;
                    }
                }
            }
    }
    
    private void moveBullets(){
        for(int i = 0; i < bullets.size();i++){                             //Move bullets
           try{
                Bullet b = bullets.get(i);
                if(!b.move()){                
                    this.getContentPane().remove(b);
                    bullets.remove(b);       
                    this.validate();
                } 
           } catch (java.lang.NullPointerException e){
               
           }            
        }        
    }
    
    private void shotCheck(){
    for(int i = 0; i< meteors.size();i++){                              //Meteor get shot
        Meteor  m = meteors.get(i);
        for(int j = 0; j < bullets.size();j++){
            Bullet b = bullets.get(j);
            if(m.shot((int)b.posX, (int)b.posY)){
                if(m.type>1){
                    double direction1;
                    double direction2;
                    if(b.direction+1>6) direction1 = 1;
                    else direction1 = (int)b.direction+1;
                    if(b.direction-1<1) direction2 = 6;
                    else direction2 = (int)b.direction-1;
                    Meteor m2 = new Meteor(xSize,ySize,m.posX,m.posY,m.type-1, direction1);
                    Meteor m3 = new Meteor(xSize,ySize,m.posX,m.posY,m.type-1, direction2);
                    meteors.add(m2);
                    meteors.add(m3);                         
                    this.getContentPane().add(m2);
                    this.validate();
                    //this.repaint();
                    this.getContentPane().add(m3);    
                    this.validate();
                    //this.repaint();
                    this.getContentPane().remove(b);
                    meteors.remove(i);                    
                    bullets.remove(j);                    
                    if(b.fromPlayer)score++;
                    this.getContentPane().remove(m);  
               } else {                    
                   this.getContentPane().remove(m);                   
                    this.getContentPane().remove(b);
                    meteors.remove(i);                   
                    bullets.remove(j);                    
                    shotAsteroids++;
                    if(b.fromPlayer)score++;
                }
            }
            if(b.fromPlayer && ufoShip != null){
                if(ufoShip.getShot((int)b.posX, (int)b.posY)){
                    this.getContentPane().remove(ufoShip);                    
                    bullets.remove(b);      
                    this.getContentPane().remove(b);
                    score+=25;
                    ufoSpawned = false;
                    ufoShip = null;
                }
            } else {
                if(!b.fromPlayer && sh.getShot((int)b.posX, (int)b.posY)){
                    this.resetShip();                      
                    bullets.remove(j);  
                    this.getContentPane().remove(b);
                }
            }
          }
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
        setSize(1020, 700);
        this.getContentPane().setBackground(Color.black);
        this.setLayout(new BorderLayout());
        this.setTitle("Asteroid Blaster");
        this.setResizable(false);         
         displayer.setLocation(450, 0);
        displayer.setFont(new java.awt.Font("LCD", 0, 28));
        displayer.setForeground(Color.green);
        displayer.setOpaque(false);
        displayer.setSize(200, 24);
        displayer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        this.getContentPane().add(displayer);
        sh = new SpaceShip(xSize,ySize,30);               
        this.getContentPane().add(sh);        
        this.validate();
        this.repaint();
       
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

//class KeyBoard extends KeyEvent implements Runnable
