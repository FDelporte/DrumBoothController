#include <Adafruit_NeoPixel.h>

// Test messages: 
// 1:25:0:127:0
// 2:5:255:0:0:0:0:250
// 3:25:255:0:0:0:0:250
// 4:10:255:0:0:0:0:250
// 5:50
// 6
// 98
// 99

// Define the number of LEDS in your strip here
#define PIN1 6
#define PIN2 7
#define PIN3 8
#define NUMBER_OF_LEDS 11

// Variables used by the code to handle the incoming LED commands
char input[50];
int incomingByte = 0;

// Variables defined by the incoming LED commands
byte commandId = 0;
int animationSpeed = 10;
uint32_t rgb1 = 0;
uint32_t rgb2 = 0;

int currentLoop = 0;

// LED strip initialization
// Parameter 1 = number of pixels in strip
// Parameter 2 = Arduino pin number (most are valid)
// Parameter 3 = pixel type flags, add together as needed:
//   NEO_KHZ800  800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
//   NEO_KHZ400  400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
//   NEO_GRB     Pixels are wired for GRB bitstream (most NeoPixel products)
//   NEO_RGB     Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)
//   NEO_RGBW    Pixels are wired for RGBW bitstream (NeoPixel RGBW products)
Adafruit_NeoPixel strip1 = Adafruit_NeoPixel(NUMBER_OF_LEDS, PIN1, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel strip2 = Adafruit_NeoPixel(NUMBER_OF_LEDS, PIN2, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel strip3 = Adafruit_NeoPixel(NUMBER_OF_LEDS, PIN3, NEO_GRB + NEO_KHZ800);

// Code executed at startup of the Arduino board
void setup() {
  // Configure serial speed and wait till it is available
  // This is used to output logging info and can receive LED commands
  Serial.begin(115200);  
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  // Initialize the leds
  initLeds();
  
  // Set the initial LED effect
  String message = "1:10:10:10";
  message.toCharArray(input, 50);
}

void loop() {  
  checkSerial();
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
    } else if (commandId == 98) {
      rgb1 = strip1.Color(255, 255, 255);
      setStaticColor();
    } else if (commandId == 99) {
      clearLeds();
    } else {
      Serial.print("Command ");
      Serial.print(commandId);
      Serial.println(" is not implemented");
    }

    currentLoop = 0;
  }

  delay(5);
}
