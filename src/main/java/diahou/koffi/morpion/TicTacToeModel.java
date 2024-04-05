package diahou.koffi.morpion;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.NumberExpression;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.*;

public class TicTacToeModel {
    /**
     * 3 * Taille du plateau de jeu (pour être extensible).
     * 4
     */
    private final static int BOARD_WIDTH = 3;
    private final static int BOARD_HEIGHT = 3;
    /**
     * 8 * Nombre de pièces alignés pour gagner (idem).
     * 9
     */
    private final static int WINNING_COUNT = 3;
    /**
     * 12 * Joueur courant.
     * 13
     */
    private final ObjectProperty<Owner> turn = new SimpleObjectProperty<>(Owner.FIRST);
    /**
     * 17 * Vainqueur du jeu, NONE si pas de vainqueur.
     * 18
     */
    private final ObjectProperty<Owner> winner = new SimpleObjectProperty<>(Owner.NONE);
    /**
     * 22 * Plateau de jeu.
     * 23
     */
    private final ObjectProperty<Owner>[][] board ;
    /**
     * 26 * Positions gagnantes.
     * 27
     */
    private final BooleanProperty[][] winningBoard ;

    /**
     * 31 * Constructeur privé.
     * 32
     */
    TicTacToeModel() {
        this.board = new ObjectProperty[BOARD_HEIGHT][BOARD_WIDTH];
        this.winningBoard = new BooleanProperty[BOARD_HEIGHT][BOARD_WIDTH];

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                this.board[i][j] = new SimpleObjectProperty<>(Owner.NONE);
                this.winningBoard[i][j] = new SimpleBooleanProperty(false);
            }
        }
    }

    /**
     * 36 * @return la seule instance possible du jeu.
     * 37
     */
    public static TicTacToeModel getInstance() {
        return TicTacToeModelHolder.INSTANCE;
    }

    /**
     * 43 * Classe interne selon le pattern singleton.
     * 44
     */

    private static class TicTacToeModelHolder {
        private static final TicTacToeModel INSTANCE = new TicTacToeModel();
    }

    public void restart() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                this.board[i][j] = new SimpleObjectProperty<>(Owner.NONE);
                this.winningBoard[i][j] = new SimpleBooleanProperty(false);
            }
        }
        turn.setValue(Owner.FIRST);
        this.setWinner(Owner.NONE);
    }

    public final ObjectProperty<Owner> turnProperty() {
        return turn;
    }

    public final ObjectProperty<Owner> getSquare(int row, int column) {
        return board[row][column];
    }

    public final BooleanProperty getWinningSquare(int row, int column) {
        return winningBoard[row][column];
    }

    /**
     * 61 * Cette fonction ne doit donner le bon résultat que si le jeu
     * 62 * est terminé. L’affichage peut être caché avant la fin du jeu.
     * 63 *
     * 64 * @return résultat du jeu sous forme de texte
     * 65
     */
    public final StringExpression getEndOfGameMessage() {
        BooleanBinding binding = this.gameOver();
        StringExpression expression = Bindings.concat("");
        if (binding.get()){
            if (winner.get().equals(Owner.NONE)){
                expression = Bindings.concat("Game over: match null pas de gagnant");
            }else if (winner.get().equals(Owner.FIRST)){
                expression = Bindings.concat("Game over: Le gagnant est le premier joueur.");
            }else  {
                expression = Bindings.concat("Game over: Le gagnant est le deuxième joueur");
            }
            return expression;
        }
        return expression;
    }

    public void setWinner(Owner winner) {
        this.winner.setValue(winner);
    }

    public boolean validSquare(int row, int column) {
        return board[row][column].get().equals( Owner.NONE);
    }

    public void nextPlayer() {
        if (turn.get() == Owner.FIRST){
            turn.setValue(Owner.SECOND);
        }else if (turn.get() == Owner.SECOND){
            turn.setValue(Owner.FIRST);
        }
    }

    /**
     * 75 * Jouer dans la case (row, column) quand c’est possible.
     * 76
     */
    public void play(int row, int column) {
        board[row][column].setValue(turn.get());
        nextPlayer();
    }

    /**
     * 80 * @return true s’il est possible de jouer dans la case
     * 81 * c’est-à-dire la case est libre et le jeu n’est pas terminé
     * 82
     */
    public BooleanBinding legalMove(int row, int column) {
        BooleanBinding bindingOver = this.gameOver();
        boolean bool = bindingOver.get();
        ObjectProperty<Owner> ownerObjectProperty = board[row][column];
        BooleanProperty bool1 = new SimpleBooleanProperty(true);
        BooleanProperty bool2 = new SimpleBooleanProperty(false);
        BooleanBinding binding;
        if (ownerObjectProperty.get() != Owner.NONE || bool){
            binding = bool1.and(bool2);
        }else {
            binding = bool1.or(bool2);
        }
        return binding;
    }

    public NumberExpression getScore(Owner owner) {
        IntegerProperty num1 = new SimpleIntegerProperty(0);

        int cpt = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j].get().equals(owner)){
                    cpt ++;
                }
            }
        }
        NumberExpression sum = num1.add(cpt);
        return sum;
    }


    /**
     *  @return true si le jeu est terminé
     *  (soit un joueur a gagné, soit il n’y a plus de cases à jouer)
     */
    public BooleanBinding gameOver() {

        BooleanProperty bool1 = new SimpleBooleanProperty(true);
        BooleanProperty bool2 = new SimpleBooleanProperty(false);
        BooleanBinding binding = bool1.and(bool2);

        // Vérifier les lignes
        for (int i = 0; i < board.length; i++) {
            int cpt1 = 0, cpt2 = 0;
            for (int j = 0; j < board.length; j++) {
                if (board[i][j].get().equals(Owner.FIRST)) {
                    cpt1++;
                } else if (board[i][j].get().equals(Owner.SECOND)) {
                    cpt2++;
                }
            }
            if (cpt1 == WINNING_COUNT) {
                for (int j = 0; j < board.length; j++) {
                    winningBoard[i][j].set(true);
                }
                this.setWinner(Owner.FIRST);
                return bool1.or(bool2);
            } else if (cpt2 == WINNING_COUNT) {
                for (int j = 0; j < board.length; j++) {
                    winningBoard[i][j].set(true);
                }
                this.setWinner(Owner.SECOND);
                return bool1.or(bool2);
            }
        }

        // Vérifier les colonnes
        for (int i = 0; i < board.length; i++) {
            int cpt1 = 0, cpt2 = 0;
            for (int j = 0; j < board.length; j++) {
                if (board[j][i].get().equals(Owner.FIRST)) {
                    cpt1++;
                } else if (board[j][i].get().equals(Owner.SECOND)) {
                    cpt2++;
                }
            }
            if (cpt1 == WINNING_COUNT) {
                for (int j = 0; j < board.length; j++) {
                    winningBoard[j][i].set(true);
                }
                this.setWinner(Owner.FIRST);
                return bool1.or(bool2);
            } else if (cpt2 == WINNING_COUNT) {
                for (int j = 0; j < board.length; j++) {
                    winningBoard[j][i].set(true);
                }
                this.setWinner(Owner.SECOND);
                return bool1.or(bool2);
            }
        }

        // Vérifier la diagonale
        int cpt1 = 0, cpt2 = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i][i].get().equals(Owner.FIRST)) {
                cpt1++;
            } else if (board[i][i].get().equals(Owner.SECOND)) {
                cpt2++;
            }
        }
        if (cpt1 == WINNING_COUNT) {
            for (int i = 0; i < board.length; i++) {
                winningBoard[i][i].set(true);
            }
            this.setWinner(Owner.FIRST);
            return bool1.or(bool2);
        } else if (cpt2 == WINNING_COUNT) {
            for (int i = 0; i < board.length; i++) {
                winningBoard[i][i].set(true);
            }
            this.setWinner(Owner.SECOND);
            return bool1.or(bool2);
        }

        // Vérifier la contre-diagonale
        cpt1 = 0;
        cpt2 = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i][board.length - 1 - i].get().equals(Owner.FIRST)) {
                cpt1++;
            } else if (board[i][board.length - 1 - i].get().equals(Owner.SECOND)) {
                cpt2++;
            }
        }
        if (cpt1 == WINNING_COUNT) {
            for (int i = 0; i < board.length; i++) {
                winningBoard[i][board.length - 1 - i].set(true);
            }
            this.setWinner(Owner.FIRST);
            return bool1.or(bool2);
        } else if (cpt2 == WINNING_COUNT) {
            for (int i = 0; i < board.length; i++) {
                winningBoard[i][board.length - 1 - i].set(true);
            }
            this.setWinner(Owner.SECOND);
            return bool1.or(bool2);
        }


         // Verifier s'il n'a plus de case à jouer
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j].get().equals(Owner.NONE)){
                    return binding;
                }else
                if (i == 2 && j == 2){
                    if (!board[2][2].get().equals(Owner.NONE)){
                        binding = bool1.or(bool2);
                        return binding;
                    }
                }
            }
        }
        return binding;
    }
}