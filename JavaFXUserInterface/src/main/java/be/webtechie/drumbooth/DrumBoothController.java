package be.webtechie.drumbooth;

import be.webtechie.drumbooth.event.EventManager;
import be.webtechie.drumbooth.led.LedCommand;
import be.webtechie.drumbooth.server.WebHandler;
import be.webtechie.drumbooth.ui.MenuWindow;
import com.pi4j.util.Console;
import io.undertow.Undertow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DrumBoothController extends Application {

    private static final String SERIAL_DEVICE = "/dev/tty0";
    private static final int WEBSERVER_PORT = 8080;
    private static final String WEBSERVER_HOST = "172.16.1.50";
    private final Console console = new Console();
    private EventManager eventManager;

    /**
     * Entry point of the application.
     */
    public void run() {
        console.println("Starting UI");
        launch();
    }

    /**
     * Starting point of the JavaFX application.
     *
     * @param stage Tha JavaFX stage
     */
    @Override
    public void start(Stage stage) {
        console.println("Starting application");

        // Initialize the EventManager
        eventManager = new EventManager(console, SERIAL_DEVICE);

        // Initialize the web server
        this.startWebServer();

        // Set LED strips in start.sh-up state
        eventManager.sendSerialCommand(LedCommand.getInitialState());

        var scene = new Scene(new MenuWindow(console, eventManager), 1024, 600);
        stage.setScene(scene);
        stage.setTitle("Drumbooth Control Panel");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        // Make sure the application quits completely on close
        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    private void startWebServer() {
        try {
            Undertow server = Undertow.builder()
                    .addHttpListener(WEBSERVER_PORT, WEBSERVER_HOST)
                    .setHandler(new WebHandler(eventManager))
                    .build();
            server.start();
        } catch (Exception ex) {
            console.println("Could not start web server, error: {}", ex.getMessage());
        }
    }
}