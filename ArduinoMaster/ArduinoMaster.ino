#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>

RF24 radio(9, 10); // CE, CSN         
const byte address[6] = "00001";     //Byte of array representing the address. This is the address where we will send the data. This should be same on the receiving side.

int potPinR = 0;
int potPinG = 1;
int potPinB = 2;

byte colorR = 0xff;
byte colorG = 0xff;
byte colorB = 0xff;

byte effect = 0x00;

void setup() {
  radio.begin();                  //Starting the Wireless communication
  radio.openWritingPipe(address); //Setting the address where we will send the data
  radio.setPALevel(RF24_PA_MIN);  //You can set it as minimum or maximum depending on the distance between the transmitter and receiver.
  radio.stopListening();          //This sets the module as transmitter
}

void loop() {
  // Read the selected color
  colorR = analogRead(potPinR);
  colorG = analogRead(potPinG);
  colorB = analogRead(potPinB);

  Serial.print("Colors: ");
  Serial.print(colorR, HEX);
  Serial.print("/");
  Serial.print(colorG, HEX);
  Serial.print("/");
  Serial.print(colorB, HEX);
  Serial.print(", effect: ");
  Serial.print(effect, HEX);
  Serial.println("");
  
  // Read the selected effect

  // Transmit the values
  radio.write(&colorR, sizeof(colorR));
  radio.write(&colorG, sizeof(colorG));
  radio.write(&colorB, sizeof(colorB));
  radio.write(&effect, sizeof(effect));

  // Put on the display

  
  delay(1000);
}
