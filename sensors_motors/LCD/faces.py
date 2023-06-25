from time import sleep_ms #, ticks_us, ticks_diff
import framebuf
import gc
import micropython
import uasyncio as asyncio
from machine import Pin, SPI
import math

# User orientation constants
LANDSCAPE = 0  # Default
REFLECT = 1
USD = 2
PORTRAIT = 4
# Display types
GENERIC = (0, 0, 0)
TDISPLAY = (52, 40, 1)




class ST7789(framebuf.FrameBuffer):

    lut = bytearray(0x00 for _ in range(32))  # set all colors to BLACK

    # Convert r, g, b in range 0-255 to a 16 bit colour value rgb565.
    # LS byte goes into LUT offset 0, MS byte into offset 1
    # Same mapping in linebuf so LS byte is shifted out 1st
    # For some reason color must be inverted on this controller.
    @staticmethod
    def rgb(r, g, b):
        return ((b & 0xf8) << 5 | (g & 0x1c) << 11 | (g & 0xe0) >> 5 | (r & 0xf8)) ^ 0xffff

    # rst and cs are active low, SPI is mode 0
    def __init__(self, spi, cs, dc, rst, height=320, width=240,
                 disp_mode=LANDSCAPE, init_spi=False, display=GENERIC):
        if not 0 <= disp_mode <= 7:
            raise ValueError('Invalid display mode:', disp_mode)
        if not display in (GENERIC, TDISPLAY):
            raise ValueError('Invalid display type.')
        self._spi = spi  # Clock cycle time for write 16ns 62.5MHz max (read is 150ns)
        self._rst = rst  # Pins
        self._dc = dc
        self._cs = cs
        self.height = height  # Required by Writer class
        self.width = width
        self._offset = display[:2]  # display arg is (x, y, orientation)
        orientation = display[2]  # where x, y is the RAM offset
        self._spi_init = init_spi  # Possible user callback
        self._lock = asyncio.Lock()
        mode = framebuf.RGB565  # Use RGB565
        gc.collect()
        buf = bytearray(height * width * 2)  # Ceiling division for odd widths
        self._mvb = memoryview(buf)
        super().__init__(buf, width, height, mode)
        self._linebuf = bytearray(self.width * 2)  # 16 bit color out
        self._init(disp_mode, orientation)
#         self.show()
        

    # Hardware reset
    def _hwreset(self):
        self._dc(0)
        self._rst(1)
        sleep_ms(1)
        self._rst(0)
        sleep_ms(1)
        self._rst(1)
        sleep_ms(1)

    # Write a command, a bytes instance (in practice 1 byte).
    def _wcmd(self, buf):
        self._dc(0)
        self._cs(0)
        self._spi.write(buf)
        self._cs(1)

    # Write a command followed by a data arg.
    def _wcd(self, c, d):
        self._dc(0)
        self._cs(0)
        self._spi.write(c)
        self._cs(1)
        self._dc(1)
        self._cs(0)
        self._spi.write(d)
        self._cs(1)

    # Initialise the hardware. Blocks 163ms. Adafruit have various sleep delays
    # where I can find no requirement in the datasheet. I removed them with
    # other redundant code.
    def _init(self, user_mode, orientation):
        self._hwreset()  # Hardware reset. Blocks 3ms
        if self._spi_init:  # A callback was passed
            self._spi_init(self._spi)  # Bus may be shared
        cmd = self._wcmd
        wcd = self._wcd
        cmd(b'\x01')  # SW reset datasheet specifies 120ms before SLPOUT
        sleep_ms(150)
        cmd(b'\x11')  # SLPOUT: exit sleep mode
        sleep_ms(10)  # Adafruit delay 500ms (datsheet 5ms)
        wcd(b'\x3a', b'\x55')  # _COLMOD 16 bit/pixel, 65Kbit color space
        cmd(b'\x20') # INVOFF Adafruit turn inversion on. This driver fixes .rgb
        cmd(b'\x13')  # NORON Normal display mode

        # Table maps user request onto hardware values. index values:
        # 0 Normal 
        # 1 Reflect
        # 2 USD
        # 3 USD reflect
        # Followed by same for LANDSCAPE
        if not orientation:
            user_mode ^= PORTRAIT
        # Hardware mappings
        # d7..d5 of MADCTL determine rotation/orientation datasheet P124, P231
        # d5 = MV row/col exchange
        # d6 = MX col addr order
        # d7 = MY page addr order
        # LANDSCAPE = 0
        # PORTRAIT = 0x20
        # REFLECT = 0x40
        # USD = 0x80
        mode = (0x60, 0xe0, 0xa0, 0x20, 0, 0x40, 0xc0, 0x80)[user_mode]
        # Set display window depending on mode, .height and .width.
        self.set_window(mode)
        wcd(b'\x36', int.to_bytes(mode, 1, 'little'))
        cmd(b'\x29')  # DISPON. Adafruit then delay 500ms.

    # Define the mapping between RAM and the display.
    # Datasheet section 8.12 p124.
    def set_window(self, mode):
        portrait, reflect, usd = 0x20, 0x40, 0x80
        rht = 320
        rwd = 240  # RAM ht and width
        wht = self.height  # Window (framebuf) dimensions.
        wwd = self.width  # In portrait mode wht > wwd
        if mode & portrait:
            xoff = self._offset[1]  # x and y transposed
            yoff = self._offset[0]
            xs = xoff
            xe = wwd + xoff - 1
            ys = yoff  # y start
            ye = wht + yoff - 1 # y end


    #@micropython.native # Made virtually no difference to timing.
    def show(self):  # Blocks for 83ms @60MHz SPI
#         ts = ticks_us()
        clut = ST7789.lut
        wd = self.width # Ceiling division for odd number widths
        end = self.height * wd *2
        lb = memoryview(self._linebuf)
        buf = self._mvb
        if self._spi_init:  # A callback was passed
            self._spi_init(self._spi)  # Bus may be shared
        self._dc(0)
        self._cs(0)
        self._spi.write(b'\x2c')  # RAMWR
        self._dc(1)
#         for start in range(0, end, 480):
#             _lcopy(lb, buf[start:], clut, wd)  # Copy and map colors
#             self._spi.write(lb)
#             self._spi.write(buf[start:start+480])
        self._spi.write(buf[0:])
        self._cs(1)
#         print(ticks_diff(ticks_us(), ts),'us')


            


def happyFace():
    SSD = ST7789

    pdc = Pin(8, Pin.OUT, value=0)
    pcs = Pin(9, Pin.OUT, value=1)
    prst = Pin(15, Pin.OUT, value=1)
    pbl = Pin(13, Pin.OUT, value=1)
    
    gc.collect()
    spi = SPI(1, 60_000_000, sck=Pin(10), mosi=Pin(11), miso=Pin(12))

    ssd = SSD(spi, height=320, width=240, disp_mode=2, dc=pdc, cs=pcs, rst=prst)
    ssd.fill(ssd.rgb(0x0000, 0x0000, 0x0000))
    emoji_size = 150

    start_x = (240 - emoji_size) // 2
    start_y = (320 - emoji_size) // 2

    eye_radius = emoji_size // 10
    eye_spacing = emoji_size // 5
    eye_x_left = start_x + emoji_size // 4 - eye_spacing // 2 - eye_radius + 38
    eye_x_right = start_x + emoji_size // 4 + eye_spacing // 2 + eye_radius + 38
    eye_y = start_y + emoji_size // 3
    mouth_radius = emoji_size // 5
    mouth_x = start_x + emoji_size // 2

    mouth_y = start_y + emoji_size // 2 + emoji_size // 5

    for y in range(start_y, start_y + emoji_size):
        for x in range(start_x, start_x + emoji_size):
            distance = math.sqrt((x - start_x - emoji_size // 2) ** 2 + (y - start_y - emoji_size // 2) ** 2)
            if distance <= emoji_size // 2:
                ssd.text('.', x, y, ssd.rgb(0x00, 0xFF, 0x00))

    for i in range(-eye_radius, eye_radius + 1):
        for j in range(-eye_radius, eye_radius + 1):
            if math.sqrt(i ** 2 + j ** 2) <= eye_radius:
                ssd.text('.', eye_x_left + i, eye_y + j, ssd.rgb(0xFFFF, 0xFFFF, 0xFFFF))
                ssd.text('.', eye_x_right + i, eye_y + j, ssd.rgb(0xFFFF, 0xFFFF, 0xFFFF))
                
    for i in range(-mouth_radius, mouth_radius + 1):
        for j in range(-mouth_radius, mouth_radius + 1):
            if j <= math.sqrt(mouth_radius ** 2 - i ** 2):
                ssd.text('.', mouth_x + i, mouth_y + j, ssd.rgb(0xFFFF, 0xFFFF, 0xFFFF))

    ssd.text('I am Happy', 81, 270, ssd.rgb(0x00, 0xFF, 0x00))
    ssd.show()
    
def sadColdFace():
    SSD = ST7789

    pdc = Pin(8, Pin.OUT, value=0)
    pcs = Pin(9, Pin.OUT, value=1)
    prst = Pin(15, Pin.OUT, value=1)
    pbl = Pin(13, Pin.OUT, value=1)
    
    gc.collect()
    spi = SPI(1, 60_000_000, sck=Pin(10), mosi=Pin(11), miso=Pin(12))

    ssd = SSD(spi, height=320, width=240, disp_mode=2, dc=pdc, cs=pcs, rst=prst)
    ssd.fill(ssd.rgb(0x0000, 0x0000, 0x0000))
    emoji_size = 150

    start_x = (240 - emoji_size) // 2
    start_y = (320 - emoji_size) // 2

    eye_radius = emoji_size // 10
    eye_spacing = emoji_size // 5
    eye_x_left = start_x + emoji_size // 4 - eye_spacing // 2 - eye_radius + 38
    eye_x_right = start_x + emoji_size // 4 + eye_spacing // 2 + eye_radius + 38
    eye_y = start_y + emoji_size // 3
    mouth_radius = emoji_size // 5
    mouth_x = start_x + emoji_size // 2

    mouth_y = start_y + emoji_size // 2 + emoji_size // 5
    
    for y in range(start_y, start_y + emoji_size):
        for x in range(start_x, start_x + emoji_size):
            distance = math.sqrt((x - start_x - emoji_size // 2) ** 2 + (y - start_y - emoji_size // 2) ** 2)
            if distance <= emoji_size // 2:
                ssd.text('.', x, y, ssd.rgb(0xAD, 0xD8, 0xE6))

    for i in range(-eye_radius, eye_radius + 1):
        for j in range(-eye_radius, eye_radius + 1):
            if math.sqrt(i ** 2 + j ** 2) <= eye_radius:
                ssd.text('.', eye_x_left + i, eye_y + j, ssd.rgb(0xFFFF, 0xFFFF, 0xFFFF))
                ssd.text('.', eye_x_right + i, eye_y + j, ssd.rgb(0xFFFF, 0xFFFF, 0xFFFF))
                
    for i in range(-mouth_radius, mouth_radius + 1):
        for j in range(-2, math.floor(math.sqrt(mouth_radius ** 2 - i ** 2)) + 1):
            ssd.text('.', mouth_x + i, mouth_y - j, ssd.rgb(0xFFFF, 0xFFFF, 0xFFFF))  # Mouth
            
    ssd.text('I am COLD', 85, 270, ssd.rgb(0xAD, 0xD8, 0xE6))
                
    ssd.show()
    
    
def sadWarmFace():
    SSD = ST7789

    pdc = Pin(8, Pin.OUT, value=0)
    pcs = Pin(9, Pin.OUT, value=1)
    prst = Pin(15, Pin.OUT, value=1)
    pbl = Pin(13, Pin.OUT, value=1)
    
    gc.collect()
    spi = SPI(1, 60_000_000, sck=Pin(10), mosi=Pin(11), miso=Pin(12))

    ssd = SSD(spi, height=320, width=240, disp_mode=2, dc=pdc, cs=pcs, rst=prst)
    ssd.fill(ssd.rgb(0x0000, 0x0000, 0x0000))
    emoji_size = 150

    start_x = (240 - emoji_size) // 2
    start_y = (320 - emoji_size) // 2

    eye_radius = emoji_size // 10
    eye_spacing = emoji_size // 5
    eye_x_left = start_x + emoji_size // 4 - eye_spacing // 2 - eye_radius + 38
    eye_x_right = start_x + emoji_size // 4 + eye_spacing // 2 + eye_radius + 38
    eye_y = start_y + emoji_size // 3
    mouth_radius = emoji_size // 5
    mouth_x = start_x + emoji_size // 2

    mouth_y = start_y + emoji_size // 2 + emoji_size // 5
    
    for y in range(start_y, start_y + emoji_size):
        for x in range(start_x, start_x + emoji_size):
            distance = math.sqrt((x - start_x - emoji_size // 2) ** 2 + (y - start_y - emoji_size // 2) ** 2)
            if distance <= emoji_size // 2:
                ssd.text('.', x, y, ssd.rgb(0xFFFF, 0x0000, 0x0000))

    for i in range(-eye_radius, eye_radius + 1):
        for j in range(-eye_radius, eye_radius + 1):
            if math.sqrt(i ** 2 + j ** 2) <= eye_radius:
                ssd.text('.', eye_x_left + i, eye_y + j, ssd.rgb(0xFFFF, 0xFFFF, 0xFFFF))
                ssd.text('.', eye_x_right + i, eye_y + j, ssd.rgb(0xFFFF, 0xFFFF, 0xFFFF))
                
    for i in range(-mouth_radius, mouth_radius + 1):
        for j in range(-2, math.floor(math.sqrt(mouth_radius ** 2 - i ** 2)) + 1):
            ssd.text('.', mouth_x + i, mouth_y - j, ssd.rgb(0xFFFF, 0xFFFF, 0xFFFF))  # Mouth
            
    ssd.text('I am HOT', 85, 270, ssd.rgb(0xFFFF, 0x0000, 0x0000))
                
    ssd.show()

    
#happyFace()
#sadColdFace()
sadWarmFace()










