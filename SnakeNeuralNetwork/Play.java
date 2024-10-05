import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;

import kareltherobot.Directions;

public class Play {
	
	static int direction = 0;
	
	static boolean hasChange = false;
	
	public static void main(String[] args) {
		
		
		
		Engine engine = new Engine();
		
		WorldViewer viewer = new WorldViewer(engine.getWorldSize());
		
		
		JTextField textField = new JTextField();
		 
		textField.addKeyListener(new MKeyListener());
 
		JFrame jframe = new JFrame();
 
		jframe.add(textField);
 
		jframe.setSize(1, 1);
 
		jframe.setVisible(true);
  
		jframe.setLocation(1000, 0);
		
		
		boolean isAlive = true;
		
		viewer.view(engine.getWorld());
		WorldViewer.wait(1000);
		
		while (isAlive) {
			engine.changeDirection(direction);
			hasChange = false;
			isAlive = engine.moveSnake();
			viewer.view(engine.getWorld());
			WorldViewer.wait(400);
		}
		
	}
}

class MKeyListener extends KeyAdapter implements Directions {
	 
    @Override
    public void keyPressed(KeyEvent event) {
    	
    	if (Play.hasChange)
    		return;
 
	    char ch = event.getKeyChar();
	    
	    if (ch == 'w' && Play.direction != 3)
	    	Play.direction = 1;
	    else if (ch == 'a' && Play.direction != 0)
	    	Play.direction = 2;
	    else if (ch == 's' && Play.direction != 1)
	    	Play.direction = 3;
	    else if (ch == 'd' && Play.direction != 2)
	    	Play.direction = 0;
	    else
	    	return;
	    Play.hasChange = true;
    
    }
}
