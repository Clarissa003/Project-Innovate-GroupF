import socket
import ssl
import ujson
wifi_ssid = 'samba1'
wifi_password = ''
def connect_to_wifi():
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    wlan.connect(wifi_ssid, wifi_password)
    while not wlan.isconnected():
        pass
    print('Connected to WLAN')
def api_get_boundary_parameters():
    url = 'https://us-central1-potcast-plant.cloudfunctions.net/app/api/update/1'
    port = 443
    
    # Establish a TCP socket connection
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect(socket.getaddrinfo(url, port)[0][-1])
    
    # Wrap the socket with SSL/TLS
    context = ssl.create_default_context()
    secure_sock = context.wrap_socket(sock, server_hostname=url)
    
    # Send an HTTP GET request
    request = "GET /api/plants/read/1 HTTP/1.0\r\nHost: {}\r\n\r\n".format(url)
    secure_sock.write(request.encode())
    
    # Receive and process the response
    response = secure_sock.read()
    response_lines = response.split(b'\r\n')
    
    if response_lines[0].startswith(b'HTTP/1.1 200'):
        response_body = b''.join(response_lines[response_lines.index(b'') + 1:])
        data = ujson.loads(response_body)
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
    else:
        print("Error: API request failed")
    
    # Close the secure socket
    secure_sock.close()

# Call the API function
api_get_boundary_parameters()
while True:
    print_globals()
    connect_to_wifi()
    sleep(5)
    api_get_boundary_parameters()
    sleep(5)
    print_globals()
