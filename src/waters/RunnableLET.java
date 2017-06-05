package waters;

public class RunnableLET {
	String name;
	int nInstructions;
	double executionTime;
	double responseTime; //reaction

	
	public RunnableLET(String name,int nInstructions){
		this.name=name;
		this.nInstructions=nInstructions;
		this.executionTime=(this.nInstructions/200000000.0)*(1000000.0); // (n/f)*usec;
		this.responseTime=0;
	}
	
	public String toString(){
		return "Runnable name: "+name+", nInstructions: "+nInstructions +", executionTime: "+executionTime;
	}
	
}
