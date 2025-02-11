package cz.jcu.prf.uai.javamugs.clonehero.logic;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class PressChart
{
  private final Press[] presses;
  private int lastCalledItem;

  /**
   * Set array of Presses from Array list
   *
   * @param presses arraylist
   */
  public PressChart(ArrayList<Press> presses)
  {
    if (presses == null)
    {
      throw new InvalidParameterException();
    }

    int size = presses.size();
    this.presses = new Press[size];
    for (var i = 0; i < size; i++)
    {
      Press press = new Press(presses.get(i).getColor(), presses.get(i).getDrawTime());

      this.presses[i] = press;
    }

    this.lastCalledItem = 0;
  }

  /**
   * Get all Chords which are called between last time and actual call
   *
   * @param currentTime actual time
   * @return Chord
   */
  public Chord next(double currentTime)
  {

    int i = this.lastCalledItem;
    boolean[] arr = new boolean[5];

    if (presses.length <= this.lastCalledItem)
    {
      return new Chord(arr[Chord.RED], arr[Chord.YELLOW], arr[Chord.GREEN], arr[Chord.BLUE], arr[Chord.MAGENTA]);
    }

    while (i < presses.length && presses[i].getDrawTime() < currentTime)
    {
      arr[presses[i].getColor()] = true;

      i++;
    }

    this.lastCalledItem = i;

    return new Chord(arr[Chord.RED], arr[Chord.YELLOW], arr[Chord.GREEN], arr[Chord.BLUE], arr[Chord.MAGENTA]);
  }

  /**
   * get Array of press chart
   *
   * @return Array of press chart
   */
  public Press[] getPresses()
  {
    return presses;
  }
}
