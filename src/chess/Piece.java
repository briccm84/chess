package chess;

import chess.pieces.Move;
public abstract class Piece {
    public Space space;
    public int x,y;
    Pieces type;
    TeamColor color;
    protected Move move;
    public Piece(Pieces type,TeamColor color){
        this.type = type;
        this.color = color;
    }
    public boolean isInCheck(){
        if(space.getBoard().isWhitesTurn()){
            return space.getBoard().getWhiteKing().isInCheck();
        }
        return space.getBoard().getBlackKing().isInCheck();
    }
    public boolean validMove(Move move){return !isInCheck() && !space.getSpace(move.getMoveTox(),move.getMoveToy()).getPiece().pathBlocked(move);}
    public abstract boolean possibleMove(Move move);
    public boolean pathBlocked(Move move){return true;}
    public void setSpace(Space space){this.space = space;}
    public String getFileName(){
        String fileName;
        if (color == TeamColor.WHITE) {
            fileName = switch (type) {
                case PAWN -> "images/whitePawn.png";
                case ROOK -> "images/whiteRook.png";
                case KNIGHT -> "images/whiteKnight.png";
                case BISHOP -> "images/whiteBishop.png";
                case QUEEN -> "images/whiteQueen.png";
                case KING -> "images/whiteKing.png";
                case EMPTY -> "space is empty";
                default -> "no piece";
            };
        }
        else if(color == TeamColor.BLACK) {
            fileName = switch (type) {
                case PAWN -> "images/blackPawn.png";
                case ROOK -> "images/blackRook.png";
                case KNIGHT -> "images/blackKnight.png";
                case BISHOP -> "images/blackBishop.png";
                case QUEEN -> "images/blackQueen.png";
                case KING -> "images/blackKing.png";
                case EMPTY -> "space is empty";
                default -> "no piece";
            };
        }
        else {
            fileName = "space is empty";
        }
        return fileName;
    }
    public void setPosition(int x,int y){
        this.x = x;
        this.y = y;
    }
    public Pieces getType(){return this.type;}
    public TeamColor getColor(){return this.color;}
    public boolean isWhite(){return this.color == TeamColor.WHITE;}
    public boolean wasEmpty(){return this.move.getCaptured().getType() == Pieces.EMPTY;}
    public boolean wasSameColor(){return this.move.getCaptured().getColor() == getColor();}
    public String toString(){return getColor()+" "+getType();}
    public int gety() {return y;}
    public int getx() {return x;}
}
