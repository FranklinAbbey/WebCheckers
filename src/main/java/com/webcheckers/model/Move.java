package com.webcheckers.model;

import java.util.Objects;

/**
 * Move represents a piece's starting and ending point when made
 * by a Player
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
public class Move {

    private Position start;
    private Position end;

    private Piece.PieceColor pieceColor;
    private Player player;

    public enum Type {
        SINGLE,
        JUMP
    }

    private Type type;
    private final int BOARD_DIM = 8;

    /**
     * Create a new Move. This requires a Start and End Position object
     *
     * @param start - starting coordinates
     * @param end - ending coordinates
     */
    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
        pieceColor = null;
        player = null;
        type = null;

    }

    /**
     * Retrieve the starting position of a Move
     *
     * @return Position
     */
    public Position getStart() {
        return start;
    }

    /**
     * Retrieve the ending position of a Move
     *
     * @return Position
     */
    public Position getEnd() {
        return end;
    }

    /**
     * Determine if a move is of Jump Type
     *
     * @return boolean
     */
    public boolean isJumpType() {
        return this.type == Type.JUMP;
    }

    /**
     *  Set the piece color associated with a Move
     *
     * @param pieceColor
     */
    public void setPieceColor(Piece.PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    /**
     *  Set the Player associated with a Move
     *
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     *  Set the Type associated with a Move
     *
     * @param type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Determine if a Move was made by moving over one space diagonally
     *
     * @return boolean
     */
    public boolean isSingleMove() {

        int rowDiff = (this.getStart().getRow() - this.getEnd().getRow());
        int cellDiff = (this.getStart().getCell() - this.getEnd().getCell());

        return ((rowDiff == -1 || rowDiff == 1) && ((cellDiff == -1 || cellDiff == 1)));
    }

    /**
     * Determine if a Move was made by jumping over an opponent's piece in a span
     * of two spaces diagonally
     *
     * @return boolean
     */
    public boolean isJumpMove(Board board) {
        int rowDiff = (this.getStart().getRow() - this.getEnd().getRow());
        int cellDiff = (this.getStart().getCell() - this.getEnd().getCell());

        //make sure the Piece making the possible move is the color of the Turn
        if(board.spaceAt(this.getStart()).getPiece() != null) {
            if (board.spaceAt(this.getStart()).getPiece().getColor() != (this.pieceColor))
                return false;
        }
        //make sure the Space being landed in is empty
        if(board.spaceAt(this.getEnd()).getPiece() != null)
            return false;
        //make sure the Space being jumped actually had a Piece in it
        if(board.spaceAt(this.getJumpedPosition()).getPiece() == null)
            return false;
        //make sure the Space being jumped is an opponent Piece
        if(board.spaceAt(this.getJumpedPosition()).getPiece().equals(
                board.spaceAt(this.getStart()).getPiece()))
            return false;

        return ((rowDiff == -2 || rowDiff == 2) && ((cellDiff == -2 || cellDiff == 2)));
    }

    /**
     * Determine if a Move was correctly oriented
     *
     * @return boolean
     */
    public boolean isForward() {
        int rowDiff = (this.getStart().getRow() - this.getEnd().getRow());

        //Red Pieces are placed at the "top" of the Board (pre-flip, row index <= 2)
        if(pieceColor.equals(Piece.PieceColor.RED))
            return rowDiff < 0;
        //White Pieces are placed at the "bottom" of the Board (pre-flip, row index >= 5)
        else if(pieceColor.equals(Piece.PieceColor.WHITE))
            return rowDiff > 0;
        else
            return false;
    }

    /**
     * Get the coordinates of the Space that was jumped during a Jump Move.
     *
     * @return Position
     */
    public Position getJumpedPosition() {
        int rowDiff = (this.getStart().getRow() - this.getEnd().getRow());
        int cellDiff = (this.getStart().getCell() - this.getEnd().getCell());

        int jumpedRow;
        int jumpedCell;

        //the Row and Cell differences are either +2 or -2
        if(rowDiff > 0) {
            jumpedRow = this.getStart().getRow() - 1;
        }
        else
            jumpedRow = this.getStart().getRow() + 1;

        if(cellDiff > 0) {
            jumpedCell = this.getStart().getCell() - 1;
        }
        else
            jumpedCell = this.getStart().getCell() + 1;

        return new Position(jumpedRow, jumpedCell);
    }

    /**
     * Determine if a Move remains on the CheckersBoard
     *
     * @return boolean
     */
    public boolean onBoard() {
        if(this.getEnd().getRow() >= 0 && this.getEnd().getRow() < BOARD_DIM) {
            return this.getEnd().getCell() >= 0 && this.getEnd().getCell() < BOARD_DIM;
        }
        return false;
    }

    /**
     * Determine if a Move landed in the last (or "edge") row of the Board
     * and should be Kinged. This would be the last row in the Player's move direction
     *
     * @return boolean - true: the Move landed in an edge row
     *                   false: the Move did not land in an edge row
     */
    public boolean endsOnKingRow() {
        if(this.getEnd().getRow() == BOARD_DIM - 1 && pieceColor == Piece.PieceColor.RED)
            return true;
        else if(this.getEnd().getRow() == 0 && pieceColor == Piece.PieceColor.WHITE)
            return true;

        return false;
    }

    /**
     * Orient a Move correctly if it was made on a flipped version of the board
     *
     * @return Move - the new Move with translated coordinates
     */
    public Move flipMove() {
        Position start = this.getStart();
        Position end = this.getEnd();

        //the Cell Index of the Move does not need to be changed, base on how the
        //board is laid out
        Position newStart = new Position ((BOARD_DIM - 1) - start.getRow(),
                start.getCell());
        Position newEnd = new Position ((BOARD_DIM - 1) - end.getRow(),
                end.getCell() );

        return new Move(newStart, newEnd);
    }

    /**
     * Equals method
     *
     * @param o - object to be tested for equality against called object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(start, move.start) &&
                Objects.equals(end, move.end) &&
                pieceColor == move.pieceColor &&
                Objects.equals(player, move.player);
    }

    /**
     * ToString method
     *
     * @return - Move's start and end displayed
     */
    @Override
    public String toString() {
        return "Move{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

}
