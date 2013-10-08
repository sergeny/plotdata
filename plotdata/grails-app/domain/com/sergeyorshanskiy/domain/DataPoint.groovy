package com.sergeyorshanskiy.domain
import java.util.Date
import groovy.transform.ToString

/**
 * This GORM domain class represents a real numerical value at some period of time. 
 * Think about a single observation made by a scientist.
 * For example, the CPU load was 74.364% yesterday at 2:34:23pm EST.
 * Another example: Apple stock traded for $480.74 at the same moment in time.
 *
 * @author  Sergey Orshanskiy
 */


@ToString
class DataPoint {
    long time   // 64-bit Unix timestamp.
	double value

	// We are only interested in points if they belong to some series.
	// A particular time-value pair is meaningless by itself.
	static belongsTo = [series: TimeSeries] 
	
    static constraints = {    		
	}
	
	DataPoint(double value) {
		time = new Date().getTime()
		this.value = value
	}
}

