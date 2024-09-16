package be.webtechie.drumbooth.ui;

import be.webtechie.drumbooth.event.EventListener;
import be.webtechie.drumbooth.event.EventManager;
import be.webtechie.drumbooth.led.LedCommand;
import be.webtechie.drumbooth.relay.RelayCommand;
import be.webtechie.drumbooth.relay.Relay;
import be.webtechie.drumbooth.ui.component.RelayToggleSwitch;
import com.pi4j.io.gpio.digital.DigitalState;
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
        this.setPadding(new Insets(25, 25, 25, 125));

        HBox row1 = new HBox();
        row1.setAlignment(Pos.CENTER);
        row1.setSpacing(25);
        row1.getChildren().add(this.createRelayToggleSwitch("PANEEL", Relay.RELAY_1));
        row1.getChildren().add(this.createRelayToggleSwitch("WIT", Relay.RELAY_2));
        row1.getChildren().add(this.createRelayToggleSwitch("KLEUR", Relay.RELAY_3));
        this.getChildren().add(row1);

        HBox row2 = new HBox();
        row2.setSpacing(25);
        row2.getChildren().add(this.createRelayToggleSwitch("STROBO", Relay.RELAY_4));
        row2.getChildren().add(this.createRelayToggleSwitch("BOLSPOT", Relay.RELAY_5));
        row2.getChildren().add(this.createRelayToggleSwitch("BOLMTR", Relay.RELAY_6));
        this.getChildren().add(row2);

        console.println("Toggle switch screen created");
    }

    /**
     * Create a ToggleSwitch which will call the RelayController on change.
     *
     * @param text     Label for the toggle switch
     * @param relay    The relay on the board to be controlled
     * @return The created VBox with ToggleSwitch
     */
    private VBox createRelayToggleSwitch(String text, Relay relay) {
        VBox toggleHolder = new VBox();
        toggleHolder.setMinWidth(150);
        toggleHolder.setAlignment(Pos.CENTER);
        toggleHolder.setSpacing(10);
        toggleHolder.setPadding(new Insets(10));
        toggleHolder.getStyleClass().add("toggleHolder");

        Label lbl = new Label(text);
        lbl.setMinWidth(150);
        lbl.setAlignment(Pos.CENTER);
        lbl.getStyleClass().add("labelName");
        toggleHolder.getChildren().add(lbl);

        RelayToggleSwitch relayToggleSwitch = new RelayToggleSwitch(relay);
        relayToggleSwitch.setSelected(relay.getInitialState() == DigitalState.HIGH);
        relayToggleSwitch.selectedProperty().addListener((observable, oldValue, selected) -> toggleRelay(relayToggleSwitch));
        toggleHolder.getChildren().add(relayToggleSwitch);
        this.relayToggleSwitches.add(relayToggleSwitch);

        toggleHolder.setOnMouseClicked(e -> this.changeToggleSwitch(relayToggleSwitch));

        return toggleHolder;
    }

    private void changeToggleSwitch(ToggleSwitch toggleSwitch) {
        console.println("Changing toggle switch " + toggleSwitch);
        toggleSwitch.setSelected(!toggleSwitch.isSelected());
    }

    private void toggleRelay(RelayToggleSwitch relayToggleSwitch) {
        console.println("Toggling relay " + relayToggleSwitch.getRelay());
        this.eventManager.sendRelayCommand(new RelayCommand(
                relayToggleSwitch.getRelay(),
                (relayToggleSwitch.isSelected() ? DigitalState.HIGH : DigitalState.LOW)));
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
                console.println("Need to toggle UI for: " + relayCommand.relay());
                relayToggleSwitch.setSelected(relayCommand.state() == DigitalState.HIGH);
            }
        }
    }
}
