
import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;

import kareltherobot.*;

public class WorldViewer implements Directions {
	
	private int worldSize;
	
	private KarelCoords head = new KarelCoords(1, 1, East, infinity, Color.GREEN);
	
	private KarelCoords apple = new KarelCoords(1, 1, East, infinity, Color.RED);
	
	public WorldViewer(int worldSize) {
		this.worldSize = worldSize;
		
		World.setVisible();
		World.setDelay(0);
		World.setBeeperColor(Color.GREEN);
		World.setSize(worldSize, worldSize);
	}
	
	public void view(int[][] world) {
		
		head.clearAllAt(1, 1, worldSize, worldSize);
		
		Point headLocation = new Point();
		
		for (int row = 0; row < worldSize; row++) {
			for (int col = 0; col < worldSize; col++) {
				if (world[row][col] == 1)
					head.putAt(col+1, worldSize-row);
				
				else if (world[row][col] == 2) {
					apple.goToCoord(col+1, worldSize-row);
					apple.faceDirection(North);
				}
				
				else if (world[row][col] != 0)
					headLocation.move(col, worldSize-row);
			}
		}
		
		head.goToCoord(headLocation.x+1, headLocation.y);
		
		if (world[worldSize-headLocation.y][headLocation.x] == 3)
			head.faceDirection(East);
		else if (world[worldSize-headLocation.y][headLocation.x] == 4)
			head.faceDirection(North);
		else if (world[worldSize-headLocation.y][headLocation.x] == 5)
			head.faceDirection(West);
		else 
			head.faceDirection(South);
		
		
	}
	
	public static void wait(int miliseconds) { KarelCoords.wait(miliseconds); }
	
}
