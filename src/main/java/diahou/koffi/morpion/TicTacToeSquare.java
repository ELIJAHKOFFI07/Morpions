package diahou.koffi.morpion;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class TicTacToeSquare extends TextField {
    private static TicTacToeModel model = TicTacToeModel.getInstance();

    private ObjectProperty<Owner> ownerProperty = new SimpleObjectProperty<>(Owner.NONE);

     private BooleanProperty winnerProperty = new SimpleBooleanProperty(false);

    public  ObjectProperty<Owner> ownerProperty() {
        return ownerProperty;
    }

    public  BooleanProperty winnerProperty(){
        return winnerProperty;
    }

     public TicTacToeSquare(final int row, final int column) {

            // Définition des styles de base
                     String baseStyle = "-fx-max-width: 120px; -fx-max-height: 120px; -fx-alignment: center;";

            // Style de base pour les cases non gagnantes
                     String nonWinnerStyle = "-fx-font-size: 22px; " + baseStyle;

            // Style pour les cases gagnantes
                     String winnerStyle = "-fx-font-size: 32px; -fx-font-weight: bold; " + baseStyle;

            // Style pour les bordures
                     String borderStyle = "-fx-border-color: black; -fx-border-width: 1px; -fx-border-style: solid;";

            // Style de fond
                     String backgroundStyle = "-fx-background-color: white;";

            // Application des styles de base
                     this.setStyle(baseStyle);
                     this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null)));
                     this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                     this.setEditable(false);

            // Liaison du texte en fonction du propriétaire de la case
                     this.textProperty().bind(Bindings.createObjectBinding(() -> {
                         if (ownerProperty().get().equals(Owner.FIRST)) {
                             return "X";
                         } else if (ownerProperty().get().equals(Owner.SECOND)) {
                             return "O";
                         } else {
                             return "";
                         }
                     }, ownerProperty()));

            // Liaison du style en fonction du vainqueur
                     this.styleProperty().bind(
                             Bindings.when(winnerProperty())
                                     .then(winnerStyle + " " + borderStyle)
                                     .otherwise(nonWinnerStyle + " " + borderStyle)
                     );
     }
}
