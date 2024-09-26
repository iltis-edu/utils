package de.tudortmund.cs.iltis.utils.weblib;

public class WebLibAdapter {
    private static String body;
    static WebLibManager libs =
            WebLibManager.initFromConfig(WebLibAdapter.class.getResourceAsStream("/config.xml"))
                    .get();

    public static WebLib getWebLib(String name) {
        return libs.getLibrary(name).orElseThrow(IllegalArgumentException::new);
    }

    public static WebLibFunction getWebLibFunction(String webLibName, String functionName) {
        return getWebLib(webLibName)
                .getFunction(functionName)
                .orElseThrow(IllegalArgumentException::new);
    }

    public static String call(String webLibName, String functionName, String argument) {
        WebLibFunction fn = getWebLibFunction(webLibName, functionName);
        fn.call(
                argument,
                new WebLibFunctionCallHandler() {
                    @Override
                    public void onSuccess(String body) {
                        WebLibAdapter.body = body;
                    }

                    @Override
                    public void onError(Exception exception) {
                        throw new RuntimeException(exception);
                    }
                });
        return WebLibAdapter.body;
    }

    public static String call(
            String webLibName, String functionName, String argument, long timeoutSeconds) {
        WebLibFunction fn = getWebLibFunction(webLibName, functionName);
        fn.call(
                argument,
                timeoutSeconds,
                new WebLibFunctionCallHandler() {
                    @Override
                    public void onSuccess(String body) {
                        WebLibAdapter.body = body;
                    }

                    @Override
                    public void onError(Exception exception) {
                        throw new RuntimeException(exception);
                    }
                });
        return WebLibAdapter.body;
    }
}
