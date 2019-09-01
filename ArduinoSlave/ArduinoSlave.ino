#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>

RF24 radio(9, 10); // CE, CSN
const byte address[6] = "00001";

byte colorR = 0xff;
byte colorG = 0xff;
byte colorB = 0xff;

byte effect = 0x00;

void setup() {
  pinMode(6, OUTPUT);
  Serial.begin(9600);
  radio.begin();
  radio.openReadingPipe(0, address);   //Setting the address at which we will receive the data
  radio.setPALevel(RF24_PA_MIN);       //You can set this as minimum or maximum depending on the distance between the transmitter and receiver.
  radio.startListening();              //This sets the module as receiver
}

void loop() {
  if (radio.available()) {
    //Looking for the data.
    radio.read(&colorR, sizeof(colorR));
    radio.read(&colorG, sizeof(colorG));
    radio.read(&colorB, sizeof(colorB));
    radio.read(&effect, sizeof(effect));

    Serial.print("Colors: ");
    Serial.print(colorR, HEX);
    Serial.print("/");
    Serial.print(colorG, HEX);
    Serial.print("/");
    Serial.print(colorB, HEX);
    Serial.print(", effect: ");
    Serial.print(effect, HEX);
    Serial.println("");

    // TODO set the RBG led output
  }
  
  delay(5);
} 
