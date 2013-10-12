package plotdata

import com.sergeyorshanskiy.domain.StockSeries;


class StockController {


	def json(String ticker, long strictlyafter) {   // return pure JSON, not JSONP
		println "json " + strictlyafter + params
		def series = StockSeries.findByTicker(ticker)   // No problem even if ticker is nul
		if (series) {
			assert( 0 > null )   // If strictlyAfter is not defined, pick everything
			render (contentType: "application/json") {
				series.points.findAll{ it.time > strictlyafter }.sort{ it.time }.collect{[it.time, it.value]}
			}
		} else {
			response.status = 404
			render "No data is available for ticker "+ticker
			return
		}	
	}
    
	def index() {
		[:]
	}
}
