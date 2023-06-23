import machine
import utime

# Configure GPIO pin
motor_pin = machine.Pin(16, machine.Pin.OUT)

# Turn the motor on and off in a loop
while True:
    motor_pin.on()  # Turn the motor on
    utime.sleep(1)  # Wait for 1 second
    motor_pin.off()  # Turn the motor off
    utime.sleep(1)  # Wait for 1 second