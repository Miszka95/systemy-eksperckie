package pl.edu.wat;

public class App {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        Algorithm algorithm = new Algorithm();
        algorithm.run();
        System.out.println("Running time: " + (System.currentTimeMillis() - time)/1000d);
    }
}
