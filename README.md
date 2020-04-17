# Drum booth controller
JavaFX UI + Arduino project to control the lights and LED strips in a drum booth.

This project combines multiple examples of the book ["Getting started with Java on Raspberry Pi"](https://leanpub.com/gettingstartedwithjavaontheraspberrypi/). The sources of the examples in this book are freely available on [GitHub](https://github.com/FDelporte/JavaOnRaspberryPi). For more info see ["Drumbooth controller with Raspberry Pi and JavaFX"](https://webtechie.be/post/2020-03-30-drumbooth-controller-with-java-javafx-raspberrypi-arduino/).

![](images/ledstrips-rainbow-effect.jpg)

## Wiring

* USB cable between both boards (for serial communication)
* 5V and ground from power supply to Pi, Arduino and LED strips
* Control cable between pins 6, 7 and 8 to each of the three LED strips

![Wiring scheme](images/drumbooth-wiring.png)
![Installation](images/installation.jpg)

## Commands between Java and Arduino application

The commands shared between both boards are strings in the structure “COMMAND_ID:SPEED:R1:G1:B1:R2:G2:B2”, where the command ID is one of the following options:

![](images/led-effects.png)

## Java application

### JavaFX user interface with three screens

![Relays controller](images/screenshot-relays.png)
![LED strip controller](images/screenshot-ledstrips.png)
![Exit and shutdown](images/screenshot-exit.png)

### Undertow webpage to trigger some actions

![Web interface after selecting running light](images/web-running.png)
![eb interface after selecting "red alert"](images/web-redalert.png)

### Build

```
mvn clean package
```

### Running on Raspberry Pi

* Copy this file to the Pi: "drumbooth-0.0.1-jar-with-dependencies.jar"
* Run as with Java or with the included script which can also be used at startup of the Pi to automatically launch the application

```
java -jar /home/pi/DrumBoothController/JavaFXUserInterface/target/drumbooth-0.0.1-jar-with-dependencies.jar

OR

bash DrumBoothController/JavaFXUserInterface/scripts/start
```

## Arduino project
	
### Application

Controls three WS2812 LED strips with the same effect. Code is separated into multiple file so it's easier to understand and maintain.

### Inspired by
* [Music Reactive LED Strip ](https://create.arduino.cc/projecthub/buzzandy/music-reactive-led-strip-5645ed)

