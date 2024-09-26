package de.tudortmund.cs.iltis.utils.weblib;

import java.io.InputStream;

/** A sample application to see how this library could/should be used. */
public class App {

    public static void main(String[] args) {

        // Loads the config from the resources folder (note the leading slash!!)
        // This should be changed to individual configuration files for each project which uses this
        // one as a dependency
        InputStream inputStream = App.class.getResourceAsStream("/config.xml");

        // The loose coupling makes it likely that either parsing of "look-up by name" may fail,
        // hence the Optionals
        WebLibManager libs = WebLibManager.initFromConfig(inputStream).get();
        WebLib princess = libs.getLibrary("princess").get();
        WebLibFunction checkFormula = princess.getFunction("checkFormula").get();

        // The encoding/format of the arguments depends solely on the web lib

        // Example 1: valid formula
        String arguments =
                "{\n"
                        + "\"options\" : [\"-inputFormat=pri\", \"-quiet\"],\n"
                        + "\"formula\" : \"\\\\functions { int x, y, z; } \\\\problem { x > y & y > z -> x > z}\"\n"
                        + "}";

        // Example 2: invalid formula
        //		String arguments = "{\n" +
        //			"\"options\" : [\"-inputFormat=pri\", \"-quiet\"],\n" +
        //			"\"formula\" : \"\\\\functions { int x, y, z; } \\\\problem { x > y & y > z -> x <
        // z}\"\n" +
        //			"}";

        // Example 3: malformed formula (triggers the onError handling mechanism
        //		String arguments = "{\n" +
        //			"\"options\" : [\"-inputFormat=pri\", \"-quiet\"],\n" +
        //			"\"formula\" : \"\\\\functions { int x, y, z; } \\\\problem { x > y & y > z -> x > >
        // z}\"\n" +
        //			"}";

        checkFormula.call(
                arguments,
                new WebLibFunctionCallHandler() {
                    @Override
                    public void onSuccess(String body) {
                        System.out.println(body);
                    }

                    @Override
                    public void onError(Exception exception) {
                        System.out.println(exception.getMessage());
                    }
                });
    }
}
