/**
 * Copyright (C) 2015 Magdalen Berns <m.berns@sms.ed.ac.uk>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
class Point {
    double x, y, z;

    Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Point() {
    }

    /**
     * Constructor initialise the vector to (x,y,z);
     * @param p
     */
    public Point(Point p) {
        this.setPoint(p.x, p.y, p.z);
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     */
    void setPoint(double a, double b, double c) {
        this.x = a;
        this.y = b;
        this.z = c;
    }

    public double magnitude() {
        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    /**
     * subtract
     *
     * @param pA a Point instance
     * @param pB Point instance
     * @return a Point instance with the components of b subtracted from a
     */
    public static Point subtract(Point pA, Point pB) {
        return new Point((pA.getX() - pB.getX()),
                        (pA.getY() - pB.getY()),
                        (pA.getZ() - pB.getZ())
        );
    }

    /**
     * @param p a Point instance
     * @return Point instance with a components added to b components
     */
    public Point add(Point p){
        x += p.getX();
        y += p.getY();
        z += p.getZ();
        return new Point(x, y, z);
    }

    /**
     * @param scaler a double value
     * @return Point instance with a components added to b components
     */
    public Point add(double scaler){
        x += scaler;
        y += scaler;
        z += scaler;
        return new Point(x, y, z);
    }

    /**
     * @param p
     * @param scaler
     * @return method to multiply by a Point by a scaler and return an object of type Point
     */
    public static Point multiply(Point p, double scaler){
        return new Point ((p.x * scaler), (p.y * scaler), (p.z * scaler));
    }

    /**
     * @param scaler
     * @return method to multiply by a Point by a scaler and return an object of type Point
     */
    public Point multiply(double scaler){
        return new Point ((x * scaler), (y * scaler), (z * scaler));
    }

    public double getX(){
      return x;
    }

 /**
  * getY 
  * @return y coordinate
  */
  public double getY() {
      return y;
  }
 /**
  *  getter for z
  * @return
  **/
  public double getZ(){
    return z;
  }
 /**
  * 
  * @param i
  * @param dimension
  */
  public void setDimension(int i, double dimension) {
    switch (i) {
      case 0:
            x = dimension;
        break;

      case 1:
            y = dimension;
        break;

     case 2:
            z = dimension;
       break;
    }
  }

    /**
    * getDimension
    * @param i
    * @return the x, y or z coordinate
    */
    public double getDimension(int i) {
        double dimension = 0.0;

        switch (i) {
              case 0:
                  dimension = x;
              break;

              case 1:
                  dimension = y;
              break;

              case 2:
                  dimension = z;
               break;
            }
          return dimension;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    /**
     * @return a copy of the Point instance.
     */
    public Point copy(){
    return new Point(x, y, z);
    }

    public void printString(String label) {
        System.out.printf("%s %s\n", label, this.toString());
    }

    /**
     *
     * @return String with the components
     */
    public String toString(){
      return " " + x + " " + y + " " + z + " ";
    }
}