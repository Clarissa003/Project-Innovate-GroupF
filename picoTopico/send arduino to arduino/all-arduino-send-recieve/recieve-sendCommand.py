import machine
import time

uart = machine.UART(0, baudrate=9600, tx=machine.Pin(0), rx=machine.Pin(1))  # Adjust the UART pins as per your connections

while True:
    if uart.any():
        data = uart.read()
        print("Received:", data.decode())
    uart.write("light")
    time.sleep(5)
    uart.write("water")
    time.sleep(5)