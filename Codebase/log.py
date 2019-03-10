import serial
from time import sleep

connection = serial.Serial('/dev/ttyACM0', baudrate=115200, bytesize=serial.EIGHTBITS, parity=serial.PARITY_NONE, stopbits=serial.STOPBITS_ONE, timeout=1, xonxoff=0, rtscts=1)
	 	
connection.setRTS(True)
connection.setDTR(False)
sleep(5)

while True:
	print(connection.read(10))

