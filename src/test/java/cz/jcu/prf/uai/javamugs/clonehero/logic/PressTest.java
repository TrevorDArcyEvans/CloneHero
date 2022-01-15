package cz.jcu.prf.uai.javamugs.clonehero.logic;

import junit.framework.TestCase;

/**
 * Created by Pavel on 05.07.2017.
 */
public class PressTest extends TestCase
{
  public void testGetColor()
  {
    var press = new Press(Chord.MAGENTA, 500.0);
    assertEquals(Chord.MAGENTA, press.getColor());
  }

  public void testGetDrawTime()
  {
    var press = new Press(Chord.MAGENTA, 500.0);
    assertEquals(500.0, press.getDrawTime());
  }
}
