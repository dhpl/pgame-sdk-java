package vn.pgame.sdk;

public final class PGameException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final int errorCode;

    public PGameException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
