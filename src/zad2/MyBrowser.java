package zad2;

import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MyBrowser extends Region {
    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();

    public MyBrowser(){
        webEngine.load("http://wikipedia.pl");
        webView.setPrefHeight(800);
        webView.setPrefWidth(800);
        getChildren().add(webView);
    }
}
