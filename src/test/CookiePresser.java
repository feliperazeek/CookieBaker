package geeks.aretotally.in.cookiemonsta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class HelixCookieLookup.
 * 
 * @author Jean Aristide Date Apr 4, 2007 9:36:54 AM
 */
public class CookiePresser {

	/** Logger. */
	private static Logger logger = Logger.getLogger(CookiePresser.class.getCanonicalName());

	/** The request. */
	protected HttpServletRequest request;

	/** The cookie name. */
	protected String cookieName;

	/** The Constant COOKIE_RECORD_DELIMITER. */
	protected static final String COOKIE_RECORD_DELIMITER = "RECORD_DEL";

	/** The Constant COOKIE_LIST_DELIMITER. */
	protected static final String COOKIE_LIST_DELIMITER = "LIST_DEL";

	/** The Constant COOKIE_COMPARISON_DELIMITER. */
	protected static final String COOKIE_COMPARISON_DELIMITER = "COMPARE_DEL";

	/** The Constant COOKIE_EXPIRATION. */
	public static final String COOKIE_EXPIRATION = "COOKIE_EXPI_RA__ION";

	/** The record. */
	protected Map<String, Object> record = new HashMap<String, Object>();

	/** The ENDOFLINE. */
	protected static String ENDOFLINE = "_N|0F~_";

	/**
	 * The Constructor.
	 * 
	 * @param request
	 *            the request
	 * @param cookieName
	 *            the cookie name
	 */
	public CookiePresser(HttpServletRequest request, String cookieName) {
		this.request = request;
		this.cookieName = cookieName;

		try {
			this.loadMap();
		} catch (Exception e) {
			logger.log(Level.WARNING, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * Gets the cookie.
	 * 
	 * @param request
	 *            the request
	 * @param name
	 *            the name
	 * @return the cookie
	 */
	private Cookie getCookie(HttpServletRequest request, String name) {
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (name.equals(cookies[i].getName())) {
					return cookies[i];
				}
			}
		}
		return null;
	}

	/**
	 * Load map.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	protected void loadMap() throws Exception {
		if (this.request != null) {
			Cookie cookie = this.getCookie(this.request, this.cookieName);
			if (cookie != null && cookie.getValue() != null
					& !"null".equalsIgnoreCase(cookie.getValue())) {
				String encryptedValue = cookie.getValue();
				encryptedValue = encryptedValue.replace(ENDOFLINE, "\n");
				String decryptedValue = SecretWriting.decrypt(encryptedValue);
				this.extractCookieValues(decryptedValue);
			}
		}
	}

	/**
	 * Extract cookie values.
	 * 
	 * @param decryptedValue
	 *            the decrypted value
	 */
	protected void extractCookieValues(String decryptedValue) {
		String[] records = decryptedValue.split(COOKIE_RECORD_DELIMITER);

		for (String rcrd : records) {
			String attributes[] = rcrd.split(COOKIE_COMPARISON_DELIMITER);
			if (attributes.length == 2) {
				String keyAttribute = attributes[0];
				String valueAttribute = attributes[1];

				if (!valueAttribute.contains(COOKIE_LIST_DELIMITER)) {
					this.record.put(keyAttribute, valueAttribute);
				} else {
					this.record.put(
							keyAttribute,
							new ArrayList<String>(Arrays.asList(valueAttribute
									.split(COOKIE_LIST_DELIMITER))));
				}
			}
		}
	}

	/**
	 * Gets the value.
	 * 
	 * @param name
	 *            the name
	 * @return the value
	 */
	@SuppressWarnings("unchecked")
	public String getValue(String name) {
		Object obj = this.record.get(name);
		if (obj != null) {
			if (obj instanceof String) {
				return (String) obj;
			} else if (obj instanceof List) {
				List<String> values = (List<String>) obj;
				if (!values.isEmpty()) {
					return values.get(0);
				}
			}
		}

		return null;
	}

	/**
	 * Gets the values.
	 * 
	 * @param name
	 *            the name
	 * @return the values
	 */
	@SuppressWarnings("unchecked")
	public List<String> getValues(String name) {
		Object obj = this.record.get(name);
		if (obj != null) {
			if (obj instanceof List) {
				return (List<String>) obj;
			} else if (obj instanceof String) {
				List<String> o = new ArrayList<String>();
				o.add((String) obj);
				return o;
			}
		}
		return new ArrayList<String>();
	}

	/**
	 * Exists.
	 * 
	 * @return true, if exists
	 */
	public boolean exists() {
		Cookie cookie = this.getCookie(this.request, this.cookieName);

		if (cookie == null || cookie.getValue() == null
				|| "null".equalsIgnoreCase(cookie.getValue())) {
			return false;
		}

		return true;
	}

	/**
	 * Expired.
	 * 
	 * @return true, if expired
	 */
	public boolean expired() {
		String expString = this.getValue(COOKIE_EXPIRATION);
		if (expString == null) {
			return false;
		}

		Date date = new Date(Long.parseLong(expString));
		return date.before(new Date());
	}

	/**
	 * Gets the record.
	 * 
	 * @return Returns the record.
	 */
	public Map<String, Object> getRecord() {
		return this.record;
	}

	/**
	 * Overriding Method.
	 * 
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("############################################\n");
		sb.append("CookieName:\t");
		sb.append(this.cookieName);
		sb.append("\n");

		for (String key : this.record.keySet()) {
			sb.append(key);
			sb.append(" --->\t{");
			sb.append(this.record.get(key));
			sb.append("}\n");
		}
		sb.append("############################################\n");

		return sb.toString();
	}
}
