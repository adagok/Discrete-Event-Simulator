package simulator;
import java.util.*;

import simulator.simulator.Tuple;
import simulator.simulator.TupleComparator;

public class SimulatorT {
	public double lambda;
	public double serviceT;
	
	public SimulatorT(double lambda, double serviceT) {
		this.lambda=lambda;
		this.serviceT=serviceT;
	}
	public static class Tuple { 
	    public int name; 
	    public int num;
	    public double time; 
	         
	    
	    public Tuple(int name, int num, double time) { 
	      
	        this.name = name; 
	        this.num = num; 
	        this.time = time; 
	    } 
	       
	  } 
	  private static int numInQueue = 0;
	  private static int sumQ = 0;
	  public static class TupleComparator implements Comparator<Tuple>{ 

	            public int compare(Tuple t1, Tuple t2) { 
	                if (t1.time < t2.time) 
	                    return -1; 
	                else if (t1.time > t2.time) 
	                    return 1; 
	                return 0; 
	                } 
	        } 

	  public static double sumTs=0.0;
	  public static void simulate(double time) { 
		  
		  double curr=0;
		  int avgA=6;
		  double avgS=0.15;
		  double death=0;
		  double newS=0;
		  double newT=0;
		  double currTq=0.0;
		  double sumTq=0.0;
		  PriorityQueue<Tuple> PQ = new PriorityQueue<Tuple>(new TupleComparator());
		  newT=Exp(avgA);
		  sumQ=sumQ+1;
	  	  PQ.add(new Tuple(0, 0 ,newT+curr));
	  	  curr = newT+curr;
	  	  Tuple arrNeedDep =PQ.peek();
		    while(curr<time){

		    	Tuple schedule =PQ.poll();
		    	curr=schedule.time;
		    	if(schedule.name == 0) { //birth

		    		numInQueue++;
		    		sumQ=sumQ+1;
		    		System.out.println("R"+schedule.num+" ARR:"+ curr);
		    		if(numInQueue == 1) {
		    			System.out.println("R"+schedule.num+" START:"+ curr);
			    		newS=Exp(1/avgS);
			    		sumTs=newS+sumTs;
				    	PQ.add(new Tuple(1, schedule.num ,newS+curr));

				    	
			    	}
		    		else {
		    			arrNeedDep = schedule;
		    		}
			    	newT=Exp(avgA);

			    	PQ.add(new Tuple(0, schedule.num +1,newT+curr));
			    	
		    	}
		    	else if(schedule.name == 1) { //death

		    		currTq=schedule.time-arrNeedDep.time;
		    		sumTq=currTq+sumTq;
		    		System.out.println("R"+schedule.num+" DONE:"+ curr);
		    		if(numInQueue>0) {
		    			numInQueue--;
		    			if(numInQueue>0) {

			    			double serviceT =Math.max(curr, arrNeedDep.time);
			    			curr=serviceT;

			    			newS=Exp(1/avgS);
			    			sumTs=sumTs+newS;
			    			PQ.add(new Tuple(2, (schedule.num+1), curr));
			    			
				  	    	PQ.add(new Tuple(1, (schedule.num+1),serviceT+newS));

			    			
		    			}
		    		}
		    	}
		    	else { //service time
		    		System.out.println("R"+schedule.num+" START:"+ curr);

		    	}

		    }
		    double util = sumTs/curr;
		    System.out.println("UTIL: "+ util);
		    double avgL= sumQ/curr;
		    System.out.println("QLEN: "+ avgL);
		    double Tq=sumTq/curr;
		    System.out.println("TRESP: "+ Tq);
	  }
	  
	  public static double Exp(double lambda) { 
	    Random RandomGenerator = new Random(); 
	    double y = RandomGenerator.nextDouble();
	    double x = (- Math.log(1.0-y))/lambda; 
	    return x; 
	  }
	  public static void main (String[] args) { 
		int avgA=6;
		double avgS=0.15;
		double time =10000;
	    simulate(time);

	    
	  }
}
	  