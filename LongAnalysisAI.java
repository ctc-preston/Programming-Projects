import java.util.Arrays;
import java.util.Random;

public class LongAnalysisAI extends AI {
	
	private double[] nodes1 = new double[20];
	private double[] nodes2 = new double[20];
	
	public LongAnalysisAI() {
		
		inputs = new double[25];
		outputs = new double[3];
		
		bias = new double[3][];
		weights = new double[3][];
		
		weights[0] = new double[inputs.length * nodes1.length];
		weights[1] = new double[nodes1.length * nodes2.length];
		weights[2] = new double[outputs.length * nodes2.length];
		
		for (int i = 0; i < weights.length; i++)
			for (int j = 0; j < weights[i].length; j++)
				weights[i][j] = Math.random() * 2 - 1;
		
		bias[0] = new double[nodes1.length];
		bias[1] = new double[nodes2.length];
		bias[2] = new double[outputs.length];
		
	}
	
	public LongAnalysisAI(double[][] weights, double[][] bias) {
		super(weights, bias);
		inputs = new double[25];
		outputs = new double[3];
	}
	
	public void reroll() {
		for (int i = 0; i < weights.length; i++)
			for (int j = 0; j < weights[i].length; j++)
				weights[i][j] = Math.random() * 2 - 1;
		
		for (int i = 0; i < bias.length; i++)
			for (int j = 0; j < bias[i].length; j++)
				bias[i][j] = Math.random() * 2 - 1;
		
//		bias[0] = new double[nodes1.length];
//		bias[1] = new double[nodes2.length];
//		bias[2] = new double[outputs.length];
	}
	
	public double[] runAI(double[] in) {
		
		inputs = in;
		
		for (int i = 0; i < nodes1.length; i++) {
			nodes1[i] = bias[0][i];
			for (int j = 0; j < inputs.length; j++) {
				nodes1[i] += inputs[j] * weights[0][i * inputs.length + j];
			}
			nodes1[i] = sigmoid(nodes1[i]);
		}
		
		for (int i = 0; i < nodes2.length; i++) {
			nodes2[i] = bias[1][i];
			for (int j = 0; j < nodes1.length; j++) {
				nodes2[i] += nodes1[j] * weights[1][i * nodes1.length + j];
			}
			nodes2[i] = sigmoid(nodes2[i]);
		}
		
		
		for (int i = 0; i < outputs.length; i++) {
			outputs[i] = bias[2][i];
			for (int j = 0; j < nodes2.length; j++) {
				outputs[i] += nodes2[j] * weights[2][i * nodes2.length + j];
			}
			outputs[i] = sigmoid(outputs[i]);
		}
		
		return outputs;
		
	}
	
	public AI factory(double[][] weights, double[][] bias) {
		return new LongAnalysisAI(weights, bias);
	}
	
	
	@Override
	public String toString() {
		return "Inputs: " + Arrays.toString(inputs) + "\nWeights[0]: " + Arrays.toString(weights[0]) + "\nBiases[0]: " + Arrays.toString(bias[0])  +
		"\nNodes1: " + Arrays.toString(nodes1) + "\nWeights[1]: " + Arrays.toString(weights[1]) + "\nBiases[1]: " + Arrays.toString(bias[1])  +
		"\nNodes2: " + Arrays.toString(nodes2) + "\nWeights[2]: " + Arrays.toString(weights[2]) + "\nBiases[2]: " + Arrays.toString(bias[2]) +
		"\nOutputs: " + Arrays.toString(outputs);
	}
	
}
