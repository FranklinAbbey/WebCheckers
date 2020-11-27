package com.webcheckers.model;

import java.util.Iterator;

/**
 * BoardView used by GUI to display a Board
 *
 * @authors
 *      Frank Abbey
 *      Summer DiStefano
 *      Raisa Hossain
 *      Stephen Bosonac
 *
 * @version 4/1/2020
 *
 */
public class BoardView {

    private Board board;
    private int dim = 8;
    private Row[] rows;

    public BoardView(Board board) {
        this.board = board;
        this.rows = board.getRows();
    }

    /**
     * Creates an iterator object used to iterate through the rows on this board.
     *
     * @return Board iterator
     */
    public Iterator<Row> iterator() {
        Iterator<Row> boardViewIterator = new Iterator<Row>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < dim && rows[currentIndex] != null;
            }

            @Override
            public Row next() {
                return rows[currentIndex++];
            }
        };
        return boardViewIterator;
    }

    /**
     * 'Flip' or orient the board correctly so that each Player views their
     * pieces at the bottom of the board
     */
    public void flip() {
        //create a Board without any Pieces on it initially
        Board flippedBoard = new Board(false);

        for(int i = 0; i < this.rows.length; i++) {

            Row row = this.rows[(dim - 1) - i];

            for(int k = 0; k < row.getSpaces().length; k++) {

                //swap the spaces based on their position
                Space space = row.getSpaces()[(dim - 1) - k];
                flippedBoard.getRows()[i].getSpaces()[k] = space;

            }
        }

        this.board = flippedBoard;
    }

    /**
     * Retrieve the Board object stored within the BoardView
     *
     * @return Board
     */
    public Board getBoard() {
        return this.board;
    }

}
