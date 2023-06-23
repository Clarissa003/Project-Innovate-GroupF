# Import modules
import network
import urequests
import json

humidity = 0
moisture = 0
sunlight = 69
temperature = 0
water_level = 0

# wifi_ssid = 'samba1'
# wifi_password = ''

# wifi_ssid = 'Ziggo-ap-4d4be42'
# wifi_password = 'wwfCJv8xnyvz'

wifi_ssid = 'asdf'
wifi_password = 'awwm715#'


# Connect to WLAN
wlan = network.WLAN(network.STA_IF)
wlan.active(True)
wlan.connect(wifi_ssid, wifi_password)
while not wlan.isconnected():
    pass
print('Connected to WLAN')

def handle_put_request(url, data):
    json_data = json.dumps(data)
    data_bytes = json_data.encode('utf-8')
    headers = {
        'Content-Type': 'application/json',
        'Content-Length': str(len(data_bytes))
    }
    response = urequests.put(url, headers=headers, data=data_bytes)  # Disable SSL/TLS encryption
    print("Response Status Code:", response.status_code)
    print("Response Headers:", response.headers)
    print("Response Content:", response.content)
    if response.status_code == 200:
        if response.content:
            return response.json()
        else:
            print("Error: Empty response content")
    else:
        print("Error: API request failed")
    return None



def api_send_data():
    url = 'https://us-central1-potcast-plant.cloudfunctions.net/app/api/pots/update/1'
    data = {
        'temperature': temperature,
        'moisture': moisture,
        'waterlevel': water_level,  # Set water level as needed
        'humidity': humidity,
    }
    response = handle_put_request(url, data)
    if response is not None and response.status_code == 200:
        print("Data sent successfully.")
    else:
        print("Error: Failed to send data.")

def api_send_sunlight():
    url = 'https://us-central1-potcast-plant.cloudfunctions.net/app/api/pots/update/sunlight/1'
    data = {
        'sunlight': sunlight,  # Use the actual sunlight variable value
    }
    response = handle_put_request(url, data)
    if response is not None and response.status_code == 200:
        print("Sunlight data sent successfully.")
    else:
        print("Error: Failed to send sunlight data.")

def main():
    api_send_data()
    api_send_sunlight()

if __name__ == "__main__":
    main()