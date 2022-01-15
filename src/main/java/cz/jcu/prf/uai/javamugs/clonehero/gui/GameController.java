package cz.jcu.prf.uai.javamugs.clonehero.gui;

import cz.jcu.prf.uai.javamugs.clonehero.logic.Chord;
import cz.jcu.prf.uai.javamugs.clonehero.logic.Game;
import cz.jcu.prf.uai.javamugs.clonehero.logic.GameReport;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class GameController
{
  public Canvas canvas;
  public BorderPane rootContainer;
  public Label scoreLabel;
  public Label multiplierLabel;

  private Stage stage;
  private Game game;
  private MediaPlayer mediaPlayer;
  private Chord pressedButtons;
  private URI songURI;
  private ArrayList<BallAnimation> ballAnimations;
  private AnimationTimer mainCycle;
  private final int[] highlightedStrings = new int[5];
  private final int[] lights = new int[5];
  private final Random random = new Random();
  private Image background;

  private boolean screencap;
  private int frameCount = 1;

  public void setGame(Game game)
  {
    this.game = game;
  }

  public void setSongURI(URI songURI)
  {
    this.songURI = songURI;
  }

  /**
   * Method to be called on start, after initialization
   */
  public void start(boolean screencap)
  {
    this.screencap = screencap;
    this.stage = (Stage) rootContainer.getScene().getWindow();
    background = new Image(Objects.requireNonNull(getClass().getResource("/bg.jpg")).toString());
    stage.setOnCloseRequest(this::handleOnCloseRequest);

    //Key presses
    rootContainer.getScene().setOnKeyPressed(this::handleOnKeyPressed);

    var sound = new Media(songURI.toString());
    mediaPlayer = new MediaPlayer(sound);
    mediaPlayer.play();

    //final long startNanoTime = System.nanoTime();
    pressedButtons = new Chord(false, false, false, false, false);

    ballAnimations = new ArrayList<>();
    this.mainCycle = getAnimationTimer();
    this.mainCycle.start();
  }

  private AnimationTimer getAnimationTimer()
  {
    return new AnimationTimer() // 60 FPS
    {
      @Override
      public void handle(long currentNanoTime)
      {
        var report = game.tick(mediaPlayer.getCurrentTime().toMillis(), pressedButtons);
        for (var i = 0; i < highlightedStrings.length; i++)
        {
          if (report.getHitChord().getChords()[i])
          {
            highlightedStrings[i] = 20;
          }
          if (report.getMissChord().getChords()[i])
          {
            highlightedStrings[i] = -20;
          }
          if (lights[i] > 0 && pressedButtons.getChords()[i])
          {
            lights[i] = 0;
          }
          else if (report.getHitChord().getChords()[i] || report.getMissChord().getChords()[i])
          {
            lights[i] = 120;
          }
        }

        if (!report.getChordToDraw().isEmpty())
        {
          boolean[] chordArr = report.getChordToDraw().getChords();
          for (var i = 0; i < chordArr.length; i++)
          {
            if (chordArr[i])
            {
              ballAnimations.add(new BallAnimation(i, game.getTimeOffset(), mediaPlayer.getCurrentTime().toMillis()));
            }
          }
        }

        try
        {
          renderCanvas(report);
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        pressedButtons = new Chord(false, false, false, false, false);
        if (mediaPlayer.getCurrentTime().toMillis() >= mediaPlayer.getTotalDuration().toMillis())
        {
          stop();
          end(report);
        }
      }
    };
  }

  /**
   * Method to be called on song end
   *
   * @param lastReport last GameReport
   */
  private void end(GameReport lastReport)
  {
    var alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Clone Hero");
    alert.setHeaderText("You've completed this song!");
    alert.setContentText("Your score: " + lastReport.getScore());
    alert.setOnCloseRequest(event -> stage.hide());
    alert.show();
  }

  /**
   * Renders graphics
   *
   * @param report current GameReport
   */
  private void renderCanvas(GameReport report) throws IOException
  {
    var gc = canvas.getGraphicsContext2D();
    gc.drawImage(background, 0, 0);
    for (var i = 0; i < highlightedStrings.length; i++)
    {
      //Strings
      gc.setLineWidth(5);
      double x = 250 + i * 75;
      if (highlightedStrings[i] > 0)
      {
        gc.setStroke(Color.GREEN);
        highlightedStrings[i]--;
        int randomInt = random.nextInt(6);
        randomInt -= randomInt / 2;
        x += randomInt;
      }
      else if (highlightedStrings[i] < 0)
      {
        gc.setStroke(Color.RED);
        highlightedStrings[i]++;
      }
      else
      {
        gc.setStroke(Color.LIGHTGREY);
      }

      gc.strokeLine(x, 0, x, canvas.getHeight());

      //Circles
      gc.setFill(cz.jcu.prf.uai.javamugs.clonehero.gui.CloneHeroColors.COLORARRAY[i]);
      gc.setLineWidth(3);
      x = 225 + i * 75;
      gc.fillOval(x, canvas.getHeight() - 75, 50, 50);
      gc.strokeOval(x, canvas.getHeight() - 75, 50, 50);

      gc.setStroke(Color.BLACK);
      gc.setFill(gc.getStroke());
      gc.setFont(Font.font(30));
      gc.setTextAlign(TextAlignment.CENTER);
      var text = "";
      switch (i)
      {
        case 0:
          text = "A";
          break;
        case 1:
          text = "S";
          break;
        case 2:
          text = "D";
          break;
        case 3:
          text = "K";
          break;
        case 4:
          text = "L";
          break;
      }
      gc.fillText(text, x + 25, canvas.getHeight() - 40);

      if (lights[i] > 0)
      {
        lights[i]--;
        var color = cz.jcu.prf.uai.javamugs.clonehero.gui.CloneHeroColors.COLORARRAY[i];
        gc.setFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.2));
        gc.fillPolygon(
          new double[]
            {
              i * (canvas.getWidth() / (lights.length - 1)),
              0,
              canvas.getWidth()
            },
          new double[]
            {
              -50,
              canvas.getHeight() + 1500,
              canvas.getHeight() + 1500
            },
          3);
      }
    }

    scoreLabel.setText(String.valueOf(report.getScore()));
    multiplierLabel.setText(String.format("%.1f", report.getMultiplier()) + "x");
    for (var i = 0; i < ballAnimations.size(); i++)
    {
      if (ballAnimations.get(i).isFinished())
      {
        ballAnimations.remove(i);
        i--;
      }
      else
      {
        ballAnimations.get(i).animate(mediaPlayer.getCurrentTime().toMillis());
      }
    }

    if (screencap)
    {
      var width = (int) canvas.getWidth();
      var height = (int) canvas.getHeight();
      var writableImage = new WritableImage(width, height);
      canvas.snapshot(null, writableImage);
      RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
      var file = new File("image" + String.format("%03d", frameCount) + ".png");
      ImageIO.write(renderedImage, "png", file);
      frameCount++;
    }
  }

  private void handleOnKeyPressed(KeyEvent event)
  {
    switch (event.getCode())
    {
      case A:
        pressedButtons.getChords()[0] = true;
        break;
      case S:
        pressedButtons.getChords()[1] = true;
        break;
      case D:
        pressedButtons.getChords()[2] = true;
        break;
      case K:
        pressedButtons.getChords()[3] = true;
        break;
      case L:
        pressedButtons.getChords()[4] = true;
        break;
    }
  }

  private void handleOnCloseRequest(WindowEvent we)
  {
    mediaPlayer.stop();
    mediaPlayer = null;
    mainCycle.stop();
    mainCycle = null;
  }

  private class BallAnimation
  {
    private final int color;
    private double y;
    private boolean finished;
    private final double startTime;
    private final double endTime;

    /**
     * @param color      number of string
     * @param timeOffset length of the animation
     * @param startTime  time of the animation start
     */
    public BallAnimation(int color, double timeOffset, double startTime)
    {
      this.color = color;
      this.y = -50;
      finished = false;
      this.startTime = startTime;
      this.endTime = startTime + timeOffset;
    }

    /**
     * Renders the animation
     *
     * @param currentTime current song time
     */
    public void animate(double currentTime)
    {
      var ratio = (currentTime - startTime) / (endTime - startTime);
      this.y = (canvas.getHeight() - 25) * ratio - 50;
      var gc = canvas.getGraphicsContext2D();
      gc.setFill(cz.jcu.prf.uai.javamugs.clonehero.gui.CloneHeroColors.COLORARRAY[color]);
      gc.fillOval(225 + 75 * color, y, 50, 50);
      if (currentTime >= endTime + 500)
      {
        finished = true;
      }
    }

    /**
     * @return true if animation is finished
     */
    public boolean isFinished()
    {
      return finished;
    }
  }
}

