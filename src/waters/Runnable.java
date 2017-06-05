package waters;

import java.util.LinkedList;

public class Runnable {
	String name;
	int nWInstructions;
	int nBInstructions;
	int nAInstructions;

	double WCET;
	double BCET;
	double ACET;
	
	double BCST;
	double WCST;

	double WCRT; //reactions
	double readLatency;
	double writeLatency;
	LinkedList<Label> labels = new LinkedList<Label>();
	double frequency=250000000.0;
	public void addLabel(Label label){
		labels.add(label);
	}
	
	public Runnable(String name, int nWInstructions,int nBInstructions,int nAInstructions){
		this.name=name;
		this.nWInstructions=nWInstructions;
		this.nBInstructions=nBInstructions;
		this.nAInstructions=nAInstructions;

		this.WCET=(this.nWInstructions/frequency)*(1000000.0); // (n/f)*usec;
		this.BCET=(this.nBInstructions/frequency)*(1000000.0); // (n/f)*usec;
		this.ACET=(this.nAInstructions/frequency)*(1000000.0); // (n/f)*usec;

		this.WCRT=0;
	}
	
	public Runnable(String name, double cost){
		this.name=name;
		this.WCET=(cost/frequency)*(1000000.0);
		this.BCET=(cost/frequency)*(1000000.0);

	}

}
