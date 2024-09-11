package be.webtechie.drumbooth.ui.component;

import be.webtechie.drumbooth.relay.definition.Relay;
import org.controlsfx.control.ToggleSwitch;

public class RelayToggleSwitch extends ToggleSwitch {

    public final Relay relay;
    public final boolean inverted;

    public RelayToggleSwitch(Relay relay, boolean inverted) {
        this.relay = relay;
        this.inverted = inverted;

        if (inverted) {
            this.setSelected(true);
        }
    }

    public Relay getRelay() {
        return relay;
    }

    public boolean isInverted() {
        return inverted;
    }
}
