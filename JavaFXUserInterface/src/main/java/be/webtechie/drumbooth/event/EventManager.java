package be.webtechie.drumbooth.event;

import be.webtechie.drumbooth.relay.RelayCommand;
import be.webtechie.drumbooth.relay.definition.Board;
import be.webtechie.drumbooth.relay.definition.Relay;
import be.webtechie.drumbooth.relay.definition.State;
import be.webtechie.drumbooth.led.LedCommand;
import com.pi4j.io.serial.Serial;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventManager {

    private static Logger logger = LogManager.getLogger(EventManager.class);

    private final Serial serial;
    private LedCommand lastLedCommand = null;
    private RelayCommand lastRelayCommand = null;


    public EventManager(Serial serial) {
        this.serial = serial;
    }

    /**
     * The list with components to be notified of a new LedCommand received from Mosquitto.
     */
    private List<EventListener> eventListeners = new ArrayList<>();

    /**
     * Used by every component which wants to be notified of new events.
     *
     * @param eventListener {@link EventListener}
     */
    public void addListener(EventListener eventListener) {
        this.eventListeners.add(eventListener);
    }

    /**
     * Used by every component which wants to be send a {@link LedCommand} to the Arduino.
     *
     * @param ledCommand {@link LedCommand}
     */
    public void sendSerialCommand(LedCommand ledCommand) {
        this.lastLedCommand = ledCommand;
        this.eventListeners.forEach(l -> l.onLedStripChange(ledCommand));

        try {
            this.serial.writeln(ledCommand.toCommandString());
        } catch (Exception ex) {
            logger.error("Error while sending : " + ex.getMessage());
        }
    }

    public void sendRelayCommand(RelayCommand relayCommand) {
        this.lastRelayCommand = relayCommand;
        this.eventListeners.forEach(l -> l.onRelayChange(relayCommand));

        String cmd = "i2cset -y 1"
                + " " + toHexString(relayCommand.getBoard().getAddress())
                + " " + toHexString(relayCommand.getRelay().getChannel())
                + " " + toHexString(relayCommand.getState().getValue());

        execute(cmd);

        logger.info(relayCommand.getRelay()
                + " on " + relayCommand.getBoard()
                + " set to " + relayCommand.getState()
                + " with command: " + cmd);
    }

    /**
     * Set all relays off
     */
    public void setAllOff() {
        setRelays(
                Arrays.asList(Board.BOARD_1, Board.BOARD_2),
                Arrays.asList(Relay.RELAY_1, Relay.RELAY_2, Relay.RELAY_3, Relay.RELAY_4),
                State.STATE_OFF
        );
    }

    /**
     * Set the state of the all the relays on the given boards.
     *
     * @param boards List of Enum Board
     * @param relays List of Enum Relay
     * @param state Enum State
     */
    public void setRelays(List<Board> boards, List<Relay> relays, State state) {
        for (Board board : boards) {
            for (Relay relay : relays) {
                this.sendRelayCommand(new RelayCommand(board, relay, state));
            }
        }
    }

    /**
     * @return The last {@link LedCommand} sent to the Arduino.
     */
    public LedCommand getLastLedCommand() {
        return this.lastLedCommand;
    }

    /**
     * @return The last {@link RelayCommand} sent to the relay board.
     */
    public RelayCommand getLastRelayCommand() {
        return this.lastRelayCommand;
    }

    /**
     * Convert the value to HEX byte string.
     *
     * @param value Numeric value
     * @return String value in HEX format
     */
    private static String toHexString(int value) {
        return String.format("0x%02X", value).substring(0, 4);
    }

    /**
     * Execute the given command, this is called by the public methods.
     *
     * @param cmd String command to be executed.
     */
    private static void execute(String cmd) {
        try {
            logger.info("Executing: " + cmd);

            // Get a process to be able to do native calls on the operating system.
            // You can compare this to opening a terminal window and running a command.
            Process p = Runtime.getRuntime().exec(cmd);

            // Get the error stream of the process and print it
            // so we will now if something goes wrong.
            InputStream error = p.getErrorStream();
            for (int i = 0; i < error.available(); i++) {
                logger.error("CMD error: " + error.read());
            }

            // Get the output stream of the process and print it
            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                logger.info("CMD info: " + line);
            }
            input.close();

            // We don't need the process anymore.
            p.destroy();
        } catch (IOException ex) {
            logger.error("Error while sending relay I²C command: " + ex.getMessage());
        }
    }

    /*
    // I²C seems to be broken in Pi4J when used with after Java 8.
    // So below code doesn't work, but is added here as a reference.

    private I2CBus i2c;

    public RelayController() {
        try {
            this.i2c = I2CFactory.getInstance(I2CBus.BUS_1);
        } catch (UnsupportedBusNumberException ex) {
            logger.error("Bus number not supported: "
                    + ex.getMessage());
            this.i2c = null;
        } catch (IOException ex) {
            logger.error("Error while initializing the relay controller: "
                    + ex.getMessage());
            this.i2c = null;
        }
    }

    public void setRelay(Board board, Relay relay, State state) {
        logger.info("Setting relay on board "
                + String.format("0x%02X", board.getAddress())
                + ", relay " + String.format("0x%02X", relay.getChannel())
                + ", state " + String.format("0x%02X", state.getValue()));

        if (this.i2c == null) {
            logger.error("I²C not available");
            return;
        }

        try {
            I2CDevice device = i2c.getDevice(board.getAddress());
            device.write(relay.getChannel(), state.getValue());
        } catch (IOException ex) {
            logger.error("Error while setting relay: " + ex.getMessage());
        }
    }
    */
}
