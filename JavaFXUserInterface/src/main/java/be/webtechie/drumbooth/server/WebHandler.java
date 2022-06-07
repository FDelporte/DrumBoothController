package be.webtechie.drumbooth.server;

import be.webtechie.drumbooth.event.EventManager;
import be.webtechie.drumbooth.relay.RelayCommand;
import be.webtechie.drumbooth.relay.definition.Board;
import be.webtechie.drumbooth.relay.definition.Relay;
import be.webtechie.drumbooth.relay.definition.State;
import be.webtechie.drumbooth.led.LedCommand;
import be.webtechie.drumbooth.led.LedEffect;
import io.undertow.io.Sender;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import javafx.scene.paint.Color;

/**
 * Handler for the internal Undertow webserver.
 */
public class WebHandler implements HttpHandler {

    private final EventManager eventManager;

    /**
     * Constructor
     *
     * @param eventManager {@link EventManager}
     */
    public WebHandler(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * Whenever a request is received by Undertow webserver, this handler will be called.
     *
     * @param exchange {@link HttpServerExchange}
     */
    @Override
    public void handleRequest(HttpServerExchange exchange) {
        String path = exchange.getRequestPath();

        switch (path) {
            case "/red-alert":
                // All relays off
                this.eventManager.setAllOff();
                // Relay ON as state of TL is inverted
                this.eventManager.sendRelayCommand(new RelayCommand(Board.BOARD_1, Relay.RELAY_1, State.STATE_ON));
                // Blinking red on LED strips
                this.eventManager.sendSerialCommand(new LedCommand(LedEffect.BLINKING, 50, Color.RED, Color.WHITE));
                this.returnSuccess(exchange, "RED ALERT message has been sent");
                break;
            case "/all-white":
                // All relays off = TL on
                this.eventManager.setAllOff();
                // LED strips full white
                this.eventManager.sendSerialCommand(new LedCommand(LedEffect.ALL_WHITE, 10, Color.WHITE, Color.BLACK));
                this.returnSuccess(exchange, "ALL WHITE message has been sent");
                break;
            case "/all-out":
                // All relays off
                this.eventManager.setAllOff();
                // Relay ON as state of TL is inverted
                this.eventManager.sendRelayCommand(new RelayCommand(Board.BOARD_1, Relay.RELAY_1, State.STATE_ON));
                // LED Strips off
                this.eventManager.sendSerialCommand(new LedCommand(LedEffect.ALL_OUT, 10, Color.BLACK, Color.BLACK));
                this.returnSuccess(exchange, "ALL OUT message has been sent");
                break;
            default:
                this.returnError(exchange, StatusCodes.NOT_FOUND, "The requested path is not available");
                break;
        }
    }

    /**
     * Return a success page.
     *
     * @param exchange {@link HttpServerExchange}
     * @param message The message to be shown on the page
     */
    private void returnSuccess(HttpServerExchange exchange, String message) {
        this.returnPage(exchange, StatusCodes.OK, "LED command handled", message);
    }

    /**
     * Return an error page.
     *
     * @param exchange {@link HttpServerExchange}
     * @param statusCode HTTP status code
     * @param message The message to be shown on the page
     */
    private void returnError(HttpServerExchange exchange, int statusCode, String message) {
        this.returnPage(exchange, statusCode, "Error", message);
    }

    /**
     * Create and return the page.
     *
     * @param exchange {@link HttpServerExchange}
     * @param statusCode HTTP status code
     * @param title Title of the page
     * @param content Content of the page
     */
    private void returnPage(HttpServerExchange exchange, int statusCode, String title, String content) {
        StringBuilder lastLedCommandOutput = new StringBuilder();
        LedCommand lastLedCommand = this.eventManager.getLastLedCommand();
        if (lastLedCommand != null) {
            lastLedCommandOutput.append("Last effect: ").append(lastLedCommand.getLedEffect().name()).append("<br/>");

            if (lastLedCommand.getLedEffect().useColor1()) {
                lastLedCommandOutput
                        .append("<span style='color: ")
                        .append(lastLedCommand.getColor1AsHexString())
                        .append(";'>Color 1: ")
                        .append(lastLedCommand.getColor1AsHexString())
                        .append("</span><br/>");
            }
            if (lastLedCommand.getLedEffect().useColor2()) {
                lastLedCommandOutput
                        .append("<span style='color: ")
                        .append(lastLedCommand.getColor2AsHexString())
                        .append(";'>Color 1: ")
                        .append(lastLedCommand.getColor2AsHexString())
                        .append("</span><br/>");

            }
            if (lastLedCommand.getLedEffect().useSpeed()) {
                lastLedCommandOutput.append("Speed: ").append(lastLedCommand.getSpeed()).append("<br/>");
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n")
                .append("<html>\n")
                .append("   <head>\n")
                .append("       <title>").append(title).append("</title>\n")
                .append("       <style>\n")
                .append("           body {\n")
                .append("               font-family: sans-serif; font-size: 14px; background: #133353; color: #ffffff;\n")
                .append("           }\n")
                .append("           a {\n")
                .append("               color: #ffffff; font-size: 18px; font-weight: bold;\n")
                .append("           }\n")
                .append("           ul {\n")
                .append("               list-style: none;  margin 0 0 20px  0; padding: 0;\n")
                .append("           }\n")
                .append("           li {\n")
                .append("               margin: 0 20px 0 0; display: inline;\n")
                .append("           }\n")
                .append("       </style>\n")
                .append("   </head>\n")
                .append("   <body>\n")
                .append("       <ul>\n")
                .append("           <li><a href='/red-alert'>RED ALERT</a></li>\n")
                .append("           <li><a href='/all-white'>ALL WHITE</a></li>\n")
                .append("           <li><a href='/all-out'>ALL OUT</a></li>\n")
                .append("       </ul>\n")
                .append("       ").append(content).append("\n")
                .append("       <br/><br/>")
                .append(lastLedCommandOutput)
                .append("   </body>\n ")
                .append("</html>");
        exchange.getResponseHeaders().put(Headers.CONTENT_LENGTH, sb.toString().length());
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
        Sender sender = exchange.getResponseSender();
        exchange.setStatusCode(statusCode);
        sender.send(sb.toString());
    }
}
