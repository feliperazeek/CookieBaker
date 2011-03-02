package geeks.aretotally.in.cookiebaker;

import java.io.Serializable;
import java.util.Arrays;

/**
 * The Class SampleObject.
 */
public class SampleObject implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The my string. */
	private String myString;

	/** The my integer. */
	private Integer myInteger;
	
	/** The my array. */
	private String[] myArray;
	
	/**
	 * Instantiates a new sample object.
	 */
	public SampleObject() {
		
	}
	
	/**
	 * Instantiates a new sample object.
	 *
	 * @param myString the my string
	 * @param myInteger the my integer
	 * @param myArray the my array
	 */
	public SampleObject(String myString, Integer myInteger, String[] myArray) {
		super();
		this.myString = myString;
		this.myInteger = myInteger;
		this.myArray = myArray;
	}

	/**
	 * Gets the my string.
	 *
	 * @return the my string
	 */
	public String getMyString() {
		return myString;
	}

	/**
	 * Sets the my string.
	 *
	 * @param myString the new my string
	 */
	public void setMyString(String myString) {
		this.myString = myString;
	}

	/**
	 * Gets the my integer.
	 *
	 * @return the my integer
	 */
	public Integer getMyInteger() {
		return myInteger;
	}

	/**
	 * Sets the my integer.
	 *
	 * @param myInteger the new my integer
	 */
	public void setMyInteger(Integer myInteger) {
		this.myInteger = myInteger;
	}

	/**
	 * Gets the my array.
	 *
	 * @return the my array
	 */
	public String[] getMyArray() {
		return myArray;
	}

	/**
	 * Sets the my array.
	 *
	 * @param myArray the new my array
	 */
	public void setMyArray(String[] myArray) {
		this.myArray = myArray;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SampleObject [myString=" + myString + ", myInteger="
				+ myInteger + ", myArray=" + Arrays.toString(myArray) + "]";
	}
	


}
