package be.webtechie.drumbooth.relay;

import be.webtechie.drumbooth.relay.definition.Board;
import be.webtechie.drumbooth.relay.definition.Relay;
import be.webtechie.drumbooth.relay.definition.State;

/**
 * RelayCommand as it is exchanged with the relay board.
 */
public class RelayCommand {

    private final Board board;
    private final Relay relay;
    private final State state;

    /**
     * Initialize a relay command.
     *
     * @param board {@link Board}
     * @param relay {@link Relay}
     * @param state {@link State}
     */
    public RelayCommand(Board board, Relay relay, State state) {
        this.board = board;
        this.relay = relay;
        this.state = state;
    }

    public Board getBoard() {
        return board;
    }

    public Relay getRelay() {
        return relay;
    }

    public State getState() {
        return state;
    }
}
