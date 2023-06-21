#include <Wire.h>
#include "Si115X.h"
#include <Arduino.h>
#define SerialUART Serial1
Si115X si1151;

#ifdef ARDUINO_SAMD_VARIANT_COMPLIANCE
#define SERIAL SerialUSB
#else
#define SERIAL Serial
#endif

unsigned char low_data[8] = {0};
unsigned char high_data[12] = {0};

#define NO_TOUCH       0xFE
#define THRESHOLD      100
#define ATTINY1_HIGH_ADDR   0x78
#define ATTINY2_LOW_ADDR   0x77

void setup() {

  uint8_t conf[4];
  Serial.begin(9600);
  SERIAL.begin(115200);
  SerialUART.begin(9600);
  Wire.begin();

  if (!si1151.Begin())
      Serial.println("Si1151 is not ready!");
  else
      Serial.println("Si1151 is ready!");

}

void loop(){
  waterLevel();
  // lightSen();

}

void lightSen(){
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


void getHigh12SectionValue(void){
  memset(high_data, 0, sizeof(high_data));
  Wire.requestFrom(ATTINY1_HIGH_ADDR, 12);
  while (12 != Wire.available());

  for (int i = 0; i < 12; i++) {
    high_data[i] = Wire.read();
  }
  delay(10);
}

void getLow8SectionValue(void){
  memset(low_data, 0, sizeof(low_data));
  Wire.requestFrom(ATTINY2_LOW_ADDR, 8);
  while (8 != Wire.available());

  for (int i = 0; i < 8 ; i++) {
    low_data[i] = Wire.read(); // receive a byte as character
  }
  delay(10);
}

void waterLevel(){
  int sensorvalue_min = 250;
  int sensorvalue_max = 255;
  int low_count = 0;
  int high_count = 0;
  while (1){
    uint32_t touch_val = 0;
    uint8_t trig_section = 0;
    low_count = 0;
    high_count = 0;
    getLow8SectionValue();
    getHigh12SectionValue();

    for (int i = 0 ; i < 8; i++) {
      if (low_data[i] > THRESHOLD) {
        touch_val |= 1 << i;

      }
    }
    for (int i = 0 ; i < 12; i++) {
      if (high_data[i] > THRESHOLD) {
        touch_val |= (uint32_t)1 << (8 + i);
      }
    }

    while (touch_val & 0x01){
      trig_section++;
      touch_val >>= 1;
    }
    SERIAL.println(trig_section * 5);
    delay(1000);
  }
}