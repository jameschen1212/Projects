
public class Simulator {
	/* Average arrival rate of requests (requests per millisecond). */
    static double lambda;
    /* Average service time at the server (milliseconds). */
    static double Ts0;
    static double Ts1;
    static double Ts2;
    static double[] serviceTimeS3;
    static double[] s3prob;
    static int K2;//Maximum queue length
    static double[] routing_prob_0;
    static double[] routing_prob_3;
    static int[] routing_out_0 = {1, 2};
    static int[] routing_out_3 = {1, 2, 3};
    static double routing_prob_3_out;
    static double routing_prob_3_1;
    static double routing_prob_3_2;

    public static void simulate(double time) {
        Controller.runSimulation(time);
    }
    
    public static void main(String args[]) {
    	serviceTimeS3 = new double[3];
    	s3prob = new double[3];
    	routing_prob_0 = new double[2];
    	routing_prob_3 = new double[3];
        /* Simulation time (milliseconds). */
        double time = Double.parseDouble(args[0]);

        /* Average arrival rate of requests (requests per millisecond). */
        lambda = Double.parseDouble(args[1]);

        /* Average service time at the server (milliseconds). */
        Ts0 = Double.parseDouble(args[2]);
        Ts1 = Double.parseDouble(args[3]);
        Ts2 = Double.parseDouble(args[4]);
        serviceTimeS3[0] = Double.parseDouble(args[5]);
        s3prob[0] = Double.parseDouble(args[6]);
        serviceTimeS3[1] = Double.parseDouble(args[7]);
        s3prob[1] = Double.parseDouble(args[8]);
        serviceTimeS3[2] = Double.parseDouble(args[9]);
        s3prob[2] = Double.parseDouble(args[10]);
        K2 = Integer.parseInt(args[11]);
        routing_prob_0[0] = Double.parseDouble(args[12]);
        routing_prob_0[1] = Double.parseDouble(args[13]);
        routing_prob_3[0] = Double.parseDouble(args[15]);
        routing_prob_3[1] = Double.parseDouble(args[16]);
        routing_prob_3[2] = Double.parseDouble(args[14]);
        


    	simulate(time);
    }
}
