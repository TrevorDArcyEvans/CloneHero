package cz.jcu.prf.uai.javamugs.clonehero.logic;

import junit.framework.TestCase;

/**
 * GameReport class test.
 *
 * @author Daniel Hryzbil
 */
public class GameReportTest extends TestCase
{
  public void testGameReportContainer()
  {
    var score = new Score();
    score.addMultiplier(1.0);
    score.addScore(200);
    var chordToDraw = new Chord(true, false, true, false, true);
    var chordHit = new Chord(false, true, false, true, false);
    var chordMiss = new Chord(false, true, true, true, false);
    var report = new GameReport(score, chordToDraw, chordHit, chordMiss);
    assertNotNull(report);
    assertEquals(chordToDraw.getChords(), report.getChordToDraw().getChords());
    assertEquals(chordHit.getChords(), report.getHitChord().getChords());
    assertEquals(chordMiss.getChords(), report.getMissChord().getChords());
    assertEquals(score.getScore(), report.getScore());
    assertEquals(score.getMultiplier(), report.getMultiplier());
  }
}
