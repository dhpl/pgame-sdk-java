package vn.pgame.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

final class NativeLibraryLoader {
    private static final String VERSION = "0.1.0";
    private static final String RESOURCE_ROOT =
            "/META-INF/native/windows-x86_64/";
    private static boolean loaded;

    private NativeLibraryLoader() {}

    static synchronized void load() {
        if (loaded) return;

        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        String architecture = System.getProperty("os.arch", "").toLowerCase(Locale.ROOT);
        if (!os.startsWith("windows") || !(architecture.equals("amd64") || architecture.equals("x86_64"))) {
            throw new UnsatisfiedLinkError(
                    "PGame SDK supports Windows x86_64 only; detected " + os + "/" + architecture);
        }

        try {
            Path directory = Files.createTempDirectory("pgame-sdk-" + VERSION + "-");
            directory.toFile().deleteOnExit();
            Path core = extract(directory, "PGameSDK.dll");
            Path jni = extract(directory, "PGameSDKJNI.dll");

            // Load the core first so the Windows loader can resolve the JNI DLL dependency.
            System.load(core.toAbsolutePath().toString());
            System.load(jni.toAbsolutePath().toString());
            loaded = true;
        } catch (IOException error) {
            UnsatisfiedLinkError linkError = new UnsatisfiedLinkError(
                    "Cannot extract PGame native libraries: " + error.getMessage());
            linkError.initCause(error);
            throw linkError;
        }
    }

    static boolean isSupportedPlatform(String osName, String architecture) {
        String os = osName == null ? "" : osName.toLowerCase(Locale.ROOT);
        String arch = architecture == null ? "" : architecture.toLowerCase(Locale.ROOT);
        return os.startsWith("windows") && (arch.equals("amd64") || arch.equals("x86_64"));
    }

    private static Path extract(Path directory, String fileName) throws IOException {
        String resourceName = RESOURCE_ROOT + fileName;
        Path destination = directory.resolve(fileName);

        try (InputStream input = NativeLibraryLoader.class.getResourceAsStream(resourceName)) {
            if (input == null) {
                throw new IOException("Missing JAR resource " + resourceName);
            }
            Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
        }

        destination.toFile().deleteOnExit();
        return destination;
    }
}
