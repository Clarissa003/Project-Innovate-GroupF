
const int motorPin = 9;
void setup() {
  pinMode(motorPin, OUTPUT);
}
void loop() {
  digitalWrite(motorPin, HIGH);
  delay(1000); 
  // Turn the motor off
  digitalWrite(motorPin, LOW);
  delay(1000); // Wait for 1 second
}