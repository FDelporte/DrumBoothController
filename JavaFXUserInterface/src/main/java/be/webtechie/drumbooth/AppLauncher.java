package be.webtechie.drumbooth;

/**
 * The AppLauncher class provides the main entry point
 * for launching the application.
 */
public class AppLauncher {

    public static void main(String[] args) {
        var app = new DrumBoothController();
        app.run();
    }
}
