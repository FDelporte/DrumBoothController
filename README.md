# Drum booth controller

JavaFX UI running on Raspberry Pi with relays and a serial connection to an Arduino to control the lights and LED strips in a drum booth.

This project combines multiple examples of the book ["Getting started with Java on Raspberry Pi"](https://leanpub.com/gettingstartedwithjavaontheraspberrypi/). All the sources of the examples in this book are freely available on [GitHub](https://github.com/FDelporte/JavaOnRaspberryPi). 

![LED strips on the ceiling of the drum booth](images/ledstrips-rainbow-effect.jpg)

## History of this Project

As Java, JavaFX, and the used hardware has evolved, the main branch deviated from the original blog post.

### 202409 Waveshare Raspberry Pi Zero


### 202003 Raspberry Pi with I2C stack-on relays boards

Full description is available in this blog post: ["Drumbooth controller with Raspberry Pi and JavaFX"](https://webtechie.be/post/2020-03-30-drumbooth-controller-with-java-javafx-raspberrypi-arduino/).

If you need the sources of this version, please check-out the tag [i2c-relays](https://github.com/FDelporte/DrumBoothController/releases/tag/i2c-relays).

## Wiring

* Shared
  * USB cable between both boards (for serial communication)
  * 5V and ground from power supply to Pi, Arduino and LED strips
* Raspberry Pi
  * Raspberry Pi Zero 2 W
  * Mounted in a [Waveshare Industrial 6-ch Relay Module for Raspberry Pi Zero, RS485/CAN, Isolated Protections](https://www.waveshare.com/product/raspberry-pi/boards-kits/raspberry-pi-zero-2-w-cat/rpi-zero-relay.htm)
* Arduino
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

Build and copy the files to your Raspberry Pi

```shell
$ mvn clean package
$ scp target/drumbooth-0.0.1 pi@drumbooth.local://home/pi/drumbooth
$ scp scripts/start.sh pi@drumbooth.local://home/pi/drumbooth
```

### Running on Raspberry Pi

* Install Pi4J v1 and WiringPi

```shell
$ curl -sSL https://pi4j.com/install | sudo bash
$ sudo pi4j --wiringpi
```

* Copy this file to the Pi: "drumbooth-0.0.1-jar-with-dependencies.jar"
* Install Java 11 on the Raspberry Pi if needed (with SDKMAN), we must use `sudo sdk install` to make the Java available for the sudo-user that also needs to be used to start the Pi4J application.

```shell
$ sudo apt install zip
$ curl -s "https://get.sdkman.io" | bash
$ sudo sdk install java 11.0.13-zulu
```

* Download the JavaFX runtime

```shell
$ wget -O openjfx.zip https://gluonhq.com/download/javafx-17-ea-sdk-linux-arm32/
$ unzip openjfx.zip
$ sudo mv javafx-sdk-17/ /opt/javafx-sdk-17/
```

* Run with the included script which can also be used at startup of the Pi to automatically launch the application

```
sudo bash /home/pi/drumbooth/start.sh
```

### TFT screen


Settings to be added to config.txt

```text
max_usb_current=1
hdmi_force_hotplug=1
config_hdmi_boost=10
hdmi_group=2
hdmi_mode=87
hdmi_cvt 1024 600 60 0 0 0
```

## Arduino project

http://wiki.openmusiclabs.com/wiki/ArduinoFHT
http://wiki.openmusiclabs.com/wiki/FHTExample
	
### Application

Controls three WS2812 LED strips with the same effect. Code is separated into multiple file so it's easier to understand and maintain.

### Inspired by

* [Music Reactive LED Strip ](https://create.arduino.cc/projecthub/buzzandy/music-reactive-led-strip-5645ed)

