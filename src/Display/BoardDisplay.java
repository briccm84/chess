package Display;

import chess.*;
import chess.pieces.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Handler;


public class BoardDisplay extends GridPane {
    BorderPane parent;
    ChessDisplay chessDisplay;
    SpaceDisplay[][] boardDisplay = new SpaceDisplay[8][8];
    Board board;
    VBox promotionMessage;
    int[] highlightedPosition = {-1,-1};
    public void createUI(BorderPane parent, Board board,ChessDisplay chessDisplay) {
        this.parent = parent;
        this.board = board;
        this.chessDisplay = chessDisplay;
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0),new Insets(0))));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardDisplay[i][j] = new SpaceDisplay(this,i,j,board.getSpace(i,j));
                boardDisplay[i][j].showPiece();
            }
        }
        board.connectSpaceDisplays(boardDisplay);
        parent.setCenter(this);
    }
    public Pieces getPromotionChoice(Pawn pawn){
        AtomicReference<Pieces> type = new AtomicReference<>(Pieces.EMPTY);
        //System.out.println("promotion");
        this.promotionMessage = new VBox();
        this.promotionMessage.getChildren().add(new Text("choose piece to promote pawn"));
        Piece[] pieces = new Piece[]{new Rook(Pieces.ROOK,TeamColor.WHITE),new Knight(Pieces.KNIGHT,TeamColor.WHITE),new Bishop(Pieces.BISHOP,TeamColor.WHITE),new Queen(Pieces.QUEEN,TeamColor.WHITE)};
        HBox promotionOptions = new HBox();
        int index = 0;
        for(Piece piece: pieces) {
            String fileName = piece.getFileName();
            try {
                FileInputStream inputStream = new FileInputStream(fileName);
                Image image = new Image(inputStream);
                ImageView imageView = new ImageView(image);
                EventHandler<MouseEvent> clickedPiece = e -> {
                    pawn.promote(piece.getType());
                    pawn.space.getSpaceDisplay().removePiece();
                    pawn.space.getSpaceDisplay().showPiece();
                    removePromotionMessage();
                    if(pawn.isWhite()){
                        pawn.space.getBoard().setBlacksTurn();
                    }
                    else{
                        pawn.space.getBoard().setWhitesTurn();
                    }
                    //type.set(piece.getType());
                    //System.out.println("clickedPiece: "+type );
                };
                imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, clickedPiece);
                 promotionOptions.getChildren().add(imageView);

            } catch (Exception e) {
                System.out.println(e);
            }
            index++;
        }
        promotionMessage.getChildren().add(promotionOptions);
        this.parent.setBottom(promotionMessage);

        return type.get();
    }
    public void removePromotionMessage(){
        this.parent.getChildren().remove(this.promotionMessage);
    }

    public void spaceHighlighted(int x,int y){
        highlightedPosition[0] = x;
        highlightedPosition[1] = y;
    }
    public void unhighlight(){
        boardDisplay[highlightedPosition[0]][highlightedPosition[1]].unhighlight();
        highlightedPosition[0] = -1;
        highlightedPosition[1] = -1;
    }
    public void removePieceForMove(){boardDisplay[highlightedPosition[0]][highlightedPosition[1]].removePiece();}
    public void captured(Piece piece){
        this.chessDisplay.captured(piece);
    }
    public boolean isSelected(){return highlightedPosition[0] != -1;}
    public int[] getSelectedSpace(){return highlightedPosition;}
    public void winner(String teamColor){
        this.getChildren().add(new Text(teamColor));
    }
}
