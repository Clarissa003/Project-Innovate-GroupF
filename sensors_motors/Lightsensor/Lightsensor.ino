#include "Si115X.h"
#include <Arduino.h>
#define SerialUART Serial1
Si115X si1151;

/**
 * Setup for configuration
 */
void setup()
{
    uint8_t conf[4];

    Wire.begin();
    Serial.begin(9600);
    SerialUART.begin(9600);
    if (!si1151.Begin())
        Serial.println("Si1151 is not ready!");
    else
        Serial.println("Si1151 is ready!");

}

/**
 * Loops and reads data from registers
 */
void loop()
{
  // if (SerialUART.available()) {
    String data = SerialUART.readStringUntil('\n');
    Serial.println("IR: ");
    Serial.println(si1151.ReadHalfWord());
    Serial.println("VISIBLE: ");
    Serial.println(si1151.ReadHalfWord_VISIBLE());
    Serial.println("UV: ");
    Serial.println(si1151.ReadHalfWord_UV());
  // }
}