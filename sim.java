******************** 
* Taylor Sprinkle  *
* ECE 463          *
* Branch Predictor *
********************

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.Math.*;


public class sim {

	public static int M=0,  arindex=0, table[], preds=0, mispreds=0, numentries=0, N=0, initvalue = 2;
	public static String inputfile, bhrtable="";
	public static double mispredictionrate=0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean bmodal = false, prediction=false;
		
		String sim = args[0];
		if(sim.equals("gshare")) bmodal = false;
		else if(sim.equals("bimodal")) bmodal = true;
		
		if(bmodal){
			M = Integer.parseInt(args[1]);
			inputfile = args[4];
			maketable();
			
		}
		
		if(!bmodal){
			M = Integer.parseInt(args[1]);
			N = Integer.parseInt(args[2]);
			inputfile = args[5];
			maketable();
			buildBHR();
			
		}
		

		 try{

			  FileInputStream fstream = new FileInputStream(inputfile);
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;

			  while ((strLine = br.readLine()) != null)   {
				
				  if(bmodal){
					getm(strLine);
					if(table[arindex] > 2 || table[arindex] == 2) prediction = true;
					else prediction = false;
					preds++;
					if(strLine.charAt(strLine.length()-1)=='t'){
						if(prediction == false)mispreds++;
						if(table[arindex] < 3) table[arindex]++;
					}
					
					if(strLine.charAt(strLine.length()-1)=='n'){
						if(prediction==true) mispreds++;
						if(table[arindex] > 0) table[arindex]--;
					}
					
				}
				  
				  if(!bmodal){
					  getnm(strLine);
					  if(table[arindex] > 2 || table[arindex] == 2) prediction = true;
						else prediction = false;
						preds++;
						if(strLine.charAt(strLine.length()-1)=='t'){
							if(prediction == false)mispreds++;
							if(table[arindex] < 3) table[arindex]++;
						updateBHRtaken();
						
						}
						
						if(strLine.charAt(strLine.length()-1)=='n'){
							if(prediction==true) mispreds++;
							if(table[arindex] > 0) table[arindex]--;
							updateBHRnot();
							
						}
						
				  }


			  }

			    }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }

		mispredictionrate = ((double) mispreds / preds)*100;
		 System.out.println("COMMAND");
		
		 System.out.println("OUTPUT");
		 System.out.println("number of predictions: " + preds);
	        System.out.println("number of mispredictions: " + mispreds);
	        System.out.println("misprediction rate: " + (mispredictionrate) + "%");
	       if(bmodal) System.out.println("FINAL BIMODAL CONTENTS");
	       if(!bmodal) System.out.println("FINAL GSHARE CONTENTS");
	        for(int i=0; i<Math.pow(2, M);i++)
	        	System.out.println(i  + "  " + table[i]);		 
		// TODO Auto-generated method stub 

	}
	
	
	public static void maketable(){
		

		
	int x = (int) Math.pow(2, M);

	table = new int[x];

	//fill up table with 2
	for(int i =0; i< Math.pow(2, M); i++)
	{
	table[i] = initvalue;
	}
	}

public static void buildBHR(){//Builds Branch History Register Table
	
	for(int i=0; i<N; i++)
		  bhrtable +="0";//initialize to zeroes
	
}

public static void getm(String strLine){


String value = strLine.substring(0,strLine.length()-2);
//value has the hex value

int valueint = Integer.parseInt(value,16);

int thefirst = Integer.parseInt(value.substring(0,1));

String exzero = "";
if(thefirst == 0)      exzero = "0000"; else if(thefirst == 1) exzero = "000"; else if(thefirst == 2) exzero = "00";
else if(thefirst == 3) exzero = "00";   else if(thefirst == 4) exzero = "0";   else if(thefirst == 5) exzero = "0";
else if(thefirst == 6) exzero = "0";    else if(thefirst == 7) exzero = "0";   


String valuebinary = Integer.toBinaryString(valueint);

String nicevalue  = exzero + valuebinary;
//nicevalue is in bits

String nicervalue = nicevalue.substring(0,nicevalue.length()-2);

String  minbits = nicervalue.substring((nicervalue.length()- M) , nicervalue.length());
//minbits is the actual m in binary


arindex = Integer.parseInt(minbits,2);

}




public static void getnm(String strLine){

	

	String value = strLine.substring(0,strLine.length()-2);
	//value has the hex value

	int valueint = Integer.parseInt(value,16);

	int thefirst = Integer.parseInt(value.substring(0,1));
		
	String exzero = "";
	if(thefirst == 0)      exzero = "0000"; else if(thefirst == 1) exzero = "000"; else if(thefirst == 2) exzero = "00";
	else if(thefirst == 3) exzero = "00";   else if(thefirst == 4) exzero = "0";   else if(thefirst == 5) exzero = "0";
	else if(thefirst == 6) exzero = "0";    else if(thefirst == 7) exzero = "0";   

	String valuebinary = Integer.toBinaryString(valueint);

	String nicevalue  = exzero + valuebinary;
	//nicevalue is in bits

	String nicervalue = nicevalue.substring(0,nicevalue.length()-2);

	String  minbits = nicervalue.substring((nicervalue.length()- M) , nicervalue.length());
	//minbits is the actual m in binary

	String  nbitsofm = minbits.substring(0, N);//get the rest of the bits of the m
	//first n bits of m


	int nofm =Integer.parseInt(nbitsofm,2);

	int nlegit = Integer.parseInt(bhrtable,2);

	int xor = nofm^nlegit;

	String xorinbin = Integer.toBinaryString(xor);

	String restm = minbits.substring(N,minbits.length());

	String indexinbits = xorinbin+restm;

	arindex = Integer.parseInt(indexinbits,2);






	}

public static void updateBHRtaken(){
	bhrtable = bhrtable.substring(0, N-1);
	bhrtable = "1"+bhrtable;
}

public static void updateBHRnot(){
	bhrtable = bhrtable.substring(0, N-1);
	bhrtable = "0"+bhrtable;
}



}


