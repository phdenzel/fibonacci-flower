/*
   @author: dephil

   Fibonacci sequence class

   Compilation (command line):
     > javac Fibonacci.java
     > java Fibonacci <args>

   Arguments:
     N - (Integer) length of the Fibonacci sequence

   Usage (command line):
     > java Fibonacci 30

   Note: 92 steps is the maximum 'long' numbers can go until the sign plays a role in Java

 */
import java.util.*;

public class Fibonacci {

long current;
long last;
double ratio;
List<Long> numbers;
int numbers_index;

public Fibonacci() {
        this.current = 2;
        this.last = 1;
        this.ratio = current/(double)last;
        this.numbers = new ArrayList<Long>();
        // numbers.add(0);
        numbers.add(last);
        numbers_index++;
        numbers.add(last);
        numbers_index++;
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
        //int n=10;
        if (n>3) {
                n -= 3;
        }
        else {
                n = 0;
        }
        Fibonacci f = new Fibonacci();
        f.nsteps(n);
        System.out.println(f.numbers);
        System.out.println(f.ratio);
}

} /* END FIBONACCI ********************************************************** */
