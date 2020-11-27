package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Board represents the status of the Checkers
 * game board
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
public class Board {

    /** Number of rows on the board. */
    private int dim = 8;

    /** Array of rows on this board. */
    private Row[] rows;

    /** Iterator for iterating through the rows on this board. */
    private Iterator<Row> board;

    private int redPieces;
    private int whitePieces;

    /**
     * Default Constructor for a Checkers board.
     */
    public Board() {
        this.rows = createBoard();
        this.board = iterator();
        redPieces = 0;
        whitePieces = 0;
        initialPieces();
    }

    /**
     * Second constructor that takes a boolean as it's parameter
     * @param pieces - true: the board will initialize pieces on itself
     *                 false: the board will be blank, with no pieces
     */
    public Board(boolean pieces) {
        this.rows = createBoard();
        this.board = iterator();
        redPieces = 0;
        whitePieces = 0;
        if(pieces)
            initialPieces();
    }

    //TODO - remove test functions when done

    public static Board testInabilityBoard() {
        Board testBoard = new Board(false);

        // Test setup for victory/defeat by inability to move (white player loses).
        testBoard.spaceAt(new Position(5,4)).placePiece(new Piece(Piece.PieceColor.WHITE));
        testBoard.spaceAt(new Position(2,3)).placePiece(new Piece(Piece.PieceColor.RED));
        testBoard.spaceAt(new Position(4,3)).placePiece(new Piece(Piece.PieceColor.RED));
        testBoard.spaceAt(new Position(3,2)).placePiece(new Piece(Piece.PieceColor.RED));
        testBoard.spaceAt(new Position(4,5)).placePiece(new Piece(Piece.PieceColor.RED));
        testBoard.spaceAt(new Position(3,6)).placePiece(new Piece(Piece.PieceColor.RED));
        testBoard.spaceAt(new Position(6,3)).placePiece(new Piece(Piece.PieceColor.RED));
        testBoard.spaceAt(new Position(7,2)).placePiece(new Piece(Piece.PieceColor.RED));
        testBoard.spaceAt(new Position(6,5)).placePiece(new Piece(Piece.PieceColor.RED));
        testBoard.spaceAt(new Position(7,6)).placePiece(new Piece(Piece.PieceColor.RED));

    return testBoard;
}

    public static Board testAIMultiJumpBoard() {
        Board testBoard = new Board(false);
        testBoard.spaceAt(new Position(3,4)).placePiece(new Piece(Piece.PieceColor.RED));
        testBoard.spaceAt(new Position(2,5)).placePiece(new Piece(Piece.PieceColor.RED));

        testBoard.spaceAt(new Position(5,4)).placePiece(new Piece(Piece.PieceColor.WHITE));
        testBoard.spaceAt(new Position(6,1)).placePiece(new Piece(Piece.PieceColor.WHITE));
        return testBoard;
    }

    public static Board testMultipleJumpBoard() {
        Board testBoard = new Board(false);
        testBoard.spaceAt(new Position(3,4)).placePiece(new Piece(Piece.PieceColor.WHITE));
        testBoard.spaceAt(new Position(3,0)).placePiece(new Piece(Piece.PieceColor.WHITE));
        testBoard.spaceAt(new Position(5,4)).placePiece(new Piece(Piece.PieceColor.WHITE));

        testBoard.spaceAt(new Position(2,3)).placePiece(new Piece(Piece.PieceColor.RED));
        testBoard.spaceAt(new Position(0,5)).placePiece(new Piece(Piece.PieceColor.RED));
        return testBoard;
    }

    public static Board testKingBoard() {
        Board testBoard = new Board(false);
        testBoard.spaceAt(new Position(1,4)).placePiece(new Piece(Piece.PieceColor.WHITE));

        testBoard.spaceAt(new Position(6,3)).placePiece(new Piece(Piece.PieceColor.RED));
        return testBoard;
    }


    /**
     * Used by the constructor to create the rows contained on this board.
     *
     * @return Array of rows
     */
    private Row[] createBoard() {
        Row[] rows = new Row[dim];
        for (int r = 0; r < this.dim; ++r) {
            if (r % 2 == 0) {
                rows[r] = new Row(r, Space.SpaceColor.BLACK);
            } else {
                rows[r] = new Row(r, Space.SpaceColor.WHITE);
            }
        }
        return rows;
    }

    /**
     * Creates an iterator object used to iterate through the rows on this board.
     *
     * @return Board iterator
     */
    public Iterator<Row> iterator() {
        Iterator<Row> board = new Iterator<Row>() {
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
        return board;
    }

    /**
     * Create a copy of a the board
     *
     * @return Board board
     */
    public Board copyBoard() {

        Board boardCopy = new Board(false);
        Row[] originalRows = this.getRows();

        for(int i = 0; i < dim; i++) {

            for(int j = 0; j < dim; j++) {
                Space spaceCopy = boardCopy.getRows()[i].getSpace(j);
                Piece originalPiece = originalRows[i].getSpace(j).getPiece();
                spaceCopy.placePiece(originalPiece);
                if(originalPiece != null) {
                    if(originalPiece.isRed())
                        boardCopy.redPieces++;
                    else {
                        boardCopy.whitePieces++;
                    }
                }
            }
        }

        return boardCopy;
    }

    /**
     * Access a Space object by index
     *
     * @param position a Position object
     * @return Space space
     */
    public Space spaceAt(Position position) {
        return rows[position.getRow()].getSpace(position.getCell());
    }

    /**
     * Helper method to retrieve a piece on the Board at a
     * particular Position
     *
     * @param position
     * @return Piece
     */
    public Piece getPiece(Position position) {
        return this.spaceAt(position).getPiece();
    }

    /**
     * Method can be  used to search entire board for jump moves or if there
     * are any for a particular position
     *
     * @param pieceColor - given as either an ActiveColor or PieceColor, necessary to
     *                     only search for jump moves of a specific color
     * @param givenPosition - not null: only check that position (this is used
     *                        if a Player is making a second jump move, and can only
     *                        use that same Piece)
     *                        null :check all available jump moves for that Player
     * @return true - there is at least one jump move available
     *         false - there are no jump moves available
     */
    public boolean jumpMoveAvailable(Object pieceColor, Position givenPosition) {
        //this allows 'jumpMoveAvailable()' to take Game.ActiveColor or Piece.PieceColor
        if(pieceColor instanceof CheckersGame.ActiveColor) {
            if(pieceColor == CheckersGame.ActiveColor.RED)
                pieceColor = Piece.PieceColor.RED;
            else
                pieceColor = Piece.PieceColor.WHITE;
        }

        Move[] possibleJumpMoves;

        //searching entire board
        if(givenPosition == null) {

            this.board = iterator();
            while (this.board.hasNext()) {
                Row row = this.board.next();

                Iterator<Space> rowIterator = row.iterator();
                while (rowIterator.hasNext()) {
                    Space space = rowIterator.next();

                    if (space.getPiece() != null) {

                        Position currentPosition = new Position(row.getIndex(), space.getCellIdx());
                        possibleJumpMoves = createMovesByType(currentPosition, false);

                        Piece currentPiece = this.getPiece(currentPosition);

                        //go through the four possible jump moves returned
                        for (Move possibleJumpMove : possibleJumpMoves) {
                            possibleJumpMove.setPieceColor((Piece.PieceColor) pieceColor);
                            //'isJumpMove()' will make sure there is a Piece to be jumped
                            if(possibleJumpMove.onBoard() && possibleJumpMove.isJumpMove(this)) {
                                //if the piece is not a King, the move must also be forward
                                if(!currentPiece.isKing() && possibleJumpMove.isForward()) {
                                    return true;
                                }
                                //otherwise, the Piece is a King and the Move doesn't have to be forward
                                if(currentPiece.isKing())
                                    return true;
                            }

                        }
                    }

                }
            }
        }
        //only searching a specific Position
        else {
            possibleJumpMoves = createMovesByType(givenPosition, false);

            Piece currentPiece = this.getPiece(givenPosition);

            //go through the four possible jump moves returned
            for(Move possibleJumpMove : possibleJumpMoves) {
                possibleJumpMove.setPieceColor((Piece.PieceColor) pieceColor);

                //'isJumpMove()' will make sure there is a Piece to be jumped
                if(possibleJumpMove.onBoard() && possibleJumpMove.isJumpMove(this)) {
                    //if the piece is not a King, the move must also be forward
                    if(!currentPiece.isKing() && possibleJumpMove.isForward()) {
                        return true;
                    }
                    //otherwise, the Piece is a King and the Move doesn't have to be forward
                    if(currentPiece.isKing())
                        return true;
                }

            }
        }

        return false;
    }

    /**
     * Search the board for all Moves that a given color could take
     *
     * @param givenPieceColor - the color whose available moves will be calculated
     * @return List - an ArrayList of the available moves
     */
    public List<Move> getAvailableMoves(Piece.PieceColor givenPieceColor) {
        List<Move> allMoves = new ArrayList<>();

        this.board = iterator();
        while(board.hasNext()) {
            Row row = board.next();

            Iterator<Space> rowIterator = row.iterator();
            while(rowIterator.hasNext()) {

                Space space = rowIterator.next();
                Position currentPosition = new Position(row.getIndex(), space.getCellIdx());
                Piece currentPiece = space.getPiece();

                Move[] possibleSingleMoves;
                Move[] possibleJumpMoves;

                if(currentPiece != null && currentPiece.getColor() == givenPieceColor) {

                    //find and add all valid SINGLE MOVEs available for this space
                    possibleSingleMoves = createMovesByType(currentPosition, true);
                    for(Move singleMove: possibleSingleMoves) {
                        singleMove.setPieceColor(givenPieceColor);
                        //check the validity of the single Move and add it to the List if valid
                        if(singleMove.onBoard()) {
                            //since the Javascript normally handles a Player moving their Piece to an occupied
                            //space, here we must determine that
                            if(this.spaceAt(singleMove.getEnd()).getPiece() == null) {
                                if(!currentPiece.isKing()) {
                                    if(singleMove.isForward()) {
                                        allMoves.add(singleMove);
                                    }
                                }
                                else
                                    allMoves.add(singleMove);
                            }
                        }

                    }

                    //find and add all valid JUMP MOVEs available for this space
                    possibleJumpMoves = createMovesByType(currentPosition, false);

                    //go through the four possible jump moves returned
                    for (Move jumpMove : possibleJumpMoves) {
                        jumpMove.setPieceColor(givenPieceColor);
                        //giving the move its type allows for faster identification later
                        jumpMove.setType(Move.Type.JUMP);
                        //'isJumpMove()' will make sure there is a Piece to be jumped
                        if (jumpMove.onBoard() && jumpMove.isJumpMove(this)) {
                            //if the piece is not a King, the move must also be forward
                            if(!currentPiece.isKing()) {
                                if(jumpMove.isForward()) {
                                    allMoves.add(jumpMove);
                                }
                            }
                            //otherwise, the Piece is a King and the Move doesn't have to be forward
                            else
                                allMoves.add(jumpMove);
                        }
                    }

                }

            }
        }

        return allMoves;
    }

    public Row[] getRows() {
        return rows;
    }

    public int getRedPieces() {
        return redPieces;
    }

    public int getWhitePieces() {
        return whitePieces;
    }

    public void removeRedPiece() {
        redPieces--;
    }

    public void removeWhitePiece() {
        whitePieces--;
    }

    /**
     * Iterate through an array of Spaces where Pieces must be removed
     * (primarily used for multiple jump moves)
     *
     * @param positions - the spaces with Pieces that must be removed
     */
    public void removePieces(List<Position> positions) {
        //System.out.println("removePieces, 'spaces' = " + spaces);

        if(!positions.isEmpty()) {
            for (Position current : positions) {
                if (this.spaceAt(current).getPiece().isRed())
                    removeRedPiece();
                else
                    removeWhitePiece();

                //System.out.println("removePieces, 'current piece' = " + current.getPiece());

                this.spaceAt(current).removePiece();

            }
        }

    }

    /**
     * Create an array of all possible Moves for a given Position on the
     * Board. These Moves may not be valid.
     *
     * @param currentPosition - the position to be analyzed
     * @param single - true: search for all single Moves for a position
     *                 false: search for all jump moves for a position
     * @return Move[] - an array with four possible moves
     */
    public Move[] createMovesByType(Position currentPosition, boolean single) {
        int rowIndex = currentPosition.getRow();
        int cellIndex = currentPosition.getCell();
        int modifier;

        if(single)
            modifier = 1;
        else
            modifier = 2;

        Move[] possibleMoves = {
                new Move(currentPosition, new Position(rowIndex + modifier, cellIndex + modifier)),
                new Move(currentPosition, new Position(rowIndex + modifier, cellIndex - modifier)),
                new Move(currentPosition, new Position(rowIndex - modifier, cellIndex + modifier)),
                new Move(currentPosition, new Position(rowIndex - modifier, cellIndex - modifier)),
        };
        return possibleMoves;
    }



    /**
     * Set up initial placement of the pieces in the top
     * and bottom 3 rows
     */
    public void initialPieces() {
        while(board.hasNext()) {
            Row row = board.next();

            Iterator<Space> rowIterator = row.iterator();
            while(rowIterator.hasNext()) {
                Space space = rowIterator.next();

                if(space.isValid()) {
                    //determine if on redPlayer side of the board or whitePlayer side
                    if(row.getIndex() <= 2) {
                        space.placePiece(new Piece(Piece.PieceColor.RED));
                        redPieces++;
                    } else if(row.getIndex() <= 7 && row.getIndex() >= 5) {
                        space.placePiece(new Piece(Piece.PieceColor.WHITE));
                        whitePieces++;
                    }
                }
            }
        }

    }

}
