/*
   @author: dephil

   Lucas sequence class

   Compilation (command line):
     > javac Lucas.java
     > java Lucas <args>

   Arguments:
     N  -  (Integer) length of the Lucas sequence

   Usage (command line):
     > java Lucas 100

   Note: 92 steps is the maximum 'long' numbers can go until the sign plays a role in Java

 */
import java.util.*;

public class Lucas {

long current;
long last;
double ratio;
List<Long> numbers;
int numbers_index;

public Lucas() {
        this.current = 1;
        this.last = 2;
        this.ratio = current/(double)last;
        this.numbers = new ArrayList<Long>();
        numbers.add(last);
        numbers.add(current);
        numbers_index++;
}

public void update() {
        // update numbers
        numbers.add(current);
        // update numbers index
        numbers_index++;
        // update ratio
        ratio = current/(double)last;
}

public void step() {
        long cache = current;
        current += last;
        last = cache;
        update();
}

public void nsteps(int n) {
        while (n>0) {
                this.step();
                n--;
        }
}

/* Main method ************************************************************** */

public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        //int n = 10;
        if (n>3) {
                n -= 3;
        }
        else {
                n = 0;
        }
        Lucas l = new Lucas();
        l.nsteps(n);
        System.out.println(l.numbers);
        System.out.println(l.ratio);
}

} /* END LUCAS ************************************************************** */
