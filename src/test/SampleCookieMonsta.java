package geeks.aretotally.in.cookiemonsta;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class SampleCookieMonsta.
 */
public class SampleCookieMonsta extends CookieBaker<SampleCookieMonsta> {
	
	/** The field1. */
	private String field1;

	/** The field2. */
	private Integer field2;
	
	/**
	 * Instantiates a new sample cookie monsta.
	 *
	 * @param name the name
	 * @param request the request
	 * @param response the response
	 */
	public SampleCookieMonsta(String name, HttpServletRequest request, HttpServletResponse response) {
		super(name, request, response);
	}

	/**
	 * Gets the field1.
	 *
	 * @return the field1
	 */
	public String getField1() {
		return field1;
	}

	/**
	 * Sets the field1.
	 *
	 * @param field1 the new field1
	 */
	public void setField1(String field1) {
		this.field1 = field1;
	}

	/**
	 * Gets the field2.
	 *
	 * @return the field2
	 */
	public Integer getField2() {
		return field2;
	}

	/**
	 * Sets the field2.
	 *
	 * @param field2 the new field2
	 */
	public void setField2(Integer field2) {
		this.field2 = field2;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SampleCookieMonsta [field1=" + field1 + ", field2=" + field2
				+ "]";
	}

}
