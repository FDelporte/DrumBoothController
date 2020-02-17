package be.webtechie.drumbooth.ui;

import be.webtechie.drumbooth.i2c.RelayController;
import be.webtechie.drumbooth.i2c.definition.Board;
import be.webtechie.drumbooth.i2c.definition.Relay;
import be.webtechie.drumbooth.i2c.definition.State;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ToggleSwitch;

/**
 * Builder for a screen with two rows with four toggle switches.
 */
public class RelayPanel extends VBox {

    public RelayPanel() {
        this.setSpacing(25);
        this.setPadding(new Insets(25));

        this.getChildren().add(this.createRow(Board.BOARD_1, 0, true, false, false, false));
        this.getChildren().add(this.createRow(Board.BOARD_2, 4, false, false, false, false));

        System.out.println("Toggle switch screen created");
    }

    /**
     * Create a row with four toggle switches for the given board.
     *
     * @param board The board to be controlled
     * @param offset Offset for the number showed in the label
     * @return The created HBox
     */
    private HBox createRow(Board board, int offset,
            boolean relay1Inverted, boolean relay2Inverted, boolean relay3Inverted, boolean relay4Inverted) {
        HBox row = new HBox();
        row.setSpacing(25);

        row.getChildren().add(this.createRelayToggleSwitch(
                "Relais " + (offset + 1),
                board, Relay.RELAY_1, relay1Inverted));
        row.getChildren().add(this.createRelayToggleSwitch(
                "Relais " + (offset + 2),
                board, Relay.RELAY_2, relay2Inverted));
        row.getChildren().add(this.createRelayToggleSwitch(
                "Relais " + (offset + 3),
                board, Relay.RELAY_3, relay3Inverted));
        row.getChildren().add(this.createRelayToggleSwitch(
                "Relais " + (offset + 4),
                board, Relay.RELAY_4, relay4Inverted));

        return row;
    }

    /**
     * Create a ToggleSwitch which will call the RelayController on change.
     *
     * @param label Label for the toggle switch
     * @param board The board to be controlled
     * @param relay The relay on the board to be controlled
     * @param inverted Flag for relay on which the output is connected to the non-actived side
     * @return The created ToggleSwitch
     */
    private ToggleSwitch createRelayToggleSwitch(String label, Board board, Relay relay, boolean inverted) {
        ToggleSwitch toggleSwitch = new ToggleSwitch();
        toggleSwitch.setText(label);
        toggleSwitch.selectedProperty().addListener((observable, oldValue, selected) ->
                RelayController.setRelay(board, relay,
                        selected ?
                                inverted ? State.STATE_OFF : State.STATE_ON :
                                inverted ? State.STATE_ON : State.STATE_OFF));

        if (inverted) {
            toggleSwitch.setSelected(true);
        }

        return toggleSwitch;
    }
}
