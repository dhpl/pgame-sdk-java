package vn.pgame.sdk;

public enum PGameEnvironment {
    SANDBOX(0),
    PRODUCTION(1),
    LOCAL(2);

    private final int nativeValue;

    PGameEnvironment(int nativeValue) {
        this.nativeValue = nativeValue;
    }

    int nativeValue() {
        return nativeValue;
    }
}
