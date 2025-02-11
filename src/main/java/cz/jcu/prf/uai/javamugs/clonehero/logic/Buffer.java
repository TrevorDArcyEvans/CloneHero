package cz.jcu.prf.uai.javamugs.clonehero.logic;

import java.util.ArrayList;

/**
 * Created by ivank on 04.07.2017.
 */
public class Buffer
{
  private final ArrayList<BufferRecord> bufferRecords;
  private int tolerance;

  /**
   * Creates Buffer with set difficulty
   *
   * @param difficulty 1-3 - tolerance of hits and misses
   */
  public Buffer(int difficulty)
  {
    bufferRecords = new ArrayList<>();
    switch (difficulty)
    {
      case 1:
        this.tolerance = 200;
        break;
      case 2:
        this.tolerance = 150;
        break;
      case 3:
        this.tolerance = 100;
        break;
    }
  }

  /**
   * Adds chord to queue and time to list.
   *
   * @param chord     Chord to add to buffer
   * @param pressTime time to when the chord should be pressed
   */
  public void addToBuffer(Chord chord, double pressTime)
  {
    if (!chord.isEmpty())
    {
      bufferRecords.add(new BufferRecord(chord, pressTime));
    }
  }

  /**
   * Checks if time exists in list and dequeue Chords from queue if so.
   *
   * @param pressedKeys Chord of pressed keys from
   * @param pressTime   Time of the press
   * @return Pair of hits and misses, never null
   */
  public BufferReport check(Chord pressedKeys, double pressTime)
  {
    double minTime = pressTime - tolerance;
    double maxTime = pressTime + tolerance;
    ArrayList<BufferRecord> expectedBufferRecords = new ArrayList<>();
    for (BufferRecord bufferRecord : bufferRecords)
    { // get expected chords
      double time = bufferRecord.getTime();
      if (time > minTime && time < maxTime)
      {
        expectedBufferRecords.add(bufferRecord);
      }
    }

    Chord hitChord = new Chord(false, false, false, false, false);
    Chord missChord = new Chord(false, false, false, false, false);
    if (expectedBufferRecords.isEmpty())
    {
      System.arraycopy(pressedKeys.getChords(), 0, missChord.getChords(), 0, missChord.getChords().length);
    }

    for (BufferRecord expectedBufferRecord : expectedBufferRecords)
    {
      expectedBufferRecord.checkUnexpectedPresses(pressedKeys, missChord);
      expectedBufferRecord.checkHits(pressedKeys, hitChord);
    }

    // check empty expected chords and chord out of time
    for (var i = 0; i < bufferRecords.size(); i++)
    {
      boolean remove = false;
      if (bufferRecords.get(i).getChord().isEmpty())
      {
        remove = true;
      }
      else if (bufferRecords.get(i).getTime() + tolerance < pressTime)
      {
        remove = true;
        bufferRecords.get(i).checkMisses(missChord);
      }
      if (remove)
      {
        bufferRecords.remove(i);
        i--;
      }
    }

    return new BufferReport(hitChord, missChord);
  }

  /**
   * @return size of Chord queue
   */
  public int getChordCount()
  {
    return bufferRecords.size();
  }

  /**
   * @return size of times list
   */
  public int getTimesCount()
  {
    return bufferRecords.size();
  }

  private static class BufferRecord
  {
    private final Chord chord;
    private final double time;

    public BufferRecord(Chord chord, double time)
    {
      this.chord = chord;
      this.time = time;
    }

    /**
     * Updates chord with hits
     *
     * @param pressedChord     chord of pressed keys
     * @param hitChordToUpdate chord to update
     */
    public void checkHits(Chord pressedChord, Chord hitChordToUpdate)
    {
      for (var i = 0; i < chord.getChords().length; i++)
      {
        if (chord.getChords()[i] && pressedChord.getChords()[i])
        {
          hitChordToUpdate.getChords()[i] = true;
          chord.getChords()[i] = false;
        }
      }
    }

    /**
     * Updates chord with unexpected presses (misses)
     *
     * @param pressedChord      chord of pressed keys
     * @param missChordToUpdate chord to update
     */
    public void checkUnexpectedPresses(Chord pressedChord, Chord missChordToUpdate)
    {
      for (var i = 0; i < chord.getChords().length; i++)
      {
          if (!chord.getChords()[i] && pressedChord.getChords()[i])
          {
              missChordToUpdate.getChords()[i] = true;
          }
      }
    }

    /**
     * Updates chord with missed presses
     *
     * @param missChordToUpdate chord to update
     */
    public void checkMisses(Chord missChordToUpdate)
    {
      for (var i = 0; i < chord.getChords().length; i++)
      {
          if (chord.getChords()[i])
          {
              missChordToUpdate.getChords()[i] = true;
          }
      }
    }

    public double getTime()
    {
      return time;
    }

    public Chord getChord()
    {
      return chord;
    }
  }
}
