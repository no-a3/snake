
import javax.swing.JFrame;

/**
 *
 * @author USER
 */
public class GameFrame extends JFrame {

    GameFrame(){
    	
    	

        this.add(new GamePanel());
        this.setTitle("Snake game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack(); //take j frame and fit it around all component in frame
        this.setVisible(true);
        this.setLocationRelativeTo(null);  //to make window come in the middle of the screen
        

    }
    
}