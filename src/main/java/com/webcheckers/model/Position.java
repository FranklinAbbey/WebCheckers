package com.webcheckers.model;

/**
 * Coordinates to be used in movement logic within CheckersGame
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
public class Position {

    private int row;
    private int cell;

    /**
     * Create a new Position with default row and cell
     */
    public Position() {
        this.row = 0;
        this.cell = 0;
    }

    /**
     * Create a new Position with given row and cell
     *
     * @param row
     * @param cell
     */
    public Position(int row, int cell) {
        this.row = row;
        this.cell = cell;
    }

    /**
     * Retrieve the row index of a Position
     *
     * @return int
     */
    public int getRow() {
        return row;
    }

    /**
     * Retrieve the cell index of a Position
     *
     * @return int
     */
    public int getCell() {
        return cell;
    }

    /**
     * ToString method
     *
     * @return - Position's row and cell displayed
     */
    @Override
    public String toString() {
        return "Position{" +
                "row=" + row +
                ", cell=" + cell +
                '}';
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
        Position position = (Position) o;
        return row == position.row &&
                cell == position.cell;
    }

}
