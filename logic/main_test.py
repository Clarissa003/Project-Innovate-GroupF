import network #network connectivity
import machine
import gc
import utime
import time
import urequests
import usocket as socket
from easy_comms import Easy_comms
from time import sleep
from machine import Pin
import json

####################
# Global Variables #
####################

#unique id corresponding to the pot
pot_id = 1234

#boolean to for program to check if the post for creating a pot has or has not been made
post_sent = False

#Wifi SSID and Password, default values of 0000 will be updated upon wifi setup
# wifi_ssid = '0000'
# wifi_password = '0000'


#for testing
wifi_ssid = 'asdf'
wifi_password = 'awwm715#'

# Boundary Parameters
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

# Sensor values
humidity = 0
sunlight = 0
moisture = 0
temperature = 0
water_level = 0

#Serial Communication
com1 = Easy_comms(0,9600)

####################
#    Functions     #
####################

####_Timers_####

#debugging
def print_globals():
#     print(plant_id, humidity_max, humidity_min, sunlight_max, sunlight_min, moisture_max, moisture_min,temperature_max, temperature_min, automatic_watering)
    print(humidity, sunlight, moisture, temperature, water_level)
    
def connect_to_wifi():
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    wlan.connect(wifi_ssid, wifi_password)
    while not wlan.isconnected():
        pass
    print('Connected to WLAN')

#1-hour timer, used to call functions during daily operation
def one_hour(): 
    interval = 3600
    timer = machine.Timer(-1)
    
    def timer_callback(timer):
        functions_go_here()
        
        timer.init(period=interval * 1000, mode=machine.Timer.PERIODIC, callback=timer_callback)

#2-minute timer, used to call functions during demonstrations
def two_minutes():
    interval = 720
    timer = machine.Timer(-1)
    
    def timer_callback(timer):
        sunlight_function_goes_here()
        
        timer.init(period=interval * 1000, mode=machine.Timer.PERIODIC, callback=timer_callback)

# Communication from Python to Arduino
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


# Communication from Python to Python
def p2p_sender(value):
    output = value
    command = output
    com1.send(str(json.dumps(command)))

def p2p_recieve():
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
    
# Try to connect to wifi
def try_connect(timeout=30):
    wlan = network.WLAN(network.STA_IF)  # Create WLAN object for station mode
    wlan.active(True)  # Activate the WLAN interface
    start_time = time.time()  # Get the current time
    wlan.connect(wifi_ssid, wifi_password)
    
    while not wlan.isconnected() and (time.time() - start_time) < timeout:
        time.sleep(1)  # Wait for 1 second before checking again
    
    if wlan.isconnected():
        print("Connection succesful")
        return True
    else:

        ap_ssid = 'PotCast_Plant_1234'
        ap_password = '12345678'
        
        ap = network.WLAN(network.AP_IF)
        ap.config(essid=ap_ssid, password=ap_password)
        ap.active(True)
        
        while ap.active() == False:
          pass
        
        print('Access Point is active')
        print(ap.ifconfig())
        
        web_page()
        
        return False

# Soft Access Point if WiFi connection fails
def web_page():
    html = """
    <!DOCTYPE html>
    <html>
        <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        </head>
        <body>
            <h1>Welcome to the PotCast Plant Wi-fi Setup</h1>
            <form name="wifi" action="/submit" method="post">
                <p>Please Enter the name/SSID of the network you want to use</p>
            <input type="text" name="SSID" placeholder="SSID">
                <p>Please Enter the password of the network you want to use</p>
            <input type="text" name="PASS" placeholder="password">
                <p></p>
            <input type="submit" value="submit">
          </form>
        </body>
    </html>
    """
    return html

#Extracts form data and write them to global variables
def form_handler(request):
    global wifi_ssid, wifi_password
    
    form_data = request.split(b'\r\n\r\n')[1]
    
    ssid_start = form_data.find(b'SSID=')
    ssid_end = form_data.find(b'&', ssid_start)
    wifi_ssid = form_data[ssid_start + 5:ssid_end]
    
    pass_start = form_data.find(b'PASS=')
    wifi_password = form_data[pass_start + 5:]
    
    print("SSID", wifi_ssid)
    print("Password", wifi_password)
    
    response = 'HTTP/1.0 200 OK\r\nContent-type: text/html\r\n\r\n'
    response += '<html><body>Form submitted successfully!</body></hmtl>'
    
    print("Updated wifi_ssid", wifi_ssid)
    print("Updated wifi_password", wifi_password)
    
    sleep(5)
    #re-run's wifi connection with newly updated global variables
    try_connect()
    
    return(response)
      
# API  functions

#One time post
def api_onetime_post():
    global post_sent
    if not post_sent:
        data = {
            'id' : pot_id
            }
        json_data = json.dumps(data) #data into JSON
        data_bytes = json_data.encode('utf-8')

        content_length = len(data_bytes) #calc content length
        headers = {
            'Content-Type' : 'application/json',
            'Content-Length' : str(content_length)
            }
        response = urequests.post('https://us-central1-potcast-plant.cloudfunctions.net/app/api/pots/create',
                                 headers=headers,
                                 data=data_bytes)
        if response.status_code == 200:
            post_sent = True
    else:
      pass

####_API GET Requests_####

#Gets boundary parameters from the API, as assigned by the mobile application
def api_get_boundary_parameters():
    response = urequests.get('https://us-central1-potcast-plant.cloudfunctions.net/app/api/plants/read/1')
    if response.status_code == 200:
        data = response.json()
        globals().update({
            "plant_id" : data['id'], #identifies which plant type is in the pot
            "humidity_max" : data['humidityMax'],
            "humidity_min" : data['humidityMin'],
            "sunlight_max" : data['sunlightMax'],
            "sunlight_min" : data['sunlightMin'],
            "moisture_max" : data['moistureMax'],
            "moisture_min" : data['moistureMin'],
            "temperature_max" : data['temperatureMax'],
            "temperature_min" : data['temperatureMin']
        })
    else:
        print("Error: API request failed")

        
        
def api_get_watering_boolean():
    response = urequests.get('https://us-central1-potcast-plant.cloudfunctions.net/app/api/pots/read/1')
    if response.status_code == 200:
        data = response.json()
        globals().update({
            "automatic_watering" : data['automaticWatering'], #identifies which plant type is in the pot
        })
    else:
        print("Error: API request failed") 
    
####_API PUT Requests_####

#Sends sensor values to the pot's unique endpoint
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
    
#Sunlight values are sent to a unique endpoint, as the API uses the value for a Queue data structure
def api_send_sunlight():
    data = {
        'sunlight' : sunlight,
        }
    json_data = json.dumps(data) #data into JSON
    data_bytes = json_data.encode('utf-8')

    content_length = len(data_bytes) #calc content length
    headers = {
        'Content-Type' : 'application/json',
        'Content-Length' : str(content_length)
        }
    response = urequests.put('https://us-central1-potcast-plant.cloudfunctions.net/app/api/pots/update/sunlight/1',
                             headers=headers,
                             data=data_bytes)
    print(response.text)

###################
#  Main Program   #
###################

def main():
    connected = try_connect()
    if connected:        
        receive_send(3) # soil moisture
        receive_send(4) # temperature 
        receive_send(5) # humidity
        receive_send(6) # water level
        receive_send(7) # light
        print_globals() #for debug
#         api_send_data()
#         api_send_sunlight()
        gc.collect()
#         sleep(720) #sleep for two minutes

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)   #creating socket object
s.bind(('', 80))
s.listen(5)



while True:
    
    main()
    
    conn, addr = s.accept()
    print('Got a connection from %s' % str(addr))
    request = conn.recv(1024)
    print('Content = %s' % str(request))

    if b'/submit' in request:
        response = form_handler(request)
    else:
        response = web_page()

    conn.send(response)
    conn.close()
        