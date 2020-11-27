package com.webcheckers.model;

/**
 * Class representing a single Checkers piece.
 *
 * @authors
 *      Summer DiStefano
 *      Frank Abbey
 *      Raisa Hossain
 *      Stephen Bosonac
 *
 * @version 2/22/2020
 */
public class Piece {

    /** Possible colors for a Checkers piece. */
    public enum PieceColor {RED, WHITE}

    /** Possible types for a Checkers piece. */
    public enum PieceType {SINGLE, KING}

    /** Color of this piece. */
    private PieceColor color;

    /** Type of this piece. */
    private PieceType type;

    /**
     * Constructor for an individual Checkers piece.
     *
     * @param color Color for this piece
     */
    public Piece(PieceColor color) {
        this.type = PieceType.SINGLE;
        this.color = color;
    }

    /**
     * @return Color of this piece.
     */
    public PieceColor getColor() {
        return this.color;
    }

    /**
     * @return Type of this space.
     */
    public PieceType getType() {
        return this.type;
    }

    public boolean isRed() {
        return this.color.equals(PieceColor.RED);
    }

    /**
     * Sets this piece's type to King.
     */
    public void setToKing() {
        this.type = Piece.PieceType.KING;
    }

    /**
     * Determine quickly if a Piece is a King
     * @return
     */
    public boolean isKing() {
        return this.type == PieceType.KING;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return color == piece.color;
    }

}
