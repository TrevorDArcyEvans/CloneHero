package cz.jcu.prf.uai.javamugs.clonehero;

import cz.jcu.prf.uai.javamugs.clonehero.gui.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.Objects;

public class App extends Application
{
  private static boolean screencap;

  public static void main(String[] args)
  {
    if (args.length == 1)
    {
      screencap = Objects.equals(args[0], "screencap");
    }
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    var loader = new FXMLLoader(getClass().getClassLoader().getResource("Menu.fxml"));
    Parent root = loader.load();
    MenuController controller = loader.getController();
    primaryStage.setTitle("Clone Hero");
    primaryStage.setScene(new Scene(root));
    primaryStage.setResizable(false);
    primaryStage.getIcons().add(new Image(getClass().getResource("/icon.png").toString()));
    primaryStage.show();
    controller.start(screencap);
  }
}
