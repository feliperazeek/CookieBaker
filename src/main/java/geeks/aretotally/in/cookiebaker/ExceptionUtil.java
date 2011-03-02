package geeks.aretotally.in.cookiebaker;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionUtil {

    /**
     * Get Print Stack Trace.
     * @param exception the exception
     * @return a String representation of the stack trace
     */
    public static String getStackTrace(Exception exception) {
            return getStackTrace( (Throwable) exception );
    }

    /**
     * Get Print Stack Trace.
     * @param exception the exception
     * @return a String representation of the stack trace
     */
    public static String getStackTrace(Throwable exception) {
            return getStackTrace( null, exception );
    }

    /**
     * Get Print Stack Trace.
     * @param exception the exception
     * @param title the title
     * @return a String representation of the stack trace
     */
    public static String getStackTrace(String title, Exception exception) {
            return getStackTrace( title, (Throwable) exception );
    }

    /**
     * Get Print Stack Trace.
     * @param exception the exception
     * @param title the title
     * @return a String representation of the stack trace Note: RJS - 02/10/2006 - It is more favorable to put all the logic in one method than to have the same
     *         logic duplicated across multiple methods.
     */
    public static String getStackTrace(String title, Throwable exception) {
            StringBuffer sb = new StringBuffer();
            sb.append( "\n" );
            if ( title != null ) {
                    sb.append( title );
                    sb.append( "\n\n" );
            }
            if ( exception != null ) {
                    ByteArrayOutputStream ostr = new ByteArrayOutputStream();
                    exception.printStackTrace( new PrintStream( ostr ) );
                    sb.append( ostr );
            }
            return sb.toString();
    }
}

