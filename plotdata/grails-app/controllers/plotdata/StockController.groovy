package plotdata

class StockController {

// Content-type: application/json
	def json(String ticker) {  
		if (!ticker) { return; }
		def result="//Price information for ${ticker}\n[\n" + 
		"[1,2],\n[3,4]" + "\n]"
		render(contentType: "application/json", text: result) 
		
	}
    
}
