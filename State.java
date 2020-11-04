import java.util.LinkedList;

public class State {
    /**
     * Queuing system state
     */

    /* The request list. */
    public LinkedList<Request> queue0;
    public LinkedList<Request> queue1;
    public LinkedList<Request> queue2;
    public LinkedList<Request> queue3;
    

    /* Used to generate the ID of the next request. */
    private int nextId;
    
    /**
     * Simulation statistics
     */

    /* Used to calculate the utilization. */
    public double busyTime0;
    public double busyTime1_1;
    public double busyTime1_2;
    public double busyTime2;
    public double busyTime3;
    
    

    /* Number of completed requests during the simulation. */
    public double numCompletedRequests;
    public double numCompletedRequests0;
    public double numCompletedRequests1;
    public double numCompletedRequests2;
    public double numCompletedRequests3;

    /* Total request time. */
    public double totalRequestTime;   //responseTime
    public double requestTime0;
    public double requestTime1;
    public double requestTime2;
    public double requestTime3;

    /* Number of monitor events, each monitor event will record the queue length. */
    public double numMonitorEvents;

    /* Total queue length. */
    public double totalQueueLen;
    public double queueLen0;
    public double queueLen1;
    public double queueLen2;
    public double queueLen3;
    
    public double dropped_s2;
    public static int cpu;	
    public Request request_1;
    public Request request_2;
    
    
    
    
 
    
    
    
    public State() {
    	queue0 = new LinkedList<Request>();
    	queue1 = new LinkedList<Request>();
    	queue2 = new LinkedList<Request>();
    	queue3 = new LinkedList<Request>();
    	request_1 = null;
    	request_2 = null;
    	nextId = 0;
    	busyTime0 = 0;
    	busyTime1_1 = 0;
    	busyTime1_2 = 0;
    	busyTime2 = 0;
    	busyTime3 = 0;
    	numCompletedRequests = 0;
    	numCompletedRequests0 = 0;
    	numCompletedRequests1 = 0;
    	numCompletedRequests2 = 0;
    	numCompletedRequests3 = 0;
    	totalRequestTime = 0;
    	requestTime0 = 0;
    	requestTime1 = 0;
    	requestTime2 = 0;
    	requestTime3 = 0;
    	numMonitorEvents = 0;
    	totalQueueLen = 0;
    	queueLen0 = 0; //num_of_requests
    	queueLen1 = 0;
    	queueLen2 = 0;
    	queueLen3 = 0;
    	dropped_s2 = 0;
    }

    /**
     * Get next request ID.
     */
    public int getNextId() {
    	return nextId++;
    }
    public static int getNextCPU() {
    	cpu++;
    	return cpu%2;
    }
}