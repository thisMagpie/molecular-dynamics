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

class Particle {

    private double m;
    private Point  r;
    private Point  v;

    public Particle() {
        m = Double.NaN;
        r = new Point();
        v = new Point();
    }

    public Point getPosition() { return r; }
    public Point getVelocity(){ return v; }
    public double getMass(){ return m; }
    public void setPosition(Point r) { this.r = r; }
    public void setVelocity(Point v) { this.v = v; }
    public void setMass(double m){ this.m = m; }
}