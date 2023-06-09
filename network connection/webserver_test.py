import network
import socket
from time import sleep
from picozero import pico_temp_sensor, pico_led

###_variables_####

#network connection
ssid = 'samba1'
password = ''

###_functions_###

def connect():
    #connect to WLAN
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    wlan.connect(ssid, password)
    while wlan.isconnected() == False:
        print('Waiting for connection...')
        sleep(1)
    ip = wlan.ifconfig()[0]
    print(f'Connected on {ip}')
    return ip
    
    
def open_socket(ip):
    #open a socket
    address = (ip, 80)
    connection = socket.socket()
    connection.bind(address)
    connection.listen(1)
    return connection

def webpage(temperature, state):
    #tempalte HTML
    html = f"""
    <!DOCTYPE html>
    <html>
        <body>
            <form action="./lighton">
                <input type="submit" value="Light on" />
            </form>
            <form action="./lightoff">
                <input type="submit" value="Light off" />
            </form>
            <p>LED is {state}</p>
            <p>Temperature is {temperature}</p>
        </body>
    </html>
    """
    return str(html)

def serve(connection):
    #start a web server
    state = 'OFF'
    pico_led.off()
    temperature = 0
    while True:
        client = connection.accept()[0]
        request = client.recv(1024)
        request = str(request)
        print(request)
        client.close()
    
try:
    ip = connect()
    connection = open_socket(ip)
    serve(connection)
except KeyboardInterrupt:
    machine.reset()

