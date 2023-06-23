#include <Wire.h>
#include "Si115X.h"
#include <Arduino.h>
#include "DHT.h"
#define DHTTYPE DHT20
#define SerialUART Serial1
Si115X si1151;
DHT dht(DHTTYPE);

#ifdef ARDUINO_SAMD_VARIANT_COMPLIANCE
#define SERIAL SerialUSB
#else
#define SERIAL Serial
#endif

#if defined(ARDUINO_ARCH_AVR)
    #define debug  Serial

#elif defined(ARDUINO_ARCH_SAMD) ||  defined(ARDUINO_ARCH_SAM)
    #define debug  SerialUSB
#else
    #define debug  Serial
#endif

unsigned char low_data[8] = {0};
unsigned char high_data[12] = {0};

const int motorPin = 16;

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
  debug.begin(115200);
  debug.println("DHTxx test!");
  dht.begin();

  if (!si1151.Begin())
      Serial.println("Si1151 is not ready!");
  else
      Serial.println("Si1151 is ready!");

  pinMode(motorPin, OUTPUT);

}

void loop(){
  digitalWrite(motorPin, LOW);
  soilPump();
  if (SerialUART.available()) {
    String data = SerialUART.readStringUntil('\n');
    // Serial.print("Received: ");
    // Serial.println(data);
    if (data == String(1)){
      digitalWrite(motorPin, HIGH);
    }else if (data == String(2)){
      digitalWrite(motorPin, LOW);
    }else if (data == String(3)){
      SerialUART.println(moisture());
    }else if(data == String(4)){
    SerialUART.println(temp());
    }else if(data == String(5)){
      SerialUART.println(hum());
    }else if(data == String(6)){
      SerialUART.println(waterLevel());
    }else if(data == String(7)){
      SerialUART.println(lightSen());
    }
  }
}

void soilPump(){
  if(moisture() > 20 && moisture() < 40){
    digitalWrite(motorPin, HIGH);
  }else{
    digitalWrite(motorPin, LOW);
  }
}


int temp(){
    float temp_hum_val[2] = {0};
    if (!dht.readTempAndHumidity(temp_hum_val)) {
        // debug.print("Humidity: ");
        // debug.print(temp_hum_val[0]);
        // debug.print(" %\t");
        // debug.print("Temperature: ");
        // debug.print(temp_hum_val[1]);
        // debug.println(" *C");
        // double temp = temp_hum_val[1];
        // double hum = temp_hum_val[0];
        // return double("Humidity: " + hum + "Temperature: " + temp);
        return int(temp_hum_val[1]);
    } else {
        debug.println("Failed to get temprature and humidity value.");
    }
}
int hum(){
    float temp_hum_val[2] = {0};
    if (!dht.readTempAndHumidity(temp_hum_val)) {
        return int(temp_hum_val[0]);
    } else {
        debug.println("Failed to get temprature and humidity value.");
    }
}

int lightSen(){
  // String ir = String(si1151.ReadHalfWord());
  int vi = int(si1151.ReadHalfWord_VISIBLE());
  // String uv = String(si1151.ReadHalfWord_UV());
  // String all = String("IR: " +  ir + " VISIBLE: " + vi + " UV: " + uv );
  return vi;
}

int moisture() {
  int max_moisture = 1000;
  int moisture_raw = analogRead(A0);
  return (max_moisture - moisture_raw) / 10;
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
    low_data[i] = Wire.read();
  }
  delay(10);
}

String waterLevel(){
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
    String ST = String(trig_section * 5);
    return ST;
    delay(1000);
  }
}
