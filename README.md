# PGame SDK for Windows Java

Java SDK đăng nhập PGame qua Web OAuth dành cho game Windows x86_64. Artifact
đã chứa sẵn `PGameSDK.dll` và `PGameSDKJNI.dll`; SDK tự giải nén và load native
libraries, partner không cần copy DLL hoặc cấu hình `java.library.path`.

## Cài đặt bằng Gradle/JitPack

```gradle
repositories {
    mavenCentral()
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation "com.github.dhpl:pgame-sdk-java:0.1.0"
}
```

## Sử dụng

```java
import vn.pgame.sdk.PGameClient;
import vn.pgame.sdk.PGameConfig;
import vn.pgame.sdk.PGameEnvironment;
import vn.pgame.sdk.PGameLoginResult;

PGameConfig config = PGameConfig.builder("YOUR_CLIENT_ID")
        .environment(PGameEnvironment.SANDBOX)
        .build();

try (PGameClient client = new PGameClient(config)) {
    PGameLoginResult result = client.login();
    // Gửi result.getCode() + result.getRedirectUri() về backend partner.
}
```

Không đặt `clientSecret` trong game. Backend partner dùng `code` và
`redirectUri` để gọi token endpoint.

## Môi trường

| Giá trị | Authorize URL |
|---|---|
| `SANDBOX` | `https://staging.pgame.vn/oauth/authorize` |
| `PRODUCTION` | `https://pgame.vn/oauth/authorize` |
| `LOCAL` | `http://localhost:3001/oauth/authorize` |

## Yêu cầu

- Windows 10/11 x86_64
- JRE/JDK 8 trở lên, 64-bit

## Build và test

```bash
./gradlew clean check jar
```

Smoke test chạy được trên macOS/Linux mà không load DLL. OAuth end-to-end cần
chạy artifact trên Windows x86_64.
