/**
 * Copyright (c) 2015 Magdalen Berns <m.berns@ed.ac.uk>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 *
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class Draw extends JPanel implements ChangeListener {
  int size, dims,pixelDims;
  Graphics graphics;
  Image image;
  Timer timer;
  /**
  * DrawSIRS:
  *           Class constructor
  * @param size - The size of the lattice
  */
  Draw (int size) {
    this.size = size;
    Frame frame = new Frame("Molecular Dynamics Simulation");
    Panel panel = new Panel();

    pixelDims = 4;
    dims = size * pixelDims;

    frame.addWindowListener(new WindowAdapter() {
    public void windowClosing(WindowEvent e) {
    System.exit(0);
      }
    });

    frame.add(panel);
    panel.add(this);
    setPreferredSize(new Dimension(dims,dims));
    JPanel controlPanel = new JPanel();
    frame.add(controlPanel,BorderLayout.SOUTH);

    frame.pack();
    image = createImage(dims,dims);
    graphics = image.getGraphics();

    frame.setVisible(true);
  }

 /**
  * paint
  * @param g method overides paint in Canvas
  *        for painting pixels 
  */
  public void paint(Graphics g) {
    g.drawImage(image, 0, 0, dims, dims, this);
  }

 /**
  *  @overide In order to prevent pixel painting lock ups
  *  		  this gets called in response to repaint.
  */
  public void update(Graphics g) {
    paint(g);
  }

 /**
  * paintPixels:
  *              Method to paint pixel spins onto the screen
  *
  * @param dim the value of the i, jth term of the 2D array
  *        as an int
  * @param i The ith dims of the box array as an int
  * @param j The jth dims of the box array as an int
  */
  public void paintPixels(int dim, int i, int j) {
    if (dim == 0) graphics.setColor(new Color(140,0,0));
    else if (dim == 1) graphics.setColor(new Color(233,23,58));
    else graphics.setColor(new Color(140,0,0));
      graphics.fill3DRect(i * pixelDims,
                          j * pixelDims,
                          pixelDims,
                          pixelDims, true);
  }

  public void start() {
      timer.start();
  }

  public void stop() {
      //Stop the animating thread.
      timer.stop();
  }
  /**
   * stateChanged: 
   *              Slider Listener
   * @param e event as a ChangeEvent instance
   */
  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider)e.getSource();
    if (!source.getValueIsAdjusting()) {
      start();
    }
  }
}
