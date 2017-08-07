package waters_2017;

import java.io.IOException;


public class ExplicitCommunication {

private int Task_1 = 0;
private double BCST_1 = 0;
private double WCRT_1 = 0;
private int Task_2 = 0;
private double BCST_2 = 0;
private double WCRT_2 = 0;
private int Task_3 = 0;
private double BCST_3 = 0;
private double WCRT_3 = 0;
private double WCRT_Task_3 = 0;
	
	//Task1=period
	//BCST1 runnable del primero de la cadena = 0
	//WCRT1 runnable del primero de la cadena
	public ExplicitCommunication (int Task1 , double BCST1, double WCRT1, int Task2 , double BCST2, double WCRT2, int Task3 , double BCST3, double WCRT3, double WCRTTask3) {
		Task_1 	= Task1;
		BCST_1	= BCST1;	
		WCRT_1 	= WCRT1;
		Task_2 	= Task2;
		BCST_2	= BCST2;	
		WCRT_2 	= WCRT2;
		Task_3 	= Task3;
		BCST_3	= BCST3;	
		WCRT_3 	= WCRT3;
		WCRT_Task_3 = WCRTTask3;
	}
	
	double PhiCalculation (int Task, double BCST, double WCRT){
		double phi=0;
		phi = Task - BCST + WCRT;
		//System.out.println(phi);
		return phi;
	}
	
	
	public double DeltaCalculation (int Task_W,  double BCST_W, double WCRT_W, int Task_R,  double BCST_R, double WCRT_R) throws IOException {
		double phi_W = PhiCalculation(Task_W, BCST_W, WCRT_W); 
		double phi_R = PhiCalculation(Task_R, BCST_R, WCRT_R);
		double delta = Math.min(phi_R, phi_W);
		//System.out.println(delta);
		return delta;
	}
	
	public double AlphaCalculation (int Task_W,  double BCST_W, double WCRT_W, int Task_R,  double BCST_R, double WCRT_R) throws IOException {
		double phi_W = PhiCalculation(Task_W, BCST_W, WCRT_W); 
		System.out.println(phi_W);
		double phi_R = PhiCalculation(Task_R, BCST_R, WCRT_R);
		System.out.println(phi_R);
		double q = phi_W - phi_R;
		System.out.println(q);
		double delta = Math.min(phi_R, phi_W);
		double max = Math.max(q, 0);
		double alpha = delta + Math.floor(max/((double)Task_R))*Task_R;
		//System.out.println(alpha);
		return alpha;
	}
	
	public double RhoCalculation (int Task_W,  double BCST_W, double WCRT_W, int Task_R,  double BCST_R, double WCRT_R) throws IOException {
		double rho = 0;
		double phi_W = PhiCalculation(Task_W, BCST_W, WCRT_W); 
		//System.out.println(phi_W);
		double phi_R = PhiCalculation(Task_R, BCST_R, WCRT_R);
		//System.out.println(phi_R);
		double q = phi_W - phi_R;
		if (q>0){
			rho = Math.max(Task_W + phi_R, phi_R + Task_R*(1 + Math.floor( q/((double)Task_R) ) ) );
		}
		else {
			rho = Math.max(Task_R + phi_W, phi_W + Task_W*(1 + Math.floor( (-q)/((double)Task_W) ) ) );
		}
		//System.out.println(q);
		//double delta = Math.min(phi_R, phi_W);
		//double max = Math.max(q, 0);
		//double alpha = delta + Math.floor(max/Task_R)*Task_R;
		//System.out.println(rho);
		return rho;
	}
	
	public void L2FCalculation () throws IOException {
		double L2F = BCST_1 + DeltaCalculation (Task_1,  BCST_1, WCRT_1, Task_2,  BCST_2, WCRT_2) + DeltaCalculation (Task_2,  BCST_2, WCRT_2, Task_3,  BCST_3, WCRT_3) + WCRT_Task_3 - WCRT_3;
		System.out.println(L2F);
	}
	
	public void L2LCalculation () throws IOException {
		double L2L = BCST_1 + AlphaCalculation (Task_1,  BCST_1, WCRT_1, Task_2,  BCST_2, WCRT_2) + AlphaCalculation (Task_2,  BCST_2, WCRT_2, Task_3,  BCST_3, WCRT_3) + WCRT_Task_3 - WCRT_3;
		System.out.println(L2L);
	}
	
	public void F2FCalculation () throws IOException {
		double F2F = BCST_1 + RhoCalculation (Task_1,  BCST_1, WCRT_1, Task_2,  BCST_2, WCRT_2) + RhoCalculation (Task_2,  BCST_2, WCRT_2, Task_3,  BCST_3, WCRT_3) + WCRT_Task_3 - WCRT_3;
		System.out.println(F2F);
	}
	
}