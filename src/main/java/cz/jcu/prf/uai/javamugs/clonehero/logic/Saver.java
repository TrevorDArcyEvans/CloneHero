package cz.jcu.prf.uai.javamugs.clonehero.logic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Saver
{
  private final List<Press> toSave = new ArrayList<>();

  /**
   * Adds a press to be processed.
   *
   * @param pressToAdd MUST CONTAIN PRESS TIME, NOT DRAW TIME.
   */
  public void addPress(Press pressToAdd)
  {
    toSave.add(pressToAdd);
  }

  /**
   * Saves contained presses into .prc file.
   * If provided path lacks .prc extension, it is added.
   *
   * @param path Where file should be saved.
   * @throws IOException problem processing the file or no preses added to save.
   */
  public void save(String path) throws IOException
  {
    if (toSave.isEmpty())
    {
      throw new IOException("Nothing to save.");
    }

    if (!path.endsWith(".prc"))
    {
      path += ".prc";
    }

    PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path)));

    Press toWrite;
    long prevTime = 0, time;
    while (!toSave.isEmpty())
    {
      toWrite = toSave.remove(0);

      time = (long) toWrite.getDrawTime();
      if ((time - prevTime) < 50)
      {
        time = prevTime;
      }

      writer.println(time + ":" + toWrite.getColor());   //Draw time is actually
      // press time int this case.
      prevTime = time;
    }
    writer.close();
  }
}
