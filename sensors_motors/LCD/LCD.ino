#include <TFT_eSPI.h>
#include <SPI.h>

TFT_eSPI tft = TFT_eSPI();

void setup(void) {

  tft.init();

}

void loop() {

  int get = 3;

  // if(get == 1){

    tft.fillScreen(TFT_WHITE);

    tft.setRotation(2);
    tft.setTextColor(TFT_MAGENTA);
    tft.setCursor(50, 220, 2);
    tft.setTextSize(2);
    tft.println("I am HAPPY"); 

    tft.setRotation(4);
    tft.setCursor(80, 150, 2);
    tft.setTextColor(TFT_MAGENTA);  
    tft.setTextSize(9);
    tft.println(".");

    tft.setRotation(4);
    tft.setCursor(113, 180, 2);
    tft.setTextColor(TFT_MAGENTA);  
    tft.setTextSize(2);
    tft.println("|");

    tft.setRotation(3);
    tft.setCursor(140, 40, 4);
    tft.setTextColor(TFT_MAGENTA);  
    tft.setTextSize(6);
    tft.println(")");

    tft.setRotation(4);
    tft.setCursor(140, 150, 2);
    tft.setTextColor(TFT_MAGENTA);  
    tft.setTextSize(9);
    tft.println(".");

    delay(3000);

  // }

  // if(get == 2){

    tft.fillScreen(TFT_WHITE);

    tft.setRotation(2);
    tft.setTextColor(TFT_CYAN);
    tft.setCursor(40, 220, 2);
    tft.setTextSize(2);
    tft.println("I need WATER");

    tft.setRotation(4);
    tft.setCursor(80, 150, 2);
    tft.setTextColor(TFT_CYAN);  
    tft.setTextSize(9);
    tft.println(".");

    tft.setRotation(4);
    tft.setCursor(113, 180, 2);
    tft.setTextColor(TFT_CYAN);  
    tft.setTextSize(2);
    tft.println("|");

    tft.setRotation(3);
    tft.setCursor(140, 40, 4);
    tft.setTextColor(TFT_CYAN);  
    tft.setTextSize(6);
    tft.println("(");

    tft.setRotation(4);
    tft.setCursor(140, 150, 2);
    tft.setTextColor(TFT_CYAN);  
    tft.setTextSize(9);
    tft.println(".");

    delay(3000);

  // }

  // if(get == 3){

    tft.fillScreen(TFT_WHITE);

    tft.setRotation(2);
    tft.setTextColor(TFT_BLUE);
    tft.setCursor(40, 220, 2);
    tft.setTextSize(2);
    tft.println("What the fuck");

    tft.setRotation(4);
    tft.setCursor(80, 150, 2);
    tft.setTextColor(TFT_BLUE);  
    tft.setTextSize(9);
    tft.println(".");

    tft.setRotation(4);
    tft.setCursor(113, 180, 2);
    tft.setTextColor(TFT_BLUE);  
    tft.setTextSize(2);
    tft.println("|");

    tft.setRotation(3);
    tft.setCursor(140, 55, 4);
    tft.setTextColor(TFT_BLUE);  
    tft.setTextSize(5);
    tft.println("|");

    tft.setRotation(4);
    tft.setCursor(140, 150, 2);
    tft.setTextColor(TFT_BLUE);  
    tft.setTextSize(9);
    tft.println(".");

    delay(3000);

  // }

}



