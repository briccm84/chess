package chess.pieces;

import chess.Piece;
import chess.Pieces;
import chess.TeamColor;

import java.util.ArrayList;

public class King extends Piece {
    public King(Pieces type, TeamColor color) {super(type, color);}
    public boolean checkmate(){
        if(isInCheck()) {
            //System.out.println("check");
            ArrayList<ArrayList<int[]>> paths = new ArrayList<>();
            ArrayList<Piece> threats = getCheckmateThreats();
            ArrayList<int[]> threatPath = new ArrayList<>();
            for (Piece threat : threats) {
                //System.out.println(threat.toString());
                int[] position = {threat.getx(), threat.gety()};
                threatPath.add(position);
                int dify = threat.gety() - this.gety();
                int difx = threat.getx() - this.getx();
                //System.out.println("difx: "+difx+"   dify: "+dify);
                //bishop attack
                if (Math.abs(dify) == Math.abs(difx)) {
                    int j = threat.gety() - Integer.signum(dify);
                    for (int i = threat.getx() - Integer.signum(difx); i != this.getx(); i = i - Integer.signum(difx)) {
                        threatPath.add(new int[]{i, j});
                        j = j - Integer.signum(dify);
                    }
                }
                //rook attack
                if (dify == 0) {
                    for (int i = threat.getx() - Integer.signum(difx); i != this.getx(); i = i - Integer.signum(difx)) {
                        threatPath.add(new int[]{i, threat.gety()});
                    }
                } else if (difx == 0) {
                    for (int i = threat.gety() - Integer.signum(dify); i != this.gety(); i = i - Integer.signum(dify)) {
                        threatPath.add(new int[]{threat.getx(), i});
                    }
                }
                paths.add((ArrayList<int[]>) threatPath.clone());
                threatPath.clear();
            }
            //knight attack
            Piece knightThreat = getKnightCheckmateThreat();
            if (knightThreat.getType() == Pieces.KNIGHT) {
                //System.out.println("knight checkmate?");
                ArrayList<int[]> knightPath = new ArrayList<>();
                knightPath.add(new int[]{knightThreat.getx(), knightThreat.gety()});
                paths.add(knightPath);
            }
            //pawn attack
            Piece pawnThreat = getPawnCheckmateThreat();
            if (pawnThreat.getType() == Pieces.PAWN) {
                //System.out.println("pawn checkmate?");
                ArrayList<int[]> pawnPath = new ArrayList<>();
                pawnPath.add(new int[]{pawnThreat.getx(), pawnThreat.gety()});
                paths.add(pawnPath);
            }
            ArrayList<Piece> teamPieces;
            if (getColor() == TeamColor.WHITE) {
                teamPieces = this.space.getBoard().getWhiteAlive();
            } else {
                teamPieces = this.space.getBoard().getBlackAlive();
            }
            //blocking check
            for (ArrayList<int[]> tpath : paths) {
                for (int[] ints : tpath) {
                    //System.out.print(ints[0] + "," + ints[1] + "   ");
                    for (Piece piece : teamPieces) {
                        Move block = new Move(piece.getx(), piece.gety(), ints[0], ints[1], this.space.getBoard());
                        //System.out.println(piece.getx()+", "+piece.gety()+ "   moveto: "+ints[0]+","+ints[1]);
                        if (this.space.getBoard().move(block)) {
                            //System.out.println(block.getCaptured().getType()+" "+block.getCaptured().getColor());
                            this.space.getBoard().unMove(block);
                            return false;
                        }
                    }
                }
                //moving king out of check
            /*int[] kingMoves = new int[] {-1,0,1};
            for(int x: kingMoves){
                for(int y: kingMoves){
                    Move kingMove = new Move(getx(),gety(),getx()+x,gety()+y,this.space.getBoard());
                    if (this.space.getBoard().move(kingMove)) {
                        return false;
                    }
                }
            }*/
                //System.out.println();
            }
        }
        else{
            return  false;
        }
        return true;
    }
    @Override
    public boolean possibleMove(Move move){
        this.move = move;
        int dify = move.getMoveFromy() - move.getMoveToy();
        int difx = move.getMoveFromx() - move.getMoveTox();
        if(Math.abs(difx) <= 1 && Math.abs(dify) <= 1){
            return validMove(move);
        }
        //castling
        else if(!isInCheck()) {
            if (getColor() == TeamColor.WHITE && move.getMoveFromx() == 3 && move.getMoveFromy() == 0 && dify == 0 && move.getMoveTox() == 1 && this.space.getSpace(0, 0).getPiece().getType() == Pieces.ROOK) {
                if (validMove(move)) {
                    Piece temp = this.space.getBoard().board[0][0].getPiece();
                    this.space.getBoard().board[0][0].removePiece();
                    this.space.getBoard().board[2][0].addPiece(temp);
                    this.space.getSpace(2, 0).getSpaceDisplay().showPiece();
                    this.space.getSpace(0, 0).getSpaceDisplay().removePiece();
                    return true;
                }
            } else if (getColor() == TeamColor.WHITE && move.getMoveFromx() == 3 && move.getMoveFromy() == 0 && dify == 0 && move.getMoveTox() == 5 && this.space.getSpace(7, 0).getPiece().getType() == Pieces.ROOK) {
                if (validMove(move)) {
                    Piece temp = this.space.getBoard().board[7][0].getPiece();
                    this.space.getBoard().board[7][0].removePiece();
                    this.space.getBoard().board[4][0].addPiece(temp);
                    this.space.getSpace(4, 0).getSpaceDisplay().showPiece();
                    this.space.getSpace(7, 0).getSpaceDisplay().removePiece();
                    return true;
                }
            } else if (getColor() == TeamColor.BLACK && move.getMoveFromx() == 3 && move.getMoveFromy() == 7 && dify == 0 && move.getMoveTox() == 1 && this.space.getSpace(0, 7).getPiece().getType() == Pieces.ROOK) {
                if (validMove(move)) {
                    Piece temp = this.space.getBoard().board[0][7].getPiece();
                    this.space.getBoard().board[0][7].removePiece();
                    this.space.getBoard().board[2][7].addPiece(temp);
                    this.space.getSpace(2, 7).getSpaceDisplay().showPiece();
                    this.space.getSpace(0, 7).getSpaceDisplay().removePiece();
                    return true;
                }
            } else if (getColor() == TeamColor.BLACK && move.getMoveFromx() == 3 && move.getMoveFromy() == 7 && dify == 0 && move.getMoveTox() == 5 && this.space.getSpace(7, 7).getPiece().getType() == Pieces.ROOK) {
                if (validMove(move)) {
                    Piece temp = this.space.getBoard().board[7][7].getPiece();
                    this.space.getBoard().board[7][7].removePiece();
                    this.space.getBoard().board[4][7].addPiece(temp);
                    this.space.getSpace(4, 7).getSpaceDisplay().showPiece();
                    this.space.getSpace(7, 7).getSpaceDisplay().removePiece();
                    return true;
                }
            }
        }
        return  false;
    }
    //pathBlocked or is in check does not account for moving through check when castling
    @Override
    public boolean pathBlocked(Move move) {
        this.move = move;
        int dify = move.getMoveFromy() - move.getMoveToy();
        int difx = move.getMoveFromx() - move.getMoveTox();

        if(Math.abs(difx) <= 1 && Math.abs(dify) <= 1){
            return wasSameColor();
        }
        //castling
        else if(getColor() == TeamColor.WHITE && move.getMoveFromx() == 3 && move.getMoveFromy() == 0 && dify == 0 && move.getMoveTox() == 1 && this.space.getSpace(0,0).getPiece().getType() == Pieces.ROOK ){
            return wasSameColor() || !this.space.getSpace(2,0).isEmpty()||getKnightThreats(2,0)||!checkThreats(2,0).isEmpty();
        }
        else if(getColor() == TeamColor.WHITE && move.getMoveFromx() == 3 && move.getMoveFromy() == 0 && dify == 0 && move.getMoveTox() == 5 && this.space.getSpace(7,0).getPiece().getType() == Pieces.ROOK ){
            return wasSameColor() || !this.space.getSpace(4,0).isEmpty()|| !this.space.getSpace(6,0).isEmpty()||getKnightThreats(4,0)||!checkThreats(4,0).isEmpty();
        }
        else if(getColor() == TeamColor.BLACK && move.getMoveFromx() == 3 && move.getMoveFromy() == 7 && dify == 0 && move.getMoveTox() == 1 && this.space.getSpace(0,7).getPiece().getType() == Pieces.ROOK ){
            //System.out.println("samecolor: "+ wasSameColor()+"  2,7.isempty(): "+this.space.getSpace(2,7).isEmpty());
            return wasSameColor() || !this.space.getSpace(2,7).isEmpty()||getKnightThreats(2,7)||!checkThreats(2,7).isEmpty();
        }
        else if(getColor() == TeamColor.BLACK && move.getMoveFromx() == 3 && move.getMoveFromy() == 7 && dify == 0 && move.getMoveTox() == 5 && this.space.getSpace(7,7).getPiece().getType() == Pieces.ROOK ){
            return wasSameColor() || !this.space.getSpace(4,7).isEmpty()|| !this.space.getSpace(6,7).isEmpty()||getKnightThreats(4,7)||!checkThreats(4,7).isEmpty();
        }
        System.out.println("something went wrong with king movement");
        return  true;
    }
    @Override
    public boolean isInCheck(){
        ArrayList<Piece> threats = getKingThreats();
        return rookCheck(threats)||knightCheck()||bishopCheck(threats)||queenCheck(threats)||kingCheck()||pawnCheck() ;
    }
    public boolean rookCheck(ArrayList<Piece> threats){
        boolean u,r,d,l;
        u = threats.get(0).getType() == Pieces.ROOK && threats.get(0).getColor() != this.getColor();
        r = threats.get(2).getType() == Pieces.ROOK && threats.get(2).getColor() != this.getColor();
        d = threats.get(4).getType() == Pieces.ROOK && threats.get(4).getColor() != this.getColor();
        l = threats.get(6).getType() == Pieces.ROOK && threats.get(6).getColor() != this.getColor();
        return u||r||d||l;
    }
    public boolean knightCheck(){
        Piece[] threats =  new Piece[8];
        int index = 0;
        try{
            threats[index] = this.space.getSpace(this.x+1,this.y-2).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(this.x+2,this.y-1).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(this.x+2,this.y+1).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(this.x+1,this.y+2).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(this.x-1,this.y+2).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(this.x-2,this.y+1).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(this.x-2,this.y-1).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(this.x-1,this.y-2).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        boolean check = false;
        for (Piece p:threats) {
            //System.out.println(p.getColor()+"; "+p.getType());
            if(p.getType() == Pieces.KNIGHT && p.getColor() != getColor()){
                check = true;
                break;
            }
        }
       return check;
    }
    public boolean bishopCheck(ArrayList<Piece> threats){
        boolean ur,dr,dl,ul;
        ur = threats.get(1).getType() == Pieces.BISHOP && threats.get(1).getColor() != this.getColor();
        dr = threats.get(3).getType() == Pieces.BISHOP && threats.get(3).getColor() != this.getColor();
        dl = threats.get(5).getType() == Pieces.BISHOP && threats.get(5).getColor() != this.getColor();
        ul = threats.get(7).getType() == Pieces.BISHOP && threats.get(7).getColor() != this.getColor();
        return ur || dr || dl || ul;
    }
    public boolean queenCheck(ArrayList<Piece> threats){
        for(Piece p :threats){
            if(p.getType() == Pieces.QUEEN && p.getColor() != getColor()){
                return true;
            }
        }
        return false;
    }
    public boolean kingCheck(){
       int difx = this.space.getBoard().getBlackKing().x - this.space.getBoard().getWhiteKing().x;
       difx =  Math.abs(difx);
       int dify = this.space.getBoard().getBlackKing().y - this.space.getBoard().getWhiteKing().y;
       dify = Math.abs(dify);
       if(difx <=1 && dify <=1){
           return true;
       }
       return false;
    }
    public boolean pawnCheck(){
        int direction;
        if(getColor() == TeamColor.WHITE){
            direction = 1;
        }
        else{
            direction = -1;
        }
        Piece[] threats = new Piece[2];
        try {
            threats[0] = this.space.getSpace(this.x - 1, this.y + direction).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[0] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        try{
            threats[1] = this.space.getSpace(this.x +1,this.y+direction).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[1] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        for(Piece p: threats){
            if(p.getType()==Pieces.PAWN && p.getColor() != getColor()){
                return true;
            }
        }
        return false;
    }
    public Piece getOpposingKing(){
        if(getColor() == TeamColor.WHITE){
            return this.space.getBoard().getBlackKing();
        }
        else{
            return this.space.getBoard().getWhiteKing();
        }
    }

    //starts up then clockwise [up,ur,r,dr,d,dl,l,ul]
    public ArrayList<Piece> getKingThreats(){
            //starts at up then clockwise
            ArrayList<Piece> piecesthreat = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            piecesthreat.add(new Empty(Pieces.EMPTY,TeamColor.NONE));
        }
        //up piece
            for (int i = y-1; i >= 0; i--) {
                if(this.space.getSpace(this.x,i).isEmpty()){
                    continue;
                }
                piecesthreat.set(0,this.space.getSpace(this.x,i).getPiece());
                break;
            }
        //up right
            for (int i = 1; x+i<8 && y-i>=0 ; i++) {
                if(this.space.getSpace(this.x+i,this.y-i).isEmpty()){
                    continue;
                }
                piecesthreat.set(1,this.space.getSpace(this.x+i,this.y-i).getPiece());
                break;
            }
        //right piece
            for (int i = x+1; i < 8; i++) {
                if(this.space.getSpace(i,this.y).isEmpty()){
                    continue;
                }
                piecesthreat.set(2,this.space.getSpace(i,this.y).getPiece());
                break;
            }
        //down right
            for (int i = 1; x+i<8 && i+y<8 ; i++) {
                if(this.space.getSpace(this.x+i,this.y+i).isEmpty()){
                    continue;
                }
                piecesthreat.set(3,this.space.getSpace(this.x+i,this.y+i).getPiece());
                break;
            }
        //down piece
            for (int i = y+1; i < 8; i++) {
                if(this.space.getSpace(this.x,i).isEmpty()){
                    continue;
                }
                piecesthreat.set(4,this.space.getSpace(this.x,i).getPiece());
                break;
            }
        //down left
            for (int i = 1; x-i>=0 && y+i<8 ; i++) {
                if(this.space.getSpace(this.x-i,this.y+i).isEmpty()){
                    continue;
                }
                piecesthreat.set(5,this.space.getSpace(x-i,this.y+i).getPiece());
                break;
            }
        //left piece
            for (int i = x-1; i >= 0; i--) {
                if(this.space.getSpace(i,this.y).isEmpty()){
                    continue;
                }
                piecesthreat.set(6,this.space.getSpace(i,this.y).getPiece());
                break;
            }
            //up left
            for (int i = 1; x-i>=0 && y-i>=0 ; i++) {
                //System.out.println((x-i)+","+(y-i));
                if(this.space.getSpace(this.x-i,this.y-i).isEmpty()){
                    continue;
                }
                piecesthreat.set(7,this.space.getSpace(this.x-i,this.y-i).getPiece());
                break;
            }
            return piecesthreat;
        }
    //does not get pawn or knight threats
    public ArrayList<Piece> getCheckmateThreats(){
        ArrayList<Piece> threatMates = new ArrayList<>(2);
        ArrayList<Piece> threats = getKingThreats();
        int index = 0;
        if(threats.get(0).getType() == Pieces.QUEEN && threats.get(0).getColor() != getColor() || threats.get(0).getType() == Pieces.ROOK && threats.get(0).getColor() != getColor()){
            threatMates.add(threats.get(0));
        }
        else if(threats.get(1).getType() == Pieces.QUEEN && threats.get(1).getColor() != getColor() || threats.get(1).getType() == Pieces.BISHOP && threats.get(1).getColor() != getColor()){
            threatMates.add(threats.get(1));
        }
        else if(threats.get(2).getType() == Pieces.QUEEN && threats.get(2).getColor() != getColor() || threats.get(2).getType() == Pieces.ROOK && threats.get(2).getColor() != getColor()){
            threatMates.add(threats.get(2));
        }
        else if(threats.get(3).getType() == Pieces.QUEEN && threats.get(3).getColor() != getColor() || threats.get(3).getType() == Pieces.BISHOP && threats.get(3).getColor() != getColor()){
            threatMates.add(threats.get(3));
        }
        else if(threats.get(4).getType() == Pieces.QUEEN && threats.get(4).getColor() != getColor() || threats.get(4).getType() == Pieces.ROOK && threats.get(4).getColor() != getColor()){
            threatMates.add(threats.get(4));
        }
        else if(threats.get(5).getType() == Pieces.QUEEN && threats.get(5).getColor() != getColor() || threats.get(5).getType() == Pieces.BISHOP && threats.get(5).getColor() != getColor()){
            threatMates.add(threats.get(5));
        }
        else if(threats.get(6).getType() == Pieces.QUEEN && threats.get(6).getColor() != getColor() || threats.get(6).getType() == Pieces.ROOK && threats.get(6).getColor() != getColor()){
            threatMates.add(threats.get(6));
        }
        else if(threats.get(7).getType() == Pieces.QUEEN && threats.get(7).getColor() != getColor() || threats.get(7).getType() == Pieces.BISHOP && threats.get(7).getColor() != getColor()){
            threatMates.add(threats.get(7));
        }
        return threatMates;
    }
    public Piece getKnightCheckmateThreat(){
        Piece[] threats =  new Piece[8];
        int index = 0;
        try{
            threats[index] = this.space.getSpace(this.x+1,this.y-2).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(this.x+2,this.y-1).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(this.x+2,this.y+1).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(this.x+1,this.y+2).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(this.x-1,this.y+2).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(this.x-2,this.y+1).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(this.x-2,this.y-1).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(this.x-1,this.y-2).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        Piece threat = new Empty(Pieces.EMPTY,TeamColor.NONE);
        for (Piece p:threats) {
            if(p.getType() == Pieces.KNIGHT && p.getColor() != getColor()){
                threat = p;
                break;
            }
        }
        return threat;
    }
    public Piece getPawnCheckmateThreat(){
        if(getColor() == TeamColor.WHITE){
            if(this.space.getSpace(this.x-1,this.y+1).getPiece().getType() == Pieces.PAWN &&this.space.getSpace(this.x-1,this.y+1).getPiece().getColor() == TeamColor.BLACK){
                return this.space.getSpace(this.x-1,this.y+1).getPiece();
            }
            if(this.space.getSpace(this.x+1,this.y+1).getPiece().getType() == Pieces.PAWN &&this.space.getSpace(this.x+1,this.y+1).getPiece().getColor() == TeamColor.BLACK){
                return this.space.getSpace(this.x+1,this.y+1).getPiece();
            }
        }
        else if(getColor() == TeamColor.BLACK){
            if(this.space.getSpace(this.x-1,this.y-1).getPiece().getType() == Pieces.PAWN &&this.space.getSpace(this.x-1,this.y-1).getPiece().getColor() == TeamColor.WHITE){
                return this.space.getSpace(this.x-1,this.y-1).getPiece();
            }
            if(this.space.getSpace(this.x+1,this.y-1).getPiece().getType() == Pieces.PAWN &&this.space.getSpace(this.x+1,this.y-1).getPiece().getColor() == TeamColor.WHITE){
                return this.space.getSpace(this.x+1,this.y-1).getPiece();
            }
        }
        return new Empty(Pieces.EMPTY,TeamColor.NONE);
    }
    public ArrayList<Piece> getThreats(int x,int y){
        //starts at up then clockwise
        ArrayList<Piece> piecesthreat = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            piecesthreat.add(new Empty(Pieces.EMPTY,TeamColor.NONE));
        }
        //up piece
        for (int i = y-1; i >= 0; i--) {
            if(this.space.getSpace(x,i).isEmpty()){
                continue;
            }
            piecesthreat.set(0,this.space.getSpace(x,i).getPiece());
            break;
        }
        //up right
        for (int i = 1; x+i<8 && y-i>=0 ; i++) {
            if(this.space.getSpace(x+i,y-i).isEmpty()){
                continue;
            }
            piecesthreat.set(1,this.space.getSpace(x+i,y-i).getPiece());
            break;
        }
        //right piece
        for (int i = x+1; i < 8; i++) {
            if(this.space.getSpace(i,y).isEmpty()){
                continue;
            }
            piecesthreat.set(2,this.space.getSpace(i,y).getPiece());
            break;
        }
        //down right
        for (int i = 1; x+i<8 && i+y<8 ; i++) {
            if(this.space.getSpace(x+i,y+i).isEmpty()){
                continue;
            }
            piecesthreat.set(3,this.space.getSpace(x+i,y+i).getPiece());
            break;
        }
        //down piece
        for (int i = y+1; i < 8; i++) {
            if(this.space.getSpace(x,i).isEmpty()){
                continue;
            }
            piecesthreat.set(4,this.space.getSpace(x,i).getPiece());
            break;
        }
        //down left
        for (int i = 1; x-i>=0 && y+i<8 ; i++) {
            if(this.space.getSpace(x-i,y+i).isEmpty()){
                continue;
            }
            piecesthreat.set(5,this.space.getSpace(x-i,y+i).getPiece());
            break;
        }
        //left piece
        for (int i = x-1; i >= 0; i--) {
            if(this.space.getSpace(i,y).isEmpty()){
                continue;
            }
            piecesthreat.set(6,this.space.getSpace(i,y).getPiece());
            break;
        }
        //up left
        for (int i = 1; x-i>=0 && y-i>=0 ; i++) {
            //System.out.println((x-i)+","+(y-i));
            if(this.space.getSpace(x-i,y-i).isEmpty()){
                continue;
            }
            piecesthreat.set(7,this.space.getSpace(x-i,y-i).getPiece());
            break;
        }
        return piecesthreat;
    }
    public boolean getKnightThreats(int x,int y){
        Piece[] threats =  new Piece[8];
        int index = 0;
        try{
            threats[index] = this.space.getSpace(x+1,y-2).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(x+2,y-1).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(x+2,y+1).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(x+1,y+2).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(x-1,y+2).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(x-2,y+1).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(x-2,y-1).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        index++;
        try{
            threats[index] = this.space.getSpace(x-1,y-2).getPiece();
        }
        catch(ArrayIndexOutOfBoundsException outOfBoundsException){
            threats[index] = new Empty(Pieces.EMPTY,TeamColor.NONE);
        }
        boolean check = false;
        for (Piece p:threats) {
            if(p.getType() == Pieces.KNIGHT && p.getColor() != getColor()){
                //System.out.println(p.getColor()+" "+p.getType()+ ": "+p.getx()+","+p.gety());
                check = true;
                break;
            }
        }
        return check;
    }
    public ArrayList<Piece> checkThreats(int x, int y){
        ArrayList<Piece> threatMates = new ArrayList<>();
        ArrayList<Piece> threats = getThreats(x,y);
        if(threats.get(0).getType() == Pieces.QUEEN && threats.get(0).getColor() != getColor() || threats.get(0).getType() == Pieces.ROOK && threats.get(0).getColor() != getColor()){
            threatMates.add(threats.get(0));
        }
        if(threats.get(1).getType() == Pieces.QUEEN && threats.get(1).getColor() != getColor() || threats.get(1).getType() == Pieces.BISHOP && threats.get(1).getColor() != getColor()){
            threatMates.add(threats.get(1));
        }
        if(threats.get(2).getType() == Pieces.QUEEN && threats.get(2).getColor() != getColor() || threats.get(2).getType() == Pieces.ROOK && threats.get(2).getColor() != getColor()){
            threatMates.add(threats.get(2));
        }
        if(threats.get(3).getType() == Pieces.QUEEN && threats.get(3).getColor() != getColor() || threats.get(3).getType() == Pieces.BISHOP && threats.get(3).getColor() != getColor()){
            threatMates.add(threats.get(3));
        }
        if(threats.get(4).getType() == Pieces.QUEEN && threats.get(4).getColor() != getColor() || threats.get(4).getType() == Pieces.ROOK && threats.get(4).getColor() != getColor()){
            threatMates.add(threats.get(4));
        }
        if(threats.get(5).getType() == Pieces.QUEEN && threats.get(5).getColor() != getColor() || threats.get(5).getType() == Pieces.BISHOP && threats.get(5).getColor() != getColor()){
            threatMates.add(threats.get(5));
        }
        if(threats.get(6).getType() == Pieces.QUEEN && threats.get(6).getColor() != getColor() || threats.get(6).getType() == Pieces.ROOK && threats.get(6).getColor() != getColor()){
            threatMates.add(threats.get(6));
        }
        if(threats.get(7).getType() == Pieces.QUEEN && threats.get(7).getColor() != getColor() || threats.get(7).getType() == Pieces.BISHOP && threats.get(7).getColor() != getColor()){
            threatMates.add(threats.get(7));
        }
        /*for(Piece p:threatMates){
            System.out.println(p.getColor()+" "+p.getType()+ ": "+p.getx()+","+p.gety());
        }*/
        return threatMates;
    }

    }

