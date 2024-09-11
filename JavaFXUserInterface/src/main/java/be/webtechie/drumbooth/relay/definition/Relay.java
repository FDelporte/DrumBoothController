package be.webtechie.drumbooth.relay.definition;

public enum Relay {
    RELAY_1(5),
    RELAY_2(6),
    RELAY_3(13),
    RELAY_4(16),
    RELAY_5(19),
    RELAY_6(20);

    private final int bcm;

    Relay(int bcm) {
        this.bcm = bcm;
    }

    public int getBcm() {
        return this.bcm;
    }
}
