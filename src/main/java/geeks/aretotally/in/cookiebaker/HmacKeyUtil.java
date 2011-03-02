package geeks.aretotally.in.cookiebaker;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

// TODO: Auto-generated Javadoc
/**
 * The Class SecretWritingPassPhrase.
 */
public abstract class HmacKeyUtil {

	/** Logger - Log is never enough!. */
	protected static Logger logger = Logger.getLogger(HmacKeyUtil.class
			.getCanonicalName());

	/** The Constant CODE_DIGITS. */
	private static final int CODE_DIGITS = 5;

	/** The Constant HMAC. */
	private static final String HMAC = "HmacSHA1";

	/** The Constant SECRET. */
	private static final String SECRET = "WRHLCESS!DDE";

	/**
	 * Hmac_sha1.
	 * 
	 * @param keyBytes
	 *            the key bytes
	 * @param text
	 *            the text
	 * @return the byte[]
	 */
	private static byte[] hmac_sha1(byte[] keyBytes, byte[] text) {
		try {
			Mac hmacSha1;
			try {
				hmacSha1 = Mac.getInstance(HMAC);
			} catch (NoSuchAlgorithmException nsae) {
				return new byte[0];
			}
			SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
			hmacSha1.init(macKey);
			return hmacSha1.doFinal(text);
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	/**
	 * Gets the cryptographic token.
	 * 
	 * @param text
	 *            the text
	 * @return the cryptographic token
	 */
	public static String getCryptographicToken(String text) {
		try {
			byte[] secretKey = SECRET.getBytes();
			byte[] hash = hmac_sha1(secretKey, text.getBytes());
			// Mask the output and get the first codeDigit characters
			// as the cryptographic token
			int offset = hash[hash.length - 1] & 0xf;
			int binary = ((hash[offset] & 0x7f) << 24)
					| ((hash[offset + 1] & 0xff) << 16)
					| ((hash[offset + 2] & 0xff) << 8)
					| (hash[offset + 3] & 0xff);

			double otp = binary % Math.pow(10, CODE_DIGITS);
			String result = Integer.toString((int) otp);
			while (result.length() < CODE_DIGITS) {
				result = "0" + result;
			}
			return result;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	/**
	 * Seperate token from cookie string.
	 * 
	 * @param cookieString
	 *            the cookie string
	 * @return the string
	 */
	public static String seperateTokenFromCookieString(String cookieString) {
		return cookieString.substring(cookieString.length() - CODE_DIGITS,
				cookieString.length());
	}

	/**
	 * Seperate cookie string from token.
	 * 
	 * @param cookieString
	 *            the cookie string
	 * @return the string
	 */
	public static String seperateCookieStringFromToken(String cookieString) {
		return cookieString.substring(0, cookieString.length() - CODE_DIGITS);
	}

}
