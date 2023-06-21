#include <Arduino.h>

#define SerialUART Serial1

void setup() {
    Serial.begin(9600);
    SerialUART.begin(9600);
}

void loop() {
    if (SerialUART.available()) {
        String data = SerialUART.readStringUntil('Hallo');
        Serial.print("Received: ");
        Serial.println(data);
        SerialUART.println("Hallllo");
    }
}