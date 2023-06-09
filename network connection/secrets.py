import softAPtest

form = softAPtest.web_page()
with open ('wifi.txt', 'w') as fileOutput:
    fileOutput.write(form.getValue('SSID'))
    fileOutput.write(form.getValue('PASS'))
     
     print(SSID)