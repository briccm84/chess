package chess.pieces;

import chess.Piece;
import chess.Pieces;
import chess.TeamColor;

public class Pawn extends Piece{
    public Pawn(Pieces type, TeamColor color) {
        super(type, color);
    }
    @Override
    public boolean possibleMove(Move move){
        this.move = move;
        int dify = move.getMoveFromy() - move.getMoveToy();
        int difx = move.getMoveFromx() - move.getMoveTox();
        int direction;
        if(getColor() == TeamColor.WHITE){
            direction = -1;
        }
        else{
            direction = 1;
        }
        //standard
        if(dify == direction && difx == 0 && wasEmpty()){
            if(validMove(move)){
                //promote

                if(getColor() == TeamColor.WHITE && move.getMoveToy() == 7){
                    promoteDisplay();
                }
                else if(getColor() == TeamColor.BLACK && move.getMoveToy() == 0){
                    promoteDisplay();
                }
                return true;
            }
            else{
                return false;
            }
        }
        //attack
        else if(dify == direction && Math.abs(difx) == 1 && !wasEmpty()){
            return validMove(move);
        }
        //two start white
        else if(dify == 2 * direction && difx == 0 && getColor() == TeamColor.WHITE && move.getMoveFromy() == 1 ){
            return validMove(move);
        }
        //two start black
        else if(dify == 2 * direction && difx == 0 && getColor() == TeamColor.BLACK && move.getMoveFromy() == 6 ){
            return validMove(move);
        }
        return false;
    }
    @Override
    public boolean pathBlocked(Move move){
        int dify = move.getMoveFromy() - move.getMoveToy();
        int difx = move.getMoveFromx() - move.getMoveTox();
        int direction;
        if(getColor() == TeamColor.WHITE){
            direction = -1;
        }
        else{
            direction = 1;
        }
        //standard
        if(dify == direction && difx == 0 && wasEmpty()){
            return !wasEmpty();
        }
        //attack
        else if(dify == direction && Math.abs(difx) == 1 && !wasEmpty()){
            return move.getCaptured().getColor() == getColor();
        }
        //two start white
        else if(dify == 2 * direction && getColor() == TeamColor.WHITE && move.getMoveFromy() == 1  ){
            boolean pathBlocked = true;
            for (int i = 2; i < 3; i++) {
                if(space.getSpace(move.getMoveTox(),i).isEmpty()){
                    pathBlocked = false;
                }
                else{
                    pathBlocked = true;
                    break;
                }
            }
            return pathBlocked||!wasEmpty();
        }
        //two start black
        else if(dify == 2 * direction && getColor() == TeamColor.BLACK && move.getMoveFromy() == 6 ){
            boolean pathBlocked = true;
            for (int i = 5; i > 4; i--) {
                if(space.getSpace(move.getMoveTox(),i).isEmpty()){
                    pathBlocked = false;
                }
                else{
                    pathBlocked = true;
                    break;
                }
            }
            return pathBlocked||!wasEmpty();
        }

        return super.pathBlocked(move);
    }
    public void promote(Pieces type){
        Piece piece;
        switch(type){
            case ROOK:
                piece = new Rook(type,getColor());
                break;
            case KNIGHT:
                piece = new Knight(type,getColor());
                break;
            case BISHOP:
                piece = new Bishop(type,getColor());
                break;
            case QUEEN:
                piece = new Queen(type,getColor());
                break;
            default:
                piece = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        this.space.setPiece(piece);

    }
    public void promoteDisplay(){
        Pieces type = this.space.getSpaceDisplay().getBoardDisplay().getPromotionChoice();
        promote(type);
    }
}
