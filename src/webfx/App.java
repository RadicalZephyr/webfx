package webfx;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

public class App extends Application {
    private String windowTitle;

    private String domain;
    private String port;

    private int width;
    private int height;

    private int getIntArgOrDefault(Map<String, String> args, String argName, int defaultValue) {
        try {
            return Integer.parseInt(args.getOrDefault(argName, ""));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public void init() throws Exception {
        Map<String, String> args = getParameters().getNamed();

        this.windowTitle = args.getOrDefault("windowTitle", "Java WebFX Application");

        this.domain = args.getOrDefault("domain", "localhost");
        this.port   = args.getOrDefault("port", "3000");

        this.width  = getIntArgOrDefault(args, "width", 1024);
        this.height = getIntArgOrDefault(args, "height", 768);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(this.windowTitle);

        WebView browser = new WebView();
        WebEngine engine = browser.getEngine();

        engine.load("http://" + this.domain + ":" + this.port);

        Scene s = new Scene(browser, this.width, this.height);

        primaryStage.setScene(s);
        primaryStage.show();
    }

    private static String[] constructArgVector(Map<String, Object> options) {
        List<String> args = new ArrayList<String>();

        for (Entry<String, Object> option: options.entrySet()) {
            String argName  = option.getKey();
            String argValue = option.getValue().toString();
            args.add(String.format("--%s=%s", argName, argValue));
        }

        return args.toArray(new String[0]);
    }

    public static void start(Map<String, Object> options) {
        String[] args = constructArgVector(options);
        launch(args);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
