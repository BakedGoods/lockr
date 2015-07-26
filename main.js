Parse.Cloud.define("toggleLock", function(request, response) {
	console.log("ran");
	var state = "close";
	if(request.params.state == "close")
		state = "open";
	Parse.Cloud.httpRequest({
	  url: 'http://' + request.params.ip,
	  method: 'POST',
	  body:'{state='+state+'}',
	  success: function(httpResponse) {
		console.log(httpResponse.text);
		response.success();
	  },
	  error: function(httpResponse) {
		response.error('Request failed with response code ' + httpResponse.status);
	  }
	});
});