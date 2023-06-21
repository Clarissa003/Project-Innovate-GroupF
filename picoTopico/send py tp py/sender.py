# led_server

from easy_comms import Easy_comms
from time import sleep
from machine import Pin
import json
import allPythonSensors

com1 = Easy_comms(0,9600)

def tempAndHumidity():

    while True:
        output = allPythonSensors.humidity()
        command = {output}
        com1.send(str(json.dumps(command)))
        sleep(10)

def motor():

    while True:
        output = allPythonSensors.motor()
        command = {output}
        com1.send(str(json.dumps(command)))
        sleep(10)
    
def soilmoisture():
    
    while True:
        output = allPythonSensors.soilmoisture()
        command = {output}
        com1.send(str(json.dumps(command)))
        sleep(10)
        
soilmoisture()
#tempAndHumidity()
