package be.webtechie.drumbooth.ui;

import be.webtechie.drumbooth.event.EventManager;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuWindow extends HBox {

    private final Pane pane;
    private final Group relayButtons;
    private final Group ledStrips;
    private final Group exitConfirm;

    /**
     * Construct the main UI with the menu buttons.
     */
    public MenuWindow(EventManager eventManager) {
        this.setSpacing(25);
        this.getStylesheets().add("style.css");
        this.getStyleClass().add("bg");

        this.getChildren().add(this.getMainMenu());

        this.pane = new StackPane();
        this.getChildren().add(this.pane);

        RelayPanel relayPanel = new RelayPanel();
        this.relayButtons = new Group(relayPanel);

        LedControlPanel ledControlPanel = new LedControlPanel(eventManager);
        this.ledStrips = new Group(ledControlPanel);

        ExitPanel exitPanel = new ExitPanel();
        this.exitConfirm = new Group(exitPanel);

        this.show(this.relayButtons);
    }

    /**
     * Builds the main menu button bar.
     *
     * @return {@link VBox}
     */
    private VBox getMainMenu() {
        final VBox buttons = new VBox();
        buttons.setPadding(new Insets(5, 5, 5, 5));
        buttons.setSpacing(5);

        final Button btRelay = new Button("Lichten");
        btRelay.getStyleClass().add("menuButton");
        btRelay.setOnAction(e -> this.show(this.relayButtons));
        buttons.getChildren().add(btRelay);

        final Button btLedStrips = new Button("Strips");
        btLedStrips.getStyleClass().add("menuButton");
        btLedStrips.setOnAction(e -> this.show(this.ledStrips));
        buttons.getChildren().add(btLedStrips);

        final Button btExit = new Button("Sluit");
        btExit.getStyleClass().add("menuButton");
        btExit.setOnAction(e -> this.show(this.exitConfirm));
        buttons.getChildren().add(btExit);

        return buttons;
    }

    /**
     * Switch to the given screen.
     *
     * @param group The group node to be shown
     */
    private void show(Group group) {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(group);
    }
}
