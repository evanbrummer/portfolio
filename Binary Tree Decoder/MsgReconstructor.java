package edu.iastate.cs228.hw4;

/**
 * @author Evan Brummer
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MsgReconstructor {
	
	private static boolean WRITE_STATUS = false;

	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner sc = new Scanner(System.in);
		String fileName;
		
		System.out.print("Please enter filename to decode: ");
		fileName = sc.next();
		
		File f = new File(fileName);
		
		if (!f.isFile()) { // FILE CHECK
			System.out.println("ERROR: Not a file! Exiting...");
			System.exit(-1);
		}
		
		Scanner fsc = new Scanner(f);
		String treeString = ""; 
		String nextLine = null;
		String binMsg = null;
		
		// FLAG TO MAKE SURE WE DON'T ADD THE BINARY TO THE TREESTRING
		boolean binFlag = false; 
		
		// FLAG TO MAKE SURE WE DON'T ADD A NEW LINE AT THE BEGINNING
		boolean nlFlag = false;
		
		while (binFlag == false && fsc.hasNextLine()) {
			nextLine = fsc.nextLine();
			
			binFlag = true;
			for (int i = 0; i < nextLine.length(); i++) { // CHECK EVERY CHAR IN THE LINE TO SEE IF BINARY OR NOT
				if (nextLine.charAt(i) != '0') {
					if (nextLine.charAt(i) != '1') {
						binFlag = false;
						break;
					}
				}
			}
			
			if (binFlag == false) {
				if (nlFlag == true) {
					treeString += "\n";
				}
				
				nlFlag = true;
				treeString += nextLine;
			} else {
				binMsg = nextLine;
			}
		}
		
		if (binMsg == null) {
			System.out.println("ERROR: binMsg is null after full loop!");
		}
		
												status("treeString scanned as '" + treeString + "'");
		
		MsgTree treeRoot = new MsgTree(treeString);
		
		System.out.println("\ncharacter \tcode");
		System.out.println("----------------------");
		MsgTree.printCodes(treeRoot, binMsg);
		
		System.out.println("\nMESSAGE: ");
		
		treeRoot.decode(treeRoot, binMsg);									
												
		MsgTree.printStats();
		
		fsc.close();
		sc.close();
		
	}

	public static void status(String s) {
		if (WRITE_STATUS) {
			System.out.println("\t*** " + s);
		}
	}
	
}
