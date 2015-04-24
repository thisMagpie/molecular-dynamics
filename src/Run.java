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

import java.io.PrintWriter;

public class Run {
    static double cutOff;
    static double rho;
    static double T;
    static Dynamics dynamics;
    static double lenXYZ;
    static int N;
    static double k = 0.0;
    static int sweep = 10000;
    static double dt, t = 0.0;
    static double ke;
    static double pe;
    static double te;
    static double r;
    static double msd;
    static double tempurature;
    static double[] keArray =  new double[sweep];
    static double[] peArray =  new double[sweep];
    static double[] teArray =  new double[sweep];
    static double[] tempArray = new double[sweep];
    static double[] tArray = new double[sweep];
    static double[] msdArray = new double[sweep];

    /**
     * main
     *
     * @param args
     * @throws Exception
     *
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 6)
            throw new Exception("Wrong number of arguments: See README.md for details");
        N = Integer.parseInt(args[0]);
        Particle[] p = new Particle[N];
        T = Double.parseDouble(args[1]);
        rho = Double.parseDouble(args[2]);
        cutOff = Double.parseDouble(args[3]);
        dt = Double.parseDouble(args[4]);
        String choice = args[5];

      //  Draw draw = new Draw(N); TODO implement

        double binWidth;
        dynamics = new Dynamics(cutOff);

        for (int i = 0; i < N; i++){
            p[i] = new Particle();
            p[i].setMass(1.0);
        }

        initP(p, rho);
        initV(T, p);
        binWidth = cutOff / sweep;

        System.out.println("Histogram bin-width = " + binWidth);
        System.out.println("Cut-off point = " + cutOff);
        double[] nr;
        Point dXYZ = new Point(lenXYZ,lenXYZ,lenXYZ);
        dynamics.updateVelocities(p, (dt * 0.5), dXYZ);
        for (int i = 0; i < sweep; i++) {

            ke = 0;
            pe = 0;

            dynamics.updatePositions(p, dt, dXYZ);
       //     msd = dynamics.MSD(p, dXYZ); TODO add D

            t +=dt;

            ke = dynamics.kinetic(p);
            if(choice.equals("lj")) pe = dynamics.LJPotential(p, dXYZ);
            else if(choice.equals("wca")) pe = dynamics.WCAPotential(p, dXYZ);
            else System.out.println("Invalid choice ");

            tempurature = dynamics.T(p, k);

            ke /= N;
            pe /= N;
            r  /= N;
            te = ke + pe;

            keArray[i] = ke;
            peArray[i] = pe;
            teArray[i] = te;
            tempArray[i] = tempurature;
            tArray[i] = t;
        //    msdArray[i] = msd; TODO add D
            dynamics.updateVelocities(p, dt, dXYZ);
        }

     //   nr = Distribution.nr(binWidth, cutOff, dXYZ, p); TODO smooth properly

        String rdf = "rdf.dat";
        String kinetic = "kinetic.dat";
        String potential = "potential.dat";
        String total = "total.dat";
        String temp = "tempurature.dat";
   //     String msd = "msd.dat";// TODO add defusion const

    //    PrintWriter rdfFile = new PrintWriter(rdf);  TODO smooth properly
        PrintWriter kineticFile = new PrintWriter(kinetic);
        PrintWriter potentialFile = new PrintWriter(potential);
        PrintWriter totalFile = new PrintWriter(total);
        PrintWriter tFile = new PrintWriter(temp);
    //    PrintWriter msdFile = new PrintWriter(msd);

   //     double[] gr = Distribution.g(dXYZ, binWidth, nr, rho);  TODO smooth properly
     //   double mean = Distribution.mean(gr);
     //   double[] gaussian = Distribution.gaussian(gr, mean);
    //    double[] convolved = Distribution.convolve(gr, gaussian, 100);

     //   writeDoubles(rdfFile, binWidth, convolved);
        IOArray.writeDoubles(kineticFile, keArray);
        IOArray.writeDoubles(potentialFile, peArray);
        IOArray.writeDoubles(totalFile, teArray);
        IOArray.writeDoubles(tFile, tArray, tempArray);
      //  IOArray.writeDoubles(msdFile, tArray, msdArray);
    }

    /**
     * initP
     *
     * @param p array of Particle instances
     * @param rho the density of the Point instances
     */
    static void initP(Particle[] p, double rho) {
        int nXYZ = (int) Math.pow(N, 1.0 / 3.0) + 1;
        lenXYZ = Math.pow(N / rho, 1.0 / 3.0);
        double dXYZ = lenXYZ / nXYZ;
        double x, y, z;
        int n = 0;

        for (int ix = 0; ix < lenXYZ; ix++) {
            for (int iy = 0; iy < lenXYZ; iy++) {
                for (int iz = 0; iz < lenXYZ; iz++) {
                    if (n < N) {
                        x = (ix * dXYZ);
                        y = (iy * dXYZ);
                        z = (iz * dXYZ);
                        p[n].setPosition(new Point(x, y, z));
                        n++;
                    }
                }
            }
        }
        System.out.printf("%d points encapsulated by a %5.3f x %5.3f x %5.3f box\n",
                          N,
                          lenXYZ,
                          lenXYZ,
                          lenXYZ);
    }

    /**
     * initV
     * @param T Temperature as a double value
     * @param p Particle with the dimensions of the bounding box
     *
     */
    public static void initV(double T, Particle[] p) {
        double vXcm = 0.0, vYcm = 0.0, vZcm = 0.0, vsq = 0.0;

        for (Particle aP : p) {
            double xvt = random();
            double yvt = random();
            double zvt = random();

            aP.setVelocity(new Point(xvt, yvt, zvt));
            vXcm += xvt;
            vYcm += yvt;
            vZcm += zvt;
            vsq += xvt * xvt + yvt * yvt + zvt * zvt;
        }
        vXcm /= N;
        vYcm /= N;
        vZcm /= N;
        System.out.printf("COM velocity = %5.3f %5.3f %5.3f\n", vXcm, vYcm, vZcm);
        k = Math.sqrt(3 * ( N - 1 ) * T / vsq); // Boltzmann's constant

        System.out.printf("Boltzmann, k = %5.5f\n", T);

        for (Particle aP : p) {
            Point vT = aP.getVelocity();
            double xvt = k * (vT.getX() - vXcm); // scale
            double yvt = k * (vT.getY() - vYcm);
            double zvt = k * (vT.getZ() - vZcm);
            aP.setVelocity(new Point(xvt, yvt, zvt));
        }
    }

    /**
     * random
     *
     * @return a random double between 0 and 0.5
     */
    public static double random() {
        return Math.random() - 0.5;
    }

    /**
     * writeDoubles
     *
     * @param toFile a PrintWriter instance
     * @param x the first column to print
     * @param data an array of doubles to print
     */
    public static void writeDoubles(PrintWriter toFile, double x, double[] data){
        for (int i = 0; i < data.length; i++)
            toFile.write(((i+0.5) * x) + " " + (data[i] / data.length) + "\r\n"); //pairs.
        toFile.close();
    }
}
