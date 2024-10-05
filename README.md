# SnakeNeuralNetwork
About:
This program simulates the popular action video game snake using the KarelJRobot library as the UI and trains a neural network model through evolution to play the game.

How to run:
To run the runnable jar file, it must be run through the command line with java installed.

What happens when running:
When running the program, it will initially start training the neural network using the predetermined parameters set in the code of the runner class. It will display a percent number indicating its progress training. When it reaches 100%, a screen will pop up. Then the the program will dispaly some information on the consol. It will ask for what generation you wish to view and then how many steps it will run for (recommended to enter the total number of steps it has ran for). Then it will ask for a final enter to continue and then the user must switch back to the display screen. It will show the best neural network in that generation playing snake. When it is over the program will go back to asking for the next generation you wish to view.

Initial Parameters:
The main parameters for the program to run are the first 5 integer variables in the runner class. genAmount is the amount of generations there will be. useSteps will be the amount of turns the game will play (useful if snake is in infinite loop), stepIncrement increases the amount of steps the game will run after every 10% of the generations. genSize is the amount of neural networks simulated each generation, aiType represents how the inputs are fed into the neural network and its size. aiType 0 contains 4 inputs, 3 one's or zero's representing if there is a wall directly to the head of the snake's right, forward, and left and a fourth input for the angle direction of the apple condensed into a value between -1 and 1. aiType 1 contains 5 inputs and works the same as aiType 0 but with an additional input representing the size of the snake condensed into a value between 0 and 1. aiType 2 contains 25 inputs, 23 ones or zeros representing a map of the 5 by 5 area surrounding the head excluding the point of the head and right below the head (would always be 1) and an input for each the direction of the apple and size of snake. aiType 3 is almost the same as aiTpye 1 with the 5 inputs but with an additional array of inputs representing memory that is retained from the previous output.

About the neural networks:
The neural networks take in inputs and outputs 3 outputs (except for aiType 3 which has extra to save for the next inputs). The 3 outputs represent the 3 moves the snake can perform each step: turning left, going straight, or turning right. Every step, the inputs gained from the game state are given as inputs to the neural network and the largest output determines what move the snake will make.
