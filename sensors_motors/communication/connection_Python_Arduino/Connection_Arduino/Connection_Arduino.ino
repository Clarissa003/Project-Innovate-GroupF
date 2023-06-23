const int motorPin = 9;
void setup() {
  pinMode(motorPin, OUTPUT);
}

void loop() {
  // Turn the motor on
  digitalWrite(motorPin, HIGH);
  delay(1000); // Wait for 1 second
  // Turn the motor off
  digitalWrite(motorPin, LOW);
  delay(1000);
}