package chess;

import Display.SpaceDisplay;
import chess.pieces.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    public Space[][] board;
    public ArrayList<Piece> whiteAlive,whiteDead,blackAlive,blackDead;
    boolean whitesTurn;
    TeamColor turn;
    Move move;
    King whiteKing,blackKing;
    public Board(){
        whitesTurn = true;
        this.turn = TeamColor.WHITE;
        this.board = new Space[8][8];
        whiteAlive = new ArrayList<>();
        whiteDead = new ArrayList<>();
        blackAlive = new ArrayList<>();
        blackDead = new ArrayList<>();
        TeamColor[] colors = {TeamColor.BLACK,TeamColor.WHITE};
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Space(this,i,j,colors[((i+j)%2)],new Empty(Pieces.EMPTY,TeamColor.NONE));
            }
        }
        setUpPieces();
        move = new Move(0,0,0,0, this);
    }

    public void setUpPieces(){
        //white back row
        Piece rook = new Rook(Pieces.ROOK,TeamColor.WHITE);
        Piece knight = new Knight(Pieces.KNIGHT,TeamColor.WHITE);
        Piece bishop = new Bishop(Pieces.BISHOP,TeamColor.WHITE);
        this.whiteKing = new King(Pieces.KING,TeamColor.WHITE);
        Piece queen = new Queen(Pieces.QUEEN,TeamColor.WHITE);
        Piece rook2 = new Rook(Pieces.ROOK,TeamColor.WHITE);
        Piece knight2 = new Knight(Pieces.KNIGHT,TeamColor.WHITE);
        Piece bishop2 = new Bishop(Pieces.BISHOP,TeamColor.WHITE);
        Piece[] backRow = {rook,knight,bishop,this.whiteKing,queen,bishop2,knight2,rook2};


        //puts king in first position
        whiteAlive.add(this.whiteKing);
        whiteAlive.addAll(Arrays.asList(backRow));
        whiteAlive.remove(5);

        for (int i = 0; i < 8; i++) {
            board[i][0].addPiece(backRow[i]);
        }
        //white pawns
        for (int i = 0; i < 8; i++) {
            Piece p = new Pawn(Pieces.PAWN,TeamColor.WHITE);
            board[i][1].addPiece(p);
            whiteAlive.add(p);
        }

        //black back row
        rook = new Rook(Pieces.ROOK,TeamColor.BLACK);
        knight = new Knight(Pieces.KNIGHT,TeamColor.BLACK);
        bishop = new Bishop(Pieces.BISHOP,TeamColor.BLACK);
        this.blackKing = new King(Pieces.KING,TeamColor.BLACK);
        queen = new Queen(Pieces.QUEEN,TeamColor.BLACK);
        rook2 = new Rook(Pieces.ROOK,TeamColor.BLACK);
        knight2 = new Knight(Pieces.KNIGHT,TeamColor.BLACK);
        bishop2 = new Bishop(Pieces.BISHOP,TeamColor.BLACK);
        Piece[] blackBackRow = {rook,knight,bishop,this.blackKing,queen,bishop2,knight2,rook2};
        //puts king in first position
        blackAlive.add(this.blackKing);
        blackAlive.addAll(Arrays.asList(backRow));
        blackAlive.remove(5);

        for (int i = 0; i < 8; i++) {
            board[i][7].addPiece(blackBackRow[i]);
        }
        //black pawns
        for (int i = 0; i < 8; i++) {
            Piece p = new Pawn(Pieces.PAWN,TeamColor.BLACK);
            board[i][6].addPiece(p);
            blackAlive.add(p);
        }
    }
    public void unMove(Move move){
        Piece temp = board[move.getMoveTox()][move.getMoveToy()].getPiece();
        board[move.getMoveTox()][move.getMoveToy()].removePiece();
        board[move.getMoveTox()][move.getMoveToy()].addPiece(move.getCaptured());
        board[move.getMoveFromx()][move.getMoveFromy()].addPiece(temp);
        if(move.getCaptured().getType() != Pieces.EMPTY){
            uncaptured(move.getCaptured());
        }
    }
    public boolean move(Move move){
        this.move = move;
        Piece temp1 = board[move.getMoveFromx()][move.getMoveFromy()].getPiece();
        board[move.getMoveFromx()][move.getMoveFromy()].removePiece();
        board[move.getMoveTox()][move.getMoveToy()].addPiece(temp1);
        if(temp1.possibleMove(move)) {
            changeTurn();
        }
        else{
            unMove(move);
            return false;
        }
        return true;
    }
    //move function for display only
    public String displayMove(Move move){
        this.move = move;
        Piece temp1 = board[move.getMoveFromx()][move.getMoveFromy()].getPiece();
        board[move.getMoveFromx()][move.getMoveFromy()].removePiece();
        board[move.getMoveTox()][move.getMoveToy()].addPiece(temp1);
        if(temp1.possibleMove(move)) {
            changeTurn();
        }
        else{
            unMove(move);
            return "false";
        }
        return "true";
    }
    public boolean isCheckMate(){
        if(!whitesTurn){
            //System.out.println("black checkmate?");
            if(getBlackKing().checkmate()){
                System.out.println("white wins");
                return true;
            }
        }
        else{
            //working on winners display
            //System.out.println("white checkmate?");
            if(getWhiteKing().checkmate()){
                System.out.println("black wins");
                return true;
            }
        }
        return false;
    }
    public void connectSpaceDisplays(SpaceDisplay[][] spaceDisplays){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j].setSpaceDisplay(spaceDisplays[i][j]);
            }

        }
    }
    public void captured(Piece piece){
        if(piece.getType() != Pieces.EMPTY) {
            if (piece.color == TeamColor.WHITE) {
                whiteDead.add(piece);
                whiteAlive.remove(piece);
            } else {
                blackDead.add(piece);
                blackAlive.remove(piece);
            }
        }
    }
    public void uncaptured(Piece piece){
        if(piece.color == TeamColor.WHITE){
            whiteAlive.add(piece);
            whiteDead.remove(piece);
        }
        else{
            blackAlive.add(piece);
            blackDead.remove(piece);
        }
    }
    public Space getSpace(int x,int y){return this.board[x][y];}
    public boolean isWhitesTurn(){return whitesTurn;}
    public King getWhiteKing(){return this.whiteKing;}
    public King getBlackKing(){return this.blackKing;}
    public ArrayList<Piece> getWhiteAlive() {return whiteAlive;}
    public ArrayList<Piece> getBlackAlive() {return blackAlive;}
    public void changeTurn(){whitesTurn = !whitesTurn;}
    public void setWhitesTurn(){whitesTurn = true;}
    public void setBlacksTurn(){whitesTurn = false;}
    public void printBoard(){
        for(Space[] spaces: board){
            String row = "";
            for(Space space: spaces){
                row += space.getPieceType()+",";
            }
            System.out.println(row);
        }
    }
    public void showBoard(){
        for(Space[] spaces: board){
            for(Space space: spaces){
                space.getSpaceDisplay().showPiece();
            }
        }
    }
    public void showSpace(int x,int y){board[x][y].getSpaceDisplay().showPiece();}
}
