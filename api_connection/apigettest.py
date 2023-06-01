# Import modules
import network
import urequests

# Connect to WLAN
wlan = network.WLAN(network.STA_IF)
wlan.active(True)
wlan.connect("samba1", "")
while not wlan.isconnected():
    pass
print('Connected to WLAN')

# Make an API call
response = urequests.get('https://us-central1-potcast-plant.cloudfunctions.net/app/api/pots/read/2')
data = response.json()
print(data['sunlight'])