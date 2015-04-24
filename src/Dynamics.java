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

public class Dynamics {

    private double cutOff;

    public Dynamics(double cutOff) {
        this.cutOff = cutOff;
    }

    /**
     * kinetic
     *
     * @param p    array of Particles holding instance of each Particle object
     * @return     kinetic energy of array of Point instances
     */
    public double kinetic(Particle[] p) {
        double tempKinetic = 0.0;
        for (Particle aP : p) {
            double mag = aP.getVelocity().magnitude();
            tempKinetic += 0.5 * mag * mag * aP.getMass();
        }
        return tempKinetic;
    }

    /**
     * T
     *
     * @param p    array of Particles holding instance of each Particle object
     * @param k    boltzmann constant
     * @return     Temperature increment from an array of Point instances as a double value
     */
    public double T(Particle[] p, double k) {
        return kinetic(p) / (3 * k * (p.length - 1));
    }

    /**
     * @param separation a Point instance holding the values of separation distance between
     *                   two points
     * @return the computed LJ force between two Particles
     */
    public Point LJForce(Point separation) {
        double factor = 0.0;
        double r = separation.magnitude();
        if (r <= cutOff) {
            factor = (24.0 * Math.pow(r, -8)-(48 * Math.pow(r, -14)));
        }
        return separation.multiply(factor);
    }

    /**
     *
     * @param p    array of Particles holding instance of each Particle object
     * @param box instance of 3D bounding lattice as a Point object
     * @return
     */
    public double LJPotential(Particle[] p, Point box) {
        double energy = 0.0;
        for (int i = 0; i < p.length; i++) {
            for (int j = i; j < p.length; j++) {
                if (i != j) {
                    Point separation = MinimumImage.minimumDistance(box, p, i, j);
                    double r = separation.magnitude();
                    if (r <= cutOff)
                        energy += (4.0 * (Math.pow(r, -12.0) - Math.pow(r, -6.0)));
                }
            }
        }
        return energy;
    }

    /**
     *
     * @param p array of Particles holding instance of each Particle object
     * @param box instance of 3D bounding lattice as a Point object
     * @return
     */
    public double WCAPotential(Particle[] p, Point box) {
        double energy = 0.0;
        for (int i = 0; i < p.length; i++) {
            for (int j = i; j < p.length; j++) {
                if (i != j) {
                    Point separation = MinimumImage.minimumDistance(box, p, i, j);
                    double r = separation.magnitude();
                    if (r <= Math.pow(2, 1/6)) {
                        energy += 4.0 * (Math.pow(r, -12.0) - Math.pow(r, -6.0)) + 1;
                    } else if (r > Math.pow(2, 1/6)) {
                        energy += 0;
                    }
                }
            }
        }
        return energy;
    }

    /**
     *
     * @param p    array of Particles holding instance of each Particle object
     * @param dt   the time step at which the array of Point instances is taken as a double
     * @param box instance of 3D bounding lattice as a Point object
     */
    public void updateVelocities(Particle[] p, double dt, Point box) {
        for (int i = 0; i < p.length; i++) {
            Point force = new Point();
            for (int j = 0; j < p.length; j++) {
                if (i != j)
                    force.add(LJForce(MinimumImage.minimumDistance(box, p, i, j)));
            }
            p[i].setVelocity(p[i].getVelocity().add(Point.multiply(force, dt)));
        }
    }

    /**
     * updatePositions
     *
     * @param p    array of Particles holding instance of each Particle object
     * @param dt   the time step at which the array of Point instances is taken as a double
     * @param box instance of 3D bounding lattice as a Point object
     */
    public void updatePositions(Particle[] p, double dt, Point box) {
        for (Particle aP : p) {
            for (int j = 0; j < 3; j++) {
                double modulus = aP.getPosition().getDimension(j) % box.getX();
                if (modulus < 0.0)
                    aP.getPosition().setDimension(j, modulus + box.getX());
                else
                    aP.getPosition().setDimension(j, modulus);
            }
            aP.setPosition(aP.getPosition().add(aP.getVelocity().multiply(dt)));
        }
    }

    /**
     * MSD
     *
     * @param p    array of Particles holding instance of each Particle object
     * @param box instance of 3D bounding lattice as a Point object
     * @return mean square displacement as a double
     */
    public double MSD(Particle[] p, Point box) {
        double msd = 0.0;
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p.length; j++) {
                if (i != j)
                    msd += Math.pow(MinimumImage.minimumDistance(box, p, i, j).magnitude(), 2);
            }
        }
        return msd / (p.length -1);
    }
}
