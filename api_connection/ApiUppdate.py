# Import modules
import network
import urequests
import json
import utime

humidity = 1
sunlight = 8
moisture = 3
temperature = 4
water_level = 5

# wifi_ssid = '0000'
# wifi_password = '0000'

# wifi_ssid = 'SAMBA'
# wifi_password = ''

# wifi_ssid = 'Ziggo-ap-4d4be42'
# wifi_password = 'wwfCJv8xnyvz'

wifi_ssid = 'asdf'
wifi_password = 'awwm715#'

# Connect to WLAN
def connect_to_wifi():
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    wlan.connect(wifi_ssid, wifi_password)
    while not wlan.isconnected():
        pass
    print('Connected to WLAN')
    
def receive_send(value):
    # 1 For toggling automatic pump on
    # 2 For toggling automatic pump off
    # 3 For getting soil moisture
    # 4 For getting temp
    # 5 For humidity
    # 6 For water level
    # 7 For Light
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
        'temperature' : temperature,
        'moisture' : moisture,
        'waterlevel' : water_level,
        'humidity' : humidity,
        }
    json_data = json.dumps(data) #data into JSON
    data_bytes = json_data.encode('utf-8')

    content_length = len(data_bytes) #calc content length
    headers = {
        'Content-Type' : 'application/json',
        'Content-Length' : str(content_length)
        }

    response = urequests.put('https://us-central1-potcast-plant.cloudfunctions.net/app/api/pots/update/1',
                             headers=headers,
                             data=data_bytes)
    print(response.text)
    print('Successfully sent data to endpoint')
    
def api_send_sunlight():
    data = {
        'sunlight' : int(sunlight)
    }
    json_data = json.dumps(data)  # Convert data to JSON
    data_bytes = json_data.encode('utf-8')

    content_length = len(data_bytes)  # Calculate content length
    headers = {
        'Content-Type': 'application/json',
        'Content-Length': str(content_length)
    }

    response = urequests.put('https://us-central1-potcast-plant.cloudfunctions.net/app/api/pots/update/sunlight/1',
                             headers=headers,
                             data=data_bytes)
    print(response.text)
    print('Successfully sent data to endpoint')

while True:
    connect_to_wifi()
    receive_send(3) # soil moisture
    receive_send(4) # temperature 
    receive_send(5) # humidity
    receive_send(6) # water level
    receive_send(7)
    api_send_data()
    api_send_sunlight()
    usleep(120)
