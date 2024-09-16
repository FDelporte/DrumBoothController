package be.webtechie.drumbooth.ui.component;

import be.webtechie.drumbooth.relay.Relay;
import org.controlsfx.control.ToggleSwitch;

public class RelayToggleSwitch extends ToggleSwitch {

    public final Relay relay;

    public RelayToggleSwitch(Relay relay) {
        this.relay = relay;
    }

    public Relay getRelay() {
        return relay;
    }
}
