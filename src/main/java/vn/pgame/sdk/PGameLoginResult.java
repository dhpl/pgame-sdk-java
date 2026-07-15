package vn.pgame.sdk;

public final class PGameLoginResult {
    private final String code;
    private final String state;
    private final String redirectUri;

    public PGameLoginResult(String code, String state, String redirectUri) {
        this.code = code;
        this.state = state;
        this.redirectUri = redirectUri;
    }

    public String getCode() { return code; }
    public String getState() { return state; }
    public String getRedirectUri() { return redirectUri; }
}
