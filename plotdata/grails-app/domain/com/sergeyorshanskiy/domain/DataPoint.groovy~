package com.sergeyorshanskiy.domain
import groovy.transform.ToString

/**
 * This GORM domain class represents a real numerical value at some period of time. 
 * For example, the CPU load yesterday at 4:34:23pm EST was 74.364%.
 *
 * @author  Sergey Orshanskiy
 */


@ToString
class DataPoint {
    double x
    double y
    static constraints = {
    }

    // We cannot hide the constructor for a domain object. But consider using a factory to sample points.
    // protected static DataPoint() {}

    /**
     * Prepare to sample DataPoints. Create a factory, then repeatedly call generate().
     *
     * @param   Additional info to prepare to sample data, e.g. the stock ticker to listen for. 
     */
    static DataPointFactory getFactory(Object param) {
        new DataPointTestFactory()
    }


}


// ??? Grails will probably also inject methods into the factory classes, not just into domain classes. This should be fine.
class DataPointTestFactory extends DataPointFactory {
    DataPoint generate() {
        new DataPoint(x: 2, y: 3)
    }
}

