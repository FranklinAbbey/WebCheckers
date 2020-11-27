package com.webcheckers.model;

import java.util.Iterator;

/**
 * Class representing a single row on a Checkers board.
 *
 * @authors
 *      Summer DiStefano
 *      Frank Abbey
 *      Raisa Hossain
 *      Stephen Bosonac
 *
 * @version 2/22/2020
 */
public class Row {
    /** Number of spaces in a row. */
    private int dim = 8;

    /** Index number of this row. */
    private int index;

    /** Array of spaces in this row. */
    private Space[] spaces;

    /** Iterator for iterating through the spaces in this row. */
    private Iterator<Space> row;

    /**
     * Constructor for a single row on a Checkers board.
     *
     * @param index Index number for this row
     * @param startColor Color of first space in this row
     */
    public Row(int index, Space.SpaceColor startColor) {
        this.index = index;
        this.spaces = createSpaces(startColor);
        this.row = iterator();
    }

    /**
     * Used by the constructor to create the spaces contained in this row.
     *
     * @param startColor Space color this row will start with
     * @return Array of spaces
     */
    private Space[] createSpaces(Space.SpaceColor startColor) {
        Space[] spaces = new Space[8];
        Space.SpaceColor otherColor;
        if (startColor == Space.SpaceColor.BLACK) {
            otherColor = Space.SpaceColor.WHITE;
        } else {
            otherColor = Space.SpaceColor.BLACK;
        }
        for (int s = 0; s < dim; ++s) {
            // If space will have an even index:
            if (s % 2 == 0) {
                spaces[s] = new Space(s, startColor, null);
            } else {
                spaces[s] = new Space(s, otherColor, null);
            }
        }
        return spaces;
    }

    /**
     * Creates an Iterator object used to iterate through the spaces in this row.
     *
     * @return Row iterator
     */
    public Iterator<Space> iterator() {
        Iterator<Space> row = new Iterator<Space>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < dim && spaces[currentIndex] != null;
            }

            @Override
            public Space next() {
                return spaces[currentIndex++];
            }
        };
        return row;
    }

    /**
     * @return Index number of this row
     */
    public int getIndex() { return this.index; }

    public void setIndex(int index) {
        this.index = index;
    }

    public Space getSpace(int cell) {
        return spaces[cell];
    }

    public Space[] getSpaces() {
        return spaces;
    }

    /**
     * Checks to make sure the inex being looked at is either
     * the top 3 rows or the bottom 3 for initial piece placement
     *
     * @param index - the current index
     * @return true - the index is within that range
     *         false - the index is not within range
     */
    public boolean isStartingRow(int index) {
        return (index <= 2 || (index >= 5 && index < 8));
    }

}
