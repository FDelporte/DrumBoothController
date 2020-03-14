package be.webtechie.drumbooth.ui.component;

import be.webtechie.drumbooth.relay.definition.Board;
import be.webtechie.drumbooth.relay.definition.Relay;
import org.controlsfx.control.ToggleSwitch;

public class RelayToggleSwitch extends ToggleSwitch {

    public final Board board;
    public final Relay relay;
    public final boolean inverted;

    public RelayToggleSwitch(Board board, Relay relay, boolean inverted) {
        this.board = board;
        this.relay = relay;
        this.inverted = inverted;

        if (inverted) {
            this.setSelected(true);
        }
    }

    public Board getBoard() {
        return board;
    }

    public Relay getRelay() {
        return relay;
    }

    public boolean isInverted() {
        return inverted;
    }
}
