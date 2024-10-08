import java.util.Arrays;

public class NeuralNetworkBackProp extends NeuralNetwork {
	
	double[][] chweights;
	double[][] chbias;
	double[][] z;
	int backpropCount;
	
	public NeuralNetworkBackProp(double[][] weights, double[][] bias, int inputSize, int outputSize, int... nodeSize) {
		super(weights, bias, inputSize, outputSize, nodeSize);
		init();
	}
	
	public NeuralNetworkBackProp(int inputSize, int outputSize, int... nodeSize) {
		super(inputSize, outputSize, nodeSize);
		init();
	}
	
	public void init() {
		chweights = new double[weights.length][];
		for (int i = 0; i < weights.length; i++) {
			chweights[i] = new double[weights[i].length];
		}
		chbias = new double[bias.length][];
		for (int i = 0; i < bias.length; i++) {
			chbias[i] = new double[bias[i].length];
		}
		
		z = new double[nodes.length+1][];
		for (int i = 0; i < nodes.length; i++) {
			z[i] = new double[nodes[i].length];
		}
		z[z.length-1] = new double[outputs.length];
	}

	public double backpropagate(double[] inputs, double[] expectedOutputs) {
		backpropCount++;
		runNetworkZ(inputs);
		
		double cost = costFunc(expectedOutputs);
		
		// sorry future me
		double daijdwijk;
		double daijdbij;
		
		double[][] dcdaij = new double[nodes.length+1][];
		for (int a = 0; a < nodes.length; a++) {
			dcdaij[a] = new double[nodes[a].length];
		}
		dcdaij[dcdaij.length-1] = new double[outputs.length];
		
		for (int j = 0; j < outputs.length; j++) {
			dcdaij[dcdaij.length-1][j] = 2 * (outputs[j] - expectedOutputs[j]);
			
			for (int k = 0; k < nodes[nodes.length-1].length; k++) {
				daijdwijk = nodes[nodes.length-1][k] * sigmoidPrime(z[z.length-1][j]);
				chweights[chweights.length-1][nodes[nodes.length-1].length * j + k] += dcdaij[dcdaij.length-1][j] * daijdwijk;
			}
			
			daijdbij = sigmoidPrime(z[z.length-1][j]);
			chbias[chbias.length-1][j] += dcdaij[dcdaij.length-1][j] * daijdbij;
		}
		
		
		for (int i = nodes.length-1; i > 0; i--) {
			 for (int j = 0; j < nodes[i].length; j++) {
				 
				 for (int k = 0; k < dcdaij[i+1].length; k++) {
					dcdaij[i][j] += dcdaij[i+1][k] * weights[i+1][dcdaij[i+1].length*j + k] * sigmoidPrime(z[i+1][k]);
				 }
				 
				 for (int k = 0; k < nodes[i-1].length; k++) {
					 chweights[i][nodes[i-1].length*j + k] += dcdaij[i][j] * nodes[i-1][k] * sigmoidPrime(z[i][j]);
				 }
				 chbias[i][j] += dcdaij[i][j] * sigmoidPrime(z[i][j]);
				 
			 }
		}
		
		
		for (int j = 0; j < nodes[0].length; j++) {
			
			if (nodes.length > 1) {
				for (int k = 0; k < nodes[1].length; k++) {
					dcdaij[0][j] += dcdaij[1][k] * weights[1][nodes[1].length*j + k] * sigmoidPrime(z[1][k]);
				}
			} else {
				for (int k = 0; k < outputs.length; k++) {
					dcdaij[0][j] += dcdaij[1][k] * weights[1][outputs.length*j + k] * sigmoidPrime(z[1][k]);
				}
			}
			
			for (int k = 0; k < inputs.length; k++) {
				daijdwijk = inputs[k] * sigmoidPrime(z[0][j]);
				chweights[0][nodes[0].length * k + j] += dcdaij[0][j] * daijdwijk;
			}
			
			daijdbij = sigmoidPrime(z[0][j]);
			chbias[0][j] += dcdaij[0][j] * daijdbij;
		}
		
		return cost;
	}
	
	public void flush() {
		for (int i = 0; i < chweights.length; i++) {
			for (int j = 0; j < chweights[i].length; j++) {
				weights[i][j] -= chweights[i][j]/backpropCount;
				chweights[i][j] = 0;
			}
		}
		
		for (int i = 0; i < chbias.length; i++) {
			for (int j = 0; j < chbias[i].length; j++) {
				bias[i][j] -= chbias[i][j]/backpropCount;
				chbias[i][j] = 0;
			}
		}
		backpropCount = 0;
	}
	
	public double[] runNetworkZ(double[] in) {
		inputs = in;
		
		for (int i = 0; i < nodes[0].length; i++) {
			z[0][i] = bias[0][i];
			for (int j = 0; j < inputs.length; j++) {
				z[0][i] += inputs[j] * weights[0][i * inputs.length + j];
			}
			nodes[0][i] = sigmoid(z[0][i]);
		}
		
		for (int n = 1; n < nodes.length; n++) {
			for (int i = 0; i < nodes[n].length; i++) {
				z[n][i] = bias[n][i];
				for (int j = 0; j < nodes[n-1].length; j++) {
					z[n][i] += nodes[n-1][j] * weights[n][i * nodes[n-1].length + j];
				}
				nodes[n][i] = sigmoid(z[n][i]);
			}
		}
		
		for (int i = 0; i < outputs.length; i++) {
			z[z.length-1][i] = bias[bias.length-1][i];
			for (int j = 0; j < nodes[nodes.length-1].length; j++) {
				z[z.length-1][i] += nodes[nodes.length-1][j] * weights[weights.length-1][i * nodes[nodes.length-1].length + j];
			}
			outputs[i] = sigmoid(z[z.length-1][i]);
		}
		
		return outputs;
	}
	
	//	for multithreading
	public void condense(double[][] newWeights, double[][] newBias, int size) {
		System.out.println(backpropCount);
		backpropCount = size;
		for (int i = 0; i < chweights.length; i++) {
			for (int j = 0; j < chweights.length; j++) {
				chweights[i][j] = newWeights[i][j];
			}
		}
		for (int i = 0; i < chbias.length; i++) {
			for (int j = 0; j < chbias.length; j++) {
				chbias[i][j] = newBias[i][j];
			}
		}
	}
	
	protected static double sigmoidPrime(double x) {
		return sigmoid(x) * (1 - sigmoid(x));
	}
}
