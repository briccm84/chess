package chess.pieces;

import chess.Piece;
import chess.Pieces;
import chess.TeamColor;

public class Rook extends Piece {
    public Rook(Pieces type, TeamColor color) {super(type, color);}

    @Override
    public boolean possibleMove(Move move){
        this.move = move;
        int dify = move.getMoveFromy() - move.getMoveToy();
        int difx = move.getMoveFromx() - move.getMoveTox();
        if(dify == 0 || difx == 0){
            //can't move to same position
            if(dify == difx){
                return false;
            }
            return validMove(move);
        }
        return false;
    }
    @Override
    public boolean pathBlocked(Move move){
        int dify = move.getMoveFromy() - move.getMoveToy();
        int difx = move.getMoveFromx() - move.getMoveTox();
        boolean pathblocked = true;
        if(dify == 0){
            for (int i = move.getMoveFromx() - Integer.signum(difx); i != move.getMoveTox() ; i = i - Integer.signum(difx)) {
                if(space.getSpace(i,this.y).isEmpty()){
                    pathblocked = false;
                }
                else{
                    return true;
                }
            }
            if(Math.abs(difx) == 1){
                if(wasEmpty()){
                    pathblocked = false;
                }
            }
        }
        else if(difx == 0){
            for (int i = move.getMoveFromy() - Integer.signum(dify); i != move.getMoveToy() ; i = i - Integer.signum(dify)) {
                if(space.getSpace(x,i).isEmpty()){
                    pathblocked = false;
                }
                else{
                    return true;
                }
            }
            if(wasEmpty()){
                pathblocked = false;
            }
        }
        if(wasSameColor()){
            pathblocked = true;
        }
        if((Math.abs(difx)<=1&&Math.abs(dify)<=1) && !wasSameColor()){
            pathblocked = false;
        }
        return pathblocked;
    }
}
