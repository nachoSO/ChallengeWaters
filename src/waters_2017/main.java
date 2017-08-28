/**
 * Purpose: Waters Challenge
 *
 * @author Unimore
 * @version 1.0 25/05/16
 */

package waters_2017;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import waters_2017.Core;
import waters_2017.helper_communication;

public class main {
   public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException,ClassNotFoundException{
	   
		FileInputStream fileC = new FileInputStream(new File("ChallengeModel_2017.amxmi"));
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder =  builderFactory.newDocumentBuilder();
		Document xmlDocument = builder.parse(fileC);
		XPath xPath =  XPathFactory.newInstance().newXPath();
	
	   //load all XML information of specific core
	   File f = new File("cores.ser");
	   LinkedList<Core> cores = null;
       LinkedList<Core> coresImplicit = null;

	   if(f.exists()) { 
		   cores = loadCores();
		   coresImplicit = loadCores();

	   }else{
		   System.out.println("Loading cores...");
		   cores = initCores();
		   saveClass(cores);
	   }
	   //print results
	   
	   System.out.println("SYSTEM 0");
	   for(int j=0;j<cores.size();j++){
		   double executionTime=0.0;
		   double period=0.0;
		   double utilization=0.0;
		   System.out.println("******** CORE"+j);
		   for(int i=0;i<cores.get(j).tasks.size();i++){
			   executionTime=cores.get(j).tasks.get(i).executionTime;
			   period=cores.get(j).tasks.get(i).period;
			   utilization+=executionTime/period;
			   System.out.println(cores.get(j).tasks.get(i) + ", utilization: "+utilization);
		   }
	   }
	   /*
	   System.out.println(cores.get(2).tasks.get(2).name+" | "+cores.get(2).tasks.get(2).runnables.get(232).name+" | ");
	   cores.get(2).tasks.get(2).runnables.get(232).printLabels();
	   System.out.println(cores.get(2).tasks.get(4).name+" | "+cores.get(2).tasks.get(4).runnables.get(85).name+" | ");
	   cores.get(2).tasks.get(4).runnables.get(85).printLabels();
      
	   helper_communication.computeEffectChain(cores,"EffectChain_1",xmlDocument,xPath);
	   helper_communication.computeEffectChain(cores,"EffectChain_2",xmlDocument,xPath);
	   helper_communication.computeEffectChain(cores,"EffectChain_3",xmlDocument,xPath);
	   */

	   
	   LinkedList<Core> coresExplicit = new LinkedList<Core>();
       coresExplicit=computeExplicit(cores);
       coresImplicit=computeImplicit(coresImplicit);
       //helper_communication.matrixCommunication(coresExplicit,xmlDocument,xPath);
      
       System.out.println("FINISH");
       
       /*
       System.out.println("EXPLICIT");
       helper_communication.queryEC(coresExplicit.get(0),coresExplicit.get(2),coresExplicit.get(3),"EXPLICIT");

       System.out.println("IMPLICIT");
       helper_communication.queryEC(coresImplicit.get(0),coresImplicit.get(2),coresImplicit.get(3),"IMPLICIT");
        */
       
       //computeEC1(coresExplicit, coresImplicit);
       //computeEC2(coresExplicit, coresImplicit);
     
   }
   
   public static void computeEC1(LinkedList<Core> coresExplicit, LinkedList<Core> coresImplicit){
	   try{
    	   
   		//public ExplicitCommunication (int Task1 , double BCST1, double WCRT1, int Task2 , double BCST2, double WCRT2, int Task3 , double BCST3, double WCRT3, double WCRTTask3) {
   	   
			System.out.println(coresExplicit.get(2).tasks.get(4).runnables.get(7).name);
			System.out.println(coresExplicit.get(3).tasks.get(3).runnables.get(19).name);
			System.out.println(coresExplicit.get(2).tasks.get(0).runnables.get(8).name);

			ExplicitCommunication E2E = new ExplicitCommunication(
			       			(int)coresExplicit.get(2).tasks.get(4).period, coresExplicit.get(2).tasks.get(4).runnables.get(7).BCST, coresExplicit.get(2).tasks.get(4).runnables.get(7).WCRT,
			       			(int)coresExplicit.get(3).tasks.get(3).period, coresExplicit.get(3).tasks.get(3).runnables.get(19).BCST, coresExplicit.get(3).tasks.get(3).runnables.get(19).WCRT,
			       			(int)coresExplicit.get(2).tasks.get(0).period, coresExplicit.get(2).tasks.get(0).runnables.get(8).BCST, coresExplicit.get(2).tasks.get(0).runnables.get(8).WCRT,
			       			coresExplicit.get(2).tasks.get(0).responseTime);
			      	
			/*
			System.out.println("EXPLICIT EC1");
			System.out.println(coresExplicit.get(2).tasks.get(4).runnables.get(7).name +
					"; Period: "+(int)coresExplicit.get(2).tasks.get(4).period +" | BCST: "+coresExplicit.get(2).tasks.get(4).runnables.get(7).BCST +" | WCRT: "+coresExplicit.get(2).tasks.get(4).runnables.get(7).WCRT);
			
			System.out.println(coresExplicit.get(3).tasks.get(3).runnables.get(19).name +
					"; Period: "+(int)coresExplicit.get(3).tasks.get(3).period +" | BCST: "+coresExplicit.get(3).tasks.get(3).runnables.get(19).BCST +" | WCRT: "+coresExplicit.get(3).tasks.get(3).runnables.get(19).WCRT);
			
			System.out.println(coresExplicit.get(2).tasks.get(0).runnables.get(8).name +
					"; Period: "+(int)coresExplicit.get(2).tasks.get(0).period +" | BCST: "+coresExplicit.get(2).tasks.get(0).runnables.get(8).BCST +" | WCRT: "+coresExplicit.get(2).tasks.get(0).runnables.get(8).WCRT+
					" RT TASK: "+coresExplicit.get(2).tasks.get(0).responseTime);
			

			System.out.println("IMPLICIT EC1");

			System.out.println(coresImplicit.get(2).tasks.get(4).runnables.get(8).name +
					"; Period: "+(int)coresImplicit.get(2).tasks.get(4).period +" | BCST1_0: "+coresImplicit.get(2).tasks.get(4).runnables.getFirst().BCST +" | WCRT1_0: "+coresImplicit.get(2).tasks.get(4).runnables.getFirst().WCRT
					+" | BCST1_last: "+ coresImplicit.get(2).tasks.get(4).runnables.getLast().BCST+ " | WCRT1_last: "+coresImplicit.get(2).tasks.get(4).runnables.getLast().WCRT);
			
			
			System.out.println(coresImplicit.get(3).tasks.get(3).runnables.get(20).name +
					"; Period: "+(int)coresImplicit.get(3).tasks.get(3).period +" | BCST1_0: "+coresImplicit.get(3).tasks.get(3).runnables.getFirst().BCST +" | WCRT1_0: "+coresImplicit.get(3).tasks.get(3).runnables.getFirst().WCRT
					+" | BCST1_last: "+ coresImplicit.get(3).tasks.get(3).runnables.getLast().BCST+ " | WCRT1_last: "+coresImplicit.get(3).tasks.get(3).runnables.getLast().WCRT);
			
			
			System.out.println(coresImplicit.get(2).tasks.get(0).runnables.get(10).name +
					"; Period: "+(int)coresImplicit.get(2).tasks.get(0).period +" | BCST1_0: "+coresImplicit.get(2).tasks.get(0).runnables.getFirst().BCST +" | WCRT1_0: "+coresImplicit.get(2).tasks.get(0).runnables.getFirst().WCRT
					+" | BCST1_last: "+ coresImplicit.get(2).tasks.get(0).runnables.getLast().BCST+ " | WCRT1_last: "+coresImplicit.get(2).tasks.get(0).runnables.getLast().WCRT);
			*/
			
			ImplicitCommunication MCOP = new ImplicitCommunication(
			       			(int)coresImplicit.get(2).tasks.get(4).period, coresImplicit.get(2).tasks.get(4).runnables.getFirst().WCRT, coresImplicit.get(2).tasks.get(4).runnables.getLast().BCST, coresImplicit.get(2).tasks.get(4).runnables.getLast().WCRT,
			       			(int)coresImplicit.get(3).tasks.get(3).period, coresImplicit.get(3).tasks.get(3).runnables.getFirst().WCRT, coresImplicit.get(3).tasks.get(3).runnables.getLast().BCST, coresImplicit.get(3).tasks.get(3).runnables.getLast().WCRT,
			       			(int)coresImplicit.get(2).tasks.get(0).period, coresImplicit.get(2).tasks.get(0).runnables.getFirst().WCRT, coresImplicit.get(2).tasks.get(0).runnables.getLast().BCST, coresImplicit.get(2).tasks.get(0).runnables.getLast().WCRT
			       			);
			
			//public LETCommunication (int Task1, int Task2, int Task3) {
			 LETCommunication LET = new LETCommunication((int)coresImplicit.get(2).tasks.get(4).period,
					 (int)coresImplicit.get(3).tasks.get(3).period,
					 (int)coresImplicit.get(2).tasks.get(0).period);
			 
         
          	  System.out.println("EXPLICIT");
             E2E.L2FCalculation();
             E2E.L2LCalculation();
             E2E.F2FCalculation();

             
             System.out.println("IMPLICIT");
             MCOP.L2FCalculation();
             MCOP.L2LCalculation();
             MCOP.F2FCalculation();
             
             System.out.println("LET");
             LET.L2FCalculation();
             LET.L2LCalculation();
             LET.F2FCalculation();

      }catch (IOException e) {
             System.out.println(e.getMessage());
      }
   }

   public static void computeEC2(LinkedList<Core> coresExplicit, LinkedList<Core> coresImplicit){
	   try{
    	   
			System.out.println(coresExplicit.get(0).tasks.get(0).runnables.get(3).name);
			System.out.println(coresExplicit.get(2).tasks.get(0).runnables.get(3).name);
			System.out.println(coresExplicit.get(2).tasks.get(3).runnables.get(36).name);

	   		//public ExplicitCommunication (int Task1 , double BCST1, double WCRT1, int Task2 , double BCST2, double WCRT2, int Task3 , double BCST3, double WCRT3, double WCRTTask3) {
			ExplicitCommunication E2E = new ExplicitCommunication(
						    799, coresExplicit.get(0).tasks.get(0).runnables.get(3).BCST, coresExplicit.get(0).tasks.get(0).runnables.get(3).WCRT,
			       			(int)coresExplicit.get(2).tasks.get(0).period, coresExplicit.get(2).tasks.get(0).runnables.get(3).BCST, coresExplicit.get(2).tasks.get(0).runnables.get(3).WCRT,
			       			(int)coresExplicit.get(2).tasks.get(3).period, coresExplicit.get(2).tasks.get(3).runnables.get(36).BCST, coresExplicit.get(2).tasks.get(3).runnables.get(36).WCRT,
			       			coresExplicit.get(2).tasks.get(3).responseTime);
			/*
			System.out.println("EXPLICIT EC2");
			System.out.println(coresExplicit.get(0).tasks.get(0).runnables.get(3).name +
					"; Period: "+(int)coresExplicit.get(0).tasks.get(0).period +" | BCST: "+coresExplicit.get(0).tasks.get(0).runnables.get(3).BCST +" | WCRT: "+coresExplicit.get(0).tasks.get(0).runnables.get(3).WCRT);
			
			System.out.println(coresExplicit.get(2).tasks.get(0).runnables.get(3).name +
					"; Period: "+(int)coresExplicit.get(2).tasks.get(0).period +" | BCST: "+coresExplicit.get(2).tasks.get(0).runnables.get(3).BCST +" | WCRT: "+coresExplicit.get(2).tasks.get(0).runnables.get(3).WCRT);
			
			System.out.println(coresExplicit.get(2).tasks.get(3).runnables.get(36).name +
					"; Period: "+(int)coresExplicit.get(2).tasks.get(3).period +" | BCST: "+coresExplicit.get(2).tasks.get(3).runnables.get(36).BCST +" | WCRT: "+coresExplicit.get(2).tasks.get(3).runnables.get(36).WCRT+
					" RT TASK: "+coresExplicit.get(2).tasks.get(3).responseTime);
			

			System.out.println("IMPLICIT EC2");
			System.out.println(coresImplicit.get(0).tasks.get(0).runnables.get(4).name +
					"; Period: "+(int)coresImplicit.get(0).tasks.get(0).period +" | BCST1_0: "+coresImplicit.get(0).tasks.get(0).runnables.getFirst().BCST +" | WCRT1_0: "+coresImplicit.get(0).tasks.get(0).runnables.getFirst().WCRT
					+" | BCST1_last: "+ coresImplicit.get(0).tasks.get(0).runnables.getLast().BCST+ " | WCRT1_last: "+coresImplicit.get(0).tasks.get(0).runnables.getLast().WCRT);
			
			
			System.out.println(coresImplicit.get(2).tasks.get(0).runnables.get(4).name +
					"; Period: "+(int)coresImplicit.get(2).tasks.get(0).period +" | BCST1_0: "+coresImplicit.get(2).tasks.get(0).runnables.getFirst().BCST +" | WCRT1_0: "+coresImplicit.get(2).tasks.get(0).runnables.getFirst().WCRT
					+" | BCST1_last: "+ coresImplicit.get(2).tasks.get(0).runnables.getLast().BCST+ " | WCRT1_last: "+coresImplicit.get(2).tasks.get(0).runnables.getLast().WCRT);
			
			
			System.out.println(coresImplicit.get(2).tasks.get(3).runnables.get(37).name +
					"; Period: "+(int)coresImplicit.get(2).tasks.get(3).period +" | BCST1_0: "+coresImplicit.get(2).tasks.get(3).runnables.getFirst().BCST +" | WCRT1_0: "+coresImplicit.get(2).tasks.get(3).runnables.getFirst().WCRT
					+" | BCST1_last: "+ coresImplicit.get(2).tasks.get(3).runnables.getLast().BCST+ " | WCRT1_last: "+coresImplicit.get(2).tasks.get(3).runnables.getLast().WCRT);
			*/
			

			//public ImplicitCommunication (int Task1 , double BCST1_0, double WCRT1_0, double BCST1_last, double WCRT1_last, 
			//		int Task2 , double BCST2_0, double WCRT2_0, double BCST2_last, double WCRT2_last, 
			//		int Task3 , double BCST3_0, double WCRT3_0, double BCST3_last, double WCRT3_last) {
			ImplicitCommunication MCOP = new ImplicitCommunication(
						    799, coresImplicit.get(0).tasks.get(0).runnables.getFirst().WCRT, coresImplicit.get(0).tasks.get(0).runnables.getLast().BCST, coresImplicit.get(0).tasks.get(0).runnables.getLast().WCRT,
			       			(int)coresImplicit.get(2).tasks.get(0).period, coresImplicit.get(2).tasks.get(0).runnables.getFirst().WCRT, coresImplicit.get(2).tasks.get(0).runnables.getLast().BCST, coresImplicit.get(2).tasks.get(0).runnables.getLast().WCRT,
			       			(int)coresImplicit.get(2).tasks.get(3).period, coresImplicit.get(2).tasks.get(3).runnables.getFirst().WCRT, coresImplicit.get(2).tasks.get(3).runnables.getLast().BCST, coresImplicit.get(2).tasks.get(3).runnables.getLast().WCRT
			       			);
   	   
			 LETCommunication LET = new LETCommunication(799,
					 (int)coresImplicit.get(2).tasks.get(0).period,
					 (int)coresImplicit.get(2).tasks.get(3).period);
             //ExplicitCommunication E2E = new ExplicitCommunication(800,9.844000928,24.272001184000004,2000,3.896001524,39.668002,50000,795.6880232479997,16354.920024780005,16981.936030792007);
             //ExplicitCommunication E2E = new ExplicitCommunication(800,11.210000986666667,20.22666765333333,2000,11.55000166666667,33.056668333333334,50000,677.3266867,12469.11002065,16935.212000000007);
             //ImplicitCommunication MCOP = new ImplicitCommunication(800,0,159.392,9.844,24.272,2000,0,266.468,101.028,323.2680000000001,50000,0, 13122.360000000004,1034.2160000000001,16981.936000000005);

          	  System.out.println("EXPLICIT");
             E2E.L2FCalculation();
             E2E.L2LCalculation();
             E2E.F2FCalculation();

             
             System.out.println("IMPLICIT");
             MCOP.L2FCalculation();
             MCOP.L2LCalculation();
             MCOP.F2FCalculation();
             
             System.out.println("LET");
             LET.L2FCalculation();
             LET.L2LCalculation();
             LET.F2FCalculation();

      }catch (IOException e) {
             System.out.println(e.getMessage());
      }
   }
   
   public static LinkedList<Core> initCores(){
		FileInputStream file;
		LinkedList<Core> cores=new LinkedList<Core>();
		try {
			
			file = new FileInputStream(new File("ChallengeModel_2017.amxmi"));
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder =  builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(file);
			XPath xPath =  XPathFactory.newInstance().newXPath();
			
			Core core0=helper_communication.load_core("Scheduler_CORE0?type=os.TaskScheduler",xmlDocument,xPath); //all preempptive |  U: 0.9701929364654952
			Core core1=helper_communication.load_core("Scheduler_CORE1?type=os.TaskScheduler",xmlDocument,xPath); //all preempptive | U: 1.3357246246246244
			Core core2=helper_communication.load_core("Scheduler_CORE2?type=os.TaskScheduler",xmlDocument,xPath); //preempptive & cooperative | U: 1.0685265349999995
			Core core3=helper_communication.load_core("Scheduler_CORE3?type=os.TaskScheduler",xmlDocument,xPath); //all preempptive | U: 1.1793503684210531
			
			//Core core0=load_manual_core();
			//order tasks by priority
			Collections.sort(core0.tasks);
			Collections.sort(core1.tasks);
			Collections.sort(core2.tasks);
			Collections.sort(core3.tasks);
			System.out.println("Cores already loaded");
			
			cores.add(core0);
			cores.add(core1);
			cores.add(core2);
			cores.add(core3);
			//matrixCommunication(cores,xmlDocument,xPath);
			//System.out.println("Matrix communication computed");
			
			//this function balance the utilization	
			//relaxSystem2(core0,core2,core3);
			//calculate response time of all core tasks
			System.out.println("Computing RTA");
			core0.RTAcore(); 
			core1.RTAcore(); 
			core2.RTAcore(); 
			core3.RTAcore(); 

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cores;

   }
   
   public static LinkedList<Core> computeExplicit(LinkedList<Core> coresExplicit){
	   
	   for(Core c:coresExplicit){
		   for(Task t:c.tasks){
			   for(Runnable r:t.runnables){
				   for(Label l: r.labels){
					   
					   if(l.labelLocation.equals("GRAM") | !l.labelLocation.replace("LRAM", "").equals(c.name.replace("Scheduler_CORE",""))){
						   r.WCET += (l.frequencyAccess*9/r.frequency)*(1000000.0);
						   r.ACET += (l.frequencyAccess*9/r.frequency)*(1000000.0);
						   r.BCET += (l.frequencyAccess*9/r.frequency)*(1000000.0);

					   }
					   else{
						   r.WCET += (l.frequencyAccess*1/r.frequency)*(1000000.0);
						   r.ACET += (l.frequencyAccess*1/r.frequency)*(1000000.0);
						   r.BCET += (l.frequencyAccess*1/r.frequency)*(1000000.0);

					   }
				   }
			   }
		   }
		    
	   }
	   
       for(Core c:coresExplicit){
    	   for(Task t:c.tasks)
    		   t.executionTime=t.processInstructions();
           c.RTAcore(); 

       }
       
	   System.out.println("EXPLICIT SYSTEM");

       for(int j=0;j<coresExplicit.size();j++){
		   double executionTime=0.0;
		   double period=0.0;
		   double utilization=0.0;
		   System.out.println("******** CORE"+j);
		   for(int i=0;i<coresExplicit.get(j).tasks.size();i++){
			   executionTime=coresExplicit.get(j).tasks.get(i).executionTime;
			   period=coresExplicit.get(j).tasks.get(i).period;
			   utilization+=executionTime/period;
			   System.out.println(coresExplicit.get(j).tasks.get(i) + ", utilization: "+utilization);
		   }
	   }
	   
	   return coresExplicit;
	   
   }
   
   public static LinkedList<Core> computeImplicit(LinkedList<Core> coresImplicit){
	   
	   for(Core c:coresImplicit){
		   for(Task t:c.tasks){
			   int IMPLICIT_costCopyInPerTask=0;
			   int IMPLICIT_costCopyOutPerTask=0;
			   for(Runnable r:t.runnables){
				   for(Label l: r.labels){
					   
					   if(l.isShared){
						   if(l.labelLocation.equals("GRAM") | !l.labelLocation.replace("LRAM", "").equals(c.name.replace("Scheduler_CORE",""))){
							   if(l.typeOfAccess.equals("R")){
								   IMPLICIT_costCopyInPerTask+=9;
							   }
							   else{
								   IMPLICIT_costCopyOutPerTask+=9;
								   IMPLICIT_costCopyInPerTask+=9;
							   }
						   }else{
							   if(l.typeOfAccess.equals("R")){
								   IMPLICIT_costCopyInPerTask+=1;
							   }
							   else{
								   IMPLICIT_costCopyOutPerTask+=1;
								   IMPLICIT_costCopyInPerTask+=1;

							   }
						   }
						   r.WCET += (l.frequencyAccess*1/r.frequency)*(1000000.0);
						   r.ACET += (l.frequencyAccess*1/r.frequency)*(1000000.0);
						   r.BCET += (l.frequencyAccess*1/r.frequency)*(1000000.0);
					   }else{ 
						   int cost=0;
						   
						   if(l.labelLocation.equals("GRAM") | !l.labelLocation.replace("LRAM", "").equals(c.name.replace("Scheduler_CORE","")))
							   cost = 9;
						   else
							   cost = 1;
						   
						   r.WCET += (l.frequencyAccess*cost/r.frequency)*(1000000.0);
						   r.ACET += (l.frequencyAccess*cost/r.frequency)*(1000000.0);
						   r.BCET += (l.frequencyAccess*cost/r.frequency)*(1000000.0);
					   }

					   
				   }
			   }
			   double costReading=IMPLICIT_costCopyInPerTask;
			   double costPublishing=IMPLICIT_costCopyOutPerTask;
			   t.runnables.addFirst(new Runnable("Copy_In",costReading));
			   t.runnables.add(new Runnable("Copy_Out",costPublishing));
		   }
		    
	   }
	   
       for(Core c:coresImplicit){
    	   for(Task t:c.tasks)
    		   t.executionTime=t.processInstructions();
           c.RTAcore(); 

       }
       
	   System.out.println("IMPLICIT SYSTEM");

       for(int j=0;j<coresImplicit.size();j++){
		   double executionTime=0.0;
		   double period=0.0;
		   double utilization=0.0;
		   System.out.println("******** CORE"+j);
		   for(int i=0;i<coresImplicit.get(j).tasks.size();i++){
			   executionTime=coresImplicit.get(j).tasks.get(i).executionTime;
			   period=coresImplicit.get(j).tasks.get(i).period;
			   utilization+=executionTime/period;
			   System.out.println(coresImplicit.get(j).tasks.get(i) + ", utilization: "+utilization);
		   }
	   }
	   
	   return coresImplicit;
	   
   }
   
   public static void saveClass(LinkedList<Core> cores){
	   try {
	         FileOutputStream fileOut =
	         new FileOutputStream("cores.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(cores);
	         out.close();
	         fileOut.close();
	         System.out.println("Serialized data is saved in cores.ser");
	      }catch(IOException i) {
	         i.printStackTrace();
	      }
   }
   
   public static LinkedList<Core> loadCores() throws ClassNotFoundException{
	   LinkedList<Core> cores=null;
	   try {
		   FileInputStream fileIn = new FileInputStream("cores.ser");
		   ObjectInputStream in = new ObjectInputStream(fileIn);
		   cores = (LinkedList<Core>) in.readObject();
		   in.close();
		   fileIn.close();
	   }catch(IOException i) {
		   i.printStackTrace();
	   }
   		
	   return cores;
	}
}


