package cz.jcu.prf.uai.javamugs.clonehero.logic;

import junit.framework.*;
import java.io.IOException;

public class ParserTest extends TestCase
{
  private static final String TEST_CHARTS_PATH = "./tracks/testCharts/";
  private static final double TIME_OFFSET = 3000;

  public void testParseFile_VALID_FILE_PATH() throws Exception
  {
    final String VALID_FILE_PATH = TEST_CHARTS_PATH + "ValidChart.prc";
    var parser = new Parser();
    var chart = parser.parseFile(VALID_FILE_PATH, TIME_OFFSET);
    assertNotNull(chart);
  }

  public void testParseFile_MISSING_EXTENSION_PATH()
  {
    final String MISSING_EXTENSION_PATH = TEST_CHARTS_PATH + "MissingExtension";
    try
    {
      var parser = new Parser();
      parser.parseFile(MISSING_EXTENSION_PATH, TIME_OFFSET);
      fail();
    }
    catch (IOException e)
    {
      if (!e.getMessage().equals("Unexpected extension"))
      {
        fail();
      }
    }
  }

  public void testParseFile_TOO_LARGE_FILE_PATH()
  {
    final String TOO_LARGE_FILE_PATH = TEST_CHARTS_PATH + "TooLarge.prc";
    try
    {
      var parser = new Parser();
      parser.parseFile(TOO_LARGE_FILE_PATH, TIME_OFFSET);
      fail();
    }
    catch (IOException e)
    {
      if (!e.getMessage().equals("File is too large"))
      {
        fail();
      }
    }
  }

  public void testParseFile_DROPPING_EARLY_PRESSES_PATH() throws Exception
  {
    final String DROPPING_EARLY_PRESSES_PATH = TEST_CHARTS_PATH + "DroppingEarlyPresses.prc";
    var parser = new Parser();
    var chart = parser.parseFile(DROPPING_EARLY_PRESSES_PATH, TIME_OFFSET);
    assertTrue(chart.next(50000).isEmpty());
  }

  public void testParseFile_UNEXPECTED_FORMAT_PATH()
  {
    final String UNEXPECTED_FORMAT_PATH = TEST_CHARTS_PATH + "WrongFormat.prc";
    try
    {
      var parser = new Parser();
      parser.parseFile(UNEXPECTED_FORMAT_PATH, TIME_OFFSET);
      fail();
    }
    catch (Exception e)
    {
      if (!(e instanceof NumberFormatException))
      {
        fail();
      }
    }
  }
}
