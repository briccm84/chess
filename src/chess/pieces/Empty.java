package chess.pieces;

import chess.Piece;
import chess.Pieces;
import chess.TeamColor;

public class Empty extends Piece {
    public Empty(Pieces type, TeamColor color) {
        super(type, color);
    }
    public boolean possibleMove(Move move){
        System.out.println("an empty was moved");
        return false;
    }
}
