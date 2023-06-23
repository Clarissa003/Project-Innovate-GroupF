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

# wifi_ssid = 'samba1'
# wifi_password = ''



def print_globals():
    print(plant_id, humidity_max, humidity_min, sunlight_max, sunlight_min, moisture_max, moisture_min,temperature_max, temperature_min, automatic_watering)

# Connect to WLAN
def connect_to_wifi():
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    wlan.connect(wifi_ssid, wifi_password)
    while not wlan.isconnected():
        pass
    print('Connected to WLAN')
 
def handle_get_request(url):
    response = urequests.get(url)  # Disable SSL/TLS encryption
    if response.status_code == 200:
        return response.json()
    else:
        print("Error: API request failed")
        return None

# # Gets boundary parameters from the API, as assigned by the mobile application
def api_get_boundary_parameters():
    url = 'https://us-central1-potcast-plant.cloudfunctions.net/app/api/plants/read/1'
    data = handle_get_request(url)
    if data is not None:
        globals().update({
            "plant_id": data['id'],
            "humidity_max": data['humidityMax'],
            "humidity_min": data['humidityMin'],
            "sunlight_max": data['sunlightMax'],
            "sunlight_min": data['sunlightMin'],
            "moisture_max": data['moistureMax'],
            "moisture_min": data['moistureMin'],
            "temperature_max": data['temperatureMax'],
            "temperature_min": data['temperatureMin']
        })

# Gets watering boolean value from the API
def api_get_watering_boolean():
    url = 'https://us-central1-potcast-plant.cloudfunctions.net/app/api/pots/read/1'
    data = handle_get_request(url)
    if data is not None:
        globals().update({
            "automatic_watering": data['automaticWatering'],
        })

def main():
    print_globals()
    connect_to_wifi()
    utime.sleep_ms(5000)
    api_get_boundary_parameters()
    api_get_watering_boolean()  # Call api_get_watering_boolean() after api_get_boundary_parameters()
    utime.sleep_ms(5000)
    print_globals()

if __name__ == "__main__":
    main()
