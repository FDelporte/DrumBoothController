#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>
#include <FastLED.h>

// Test messages: 
// 1:25:0:127:0
// 2:5:255:0:0:0:0:250
// 3:25:255:0:0:0:0:250
// 4:5:255:0:0:0:0:250
// 5:5
// 6

// Define the number of LEDS in your strip here
const int numberOfLeds = 11;

// Variables used by the code to handle the incoming LED commands
char input[50];
int incomingByte = 0;

// Variables defined by the incoming LED commands
byte commandId = 0;
int animationSpeed = 10;
byte r1 = 0;
byte g1 = 0;
byte b1 = 0;
byte r2 = 0;
byte g2 = 0;
byte b2 = 0;

int currentLoop = 0;

// Radio settings
RF24 radio(9, 10); // CE, CSN
const byte address[6] = "00001";     //Byte of array representing the address. This is the address where we will send the data. This should be same on the receiving side.

// Pin definitions
#define MASTER_PIN        10
#define LED_PIN           6

#define ANALOG_PIN_POT_R  0
#define ANALOG_PIN_POT_G  1
#define ANALOG_PIN_POT_B  2

// Master/Slave selection flag
boolean isSlave = true;

void setup() {
  // Configure serial speed and wait till it is available
  // This is used to output logging info and can receive LED commands
  Serial.begin(9600);  
  while(!Serial){
    ; // Wait till serial is available
  }

  // Initialize the leds
  initLeds();

  // Initialize the wireless connection
  initWireless();

  // Set the initial LED effect
  String message = "6:";
  message.toCharArray(input, 50);
}

void loop() {
  checkSerial();
  handleRadio();  
  handleMessage();

  currentLoop++;

  // Only do LED effect when loop exceeds the defined animationSpeed
  if (currentLoop >= animationSpeed) {
    // Depending on the commandId, call the correct LED effect
    if (commandId == 1) {
      setStaticColor();
    } else if (commandId == 2) {
      setStaticFade();
    } else if (commandId == 3) {
      setBlinking(); 
    } else if (commandId == 4) {
      setRunningLight(); 
    } else if (commandId == 5) {
      setFadingRainbow(); 
    } else if (commandId == 6) {
      setStaticRainbow(); 
    } else if (commandId == 255) {
      clearLeds();
    }

    currentLoop = 0;
  }
}
