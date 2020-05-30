package android.werner.apachelogparser

import android.werner.apachelogparser.algorithm.LogFrequencyCounter
import android.werner.apachelogparser.models.LogFrequency
import org.junit.Test

import org.junit.Assert.*

class FrequencyCounterUnitTest {
    @Test
    fun logFrequencyObjectsAreEqual() {
        val log1 = LogFrequency("hi","hi","hi", 2)
        val log2 = LogFrequency("hi","hi","hi", 2)
        assertEquals(log1, log2)
    }

    @Test
    fun logFrequencyObjectsAreNotEqual() {
        val log1 = LogFrequency("hi","hi","hi", 3)
        val log2 = LogFrequency("yo","hi","hi", 2)
        assertNotEquals(log1, log2)
    }

    @Test
    fun testMostFrequentLog1() {
        val mockLog =
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page1/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.2 - - [03/Sep/2013:18:34:48 -0600] \"GET /page1/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.2 - - [03/Sep/2013:18:34:48 -0600] \"GET /page2/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.2 - - [03/Sep/2013:18:34:48 -0600] \"GET /page3/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.2 - - [03/Sep/2013:18:34:48 -0600] \"GET /page2/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page2/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page3/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page4/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page1/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page2/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page2/ HTTP/1.1\" 500 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n"

        val frequency = LogFrequencyCounter.getLogFrequencyData(mockLog)
        val expectedLog1 = LogFrequency("/page1/", "/page2/", "/page3/", 2)
        assertEquals(frequency[0], expectedLog1)
    }

    @Test
    fun testMostFrequentLog2() {
        val mockLog =
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page1/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page1/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page1/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n"

        val frequency = LogFrequencyCounter.getLogFrequencyData(mockLog)
        val expectedLog1 = LogFrequency("/page1/", "/page1/", "/page1/", 1)
        assertEquals(frequency[0], expectedLog1)
    }

    @Test
    fun testNotEnoughData() {
        val mockLog =
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page1/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page1/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n"

        val frequency = LogFrequencyCounter.getLogFrequencyData(mockLog)
        assertEquals(frequency.size,0)
    }

    @Test
    fun testSequenceCount() {
        val mockLog =
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page1/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page2/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page3/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page4/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page5/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page6/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page7/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page8/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
            "123.4.5.1 - - [03/Sep/2013:18:34:48 -0600] \"GET /page9/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n"

        val frequency = LogFrequencyCounter.getLogFrequencyData(mockLog)
        assertEquals(frequency.size,7)
    }
}
