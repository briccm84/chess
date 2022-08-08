package chess.pieces;

import chess.Piece;
import chess.Pieces;
import chess.TeamColor;

public class Bishop extends Piece {
    public Bishop(Pieces type, TeamColor color) {
        super(type, color);
    }

    @Override
    public boolean possibleMove(Move move){
        int dify = move.getMoveFromy() - move.getMoveToy();
        int difx = move.getMoveFromx() - move.getMoveTox();
        if (Math.abs(dify) == Math.abs(difx)) {
            return validMove(move);
        }
        return false;
    }

    @Override
    public boolean pathBlocked(Move move){
        this.move = move;
        int dify = move.getMoveFromy() - move.getMoveToy();
        int difx = move.getMoveFromx() - move.getMoveTox();
        boolean pathblocked = false;
        int j = move.getMoveFromy()-Integer.signum(dify);
            for (int i = move.getMoveFromx() - Integer.signum(difx); i != move.getMoveTox(); i = i - Integer.signum(difx)) {
                if (!space.getSpace(i, j).isEmpty()) {
                    return true;
                }
                j = j - Integer.signum(dify);
            }

        return pathblocked || wasSameColor();
    }
}
