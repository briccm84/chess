package chess.pieces;

import chess.Piece;
import chess.Pieces;
import chess.TeamColor;

public class Knight extends Piece {
    public Knight(Pieces type, TeamColor color) {super(type, color);}

    @Override
    public boolean possibleMove(Move move){
        this.move = move;
        int dify = move.getMoveFromy() - move.getMoveToy();
        int difx = move.getMoveFromx() - move.getMoveTox();
        if(Math.abs(difx) == 2 && Math.abs(dify) == 1){
            return validMove(move);
        }
        else if(Math.abs(difx) == 1 && Math.abs(dify) == 2){
            return validMove(move);
        }
        return false;
    }
    @Override
    public boolean pathBlocked(Move move){
        return wasSameColor();
    }
}
