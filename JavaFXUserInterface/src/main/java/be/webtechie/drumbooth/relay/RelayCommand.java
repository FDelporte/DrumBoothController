package be.webtechie.drumbooth.relay;

import be.webtechie.drumbooth.relay.definition.Relay;
import be.webtechie.drumbooth.relay.definition.State;

/**
 * RelayCommand as it is exchanged with the relay board.
 */
public record RelayCommand(Relay relay, State state) {}