/**
 * Copyright (C) 2012-2014  Magdalen Berns <m.berns@sms.ed.ac.uk>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */
class Distribution {

    private static final double period = 2 * Math.PI;
    private static double binWidth;
    private static double[] n;
    private static double rho;

    /**
    * nr
    * @param binWidth width of the bins as a double
    * @param cutOff   cutOff distance as a double
    * @param boxDims  dimensions of the box as a Point instance
    * @param p        array of Point instances
    * @return         Histogram the mean number of particles in 'shell' at given width and distance
    */
    public static double[] nr(double binWidth, double cutOff, Point boxDims, Particle[] p) {
        double[] n = new double[(int)(cutOff/binWidth+0.5)];
        for (int i=0; i < p.length ; i++) {
            for (int j = 0; j < p.length; j++) {
                if (i != j) {
                    Point separation = MinimumImage.minimumDistance(boxDims, p, i, j);
                    double r = separation.magnitude();
                    if (r <= cutOff) {
                        int index = (int) (r / binWidth);
                        n[index] += 2; // add once for each Point instance.
                    }
                }
            }
        }
        return n;
    }

    /**
     * g
     * @param binWidth width of the bins
     * @param n the g[r] histogram
     * @param rho density
     * @return Radial distribution function, g[r] as an array of ints.
     */
    public static double[] g(Point box, double binWidth, double[] n, double rho) {
        Distribution.binWidth = binWidth;
        Distribution.n = n;
        Distribution.rho = rho;
        for (int i=0; i < n.length; i++)
            n[i] /= (4.0 * Math.PI * (Math.pow((i + 1) + 0.5, 3) - Math.pow(i, 3)) * Math.pow
                    (binWidth, 3) * rho) / 3.0;
        return n;
    }

    /**
     * gaussian
     *                         Transform data into a normalised gaussian
     * @param mean
     *                         mean of data
     * @return
     *                         normalised gaussian in the form of 1D array of doubles
     */
    public static double[] gaussian(double[] data, double mean) {
        int N = data.length;
        double[] gaussian = new double[N];
        double tempGaussian= 0.0;

        for (int i=0; i < N; i++) {
            gaussian[i] = Math.sqrt(1 / (period * s2(data,mean))) *
                          Math.exp(- 1 *(Math.pow((i - mean), 2) / (2 * s2(data,mean))));
            tempGaussian += gaussian[i];
        }

        for (int i = 0; i < N; i++) {
            gaussian[i] /= tempGaussian;
        }
        return gaussian;
    }


    /**
     * convolve
     *                          convolve data with a normalised gaussian in order
     *                          to smooth the output after reducing sample rate
     * @param data
     *                          1D integer data array to be smoothed
     * @param gaussian
     *                          normalised gaussian in the form of 1D array of doubles for y axis
     * @param N
     *                          sample number appropriate to size of original data array
     * @return
     *                          Smoothed data as array of doubles
     */
    public static double[] convolve(double[] data, double[] gaussian, int N){
        double convolved[] = new double[data.length - (N + 1)];
        for (int i=0; i < convolved.length; i++){
            convolved[i] = 0.0;
            for (int j = i, k = 0; j < i + N; j++, k++){
                convolved[i] +=  data[j] * gaussian[k];
            }
        }
        return convolved;
    }
    /**
     * s2               Works out the biased difference of least squares fit
     * @param data
     *                  A 1D array of data
     * @param mean      The mean of the data
     *
     * @return          The unbiased variance squared double
     */
    public static double s2(double[] data, double mean) {
        double s2 = 0;
        for (double aData : data) {
            s2 += Math.pow((aData - mean), 2);
        }
        return s2 / (data.length - 1);
    }

    /**
     * mean
     *                   Works out the mean value of supplied data
     * @param data
     *                   Array of doubles holding the x and y values in [i][0] and [j][1] respectively
     *
     * @return
     *                  The covariance as a double giving the fit difference of least squares
     */
    public static double mean(double[] data){
        double sum = 0.0;
        for (double aData : data) sum += aData;
        return sum / (double) data.length;
    }

    /**
     * s
     *                  Works out the difference of least squares fit
     * @param s2              The biased or unbiased variance
     * @return
     *                  Standard Error on the Mean
     */
    public static double s(double s2) {
        return Math.sqrt(s2);
    }

    /**
     * standardDeviation
     *
     *                  Works out the Standard Deviation
     * @param data
     *                  The data being analysed
     * @param mean
     *                  The mean of the data
     *
     * @return
     *                  The the biased Standard Deviation as a double
     */
    public static double standardDeviation(double[] data, double mean){
        double variance = 0.0;
        for (double aData : data) variance += Math.pow((aData - mean), 2);
        return Math.sqrt(variance) / ((double)data.length);
    }
}


