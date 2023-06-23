try:
 import usocket as socket        #importing socket
except:
 import socket
import network            #importing network
import gc
import secrets
gc.collect()
ssid = 'RPI_PICO_AP'                  #Set access point name 
password = '12345678'      #Set your access point password


ap = network.WLAN(network.AP_IF)
ap.config(essid=ssid, password=password)
ap.active(True)            #activating

while ap.active() == False:
  pass
print('Connection is successful')
print(ap.ifconfig())
def web_page():
  html = """
  <!DOCTYPE html>
  <html>
      <head>
      <meta name="viewport" content="width=device-width, initial-scale=1">
      </head>
      <body>
          <h1>Welcome to the PotCast Plant Wi-fi Setup</h1>
          <form name="wifi" action=".\secrets.py" method="post">
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
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)   #creating socket object
s.bind(('', 80))
s.listen(5)
while True:
  conn, addr = s.accept()
  print('Got a connection from %s' % str(addr))
  request = conn.recv(1024)
  print('Content = %s' % str(request))
  response = web_page()
  conn.send(response)
  conn.close()