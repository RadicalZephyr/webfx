package radicalzephyr;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

public class WebFX extends Application {
    private String domain;
    private String port;
    private String width;
    private String height;

    @Override
    public void init() throws Exception {
        Map<String, String> args = getParameters().getNamed();

        this.domain = args.getOrDefault("domain", "localhost");
        this.port   = args.getOrDefault("port", "3000");

        this.width  = args.getOrDefault("width", "1024");
        this.height = args.getOrDefault("height", "768");
    }

    @Override
    public void start(Stage primaryStage) {
        WebView browser = new WebView();
        WebEngine engine = browser.getEngine();

        String domain = "localhost";
        String port = "9393";

        engine.load("http://" + domain + ":" + port);

        int width = 1024;
        int height = 768;
        Scene s = new Scene(browser, width, height);

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
