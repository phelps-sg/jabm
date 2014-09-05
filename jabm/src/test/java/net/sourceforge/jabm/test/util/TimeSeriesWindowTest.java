package net.sourceforge.jabm.test.util;

import junit.framework.TestCase;
import net.sourceforge.jabm.util.TimeSeriesWindow;

public class TimeSeriesWindowTest extends TestCase {

	TimeSeriesWindow window;
	
	public void setUp() {
		window = new TimeSeriesWindow(10);
	}
	
	public void testEmpty() {
//		window.addValue(0);
		for(int i=0; i<15; i++) {
			assertTrue(Double.isNaN(window.getValue(i)));
		}
	}
	
	public void testSimpleMovingAverageWithinWindow() {
		for(int i=0; i<10; i++) {
			window.addValue(i);
		}
		assertEquals(4.5, window.getSimpleMovingAverage(), 10E-6);
	}
	
	public void testSimpleMovingAverageBeyondWindow1() {
		for(int i=0; i<15; i++) {
			window.addValue(i);
		}
		assertEquals(9.5, window.getSimpleMovingAverage(), 10E-6);
	}
	
	public void testSimpleMovingAverageBeyondWindow2() {
		for(int i=0; i<25; i++) {
			window.addValue(i);
		}
		assertEquals(19.5, window.getSimpleMovingAverage(), 10E-6);
	}
	
	public void testLags() {
		for(int i=0; i<25; i++) {
			window.addValue(i);
		}
		assertEquals(24.0, window.getValue(0), 10E-6);
		for(int i=1; i<10; i++) {
			assertEquals(25 - i - 1, window.getValue(i), 10E-6);
		}
	}
}
