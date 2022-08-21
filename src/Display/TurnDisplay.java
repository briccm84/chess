package Display;

import chess.TeamColor;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class TurnDisplay  extends Text {
    Pane parent;
    public TurnDisplay(Pane parent){
        super();
        this.parent = parent;
        String text = "White's turn";
        this.setText(text);
        this.parent.getChildren().add(this);
    }

    public void changeTurn(TeamColor color){
        String text;
        if( color == TeamColor.WHITE){
            text = "White's turn";
        }
        else if(color == TeamColor.BLACK){
            text = "Black's turn";
        }
        else{
            text = "None's turn";
        }
        this.setText(text);
    }
}
