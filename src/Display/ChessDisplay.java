package Display;

import chess.Board;
import chess.Piece;
import chess.TeamColor;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class ChessDisplay extends Application {
    Stage s;
    CapturedPiecesDisplay whiteCapturedDisplay, blackCapturedDisplay;
    @Override
    public void start(Stage stage) {
        BorderPane borderPane = new BorderPane();
        BoardDisplay boardDisplay = new BoardDisplay();
        Board b = new Board();
        boardDisplay.createUI(borderPane, b,this);
        this.whiteCapturedDisplay = new CapturedPiecesDisplay(TeamColor.WHITE,borderPane);
        this.blackCapturedDisplay = new CapturedPiecesDisplay(TeamColor.BLACK,borderPane);
        Scene scene = new Scene(borderPane,600,600);
        s = stage;
        s.setTitle("Chess");
        s.setScene(scene);
        s.show();
    }
    public void captured(Piece piece){
        if(piece.isWhite()){
            whiteCapturedDisplay.addPiece(piece);
        }
        else{
            blackCapturedDisplay.addPiece(piece);
        }
    }

    public void startDisplay(){
        launch();
    }

    public static void main(String[] args) {
        launch();
    }
}
