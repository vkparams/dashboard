package com.evault.utils;

import java.net.URL;
import java.util.ArrayList;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ActionOnDuplicate;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.constants.TestImportance;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;

public class Sample {

//	public static void main(String args[]) {
//		TestLinkUtils tcUtils = new TestLinkUtils();
//		tcUtils.createTestCases();
//
//	}

	
	    private static void permutation(String prefix, String str){
	        int n = str.length();
        	System.out.println("str.length= "+str.length()+" str= "+str);

	       // if (n == 0) 
	            System.out.println(prefix);
	      //  else {
	            for (int i = 0; i < n; i++){
	            	System.out.println("prefix= "+prefix+" str.charAt(i)= "+str.charAt(i));
	            	System.out.println("str.substring(0, i)"+str.substring(0, i)+" str.substring(i+1)= "+str.substring(i+1));

	                permutation(prefix + str.charAt(i), 
	            str.substring(0, i) + str.substring(i+1));
	            }
	        }
	  //  }
	    public static void main(String[] args) {
	        permutation("", "ABCD");
	    }
	
}
