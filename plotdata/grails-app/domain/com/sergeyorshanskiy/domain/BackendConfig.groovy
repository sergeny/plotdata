package com.sergeyorshanskiy.domain


class BackendConfig {

	int period                              // Update the data every ${period} milliseconds
	static hasMany = [series: TimeSeries]   // To update, call generate() on each TimeSeries and perhaps save(flush:true)

    static constraints = {
    }

// default configuration
	BackendConfig() {
		period = 1000 // every second
		series = TimeSeries.findAll().findAll{ it.getClass() != TimeSeries } // Will update all TimeSeries of derived classes
	}
}
