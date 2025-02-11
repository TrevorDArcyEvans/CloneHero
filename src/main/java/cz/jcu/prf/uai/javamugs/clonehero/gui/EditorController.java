package cz.jcu.prf.uai.javamugs.clonehero.gui;

import cz.jcu.prf.uai.javamugs.clonehero.logic.Chord;
import cz.jcu.prf.uai.javamugs.clonehero.logic.Press;
import cz.jcu.prf.uai.javamugs.clonehero.logic.Saver;
import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import java.io.IOException;

public class EditorController
{
  public Label countLabel;
  public Circle circle0;
  public Circle circle1;
  public Circle circle2;
  public Circle circle3;
  public Circle circle4;
  public Button startBtn;
  public Button saveBtn;
  public TextArea textPresses;

  private Press actualPress;
  private Saver saver;
  private MediaPlayer mediaPlayer;
  private String songPath;
  private boolean isRecording;

  /**
   * Throw actual press to textarea
   *
   * @param press key press
   */
  private void setNewPressToTextarea(Press press)
  {
    var colorName = "";

    switch (press.getColor())
    {
      case 0:
        colorName = "Red\t";
        break;
      case 1:
        colorName = "Yellow";
        break;
      case 2:
        colorName = "Green";
        break;
      case 3:
        colorName = "Blue\t";
        break;
      case 4:
        colorName = "Magenta";
        break;
    }

    textPresses.setText(colorName + "\t= " + String.format ("%.4f", press.getDrawTime()) + "\n" + textPresses.getText());
  }

  /**
   * Make fade in on button press
   *
   * @param c circle which is pressed
   */
  private void setFadeIn(Circle c)
  {
    var ft = new FadeTransition(Duration.millis(300), c);
    ft.setFromValue(0.3);
    ft.setToValue(1);
    ft.setCycleCount(1);
    ft.setAutoReverse(true);
    ft.play();
  }

  /**
   * Start listen keys to press and start saving
   */
  private void startListenButtons()
  {
    startBtn.getScene().setOnKeyPressed(this::handleNoteButtons);
  }

  /**
   * Save recorded file
   */
  private void saveFile() throws IOException
  {
    var fileChooser = new FileChooser();
    fileChooser.setTitle("Select PressChart file");
    fileChooser.getExtensionFilters().clear();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PressChart file", "*.prc"));
    var pressChartFile = fileChooser.showSaveDialog(startBtn.getScene().getWindow());
    if (pressChartFile == null)
    {
      return;
    }

    var pressChartPath = pressChartFile.getAbsolutePath();
    saver.save(pressChartPath);
  }

  /**
   * Start view
   *
   * @param path FQ path to mp3 file
   */
  public void start(String path)
  {
    this.songPath = path;
    saveBtn.setVisible(false);

    var stage = (Stage) startBtn.getScene().getWindow();
    stage.setOnCloseRequest(this::handleOnCloseRequest);

    isRecording = false;
    textPresses.setDisable(true);
  }

  /**
   * FE start/stop button
   */
  public void startBtnAction()
  {
    countLabel.setText(isRecording ? "Editor" : "Recording");
    startBtn.setText(isRecording ? "Start" : "Stop");
    saveBtn.setVisible(isRecording);

    if (!isRecording)
    {
      saver = new Saver();

      var sound = new Media(this.songPath);
      mediaPlayer = new MediaPlayer(sound);
      mediaPlayer.play();
      textPresses.setText("");
    }
    else
    {
      mediaPlayer.stop();
      mediaPlayer = null;
    }
    isRecording = !isRecording;

    startListenButtons();
  }

  /**
   * FE button to save
   */
  public void saveBtn() throws IOException
  {
    saveFile();
  }

  private void handleNoteButtons(KeyEvent ke)
  {
    switch (ke.getCode())
    {
      case A:
        actualPress = new Press(Chord.RED, mediaPlayer.getCurrentTime().toMillis());
        setFadeIn(circle0);
        break;
      case S:
        actualPress = new Press(Chord.YELLOW, mediaPlayer.getCurrentTime().toMillis());
        setFadeIn(circle1);
        break;
      case D:
        actualPress = new Press(Chord.GREEN, mediaPlayer.getCurrentTime().toMillis());
        setFadeIn(circle2);
        break;
      case K:
        actualPress = new Press(Chord.BLUE, mediaPlayer.getCurrentTime().toMillis());
        setFadeIn(circle3);
        break;
      case L:
        actualPress = new Press(Chord.MAGENTA, mediaPlayer.getCurrentTime().toMillis());
        setFadeIn(circle4);
        break;
    }

    setNewPressToTextarea(actualPress);
    saver.addPress(actualPress);
  }

  private void handleOnCloseRequest(WindowEvent we)
  {
    try
    {
      mediaPlayer.stop();
    }
    catch (Exception e)
    {
      //Sometimes expected ;)
    }
    mediaPlayer = null;
  }
}
