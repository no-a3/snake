import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import javax.swing.*;
import java.util.Random;


import javax.swing.JPanel;

/**
 *
 * @author USER
 */
public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH=600;
    static final int SCREEN_HEIGHT=600;
    static final int UNIT_SIZE=25;  //demension 25 pixels for every item
    static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; //how mane obj
    static final int DELAY=80; //time
    final int x[]=new int[GAME_UNITS]; //hold coordinates for body like head
    final int y[]=new int[GAME_UNITS];
    int bodyParts=6; //amount of snake body in first
    int appleEaten;
    int appleX; //where apple located and apear when eat apple
    int appleY;
    char direction='R'; //snake begain by going right
    boolean running=false;
    Timer timer;
    Random random;
    
    String line = new String();
    
   private String bestScore = "";

    GamePanel(){  //start constract
        random=new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame(); //start game method

    }

	public void startGame(){  //method to start the game
        newApple(); //to call it
        running=true; //boolen
        timer=new Timer(DELAY,this); //dictate how fast the game will run. this is action listener interface
        timer.start();
	
        	

    }
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        draw(g);
        
        if (bestScore.equals("")) {
         	bestScore=this.getBestScore();
         	
         }

    }
    
    
    public void draw(Graphics g){ 
         //grid just to make it easy
    if (running) { //if game is run do all this
        for (int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){ //for loop as long as i less than the screen hight devided by size to draw lines across panel. increment i by 1 each time
                    //draw line x axis y axis to make squars
                        //x1   //y1  //x2  //y2
        g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
        g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
        
           
        } 
        //draw apple
        g.setColor(Color.white); 
        g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //oval for circal, this how much the apple large

        //draw snake head and body
        for (int i=0;i<bodyParts;i++){
            if (i==0) { //make head 
                g.setColor(Color.gray);
                g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE); //fill rect for rectangle method
            }
            else {  //if i not = 0 make body
                g.setColor(Color.magenta);
                g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

            }
        }
        
        //draw the score
        g.setColor(Color.red);
    	g.setFont(new Font("Times New Roman", Font.BOLD, 20));
    	FontMetrics FontM=getFontMetrics(g.getFont());  //aligning text in the center of screen
    	g.drawString("Score: "+appleEaten, (SCREEN_WIDTH-FontM.stringWidth("Score: "+appleEaten))/2, g.getFont().getSize());//score in top

    	g.drawString("Best score: "+bestScore, (SCREEN_WIDTH-FontM.stringWidth("Best score: "+bestScore)), g.getFont().getSize());
    	
    	
    }
    else { //call game over method
    	gameOver(g); //g is the graphics I receiving with this parameter
    }
    }
    public void newApple(){ //for start the game and eat apple
                                //int to do not break
        appleX=random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; //veriable x coordinate // along x axis
        appleY=random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;  //along y axis
        
    }
    public void move(){ //method to move snake
        //as long as y>0, decrement i by 1 after each iteration
        for (int i=bodyParts;i>0;i--){ //iterate through body parts
            x[i]=x[i-1]; //shift body parts, for x axis
            y[i]=y[i-1]; //for y axis
        }
        //switch to change direction of snake
        switch(direction) { //diraction is character value store R right L left U up D down
        //case any of the diractions happen
        case 'U':     
            y[0]=y[0]-UNIT_SIZE;  //y coordinate of head, - unit size to go to next position and then break
            break;
        case 'D':     
            y[0]=y[0]+UNIT_SIZE;  
            break;
        case 'L':     
            x[0]=x[0]-UNIT_SIZE;  
            break;
        case 'R':     
            x[0]=x[0]+UNIT_SIZE;  
            break;
                    
        }


    }
    public void checkApple(){   //score of eat points
    	if ((x[0]==appleX)&& (y[0]==appleY)) {  //increase amount of body parts when eat apple
    		bodyParts++;
    		appleEaten++;  //score
    		newApple();  //increase amount of apple each time
    		
    	}
    	
        
    }  
    public void checkCollisions(){
    	for (int i=bodyParts;i>0;i--) { //if i>0 we decerment i one after each iteration
    		if ((x[0]==x[i])&& (y[0]==y[i])) {  //to make sure the head colicded with body
    			running=false;
    		}
    		
    	}
    	//check if head touch left boreder
    	if (x[0]<0) {
    		running=false;
    	}
    	//check if head touch right boreder
    	if (x[0]>SCREEN_WIDTH) {
    		running=false;
    	}
    	//check if head touch top boreder
    	if (y[0]<0) {
    		running=false;
    	}
    	//check if head touch left boreder
    	if (y[0]>SCREEN_HEIGHT) {
    		running=false;
    	}
    	if (!running) { //if running false
    		timer.stop();
    		
    	}

    }
    
    
    public String getBestScore() {
    	
    	 
    	FileReader readFile=null;
        BufferedReader reader=null;

          try {
              readFile=new FileReader("C:\\Users\\USER\\eclipse-workspace\\SnakeGame\\src\\score.txt");
              reader=new BufferedReader(readFile);
              
            
             
              return reader.readLine(); }
          catch (Exception e) {
        	  return "00";
          }
          
          finally {
        	  try {
        		  
        		  if (reader!=null)
        		  reader.close();
        	  } catch (IOException e) {
        		  e.printStackTrace();
        	  }
          }
    }
    
   
    
    public void gameOver(Graphics g){
    	
    	try{
            PrintWriter writer = new PrintWriter(new FileWriter("C:\\Users\\USER\\eclipse-workspace\\SnakeGame\\src\\score.txt", true));
            writer.println(appleEaten);              
            writer.close();
            } catch(Exception ex){ex.printStackTrace();}//creates .txt file

      
    
    	//game over text
    	g.setColor(Color.red);
    	g.setFont(new Font("Times New Roman", Font.BOLD, 70));
    	FontMetrics FontM=getFontMetrics(g.getFont());  //aligning text in the center of screen
    	g.drawString("GAME OVER" , (SCREEN_WIDTH-FontM.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);  //use g draw string to pass string x and y coordinate

    }

    @Override
    public void actionPerformed(ActionEvent e) { //call move function
        
        if (running) { //the game run then true //boolen
            move(); //first we move snake
            checkApple();  //see if we ran into apple
            checkCollisions();//check collision

        }
        repaint();  //game no longer running call repaint method

     }
    private class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

     
        	switch(e.getKeyCode()) { //examine e key evevnt and get key code
              //one case for each arrow keys without able to turn 180 degrees oppisite because this go to the body
        	case KeyEvent.VK_LEFT:  //limit move to only 90 degree
        		if (direction !='R') {  //if direction not to right then go to left
        			direction='L';
        	}
        		break;
        	case KeyEvent.VK_RIGHT:  
        		if (direction !='L') {  //if direction not to left then go to right
        			direction='R';
        	}
        		break;
        	case KeyEvent.VK_UP:  
        		if (direction !='D') {  //if direction not to down then go to up
        			direction='U';
        	}
        		break;
        	case KeyEvent.VK_DOWN:  
        		if (direction !='U') {  //if direction not to up then go to down
        			direction='D';
        	}
        		break;
        	}

        }

    
     }}
