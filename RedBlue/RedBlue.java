import java.util.Random;

public class RedBlue {
	static Random random = new Random();
	public static void main(String[] args) {
		
		int x = 0;
		int max = 1000000;
		int startCount = 0;
		
		double avgs = 0;
		double correct = 0;
		double wrong = 0;
		
		// inputs
		double input1 = 0;
		double input2 = 0;
		
		// outputs
		double red = 0;
		double blue = 0;
		
		// lengths of things
		int inputLength = 2;
		int outputLength = 2;
		int nodesLength = 3;
		int biasLength = outputLength + nodesLength;
		int weightLength = inputLength * outputLength * nodesLength;
		
		
		// nodes
		double[] nodes = new double[nodesLength];
		for (int i = 0; i < nodesLength; i++) {
			nodes[i] = 0;
		}
		
		// bias
		double[] bias = new double[biasLength];
		for (int i = 0; i < biasLength; i++) {
			bias[i] = generateNumber()/10;
		}
			
		// weights
		double[] weights = new double[weightLength];
		for (int i = 0; i < weightLength; i++) {
			weights[i] = generateNumber()/10;
		}
		
		// new weights
		double[] nWeights = new double[weightLength];
		for (int i = 0; i < weightLength; i++) {
			nWeights[i] = weights[i];
		}
		
		// new bias
		double[] nBias = new double[biasLength];
		for (int i = 0; i < biasLength; i++) {
			nBias[i] = bias[i];
		}
		
		// avg weights
		double[] aWeights = new double[weightLength];
		for (int i = 0; i < weightLength; i++) {
			aWeights[i] = 0;
		}
		
		// avg bias
		double[] aBias = new double[biasLength];
		for (int i = 0; i < biasLength; i++) {
			aBias[i] = 0;
		}
		
		while (x != max) {
		x++;
		int isRed = random.nextInt(2);
		if (isRed == 1) {
			// (6)+1, (6)+4
			input1 = random.nextInt(6)+1;
			input2 = random.nextInt(6)+4;
		}
		else {
			// (3)+7, (4)+1
			input1 = random.nextInt(3)+7;
			input2 = random.nextInt(5)+1;
		}
		
		for (int i = 0; i < nodesLength; i++) {
			nodes[i] = nodeMath(input1, input2, weights[i], weights[i + 3], bias[i]);;
		}
		
		red = FinalMath(nodes[0], nodes[1], nodes[2], weights[6], weights[7], weights[8], bias[3]);
		blue = FinalMath(nodes[0], nodes[1], nodes[2], weights[9], weights[10], weights[11], bias[4]);
		
		if (startCount == 1) {
			if (red > blue) {
				if (isRed == 1)
					correct++;
				else
					wrong++;	
			}
		
		else {
				if (red < blue) {
					if (isRed == 1)
						wrong++;
					else
						correct++;	
			}
				else
					wrong++;
			}
		}
		double cost;
		cost = costFunction(isRed, red, blue);
		avgs = avgs+cost;
		
		if (x == 1 && x % (max/10) != 0)
			System.out.println(cost);
		if (x % (max/10) == 0) {
			System.out.println(avgs/(max/10));
			avgs = 0;
		}
		double newCost = 100;
		while (cost < newCost) {
			for (int i = 0; i < weightLength; i++) {
				nWeights[i] = weights[i]+makeBetter();
			}
			
			for (int i = 0; i < biasLength; i++) {
				nBias[i] = bias[i]+makeBetter();
			}
			
			for (int i = 0; i < nodesLength; i++) {
				nodes[i] = nodeMath(input1, input2, nWeights[i], nWeights[i + 3], nBias[i]);;
			}
			
			red = FinalMath(nodes[0], nodes[1], nodes[2], nWeights[6], nWeights[7], nWeights[8], nBias[3]);
			blue = FinalMath(nodes[0], nodes[1], nodes[2], nWeights[9], nWeights[10], nWeights[11], nBias[4]);
			
			newCost = costFunction(isRed, red, blue);
		}
		for (int i = 0; i < weightLength; i++) {
			aWeights[i] += nWeights[i];
		}
	
		for (int i = 0; i < biasLength; i++) {
			aBias[i] += nBias[i];
		}
		
		if (x % 100 == 0) {
			for (int i = 0; i < weightLength; i++) {
				weights[i] = aWeights[i]/100;
			}
		
			// change biases
			for (int i = 0; i < biasLength; i++) {
				bias[i] = aBias[i]/100;
			}
			
			// reset avg biases
			for (int i = 0; i < weightLength; i++) {
				aWeights[i] = 0;
			}
				
			// reset avg biases
			for (int i = 0; i < biasLength; i++) {
				aBias[i] = 0;
			}
		}
		if (!(x < max-2000))
			startCount = 1;
		}
		double percent = correct/(correct+wrong);
		System.out.println(percent*100+"%");
//		while (true)
//			QnA(a1, a2, a3, w1, w4, bias1, w2, w5, bias2, w3, w6, bias3, w7, w8, w9, bias4, w10, w11, w12, bias5);
	}
	public static double generateNumber() {
		double w = random.nextInt(61)-30;
		return w;
	}
	public static double equation(double x) {
		return 1/(1+java.lang.Math.exp(-x));
	}
	public static double nodeMath(double x1, double x2, double w1, double w2, double b) {
		return equation(x1*w1+x2*w2+b);
	}
	public static double FinalMath(double x1, double x2, double x3, double w1, double w2, double w3, double b) {
		return equation(x1*w1+x2*w2+x3*w3+b);
	}
	public static double makeBetter() {
		double change = random.nextInt(101)-50;
		return change/100;
	}
	public static double costFunction(int isRed, double red, double blue) {
		double cost;
		if (isRed == 1)
			cost = (red-1)*(red-1)+(blue)*(blue);
		else 
			cost = (red)*(red)+(blue-1)*(blue-1);
		return cost;
	}
//public static void QnA(double a1, double a2, double a3, double w1, double w4, double bias1, double w2, double w5, double bias2, double w3, double w6, double bias3, double w7, double w8, double w9, double bias4, double w10, double w11, double w12, double bias5) {
//	
//	System.out.println("");
//	System.out.println("1-6, 4-9");
//	System.out.println("7-9, 1-5");
//	System.out.println("First input");
//	double input1 = ReaderInt.read();
//	if (input1 == 0) {
//		System.out.println(w1);
//		System.out.println(w2);
//		System.out.println(w3);
//		System.out.println(w4);
//		System.out.println(w5);
//		System.out.println(w6);
//		System.out.println(w7);
//		System.out.println(w8);
//		System.out.println(w9);
//		System.out.println(w10);
//		System.out.println(w11);
//		System.out.println(w12);
//		
//		System.out.println("");
//		
//		System.out.println(bias1);
//		System.out.println(bias2);
//		System.out.println(bias3);
//		System.out.println(bias4);
//		System.out.println(bias5);
//		
//		System.out.println("");
//	}
//	System.out.println("Second input");
//	double input2 = ReaderInt.read();
//	
//	double red;
//	double blue;
//	
//	a1 = a1(input1/10, input2/10, w1, w4, bias1);
//	a2 = a2(input1/10, input2/10, w2, w5, bias2);
//	a3 = a3(input1/10, input2/10, w3, w6, bias3);
//	
//	red = red(a1, a2, a3, w7, w8, w9, bias4);
//	blue = blue(a1, a2, a3, w10, w11, w12, bias5);
//	
//	if (red > blue)
//		System.out.println("Red");
//	if (red < blue)
//		System.out.println("Blue");
//	if (red == blue)
//		System.out.println("I don't know");
//	System.out.println("Confidence: "+java.lang.Math.abs(red-blue)*100+"%");
//	System.out.println("Red: "+red);
//	System.out.println("Blue: "+blue);
}
