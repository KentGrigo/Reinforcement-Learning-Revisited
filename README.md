# Reinforcement Learning, Revisited

## What is this?
This is the joint Master's thesis by Thor Bagge and Kent Grigo.
You can read our thesis [here](https://github.com/KentGrigo/Reinforcement-Learning/blob/master/Master's%20Thesis%20-%20Thor%20Bagge%20and%20Kent%20Grigo.pdf).

We combine reinforcement-learning algorithms and search algorithms with a neural network to make a strong player for Connect Four.

Connect Four is like an extended Tic-Tac-Toe where you have a board with six rows and seven columns.
The board is vertical, meaning that you can only insert a disc at the bottom and then let them stack.
The end goal is to get four disks in a vertical, horizontal, or diagonal line.

This project contains the reinforcement-learning algorithms:
- Temporal learning (TD-lambda)
- SARSA

combined with the noise-injection methods:
- Epsilon greedy
- Softmax

and the following search algorithms:
- Minimax
- Monte-Carlo Tree Search (MCTS)

## Revisited, why?
This project meant a lot to me, so I wanted to take a look at it again.
I thought it could be fun to apply the industry standards that I've learnt since I originally worked on it.

I chose to make a new repository because I wanted the original work to stand on its own.
My ideal approach would have been to fork the repository; but since I can't fork my own repositories, I had to make a new one.
[Here's the original repository](https://github.com/KentGrigo/Reinforcement-Learning).
