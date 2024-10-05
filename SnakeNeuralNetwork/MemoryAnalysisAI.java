import java.util.Arrays;

public class MemoryAnalysisAI extends AI {
	
private double[] nodes = new double[100];

private double[] memory = new double[200];
	
	public MemoryAnalysisAI() {
		
		inputs = new double[5 + memory.length];
		outputs = new double[3 + memory.length];
		
		bias = new double[2][];
		weights = new double[2][];
		
		weights[0] = new double[inputs.length * nodes.length];
		weights[1] = new double[outputs.length * nodes.length];
		
		for (int i = 0; i < weights.length; i++)
			for (int j = 0; j < weights[i].length; j++)
				weights[i][j] = Math.random() * 2 - 1;
		
		bias[0] = new double[nodes.length];
		bias[1] = new double[outputs.length];
		
	}
	
	public MemoryAnalysisAI(double[][] weights, double[][] bias) {
		super(weights, bias);
		inputs = new double[5 + memory.length];
		outputs = new double[3 + memory.length];
	}
	
	public void setMem(double[] newMem) {
		memory = newMem;
	}
	
	public double[] runAI(double[] in) {
		
		for (int i = 0; i < in.length; i++)
			inputs[i] = in[i];
		
		for (int i = in.length; i < memory.length + in.length; i++)
			inputs[i] = memory[i - in.length];
		
		
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = bias[0][i];
			for (int j = 0; j < inputs.length; j++) {
				nodes[i] += inputs[j] * weights[0][i * inputs.length + j];
			}
			nodes[i] = sigmoid(nodes[i]);
		}
		
		
		for (int i = 0; i < outputs.length; i++) {
			outputs[i] = bias[1][i];
			for (int j = 0; j < nodes.length; j++) {
				outputs[i] += nodes[j] * weights[1][i * nodes.length + j];
			}
			outputs[i] = sigmoid(outputs[i]);
		}
		
		for (int i = outputs.length - memory.length; i < outputs.length; i++)
			memory[i - (outputs.length - memory.length)] = outputs[i];
		
		
		return Arrays.copyOfRange(outputs, 0, memory.length);
		
	}
	
	public void reroll() {
		for (int i = 0; i < weights.length; i++)
			for (int j = 0; j < weights[i].length; j++)
				weights[i][j] = Math.random() * 2 - 1;
		
		bias[0] = new double[nodes.length];
		bias[1] = new double[outputs.length];
	}
	
	public AI factory(double[][] weights, double[][] bias) {
		return new MemoryAnalysisAI(weights, bias);
	}
	
	
	@Override
	public String toString() {
		return "Inputs: " + Arrays.toString(inputs) + "\nWeights[0]: " + Arrays.toString(weights[0]) + "\nBiases[0]: " + Arrays.toString(bias[0])  +
		"\nNodes: " + Arrays.toString(nodes) + "\nWeights[1]: " + Arrays.toString(weights[1]) + "\nBiases[1]: " + Arrays.toString(bias[1]) +
		"\nOutputs: " + Arrays.toString(outputs);
	}
	
}
