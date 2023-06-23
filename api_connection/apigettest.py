# Import modules
import network #network connectivity
import machine
import utime
import time
import urequests
from easy_comms import Easy_comms
from time import sleep
from machine import Pin
import json

plant_id = 0 #identifies which plant type is in the pot
humidity_max = 0
humidity_min = 0
sunlight_max = 0
sunlight_min = 0
moisture_max = 0
moisture_min = 0
temperature_max = 0
temperature_min = 0
automatic_watering = "False"

wifi_ssid = 'samba1'
wifi_password = ''

# wifi_ssid = 'Ziggo-ap-4d4be42'
# wifi_password = 'wwfCJv8xnyvz'

# Connect to WLAN

def print_globals():
    print(plant_id, humidity_max, humidity_min, sunlight_max, sunlight_min, moisture_max, moisture_min,temperature_max, temperature_min, automatic_watering)
    
def connect_to_wifi():
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    wlan.connect(wifi_ssid, wifi_password)
    while not wlan.isconnected():
        pass
    print('Connected to WLAN')

def api_call():
    response = urequests.get('https://us-central1-potcast-plant.cloudfunctions.net/app/api/pots/read/1')
    data = response.json()
    print(data)
    
def api_get_boundary_parameters():
    response = urequests.get('https://us-central1-potcast-plant.cloudfunctions.net/app/api/plants/read/1')
    data = response.json()
    #write data to variables
    globals()["plant_id"] = data['id'] #identifies which plant type is in the pot
    globals()["humidity_max"] = data['humidityMax']
    globals()["humidity_min"] = data['humidityMin']
    globals()["sunlight_max"] = data['sunlightMax']
    globals()["sunlight_min"] = data['sunlightMin']
    globals()["moisture_max"] = data['moistureMax']
    globals()["moisture_min"] = data['moistureMin']
    globals()["temperature_max"] = data['temperatureMax']
    globals()["temperature_min"] = data['temperatureMax']
    

# Make an API call
# response = urequests.get('https://us-central1-potcast-plant.cloudfunctions.net/app/api/pots/read/2')
# data = response.json()
# print(data['sunlight'])

print_globals()
connect_to_wifi()
sleep(5)
api_get_boundary_parameters()
sleep(5)
print_globals()


