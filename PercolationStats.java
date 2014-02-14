/*----------------------------------------------------------------
 *  Author:        Webber Huang
 *  Login:         xracz.fx@gmail.com
 *  Written:       12/2/2014
 *  Last updated:  14/2/2014
 *
 *  Compilation:   javac-algs4 PercolationStats.java
 *  Execution:     java-algs4 PercolationStats
 *  
 *  A application for analysis percolation by perform a
 *  series of computational experiments.It take two arguments
 *  perform the test,first for grid size N,second for T independent
 *  computational experiments, prints out the mean, standard
 *  deviation,the 95% confidence interval for the percolation
 *  threshold and elapsed time.
 *
 *  % java-algs4 PercolationStats
 *  20 100
 *  mean                    = 0.5894750000000001
 *  stddev                  = 0.04196453378699203 
 *  95% confidence interval = 0.5812499513777496, 0.5977000486222506
 *  Elapsed time            = 0.032 
 *  50 100
 *  mean                    = 0.5929559999999997
 *  stddev                  = 0.03038717938057218 
 *  95% confidence interval = 0.5870001128414075, 0.5989118871585919 
 *  Elapsed time            = 0.068
 *
 *----------------------------------------------------------------*/

public class PercolationStats {
    private double[] results;            // array for threshold sequence
    private int numExp;
    
    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) 
            throw new IllegalArgumentException("N and T must be larger than 0!");
        
        numExp = T;
        results = new double[T];
        
        for (int i = 0; i < T; i++) {
            Percolation p = new Percolation(N);
            runTests(p, N, i);
        }       
    }
    
    // perform T independent computational experiments on an N-by-N grid
    private void runTests(Percolation p, int N, int i) {
        int openSiteCounter = 0;
        while (!p.percolates()) {
            int randomI = StdRandom.uniform(1, N+1);
            int randomJ = StdRandom.uniform(1, N+1);
            
            if (!p.isOpen(randomI , randomJ)) {
                p.open(randomI , randomJ);
                openSiteCounter++;
            }            
        }
        results[i] = (double) openSiteCounter / (N * N);
    }
    
    // sample mean of percolation threshold
    public double mean() {        
        return  StdStats.mean(results);
    }
    
    // sample standard deviation of percolation threshold
    public double stddev() {        
        return  StdStats.stddev(results);
    }    

    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        return StdStats.mean(results) - 1.96*StdStats.stddev(results)
            / Math.sqrt(numExp);
    }
    
    // returns upper bound of the 95% confidence interval
    public double confidenceHi() {
        return StdStats.mean(results) + 1.96*StdStats.stddev(results) 
            / Math.sqrt(numExp);
    }    

    // test client
    public static void main(String[] args) {  
        while (!StdIn.isEmpty()) {
            int N = StdIn.readInt();
            int T = StdIn.readInt();
            Stopwatch time = new Stopwatch();
            PercolationStats stats = new PercolationStats(N, T);            
            StdOut.println("mean                    = " + stats.mean()); 
            StdOut.println("stddev                  = " + stats.stddev()); 
            StdOut.println("95% confidence interval = " 
                               + stats.confidenceLo() 
                               + ", " + stats.confidenceHi()); 
            StdOut.println("Elapsed time            = " + time.elapsedTime()); 
        }
    }
}