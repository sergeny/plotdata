package com.sergeyorshanskiy.domain

class TimeSeries {

	static hasMany = [points: DataPoint]

    static constraints = {
    }

	// Adds a new point with the current timestamp by making a single observation.
	// It can involve taking the current CPU load, checking the current stock price, etc.
	// The point should be added through the GORM interface; do not call save() from generate().
	// Particular domain classes should inherit from TimeSeries and implement this method.
	void generate() {
		throw new UnsupportedOperationException("Inherit and implement if you want some particular behavior")
	}
	
	// Add a new point: current timestamp, specified value.
	void generate(double value) {
		addToPoints(new DataPoint(value))
		// Purposely not calling save() here. 
	}
}
