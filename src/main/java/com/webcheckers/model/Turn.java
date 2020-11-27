package com.webcheckers.model;

import com.webcheckers.ui.PostSubmitTurnRoute;
import com.webcheckers.util.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Keeps trck of all items needed during a Player's Turn
 *
 * @authors
 *      Summer DiStefano
 *      Frank Abbey
 *      Raisa Hossain
 *      Stephen Bosonac
 *
 * @version 4/1/2020
 *
 */
public class Turn {

    public enum State {
        SINGLE_MOVE,
        JUMP_MOVE,
        SUBMITTED,
        EMPTY_TURN
    }

    private CheckersGame game;
    private Board board;
    private Player player;
    public Stack<Board> boardVersions;
    public List<Position> positionsTakenThisTurn;
    private State state;

    private Piece.PieceColor pieceColor;
    private final String VALID_MOVE = "Valid move";
    private final String MORE_THAN_ONE_SPACE_MOVE = "Invalid move: Can only move one space";
    private final String NOT_FORWARD_MOVE = "Invalid move: Incorrect move direction";
    private final String MUST_MAKE_JUMP_MOVE = "Invalid move: must make jump move";
    private final String MUST_SAME_PIECE_MOVE = "Invalid move: must jump with same Piece";
    public final String FINALIZED = "Turn has been finalized";
    public final String PLAY_AGAIN = "You may play again";

    /**
     * Create a new Turn
     *
     * @param game
     * @param player
     * @param pieceColor
     */
    public Turn(CheckersGame game, Player player, Piece.PieceColor pieceColor) {
        this.game = game;
        this.board = game.getBoard();
        this.boardVersions = new Stack<>();
        boardVersions.push(board);
        this.player = player;
        this.pieceColor = pieceColor;
        this.state = State.EMPTY_TURN;
        this.positionsTakenThisTurn = new ArrayList<>();
    }

    /**
     * Get the Player for this Turn
     *
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the PieceColor of the Turn (which color's turn it is)
     *
     * @return Piece.PieceColor
     */
    public Piece.PieceColor getPlayerColor() {
        return pieceColor;
    }

    public CheckersGame getGame() {
        return game;
    }

    /**
     * Determine if a Move is valid
     *
     * @param move
     * @return Message - INFO: move is valid and has been made
     *                   ERROR: move is invalid and cannot be made
     */
    public Message validateMove(Move move) {
        Message resultMessage;

        move.setPieceColor(pieceColor);
        move.setPlayer(player);

        Piece pieceMoved = getCurrentBoard().getPiece(move.getStart());

        boolean emptyMoveStack = game.getLastMoves().empty();

        if(!pieceMoved.isKing() && !move.isForward()) {
            resultMessage = Message.error(NOT_FORWARD_MOVE);
        }
        //IF Player tries to make a SINGLE move
        else if(move.isSingleMove()) {
            //Player tries to make a single move after having made a jump move
            if(!emptyMoveStack && game.getLastMoves().peek().isJumpType()) {
                resultMessage = Message.error(MUST_MAKE_JUMP_MOVE);
            }
            //Player tries to make a first Single Move when there are any Jump Moves available
            else if(getCurrentBoard().jumpMoveAvailable(this.pieceColor, null)) {
                resultMessage = Message.error(MUST_MAKE_JUMP_MOVE);
            }
            else {
                resultMessage = new Message(VALID_MOVE, Message.Type.INFO);
                this.makeMove(move);
            }
        }
        //If the Player is making a JUMP move:
        //  1. Check if this is their first move this Turn
        //  2. If not, then it must be a jump move with a Piece they already made a Jump with
        else if(move.isJumpMove(board)) {
            //If the last Move was a jump, make sure the Player is using the same Piece
            if(!emptyMoveStack && game.getLastMoves().peek().isJumpType()) {
                if(move.getStart().equals(game.getLastMoves().peek().getEnd())) {
                    resultMessage = new Message(VALID_MOVE, Message.Type.INFO);
                    this.makeMove(move);
                }
                else {
                    resultMessage = Message.error(MUST_SAME_PIECE_MOVE);
                }
            }
            else {
                resultMessage = new Message(VALID_MOVE, Message.Type.INFO);
                this.makeMove(move);
            }

        }
        //IF the Player tried something that wasn't a jump and wasn't a single move
        else if(!move.isJumpMove(board)) {
            resultMessage = Message.error(MORE_THAN_ONE_SPACE_MOVE);
        }
        //otherwise the Move is valid
        else {
            resultMessage = new Message(VALID_MOVE, Message.Type.INFO);
            this.makeMove(move);
        }

        return resultMessage;
    }

    /**
     * If is it determined to be valid with 'validateMove()', the Move
     * is made here
     *
     * @param move
     */
    public void makeMove(Move move) {
        //make the Move on a copy of the current Board
        Board boardCopy = getCurrentBoard().copyBoard();
        boardCopy.spaceAt(move.getStart()).movePiece(boardCopy.spaceAt(move.getEnd()));

        //if the Piece is not a King, and it was not forward
        if(move.isSingleMove()) {
            setState(State.SINGLE_MOVE);
            game.addMove(move, Move.Type.SINGLE);
        }

        if(move.isJumpMove(getCurrentBoard())) {
            setState(State.JUMP_MOVE);

            //when the turn is submitted, this list will be gone through, and the
            //Pieces on the spaces will be removed
            positionsTakenThisTurn.add(move.getJumpedPosition());

            game.addMove(move, Move.Type.JUMP);
        }

        if(move.endsOnKingRow()) {
            //if the Piece is not already a King
            boardCopy.spaceAt(move.getEnd()).getPiece().setToKing();
        }

        //save this Board as the latest version
        boardVersions.push(boardCopy);

    }

    /**
     * Before a Player submits a Move, they can take it back with
     * the "Back Up" button
     */
    public void backUpMove() {
        Move lastMove = game.getLastMoves().pop();
        //reversing start/end
        Move backupMove = new Move(lastMove.getEnd(), lastMove.getStart());
        //this new Move is made
        this.makeMove(backupMove);
        this.state = State.EMPTY_TURN;
    }

    /**
     * Retrieve the most up-to-date version of the Board
     *
     * @return Board
     */
    public Board getCurrentBoard() {
        if(boardVersions.empty())
            return board;
        else
            return boardVersions.peek();
    }

    /**
     * Set the State of the Turn (SINGLE_MOVE, JUMP_MOVE, SUBMITTED, EMPTY_TURN)
     *
     * @param state
     */
    public void setState(Turn.State state) {
        this.state = state;
    }

    /**
     * Retrieve the State of a Turn
     *
     * @return Turn.State
     */
    public Turn.State getState() {
        return state;
    }

}
