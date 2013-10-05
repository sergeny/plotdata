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

    // What will happen to Grails if we forbid creating datapoints explicitly?
    // protected static DataPoint() {}

    /**
     * Prepare to sample DataPoints. Create a factory, then repeatedly call generate().
     *
     * @param   Additional info to prepare to sample data, e.g. the stock ticker to listen for. 
     */
    static DataPointFactory getFactory(Object param) {
        return new DataPointTestFactory()
    }
}


/**
 * Generate DataPoints using a factory. 
 * Inherit all factories from this class, DataPointFactory. It should only have one method, generate().
 */
// ??? Grails will probably also inject methods into the factory classes, not just into domain classes. This should be fine (doesn't work with an interface, though).
abstract class DataPointFactory {
    protected DataPointFactory() {
    }

    /**
     * Make a single observation --- sample a new data point at the current time, whatever it means in your particular case.
     * Perhaps record the current CPU load or retrieve some stock price.
     *
     * @return  Newly generated DataPoint.
     */
    public abstract DataPoint generate();
}

// ??? Grails will probably also inject methods into the factory classes, not just into domain classes. This should be fine.
class DataPointTestFactory extends DataPointFactory {
    static DataPoint generate() {
        DataPoint result = new DataPoint()
        result.x = 2
        result.y = 3
        return result        
    }
}
