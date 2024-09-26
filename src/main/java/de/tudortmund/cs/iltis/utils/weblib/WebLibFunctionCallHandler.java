package de.tudortmund.cs.iltis.utils.weblib;

/**
 * A simple callback interface to handle successful and failed web lib function calls.
 *
 * <p>In case of success, the body of the HttpResponse is provided as is (no parsing or similar).
 * <br>
 * The error handler can be called for a multitude of reasons since IO interactions are very
 * error-prone. This includes but is not limited to Security-, IO- or Interrupted-Exceptions or
 * simply a status code != 200 (which indicates some error or changes on the remote server).
 */
public interface WebLibFunctionCallHandler {

    default void onSuccess(String body) {}

    default void onError(Exception exception) {}
}
