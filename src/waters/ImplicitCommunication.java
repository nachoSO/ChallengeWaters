package waters;

import java.io.IOException;

public class ImplicitCommunication {

private int Task_1 = 0;
private double BCST_1_0 = 0;
private double WCRT_1_0 = 0;
private double BCST_1_last = 0;
private double WCRT_1_last = 0;
private int Task_2 = 0;
private double BCST_2_0 = 0;
private double WCRT_2_0 = 0;
private double BCST_2_last = 0;
private double WCRT_2_last = 0;
private int Task_3 = 0;
private double BCST_3_0 = 0;
private double WCRT_3_0 = 0;
private double BCST_3_last = 0;
private double WCRT_3_last = 0;
	
	//E_wcst_R1=I_wcst_CI
	//WCRT1_last
	//taskN = periodN (ms)
	//BCST1_0 = best case start time runnable copy in = 0
	//WCRT1_0 = worst case start time runnable copy in 
	//WCRT1_last = worst case response time copy out
	public ImplicitCommunication (int Task1 , double BCST1_0, double WCRT1_0, double BCST1_last, double WCRT1_last, 
			int Task2 , double BCST2_0, double WCRT2_0, double BCST2_last, double WCRT2_last, 
			int Task3 , double BCST3_0, double WCRT3_0, double BCST3_last, double WCRT3_last) {
		Task_1 		= Task1;
		BCST_1_0	= BCST1_0;	
		WCRT_1_0 	= WCRT1_0;
		BCST_1_last	= BCST1_last;	
		WCRT_1_last = WCRT1_last;
		Task_2 		= Task2;
		BCST_2_0	= BCST2_0;	
		WCRT_2_0 	= WCRT2_0;
		BCST_2_last	= BCST2_last;	
		WCRT_2_last = WCRT2_last;
		Task_3 		= Task3;
		BCST_3_0	= BCST3_0;	
		WCRT_3_0 	= WCRT3_0;
		BCST_3_last	= BCST3_last;	
		WCRT_3_last = WCRT3_last;
	}
	
	double PhiCalculation (int Task, double BCST, double WCRT){
		double phi=0;
		phi = Task - BCST + WCRT;
		//System.out.println(phi);
		return phi;
	}
	
	double DeltaImplicit (int Task, double WCRT_last, double WCRT_0 ){
		double DeltaImpl = WCRT_last - WCRT_0;		
		return DeltaImpl;
	}
	
	
	
	public double DeltaCalculation (int Task_W, double BCST_W_0, double WCRT_W_0, double BCST_W_last, double WCRT_W_last,
			int Task_R,  double BCST_R_0, double WCRT_R_0, double BCST_R_last, double WCRT_R_last) throws IOException {
		double phi_W = PhiCalculation(Task_W, BCST_W_last, WCRT_W_last); 
		double phi_R = PhiCalculation(Task_R, BCST_R_0, WCRT_R_0);
		double delta = Math.min(phi_R, phi_W) + DeltaImplicit(Task_R, WCRT_R_last, WCRT_R_0);
		//System.out.println(delta);
		return delta;
	}
	 
	
	public double AlphaCalculation (int Task_W, double BCST_W_0, double WCRT_W_0, double BCST_W_last, double WCRT_W_last,
			int Task_R,  double BCST_R_0, double WCRT_R_0, double BCST_R_last, double WCRT_R_last) throws IOException {
		double phi_W = PhiCalculation(Task_W, BCST_W_last, WCRT_W_last); 
		//System.out.println(phi_W);
		double phi_R = PhiCalculation(Task_R, BCST_R_0, WCRT_R_0);
		//System.out.println(phi_R);
		double q = phi_W - phi_R;
		//System.out.println(q);
		double delta = Math.min(phi_R, phi_W);
		double max = Math.max(q, 0);
		double alpha = delta + Math.floor(max/((double)Task_R))*Task_R + DeltaImplicit(Task_R, WCRT_R_last, WCRT_R_0);
		System.out.println(alpha);
		return alpha;
	}
	
	public double RhoCalculation (int Task_W, double BCST_W_0, double WCRT_W_0, double BCST_W_last, double WCRT_W_last,
			int Task_R,  double BCST_R_0, double WCRT_R_0, double BCST_R_last, double WCRT_R_last) throws IOException {
		double rho = 0;
		double phi_W = PhiCalculation(Task_W, BCST_W_last, WCRT_W_last); 
		//System.out.println(phi_W);
		double phi_R = PhiCalculation(Task_R, BCST_R_0, WCRT_R_0);
		//System.out.println(phi_R);
		double q = phi_W - phi_R;
		if (q>0){
			rho = Math.max(Task_W + phi_R, phi_R + Task_R*(1 + Math.floor( q/((double)Task_R) ) ) ) + DeltaImplicit(Task_R, WCRT_R_last, WCRT_R_0);
		}
		else {
			rho = Math.max(Task_R + phi_W, phi_W + Task_W*(1 + Math.floor( (-q)/((double)Task_W) ) ) ) + DeltaImplicit(Task_R, WCRT_R_last, WCRT_R_0);
		}
		//System.out.println(q);
		//double delta = Math.min(phi_R, phi_W);
		//double max = Math.max(q, 0);
		//double alpha = delta + Math.floor(max/Task_R)*Task_R;
		System.out.println(rho);
		return rho;
	}
	
	public void L2FCalculation () throws IOException {
		double L2F = BCST_1_0 + DeltaCalculation (Task_1,  BCST_1_0, WCRT_1_0, BCST_1_last, WCRT_1_last, Task_2,  BCST_2_0, WCRT_2_0, BCST_2_last, WCRT_2_last) + DeltaCalculation (Task_2,  BCST_2_0, WCRT_2_0, BCST_2_last, WCRT_2_last, Task_3,  BCST_3_0, WCRT_3_0, BCST_3_last, WCRT_3_last);
		System.out.println(L2F);
	}
	
	public void L2LCalculation () throws IOException {
		double L2L = BCST_1_0 + AlphaCalculation (Task_1,  BCST_1_0, WCRT_1_0, BCST_1_last, WCRT_1_last, Task_2,  BCST_2_0, WCRT_2_0, BCST_2_last, WCRT_2_last) + AlphaCalculation (Task_2,  BCST_2_0, WCRT_2_0, BCST_2_last, WCRT_2_last, Task_3,  BCST_3_0, WCRT_3_0, BCST_3_last, WCRT_3_last);
		System.out.println(L2L);
	}
	
	public void F2FCalculation () throws IOException {
		double F2F = BCST_1_0 + RhoCalculation (Task_1,  BCST_1_0, WCRT_1_0, BCST_1_last, WCRT_1_last, Task_2,  BCST_2_0, WCRT_2_0, BCST_2_last, WCRT_2_last) + RhoCalculation (Task_2,  BCST_2_0, WCRT_2_0, BCST_2_last, WCRT_2_last, Task_3,  BCST_3_0, WCRT_3_0, BCST_3_last, WCRT_3_last);		
		System.out.println(F2F);
	}
	
	
}
