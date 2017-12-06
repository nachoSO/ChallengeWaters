package waters_2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class LETCommunication {
	//int ECHyperperiod = 1;
	//int LargestPeriod = 0;
	//int Zeta = 0;
	//ArrayList<Integer> PointList = new ArrayList<>();
	
	ArrayList<Integer> TaskList = new ArrayList<Integer>();
	//private int Task_1 = 0;
	//private int Task_2 = 0;
	//private int Task_3 = 0;
	
	
	public LETCommunication(ArrayList<Integer> Input) {
		TaskList = Input;
	}

	
	// n-th publishing point
	int Ppoint (int Task_W, int Task_R, int n) throws IOException {
		int Task_max = Math.max(Task_W, Task_R);
		int P = (int)Math.floor(((double)(n*Task_max))/((double)Task_W))*Task_W;
		return P;
	}
	
	// n-th reading point
	int Qpoint (int Task_W, int Task_R, int n) throws IOException {
		int Task_max = Math.max(Task_W, Task_R);
		int Q = (int)Math.ceil(((double)(n*Task_max))/((double)Task_R))*Task_R;
		return Q;
	}

	// n-th window 
	int nth_Window (int Task_W, int Task_R, int n) throws IOException{
		int w = 0;
		w = Qpoint(Task_W,Task_R,n) - Ppoint(Task_W,Task_R,n);
		return w;
	}
	
	// Hyperperiod of two given tasks
	int Hyperperiod (int Task_A, int Task_B) throws IOException{
		int x = 0;
		int Hyperperiod = 1;

		int max = Math.max(Task_A, Task_B);
		int min = Math.min(Task_A, Task_B);
		 
		for(int i=1;i<=min;i++)
		   {
		    x=max*i; //finding multiples of the maximum number
		    if(x%min==0) //Finding the multiple of maximum number which is divisible by the minimum number.
		     {
		      Hyperperiod=x; //making the 1st multiple of maximum number as lcm, which is divisible by the minimum number
		      break; //exiting from the loop, as we don’t need anymore checking after getting the LCM
		     }
		    }
		//System.out.println(lcm);
		return Hyperperiod;
	}
	
	// Hyperperiod of the EC
	int ECHyperperiod () throws IOException{
		int ECHyperperiod = 1;
		int i;
		for (i=0;i<TaskList.size();i++){
			ECHyperperiod = Hyperperiod(ECHyperperiod, TaskList.get(i));
		}
		//int ECHyperperiod = Hyperperiod (Task_C, Hyperperiod (Task_A, Task_B));
		return ECHyperperiod;
	}

	int LargestPeriodInEC () throws IOException{
		int LargestPeriod = 0;
		int i;
		for (i=0;i<TaskList.size();i++){
			LargestPeriod = Math.max(LargestPeriod, TaskList.get(i));
		}
		return LargestPeriod;
	}
	
	// Calculating \zeta
	int BasicPathNumber() throws IOException{
		int H = ECHyperperiod();
		int L = LargestPeriodInEC();
		int Zeta = H/L;
		return Zeta;
	}
	
	// Calculating n_{W,R}^{EC}
	int n_WR (int Task_A,int Task_B) throws IOException{
	int H = ECHyperperiod();
	int nWR = H/Math.max(Task_A, Task_B);
	return nWR;
	}
	

	
	String CommunicationType (int Task_A, int Task_B) throws IOException {
		String ComType = "";
		if (Hyperperiod(Task_A, Task_B) == Math.max(Task_A, Task_B) ){
			ComType = "Harmonic Communication";
		}
		else{
			ComType = "Non-Harmonic Communication";
		}
		return ComType;
	}
	
	
	//public ArrayList<Integer> Algorithm1 () throws IOException{

	void PQPrinter() throws IOException{
		FileWriter write = new FileWriter("C:/Users/LPC/Desktop/points.txt" , true);
		PrintWriter printBuffer = new PrintWriter(write);
		//printBuffer.println();
		
		int NumberOfTasks = TaskList.size();
		/*int h;
		for (h=1;h<=NumberOfTasks;h++){
			//String ListName = "PList" + Integer.toString(h);
			ArrayList<Integer> Integer.toString(h) = new ArrayList<>();
		}*/
		//ArrayList<Integer> PList = new ArrayList<>();
		int i,n,NWR_1,NWR_2;
		int NWR_01 =  n_WR (TaskList.get(0),TaskList.get(1))*2; 
		// Task_0 --> Task_1
		for(n=0;n <= NWR_01;n++){
			System.out.print(Ppoint(TaskList.get(0), TaskList.get(1), n) + " ");
			printBuffer.print(Ppoint(TaskList.get(0), TaskList.get(1), n) + " ");
		}
		System.out.println();
		printBuffer.println();
		for (i=1;i<NumberOfTasks-1;i++){
			// Task_i-- --> Task_i
			NWR_1 =  n_WR (TaskList.get(i-1),TaskList.get(i))*2; 
			for(n=0;n <= NWR_1;n++){
				System.out.print(Qpoint(TaskList.get(i-1), TaskList.get(i), n)+ " ");
				printBuffer.print(Qpoint(TaskList.get(i-1), TaskList.get(i), n)+ " ");
			}
			System.out.println();
			printBuffer.println();
			// Task_i --> Task_i++
			NWR_2 =  n_WR (TaskList.get(i),TaskList.get(i+1))*2; 
			for(n=0;n <= NWR_2;n++){
				System.out.print(Ppoint(TaskList.get(i), TaskList.get(i+1), n)+ " ");
				printBuffer.print(Ppoint(TaskList.get(i), TaskList.get(i+1), n)+ " ");
			}
			System.out.println();
			printBuffer.println();
		}
		
		// Task_i-- --> Task_i
		int NWR_i =  n_WR (TaskList.get(NumberOfTasks-2),TaskList.get(NumberOfTasks-1))*2; 
		// Task_0 --> Task_1
		for(n=0;n <= NWR_i;n++){
			System.out.print(Qpoint(TaskList.get(NumberOfTasks-2), TaskList.get(NumberOfTasks-1), n)+ " ");
			printBuffer.print(Qpoint(TaskList.get(NumberOfTasks-2), TaskList.get(NumberOfTasks-1), n)+ " ");
		}
		System.out.println();
		printBuffer.println();
		printBuffer.close();	
	}
	
	//Get the number of lines of the input
	int readLines(String file_input) throws IOException{
		FileReader file_to_read = new FileReader(file_input);
		BufferedReader bf = new BufferedReader(file_to_read);
		String aLine;
		int numberOfLines = 0;
		 
		 while((aLine = bf.readLine()) != null){
			 numberOfLines++;
		 }
		 bf.close();
		 return numberOfLines;
	}
	
	String QPFinder(String Q_point, String P_point_Array) throws IOException{
		String PPointArray [] = P_point_Array.trim().split("\\s+");
		String PPoint = "";
		int i, d;
		for(i=0;i<PPointArray.length;i++){
			d = Integer.parseInt(Q_point) - Integer.parseInt(PPointArray[i]);
			if (d <0){
				PPoint = PPointArray[i-1];
				break;
			}
		}
		return PPoint;
	}
	
	
	String PQFinder(String P_point, String Q_point_Array) throws IOException{
		String QPointArray [] = Q_point_Array.trim().split("\\s+");
		String QPoint = "";
		int i, d;
		for(i=0;i<QPointArray.length;i++){
			d = Integer.parseInt(P_point) - Integer.parseInt(QPointArray[i]);
			if (d <= 0){
				if(i !=0) QPoint = QPointArray[i-1];
				break;
			}
		}
		return QPoint;
	}
	
	String QP_PQFinder (String Q_point, String P_Point_Array, String Q_Point_Array) throws IOException {
		String Q_point_output = "";
		String P_temp = QPFinder(Q_point,P_Point_Array);
		if (P_temp.equals("")){
			// do nothing
		}
		else{
			Q_point_output = PQFinder(P_temp, Q_Point_Array);	
		}
		
		//if (P_temp.equals("0")) continue;
		return Q_point_output;
	}
	
	 ArrayList<Integer> PathCalculation() throws IOException{
		PQPrinter();
		ArrayList<Integer> PointList = new ArrayList<Integer>();
		FileReader fr =  new FileReader("C:/Users/LPC/Desktop/points.txt");
		BufferedReader textReader = new BufferedReader(fr);
		int numberOfArrays = readLines("C:/Users/LPC/Desktop/points.txt"); 
		int Skipcounter  = 0; // times the chain was not over
		//System.out.println(numberOfLines);
		String[] PQArray = new String[numberOfArrays];
		//each element of PQArray contains one line 
		int i,j, breakflag=0;
		for (i=0;i < numberOfArrays;i++){
			PQArray[i] = textReader.readLine();
		}
		
		String QPointArray [] = PQArray[numberOfArrays-1].trim().split("\\s+");
		// Till the last element in the HEC
		int HECBound = (QPointArray.length/2)+1;
		for (i=1;i < HECBound ;i++){
			//System.out.print(QPointArray [i] + ";");
			int Q  = Integer.parseInt(QPointArray [i]);
			String Q_temp = QPointArray [i];
			for(j=numberOfArrays;j>3;j=j-2){
				Q_temp = QP_PQFinder(Q_temp,PQArray[j-2],PQArray[j-3]);
				if (Q_temp.equals("")){
					breakflag =1;
					break;
				}
			}
			if (breakflag == 1){
				breakflag = 0;
				Skipcounter ++;
				continue;
			}
			int P = Integer.parseInt(QPFinder(Q_temp,PQArray[0]));
			PointList.add(P);
			PointList.add(Q);
			//System.out.println(P + ";" + Q);
		}
		
		//accounting for the number of unfinished paths 
		int NewBound = (QPointArray.length/2)+1 +Skipcounter;
		for (i=HECBound;i < NewBound  ;i++){
			//System.out.print(QPointArray [i] + ";");
			int Q  = Integer.parseInt(QPointArray [i]);
			String Q_temp = QPointArray [i];
			for(j=numberOfArrays;j>3;j=j-2){
				Q_temp = QP_PQFinder(Q_temp,PQArray[j-2],PQArray[j-3]);
			}
			
			int P = Integer.parseInt(QPFinder(Q_temp,PQArray[0]));
			PointList.add(P);
			PointList.add(Q);
			//System.out.println(P + ";" + Q);
		}
		
		// printing all the points
		/*for(i=0;i<PointList.size();i=i+2){
			System.out.println(PointList.get(i) + ";" + PointList.get(i+1));
		}*/
		// getting rid of paths having the same start point
		return PointList;
	}
	
	 
	 
	public ArrayList<Integer> ThetaCalculation () throws IOException{
		ArrayList<Integer> PointList = PathCalculation();
		ArrayList<Integer> ThetaList = new ArrayList<>();
		int i;
		int UniqueP = PointList.get(0);
		ThetaList.add(PointList.get(0));
		ThetaList.add(PointList.get(1));
		for(i=2;i<PointList.size();i=i+2){
			if(PointList.get(i) == UniqueP){
				// do nothing
			}
			else{
				ThetaList.add(PointList.get(i));
				ThetaList.add(PointList.get(i+1));
				UniqueP = PointList.get(i);
			}
			
		}
		// printing all the points
		System.out.println("The following points denote the start and end of our paths");
		for(i=0;i<ThetaList.size();i=i+2){
			System.out.println(ThetaList.get(i) + ";" + ThetaList.get(i+1));
		}
		return ThetaList ;
	}

	
	public int DeltaCalculation (ArrayList<Integer> ThetaListArray) throws IOException {
		int Delta = 0;
		int max_path_length = 0;
		ArrayList<Integer> ThetaList = ThetaListArray;
		int i, theta;
		for(i=0;i<ThetaList.size();i=i+2){
			theta = ThetaList.get(i+1) - ThetaList.get(i);
			max_path_length = Math.max(theta, max_path_length);
		}
		Delta = TaskList.get(0) + max_path_length +TaskList.get(TaskList.size()-1);
		//System.out.println(max_path_length + ";" + Delta);
		System.out.println("The end-2-end propagation delay is :" + Delta);
		return Delta;
	}
	
	public int AlphaCalculation (ArrayList<Integer> ThetaListArray) throws IOException {
		int Alpha = 0;
		int max_path_length = 0;
		ArrayList<Integer> ThetaList = ThetaListArray;
		int i, theta, temp;
		for(i=0;i<ThetaList.size();i=i+2){
			theta = ThetaList.get(i+1) - ThetaList.get(i);
			if (i==ThetaList.size()-2){
				temp = theta + ThetaList.get(1) + ECHyperperiod () - ThetaList.get(i+1);
				System.out.println("Just Debugging : " + ThetaList.get(1) + ";" + ECHyperperiod () + ";" + ThetaList.get(i+1));
			}
			else{
				temp = theta + ThetaList.get(i+3) - ThetaList.get(i+1);
			}
			max_path_length = Math.max(temp, max_path_length);
		}
		Alpha = TaskList.get(0) + max_path_length;
		//System.out.println(max_path_length + ";" + Alpha);
		System.out.println("The end-2-end age latency is :" + Alpha);
		return Alpha;
	}
	
	public int RhoCalculation (ArrayList<Integer> ThetaListArray) throws IOException {
		int Rho = 0;
		int max_path_length = 0;
		ArrayList<Integer> ThetaList = ThetaListArray;
		int i, theta, temp;
		for(i=0;i<ThetaList.size();i=i+2){
			theta = ThetaList.get(i+1) - ThetaList.get(i);
			if (i==ThetaList.size()-2){
				temp = theta + ThetaList.get(1) + ECHyperperiod () - ThetaList.get(i+1);
				//System.out.println("Just Debugging : " + ThetaList.get(1) + ";" + ECHyperperiod () + ";" + ThetaList.get(i+1));
			}
			else{
				temp = theta + ThetaList.get(i+3) - ThetaList.get(i+1);
			}
			max_path_length = Math.max(temp, max_path_length);
		}
		Rho = TaskList.get(0) + max_path_length + TaskList.get(TaskList.size()-1);
		//System.out.println(max_path_length + ";" + Alpha);
		System.out.println("The end-2-end reaction latency is :" + Rho);
		return Rho;
	}
	
	/*public ArrayList<Integer> AlgorithmTest () throws IOException {
		ArrayList<Integer> Buffer = new ArrayList<>();
		ArrayList<Integer> Buffer_Points = new ArrayList<>();
		int i;
		for(i=0;i<TaskList.size()-2;i++){
			//System.out.print("We are here!!!!");
			System.out.println(TaskList.get(i) + ";" +TaskList.get(i+1) +";"+ TaskList.get(i+2));
			System.out.println("n; Q_ij; Q_jk");
			Buffer = Algorithm1(TaskList.get(i),TaskList.get(i+1),TaskList.get(i+2));			// <-- This works only for 3 Tasks
			int j;
			for(j=0;j<Buffer.size();j++){
				Buffer_Points.add(Buffer.get(j));
			}
		}
		
		for(i=0;i<Buffer_Points.size();i++){
			//System.out.print("We are here!!!!");
			System.out.print(Buffer_Points.get(i) + ";");			
		}
		return Buffer_Points;
	}*/
	
	
	/*public int L2LCalculation () throws IOException {
		System.out.println(Task_1 + "ms and " + Task_2 +"ms establish " + CommunicationType(Task_1, Task_2));
		System.out.println(Task_2 + "ms and " + Task_3 +"ms establish " + CommunicationType(Task_2, Task_3));
		System.out.println("The hyperperiod of the EC : " + Task_1 + "ms -->" + Task_2 + "ms -->" + Task_3 + "ms is : " + ECHyperperiod(Task_1, Task_2, Task_3));
		System.out.println("Number of bacis paths : " + BasicPathNumber());
		// we are interested in the last three elements of the tuple
		ArrayList<Integer> List1 = Algorithm1();
		ArrayList<Integer> List2 = ThetaCalculation();
		int n = 0;
		int P_JK_0 = 0;
		int w_JK_0 = 0;
		int P_JK_1 = 0;
		int w_JK_1 = 0;
		int Q_JK_0 = 0;
		int Q_JK_1 = 0;
		int L2L = 0;
		int maxL2L = 0;
		int Theta = 0;
		System.out.println("Theta, Q_n, Q_(n+1), L2L");
		for(int k=0;k<List1.size();k=k+6){
	
			n = List1.get(k + 3);
			P_JK_0 = List1.get(k + 4);
			w_JK_0 = List1.get(k + 5);
			
			if (k == ((List1.size()/6)-1)*6  ){
			P_JK_1 = List1.get(4) + ECHyperperiod(Task_1, Task_2, Task_3);
			w_JK_1 = List1.get(5);
			}
			
			else {
				P_JK_1 = List1.get(k + 10);
				w_JK_1 = List1.get(k + 11);
			}
				
			Q_JK_0 = P_JK_0 + w_JK_0;
			Q_JK_1 = P_JK_1 + w_JK_1;
			L2L = Q_JK_1 - Q_JK_0 + List2.get(Theta) + Task_1;

			System.out.println(List2.get(Theta) + ", " + Q_JK_0 +", " +  Q_JK_1 + ", " +  L2L);
			maxL2L=Math.max(L2L, maxL2L);
			Theta ++;
		}
		
		//maxL2L = maxL2L;
		System.out.println("The last-2-last propagation delay of the EC is : " + maxL2L  );
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		return maxL2L;
		
	}*/
	
	
}

