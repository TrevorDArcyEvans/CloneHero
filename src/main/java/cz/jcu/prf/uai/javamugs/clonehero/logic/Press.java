package cz.jcu.prf.uai.javamugs.clonehero.logic;

public class Press
{
  private final int color;
  private final double drawTime;

  /**
   * Constructor, set color and drawTime
   *
   * @param color    color of chord
   * @param drawTime time when chord will be drawn
   */
  public Press(int color, double drawTime)
  {
    this.color = color;
    this.drawTime = drawTime;
  }

  /**
   * @return color of chord
   */
  public int getColor()
  {
    return this.color;
  }

  /**
   * @return draw time on screen
   */
  public double getDrawTime()
  {
    return this.drawTime;
  }
}
