package com.sergeyorshanskiy.domain



/**
 * Generate DataPoints using a factory.
 * Inherit all factories from this class, DataPointFactory. It should only have one method, generate().
 */
// ??? Grails will probably also inject methods into the factory classes, not just into domain classes. This should be fine (doesn't work with an interface, though).
public abstract class DataPointFactory {
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
