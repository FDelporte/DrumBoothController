package be.webtechie.drumbooth.ui;

import be.webtechie.drumbooth.event.EventListener;
import be.webtechie.drumbooth.event.EventManager;
import be.webtechie.drumbooth.led.LedCommand;
import be.webtechie.drumbooth.led.LedEffect;
import be.webtechie.drumbooth.relay.RelayCommand;
import com.pi4j.util.Console;
import eu.hansolo.fx.colorselector.ColorSelector;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LedControlPanel extends HBox implements EventListener {

    private final Console console;
    private final EventManager eventManager;

    private final ColorSelector colorSelector1;
    private final ColorSelector colorSelector2;

    private final ToggleButton btStatic;
    private final ToggleButton btStaticFade;
    private final ToggleButton btBlinking;
    private final ToggleButton btRunning;
    private final ToggleButton btStaticRainbow;
    private final ToggleButton btFadingRainbow;
    private final ToggleButton btWhite;
    private final ToggleButton btClear;

    private final Slider slider;

    private LedEffect selectedLedEffect;

    /**
     * Flag used to prevent endless loops when effect has changed from other component.
     */
    private boolean blockSending = false;

    /**
     * Construct the UI.
     */
    public LedControlPanel(Console console, EventManager eventManager) {
        this.console = console;
        this.eventManager = eventManager;
        this.eventManager.addListener(this);

        this.setSpacing(25);

        VBox colorSelectors = new VBox();
        colorSelectors.setSpacing(25);
        this.getChildren().add(colorSelectors);

        this.colorSelector1 = new ColorSelector();
        this.colorSelector1.setPrefSize(250, 250);
        this.colorSelector1.selectedColorProperty().addListener(e -> this.sendMessage());
        this.colorSelector1.setSelectedColor(Color.BLUE);
        colorSelectors.getChildren().add(this.colorSelector1);

        this.colorSelector2 = new ColorSelector();
        this.colorSelector2.setPrefSize(250, 250);
        this.colorSelector2.selectedColorProperty().addListener(e -> this.sendMessage());
        this.colorSelector2.setSelectedColor(Color.RED);
        colorSelectors.getChildren().add(this.colorSelector2);

        GridPane effectButtons = new GridPane();
        effectButtons.setHgap(10);
        effectButtons.setVgap(10);
        this.getChildren().add(effectButtons);

        this.btStatic = new ToggleButton("Vast");
        this.btStatic.getStyleClass().add("ledButton");
        this.btStatic.setOnAction(e -> this.setEffect(LedEffect.STATIC));
        effectButtons.add(this.btStatic, 0, 0);

        this.btStaticFade = new ToggleButton("Fade");
        this.btStaticFade.getStyleClass().add("ledButton");
        this.btStaticFade.setOnAction(e -> this.setEffect(LedEffect.STATIC_FADE));
        effectButtons.add(this.btStaticFade, 1, 0);

        this.btBlinking = new ToggleButton("Knipperen");
        this.btBlinking.getStyleClass().add("ledButton");
        this.btBlinking.setOnAction(e -> this.setEffect(LedEffect.BLINKING));
        effectButtons.add(this.btBlinking, 0, 1);

        this.btRunning = new ToggleButton("Looplicht");
        this.btRunning.getStyleClass().add("ledButton");
        this.btRunning.setOnAction(e -> this.setEffect(LedEffect.RUNNING));
        effectButtons.add(this.btRunning, 1, 1);

        this.btStaticRainbow = new ToggleButton("Vaste regenboog");
        this.btStaticRainbow.getStyleClass().add("ledButton");
        this.btStaticRainbow.setOnAction(e -> this.setEffect(LedEffect.STATIC_RAINBOW));
        effectButtons.add(this.btStaticRainbow, 0, 2);

        this.btFadingRainbow = new ToggleButton("Fadende regenboog");
        this.btFadingRainbow.getStyleClass().add("ledButton");
        this.btFadingRainbow.setOnAction(e -> this.setEffect(LedEffect.FADING_RAINBOW));
        effectButtons.add(this.btFadingRainbow, 1, 2);

        this.btWhite = new ToggleButton("Wit");
        this.btWhite.getStyleClass().add("ledButton");
        this.btWhite.setOnAction(e -> this.setEffect(LedEffect.ALL_WHITE));
        effectButtons.add(this.btWhite, 0, 3);

        this.btClear = new ToggleButton("Uit");
        this.btClear.getStyleClass().add("ledButton");
        this.btClear.setOnAction(e -> this.setEffect(LedEffect.ALL_OUT));
        effectButtons.add(this.btClear, 1, 3);

        effectButtons.add(new Label(""), 0, 4, 2, 1);

        Label lblSpeed = new Label("Speed");
        lblSpeed.getStyleClass().add("ledSpeed");
        GridPane.setHalignment(lblSpeed, HPos.CENTER);
        effectButtons.add(lblSpeed, 0, 5, 2, 1);

        this.slider = new Slider();
        this.slider.setShowTickLabels(true);
        this.slider.setShowTickMarks(true);
        this.slider.valueProperty().addListener((observable, oldValue, newValue) -> this.sendMessage());
        effectButtons.add(this.slider, 0, 6, 2, 1);

        this.setEffect(LedEffect.ALL_OUT);
    }

    /**
     * Handle the chosen effect from a button to enable/disable the available UI elements and
     * highlight the button of the selected {@link LedEffect}.
     *
     * @param ledEffect {@link LedEffect}
     */
    private void setEffect(LedEffect ledEffect) {
        this.selectedLedEffect = ledEffect;

        this.colorSelector1.setDisable(!ledEffect.useColor1());
        this.colorSelector2.setDisable(!ledEffect.useColor2());
        this.slider.setDisable(!ledEffect.useSpeed());
        this.slider.setMin(ledEffect.getMinimumSpeed());
        this.slider.setMax(ledEffect.getMaximumSpeed());
        this.slider.setValue(ledEffect.getInitialSpeed());

        this.btStatic.setSelected(ledEffect == LedEffect.STATIC);
        this.btStaticFade.setSelected(ledEffect == LedEffect.STATIC_FADE);
        this.btBlinking.setSelected(ledEffect == LedEffect.BLINKING);
        this.btRunning.setSelected(ledEffect == LedEffect.RUNNING);
        this.btStaticRainbow.setSelected(ledEffect == LedEffect.STATIC_RAINBOW);
        this.btFadingRainbow.setSelected(ledEffect == LedEffect.FADING_RAINBOW);
        this.btWhite.setSelected(ledEffect == LedEffect.ALL_WHITE);
        this.btClear.setSelected(ledEffect == LedEffect.ALL_OUT);

        this.sendMessage();
    }

    /**
     * Send a message to the Arduino board if a new effect and/or different parameters are selected.
     */
    private void sendMessage() {
        if (this.slider == null) {
            // Not ready yet
            return;
        }

        if (this.blockSending) {
            // Avoid sending the same command to avoid infinite loops.
            return;
        }

        LedCommand ledCommand = new LedCommand(
                this.selectedLedEffect,
                (int) this.slider.getValue(),
                this.colorSelector1.getSelectedColor(),
                this.colorSelector2.getSelectedColor()
        );

        console.println("Sending to Arduino: " + ledCommand.toCommandString());

        this.eventManager.sendSerialCommand(ledCommand);
    }

    /**
     * {@link LedCommand} received from {@link EventManager}.
     * We block sending updates back to the Arduino until this component is fully updated to match the received command,
     * to avoid infinite loops.
     *
     * @param ledCommand The {@link LedCommand}
     */
    @Override
    public void onLedStripChange(LedCommand ledCommand) {
        this.blockSending = true;

        this.setEffect(ledCommand.getLedEffect());
        this.slider.setValue(ledCommand.getSpeed());
        this.colorSelector1.setSelectedColor(ledCommand.getColor1());
        this.colorSelector2.setSelectedColor(ledCommand.getColor2());

        this.blockSending = false;
    }

    /**
     * {@link RelayCommand} received from {@link EventManager}.
     * Not to be handled here.
     *
     * @param relayCommand The {@link RelayCommand}
     */
    @Override
    public void onRelayChange(RelayCommand relayCommand) {
        // NOP
    }
}
