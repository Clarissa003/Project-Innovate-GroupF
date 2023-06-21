from easy_comms import Easy_comms
from time import sleep
from machine import Pin
import json

com1 = Easy_comms(0,9600)

while True:
    message = com1.read()
    if message is not None:
        print(f'message: {message}')
        try:
            command = json.loads(message)
            print(message)
        except Exception as e:
            print(f'error: {e}')