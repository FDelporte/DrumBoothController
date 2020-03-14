package be.webtechie.drumbooth.ui;

import be.webtechie.drumbooth.event.EventListener;
import be.webtechie.drumbooth.event.EventManager;
import be.webtechie.drumbooth.led.LedCommand;
import be.webtechie.drumbooth.relay.RelayCommand;
import be.webtechie.drumbooth.relay.definition.Board;
import be.webtechie.drumbooth.relay.definition.Relay;
import be.webtechie.drumbooth.relay.definition.State;
import be.webtechie.drumbooth.ui.component.RelayToggleSwitch;
import java.util.ArrayList;
import java.util.List;
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
public class RelayPanel extends VBox implements EventListener {

    private static Logger logger = Logger.getLogger(RelayPanel.class);

    private final EventManager eventManager;
    private final List<RelayToggleSwitch> relayToggleSwitches;

    /**
     * Constructor
     */
    public RelayPanel(EventManager eventManager) {
        this.eventManager = eventManager;
        this.eventManager.addListener(this);

        this.relayToggleSwitches = new ArrayList<>();

        this.setSpacing(25);
        this.setPadding(new Insets(25));

        HBox row1 = new HBox();
        row1.setSpacing(25);
        row1.getChildren().add(this.createRelayToggleSwitch("WIT", Board.BOARD_1, Relay.RELAY_1, true));
        row1.getChildren().add(this.createRelayToggleSwitch("KLEUR", Board.BOARD_1, Relay.RELAY_2));
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
     * @param text     Label for the toggle switch
     * @param board    The board to be controlled
     * @param relay    The relay on the board to be controlled
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

        RelayToggleSwitch relayToggleSwitch = new RelayToggleSwitch(board, relay, inverted);
        relayToggleSwitch.selectedProperty().addListener((observable, oldValue, selected) -> toggleRelay(relayToggleSwitch));
        toggleHolder.getChildren().add(relayToggleSwitch);
        this.relayToggleSwitches.add(relayToggleSwitch);

        toggleHolder.setOnMouseClicked(e -> this.changeToggleSwitch(relayToggleSwitch));

        return toggleHolder;
    }

    private void changeToggleSwitch(ToggleSwitch toggleSwitch) {
        logger.info("Changing toggle switch " + toggleSwitch);
        toggleSwitch.setSelected(!toggleSwitch.isSelected());
    }

    private void toggleRelay(RelayToggleSwitch relayToggleSwitch) {
        logger.info("Toggling relay " + relayToggleSwitch.getBoard() + ":" + relayToggleSwitch.getRelay());
        this.eventManager.sendRelayCommand(new RelayCommand(
                relayToggleSwitch.getBoard(),
                relayToggleSwitch.getRelay(),
                relayToggleSwitch.isSelected() ?
                        relayToggleSwitch.isInverted() ? State.STATE_OFF : State.STATE_ON :
                        relayToggleSwitch.isInverted() ? State.STATE_ON : State.STATE_OFF));
    }

    /**
     * {@link LedCommand} received from {@link EventManager}. Not to be handled here.
     *
     * @param ledCommand The {@link LedCommand}
     */
    @Override
    public void onLedStripChange(LedCommand ledCommand) {
        // NOP
    }

    /**
     * {@link RelayCommand} received from {@link EventManager}. Used to make sure the toggle switches are in the correct
     * state when they are changed by other component, e.g. the web interface.
     *
     * @param relayCommand The {@link RelayCommand}
     */
    @Override
    public void onRelayChange(RelayCommand relayCommand) {
        for (RelayToggleSwitch relayToggleSwitch : this.relayToggleSwitches) {
            if (relayToggleSwitch.getBoard() == relayCommand.getBoard()
                && relayToggleSwitch.getRelay() == relayCommand.getRelay()) {
                logger.info("Need to toggle UI for: " + relayCommand.getBoard() + "-" + relayCommand.getRelay());
                relayToggleSwitch.setSelected(
                        relayCommand.getState() == State.STATE_ON ?
                                (relayToggleSwitch.isInverted() ? false : true) :
                                (relayToggleSwitch.isInverted() ? true : false));
            }
        }
    }
}
