void initWireless() {
  pinMode(MASTER_PIN, INPUT_PULLUP);   
  Serial.print("This board is ");
  if (digitalRead(MASTER_PIN) == HIGH) {
    isSlave = false;
    Serial.println("SLAVE");
  } else {
    Serial.println("MASTER");
  }

  // Start the radio
  radio.begin();                  //Starting the Wireless communication

  if (isSlave) {
    radio.openReadingPipe(0, address);   //Setting the address at which we will receive the data
    radio.setPALevel(RF24_PA_MIN);       //You can set this as minimum or maximum depending on the distance between the transmitter and receiver.
    radio.startListening();              //This sets the module as receiver
  } else {
    radio.openWritingPipe(address); //Setting the address where we will send the data
    radio.setPALevel(RF24_PA_MIN);  //You can set it as minimum or maximum depending on the distance between the transmitter and receiver.
    radio.stopListening();          //This sets the module as transmitter
  }
}

void handleRadio() {
  if (isSlave) {
    receiveValuesFromRadio();
  } else {
    sendValuesFromInputsOverRadio();
  }
}

void sendValuesFromInputsOverRadio() {
  // Read the selected effect
  // TODO  
  
  // Read the selected colors
  // TODO  

  // Send command over radio (only when changed)
  // command = 4:5:255:0:0:0:0:250
  // TODO radio.write(&command, sizeof(command));
}

void receiveValuesFromRadio() {
  if (radio.available()) {
    // Read input command from radio
    // TODO radio.readData(&input, sizeof(command));
  }
}
