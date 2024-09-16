package be.webtechie.drumbooth.relay;

import com.pi4j.io.gpio.digital.DigitalState;

public enum Relay {
    RELAY_1(5, DigitalState.HIGH),
    RELAY_2(6, DigitalState.HIGH),
    RELAY_3(13, DigitalState.LOW),
    RELAY_4(16, DigitalState.LOW),
    RELAY_5(19, DigitalState.LOW),
    RELAY_6(20, DigitalState.LOW);

    private final int bcm;
    private final DigitalState initialState;

    Relay(int bcm, DigitalState initialState) {
        this.bcm = bcm;
        this.initialState = initialState;
    }

    public int getBcm() {
        return this.bcm;
    }

    public DigitalState getInitialState() {
        return initialState;
    }
}
