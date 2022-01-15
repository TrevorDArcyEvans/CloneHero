package cz.jcu.prf.uai.javamugs.clonehero.logic;

import junit.framework.TestCase;

/**
 * Score class test.
 *
 * @author Daniel Hryzbil
 */
public class ScoreTest extends TestCase
{
  public void test_constructor_resets()
  {
    var score = new Score();
    assertEquals(0, score.getScore());
    assertEquals(1.0, score.getMultiplier());
  }

  public void testScoreContainer()
  {
    var score = new Score();

    score.addScore(500);
    assertEquals(500, score.getScore());
    assertEquals(1.0, score.getMultiplier());

    score.addMultiplier(0.5);
    assertEquals(500, score.getScore());
    assertEquals(1.5, score.getMultiplier());

    score.addScore(500);
    assertEquals(1250, score.getScore());
    assertEquals(1.5, score.getMultiplier());

    score.addScore(500);
    assertEquals(2000, score.getScore());
    assertEquals(1.5, score.getMultiplier());

    score.resetMultiplier();
    assertEquals(2000, score.getScore());
    assertEquals(1.0, score.getMultiplier());

    score.addScore(1000);
    assertEquals(3000, score.getScore());
    assertEquals(1.0, score.getMultiplier());

    score.resetScore();
    assertEquals(0, score.getScore());
    assertEquals(1.0, score.getMultiplier());
  }
}
