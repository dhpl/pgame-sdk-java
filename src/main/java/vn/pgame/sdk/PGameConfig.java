package vn.pgame.sdk;

public final class PGameConfig {
    private final PGameEnvironment environment;
    private final String clientId;
    private final String scope;
    private final String authorizeUrl;
    private final int timeoutSeconds;

    private PGameConfig(Builder builder) {
        environment = builder.environment;
        clientId = builder.clientId;
        scope = builder.scope;
        authorizeUrl = builder.authorizeUrl;
        timeoutSeconds = builder.timeoutSeconds;
    }

    public static Builder builder(String clientId) {
        return new Builder(clientId);
    }

    PGameEnvironment environment() { return environment; }
    String clientId() { return clientId; }
    String scope() { return scope; }
    String authorizeUrl() { return authorizeUrl; }
    int timeoutSeconds() { return timeoutSeconds; }

    public static final class Builder {
        private PGameEnvironment environment = PGameEnvironment.SANDBOX;
        private final String clientId;
        private String scope = "openid profile email phone";
        private String authorizeUrl;
        private int timeoutSeconds = 300;

        private Builder(String clientId) {
            if (clientId == null || clientId.trim().isEmpty()) {
                throw new IllegalArgumentException("clientId is required");
            }
            this.clientId = clientId;
        }

        public Builder environment(PGameEnvironment value) {
            environment = value;
            return this;
        }

        public Builder scope(String value) {
            scope = value;
            return this;
        }

        public Builder authorizeUrl(String value) {
            authorizeUrl = value;
            return this;
        }

        public Builder timeoutSeconds(int value) {
            timeoutSeconds = value;
            return this;
        }

        public PGameConfig build() {
            return new PGameConfig(this);
        }
    }
}
