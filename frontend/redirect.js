 http = require('http');


// create a http server
http.createServer(function (req, res) {

	if (req.url == '/') {

		res.writeHead(301, { "Location": "https://wallet.credits.com/" });
		return res.end();
	}
}).listen(80);


