package plotdata

class StockController {

// Content-type: application/json
	def json(String ticker) {   // return pure JSON, not JSONP
		if (!ticker) { return; }
		def result="""[
		[1, 2],
		[3, 4],
		[5, 2]
		]""" 
		render(contentType: "application/json" /* "text/javascript" */, text: result) 
	}
    
	def index() {
	
		[:]
	}
}
