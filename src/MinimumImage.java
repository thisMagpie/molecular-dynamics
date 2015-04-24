
class MinimumImage {

  /**
   * minimumDistance
   *
   * @param dims the dimensions of the bounding box
   * @param p    an array of Points
   * @param i    index of the Point array
   * @param j    index of the Point array
   * @return a Point instance storing the separation distance between two particles with
   * minimum image correction.
   */
  public static Point minimumDistance(Point dims, Particle[] p, int i, int j) {
    double xSep = Point.subtract(p[j].getPosition(), p[i].getPosition()).getX();
    double ySep = Point.subtract(p[j].getPosition(), p[i].getPosition()).getY();
    double zSep = Point.subtract(p[j].getPosition(), p[i].getPosition()).getZ();

    xSep = (xSep - dims.getX() * (int) Math.round(xSep / dims.getX()));
    ySep = (ySep - dims.getY() * (int) Math.round(ySep / dims.getY()));
    zSep = (zSep - dims.getZ() * (int) Math.round(zSep / dims.getZ()));

    return new Point(xSep, ySep, zSep);
  }
}