package be.webtechie.drumbooth;

import be.webtechie.drumbooth.i2c.RelayController;
import be.webtechie.drumbooth.i2c.definition.Board;
import be.webtechie.drumbooth.i2c.definition.Relay;
import be.webtechie.drumbooth.i2c.definition.State;
import be.webtechie.drumbooth.ui.ToggleSwitchScreen;
import java.util.Arrays;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        System.out.println("Starting application");

        // Set all relays out
        RelayController.setRelays(
                Arrays.asList(Board.BOARD_1, Board.BOARD_2),
                Arrays.asList(Relay.RELAY_1, Relay.RELAY_2, Relay.RELAY_3, Relay.RELAY_4),
                State.STATE_OFF);
        System.out.println("All relays turned off");

        var scene = new Scene(new ToggleSwitchScreen(), 640, 480);
        stage.setScene(scene);
        stage.setTitle("I²C Relay controller");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}