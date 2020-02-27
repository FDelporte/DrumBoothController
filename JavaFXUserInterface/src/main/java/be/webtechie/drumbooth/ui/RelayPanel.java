package be.webtechie.drumbooth.ui;

import be.webtechie.drumbooth.i2c.RelayController;
import be.webtechie.drumbooth.i2c.definition.Board;
import be.webtechie.drumbooth.i2c.definition.Relay;
import be.webtechie.drumbooth.i2c.definition.State;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import org.controlsfx.control.ToggleSwitch;

/**
 * Builder for a screen with two rows with four toggle switches.
 */
public class RelayPanel extends VBox {
    private static Logger logger = Logger.getLogger(RelayPanel.class);

    /**
     * Constructor
     */
    public RelayPanel() {
        this.setSpacing(25);
        this.setPadding(new Insets(25));

        HBox row1 = new HBox();
        row1.setSpacing(25);
        row1.getChildren().add(this.createRelayToggleSwitch("TL WIT", Board.BOARD_1, Relay.RELAY_1, true));
        row1.getChildren().add(this.createRelayToggleSwitch("-", Board.BOARD_1, Relay.RELAY_2));
        row1.getChildren().add(this.createRelayToggleSwitch("-", Board.BOARD_1, Relay.RELAY_3));
        row1.getChildren().add(this.createRelayToggleSwitch("-", Board.BOARD_1, Relay.RELAY_4));
        this.getChildren().add(row1);

        HBox row2 = new HBox();
        row2.setSpacing(25);
        row2.getChildren().add(this.createRelayToggleSwitch("STROBO", Board.BOARD_2, Relay.RELAY_1));
        row2.getChildren().add(this.createRelayToggleSwitch("BOLSPOT", Board.BOARD_2, Relay.RELAY_2));
        row2.getChildren().add(this.createRelayToggleSwitch("BOLMTR", Board.BOARD_2, Relay.RELAY_3));
        row2.getChildren().add(this.createRelayToggleSwitch("-", Board.BOARD_2, Relay.RELAY_4));
        this.getChildren().add(row2);

        logger.info("Toggle switch screen created");
    }

    /**
     * Create a ToggleSwitch which will call the RelayController on change.
     *
     * @param label Label for the toggle switch
     * @param board The board to be controlled
     * @param relay The relay on the board to be controlled
     * @return The created VBox with ToggleSwitch
     */
    private VBox createRelayToggleSwitch(String label, Board board, Relay relay) {
        return this.createRelayToggleSwitch(label, board, relay, false);
    }

    /**
     * Create a ToggleSwitch which will call the RelayController on change.
     *
     * @param text Label for the toggle switch
     * @param board The board to be controlled
     * @param relay The relay on the board to be controlled
     * @param inverted Flag for relay on which the output is connected to the non-actived side
     * @return The created VBox with ToggleSwitch
     */
    private VBox createRelayToggleSwitch(String text, Board board, Relay relay, boolean inverted) {
        VBox toggleHolder = new VBox();
        toggleHolder.setMinWidth(100);
        toggleHolder.setAlignment(Pos.CENTER);
        toggleHolder.setSpacing(10);
        toggleHolder.setPadding(new Insets(10));
        toggleHolder.getStyleClass().add("toggleHolder");

        Label lbl = new Label(text);
        lbl.setMinWidth(100);
        lbl.setAlignment(Pos.CENTER);
        lbl.getStyleClass().add("labelName");
        toggleHolder.getChildren().add(lbl);

        ToggleSwitch toggleSwitch = new ToggleSwitch();
        toggleSwitch.selectedProperty().addListener((observable, oldValue, selected) ->
                toggleRelay(board, relay, toggleSwitch, inverted));
        if (inverted) {
            toggleSwitch.setSelected(true);
        }
        toggleHolder.getChildren().add(toggleSwitch);

       toggleHolder.setOnMouseClicked(e -> this.changeToggleSwitch(toggleSwitch));

        return toggleHolder;
    }

    private void changeToggleSwitch(ToggleSwitch toggleSwitch) {
        logger.info("Changing toggle switch " + toggleSwitch);
        toggleSwitch.setSelected(!toggleSwitch.isSelected());
    }

    private void toggleRelay(Board board, Relay relay, ToggleSwitch toggleSwitch, boolean inverted) {
        logger.info("Toggling relay " + board + ":" + relay);
        RelayController.setRelay(board, relay,
                toggleSwitch.isSelected() ?
                        inverted ? State.STATE_OFF : State.STATE_ON :
                        inverted ? State.STATE_ON : State.STATE_OFF);
    }
}
