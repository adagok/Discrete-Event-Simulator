package simulatorK;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;


public class simulatorK {
	public double avgA;
	public double avgS;
	public int K;
	
	public simulatorK(double lambda, double serviceT, int K) {
		this.avgA=lambda;
		this.avgS=serviceT;
		this.K=K;
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
	  public void simulate(double time) { 

		  double curr=0;

		  double death=0;
		  double newS=0;
		  double newT=0;
		  double currTq=0.0;
		  double sumTq=0.0;
		  boolean arrNeedRej = false;
		  boolean isbusy = false;
		  double numAcc = 0;
		  int numRej=0;
		  int rejected=-1;
		  int numInQueue = 0;
		  double sumQ = 0;
		  double loop_num=0;
		  double currTs=0;
		  double currTw=0;
		  double currQ=0;

		  PriorityQueue<Tuple> PQ = new PriorityQueue<Tuple>(new TupleComparator());
		  PriorityQueue<Tuple> Arr = new PriorityQueue<Tuple>(new TupleComparator());
		  newT=Exp(avgA);
		  sumQ=sumQ+1;

	  	  PQ.add(new Tuple(0, 0 ,newT+curr));
	  	  curr = newT+curr;
	  	  Tuple arrNeedDep =PQ.peek();
		    while(curr<time){
		    	
		    	loop_num++;
		    	Tuple schedule =PQ.poll();
		    	curr=schedule.time;

		    	sumQ=sumQ+numInQueue;
		    	if(schedule.name == 0) { //birth
		    		//if the queue is full then request is rejected
		    		if(numInQueue<K) {

		    			numAcc++;
		    			arrNeedRej=false;
		    			numInQueue++;
		    			System.out.println("R"+schedule.num+" ARR: "+ curr+"\n");
		    			if(numInQueue == 1) {
			    			isbusy=true;
			    			System.out.println("R"+schedule.num+" START: "+ curr+"\n");
				    		newS=Exp(1/avgS);
				    		sumTs=newS+sumTs;
				    		sumTq=sumTq+newS;
					    	PQ.add(new Tuple(1, schedule.num ,newS+curr));

					    	
				    	}
			    		else {
			    			isbusy=false;
			    			
			    			Arr.add(schedule);
			    		}

		    		}
		    		else {
		    			numRej++;
		    			System.out.println("R"+schedule.num+" DROP: "+ curr+"\n");
		    			rejected=schedule.num;
		    			arrNeedRej=true;
		    		
		    		}
		    		newT=Exp(avgA);
		    		
			    	PQ.add(new Tuple(0, schedule.num +1,newT+curr));
			    	
			    	
		    	}
		    	else if(schedule.name == 1) { //death

		    		currTw = currTq - currTs;
		    		currQ=numInQueue;

		    		System.out.println("R"+schedule.num+" DONE: "+ curr+"\n");
		    		if(numInQueue>0) {
		    			numInQueue--;
		    			isbusy=true;
		    			if(numInQueue>0) {
		    			
		    				if (Arr.size()>0) {
		    					arrNeedDep =Arr.poll();
		    					currTq=schedule.time-arrNeedDep.time;
		    				}
			    			double serviceT =Math.max(curr, arrNeedDep.time);
			    		
			    			newS=Exp(1/avgS);
			    			sumTs=sumTs+newS;
			    			sumTq=sumTq+newS+currTq;
			    			curr=serviceT;
			    			
			    			
			    			PQ.add(new Tuple(2, (schedule.num+1), curr));
			    			
				  	    	PQ.add(new Tuple(1, (schedule.num+1),serviceT+newS));
			    			
			    			
		    			}
		    			else {
		    				isbusy=false;
		    			}
		    		}
		    	}
		    	else { //service time
		
		    		System.out.println("R"+schedule.num+" START: "+ curr+"\n");

		    	}

		    }
		    double util = sumTs/curr;
		    System.out.println("UTIL: "+ util+"\n");
		    double avgL= sumQ/loop_num;
		    System.out.println("QLEN: "+ avgL+"\n");

		    System.out.printf("TRESP: %f\n", (sumTq/numAcc));
		    System.out.println("DROPPED: "+ numRej+"\n");
	  }
	  private double abs(double d) {
		if (d <0){
			d=d*(-1);
		}
		return d;
		
	}
	public static double cleanDouble(double number)
		{
			double cleanNumber = number * 10000.0;
			cleanNumber = ((int)cleanNumber) / 10000.0;
			return cleanNumber;
		}
	  
	  public static double Exp(double lambda) { 
	    Random RandomGenerator = new Random(); 
	    double y = RandomGenerator.nextDouble();
	    double x = (- Math.log(1.0-y))/lambda; 
	    return x; 
	  }
	  public static void main (String[] args) { 

		double time =Double.parseDouble(args[0]);
		double avgA=Double.parseDouble(args[1]);
		double avgS=Double.parseDouble(args[2]);
		int K=Integer.parseInt(args[3]);
		simulatorK sim =new simulatorK(avgA, avgS, K);
		sim.simulate(time);

	  }
}
