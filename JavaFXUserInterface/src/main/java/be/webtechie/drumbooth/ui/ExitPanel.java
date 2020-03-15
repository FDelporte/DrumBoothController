package be.webtechie.drumbooth.ui;

import be.webtechie.drumbooth.event.EventManager;
import be.webtechie.drumbooth.led.LedCommand;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Builder for the exit screen.
 */
public class ExitPanel extends VBox {
    private static Logger logger = LogManager.getLogger(ExitPanel.class);

    private final EventManager eventManager;

    public ExitPanel(EventManager eventManager) {
        this.eventManager = eventManager;

        this.setSpacing(25);
        this.setPadding(new Insets(25));
        this.setAlignment(Pos.TOP_LEFT);

        Button btExit = new Button("Afsluiten");
        btExit.getStyleClass().add("exitButton");
        btExit.setOnAction(e -> this.exit());

        this.getChildren().add(btExit);
    }

    /**
     * Power off the Raspberry Pi
     */
    private void exit() {
        try {
            this.eventManager.setAllOff();
            this.eventManager.sendSerialCommand(LedCommand.getInitialState());

            // Wait till serial command is handled
            Thread.sleep(2500);

            Process p = Runtime.getRuntime().exec(new String[]{"shutdown", "now"});
            p.waitFor();
        } catch (Exception ex) {
            logger.error("Exit error: " + ex.getMessage());
        }

        Platform.exit();
        System.exit(0);
    }
}
