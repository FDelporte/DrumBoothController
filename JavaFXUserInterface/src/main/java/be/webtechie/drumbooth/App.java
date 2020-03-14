package be.webtechie.drumbooth;

import be.webtechie.drumbooth.event.EventManager;
import be.webtechie.drumbooth.led.LedCommand;
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
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class App extends Application {
    private static Logger logger = Logger.getLogger(App.class);

    private static final String SERIAL_DEVICE = "/dev/ttyACM0";
    private static final Baud SERIAL_SPEED = Baud._115200;
    private static final int WEBSERVER_PORT = 8080;
    private static final String WEBSERVER_HOST = "192.168.0.160";

    private static EventManager eventManager;

    /**
     * Entry point of the application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Logger.getRootLogger().getLoggerRepository().resetConfiguration();
        initLog();
        launch();
    }

    /**
     * Starting point of the JavaFX application.
     *
     * @param stage Tha JavaFX stage
     */
    @Override
    public void start(Stage stage) {
        logger.info("Starting application");

        // Create an instance of the serial communications class
        final Serial serial = SerialFactory.createInstance();

        // Initialize the EventManager
        eventManager = new EventManager(serial);

        // Initialize the serial communication with the Arduino board
        this.startSerialCommunication(serial);

        // Initialize the web server
        this.startWebServer();

        // Set all relays out, to make sure they match with the UI
        eventManager.setAllOff();
        logger.info("All relays in initial state");

        // Set LED strips in start-up state
        eventManager.sendSerialCommand(LedCommand.getInitialState());

        var scene = new Scene(new MenuWindow(eventManager), 1024, 600);
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

    /**
     * Start the serial communication
     *
     * @param serial Pi4J serial factory
     */
    private void startSerialCommunication(Serial serial) {
        // Can't be used on Windows (e.g. while developing and debugging)
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return;
        }

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
            logger.info("Connection: " + config.toString());

            // Open the serial port with the configuration
            serial.open(config);
        } catch (Exception ex) {
            logger.error("Could not start serial communication, error: " + ex.getMessage());
        }
    }

    private void startWebServer() {
        try {
            Undertow server = Undertow.builder()
                    .addHttpListener(WEBSERVER_PORT, WEBSERVER_HOST)
                    .setHandler(new WebHandler(eventManager))
                    .build();
            server.start();
        } catch (Exception ex) {
            logger.error("Could not start web server, error: " + ex.getMessage());
        }
    }

    private static void initLog() {
        PatternLayout logPattern = new PatternLayout("%d{yyyyMMdd HH:mm:ss,SSS} | %-5p | [%c{1}] | %m%n");

        ConsoleAppender console = new ConsoleAppender();
        console.setName("ConsoleLogger");
        console.setLayout(logPattern);
        console.setThreshold(Level.DEBUG);
        console.activateOptions();
        Logger.getRootLogger().addAppender(console);

        RollingFileAppender file = new RollingFileAppender();
        file.setName("FileLogger");
        file.setFile("/tmp/logs/app.log");
        file.setLayout(logPattern);
        file.setThreshold(Level.INFO);
        file.setAppend(true);
        file.activateOptions();
        file.setMaxFileSize("10MB");
        file.setMaxBackupIndex(5);
        Logger.getRootLogger().addAppender(file);
    }
}