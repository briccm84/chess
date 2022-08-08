package chess.pieces;

import chess.Board;
import chess.Piece;
import chess.Pieces;
import chess.TeamColor;

public class Move {

    private final int moveTox,moveToy,moveFromx,moveFromy;
    Piece captured;
    Board board;


    public Move( int moveFromx, int moveFromy,int moveTox, int moveToy,Board board){
        this.moveTox = moveTox;
        this.moveToy = moveToy;
        this.moveFromx = moveFromx;
        this.moveFromy = moveFromy;
        this.board = board;
        if(!(moveTox>= 0 && moveTox < 8 && moveToy >=0 && moveToy <8)) {
            System.out.println("move is to off the board");
            captured = new Empty(Pieces.EMPTY, TeamColor.NONE);
        }
        else {
            captured = this.board.board[moveTox][moveToy].getPiece();
        }
    }

    public int getMoveTox() {return moveTox;}
    public int getMoveToy() {return moveToy;}
    public int getMoveFromx() {return moveFromx;}
    public int getMoveFromy() {return moveFromy;}
    public Piece getCaptured(){return captured;}
    public String toString(){return "from: "+moveFromx+","+moveFromy+"\nto: "+moveTox+","+moveToy;}
}
