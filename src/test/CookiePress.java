package geeks.aretotally.in.cookiemonsta;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CookiePress extends CookiePresser {

	/** Logger. */
	protected static Logger logger = Logger.getLogger( CookiePress.class.getCanonicalName() );

	/** The response. */
	private final HttpServletResponse response;

	/** The max age. */
	private Integer maxAge;

	/**
	 * The Constructor.
	 * @param cookieName the cookie name
	 * @param request the request
	 * @param response the response
	 */
	public CookiePress(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		super( request, cookieName );
		this.response = response;
	}

	/**
	 * The Constructor.
	 * @param maxAge the max age
	 * @param cookieName the cookie name
	 * @param request the request
	 * @param response the response
	 */
	public CookiePress(HttpServletRequest request, HttpServletResponse response, String cookieName, int maxAge) {
		super( request, cookieName );
		this.response = response;
		this.maxAge = maxAge;
	}

	/**
	 * Add.
	 * @param value the value
	 * @param name the name
	 */
	public void add(String name, String value) {
		this.record.put( name, value );
	}

	/**
	 * Add.
	 * @param values the values
	 * @param name the name
	 */
	public void add(String name, List<String> values) {
		this.record.put( name, values );
	}

	/**
	 * Removes the cookie.
	 */
	public void delete() {
		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName( this.cookieName );
		cg.removeCookie( this.response );
	}

	/**
	 * Save.
	 * @param expDate the exp date
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	public void save(Date expDate) throws Exception {
		if ( expDate != null ) {
			this.add( COOKIE_EXPIRATION, String.valueOf( expDate.getTime() ) );
		}

		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName( this.cookieName );

		if ( this.maxAge != null ) {
			cg.setCookieMaxAge( this.maxAge );
		}

		StringBuilder holder = new StringBuilder();

		int counter = 0;
		int maxSize = this.record.keySet().size();

		for ( String key : this.record.keySet() ) {
			holder.append( key );
			holder.append( COOKIE_COMPARISON_DELIMITER );

			Object value = this.record.get( key );

			if ( value instanceof String ) {
				holder.append( this.record.get( key ) );
			} else if ( value instanceof List ) {
				holder.append( this.buildFormattedList( (List) value ) );
			}

			if ( ++counter != maxSize ) {
				holder.append( COOKIE_RECORD_DELIMITER );
			}
		}

		String encryptedValue = SecretWriting.encrypt( holder.toString() );
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer( encryptedValue, "\n" );
		while ( st.hasMoreTokens() ) {
			sb.append( st.nextToken() );
			sb.append( ENDOFLINE );
		}

		cg.addCookie( this.response, sb.toString() );
	}

	/**
	 * Save.
	 */
	@SuppressWarnings("unchecked")
	public void save() {
		try {
			this.save( null );
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	/**
	 * Builds the formatted list.
	 * @param listValues the list values
	 * @return the string
	 */
	private String buildFormattedList(List<String> listValues) {
		StringBuilder result = new StringBuilder();

		for ( int i = 0; i < listValues.size(); i++ ) {
			result.append( listValues.get( i ) );

			if ( i != listValues.size() - 1 ) {
				result.append( COOKIE_LIST_DELIMITER );
			}
		}

		return result.toString();
	}
}