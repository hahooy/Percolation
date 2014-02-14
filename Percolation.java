/*----------------------------------------------------------------
 *  Author:        Webber Huang
 *  Login:         xracz.fx@gmail.com
 *  Written:       12/2/2014
 *  Last updated:  14/2/2014
 *
 *  Compilation:   javac-algs4 Percolation.java
 *  Execution:     java-algs4 Percolation
 *  
 *  Percolation application,first input for grid size N
 *  The rest loop input are two integer arguments that 
 *  use for site index
 *
 *  % java-algs4 Percolation
 *  5
 *  5x5 grid created
 *  1 2
 *  is full: true
 *  is percolate: false
 *  3 4
 *  is full: false
 *  is percolate: false
 *
 *----------------------------------------------------------------*/

public class Percolation {
    private static final int VIRTUAL_TOP = 0; //virtual top site index at top of grid
    private int sink;                     //virtual top site index at top of grid
    private int size;                     // N
    private boolean[] openSites;          // array for site state
    private WeightedQuickUnionUF grid;    // grid data, WeightedQuickUnionUF object
    private WeightedQuickUnionUF auxGrid; // auxiliary grid to help avoid BACKWASH

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0) 
            throw new IllegalArgumentException("N larger be than 0!");
        
        size = N;
        sink = N*N + 1;
        grid = new WeightedQuickUnionUF(N*N + 2);
        auxGrid = new WeightedQuickUnionUF(N*N + 2);

        // initial open sites array with full of false
        openSites = new boolean[N*N + 2];
        for (int i = 0; i < N*N+2; i++) {
            openSites[i] = false;
        }
    }
    
    // open site (row i, column j) if it is not already
    public void open(int i, int j) { 
        indicesVerify(i, j);
        if (!isOpen(i, j)) {                 
            openSites[xyTo1D(i, j)] = true;                             
            connectNeighbor(i, j);                      
        }
    }    

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        indicesVerify(i, j);
        return openSites[xyTo1D(i, j)];        
    } 
    
    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        indicesVerify(i, j);             
        return auxGrid.connected(VIRTUAL_TOP, xyTo1D(i, j));
    }
    
    // does the system percolate?
    public boolean percolates() {
        return grid.connected(VIRTUAL_TOP, sink);
    }
    
    // convert 2-dimensional (row, column) pair to a 1-dimensional index
    private int xyTo1D(int i, int j) {
        return (i - 1)*size + j;
    }    

    // validate indices
    private void indicesVerify(int i, int j) {
        if (i <= 0 || i > size) {
            throw new IndexOutOfBoundsException("Row index i out of bounds");
        }
        if (j <= 0 || j > size) {
            throw new IndexOutOfBoundsException("Column index j out of bounds");
        }
    } 
    
    // method for link neighbor
    private void connectNeighbor(int i, int j) {
        // deal with top edge case
        if (i > 1) connectUpSite(i, j);
        else {  
            // connect site on top most grid line to Virtual Top Site
            grid.union(VIRTUAL_TOP, xyTo1D(i, j));
            auxGrid.union(VIRTUAL_TOP, xyTo1D(i, j));
        }
        // deal with bottom edge case
        if (i < size) connectDownSite(i, j);
        else {
            // connect site on bottom most grid line to Virtual Bottom Site
            // To solve BACKWASH problem,connection isn't make to auxGrid
            grid.union(sink, xyTo1D(i, j));
        }
        
        // deal with left and right site connection
        if (j > 1) connectLeftSite(i, j);
        if (j < size) connectRightSite(i, j);
    }
    
    private void connectUpSite(int i, int j) {
        if (isOpen(i-1, j)) {
            grid.union(xyTo1D(i, j), xyTo1D(i-1, j));
            auxGrid.union(xyTo1D(i, j), xyTo1D(i-1, j));
        }
    }
    
    private void connectDownSite(int i, int j) {
        if (isOpen(i+1, j)) {
            grid.union(xyTo1D(i, j), xyTo1D(i+1, j));
            auxGrid.union(xyTo1D(i, j), xyTo1D(i+1, j));
        }
    }
    
    private void connectLeftSite(int i, int j) {
        if (isOpen(i, j-1)) {
            grid.union(xyTo1D(i, j), xyTo1D(i, j-1));
            auxGrid.union(xyTo1D(i, j), xyTo1D(i, j-1));
        }
    }
    
    private void connectRightSite(int i, int j) {
        if (isOpen(i, j+1)) {
            grid.union(xyTo1D(i, j), xyTo1D(i, j+1));
            auxGrid.union(xyTo1D(i, j), xyTo1D(i, j+1));
        }
    }
    // test clien
    public static void main(String[] args)
    {
        int N = StdIn.readInt();
        Percolation p = new Percolation(N);
        StdOut.println(N + "x" + N + " grid created");
        
        while (!StdIn.isEmpty()) {
            int i = StdIn.readInt();
            int j = StdIn.readInt();
            p.open(i, j);
            StdOut.println("is full: " + p.isFull(i, j)); 
            StdOut.println("is percolate: " + p.percolates()); 
        }
    }
}