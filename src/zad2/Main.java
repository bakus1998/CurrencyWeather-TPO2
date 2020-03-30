/**
 *
 *  @author Baka Krzysztof S16696
 *
 */

package zad2;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String[] args) {
    Service s = new Service("Germany");
    String weatherJson = s.getWeather("Rome");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Wikipedia");
    MyBrowser myBrowser = new MyBrowser();
    Scene scene = new Scene(myBrowser, 800, 800);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
