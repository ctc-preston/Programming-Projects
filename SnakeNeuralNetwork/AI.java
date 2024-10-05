import java.util.Random;

public abstract class AI {
	
	protected double[] inputs;
	protected double[] outputs;
	
	protected double[][] bias;
	protected double[][] weights;
	
	public AI() {}
	
	public AI(double[][] weights, double[][] bias) {
		
		this.weights = weights;
		this.bias = bias;
		
	}
	
	public abstract double[] runAI(double[] inputs);	
	
	
	public abstract AI factory(double[][] weights, double[][] bias);
	
	public abstract void reroll();
	
	public AI getNextGen() {
		
		double[][] newWeights = new double[weights.length][];
		for (int i = 0; i < weights.length; i++) {
			newWeights[i] = new double[weights[i].length];
			for (int j = 0; j < weights[i].length; j++)
				newWeights[i][j] = weights[i][j];
		}
		
		double[][] newBias = new double[bias.length][];
		for (int i = 0; i < bias.length; i++) {
			newBias[i] = new double[bias[i].length];
			for (int j = 0; j < bias[i].length; j++)
				newBias[i][j] = bias[i][j];
		}
		
		Random rand = new Random();
		
		for (int n = 0; n < newWeights.length; n++)
			for (int j = 0; j < newWeights[n].length; j++)
				newWeights[n][j] += (1 - Math.abs(newWeights[n][j])) * ((rand.nextInt(200) - 100) / 1000.0);
		
		
		for (int n = 0; n < newWeights.length; n++)
			for (int j = 0; j < newBias[n].length; j++)
				newBias[n][j] += (rand.nextInt(400) - 200) / 1000.0;
		
		
		return factory(newWeights, newBias);
		
	}
	
	public double[][] getWeights() { return weights; }
	
	public double[][] getBias() { return bias; }
	
	protected static double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}
	
}
