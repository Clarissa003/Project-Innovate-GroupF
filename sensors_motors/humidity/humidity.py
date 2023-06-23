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
        print("temper : " + str(temper))
        print("humidity : " + str(humidity))
        
def motor():
    import machine
    import utime

    # Configure GPIO pin
    motor_pin = machine.Pin(16, machine.Pin.OUT)

    # Turn the motor on and off in a loop
    while True:
        motor_pin.on()  # Turn the motor on
        utime.sleep(1)  # Wait for 1 second
        motor_pin.off()  # Turn the motor off
        utime.sleep(1)  # Wait for 1 second
            
humidity()
#motor()
