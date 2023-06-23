# Import modules
import network
import urequests
import json

humidity = 0
sunlight = 0
moisture = 0
temperature = 0
water_level = 0

wifi_ssid = 'samba1'
wifi_password = ''

# wifi_ssid = 'Ziggo-ap-4d4be42'
# wifi_password = 'wwfCJv8xnyvz'

# Connect to WLAN
wlan = network.WLAN(network.STA_IF)
wlan.active(True)
wlan.connect(wifi_ssid, wifi_password)
while not wlan.isconnected():
    pass
print('Connected to WLAN')

data = {
    'temperature' : temperature,
    'moisture' : moisture,
    'sunlight' : sunlight,
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
response = urequests.put('https://us-central1-potcast-plant.cloudfunctions.net/app/api/update/1',
                         headers=headers,
                         data=data_bytes)
print(response.text)
