import java.util.PriorityQueue;

public class Event implements Comparable<Event> {
    /**
     * Time that the event happens
     */
    private double time;
    
    private EventType eventType;
    
    public double getTime() {
    	return time;
    }

	/**
	 * @param time when the event starts
	 * @param eventType Monitor or Birth or Deaths
	 */
	public Event(double time, EventType eventType) {
		this.time = time;
		this.eventType = eventType;
		
	}

    /**
     * 
     * @param schedule contains all the scheduled future events
     * @param state of the simulation (request queue, logging info, etc.)
     * @param timestamp current time in the discrete simulation
     */
    public void function(PriorityQueue<Event> schedule, State state, double timestamp) {	
    	schedule.remove(this);

		switch (eventType) {
		case ROUTE_S0:
	    	/**
		     * remove the record of the request from the data structure of requests in the system
		     * Also, collect and compute some statistics.
	    	 */
		    Request req = state.queue0.remove();
		    req.setFinishServiceTime(timestamp);
	    
	    	state.requestTime0 += req.getTq();
		    state.busyTime0 += req.getTs();
		    state.numCompletedRequests0 += 1;

		    System.out.printf("R%d DONE S0: %f\n", req.getId(), timestamp);
		    switch (RedirectS0()) { //which server to go into either S1 or S2
		    case 1: //S1
		    	System.out.printf("R%d FROM S0 TO S1: %f\n", req.getId(), timestamp);
		    	req.setArrivalTime(timestamp);
		    	state.queue1.add(req);
		    	if(state.request_1 == null && state.request_2 == null) {
		    		if(State.getNextCPU() == 0) {
		    			state.request_1 = state.queue1.remove();
		    			System.out.printf("R%d START S1,1: %f\n", req.getId(), timestamp);
		    			req.setStartServiceTime(timestamp);
		    			schedule.add(new Event(timestamp + getTimeOfNextDeathS1(), EventType.ROUTE_S1_1));
		    		}
		    		else {
		    			state.request_2 = state.queue1.remove();
		    			System.out.printf("R%d START S1,2: %f\n", req.getId(), timestamp);
		    			req.setStartServiceTime(timestamp);
		    			schedule.add(new Event(timestamp + getTimeOfNextDeathS1(), EventType.ROUTE_S1_2));
		    		}
		    	}
		    	else if(state.request_1 == null && state.queue1.size() > 0){
		    		state.request_1 = state.queue1.remove();
		    		System.out.printf("R%d START S1,1: %f\n", req.getId(), timestamp);
	    			req.setStartServiceTime(timestamp);
	    			schedule.add(new Event(timestamp + getTimeOfNextDeathS1(), EventType.ROUTE_S1_1));
		    	}
		    	else if(state.request_2 == null && state.queue1.size() > 0) {
		    		state.request_2 = state.queue1.remove();
		    		System.out.printf("R%d START S1,2: %f\n", req.getId(), timestamp);
	    			req.setStartServiceTime(timestamp);
	    			schedule.add(new Event(timestamp + getTimeOfNextDeathS1(), EventType.ROUTE_S1_2));
		    	}
		    	break;
		    case 2:
		    	System.out.printf("R%d FROM S0 TO S2: %f\n", req.getId(), timestamp);
		    	req.setArrivalTime(timestamp);
		    	if(state.queue2.size() == Simulator.K2) {
		    		state.dropped_s2++;
		    		System.out.printf("R%d DROP S2: %f\n", req.getId(), timestamp);
		    	}
		    	else {
		    		state.queue2.add(req);
		    		if(state.queue2.size() == 1) {
		    			req.setStartServiceTime(time);
		    			schedule.add(new Event(timestamp + getTimeOfNextDeathS2(), EventType.ROUTE_S2));
		    			System.out.printf("R%d START S2: %f\n", req.getId(), timestamp);
		    			
		    		}
		    	}
		    	break;
		    }
		    

		    /**
		     * look for another blocked event in the queue that wants to execute and schedule it's death.
		     * at this time the waiting request enters processing time.
	    	 */
		    if (state.queue0.size() > 0){
				Request nextReq = state.queue0.peek();
				nextReq.setStartServiceTime(timestamp);

				System.out.printf("R%d START S0: %f\n", nextReq.getId(), timestamp);

				/* Schedule the next death event. */
				Event nextDeath = new Event(timestamp + getTimeOfNextDeathS0(), EventType.ROUTE_S0);
				schedule.add(nextDeath);
		    }
	    
	    	break;
	    
	    
		case BIRTH:
	    	/**
		     * add the newly born request to the data structure of requests in the system.
		     */
		    Request request = new Request(state.getNextId());
		    request.setEntryTime(timestamp);
		    request.setArrivalTime(timestamp);
		    state.queue0.add(request);
		    System.out.printf("R%d ARR: %f\n", request.getId(), request.getArrivalTime());
		    if(state.queue0.size() == 1) {
		    	request.setStartServiceTime(timestamp);
		    	schedule.add(new Event(timestamp + getTimeOfNextDeathS0(), EventType.ROUTE_S0));
		    	System.out.printf("R%d START S0: %f\n", request.getId(), timestamp);
		    }
		    /**
		     * if the queue is empty then start executing directly there is no waiting time.
	    	 */
	    
		    /**
		     * schedule the next arrival
		     */
		    Event nextArrival = new Event(timestamp + getTimeOfNextBirth(), EventType.BIRTH);
		    schedule.add(nextArrival);
		    
		    break;
		    
	    
	    
		case MONITOR:
		    /**
		     * inspect the data structures describing the simulated system and log them
		     */
		    state.numMonitorEvents += 1;
		    state.queueLen0 += state.queue0.size();
		    state.queueLen1 += state.queue1.size();	
		    if(state.request_1 != null) {
		    	state.totalQueueLen += 1;
		    	state.queueLen1 += 1;
		    }
		    if(state.request_2 != null) {
		    	state.totalQueueLen += 1;
		    	state.queueLen1 += 1;
		    }
		    state.queueLen2 += state.queue2.size();
		    state.queueLen3 += state.queue3.size();
		    state.totalQueueLen += state.queue0.size() + state.queue1.size() + state.queue2.size() + state.queue3.size();
		    
		  
	    
		    /**
		     * Schedule another monitor event following PASTA principle
		     */
		    Event nextMonitor = new Event(timestamp + getTimeOfNextMonitor(), EventType.MONITOR);
		    schedule.add(nextMonitor);
	    
		    break;
		case ROUTE_S1_1:
			req = state.request_1;
			state.numCompletedRequests1 += 1;
			req.setFinishServiceTime(timestamp);
			state.requestTime1 += req.getTq();
			state.busyTime1_1 += req.getTs();
			System.out.printf("R%d DONE S1,1: %f\n", req.getId(), timestamp);
			System.out.printf("R%d FROM S1 TO S3: %f\n", req.getId(), timestamp);
			req.setArrivalTime(timestamp);
			state.queue3.add(req);
			if(state.queue3.size() == 1) {
				req.setStartServiceTime(timestamp);
				schedule.add(new Event(timestamp + getTimeOfNextDeathS3(), EventType.ROUTE_S3));
				System.out.printf("R%d START S3: %f\n", req.getId(), timestamp);
			}
			if (state.queue1.size() > 0){
				state.request_1 = state.queue1.remove();
				state.request_1.setStartServiceTime(timestamp);
				System.out.printf("R%d START S1,1: %f\n", state.request_1.getId(), timestamp);

				/* Schedule the next death event. */
				Event nextDeath = new Event(timestamp + getTimeOfNextDeathS1(), EventType.ROUTE_S1_1);
				schedule.add(nextDeath);
		    }
			else {
				state.request_1 = null;
			}
			break;
			
		case ROUTE_S1_2:
			req = state.request_2;
			state.numCompletedRequests1 += 1;
			req.setFinishServiceTime(timestamp);
			state.requestTime1 += req.getTq();
			state.busyTime1_2 += req.getTs();
			System.out.printf("R%d DONE S1,2: %f\n", req.getId(), timestamp);
			System.out.printf("R%d FROM S1 TO S3: %f\n", req.getId(), timestamp);
			req.setArrivalTime(timestamp);
			state.queue3.add(req);
			if(state.queue3.size() == 1) {
				req.setStartServiceTime(timestamp);
				schedule.add(new Event(timestamp + getTimeOfNextDeathS3(), EventType.ROUTE_S3));
				System.out.printf("R%d START S3: %f\n", req.getId(), timestamp);
			}
			if (state.queue1.size() > 0){
				state.request_2 = state.queue1.remove();
				state.request_2.setStartServiceTime(timestamp);
				System.out.printf("R%d START S1,2: %f\n", state.request_1.getId(), timestamp);

				/* Schedule the next death event. */
				Event nextDeath = new Event(timestamp + getTimeOfNextDeathS1(), EventType.ROUTE_S1_2);
				schedule.add(nextDeath);
		    }
			else {
				state.request_2 = null;
			}
			break;
			
		case ROUTE_S2:
			req = state.queue2.remove();
			state.numCompletedRequests2 += 1;
			req.setFinishServiceTime(timestamp);
			state.requestTime2 += req.getTq();
			state.busyTime2 += req.getTs();
			System.out.printf("R%d DONE S2: %f\n", req.getId(), timestamp);
			System.out.printf("R%d FROM S2 TO S3: %f\n", req.getId(), timestamp);
			req.setArrivalTime(timestamp);
			state.queue3.add(req);
			if(state.queue3.size() == 1) {
				req.setStartServiceTime(timestamp);
				schedule.add(new Event(timestamp + getTimeOfNextDeathS3(), EventType.ROUTE_S3));
				System.out.printf("R%d START S3: %f\n", req.getId(), timestamp);
			}
			if (state.queue2.size() > 0){
				Request nextReq = state.queue2.peek();
				nextReq.setStartServiceTime(timestamp);
				System.out.printf("R%d START S2: %f\n", nextReq.getId(), timestamp);
				

				/* Schedule the next death event. */
				Event nextDeath = new Event(timestamp + getTimeOfNextDeathS2(), EventType.ROUTE_S2);
				schedule.add(nextDeath);
		    }
			break;
			
		case ROUTE_S3:
			req = state.queue3.remove();
			state.numCompletedRequests3 += 1;
			req.setFinishServiceTime(timestamp);
			state.requestTime3 += req.getTq();
			state.busyTime3 += req.getTs();
			System.out.printf("R%d DONE S3: %f\n", req.getId(), timestamp);
			switch(RedirectS3()) {
			case 1:
				System.out.printf("R%d FROM S3 TO S1: %f\n", req.getId(), timestamp);
				req.setArrivalTime(timestamp);
				state.queue1.add(req);
				if(state.request_1 == null && state.request_2 == null) {
		    		if(State.getNextCPU() == 0) {
		    			state.request_1 = state.queue1.remove();
		    			System.out.printf("R%d START S1,1: %f\n", req.getId(), timestamp);
		    			req.setStartServiceTime(timestamp);
		    			schedule.add(new Event(timestamp + getTimeOfNextDeathS1(), EventType.ROUTE_S1_1));
		    		}
		    		else {
		    			state.request_2 = state.queue1.remove();
		    			System.out.printf("R%d START S1,2: %f\n", req.getId(), timestamp);
		    			req.setStartServiceTime(timestamp);
		    			schedule.add(new Event(timestamp + getTimeOfNextDeathS1(), EventType.ROUTE_S1_2));
		    		}
		    	}
		    	else if(state.request_1 == null && state.queue1.size() > 0){
		    		state.request_1 = state.queue1.remove();
		    		System.out.printf("R%d START S1,1: %f\n", req.getId(), timestamp);
	    			req.setStartServiceTime(timestamp);
	    			schedule.add(new Event(timestamp + getTimeOfNextDeathS1(), EventType.ROUTE_S1_1));
		    	}
		    	else if(state.request_2 == null && state.queue1.size() > 0) {
		    		state.request_2 = state.queue1.remove();
		    		System.out.printf("R%d START S1,2: %f\n", req.getId(), timestamp);
	    			req.setStartServiceTime(timestamp);
	    			schedule.add(new Event(timestamp + getTimeOfNextDeathS1(), EventType.ROUTE_S1_2));
		    	}
				break;
			case 2:
				System.out.printf("R%d FROM S3 TO S2: %f\n", req.getId(), timestamp);
				req.setArrivalTime(timestamp);
				if(state.queue2.size() == Simulator.K2) {
		    		state.dropped_s2++;
		    		System.out.printf("R%d DROP S2: %f\n", req.getId(), timestamp);
		    	}
		    	else {
		    		state.queue2.add(req);
		    		if(state.queue2.size() == 1) {
		    			req.setStartServiceTime(time);
		    			schedule.add(new Event(timestamp + getTimeOfNextDeathS2(), EventType.ROUTE_S2));
		    			System.out.printf("R%d START S2: %f\n", req.getId(), timestamp);
		    			
		    		}
		    	}
				break;
			case 3:
				System.out.printf("R%d FROM S3 TO OUT: %f\n", req.getId(), timestamp);
				state.numCompletedRequests += 1;
				req.setFinishServiceTime(timestamp);
				state.totalRequestTime += req.getTqSys();
				break;
			}
			if(state.queue3.size() > 0) {
				Request nextReq = state.queue3.peek();
				System.out.printf("R%d START S3: %f\n", nextReq.getId(), timestamp);
				nextReq.setStartServiceTime(timestamp);
				Event nextDeath = new Event(timestamp + getTimeOfNextDeathS3(), EventType.ROUTE_S3);
				schedule.add(nextDeath);
			}
			break;
		}
    }

    /* Make sure the events are sorted according to their happening time. */
    public int compareTo(Event e) {
    	double diff = time - e.getTime();
    	if (diff < 0) {
	    	return -1;
		} else if (diff > 0) {
		    return 1;
		} else {
		    return 0;
		}
    }

    /**
	 * exponential distribution
	 * used by {@link #getTimeOfNextBirth()}, {@link #getTimeOfNextDeath()} and {@link #getTimeOfNextMonitor()} 
	 * @param rate
	 * @return
	 */
	public static double exp(double rate) {
		return (- Math.log(1.0 - Math.random()) / rate);
	}
	
	/**
	 * 
	 * @return time for the next birth event
	 */
	public static double getTimeOfNextBirth() {
		return exp(Simulator.lambda);
	}

	/**
	 * 
	 * @return time for the next death event
	 */
	public static double getTimeOfNextDeathS0() {
		return exp(1.0/Simulator.Ts0);
	}
	public static double getTimeOfNextDeathS1() {
		return exp(1.0/Simulator.Ts1);
	}
	public static double getTimeOfNextDeathS2() {
		return exp(1.0/Simulator.Ts2);
	}
	public static double getTimeOfNextDeathS3() {
		return Custom.getCustom(Simulator.serviceTimeS3, Simulator.s3prob);
	}
	public static int RedirectS0() {
		return Custom.getCustomInt(Simulator.routing_out_0, Simulator.routing_prob_0);
		
	}
	public static int RedirectS3() {
		return Custom.getCustomInt(Simulator.routing_out_3, Simulator.routing_prob_3);
	}

	/**
	 * 
	 * @return time for the next monitor event
	 */
	public static double getTimeOfNextMonitor() {
		return exp(Simulator.lambda);
	}
}