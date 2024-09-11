package be.webtechie.drumbooth.ui;

import be.webtechie.drumbooth.event.EventListener;
import be.webtechie.drumbooth.event.EventManager;
import be.webtechie.drumbooth.led.LedCommand;
import be.webtechie.drumbooth.relay.RelayCommand;
import be.webtechie.drumbooth.relay.definition.Relay;
import be.webtechie.drumbooth.relay.definition.State;
import be.webtechie.drumbooth.ui.component.RelayToggleSwitch;
import com.pi4j.util.Console;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ToggleSwitch;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for a screen with two rows with four toggle switches.
 */
public class RelayPanel extends VBox implements EventListener {

    private final Console console;
    private final EventManager eventManager;
    private final List<RelayToggleSwitch> relayToggleSwitches;

    /**
     * Constructor
     */
    public RelayPanel(Console console, EventManager eventManager) {
        this.console = console;
        this.eventManager = eventManager;
        this.eventManager.addListener(this);

        this.relayToggleSwitches = new ArrayList<>();

        this.setSpacing(25);
        this.setPadding(new Insets(25));

        HBox row1 = new HBox();
        row1.setSpacing(25);
        row1.getChildren().add(this.createRelayToggleSwitch("WIT", Relay.RELAY_1, true));
        row1.getChildren().add(this.createRelayToggleSwitch("KLEUR", Relay.RELAY_2));
        this.getChildren().add(row1);

        HBox row2 = new HBox();
        row2.setSpacing(25);
        row2.getChildren().add(this.createRelayToggleSwitch("STROBO", Relay.RELAY_3));
        row2.getChildren().add(this.createRelayToggleSwitch("BOLSPOT", Relay.RELAY_4));
        row2.getChildren().add(this.createRelayToggleSwitch("BOLMTR", Relay.RELAY_5));
        row2.getChildren().add(this.createRelayToggleSwitch("-", Relay.RELAY_6));
        this.getChildren().add(row2);

        console.println("Toggle switch screen created");
    }

    /**
     * Create a ToggleSwitch which will call the RelayController on change.
     *
     * @param label Label for the toggle switch
     * @param relay The relay on the board to be controlled
     * @return The created VBox with ToggleSwitch
     */
    private VBox createRelayToggleSwitch(String label, Relay relay) {
        return this.createRelayToggleSwitch(label, relay, false);
    }

    /**
     * Create a ToggleSwitch which will call the RelayController on change.
     *
     * @param text     Label for the toggle switch
     * @param relay    The relay on the board to be controlled
     * @param inverted Flag for relay on which the output is connected to the non-actived side
     * @return The created VBox with ToggleSwitch
     */
    private VBox createRelayToggleSwitch(String text, Relay relay, boolean inverted) {
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

        RelayToggleSwitch relayToggleSwitch = new RelayToggleSwitch(relay, inverted);
        relayToggleSwitch.selectedProperty().addListener((observable, oldValue, selected) -> toggleRelay(relayToggleSwitch));
        toggleHolder.getChildren().add(relayToggleSwitch);
        this.relayToggleSwitches.add(relayToggleSwitch);

        toggleHolder.setOnMouseClicked(e -> this.changeToggleSwitch(relayToggleSwitch));

        return toggleHolder;
    }

    private void changeToggleSwitch(ToggleSwitch toggleSwitch) {
        console.println("Changing toggle switch {}", toggleSwitch);
        toggleSwitch.setSelected(!toggleSwitch.isSelected());
    }

    private void toggleRelay(RelayToggleSwitch relayToggleSwitch) {
        console.println("Toggling relay {}", relayToggleSwitch.getRelay());
        this.eventManager.sendRelayCommand(new RelayCommand(
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
            if (relayToggleSwitch.getRelay() == relayCommand.relay()) {
                console.println("Need to toggle UI for: {}", relayCommand.relay());
                var setTo = (relayCommand.state() == State.STATE_ON) == (!relayToggleSwitch.isInverted());
                relayToggleSwitch.setSelected(setTo);
            }
        }
    }
}
