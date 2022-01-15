package cz.jcu.prf.uai.javamugs.clonehero.gui;

import cz.jcu.prf.uai.javamugs.clonehero.logic.Game;
import cz.jcu.prf.uai.javamugs.clonehero.logic.Parser;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Locale;

public class MenuController
{
  private static final String Tracks = "tracks";

  /**
   * List of songs in 'tracks' directory inc .mp3 extension
   */
  @FXML
  public ComboBox<String> songs;

  public Label difficultyLabel;
  public Slider speedSlider;
  public Slider difficultySlider;
  public Button exitButton;
  public BorderPane rootContainer;

  private Stage stage;
  private boolean screencap;

  /**
   * Method to be called on start, after initialization
   */
  public void start(boolean screencap)
  {
    this.screencap = screencap;
    var userDir = new File(System.getProperty("user.dir"));
    var repo = new File(userDir, Tracks);
    var repoSongs = repo.list((dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(".mp3"));
    songs.getItems().setAll(repoSongs);
    songs.getSelectionModel().selectFirst();
    var image = new Image(getClass().getResource("/splash.jpg").toString());
    var backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, null);
    var background = new Background(backgroundImage);
    rootContainer.setBackground(background);
    this.stage = (Stage) rootContainer.getScene().getWindow();
    stage.setOnCloseRequest(we -> System.exit(0));
    difficultyLabel.textProperty().bind(Bindings.format("%.0f", difficultySlider.valueProperty()));
  }

  /**
   * Method to be called on play button click
   */
  public void playButtonAction() throws IOException
  {
    var userDir = new File(System.getProperty("user.dir"));
    var tracksURI = new File(userDir, Tracks);
    var songFile = new File(tracksURI, songs.getValue());
    var songURI = songFile.toURI();
    var pressChartPath = songFile.getAbsolutePath().replace(".mp3", ".prc");
    var parser = new Parser();
    var timeOffset = (int) speedSlider.getValue();
    var difficulty = (int) difficultySlider.getValue();
    var pressChart = parser.parseFile(pressChartPath, timeOffset);
    var game = new Game(timeOffset, difficulty, pressChart);

    openGameWindow(game, songURI); //TODO put method under logic
  }

  /**
   * Creates Game window
   *
   * @param game    game core
   * @param songURI path to the song
   */
  private void openGameWindow(Game game, URI songURI) throws IOException
  {
    var loader = new FXMLLoader(getClass().getResource("/Game.fxml"));
    Parent root = loader.load();
    GameController gameController = loader.getController();
    gameController.setGame(game);
    gameController.setSongURI(songURI);
    var gameStage = new Stage();
    gameStage.setTitle("Clone Hero");
    gameStage.setScene(new Scene(root));
    gameStage.setResizable(false);
    gameStage.getIcons().add(new Image(getClass().getResource("/icon.png").toString()));
    gameStage.show();
    gameController.start(screencap);
  }

  /**
   * Method to be called on editor button click
   */
  public void editorButtonAction() throws IOException
  {
    var loader = new FXMLLoader(getClass().getResource("/Editor.fxml"));
    Parent root = loader.load();
    EditorController editorController = loader.getController();
    var fileChooser = GetFileChooser();
    fileChooser.setTitle("Select song");
    fileChooser.getExtensionFilters().clear();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3", "*.mp3"));
    File songFile = fileChooser.showOpenDialog(stage);
    if (songFile == null)
    {
      return;
    }

    var editorStage = new Stage();
    editorStage.setTitle("Clone Hero Editor");
    editorStage.setScene(new Scene(root));
    editorStage.setResizable(false);
    editorStage.getIcons().add(new Image(getClass().getResource("/icon.png").toString()));
    editorStage.show();

    editorController.start(songFile.toURI().toString());
  }

  /**
   * Method to be called on exit button click
   */
  public void exitButtonAction()
  {
    stage.close();
  }

  private static FileChooser GetFileChooser()
  {
    var userDir = new File(System.getProperty("user.dir"));
    var fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(userDir);
    return fileChooser;
  }
}

