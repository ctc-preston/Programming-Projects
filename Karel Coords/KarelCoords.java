import java.awt.Color;
import java.util.concurrent.TimeUnit;

import kareltherobot.*;

public class KarelCoords implements Directions {
	Robot karel;
	int[] coords;
	Direction direction;
	int beepers;
	
	
	public KarelCoords(int x, int y, Direction startDirection, int startBeepers) {
		karel = new Robot(y, x, startDirection, startBeepers);
		coords = new int[]{x, y};
		
		direction = startDirection;
		beepers = startBeepers;
	}
	
	public KarelCoords(int x, int y, Direction startDirection, int startBeepers, Color color) {
		karel = new Robot(y, x, startDirection, startBeepers, color);
		coords = new int[]{x, y};
		
		direction = startDirection;
		beepers = startBeepers;
	}
	
	
	
	public void turnOff() {
		karel.turnOff();
	}
	
	public void setVisible(boolean visible) {
		karel.setVisible(visible);
	}
	
	public boolean facing(Direction testDirection) {
		if (direction == testDirection)
			return true;
		else
			return false;
	}
	
	public void move() {
		karel.move();
		
		if (facing(East))
			coords[0] += 1;
		else if (facing(North))
			coords[1] += 1;
		else if (facing(West))
			coords[0] -= 1;
		else
			coords[1] -= 1;
	}
	
	public void move(int amount) {
		for (int i = 0; i < amount; i++) {
			move();
		}
	}
	
	public void turnLeft() {
		karel.turnLeft();
		
		if (facing(East))
			direction = North;
		else if (facing(North))
			direction = West;
		else if (facing(West))
			direction = South;
		else
			direction = East;
	}
	
	public void turnAround() {
		turnLeft();
		turnLeft();
	}
	
	public void turnRight() {
		turnAround();
		turnLeft();
	}
	
	public boolean nextToABeeper() {
		return karel.nextToABeeper();
	}
	
	public void putBeeper() {
		karel.putBeeper();
	}
	
	public void putBeeper(int amount) {
		for (int i = 0; i < amount; i++)
			putBeeper();
	}
	
	public void pickBeeper() {
		karel.pickBeeper();
	}
	
	public void pickBeeper(int amount) {
		for (int i = 0; i < amount; i++)
			pickBeeper();
	}
	
	public void pickAllBeepers() {
		while (nextToABeeper())
			pickBeeper();
	}
	
	public void setBeepers(int amount) {
		while (nextToABeeper())
			pickBeeper();
		for (int i = 0; i < amount; i++)
			putBeeper();
	}
	
	
	public void faceDirection(Direction testDirection) {
		while (direction != testDirection) {
			turnLeft();
		}
	}
	
	public void returnHome() {
		faceDirection(West);
		while (coords[0] != 1) {
			move();
		}
		faceDirection(South);
		while (coords[1] != 1) {
			move();
		}
		faceDirection(East);
	}
	
	public void goToCoord(int x, int y) {
		if (coords[0] < x) {
			faceDirection(East);
			move(x - coords[0]);
		} else if (coords[0] > x) {
			faceDirection(West);
			move(coords[0] - x);
		}
		
		if (coords[1] < y) {
			faceDirection(North);
			move(y - coords[1]);
		} else if (coords[1] > y) {
			faceDirection(South);
			move(coords[1] - y);
		}
	}
	
	public void putAt(int x, int y) {
		goToCoord(x, y);
		putBeeper();
	}
	
	public void putAt(int x, int y, int amount) {
		goToCoord(x, y);
		for (int i = 0; i < amount; i++)
			putBeeper();
	}
	
	public void pickAt(int x, int y) {
		goToCoord(x, y);
		pickBeeper();
	}
	
	public void pickAt(int x, int y, int amount) {
		goToCoord(x, y);
		for (int i = 0; i < amount; i++)
			pickBeeper();
	}
	
	public void pickAllAt(int x, int y) {
		goToCoord(x, y);
		while (nextToABeeper())
			pickBeeper();
	}
	
	
	public void buildAt(int x1, int y1, int x2, int y2) {
		goToCoord(x1, y1);
		faceDirection(East);
		for (int i = 0; i < y2 - y1 + 1; i++) {
			for (int j = 0; j < x2 - x1; j++) {
				putBeeper();
				move();
			}
			putBeeper();
			if (coords[1] != y2) {
				if (direction == East) {
					turnLeft();
					move();
					turnLeft();
				} else {
					turnRight();
					move();
					turnRight();
				}
			}
		}
	}
	
	public void buildAt(int x1, int y1, int x2, int y2, int amount) {
		goToCoord(x1, y1);
		faceDirection(East);
		for (int i = 0; i < y2 - y1 + 1; i++) {
			for (int j = 0; j < x2 - x1; j++) {
				putBeeper(amount);
				move();
			}
			putBeeper(amount);
			if (coords[1] != y2) {
				if (direction == East) {
					turnLeft();
					move();
					turnLeft();
				} else {
					turnRight();
					move();
					turnRight();
				}
			}
		}
	}
	
	public void clearAt(int x1, int y1, int x2, int y2) {
		goToCoord(x1, y1);
		faceDirection(East);
		for (int i = 0; i < y2 - y1 + 1; i++) {
			for (int j = 0; j < x2 - x1; j++) {
				pickBeeper();
				move();
			}
			pickBeeper();
			if (coords[1] != y2) {
				if (direction == East) {
					turnLeft();
					move();
					turnLeft();
				} else {
					turnRight();
					move();
					turnRight();
				}
			}
		}
	}
	
	public void clearAt(int x1, int y1, int x2, int y2, int amount) {
		goToCoord(x1, y1);
		faceDirection(East);
		for (int i = 0; i < y2 - y1 + 1; i++) {
			for (int j = 0; j < x2 - x1; j++) {
				pickBeeper(amount);
				move();
			}
			pickBeeper(amount);
			if (coords[1] != y2) {
				if (direction == East) {
					turnLeft();
					move();
					turnLeft();
				} else {
					turnRight();
					move();
					turnRight();
				}
			}
		}
	}
	
	public void clearAllAt(int x1, int y1, int x2, int y2) {
		goToCoord(x1, y1);
		faceDirection(East);
		for (int i = 0; i < y2 - y1 + 1; i++) {
			for (int j = 0; j < x2 - x1; j++) {
				pickAllBeepers();
				move();
			}
			pickAllBeepers();
			if (coords[1] != y2) {
				if (direction == East) {
					turnLeft();
					move();
					turnLeft();
				} else {
					turnRight();
					move();
					turnRight();
				}
			}
		}
	}
	
	public static void wait(int miliseconds) {
		
		try {
			TimeUnit.MILLISECONDS.sleep(miliseconds);
		} catch (InterruptedException e) {}
		
	}
	
	
	
}
