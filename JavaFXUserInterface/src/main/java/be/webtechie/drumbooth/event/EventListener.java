package be.webtechie.drumbooth.event;

import be.webtechie.drumbooth.relay.RelayCommand;
import be.webtechie.drumbooth.led.LedCommand;

public interface EventListener {

    /**
     * Whenever a new {@link LedCommand} is pushed from one of the components,
     * all listeners will be notified to handle it for their own use.
     *
     * @param ledCommand {@link LedCommand}
     */
    void onLedStripChange(LedCommand ledCommand);

    /**
     * Whenever a new {@link RelayCommand} is pushed from one of the components,
     * all listeners will be notified to handle it for their own use.
     *
     * @param relayCommand {@link RelayCommand}
     */
    void onRelayChange(RelayCommand relayCommand);
}
