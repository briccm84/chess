package chess;

import Display.SpaceDisplay;
import chess.pieces.Empty;

public class Space {
    int x,y;
    Board board;
    TeamColor color;
    char letterPosition;
    public Piece piece;
    SpaceDisplay spaceDisplay;
    public Space(Board board,int x, int y,TeamColor color,Piece piece){
        this.board = board;
        this.x = x;
        this.y = y;
        this.color = color;
        this.piece = piece;
        this.piece.setPosition(x,y);
        this.letterPosition = (char)((int)'a'+x);
    }
    public void addPiece(Piece p){
            this.board.captured(this.piece);
        this.piece = p;
        this.piece.setSpace(this);
        this.piece.setPosition(x,y);
    }
    public void setSpaceDisplay(SpaceDisplay spaceDisplay){this.spaceDisplay = spaceDisplay;}
    public SpaceDisplay getSpaceDisplay() {return this.spaceDisplay;}
    public void removePiece(){
        this.piece = new Empty(Pieces.EMPTY,TeamColor.NONE);
    }

    public TeamColor getColor(){
        return color;
    }
    public Pieces getPieceType(){return piece.type;}
    public TeamColor getPieceColor(){return piece.color;}
    public Piece getPiece(){return piece;}
    public void setPiece(Piece piece){this.piece = piece;}
    public boolean isEmpty(){return this.piece.getType() == Pieces.EMPTY;}
    public Space getSpace(int x, int y){return board.getSpace(x,y);}

    public Board getBoard() {return board;}
}
