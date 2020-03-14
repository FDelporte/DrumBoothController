# Drum booth controller
JavaFX UI + Arduino project to control the lights in a drum booth.

This project combines multiple examples of the book ["Getting started with Java on Raspberry Pi"](https://leanpub.com/gettingstartedwithjavaontheraspberrypi/). The sources of the examples in this book are freely available on [GitHub](https://github.com/FDelporte/JavaOnRaspberryPi).

## JavaFX

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

### LED Strip
* WS2812IC 
* https://www.dx.com/p/5mroll-DC5V-ws2812b-150led-300led-ws2812IC-builtin-Individually-Addressable-3060ledsm-5050-RGB-Dream-Color-LED-Strip-Light-5m-60led-white-IP65-Changeable-2067924

