def humidity():
    from machine import I2C
    from dht20 import DHT20
    import utime
    i2c = I2C(1)
    print(i2c.scan())
    dht20 = DHT20(i2c)
    while True:
        temper = dht20.dht20_temperature()
        humidity = dht20.dht20_humidity()
        result = f"Temp = {temper} and humidity = {humidity}"
        return result
        
def motor(inp, time):
    import machine
    import utime
    
    motor_pin = machine.Pin(16, machine.Pin.OUT)
    
    if inp == True :
        while True:
            motor_pin.on()  # Turn the motor on
            utime.sleep(time)  # Wait for 1 second
    else:
            motor_pin.off()  # Turn the motor off
        
        
def soilmoisture():
    from machine import ADC, Pin
    import utime

    soil = ADC(Pin(26))

    min_moisture=0
    max_moisture=65535

    readDelay = 0.5

    while True:
        moisture = (max_moisture-soil.read_u16())*100/(max_moisture-min_moisture)
        
        result = "moisture: " + "%.2f" % moisture +"% (adc: "+str(soil.read_u16())+")"
        utime.sleep(readDelay)
        return result

def soilPump(inputSoilMin, givenTime):
    
    if soilmoisture().moisture < inputSoilMin:
        motor(True, givenTime)
    else:
        motor(False)
        
    
            
humidity()
#motor()
