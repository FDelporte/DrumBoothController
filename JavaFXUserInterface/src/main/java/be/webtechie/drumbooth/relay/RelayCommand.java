package be.webtechie.drumbooth.relay;

import com.pi4j.io.gpio.digital.DigitalState;

/**
 * RelayCommand as it is exchanged with the relay board.
 */
public record RelayCommand(Relay relay, DigitalState state) {}