/*
   @author: dephil

   Fibonacci flower simulation

   Compilation (command line):
     > javac FibonacciFlower.java
     > java FibonacciFlower

   Usage (command line):
     > java FibonacciFlower

   Note:
     relevant parameter: G = VR * periodicity / meristem_radius
     fine-tune the parameter to find different patterns

 */
import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class FibonacciFlower extends JPanel {

// Flower content
Seeds seeds;
double meristem_radius = 0.5;      // area's radius of seed creation
double GROWTH=1.;                  // interaction strength
double VR = 1.;                    // radial drift speed
// integration parameters
double dt = 0.05;                  // eulerian timestep
int periodicity = 3;               // periodicity of creation of new seeds in units of timesteps
int end = 3250;                    // animation stop in units of timesteps
// JPanel content
final static int XWIN = 1000;
final static int YWIN = 1000;
final static int XO = XWIN / 2;
final static int YO = YWIN / 2;
final static double SCALE = 10.;
final static int SEED_SIZE = 10;


//// Flower base
public FibonacciFlower() {
        this.seeds = new Seeds();
}

//// JPanel methods
public void paint(Graphics g) {
        synchronized (seeds.pos){
                g.setColor(Color.GRAY);
                g.fillRect(0, 0, XWIN, YWIN);
                g.setColor(Color.GREEN);
                int aged_size;
                for (int i=0; i<seeds.nSeeds; i++) {
                        aged_size = (int)(SEED_SIZE*seeds.r(i)/SCALE);
                        if (aged_size < 5)
                                aged_size = 5; // otherwise not visible
                        g.fillOval((int)(XO + SCALE*seeds.pos(i)[0]),(int)(YO + SCALE*seeds.pos(i)[1]), aged_size, aged_size);
                }
        }
}

public void run() {
        long t_count = 0; // timestep counter
        double meristem_angle = 0.;
        while (true) {
                // grow new seeds
                if (t_count % periodicity == 0) {
                        meristem_angle = seeds.potential_min(meristem_radius);
                        seeds.add(meristem_radius, meristem_angle);
                        // System.out.println(seeds.nSeeds);
                }

                // propagate existing seeds
                synchronized (seeds.pos){
                        double[] cache;
                        for (int i=0; i<seeds.nSeeds; i++) {
                                cache = seeds.radial_step(i, seeds.pos(i));
                                seeds.pos.set(i, cache);
                        }
                }
                t_count++;
                // System.out.println(t_count);

                // break when flower completed
                if (t_count > end) {
                        break;
                }

                // animation control
                try {
                        Thread.sleep(10);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                repaint();
        }
}

/* Nested Seeds class ******************************************************* */
class Seeds {

//// seed properties on 2D plane
List<double[]> pos;           // cartesian x and y positions
int nSeeds;                   // number of seeds

public Seeds() {
        this.pos = new ArrayList<double[]>();
        this.nSeeds = 0;
}

// add a seed to the flower
public void add(double r, double phi) {
        double[] cart = pol2cart(r, phi);
        pos.add(cart);
        nSeeds++;
}

////////////
//// getters
public double[] pos(int i) {
        if (i<nSeeds) {
                return pos.get(i);
        }
        else {
                return new double[] {0., 0.}; // not ideal
        }
}

// seed's distance from the center
public double r(int i) {
        double x = this.pos(i)[0];
        double y = this.pos(i)[1];
        return Math.sqrt(x*x+y*y); // r
}

// seed's angular coordinate
public double phi(int i) {
        double x = this.pos(i)[0];
        double y = this.pos(i)[1];
        return Math.atan2(y,x); // phi
}

/////////////////////////////
//// seed upates
public double[] drift(double[] coords, double[] speed) {
        int dim = coords.length;
        double[] next_pos = new double[dim];
        for (int i=0; i<dim; i++) {
                next_pos[i] = coords[i] + dt * speed[i];
        }
        return next_pos;
}

// propagation step to model growth
public double[] radial_step(int i, double[] coords) {
        double[] update = new double[coords.length]; // [position update]
        double[] speed = radial_vel(i, VR);
        update = drift(coords, speed);
        return update;
}

///////////////////////////////////////////////
//// transformations and other helper functions
public double[] pol2cart(double r, double phi) {
        double[] cart = new double[2];
        cart[0] = r*Math.cos(phi); // x
        cart[1] = r*Math.sin(phi); // y
        return cart;
}

// purely radial velocity in cartesian coordinates
public double[] radial_vel(int i, double radial_speed) {
        double[] radial_vel = new double[2];
        double angle = phi(i);
        radial_vel[0] = radial_speed*Math.cos(angle);
        radial_vel[1] = radial_speed*Math.sin(angle);
        return radial_vel;
}

// calculates the potential at position
public double potential(double[] position) {
        double scalar = 0;
        double dx;
        double dy;
        double d;
        for (int j=0; j<nSeeds; j++) {
                if (pos(j)[0]==position[0] && pos(j)[1]==position[1])
                        continue;
                dx = this.pos(j)[0]-position[0];
                dy = this.pos(j)[1]-position[1];
                d = 1./Math.sqrt(dx*dx+dy*dy); // check for zero division elsewhere
                scalar += GROWTH*d;
        }
        return scalar;
}

// calculates the optimal position on a circle due to the potential
public double potential_min(double circle_radius) {
        double dphi = 0.05;
        double phi_crit = 2*Math.PI;
        double pot = 1000000;
        double[] coords = new double[2];
        for (double angle=0; angle<2*Math.PI; angle+=dphi) {
                coords = pol2cart(circle_radius, angle);
                if (pot > potential(coords)) {
                        pot = potential(coords);
                        phi_crit = angle;
                }
        }
        return phi_crit;
}

} /* END NESTED SEEDS CLASS ************************************************* */

/* Main method ************************************************************** */

public static void main(String[] args) {
        FibonacciFlower ff = new FibonacciFlower();
        JFrame frame = new JFrame("Fibonacci Flower");
        frame.setBounds(10, 10, XWIN, YWIN);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(ff);
        frame.setVisible(true);
        ff.run();
}

} /* END FIBONACCIFLOWER **************************************************** */
