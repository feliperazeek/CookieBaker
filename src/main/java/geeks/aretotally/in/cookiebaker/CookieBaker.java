package geeks.aretotally.in.cookiebaker;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * CookieBaker helps you save and read simple pojos as cookies
 * 
 * <p><b>Read Cookie</b></p>
 * <p>SamplePojo o = CookieBaker.getCookie(request, SamplePojo.class, name);</p>
 * <p><b>Write Cookie</b></p>
 * <p>CookieBaker.saveCookie(response, name, new SamplePojo(arg1, arg2));</p>
 * 
 * @author Felipe Oliveira
 * @version 0.1
 * 
 */
public abstract class CookieBaker {
	
	/** Logger - Log is never enough!. */
	protected static Logger logger = Logger.getLogger(CookieBaker.class.getCanonicalName());

	/** The mapper. */
	private static transient ObjectMapper mapper;

	/**
	 * Gets the mapper.
	 * 
	 * @return the mapper
	 */
	private static ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
		}
		return mapper;
	}

	/**
	 * Gets the values.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param request
	 *            the request
	 * @param clazz
	 *            the clazz
	 * @param name
	 *            the name
	 * @return the values
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T getCookie(HttpServletRequest request, Class<T> clazz,String name) {
		try {
			// Get Cookie
			Cookie cookie = getCookieByName(request, name);
			if (cookie == null || cookie.getValue() == null) {
				return null;
			}
			
			// Get Value
			String value = cookie.getValue();
			
			// Get Token
			String existingCryptToken = HmacKeyUtil.seperateTokenFromCookieString(value);
			String generatedToken = HmacKeyUtil.getCryptographicToken(HmacKeyUtil.seperateCookieStringFromToken(value));
			
			// Check Token
			if ( existingCryptToken == null || generatedToken == null ) {
				throw new RuntimeException("Invalid Null Token!");
			}
			if(!existingCryptToken.equals(generatedToken)) {
				throw new RuntimeException("Invalid Token Match - Existing Token: " + existingCryptToken + ", Generated Token: " + generatedToken);
			}
			
			// Map Object
			Object data = getMapper().readValue(cookie.getValue(), clazz);
			return (T) data;
			
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	/**
	 * Save.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param response
	 *            the response
	 * @param name
	 *            the name
	 * @param object
	 *            the object
	 */
	public static <T extends Serializable> void saveCookie(HttpServletResponse response,
			String name, T object) {
		saveCookie(response, name, object, "/", -1, null);
	}

	/**
	 * Save.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param response
	 *            the response
	 * @param name
	 *            the name
	 * @param object
	 *            the object
	 * @param path
	 *            the path
	 * @param maxAge
	 *            the max age
	 * @param domain
	 *            the domain
	 */
	public static <T extends Serializable> void saveCookie(HttpServletResponse response, String name, T object, String path, Integer maxAge, String domain) {
		try {
			String value = getMapper().writeValueAsString(object);
			setCookie(response, name, value, path, maxAge, domain);
		
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	/**
	 * Sets the cookie.
	 * 
	 * @param response
	 *            the response
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 * @param path
	 *            the path
	 * @param maxAge
	 *            the max age
	 * @param domain
	 *            the domain
	 */
	private static void setCookie(HttpServletResponse response, String name, String value, String path, Integer maxAge, String domain) {
		if (response == null) {
			return;
		}
		String token = HmacKeyUtil.getCryptographicToken(value);
		value = value + token;
		Cookie c = new Cookie(name, value);
		c.setPath(path);
		c.setMaxAge(maxAge);
		if ( domain != null ) {
			c.setDomain(domain);
		}
		response.addCookie(c);
	}

	/**
	 * Gets the cookie by name.
	 * 
	 * @param request
	 *            the request
	 * @param name
	 *            the name
	 * @return the cookie by name
	 */
	private static Cookie getCookieByName(HttpServletRequest request, String name) {
		if (request == null) {
			return null;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			Cookie c;
			for (int i = 0; i < cookies.length; i++) {
				c = cookies[i];
				if (c != null && c.getName().equals(name)) {
					return c;
				}
			}
		}
		return null;
	}
}
