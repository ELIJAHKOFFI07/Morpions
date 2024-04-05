package diahou.koffi.morpion;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class morpionController implements Initializable {

    @FXML
    private Label CASEL;

    @FXML
    private Label CASEO;

    @FXML
    private Label CASEX;

    @FXML
    private GridPane board;

    @FXML
    private Label gameOver;

    @FXML
    private Button restart;

    TicTacToeModel model = TicTacToeModel.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**
         *
         * Creer la grille de jeux
         *
         * **/
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                TicTacToeSquare square = new TicTacToeSquare(row,col);
                square.ownerProperty().bind(model.getSquare(row,col));
                square.winnerProperty().bind(model.getWinningSquare(row,col));

                board.add(square, row, col);
                Background originalBackground = square.getBackground();

                int finalRow = row;
                int finalCol = col;
                square.setOnMouseEntered(event -> {
                    if (model.legalMove(finalRow, finalCol).get()){
                        BackgroundFill backgroundFill = new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, null);
                        Background background = new Background(backgroundFill);
                        square.setBackground(background);
                    }else {
                        BackgroundFill backgroundFill = new BackgroundFill(Color.RED, CornerRadii.EMPTY, null);
                        Background background = new Background(backgroundFill);
                        square.setBackground(background);
                    }
                });
                square.setOnMouseExited(event -> {
                    BackgroundFill backgroundFill = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null);
                    Background background = new Background(backgroundFill);
                    square.setBackground(background);
                });
            }
        }
        /**
         *
         *
         * Jouer dans une case
         *
         * **/
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TicTacToeSquare square = findSquare(board,j,i);
                int finalI = i;
                int finalJ = j;
                square.setOnMouseClicked(event -> {
                    if (model.legalMove(finalJ, finalI).get()){
                        model.play(finalJ, finalI);
                    }
                    score();
                });
            }
        }
    }


    void score(){
        gameOver.textProperty().bind(model.getEndOfGameMessage());
        CASEX.textProperty().bind(Bindings.concat(model.getScore(Owner.FIRST).asString()," case pour X"));
        if (!model.gameOver().get()){
            CASEL.textProperty().bind(Bindings.concat(model.getScore(Owner.NONE).asString()," case libre"));
        }else {
            CASEL.textProperty().bind(Bindings.concat(""));
        }
        CASEO.textProperty().bind(Bindings.concat(model.getScore(Owner.SECOND).asString()," case pour O"));
        if (model.gameOver().get()){
            CASEX.setStyle("-fx-background-color: red;");
        }else {
            CASEX.setStyle("-fx-background-color: #76E0ED;");
        }
        CASEO.setStyle("-fx-background-color: red;");
    }

    @FXML
    void restart(ActionEvent event) {
        model.restart();
        score();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TicTacToeSquare square = findSquare(board,i,j);
                int finalJ = j;
                int finalI = i;
                square.textProperty().bind(Bindings.createObjectBinding(()->{
                    if (model.getSquare(finalI, finalJ).get().equals(Owner.FIRST)){
                        return "X";
                    }else if (model.getSquare(finalI, finalJ).get().equals(Owner.SECOND)){
                        return "O";
                    }else {
                        return "";
                    }
                },model.getSquare(finalI, finalJ)));

                square.styleProperty().bind(
                        Bindings.when(model.getWinningSquare(finalI, finalJ))
                                .then("-fx-font-size: 32px; -fx-max-width: 120px; -fx-max-width: 120px; -fx-max-height: 120px; -fx-alignment: center;")
                                .otherwise("-fx-font-size: 22px; -fx-max-width: 120px; -fx-max-height: 120px; -fx-alignment: center;")
                );
            }
        }
    }

    private TicTacToeSquare findSquare(GridPane gridPane, int col, int row) {
        for (Node square : gridPane.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(square);
            Integer colIndex = GridPane.getColumnIndex(square);

            if (rowIndex != null && colIndex != null && rowIndex == row && colIndex == col) {
                return (TicTacToeSquare) square;
            }
        }
        return null;
    }

}