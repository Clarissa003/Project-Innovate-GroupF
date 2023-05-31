from time import sleep
from picozero import pico_temp_sensor, pico_led

###_variables_####

#stored time value for functions to check against
stored_time = 0

#boundary values assigned by api_recieve
sunlight_average_maximum = 0
sunlight_average_minimum = 0

soil_moisture_minimum = 0
soil_moisture_maximum = 0

air_humidity_minimum = 0
air_humidity_maximum = 0

water_level_minimum = 0
water_level_maximum = 0

#length of time that pump and valve should be turned on
watering_time = 0

#auto-watering boolean that will be toggled via the application
automatic_watering = True

##_sensor values update on a 30 minute interval_##

#array that contains 48 values, 1 reading for each hour of the day
sunlight_values = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

#average value of sunlight_values array
sunlight_value_average = 0

#integer updated from check_soil_moisture
soil_moisture_value = 0

#integer updated from check_air_humidity
air_humidity_value = 0

#integer updated from check_water_level
water_level_value = 0


###_functions_###

def api_send():
    #if current_time >= last_time + 30 minutes
        #write sensor value variable to a json file
            #attempt to send json file to api

def api_receive():
    #if json file has sent_pot_serial_no === this_pot_serial_no
        #update boundary values
        
def check_sunlight():
    #if current_time >= last_time + 30 minutes
        #check the sensor values and store the values to variable sunlight_values        

def check_soil_moisture():
    #if current_time >= last_time + 30 minutes
        #check the sensor values and store the values to variable soil_moisture_value
    
def check_air_humidity():
    #if current_time >= last_time + 30 minutes
        #check the sensor values and store the values to variable air_humidity_value   
    
def check_water_level():
    #if current_time >= last_time + 30 minutes
        #check the sensor values and store the values to variable water_level_value 
    
def water_plant():
    #if soil_moisture_value < soil_moisture_minimum
    
    #run water_level to check water level
        #if water_level_value < [necessary value]
            #send notification that water level is too low and is unable to water
        #else
            #while loop to power on solenoid and pump for watering_time
            #run water_level to check water level
      
    
try:
except KeyboardInterrupt:
    machine.reset()

