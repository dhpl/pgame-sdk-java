package vn.pgame.sdk;

public final class PGameSdkSmokeTest {
    public static void main(String[] args) throws Exception {
        PGameConfig config = PGameConfig.builder("test-client")
                .environment(PGameEnvironment.SANDBOX)
                .scope("openid profile")
                .timeoutSeconds(30)
                .build();
        require(config != null, "config should be created");

        boolean rejectedEmptyClient = false;
        try {
            PGameConfig.builder("  ");
        } catch (IllegalArgumentException expected) {
            rejectedEmptyClient = true;
        }
        require(rejectedEmptyClient, "empty clientId should be rejected");

        require(NativeLibraryLoader.isSupportedPlatform("Windows 11", "amd64"),
                "Windows amd64 should be supported");
        require(!NativeLibraryLoader.isSupportedPlatform("Mac OS X", "x86_64"),
                "macOS should be rejected");

        PGameLoginResult result = new PGameLoginResult(
                "code", "state", "http://127.0.0.1:49152/pgame-callback");
        require("code".equals(result.getCode()), "code getter mismatch");
        require(result.getRedirectUri().contains("49152"), "redirect URI mismatch");

        Class.forName("vn.pgame.sdk.PGameClient", false,
                PGameSdkSmokeTest.class.getClassLoader());
        System.out.println("PGame Java SDK smoke test passed");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
