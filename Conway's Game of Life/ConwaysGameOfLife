import java.util.concurrent.TimeUnit;

import kareltherobot.*;

import java.util.Random;

public class ConwaysGameOfLife implements Directions {
	final static int STREET_SIZE = 35;
	final static int AVENUE_SIZE = 35;
	
	static int[][] beeperWorld = new int[STREET_SIZE][AVENUE_SIZE];
	
	static KarelCoords karel = new KarelCoords(1, 1, East, infinity);
	
	
	public static void main(String[] args) {
		
		Random rand = new Random();
		
		int delay = 500;
		
		World.setVisible(true);
		World.setSize(STREET_SIZE, AVENUE_SIZE);
		
		World.setDelay(0);
		
		karel.setVisible(false);
		
		int[][] newBeeperWorld = new int[STREET_SIZE][AVENUE_SIZE];
		
		
		// random generate
//		for (int i = 0; i < STREET_SIZE; i++) {
//			for (int j = 0; j < AVENUE_SIZE; j++) {
//				if (rand.nextInt(3) == 0)
//					put(i + 1, j + 1);
//			}
//		}
		
		put(17, 17);
		put(16, 17);
		put(18, 17);
		put(16, 18);
		put(18, 18);
		put(16, 19);
		put(18, 19);
		
		
		
//		put(3, 5);
//		put(3, 6);
//		put(3, 7);
//		put(3, 11);
//		put(3, 12);
//		put(3, 13);
//		
//		put(5, 3);
//		put(5, 8);
//		put(5, 10);
//		put(5, 15);
//		
//		put(6, 3);
//		put(6, 8);
//		put(6, 10);
//		put(6, 15);
//		
//		put(7, 3);
//		put(7, 8);
//		put(7, 10);
//		put(7, 15);
//		
//		put(8, 5);
//		put(8, 6);
//		put(8, 7);
//		put(8, 11);
//		put(8, 12);
//		put(8, 13);
//		
//		put(10, 5);
//		put(10, 6);
//		put(10, 7);
//		put(10, 11);
//		put(10, 12);
//		put(10, 13);
//		
//		put(11, 3);
//		put(11, 8);
//		put(11, 10);
//		put(11, 15);
//		
//		put(12, 3);
//		put(12, 8);
//		put(12, 10);
//		put(12, 15);
//		
//		put(13, 3);
//		put(13, 8);
//		put(13, 10);
//		put(13, 15);
//		
//		put(15, 5);
//		put(15, 6);
//		put(15, 7);
//		put(15, 11);
//		put(15, 12);
//		put(15, 13);
		
		wait(2000);
		
		while (true) {
			int neighbours;
			for (int i = 0; i < STREET_SIZE; i++) {
				for (int j = 0; j < AVENUE_SIZE; j++) {
					neighbours = findNeighbors(i, j);
					
					if (beeperWorld[i][j] == 1) {
						if (neighbours < 2 || neighbours > 3)
							newBeeperWorld[i][j] = 0;
						else
							newBeeperWorld[i][j] = 1;
					} else if (neighbours == 3)
						newBeeperWorld[i][j] = 1;
				}
			}
			
			for (int i = 0; i < STREET_SIZE; i++) {
				for (int j = 0; j < AVENUE_SIZE; j++) {
					if (beeperWorld[i][j] != newBeeperWorld[i][j])
						set(i + 1, j + 1, newBeeperWorld[i][j]);
				}
			}
			
			
			wait(delay);

		}
	}
	
	static void put(int x, int y) {
		beeperWorld[x - 1][y - 1] = 1;
		karel.putAt(x, y);
	}
	
	static void set(int x, int y, int amount) {
		karel.goToCoord(x, y);
		karel.setBeepers(amount);
		beeperWorld[x - 1][y - 1] = amount;
	}
	
	static int findNeighbors(int i, int j) {
		int x1 = -1, y1 = -1, x2 = 2, y2 = 2;
		
		if (i == 0)
			x1 = 0;
		if (i == STREET_SIZE - 1)
			x2 = 1;
		if (j == 0)
			y1 = 0;
		if (j == AVENUE_SIZE - 1)
			y2 = 1;
		
		
		int neighbours = 0;
		for (int k = x1; k < x2; k++) {
			for (int l = y1; l < y2; l++) {
				if (beeperWorld[i + k][j + l] == 1 && (k != 0 || l != 0))
					neighbours++;
			}
		}
		return neighbours;	
	}
	
	static void wait(int delay) {
		try {
			TimeUnit.MILLISECONDS.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
