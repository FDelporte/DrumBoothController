package be.webtechie.drumbooth;

import be.webtechie.drumbooth.i2c.RelayController;
import be.webtechie.drumbooth.i2c.definition.Board;
import be.webtechie.drumbooth.i2c.definition.Relay;
import be.webtechie.drumbooth.i2c.definition.State;
import be.webtechie.drumbooth.server.WebHandler;
import be.webtechie.drumbooth.ui.MenuWindow;
import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.StopBits;
import io.undertow.Undertow;
import java.util.Arrays;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static final String SERIAL_DEVICE = "/dev/ttyACM0";
    private static final Baud SERIAL_SPEED = Baud._115200;

    @Override
    public void start(Stage stage) {
        System.out.println("Starting application");

        // TODO central controller so UI is updated on web change

        // Create an instance of the serial communications class
        final Serial serial = SerialFactory.createInstance();
        this.startSerialCommunication(serial);

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "192.168.0.160")
                .setHandler(new WebHandler(serial))
                .build();
        server.start();

        // Set all relays out
        RelayController.setRelays(
                Arrays.asList(Board.BOARD_1, Board.BOARD_2),
                Arrays.asList(Relay.RELAY_1, Relay.RELAY_2, Relay.RELAY_3, Relay.RELAY_4),
                State.STATE_OFF);
        System.out.println("All relays turned off");

        var scene = new Scene(new MenuWindow(serial), 640, 480);
        stage.setScene(scene);
        stage.setTitle("I²C Relay controller");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Start the serial communication
     *
     * @param serial Pi4J serial factory
     */
    private void startSerialCommunication(Serial serial) {
        try {
            // Create serial config object
            SerialConfig config = new SerialConfig();
            config.device(SERIAL_DEVICE)
                    .baud(SERIAL_SPEED)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);

            // Display connection details
            System.out.println("Connection: " + config.toString());

            // Open the serial port with the configuration
            serial.open(config);
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}