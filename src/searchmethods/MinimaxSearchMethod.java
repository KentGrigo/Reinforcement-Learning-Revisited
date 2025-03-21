package searchmethods;

import auxiliaries.RNG;
import game.*;
import java.util.*;
import neuralnetworks.BoardEncoder;
import reinforcementlearning.Move;
import reinforcementlearning.NeuralNetworkPlayer;
import auxiliaries.Pair;
import reinforcementlearning.ValueFunction;

public class MinimaxSearchMethod implements SearchMethod {

    protected Pair<Move, List<Move>> minimax(Game game, NeuralNetworkPlayer player, int[][] board, BoardChecker boardChecker, ValueFunction vf, BoardEncoder encoder, Move move, int depth, double alpha, double beta, boolean maximizingPlayer) {
        int currentPlayer;
        int previousPlayer;
        int row = move.getRow();
        int column = move.getColumn();
        if (maximizingPlayer) {
            currentPlayer = game.getPlayerNumber(player);
            previousPlayer = Game.getOtherPlayer(currentPlayer);
        } else {
            previousPlayer = game.getPlayerNumber(player);
            currentPlayer = Game.getOtherPlayer(previousPlayer);
        }
        boolean isDraw, hasWinner, initialCall;
        if (row == -1 || column == -1) {
            initialCall = true;
            isDraw = false;
            hasWinner = false;
        } else {
            initialCall = false;
            isDraw = boardChecker.isDraw();
            hasWinner = boardChecker.checkWinner(previousPlayer, row, column);
        }
        if (depth == 0 || isDraw || hasWinner) {
            double res;
            if (hasWinner) {
                if (maximizingPlayer) {
                    res = player.getLoseReward();
                } else {
                    res = player.getWinReward();
                }
            } else if (isDraw) {
                res = player.getDrawReward();
            } else {
                //opponent's turn if the current iteration is maximization for us.
                res = move.getValue();
            }
            move.setValue(res);
            return new Pair(move, null);
        }

        List<Move> possibleMoves = boardChecker.getPossibleMoves();

        int nextTurn;
        int encodingPlayer;
        Comparator<Move> queueComparator;
        if (maximizingPlayer) {
            nextTurn = -1;
            encodingPlayer = currentPlayer;
            queueComparator = Move.Companion.getDecreasingComparator();
        } else {
            nextTurn = 1;
            encodingPlayer = previousPlayer;
            queueComparator = Move.Companion.getIncreasingComparator();
        }
        PriorityQueue<Move> pq = new PriorityQueue<>(possibleMoves.size(), queueComparator);

        int tmpRow, tmpColumn;
        double res;
        List<Move> moves;
        if (initialCall) {
            moves = new LinkedList<>();
        } else {
            moves = null;
        }
        for (Move tmpMove : possibleMoves) {
            tmpRow = tmpMove.getRow();
            tmpColumn = tmpMove.getColumn();
            board[tmpRow][tmpColumn] = currentPlayer;
            res = vf.evaluate(encoder.encodeBoard(game, board, encodingPlayer, nextTurn));
            Move m = new Move(tmpRow, tmpColumn, res);
            pq.add(m);
            board[tmpRow][tmpColumn] = 0;
        }

        Move bestMove = null;

        if (maximizingPlayer) {
            while (!pq.isEmpty()) {
                Move tmpMove = pq.poll();
                tmpRow = tmpMove.getRow();
                tmpColumn = tmpMove.getColumn();
                board[tmpRow][tmpColumn] = currentPlayer;
                Move nextMove = minimax(game, player, board, boardChecker, vf, encoder, tmpMove, depth - 1, alpha, beta, !maximizingPlayer).getFirst();
                board[tmpRow][tmpColumn] = 0;
                if (initialCall) {
                    moves.add(tmpMove);
                }
                tmpMove.setValue(nextMove.getValue());
                bestMove = Move.Companion.max(bestMove, tmpMove);
                alpha = Math.max(alpha, bestMove.getValue());
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            while (!pq.isEmpty()) {
                Move tmpMove = pq.poll();
                tmpRow = tmpMove.getRow();
                tmpColumn = tmpMove.getColumn();
                board[tmpRow][tmpColumn] = currentPlayer;
                Move nextMove = minimax(game, player, board, boardChecker, vf, encoder, tmpMove, depth - 1, alpha, beta, !maximizingPlayer).getFirst();
                board[tmpRow][tmpColumn] = 0;
                if (initialCall) {
                    moves.add(tmpMove);
                }
                tmpMove.setValue(nextMove.getValue());
                bestMove = Move.Companion.min(bestMove, tmpMove);
                beta = Math.min(beta, bestMove.getValue());
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return new Pair(bestMove, moves);
    }

    @Override
    public List<Move> searchForMoves(Game game, NeuralNetworkPlayer player, int searchBudget) {
        if (searchBudget <= 0) {
            throw new IllegalArgumentException("Cannot have search depth " + searchBudget);
        }
        int[][] board = game.getBoard();
        BoardChecker boardChecker = game.getBoardChecker(board);
        ValueFunction vf = player.getValueFunction();
        BoardEncoder encoder = player.getBoardEncoder();
        return minimax(game, player, board, boardChecker, vf, encoder, new Move(-1, -1), searchBudget, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true).getSecond();
    }

    @Override
    public Move searchForMove(Game game, Player player, int searchBudget) {
        List<Move> possibleMoves = game.getPossibleMoves();
        Move bestMove = null;
        double bestColumnValue = Double.NEGATIVE_INFINITY;
        int freeRow, freeColumn;
        double currentColumnValue;
        int[][] board = game.getBoard();
        boolean foundMove = false;
        BoardChecker boardChecker = game.getBoardChecker(board);
        for (Move move : possibleMoves) {
            freeRow = move.getRow();
            freeColumn = move.getColumn();
            currentColumnValue = minimax(game, player, board, boardChecker, freeRow, freeColumn, searchBudget - 1, true);
            if (currentColumnValue != 0) {
                foundMove = true;
            }
//            System.out.println("("+(freeColumn+1)+", value: " + currentColumnValue+")");
            if (currentColumnValue > bestColumnValue) {
                bestColumnValue = currentColumnValue;
                bestMove = move;
            }
        }
        if (!foundMove) {
            bestMove = possibleMoves.get(RNG.nextInt(possibleMoves.size()));
        }
        return bestMove;
    }

    private double minimax(Game game, Player player, int[][] board, BoardChecker boardChecker, int row, int column, int depth, boolean maximizingPlayer) {
        int currentPlayer;
        if (maximizingPlayer) {
            currentPlayer = game.getPlayerNumber(player);
        } else {
            currentPlayer = Game.getOtherPlayer(game.getPlayerNumber(player));
        }
        board[row][column] = currentPlayer;
        boolean isDraw = boardChecker.isDraw();
        boolean hasWinner = boardChecker.checkWinner(currentPlayer, row, column);
        if (depth == 0 || isDraw || hasWinner) {
            double res = 0;
            if (hasWinner) {
                if (maximizingPlayer) {
                    res = 1;
                } else {
                    res = -1;
                }
//                System.out.println("res: " + res);
            } else if (isDraw) {
                res = 0.5;
            }
            board[row][column] = 0;
            return res;
        }

        double bestValue;
        if (maximizingPlayer) {
            bestValue = Double.POSITIVE_INFINITY;
            List<Move> possiblesMoves = boardChecker.getPossibleMoves();
            for (Move move : possiblesMoves) {
                double res = minimax(game, player, board, boardChecker, move.getRow(), move.getColumn(), depth - 1, false);
                bestValue = Math.min(bestValue, res);
            }
        } else {
            bestValue = Double.NEGATIVE_INFINITY;
            List<Move> possiblesMoves = boardChecker.getPossibleMoves();
            for (Move move : possiblesMoves) {
                double res = minimax(game, player, board, boardChecker, move.getRow(), move.getColumn(), depth - 1, true);
                bestValue = Math.max(bestValue, res);
            }
        }
        board[row][column] = 0;
        return bestValue;
    }

}
