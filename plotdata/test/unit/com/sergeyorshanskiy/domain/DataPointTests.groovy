package com.sergeyorshanskiy.domain



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(DataPoint)
class DataPointTests {

    void testSomething() {
        DataPoint dp = com.sergeyorshanskiy.domain.DataPoint.getFactory().generate()
        //      fail "Implement me"
    }
}
