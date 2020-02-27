package be.webtechie.drumbooth.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

/**
 * Builder for the exit screen.
 */
public class ExitPanel extends VBox {
    private static Logger logger = Logger.getLogger(ExitPanel.class);

    public ExitPanel() {
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
            Process p = Runtime.getRuntime().exec(new String[]{"shutdown", "now"});
            p.waitFor();
        } catch (Exception ex) {
            logger.error("Exit error: " + ex.getMessage());
        }

        Platform.exit();
        System.exit(0);
    }
}
