
import java.util.*;

public class Runner {
	public static void main(String[] args) {
		
		int genAmount = 1000;
		int useSteps = 200;
		int stepIncrement = 0;
		int genSize = 100;
		
//		0 - Short, 1 - Mid, 2 - Long, 3 - Mem
		int aiType = 2;
		
		
		Object[] generation;
		
		Object[][] savedGenerations = new Object[genAmount][];
		
		AI[] aiArray = new AI[genSize];
		
		
		if (aiType == 0)
			for (int i = 0; i < genSize; i++)
				aiArray[i] = new ShortAnalysisAI();
		
		else if (aiType == 1)
			for (int i = 0; i < genSize; i++)
				aiArray[i] = new MidAnalysisAI();
		
		else if (aiType == 2)
			for (int i = 0; i < genSize; i++)
				aiArray[i] = new LongAnalysisAI();
		
		else if (aiType == 3)
			for (int i = 0; i < genSize; i++)
				aiArray[i] = new MemoryAnalysisAI();
		
		
		for (int gen = 0; gen < genAmount; gen++) {
			AI saveBest;
			if (gen != 0)
				saveBest = (AI) savedGenerations[gen-1][0];
			else
				saveBest = null;
			
			Random rand = new Random();
			
			generation = runGeneration(aiArray, useSteps, aiType, rand.nextLong());
			
			if (gen % (genAmount / 100.0) == 0 || gen == genAmount-1) {
				
				if (gen * 100 / genAmount >= 10)
					useSteps += stepIncrement;
				
				if (gen == genAmount-1)
					System.out.println("100%");
				else
					System.out.println(gen / (genAmount / 100.0) + "%");
			}
			
			savedGenerations[gen] = generation;
			
			int i;
			if (saveBest != null) {
				aiArray[0] = saveBest;
				aiArray[1] = (AI) generation[0];
				i = 2;
			}
				else {
					aiArray[0] = (AI) generation[0];
					i = 1;
				}
			
			
			for ( ; i < genSize / 10 * 4; i++)
				aiArray[i] = ((AI) generation[0]).getNextGen();
			
			for ( ; i < genSize / 10 * 6; i++)
				aiArray[i] = ((AI) generation[1]).getNextGen();
			
			for ( ; i < genSize / 10 * 7; i++)
				aiArray[i] = ((AI) generation[2]).getNextGen();
			
			
			for ( ; i < genSize; i++)
				aiArray[i].reroll();
			
		}
		
		int highest = 0;
		double avg = 0;
		for (int i = 0; i < savedGenerations.length; i++) {
			if ((double) savedGenerations[highest][4] < (double) savedGenerations[i][4])
				highest = i;
			if (i >= genAmount / 10 * 9)
				avg += (double) savedGenerations[i][4];
		}
		avg /= genAmount / 10;
		
		
		
		WorldViewer viewer = new WorldViewer(Engine.getWorldSize());
		
		Scanner s = new Scanner(System.in);
		
		int input = 0;
		while (true) {
			
			System.out.println("Total Generations: " + genAmount);
			System.out.println("Best: " + (highest + 1) + " Score: " + savedGenerations[highest][4]);
			System.out.println("Avg: " + avg);
			System.out.println("Enter Generation to view: ");
			input = s.nextInt() - 1;
			
			
			if (input < 0)
				break;
			
			AI ai = (AI) savedGenerations[input][0];
			Engine engine = new Engine((long) savedGenerations[input][3]);
			
			
			int[][] world = engine.getWorld();
			
			viewer.view(world);
			
			double score = (double) savedGenerations[input][4];
			int simSteps = (int) savedGenerations[input][5];
			int inputSteps = (int) savedGenerations[input][6];
			
			System.out.println("Score: " + score);
			System.out.println("Steps: " + simSteps + "/" + inputSteps);
			System.out.println();
			
			
			System.out.println("Enter amount of steps to simulate");
			int repitions = s.nextInt();
			
			if (repitions == -1) askAI(ai);
			
			if (repitions == 0)
				continue;
			
			System.out.println("Enter to Continue: ");
			s.nextLine();
			s.nextLine();
			
			WorldViewer.wait(3000);
			
			boolean isAlive = true;
			
			for (int i = 1; i < repitions + 1 && isAlive; i++) {
			
				double[] inputs = getInputs(engine, aiType);
				
				double[] outputs = ai.runAI(inputs);
			
				int max = 0;
				for (int j = 1; j < outputs.length; j++)
					if (outputs[max] < outputs[j])
						max = j;
				
				engine.shiftDirection(max);
				isAlive = engine.moveSnake();
				
				world = engine.getWorld();
				
				viewer.view(world);
				WorldViewer.wait(400);
			}
			
			System.out.println(Arrays.toString(engine.getLongInputs()));
			System.out.println(engine.getDirection());
			System.out.println();
			
			int offset = 0;
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					if (i == 2 && j == 2) {System.out.print("1.0 "); offset++; continue;}
					if (i == 3 && j == 2) {System.out.print("1.0 "); offset++; continue;}
					System.out.print(engine.getLongInputs()[i * 5 + j - offset] + " ");
				}
				System.out.println();
			}
			
			
			System.out.println(ai);
			System.out.println();
			System.out.println("Survived: " + engine.getSteps());
			System.out.println("Score: " + engine.getScore());
			
		}
		
		s.close();
		
	}
	
	
	
	
	
	
	public static Object[] runGeneration(AI[] ai, int repitions, int aiType, long seed) {
		
		double[] shortScores = new double[ai.length];
		
		Engine[] engines = new Engine[ai.length];
		for (int i = 0; i < engines.length; i++)
			engines[i] = new Engine(seed);
		
		
		
		int highScore1 = 0;
		int highScore2 = 1;
		int highScore3 = 2;
		for (int i = 0; i < ai.length; i++) {
			
			
			int count;
			boolean isAlive = true;
			for (count = 0; count < repitions && isAlive; count++) {
				
				double[] inputs = getInputs(engines[i], aiType);
				
				double[] outputs = ai[i].runAI(inputs);
			
				int max = 0;
				for (int j = 1; j < outputs.length; j++)
					if (outputs[max] < outputs[j])
						max = j;
				
				engines[i].shiftDirection(max);
				
				isAlive = engines[i].moveSnake();
				
				
			}
			
			shortScores[i] = engines[i].getScore();
			
			if (shortScores[highScore1] < shortScores[i]) {
				highScore3 = highScore2;
				highScore2 = highScore1;
				highScore1 = i;
			}
			
			else if (shortScores[highScore2] < shortScores[i]) {
				highScore3 = highScore2;
				highScore2 = i;
			}
			
			else if (shortScores[highScore3] < shortScores[i]) {
				highScore3 = i;
			}
		}
		
		return new Object[] {ai[highScore1], ai[highScore2], ai[highScore3], 
				engines[highScore1].getSeed(), engines[highScore1].getScore(), engines[highScore1].getSteps(), repitions};
	}
	
	public static double[] getInputs(Engine engine, int aiType) {
		if (aiType == 0)
			return engine.getShortInputs();
		
		else if (aiType == 1)
			return engine.getMidInputs();
		
		else if (aiType == 2)
			return engine.getLongInputs();
		
		else if (aiType == 3)
			return engine.getMidInputs();
		
		return null;
	}
	
	
	public static void askAI(AI ai) {
		Scanner scan = new Scanner(System.in);
		while (true) {
			System.out.println("Enter inputs (left, forward, right, angle, (mid) size)");
			String input = scan.nextLine();
			
			if (input.equals("0")) break;
			
			double[] arr = new double[input.split(" ").length];
			for (int i = 0; i < input.split(" ").length; i++) {
				arr[i] = Double.parseDouble(input.split(" ")[i]);
			}
			
			
			if (ai instanceof MemoryAnalysisAI) {
				System.out.println("Enter memory x4");
				input = scan.nextLine();
				
				double[] mem = new double[input.split(" ").length];
				for (int i = 0; i < input.split(" ").length; i++) {
					mem[i] = Double.parseDouble(input.split(" ")[i]);
				}
				
				((MemoryAnalysisAI) ai).setMem(mem);
			}
			
			ai.runAI(arr);
			
			System.out.println(ai);
			
		}
		scan.close();
	}
}
