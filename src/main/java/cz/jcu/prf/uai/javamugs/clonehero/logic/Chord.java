package cz.jcu.prf.uai.javamugs.clonehero.logic;

public class Chord
{
  public static final int RED = 0;
  public static final int YELLOW = 1;
  public static final int GREEN = 2;
  public static final int BLUE = 3;
  public static final int MAGENTA = 4;

  private final boolean[] arr = new boolean[5];

  /**
   * Constructor, set up all variables
   *
   * @param red     color
   * @param yellow  color
   * @param green   color
   * @param blue    color
   * @param magenta color
   */
  public Chord(boolean red, boolean yellow, boolean green, boolean blue, boolean magenta)
  {
    arr[RED] = red;
    arr[YELLOW] = yellow;
    arr[GREEN] = green;
    arr[BLUE] = blue;
    arr[MAGENTA] = magenta;
  }

  /**
   * Get array of chords
   *
   * @return boolean array size of 5
   */
  public boolean[] getChords()
  {
    return arr;
  }

  /**
   * Check if is everything false
   */
  public boolean isEmpty()
  {
    for (boolean anArr : arr)
    {
      if (anArr)
      {
        return false;
      }
    }

    return true;
  }
}
