import json

from twisted.web.server import Site
from twisted.web.resource import Resource
from twisted.internet import reactor
from twisted.web.static import File

import servo

class lockAPI(Resource):
   def render_POST(self, request):
        if "state" in request.args:
            if request.args["state"][0] == "open":
                servo.openLock()
                print "lock open"
                return json.dumps({"result": "open"})
            if request.args["state"][0] == "close":
                servo.closeLock()
                print"lock close"
                return json.dumps({"result": "close"})

root = File("lampwww")
root.putChild("lock", lockAPI())
factory = Site(root)
reactor.listenTCP(8080, factory)

reactor.run()