# DrumLights
Arduino project with
* one as master with control elements
* multiple wireless slaves with RGB led lights

## One Arduino code for multiple Arduino boards

### One master board
* Switch to configure as master board
* 3 pot meters for R/G/B mixing
* Radio to transmit selected color codes to the slaves
* Controls own RGB led strip

### Multiple slave boards
* Radio to receive selected color codes from the master
* Control of RGB led strip according to received color code and effect

## Wiring
Slave is identical to master for radio and led strip, but doesn't have the master switch and potentio meters.

![Master Arduino](Wiring/wirings_bb.jpg)
	
## Components

### LED Strip
* WS2812IC 
* https://www.dx.com/p/5mroll-DC5V-ws2812b-150led-300led-ws2812IC-builtin-Individually-Addressable-3060ledsm-5050-RGB-Dream-Color-LED-Strip-Light-5m-60led-white-IP65-Changeable-2067924

### Wireless
* NRF24l01
* https://www.dx.com/p/nrf24l01-2-4ghz-enhanced-wireless-modules-black-4-pcs-2057721

## Libraries to install in Arduino IDE
* FastLED by Daniel Garcia
* RF24 by TMRh20

## Inspired by
* https://randomnerdtutorials.com/guide-for-ws2812b-addressable-rgb-led-strip-with-arduino/

### Organizing your Arduino code
* [Multiple source files in an Arduino project](https://subethasoftware.com/2013/04/10/multiple-source-files-in-an-arduino-project/)

### Arduino and WS2812 addressable RGH
* [Guide for WS2812B Addressable RGB LED Strip with Arduino](https://randomnerdtutorials.com/guide-for-ws2812b-addressable-rgb-led-strip-with-arduino/)

### Arduino and NRF24L01 wireless
* [Arduino Wireless Communication – NRF24L01 Tutorial](https://howtomechatronics.com/tutorials/arduino/arduino-wireless-communication-nrf24l01-tutorial/)
* [nRF24L01 Interfacing with Arduino | Wireless Communication ](https://create.arduino.cc/projecthub/muhammad-aqib/nrf24l01-interfacing-with-arduino-wireless-communication-0c13d4?ref=tag&ref_id=wireless&offset=10)
* [NRFLite](https://github.com/dparson55/NRFLite)

### Arduino and Mosquitto
* [MQTT Tutorial for Raspberry Pi, Arduino, and ESP8266](https://www.baldengineer.com/mqtt-tutorial.html)


