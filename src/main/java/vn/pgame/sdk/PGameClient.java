package vn.pgame.sdk;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

public final class PGameClient implements AutoCloseable {
    static {
        NativeLibraryLoader.load();
    }

    private volatile long nativeHandle;
    private int activeLogins;

    public PGameClient(PGameConfig config) {
        if (config == null) throw new IllegalArgumentException("config is required");
        nativeHandle = nativeCreate(
                config.environment().nativeValue(),
                config.clientId(),
                config.scope(),
                config.authorizeUrl(),
                config.timeoutSeconds());
    }

    public PGameLoginResult login() {
        final long handle;
        synchronized (this) {
            handle = nativeHandle;
            if (handle == 0) throw new IllegalStateException("PGameClient is closed");
            activeLogins++;
        }

        try {
            return nativeLogin(handle);
        } finally {
            synchronized (this) {
                activeLogins--;
                notifyAll();
            }
        }
    }

    public CompletableFuture<PGameLoginResult> loginAsync() {
        return loginAsync(ForkJoinPool.commonPool());
    }

    public CompletableFuture<PGameLoginResult> loginAsync(Executor executor) {
        return CompletableFuture.supplyAsync(this::login, executor);
    }

    public synchronized void cancel() {
        final long handle = nativeHandle;
        if (handle != 0) nativeCancel(handle);
    }

    @Override
    public void close() {
        final long handle;
        synchronized (this) {
            handle = nativeHandle;
            if (handle == 0) return;
            nativeHandle = 0;
        }

        nativeCancel(handle);
        boolean interrupted = false;
        synchronized (this) {
            while (activeLogins > 0) {
                try {
                    wait();
                } catch (InterruptedException error) {
                    interrupted = true;
                }
            }
        }
        nativeDestroy(handle);
        if (interrupted) Thread.currentThread().interrupt();
    }

    private static native long nativeCreate(
            int environment,
            String clientId,
            String scope,
            String authorizeUrl,
            int timeoutSeconds);

    private static native PGameLoginResult nativeLogin(long handle);
    private static native void nativeCancel(long handle);
    private static native void nativeDestroy(long handle);
}
