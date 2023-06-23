import json
import requests
from sense_hat import SenseHat
from time import sleep
sense=SenseHat()
sense.clear()

for x in range(5):
    reading = json.dumps({
        'time': x,
        'temperature': sense.get_temperature(),
        'humidity': sense.get_humidity()
        })
    sleep(1)
    response = requests.put("api url goes here")
    print(response.text)
