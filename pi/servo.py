import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)
GPIO.setup(18, GPIO.OUT)
pwm = GPIO.PWM(18, 100)
pwm.start(5)

def update(x):
    duty = float(x) / 10.0 + 2.5
    pwm.ChangeDutyCycle(duty)

def openLock():
    print "Opened"
    update(0)
    #pwm.stop()

def closeLock():
    print "Closed"
    update(110)
    #pwm.stop()