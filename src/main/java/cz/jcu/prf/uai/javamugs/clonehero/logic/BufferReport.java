package cz.jcu.prf.uai.javamugs.clonehero.logic;

/**
 * Report of hits and misses and Chord of expected hits
 *
 * @author  ivank on 05.07.2017.
 */
public class BufferReport
{
  private int hit;
  private int miss;
  private final Chord hitChord;
  private final Chord missChord;

  /**
   * Creates pair of hits and misses
   *
   * @param hitChord  chord of hits
   * @param missChord chord of misses
   */
  public BufferReport(Chord hitChord, Chord missChord)
  {
    this.hit = 0;
    for (var i = 0; i < hitChord.getChords().length; i++)
    {
      if (hitChord.getChords()[i])
      {
        this.hit++;
      }
    }

    this.miss = 0;
    for (var i = 0; i < missChord.getChords().length; i++)
    {
      if (missChord.getChords()[i])
      {
        this.miss++;
      }
    }

    this.hitChord = hitChord;
    this.missChord = missChord;
  }

  /**
   * @return Hit count
   */
  public int getHit()
  {
    return hit;
  }

  /**
   * @return Miss count
   */
  public int getMiss()
  {
    return miss;
  }

  /**
   * @return Chord where true is miss
   */
  public Chord getHitChord()
  {
    return hitChord;
  }

  /**
   * @return Chord where true is hit
   */
  public Chord getMissChord()
  {
    return missChord;
  }
}
