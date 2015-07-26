# __author__ = 'michelle'
import socket
from threading import Timer
from parse_rest.connection import register
from parse_rest.datatypes import Object
from parse_rest.user import User
from time import sleep
import urllib
import re
import parse_settings

register(APPLICATION_ID, REST_API_KEY, master_key=MASTER_KEY)

def get_external_ip():
    site = urllib.urlopen("http://checkip.dyndns.org/").read()
    grab = re.findall('([0-9]+\.[0-9]+\.[0-9]+\.[0-9]+)', site)
    address = grab[0]
    return address

class RepeatedTimer(object):
    def __init__(self, interval, function, *args, **kwargs):
        self._timer     = None
        self.interval   = interval
        self.function   = function
        self.args       = args
        self.kwargs     = kwargs
        self.is_running = False
        self.start()

    def _run(self):
        self.is_running = False
        self.start()
        self.function(*self.args, **self.kwargs)

    def start(self):
        if not self.is_running:
            self._timer = Timer(self.interval, self._run)
            self._timer.start()
            self.is_running = True

    def stop(self):
        self._timer.cancel()
        self.is_running = False



class Lock(Object):
    pass

ip =0
newip=1
def checkIP():
    global ip
    global newip
    newip= get_external_ip()
    if  ip!= newip:
        ip=newip
        L = Lock.Query.get(objectId="1H7TQAu8LH")
        L.ip=str(newip)
        L.save()




print "starting..."
rt = RepeatedTimer(30, checkIP) # it auto-starts, no need of rt.start()