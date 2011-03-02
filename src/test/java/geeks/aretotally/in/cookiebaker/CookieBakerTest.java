package geeks.aretotally.in.cookiebaker;

import geeks.aretotally.in.cookiebaker.CookieBaker;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class CookieBakerTest.
 */
public class CookieBakerTest extends TestCase {
	
	/** Logger - Log is never enough!. */
	protected static Logger logger = Logger.getLogger(CookieBakerTest.class.getCanonicalName());
	
	/**
	 * Instantiates a new cookie baker test.
	 *
	 * @param name the name
	 */
	public CookieBakerTest(String name) {
		super(name);
	}

	/**
	 * Test.
	 */
	public void test() {
		// Init Mock Request and Response
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		// Define Cookie Name
		String name = String.valueOf(new Random().nextLong());
		
		// Define values that will be used on Pojo for testing
		String myString = String.valueOf(new Random().nextLong());
		Integer myInteger = new Random().nextInt();
		String[] myArray = {myString, myString};
		
		// Try to get cookie - it shouldn't have anything
		Assert.assertNull( CookieBaker.getCookie(request, SampleObject.class, name) );
		
		// Get a Sample Pojo
		SampleObject o1 = this.getSample(myString, myInteger, myArray);
		
		// Save Cookie
		CookieBaker.saveCookie(response, name, o1);
		
		// Gotta pass the response cookies to request
		request.setCookies(response.getCookies());
		
		// Print Cookies
		for ( Cookie c: response.getCookies() ) {
			logger.log(Level.INFO, "Cookie - name: " + c.getName() + ", value: " + c.getValue());
		}
		
		// Now make sure it found the cookie
		Assert.assertNotNull( CookieBaker.getCookie(request, SampleObject.class, name) );
		
		// Make sure all the values match
		SampleObject o2 = CookieBaker.getCookie(request, SampleObject.class, name);
		Assert.assertEquals(myString, o2.getMyString());
		Assert.assertEquals(myInteger, o2.getMyInteger());
		Assert.assertTrue(Arrays.deepEquals(myArray, o2.getMyArray()));
		
		// Now modify the first char of the cookie value and make sure it blows up because it won't match the token
		for ( Cookie c: response.getCookies() ) {
			String value = c.getValue();
			value = "a" + value;
			c.setValue(value);
		}
		
		// Make sure it throws an exception because the cookie was modified
		boolean exception = false;
		try {
			CookieBaker.getCookie(request, SampleObject.class, name);
		} catch (Throwable t) {
			exception = true;
		}
		Assert.assertTrue(exception);
	}
	
	/**
	 * Gets the sample.
	 *
	 * @param myString the my string
	 * @param myInteger the my integer
	 * @param myArray the my array
	 * @return the sample
	 */
	private SampleObject getSample(String myString, Integer myInteger, String[] myArray) {
		return new SampleObject(myString, myInteger, myArray);
	}

}
