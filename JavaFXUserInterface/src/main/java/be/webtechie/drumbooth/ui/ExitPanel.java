package be.webtechie.drumbooth.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Builder for the exit screen.
 */
public class ExitPanel extends VBox {

    public ExitPanel() {
        this.setSpacing(25);
        this.setPadding(new Insets(25));

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
            Process p = Runtime.getRuntime().exec(new String[]{ "su", "-c", "shutdown" });
            p.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Platform.exit();
        System.exit(0);
    }
}
