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
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
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
      /*
       System.out.println("FINISH");
       
       
       System.out.println("EXPLICIT");
       helper_communication.queryEC(coresExplicit.get(0),coresExplicit.get(2),coresExplicit.get(3),"EXPLICIT");

       System.out.println("IMPLICIT");
       helper_communication.queryEC(coresImplicit.get(0),coresImplicit.get(2),coresImplicit.get(3),"IMPLICIT");
       */
       
       LinkedList<EffectChain> effectChainsExplicit =helper_communication.effectChainBuilder(coresExplicit,xmlDocument,xPath);
       LinkedList<EffectChain> effectChainsImplicit =helper_communication.effectChainBuilder(coresImplicit,xmlDocument,xPath);
       LinkedList<EffectChain> effectChainsLET      =helper_communication.effectChainBuilder(coresImplicit,xmlDocument,xPath);
       
       computeEC(effectChainsExplicit,effectChainsImplicit,effectChainsLET);
       //computeECExplicit(effectChainsExplicit);
       //computeECImplicit(effectChainsImplicit);
       //computeECLET(effectChainsLET);
       
       //processEffectChains(effectChains);
       System.out.println("Finish");
       //matrixCommunication(cores,xmlDocument,xPath);
       //computeEC1(coresExplicit, coresImplicit);
       //computeEC2(coresExplicit, coresImplicit);
     
   }
   
   //#cores;#tasks;Age_Explicit;Reaction_Explicit;Age_Implicit;Reaction_Implicit;Age_LET;Reaction_LET; .csv
   private static void computeEC(LinkedList<EffectChain> effectChainsExplicit,
	   LinkedList<EffectChain> effectChainsImplicit, LinkedList<EffectChain> effectChainsLET) throws IOException {
	   String path = "./res/res.csv";
	   PrintWriter writer = new PrintWriter(path);
	   writer.println("#cores;#tasks;Age_Explicit;Reaction_Explicit;Age_Implicit;Reaction_Implicit;Age_LET;Reaction_LET");
	   for(int i=0;i<effectChainsExplicit.size();i++) {
		   
			ArrayList<Double> ExplicitTasks  = new ArrayList<Double>();
			ArrayList<Double> ImplicitTasks  = new ArrayList<Double>();
			ArrayList<Integer> LETTasks  = new ArrayList<Integer>();
    		File file = new File("C:/Users/LPC/Desktop/points.txt");
    		file.delete();
    		
    		int numCores=effectChainsExplicit.get(i).numCores;
    		int numTasks=effectChainsExplicit.get(i).steps+1;
			for (pairRT pRT:effectChainsExplicit.get(i).comm_element) {
				ExplicitTasks.add(pRT.task.period);		
				ExplicitTasks.add(pRT.runnable.BCST);				
				ExplicitTasks.add(pRT.runnable.WCRT);
			}
			
			for (pairRT pRT:effectChainsImplicit.get(i).comm_element) {

				ImplicitTasks.add(pRT.task.period);		
				ImplicitTasks.add(pRT.task.runnables.getFirst().WCRT);				
				ImplicitTasks.add(pRT.task.runnables.getLast().BCST);			
				ImplicitTasks.add(pRT.task.runnables.getLast().WCRT);
			}
			
			for (pairRT pRT:effectChainsLET.get(i).comm_element) {

				LETTasks.add((int)pRT.task.period);
			}

			ExplicitCommunicationNTasks E2E = new ExplicitCommunicationNTasks(ExplicitTasks);
			double ageExplicit =  E2E.L2LCalculation();
			double reactionExplicit = E2E.F2FCalculation();
			
			ImplicitCommunicationNTasks MCOP = new ImplicitCommunicationNTasks(ImplicitTasks);
			double ageImplicit =  MCOP.L2LCalculation();
			double reactionImplicit = MCOP.F2FCalculation();
			
			ArrayList<Integer> Points = new ArrayList<Integer>();
			LETCommunicationNTasks LET = new LETCommunicationNTasks(LETTasks);
			Points = LET.ThetaCalculation();
			double ageLET = LET.AlphaCalculation(Points);
			double reactionLET  = LET.RhoCalculation(Points);
			
			if(ageExplicit<ageLET && reactionExplicit<reactionLET && ageImplicit<ageLET && reactionImplicit<reactionLET) {

				writer.println(numCores+";"+numTasks+";"+ageExplicit+";"+reactionExplicit+";"+ageImplicit+";"+reactionImplicit+";"+ageLET+";"+reactionLET);
			}

	   }
	   writer.close();

	   
   }
   
	private static void removeFiles(String paradigm) {
		File dir = new File("./res/"+paradigm+"/");
		for(File file: dir.listFiles()) 
		    if (!file.isDirectory()) 
		        file.delete();
   }

   public static void computeECExplicit(LinkedList<EffectChain> effectChainsExplicit) throws IOException{
	   System.out.println("EXPLICIT");
	   removeFiles("explicit");
	   // Task1, BCST1, WCRT1, Task2 , BCST2, WCRT2, Task3, BCST3, WCRT3
	   for(EffectChain ec:effectChainsExplicit) {
		   
			ArrayList<Double> ExplicitTasks  = new ArrayList<Double>();
			for (pairRT pRT:ec.comm_element) {
				ExplicitTasks.add(pRT.task.period);		
				ExplicitTasks.add(pRT.runnable.BCST);				
				ExplicitTasks.add(pRT.runnable.WCRT);

			}
			ExplicitCommunicationNTasks E2E = new ExplicitCommunicationNTasks(ExplicitTasks);

			double age =  E2E.L2LCalculation();
			double reaction = E2E.F2FCalculation();

			writeFile(ec,reaction,age,"explicit");
		
	   }

   }
   

   public static void computeECImplicit(LinkedList<EffectChain> effectChainsImplicit) throws IOException{
	   System.out.println("IMPLICIT");
	   removeFiles("implicit");
	   //Task1 p, WCRT1_0, BCST1_last, WCRT_Task1, Task2 , WCRT2_0, BCST2_last, WCRT_Task2, Task3 , WCRT3_0, BCST3_last, WCRT_Task3
	   for(EffectChain ec:effectChainsImplicit) {

			ArrayList<Double> ImplicitTasks  = new ArrayList<Double>();
			for (pairRT pRT:ec.comm_element) {
				ImplicitTasks.add(pRT.task.period);		
				ImplicitTasks.add(pRT.task.runnables.getFirst().WCRT);				
				ImplicitTasks.add(pRT.task.runnables.getLast().BCST);			
				ImplicitTasks.add(pRT.task.runnables.getLast().WCRT);		


			}
			ImplicitCommunicationNTasks MCOP = new ImplicitCommunicationNTasks(ImplicitTasks);
			
			double age =  MCOP.L2LCalculation();
			double reaction = MCOP.F2FCalculation();
			
			writeFile(ec,reaction,age,"implicit");
	    
	   }
	   
   }

   
   public static void computeECLET(LinkedList<EffectChain> effectChainsLET){
	   try{
			System.out.println("LET");
			removeFiles("let");
			for(EffectChain ec:effectChainsLET) {
	    		File file = new File("C:/Users/LPC/Desktop/points.txt");
	    		file.delete();

				ArrayList<Integer> LETTasks  = new ArrayList<Integer>();
				for (pairRT pRT:ec.comm_element) {
					LETTasks.add((int)pRT.task.period);				
				}

				ArrayList<Integer> Points = new ArrayList<Integer>();
				LETCommunicationNTasks LET = new LETCommunicationNTasks(LETTasks);
				Points = LET.ThetaCalculation();
				double reaction  = LET.RhoCalculation(Points);
				double age = LET.AlphaCalculation(Points);

				writeFile(ec,reaction,age,"let");
			}
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}
   }
   
   public static void writeFile(EffectChain ec, double reaction, double age, String paradigm) throws IOException {
	   	String path = "";
	   	if(ec.numCores==1)
		   	path = "./res/"+paradigm+"/"+"intraCore_s"+ec.steps+".txt";
	   	else
		   	path = "./res/"+paradigm+"/"+"interCore_c"+ec.numCores+"_s"+ec.steps+".txt";

		PrintWriter writer = new PrintWriter(new FileWriter(path, true));
		writer.println(ec.res);
		writer.println("Age: "+age);
		writer.println("Reaction: "+reaction);
		writer.close();
	   
	    writer.close();
	   
   }
   
	private static void processEffectChains(LinkedList<EffectChain> effectChains) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
	    PrintWriter intraCore_s1= new PrintWriter("res/intraCore_s1.txt", "UTF-8");
	    PrintWriter intraCore_s2= new PrintWriter("res/intraCore_s2.txt", "UTF-8");
	    PrintWriter intraCore_s3= new PrintWriter("res/intraCore_s3.txt", "UTF-8");
	    PrintWriter intraCore_s4= new PrintWriter("res/intraCore_s4.txt", "UTF-8");
	    PrintWriter intraCore_s5= new PrintWriter("res/intraCore_s5.txt", "UTF-8");
	    PrintWriter intraCore_s6= new PrintWriter("res/intraCore_s6.txt", "UTF-8");
	    PrintWriter intraCore_s7= new PrintWriter("res/intraCore_s7.txt", "UTF-8");
	    PrintWriter intraCore_s8= new PrintWriter("res/intraCore_s8.txt", "UTF-8");
	    
	    PrintWriter interCore_c2_s1= new PrintWriter("res/interCore_c2_s1.txt", "UTF-8");
	    PrintWriter interCore_c2_s2= new PrintWriter("res/interCore_c2_s2.txt", "UTF-8");
	    PrintWriter interCore_c2_s3= new PrintWriter("res/interCore_c2_s3.txt", "UTF-8");
	    PrintWriter interCore_c2_s4= new PrintWriter("res/interCore_c2_s4.txt", "UTF-8");
	    PrintWriter interCore_c2_s5= new PrintWriter("res/interCore_c2_s5.txt", "UTF-8");
	    PrintWriter interCore_c2_s6= new PrintWriter("res/interCore_c2_s6.txt", "UTF-8");
	    PrintWriter interCore_c2_s7= new PrintWriter("res/interCore_c2_s7.txt", "UTF-8");
	    PrintWriter interCore_c2_s8= new PrintWriter("res/interCore_c2_s8.txt", "UTF-8");
	    
	    PrintWriter interCore_c3_s1= new PrintWriter("res/interCore_c3_s1.txt", "UTF-8");
	    PrintWriter interCore_c3_s2= new PrintWriter("res/interCore_c3_s2.txt", "UTF-8");
	    PrintWriter interCore_c3_s3= new PrintWriter("res/interCore_c3_s3.txt", "UTF-8");
	    PrintWriter interCore_c3_s4= new PrintWriter("res/interCore_c3_s4.txt", "UTF-8");
	    PrintWriter interCore_c3_s5= new PrintWriter("res/interCore_c3_s5.txt", "UTF-8");
	    PrintWriter interCore_c3_s6= new PrintWriter("res/interCore_c3_s6.txt", "UTF-8");
	    PrintWriter interCore_c3_s7= new PrintWriter("res/interCore_c3_s7.txt", "UTF-8");
	    PrintWriter interCore_c3_s8= new PrintWriter("res/interCore_c3_s8.txt", "UTF-8");	    

		
		for(EffectChain ec:effectChains) {
			if(ec.numCores==1 && ec.steps==1) {
				System.out.println(ec.res);
				intraCore_s1.println(ec.res);
			}
			if(ec.numCores==1 && ec.steps==2) {
				intraCore_s2.println(ec.res);
			}
			if(ec.numCores==1 && ec.steps==3) {
				intraCore_s3.println(ec.res);
			}
			if(ec.numCores==1 && ec.steps==4) {
				intraCore_s4.println(ec.res);
			}
			if(ec.numCores==1 && ec.steps==5) {
				intraCore_s5.println(ec.res);
			}
			if(ec.numCores==1 && ec.steps==6) {
				intraCore_s6.println(ec.res);
			}
			if(ec.numCores==1 && ec.steps==7) {
				intraCore_s7.println(ec.res);
			}
			if(ec.numCores==1 && ec.steps==8) {
				intraCore_s8.println(ec.res);
			}
			
			
			if(ec.numCores==2 && ec.steps==1) {
				interCore_c2_s1.println(ec.res);
			}
			if(ec.numCores==2 && ec.steps==2) {
				interCore_c2_s2.println(ec.res);
			}
			if(ec.numCores==2 && ec.steps==3) {
				interCore_c2_s3.println(ec.res);
			}
			if(ec.numCores==2 && ec.steps==4) {
				interCore_c2_s4.println(ec.res);
			}
			if(ec.numCores==2 && ec.steps==5) {
				interCore_c2_s5.println(ec.res);
			}
			if(ec.numCores==2 && ec.steps==6) {
				interCore_c2_s6.println(ec.res);
			}
			if(ec.numCores==2 && ec.steps==7) {
				interCore_c2_s7.println(ec.res);
			}
			if(ec.numCores==2 && ec.steps==8) {
				interCore_c2_s8.println(ec.res);
			}
			
			if(ec.numCores==3 && ec.steps==1) {
				interCore_c3_s1.println(ec.res);
			}
			if(ec.numCores==3 && ec.steps==2) {
				interCore_c3_s2.println(ec.res);
			}
			if(ec.numCores==3 && ec.steps==3) {
				interCore_c3_s3.println(ec.res);
			}
			if(ec.numCores==3 && ec.steps==4) {
				interCore_c3_s4.println(ec.res);
			}
			if(ec.numCores==3 && ec.steps==5) {
				interCore_c3_s5.println(ec.res);
			}
			if(ec.numCores==3 && ec.steps==6) {
				interCore_c3_s6.println(ec.res);
			}
			if(ec.numCores==3 && ec.steps==7) {
				interCore_c3_s7.println(ec.res);
			}
			if(ec.numCores==3 && ec.steps==8) {
				interCore_c3_s8.println(ec.res);
			}
		}
		
		intraCore_s1.close();
		intraCore_s2.close();
		intraCore_s3.close();
		intraCore_s4.close();
		intraCore_s5.close();
		intraCore_s6.close();
		intraCore_s7.close();
		intraCore_s8.close();
		
		interCore_c2_s1.close();
		interCore_c2_s2.close();
		interCore_c2_s3.close();
		interCore_c2_s4.close();
		interCore_c2_s5.close();
		interCore_c2_s6.close();
		interCore_c2_s7.close();
		interCore_c2_s8.close();
		
		interCore_c3_s1.close();
		interCore_c3_s2.close();
		interCore_c3_s3.close();
		interCore_c3_s4.close();
		interCore_c3_s5.close();
		interCore_c3_s6.close();
		interCore_c3_s7.close();
		interCore_c3_s8.close();

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
			       			(int)coresExplicit.get(2).tasks.get(0).period, coresExplicit.get(2).tasks.get(0).runnables.get(8).BCST, coresExplicit.get(2).tasks.get(0).runnables.get(8).WCRT );
      	
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
			
		 
		    System.out.println("EXPLICIT");
		    E2E.L2LCalculation();
		    E2E.F2FCalculation();
		
		   
		    System.out.println("IMPLICIT");
		    MCOP.L2LCalculation();
		    MCOP.F2FCalculation();
			
			//public LETCommunication (int Task1, int Task2, int Task3) {
			ArrayList<Integer> LETTasks  = new ArrayList<Integer>();
			LETTasks.add((int)coresImplicit.get(2).tasks.get(4).period);
			LETTasks.add((int)coresImplicit.get(3).tasks.get(3).period);
			LETTasks.add((int)coresImplicit.get(2).tasks.get(0).period);
	
			 System.out.println("LET");
			 ArrayList<Integer> Points = new ArrayList<>();
			 LETCommunication LET = new LETCommunication(LETTasks);
			 //LET.Test();
			 Points = LET.ThetaCalculation();
			 LET.RhoCalculation(Points);
			 LET.AlphaCalculation(Points);

      }catch (IOException e) {
             System.out.println(e.getMessage());
      }
   }

   public static void computeEC2(LinkedList<Core> coresExplicit, LinkedList<Core> coresImplicit){
	   try{
    	   
			System.out.println(coresExplicit.get(0).tasks.get(0).runnables.get(3).name);
			System.out.println(coresExplicit.get(2).tasks.get(0).runnables.get(3).name);
			System.out.println(coresExplicit.get(2).tasks.get(3).runnables.get(36).name);

			ExplicitCommunication E2E = new ExplicitCommunication(
						    800, coresExplicit.get(0).tasks.get(0).runnables.get(3).BCST, coresExplicit.get(0).tasks.get(0).runnables.get(3).WCRT,
			       			(int)coresExplicit.get(2).tasks.get(0).period, coresExplicit.get(2).tasks.get(0).runnables.get(3).BCST, coresExplicit.get(2).tasks.get(0).runnables.get(3).WCRT,
			       			(int)coresExplicit.get(2).tasks.get(3).period, coresExplicit.get(2).tasks.get(3).runnables.get(36).BCST, coresExplicit.get(2).tasks.get(3).runnables.get(36).WCRT);
			
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
			
			

			ImplicitCommunication MCOP = new ImplicitCommunication(
						    800, coresImplicit.get(0).tasks.get(0).runnables.getFirst().WCRT, coresImplicit.get(0).tasks.get(0).runnables.getLast().BCST, coresImplicit.get(0).tasks.get(0).runnables.getLast().WCRT,
			       			(int)coresImplicit.get(2).tasks.get(0).period, coresImplicit.get(2).tasks.get(0).runnables.getFirst().WCRT, coresImplicit.get(2).tasks.get(0).runnables.getLast().BCST, coresImplicit.get(2).tasks.get(0).runnables.getLast().WCRT,
			       			(int)coresImplicit.get(2).tasks.get(3).period, coresImplicit.get(2).tasks.get(3).runnables.getFirst().WCRT, coresImplicit.get(2).tasks.get(3).runnables.getLast().BCST, coresImplicit.get(2).tasks.get(3).runnables.getLast().WCRT
			       			);
   	   
			 
             //ExplicitCommunication E2E = new ExplicitCommunication(800,9.844000928,24.272001184000004,2000,3.896001524,39.668002,50000,795.6880232479997,16354.920024780005,16981.936030792007);
             //ExplicitCommunication E2E = new ExplicitCommunication(800,11.210000986666667,20.22666765333333,2000,11.55000166666667,33.056668333333334,50000,677.3266867,12469.11002065,16935.212000000007);
             //ImplicitCommunication MCOP = new ImplicitCommunication(800,0,159.392,9.844,24.272,2000,0,266.468,101.028,323.2680000000001,50000,0, 13122.360000000004,1034.2160000000001,16981.936000000005);

          	 System.out.println("EXPLICIT");
             E2E.L2LCalculation();
             E2E.F2FCalculation();

             
             System.out.println("IMPLICIT");
             MCOP.L2LCalculation();
             MCOP.F2FCalculation();
             
             
             ArrayList<Integer> LETTasks = new ArrayList<Integer>();
             LETTasks.add(800);
             LETTasks.add(2000);
			 LETTasks.add(50000);

 			 System.out.println("LET");
 			 ArrayList<Integer> Points = new ArrayList<>();
 			 LETCommunication LET = new LETCommunication(LETTasks);
 			 //LET.Test();
 			 Points = LET.ThetaCalculation();
 			 LET.RhoCalculation(Points);
 			 LET.AlphaCalculation(Points);
 			
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
								   //IMPLICIT_costCopyInPerTask+=9;
							   }
						   }else{
							   if(l.typeOfAccess.equals("R")){
								   IMPLICIT_costCopyInPerTask+=1;
							   }
							   else{
								   IMPLICIT_costCopyOutPerTask+=1;
								   //IMPLICIT_costCopyInPerTask+=1;
								   
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
   
public static LinkedList<Core> computeLET(LinkedList<Core> coresImplicit){
	   
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
								   //IMPLICIT_costCopyInPerTask+=9;
							   }
						   }else{
							   if(l.typeOfAccess.equals("R")){
								   IMPLICIT_costCopyInPerTask+=1;
							   }
							   else{
								   IMPLICIT_costCopyOutPerTask+=1;
								   //IMPLICIT_costCopyInPerTask+=1;
								   
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
			   //t.runnables.add(new Runnable("Copy_Out",costPublishing));
		   }
		    
	   }
	   
       for(Core c:coresImplicit){
    	   for(Task t:c.tasks)
    		   t.executionTime=t.processInstructions();
           c.RTAcore(); 

       }
       
	   System.out.println("LET SYSTEM");

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
   
   //This function prints the matrix communication
   static void matrixCommunication(LinkedList<Core> cores, Document xmlDocument,XPath xPath) throws FileNotFoundException, UnsupportedEncodingException, XPathExpressionException{
	   PrintWriter  writer_IMPLICIT= new PrintWriter("label_memory_mapping_implicit.txt", "UTF-8");
	   PrintWriter  writer_LET= new PrintWriter("label_memory_mapping_LET_h.txt", "UTF-8");

	   System.out.println("Creating matrix communication");
	   for(Core core_src:cores){
		   for(Task t_src: core_src.tasks){
			   writer_IMPLICIT.println("Task "+t_src.name+" communicates with...");
			   writer_LET.println("Task "+t_src.name+" communicates with...");
			   
			   int IMPLICIT_footprint=0;
			   int LET_footprint=0;
			   int IMPLICIT_TcostCopyInPerTask=0;
			   int IMPLICIT_TcostCopyOutPerTask=0;
			   
			   for(Core core_dst:cores){

				   for(Task t_dst: core_dst.tasks){
					   
					   int IMPLICIT_costCopyInPerTask=0;
					   int IMPLICIT_costCopyOutPerTask=0;

					   int LET_costTransactionPerTask=0;
					   
					   if(!t_src.name.equals(t_dst.name)){
						   for(String label_src: t_src.labels){
							  for(String label_dest:t_dst.labels){
								  String src_operation=label_src.substring(label_src.length() - 1);
								  String dst_operation=label_dest.substring(label_dest.length() - 1);
								  if(label_src.substring(0,label_src.length()-2).equals(label_dest.substring(0,label_dest.length()-2))
										  && !src_operation.equals(dst_operation)){
									  	  
									  String label_src_allocation=labelAllocation(label_src.substring(0,label_src.length()-2),xmlDocument,xPath);
									  String label_dst_allocation=labelAllocation(label_dest.substring(0,label_dest.length()-2),xmlDocument,xPath);
									  String core_source=core_of_task(t_src.name, xmlDocument, xPath).replace("?type=hw.Core","");
									  String core_dest=core_of_task(t_dst.name, xmlDocument, xPath).replace("?type=hw.Core","");
									  
									  //IMPLICIT
									  int IMPLICIT_costCopyIn=calculateCostCopyIn_IMPLICIT(label_src_allocation,core_source);
									  int IMPLICIT_costCopyOut=0;
									  if(src_operation.equals("w")) //cost copy out only for writers
										  IMPLICIT_costCopyOut=calculateCostCopyOut_IMPLICIT(label_src_allocation,core_source);

									  writer_IMPLICIT.println("\t Task_src: "+t_src.name+"("+core_source+")"+" label: "+label_src+ "("
									  + label_src_allocation +")" +
									  " || Task_dst: "+t_dst.name+"("+core_dest+")"+" label: "+label_dest 
									  + "("+ label_dst_allocation +")" + " || Cost copy in: "+ IMPLICIT_costCopyIn + " - Cost copy out: "+ IMPLICIT_costCopyOut);
									  IMPLICIT_costCopyInPerTask+=IMPLICIT_costCopyIn;
									  IMPLICIT_costCopyOutPerTask+=IMPLICIT_costCopyOut;
									  if(src_operation.equals("r"))
										  IMPLICIT_footprint+=size_of_label(label_src.substring(0,label_src.length()-2),xmlDocument,xPath);
									  
									  //LET
									  int LET_costTransaction=0;
									  if(src_operation.equals("w")) //cost Transaction out only for writers
										  LET_costTransaction=calculateCostTransaction_LET(label_src_allocation,core_dest);

									  writer_LET.println("\t Task_src: "+t_src.name+"("+core_source+")"+" label: "+label_src+ "("
									  + label_src_allocation +")" +
									  " || Task_dst: "+t_dst.name+"("+core_dest+")"+" label: "+label_dest 
									  + "("+ label_dst_allocation +")" + " || Cost transaction: "+ LET_costTransaction);
									  LET_costTransactionPerTask+=LET_costTransaction;
									  if(src_operation.equals("r")){
										  int footprint=size_of_label(label_src.substring(0,label_src.length()-2),xmlDocument,xPath);							  
										  if(typeOfCommunication(t_src.name,t_dst.name).equals("nh")){ //non-harmonic tasks use the double of footprint
											  if(t_src.name.equals("Task_20ms"))
												  footprint=footprint*3;
											  else if(t_src.name.equals("Task_50ms"))
												  footprint=footprint*2;
										  }else if(typeOfCommunication(t_src.name,t_dst.name).equals("a")){
											  footprint=footprint*3;
										  }
										  LET_footprint+=footprint;
									  }
								  }
							  }
						   }
					   }
					   IMPLICIT_TcostCopyInPerTask+=IMPLICIT_costCopyInPerTask;
					   IMPLICIT_TcostCopyOutPerTask+=IMPLICIT_costCopyOutPerTask;
					   
					   if(IMPLICIT_costCopyInPerTask!=0)
						   writer_IMPLICIT.println("\t \t Cost copy in "+t_src.name+" to "+t_dst.name+" is: "+IMPLICIT_costCopyInPerTask+ " cycles");
					   if(IMPLICIT_costCopyOutPerTask!=0)
						   writer_IMPLICIT.println("\t \t Cost copy out "+t_src.name+" to "+t_dst.name+" is: "+IMPLICIT_costCopyOutPerTask+ " cycles");
					   if(LET_costTransactionPerTask!=0)
						   writer_LET.println("\t \t Total cost transaction "+t_src.name+" to "+t_dst.name+ " is: "+LET_costTransactionPerTask+ " cycles");
				   }
				   
			   }
			   double costReading=IMPLICIT_TcostCopyInPerTask;
			   double costPublishing=IMPLICIT_TcostCopyOutPerTask;
			   t_src.runnables.addFirst(new Runnable("Copy_In",costReading));
			   t_src.runnables.add(new Runnable("Copy_Out",costPublishing));

			   writer_IMPLICIT.println("******** Cost copy in task: "+t_src.name+" is: "+IMPLICIT_TcostCopyInPerTask+ " cycles");
			   writer_IMPLICIT.println("******** Cost copy out task: "+t_src.name+" is: "+IMPLICIT_TcostCopyOutPerTask+ " cycles");
			   writer_IMPLICIT.println("******** Total footprint used for shared_labels task: "+t_src.name+" is: "+IMPLICIT_footprint+ " bits");
			   
			   writer_LET.println("******** Total footprint used for shared_labels task: "+t_src.name+" is: "+LET_footprint+ " bits");

		   }
	   }
	   writer_IMPLICIT.close();
	   writer_LET.close();

   }
   
   public static int size_of_label(String labelName, Document xmlDocument,XPath xPath) throws XPathExpressionException, FileNotFoundException, UnsupportedEncodingException{
	   int numberBits=0;
    String expression = "*/swModel/labels[@name='" + labelName + "']/size/@numberBits";
    NodeList label = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
    numberBits=Integer.parseInt(label.item(0).getFirstChild().getNodeValue()); 
    return numberBits;
}
 //This function returns the core of the task "taskName"
   public static String core_of_task(String taskName, Document xmlDocument,XPath xPath) throws XPathExpressionException{
	   taskName=taskName+"?type=sw.Task";
	   String result="";
       String expression = "*/mappingModel/processAllocation[@process='" + taskName + "']/@scheduler";
       NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
       for (int i = 0; i < nodeList.getLength(); i++) {
           String scheduledTask=nodeList.item(i).getFirstChild().getNodeValue();
           expression = "*/mappingModel/coreAllocation[@scheduler='" + scheduledTask + "']/@core";
           NodeList cores = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
           result=cores.item(0).getFirstChild().getNodeValue(); 
       }
       return result;
   }
   
    //here we pay in function of where source core and LRAM dest location
   private static int calculateCostTransaction_LET(String label_src_allocation,String core_source) {
	   int cost=9;
	   if(label_src_allocation.substring(label_src_allocation.length() - 1).equals(core_source.substring(core_source.length() - 1)))
		   cost=1;
	   
	   return cost;
   }
   
   private static int calculateCostCopyIn_IMPLICIT(String label_src_allocation, String core_source) {
	   int cost=9;
	   if(label_src_allocation.substring(label_src_allocation.length() - 1).equals(core_source.substring(core_source.length() - 1)))
		   cost=1;
	   
	   return cost;
   }
   
   private static int calculateCostCopyOut_IMPLICIT(String label_src_allocation, String core_source) {
	   int cost=9;
	   if(label_src_allocation.substring(label_src_allocation.length() - 1).equals(core_source.substring(core_source.length() - 1)))
		   cost=1;
	   
	   return cost;
   }

   //This function returns in which RAM is allocated the label
   private static String labelAllocation(String label, Document xmlDocument,XPath xPath) throws XPathExpressionException{
	   label=label+"?type=sw.Label";
       String expression = "*/mappingModel/mapping[@abstractElement='" + label + "']/@mem";
       NodeList labelNodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
       String labelAllocation=labelNodeList.item(0).getFirstChild().getNodeValue().replace("?type=hw.Memory", "");
       return labelAllocation;
   }
   
      private static String typeOfCommunication(String t_src, String t_dst) {
	   String communicationType="h";
	   if(t_src.contains("ISR") || t_dst.contains("ISR") || t_src.contains("Angle") || t_dst.contains("Angle")){
		   communicationType="a";
	   }else{
		   if(t_src.contains("Task_") && t_dst.contains("Task_")){
			   int t1=Integer.parseInt(t_src.replace("Task_","").replace("ms",""));
			   int t2=Integer.parseInt(t_dst.replace("Task_","").replace("ms",""));
			   if(t1>t2){
				   int mod=t1%t2;
				   if(mod!=0)
					   communicationType="nh";
			   }else{
				   int mod=t2%t1;
				   if(mod!=0)
					   communicationType="nh";
			   }
	   	   }else{
	   		   communicationType="nh";
	   	   }
	   }
	   return communicationType;
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


