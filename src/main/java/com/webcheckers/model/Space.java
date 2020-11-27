package com.webcheckers.model;

import java.util.Objects;

/**
 * Class representing a single space on a Checkers board.
 *
 * @authors
 *      Summer DiStefano
 *      Frank Abbey
 *      Raisa Hossain
 *      Stephen Bosonac
 *
 * @version 2/22/2020
 */
public class Space {
    /** Possible colors for a space. */
    public enum SpaceColor {WHITE, BLACK}

    /** Cell ID (ranges from 0 to 7). */
    private int cellIdx;

    /** Piece object that will occupy the space. */
    private Piece piece;

    /** Color of the space. */
    private SpaceColor color;

    /**
     * Constructor for a single space on a Checkers board.
     *
     * @param cellIdx ID number for this space
     * @param color Color for this space
     * @param piece Piece to be placed on this space
     */
    public Space(int cellIdx, SpaceColor color, Piece piece) {
        this.cellIdx = cellIdx;
        this.color = color;
        this.piece = piece;
    }

    /**
     * @return Cell ID for this space
     */
    public int getCellIdx() { return this.cellIdx; }

    /**
     * @return Color of this space
     */
    public SpaceColor getColor(){ return this.color; }

    /**
     * @return The Piece object on this space
     */
    public Piece getPiece() { return this.piece; }

    /**
     * Determines whether or not this space is a valid space to place a piece on.
     * To be valid, the space must be black, and must not have a piece already on it.
     *
     * @return True if space is valid, false otherwise
     */
    public boolean isValid() {
        return ((this.color != SpaceColor.BLACK) && (this.piece == null));
    }

    /**
     * Places a Piece on this space.
     *
     * @param piece Piece to be placed in this space
     */
    public void placePiece(Piece piece) { this.piece = piece; }

    /**
     * Removes a piece from this space if one is there.
     */
    public void removePiece() { this.piece = null; }

    /**
     * Move a Piece from one Space to another
     *
     * @param destinationSpace Space
     */
    public void movePiece(Space destinationSpace) {
        destinationSpace.placePiece(this.getPiece());
        this.removePiece();
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
        Space space = (Space) o;
        return cellIdx == space.cellIdx &&
                Objects.equals(piece, space.piece) &&
                color == space.color;
    }

}
