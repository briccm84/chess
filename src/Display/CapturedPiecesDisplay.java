package Display;

import chess.Piece;
import chess.TeamColor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.util.ArrayList;

public class CapturedPiecesDisplay extends VBox {
    TeamColor teamColor;
    BorderPane borderPane;
    ArrayList<Piece> capturedPieces = new ArrayList<>();
    ArrayList<ImageView> capturedViews = new ArrayList<>();
    public CapturedPiecesDisplay(TeamColor teamColor, BorderPane borderPane){
        this.teamColor = teamColor;
        this.borderPane = borderPane;
        this.getChildren().add(new Text(teamColor.toString()));
        if(teamColor == TeamColor.WHITE) {
            borderPane.setLeft(this);
        }
        else{
            borderPane.setRight(this);
        }
    }
    public void addPiece(Piece piece){
        capturedPieces.add(piece);
        String fileName = piece.getFileName();
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            Image image = new Image(inputStream);
            ImageView imageView = new ImageView(image);
            capturedViews.add(imageView);
            this.getChildren().add(imageView);
        }
        catch (Exception e){
            System.out.println("something went wrong in capturedPiecesDisplay");
        }

    }
    public void removePiece(Piece piece){
        int index = capturedPieces.indexOf(piece);
        capturedPieces.remove(piece);
        capturedViews.remove(index);
    }
}
