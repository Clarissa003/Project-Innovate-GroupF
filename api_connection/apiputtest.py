import network
import urequests
import json
import machine
import utime

humidity = 0
sunlight = 0
moisture = 0
temperature = 0
water_level = 0

wifi_ssid = 'asdf'
wifi_password = 'awwm715#'

def connect_to_wifi():
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    wlan.connect(wifi_ssid, wifi_password)
    while not wlan.isconnected():
        pass
    print('Connected to WLAN')

def receive_send(value):
    uart = machine.UART(0, baudrate=9600, tx=machine.Pin(0), rx=machine.Pin(1))  # Adjust the UART pins as per your connections
    uart.write(str(value))
    while True:
        if uart.any():
            data = uart.read()
            if value == 3:
                globals().update({"soil_moisture": data.decode()})
            elif value == 4:
                globals().update({"temperature": data.decode()})
            elif value == 5:
                globals().update({"humidity": data.decode()})
            elif value == 6:
                globals().update({"water_level": data.decode()})
            elif value == 7:
                globals().update({"sunlight": data.decode()})
            print("Received:", data.decode())
            break

def api_send_data():
    data = {
        'temperature': int(temperature),
        'moisture': int(moisture),
        'waterlevel': int(water_level),
        'humidity': int(humidity),
    }
    json_data = json.dumps(data)  # Convert data to JSON
    data_bytes = json_data.encode('utf-8')

    content_length = len(data_bytes)  # Calculate content length
    headers = {
        'Content-Type': 'application/json',
        'Content-Length': str(content_length)
    }

    response = urequests.put('https://us-central1-potcast-plant.cloudfunctions.net/app/api/pots/update/1',
                             headers=headers,
                             data=data_bytes)
    print(response.text)
    print('Successfully sent data to endpoint')

while True:
    connect_to_wifi()

    # Retrieve sensor data using receive_send() function
    receive_send(3)  # soil moisture
    receive_send(4)  # temperature
    receive_send(5)  # humidity
    receive_send(6)  # water level
    receive_send(7)  # sunlight

    # Send the data to the API
    api_send_data()
    
    utime.sleep(120)
