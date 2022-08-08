package Display;

import chess.Board;
import chess.Piece;
import chess.Space;
import chess.TeamColor;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class BoardDisplay extends GridPane {
    Pane parent;
    ChessDisplay chessDisplay;
    SpaceDisplay[][] boardDisplay = new SpaceDisplay[8][8];
    Board board;
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
