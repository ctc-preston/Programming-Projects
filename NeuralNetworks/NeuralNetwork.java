import java.util.Arrays;
import java.util.Random;

public class NeuralNetwork {
	protected double[] inputs;
	protected double[] outputs;
	
	protected double[][] bias;
	protected double[][] weights;
	
	protected double[][] nodes;
	
	public NeuralNetwork(int inputSize, int outputSize, int... nodeSize) {
		inputs = new double[inputSize];
		outputs = new double[outputSize];
		
		nodes = new double[nodeSize.length][];
		weights = new double[nodeSize.length+1][];
		bias = new double[nodeSize.length+1][];
		
		for (int i = 0; i < nodeSize.length; i++) {
			nodes[i] = new double[nodeSize[i]];
			
			if (i == 0) weights[i] = new double[inputSize * nodeSize[0]];
			else weights[i] = new double[nodeSize[i-1] * nodeSize[i]];
			
			bias[i] = new double[nodeSize[i]];
		}
		
		weights[weights.length-1] = new double[nodeSize[nodeSize.length-1] * outputSize];
		bias[bias.length-1] = new double[outputSize];
		
	}
	
	public NeuralNetwork(double[][] weights, double[][] bias, int inputSize, int outputSize, int... nodeSize) {
		
		inputs = new double[inputSize];
		outputs = new double[outputSize];
		
		this.weights = new double[weights.length][];
		for (int i = 0; i < weights.length; i++) {
			this.weights[i] = new double[weights[i].length];
			for (int j = 0; j < weights[i].length; j++)
				this.weights[i][j] = weights[i][j];
		}
		this.bias = new double[bias.length][];
		for (int i = 0; i < bias.length; i++) {
			this.bias[i] = new double[bias[i].length];
			for (int j = 0; j < bias[i].length; j++)
				this.bias[i][j] = bias[i][j];
		}
		
		nodes = new double[nodeSize.length][];
		for (int i = 0; i < nodeSize.length; i++) {
			nodes[i] = new double[nodeSize[i]];
		}
		
	}
	
	
	public double[] runNetwork(double[] in) {
		inputs = in;
		
		for (int i = 0; i < nodes[0].length; i++) {
			nodes[0][i] = bias[0][i];
			for (int j = 0; j < inputs.length; j++) {
				nodes[0][i] += inputs[j] * weights[0][i * inputs.length + j];
			}
			nodes[0][i] = sigmoid(nodes[0][i]);
		}
		
		for (int n = 1; n < nodes.length; n++) {
			for (int i = 0; i < nodes[n].length; i++) {
				nodes[n][i] = bias[n][i];
				for (int j = 0; j < nodes[n-1].length; j++) {
					nodes[n][i] += nodes[n-1][j] * weights[n][i * nodes[n-1].length + j];
				}
				nodes[n][i] = sigmoid(nodes[n][i]);
			}
		}
		
		for (int i = 0; i < outputs.length; i++) {
			outputs[i] = bias[bias.length-1][i];
			for (int j = 0; j < nodes[nodes.length-1].length; j++) {
				outputs[i] += nodes[nodes.length-1][j] * weights[weights.length-1][i * nodes[nodes.length-1].length + j];
			}
			outputs[i] = sigmoid(outputs[i]);
		}
		
		return outputs;
	}
	
	public void regenerate() {
		for (int i = 0; i < weights.length; i++)
			for (int j = 0; j < weights[i].length; j++)
				weights[i][j] = Math.random() * 2 - 1;
		
		for (int i = 0; i < bias.length; i++)
			for (int j = 0; j < bias[i].length; j++)
				bias[i][j] = Math.random() * 2 - 1;
		
	}
	
	public NeuralNetwork getNextOffspring() {
		
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
		
		int[] nodeSize = new int[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			nodeSize[i] = nodes[i].length;
		}
		
		return new NeuralNetwork(newWeights, newBias, inputs.length, outputs.length, nodeSize);
		
	}
	
	public double costFunc(double[] expected) {
		double cost = 0;
		for (int i = 0; i < outputs.length; i++) {
			cost += (outputs[i] - expected[i]) * (outputs[i] - expected[i]);
		}
		return cost;
	}
	
	public double[][] getWeights() { return weights; }
	
	public double[][] getBias() { return bias; }
	
	public double[] getOutputs() { return outputs; }
	
	protected static double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}
	
	@Override
	public String toString() {
		String s = "Inputs: " + Arrays.toString(inputs) + "\nWeights[0]: " + Arrays.toString(weights[0]) + "\nBiases[0]: " + Arrays.toString(bias[0]);
		for (int i = 0; i < nodes.length; i++) {
			s += String.format("%nNodes[%d]: " + Arrays.toString(nodes[i]) + "%nWeights[%d]: " + Arrays.toString(weights[i+1]) + "%nBias[%d]: " + Arrays.toString(bias[i+1]), i, i+1, i+1);
		}
		s += "\nOutputs: " + Arrays.toString(outputs);
		
		return s;
	}
	
	public String toDesmosString() {
		String all = "";
		if (inputs.length != 2) all += "Input not of size 2\n";
		
		
		for (int i = 0; i < weights.length; i++) {
			int just1 = (weights.length-1 + "").length();
			int just2 = (weights[i].length-1 + "").length();
			String format = String.format("w_{%%0%dd%%0%dd}=%%.15f", just1, just2);
			for (int j = 0; j < weights[i].length; j++) {
				all += String.format(format, i, j, weights[i][j]) + "\n";
			}
		}
		for (int i = 0; i < bias.length; i++) {
			int just1 = (bias.length-1 + "").length();
			int just2 = (bias[i].length-1 + "").length();
			String format = String.format("b_{%%0%dd%%0%dd}=%%.15f", just1, just2);
			for (int j = 0; j < bias[i].length; j++) {
				all += String.format(format, i, j, bias[i][j]) + "\n";
			}
		}
		for (int i = 0; i < nodes.length; i++) {
			int just1 = (nodes.length-1 + "").length();
			int just2 = (nodes[i].length-1 + "").length();
			String format = String.format("a_{%%0%dd%%0%dd}=a(", just1, just2);
			for (int j = 0; j < nodes[i].length; j++) {
				all += String.format(format, i, j);
				
				if (i == 0) {
					int justi = (inputs.length-1 + "").length();
					int justw1 = (weights.length-1 + "").length();
					int justw2 = (weights[0].length-1 + "").length();
					int justb1 = (bias.length-1 + "").length();
					int justb2 = (bias[0].length-1 + "").length();
					for (int k = 0; k < inputs.length; k++) {
						all += String.format("i_{%0"+justi+"d}w_{%0"+justw1+"d%0"+justw2+"d}+", k, 0, inputs.length*j + k);
					}
					all += String.format("b_{%0"+justb1+"d%0"+justb2+"d})\n", 0, j);
				} else {
					int justi = (nodes[i-1].length-1 + "").length();
					int justw1 = (weights.length-1 + "").length();
					int justw2 = (weights[i].length-1 + "").length();
					int justb1 = (bias.length-1 + "").length();
					int justb2 = (bias[i].length-1 + "").length();
					for (int k = 0; k < nodes[i-1].length; k++) {
						all += String.format("a_{%0"+justi+"d%0"+justi+"d}w_{%0"+justw1+"d%0"+justw2+"d}+", i-1, k, i, nodes[i-1].length*j + k);
					}
					all += String.format("b_{%0"+justb1+"d%0"+justb2+"d})\n", i, j);
				}
			}
		}
		
		for (int i = 0; i < outputs.length; i++) {
			int just = (outputs.length-1 + "").length();
			String format = String.format("o_{%%0%dd}=a(", just);
			all += String.format(format, i);
			int justi1 = (nodes.length-1 + "").length();
			int justi2 = (nodes[nodes.length-1].length-1 + "").length();
			int justw1 = (weights.length-1 + "").length();
			int justw2 = (weights[weights.length-1].length-1 + "").length();
			int justb1 = (bias.length-1 + "").length();
			int justb2 = (bias[bias.length-1].length-1 + "").length();
			for (int j = 0; j < nodes[nodes.length-1].length; j++) {
				all += String.format("a_{%0"+justi1+"d%0"+justi2+"d}w_{%0"+justw1+"d%0"+justw2+"d}+", nodes.length-1, j, weights.length-1, nodes[nodes.length-1].length*i + j);
			}
			all += String.format("b_{%0"+justb1+"d%0"+justb2+"d})\n", bias.length-1, i);
		}
		all += "a\\left(x\\right)=\\frac{1}{1+e^{-x}}\n";
		all += "i_{0}=x\ni_{1}=y\no_{0}>o_{1}";
		
		return all;
	}
}
