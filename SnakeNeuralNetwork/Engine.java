import java.util.*;

public class Engine {
	
	private static int WORLD_SIZE = 12;
	
	private int[][] world = new int[WORLD_SIZE][WORLD_SIZE];
	
	private ArrayList<Coord> body = new ArrayList<Coord>();
	
	private int direction = 0;
	
	private Random rand;
	private long seed;
	
	private Coord apple = new Coord(0, 0);
	
	private int step;
	
	public Engine() {
		rand = new Random();
		
		body.add(new Coord(WORLD_SIZE / 2, WORLD_SIZE / 2 - 1));
		body.add(new Coord(WORLD_SIZE / 2 - 1, WORLD_SIZE / 2 - 1));
		body.add(new Coord(WORLD_SIZE / 2 - 2, WORLD_SIZE / 2 - 1));
		
		world[body.get(0).getY()][body.get(0).getX()] = 3 + direction;
		world[body.get(1).getY()][body.get(1).getX()] = 1;
		world[body.get(2).getY()][body.get(2).getX()] = 1;
		
		do {
			apple.set(randomLocation(), randomLocation());
		} while (body.contains(apple));
		
		world[apple.getY()][apple.getX()] = 2;
		
	}
	
	public Engine(long seed) {
		rand = new Random(seed);
		this.seed = seed;
		
		body.add(new Coord(WORLD_SIZE / 2, WORLD_SIZE / 2 - 1));
		body.add(new Coord(WORLD_SIZE / 2 - 1, WORLD_SIZE / 2 - 1));
		body.add(new Coord(WORLD_SIZE / 2 - 2, WORLD_SIZE / 2 - 1));
		
		world[body.get(0).getY()][body.get(0).getX()] = 3 + direction;
		world[body.get(1).getY()][body.get(1).getX()] = 1;
		world[body.get(2).getY()][body.get(2).getX()] = 1;
		
		do {
			apple.set(randomLocation(), randomLocation());
		} while (body.contains(apple));
		
		world[apple.getY()][apple.getX()] = 2;
	}
	
	public boolean moveSnake() {
		
		Coord head = body.get(0);
		
		
		head.move(direction);
		
		if (body.subList(1, body.size()-1).contains(head) || 
				head.getX() >= WORLD_SIZE || head.getX() < 0 || head.getY() >= WORLD_SIZE || head.getY() < 0) 
		{
			head.move((direction + 2) % 4);
			world[head.getY()][head.getX()] = 3 + direction;
			return false;
		}
		
		head.move((direction + 2) % 4);
		
		
		world[head.getY()][head.getX()] = 1;
		
		Coord last = new Coord(body.get(body.size() - 1));
		
		for (int i = body.size() - 1; i > 0; i--)
			body.get(i).goTo(body.get(i-1));
		
		head.move(direction);
		
		world[head.getY()][head.getX()] = 3 + direction;
		
		if (head.equals(apple)) {
			do {
				apple.set(randomLocation(), randomLocation());
			} while (body.contains(apple));
			
			world[apple.getY()][apple.getX()] = 2;
			
			body.add(last);
		} else
			if (world[last.getY()][last.getX()] == 1)
				world[last.getY()][last.getX()] = 0;
		
		step++;
		
		return true;
		
	}
	
	public double[] getShortInputs() {
		Coord head = body.get(0);
		
		double xDis = apple.getX() - head.getX();
		double yDis = head.getY() - apple.getY();
		
		
		double appleAngle;
		
		if (xDis != 0) {
			appleAngle = yDis / xDis;
			
			if (xDis > 0)
				appleAngle = -Math.atan(appleAngle) + Math.PI / 2 * direction;
			else
				appleAngle = -Math.atan(appleAngle) + Math.PI / 2 * direction + Math.PI;
			
			if (appleAngle > Math.PI || appleAngle < -Math.PI)
				appleAngle = appleAngle + -Math.signum(appleAngle) * 2 * Math.PI;
				
			appleAngle /= Math.PI;
			
		} else {
			if (yDis > 0) {
				if (direction == 0)
					appleAngle = -0.5;
				else if (direction == 1)
					appleAngle = 0;
				else if (direction == 2)
					appleAngle = 0.5;
				else
					appleAngle = 1;
			}
			else
				if (direction == 0)
					appleAngle = 0.5;
				else if (direction == 1)
					appleAngle = 1;
				else if (direction == 2)
					appleAngle = -0.5;
				else
					appleAngle = 0;
		}
		
		int left, straight, right;
		
		
		Coord last = body.get(body.size() - 1);
		
		world[last.getY()][last.getX()] = 0;
		
		
		if (direction == 0) {
			if (head.getY() > 0 && world[head.getY() - 1][head.getX()] != 1)
				left = 0;
			else left = 1;
			
			if (head.getX() < WORLD_SIZE-1 && world[head.getY()][head.getX() + 1] != 1)
				straight = 0;
			else straight = 1;
			
			if (head.getY() < WORLD_SIZE-1 && world[head.getY() + 1][head.getX()] != 1)
				right = 0;
			else right = 1;
		}
		
		else if (direction == 1) {
			if (head.getX() > 0 && world[head.getY()][head.getX() - 1] != 1)
				left = 0;
			else left = 1;
			
			if (head.getY() > 0 && world[head.getY() - 1][head.getX()] != 1)
				straight = 0;
			else straight = 1;
			
			if (head.getX() < WORLD_SIZE-1 && world[head.getY()][head.getX() + 1] != 1)
				right = 0;
			else right = 1;
		}
		
		else if (direction == 2) {
			if (head.getY() < WORLD_SIZE-1 && world[head.getY() + 1][head.getX()] != 1)
				left = 0;
			else left = 1;
			
			if (head.getX() > 0 &&  world[head.getY()][head.getX() - 1] != 1)
				straight = 0;
			else straight = 1;
			
			if (head.getY() > 0 && world[head.getY() - 1][head.getX()] != 1)
				right = 0;
			else right = 1;
			
		} else {
			if (head.getX() < WORLD_SIZE-1 && world[head.getY()][head.getX() + 1] != 1)
				left = 0;
			else left = 1;
			
			if (head.getY() < WORLD_SIZE-1 && world[head.getY() + 1][head.getX()] != 1)
				straight = 0;
			else straight = 1;
			
			if (head.getX() > 0 && world[head.getY()][head.getX() - 1] != 1)
				right = 0;
			else right = 1;
		}
		
		world[last.getY()][last.getX()] = 1;
		
		return new double[] {left, straight, right, appleAngle};
	}
	
	public double[] getMidInputs() {
		double[] shortInputs = getShortInputs();
		
		return new double[] {shortInputs[0], shortInputs[1], shortInputs[2], shortInputs[3], 1 / (1 + Math.exp(-(0.1 * (body.size() - 3)))) - .5};
	}
	
	public double[] getLongInputs() {
		
		int sight = 5;
		
		double[] shortInputs = getShortInputs();
		
		double[] inputs = new double[sight*sight];
		
		inputs[23] = shortInputs[3];
		inputs[24] = 1 / (1 + Math.exp(-(0.1 * (body.size() - 3)))) - .5;
		
		if (direction == 0) {
			int offset = 0;
			for (int j = 0; j < sight; j++) {
				for (int i = 0; i < sight; i++) {
					if (i == sight/2 && j == sight/2) {offset++; continue;}
					if (i == sight/2 && j == sight/2+1) {offset++; continue;}
					
					if (body.get(0).getY() + i - 2 < WORLD_SIZE && body.get(0).getY() + i - 2 >= 0 && body.get(0).getX() - j + 2 < WORLD_SIZE && body.get(0).getX() - j + 2 >= 0)
						inputs[j * 5 + i - offset] = world[body.get(0).getY() + i - 2][body.get(0).getX() - j + 2] == 2? -1 : world[body.get(0).getY() + i - 2][body.get(0).getX() - j + 2];
					else
						inputs[j * 5 + i - offset] = 1;
				}
			}
		}
		else if (direction == 1) {
			int offset = 0;
			for (int i = 0; i < sight; i++) {
				for (int j = 0; j < sight; j++) {
					if (i == sight/2 && j == sight/2) {offset++; continue;}
					if (i == sight/2+1 && j == sight/2) {offset++; continue;}
					
					if (body.get(0).getY() + i - 2 < WORLD_SIZE && body.get(0).getY() + i - 2 >= 0 && body.get(0).getX() + j - 2 < WORLD_SIZE && body.get(0).getX() + j - 2 >= 0)
						inputs[i * 5 + j - offset] = world[body.get(0).getY() + i - 2][body.get(0).getX() + j - 2] == 2? -1 : world[body.get(0).getY() + i - 2][body.get(0).getX() + j - 2];
					else
						inputs[i * 5 + j - offset] = 1;
				}
			}
		}
		else if (direction == 2) {
			int offset = 0;
			for (int j = 0; j < sight; j++) {
				for (int i = 0; i < sight; i++) {
					if (i == sight/2 && j == sight/2) {offset++; continue;}
					if (i == sight/2 && j == sight/2+1) {offset++; continue;}
					
					if (body.get(0).getY() - i + 2 < WORLD_SIZE && body.get(0).getY() - i + 2 >= 0 && body.get(0).getX() + j - 2 < WORLD_SIZE && body.get(0).getX() + j - 2 >= 0)
						inputs[j * 5 + i - offset] = world[body.get(0).getY() - i + 2][body.get(0).getX() + j - 2] == 2? -1 : world[body.get(0).getY() - i + 2][body.get(0).getX() + j - 2];
					else
						inputs[j * 5 + i - offset] = 1;
				}
			}
		} else {
			int offset = 0;
			for (int i = 0; i < sight; i++) {
				for (int j = 0; j < sight; j++) {
					if (i == sight/2 && j == sight/2) {offset++; continue;}
					if (i == sight/2+1 && j == sight/2) {offset++; continue;}
					
					if (body.get(0).getY() - i + 2 < WORLD_SIZE && body.get(0).getY() - i + 2 >= 0 && body.get(0).getX() - j + 2 < WORLD_SIZE && body.get(0).getX() - j + 2 >= 0)
						inputs[i * 5 + j - offset] = world[body.get(0).getY() - i + 2][body.get(0).getX() - j + 2] == 2? -1 : world[body.get(0).getY() - i + 2][body.get(0).getX() - j + 2];
					else
						inputs[i * 5 + j - offset] = 1;
				}
			}
		}
		
		return inputs;
	}
	
//	public double[] getLongInputs() {
//		return null;
//	}
	
	public int[][] getWorld() { return world; }
	
	public double getScore() {
		return getApples() + getSteps() / 1000.0;
	}
	
//	public double getScore() {
//		return getApples();
//	}
	
	public double getApples() { return body.size() - 3; }
	
	public int getSteps() { return step; }
	
	public long getSeed() { return seed; }
	
	public int getDirection() { return direction; }
	
	public void changeDirection(int dir) { direction = dir; }
	
	public void shiftDirection(int shift) {
		if (shift == 0)
			direction++;
		if (shift == 2)
			direction--;
		
		if (direction > 3)
			direction -= 4;
		if (direction < 0)
			direction += 4;
	}
	
	public static int getWorldSize() { return WORLD_SIZE; }
	
	
	private int randomLocation() { return rand.nextInt(WORLD_SIZE); }
	
	
	private class Coord {
		private int x, y;
		
		public Coord(int x, int y) {
			set(x, y);
		}
		
		public Coord(Coord c) {
			set(c.getX(), c.getY());
		}
		
		public int getX() { return x; }
		
		public int getY() { return y; }
		
		public void move(int dir) {
			if (dir == 0)
				x++;
			else if (dir == 1)
				y--;
			else if (dir == 2)
				x--;
			else
				y++;
		}
		
		public void set(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void goTo(Coord c) {
			this.x = c.x;
			this.y = c.y;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof Coord)
				return x == ((Coord)o).x && y == ((Coord)o).y;
			return false;
		}
		
		@Override
		public String toString() {
			return "X: " + x + " Y: " + y;
		}
		
	}
}
