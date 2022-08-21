package Display;

import chess.Piece;
import chess.Pieces;
import chess.Space;
import chess.TeamColor;
import chess.pieces.Move;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SpaceDisplay extends Rectangle {
    Rectangle rectangle;
    StackPane stackpane;
    ImageView imageView;
    Space space;
    BoardDisplay boardDisplay;
    int x,y;
    boolean selected = false;
    double spaceSize = 60;
    public SpaceDisplay(BoardDisplay boardDisplay,int x,int y,Space space){
        this.boardDisplay = boardDisplay;
        this.space = space;
        this.x = x;
        this.y = y;
        this.stackpane = new StackPane();
        if((x+y)%2 == 0) {
            rectangle = new Rectangle(spaceSize, spaceSize, Color.BEIGE);
        }
        else{
            rectangle = new Rectangle(spaceSize, spaceSize, Color.DARKBLUE);
        }
        stackpane.getChildren().add(rectangle);
        BorderStroke defaultBorderStroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2));
        Border defaultBorder = new Border(defaultBorderStroke);
        stackpane.setBorder(defaultBorder);
        boardDisplay.add(stackpane,x,y);

        EventHandler<MouseEvent> clickSpace = e -> {
            //boardDisplay.board.printBoard();
            if(boardDisplay.isSelected()) {
                int[] temp = boardDisplay.getSelectedSpace();
                Move move = new Move(temp[0], temp[1], x, y, this.space.getBoard());
                String moveString = boardDisplay.board.displayMove(move);
                    if(moveString=="true" || moveString == "promote"){
                        boardDisplay.removePieceForMove();
                        removePiece();
                        this.boardDisplay.captured(move.getCaptured());
                        showPiece();
                        if(boardDisplay.board.isCheckMate()){
                            String winner;
                            if(this.space.getBoard().isWhitesTurn()){
                                winner = TeamColor.BLACK.toString();
                            }
                            else{
                                winner = TeamColor.WHITE.toString();
                            }
                            boardDisplay.winner(winner);
                        }

                    }
                    else if(moveString == "false"){

                    }
                boardDisplay.unhighlight();
            }
            else{
                if((boardDisplay.board.isWhitesTurn() && boardDisplay.board.getSpace(x,y).piece.isWhite())){
                    boardDisplay.spaceHighlighted(x,y);
                    highlight();
                }
                if(!boardDisplay.board.isWhitesTurn() && !boardDisplay.board.getSpace(x,y).piece.isWhite()&&!boardDisplay.board.getSpace(x,y).isEmpty()){
                    boardDisplay.spaceHighlighted(x,y);
                    highlight();
                }
            }


        };
        stackpane.addEventFilter(MouseEvent.MOUSE_CLICKED, clickSpace);
    }

    public void showPiece() {
        try {
            String fileName = space.piece.getFileName();
            if (fileName.equals("no piece")) {
                System.out.println("spaceDisplay cannot find correct file");
                System.out.println(fileName);
            }
            else if (fileName.equals("space is empty")) {
            }
            else {
                FileInputStream inputStream = new FileInputStream(fileName);
                Image image = new Image(inputStream);
                imageView = new ImageView(image);
                stackpane.getChildren().add(imageView);
            }
        }
        catch (Exception e){
            System.out.println("Something went wrong with showpiece in SpaceDisplay");
        }
    }
    public void removePiece(){
        stackpane.getChildren().remove(imageView);
        //captured is getting piece that moved to space not what was at space.
    }
    public void unhighlight(){
        BorderStroke defaultBorderStroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2));
        Border defaultBorder = new Border(defaultBorderStroke);
        stackpane.setBorder(defaultBorder);
        selected = false;
    }
    public void highlight(){
        BorderStroke borderStroke = new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null, new BorderWidths(2));
        Border highlight = new Border(borderStroke);
        stackpane.setBorder(highlight);
    }
    public BoardDisplay getBoardDisplay(){return this.boardDisplay;}
}
