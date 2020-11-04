
import java.util.PriorityQueue;

public class Controller {
    
    /**
     * initialize the schedule with a birth event and a monitor event
     * @return a schedule with two events
     */
		public static PriorityQueue<Event> initSchedule() {
		PriorityQueue<Event> schedule = new PriorityQueue<Event>();

		schedule.add(new Event(Event.getTimeOfNextBirth(), EventType.BIRTH));
		schedule.add(new Event(Event.getTimeOfNextMonitor(), EventType.MONITOR));
	
		return schedule;
    }
	    
    public static void runSimulation(double simulationTime) {
    	/**
		 * declare the data structures that hold the state of the system
		 */
		State state = new State();
		PriorityQueue<Event> schedule = initSchedule();
		double time = 0, maxTime = simulationTime;
		while(time < maxTime) {
			Event event = schedule.remove();
			time = event.getTime();
			event.function(schedule, state, time);
		}
	
		/**
		 * output the statistics over the simulated system
		 */
		System.out.printf("S0 UTIL: %f\n", state.busyTime0 / simulationTime);
		System.out.printf("S0 QLEN: %f\n", state.queueLen0 / state.numMonitorEvents);
		System.out.printf("S0 TRESP: %f\n", state.requestTime0 / state.numCompletedRequests0);
		
		System.out.printf("S1,1 UTIL: %f\n", state.busyTime1_1 / simulationTime);
		System.out.printf("S1,2 UTIL: %f\n", state.busyTime1_2 / simulationTime);
		System.out.printf("S1 QLEN: %f\n", state.queueLen1 / state.numMonitorEvents);
		System.out.printf("S1 TRESP: %f\n", state.requestTime1 / state.numCompletedRequests1);
		
		System.out.printf("S2 UTIL: %f\n", state.busyTime2 / simulationTime);
		System.out.printf("S2 QLEN: %f\n", state.queueLen2 / state.numMonitorEvents);
		System.out.printf("S2 TRESP: %f\n", state.requestTime2 / state.numCompletedRequests2);
		System.out.println("S2 DROPPED: " + state.dropped_s2);
		
		System.out.printf("S3 UTIL: %f\n", state.busyTime3 / simulationTime);
		System.out.printf("S3 QLEN: %f\n", state.queueLen3 / state.numMonitorEvents);
		System.out.printf("S3 TRESP: %f\n", state.requestTime3 / state.numCompletedRequests3);
		
		System.out.printf("QTOT: %f\n", state.totalQueueLen / state.numMonitorEvents);
		System.out.printf("TRESP: %f\n", state.totalRequestTime / state.numCompletedRequests);
    }
}