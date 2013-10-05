package com.sergeyorshanskiy.domain

import com.sergeyorshanskiy.domain.*

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(StockPrice)
class StockPriceTests {

    // TODO: make the test smarter
    void testSomething() {
        // FIXME: unit tests shouldn't relly on Internet connection...
        StockPrice sp = StockPrice.getFactory("AMZN").generate() // TODO: check something about the numbers
        assertEquals sp.ticker, "AMZN"
        
        assertNull( StockPrice.getFactory("NO_SUCH_TICKER").generate() )

        StockPrice.getFactory(["AAPL", "AMZN"]).generate() // will not construct a valid factory as the argument is not a string; defaults to factory from DataPoint
//       fail "Implement me"
    }
}
