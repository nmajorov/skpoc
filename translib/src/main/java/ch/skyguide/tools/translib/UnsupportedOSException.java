package ch.skyguide.tools.translib;

/**
 * Exceptions that can be thrown if current OS version and architecture is not 
 * supported.
 * 
 * Translib is only tested for support Linux x86_64  or Windows
 * 
 * @author Nikolaj Majorov
 *
 */
public class UnsupportedOSException extends Exception{

    
    private static final long serialVersionUID = 1L;
    
    /** Constructs a new  exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public UnsupportedOSException() {
        super();
    }

    /** Constructs a new  exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public UnsupportedOSException(String message) {
        super(message);
    }

}
