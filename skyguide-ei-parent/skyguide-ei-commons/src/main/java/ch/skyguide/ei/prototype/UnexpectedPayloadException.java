package ch.skyguide.ei.prototype;

public class UnexpectedPayloadException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnexpectedPayloadException() {
    }

    public UnexpectedPayloadException(final String message) {
        super(message);
    }

    public UnexpectedPayloadException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnexpectedPayloadException(final Throwable cause) {
        super(cause);
    }

}
