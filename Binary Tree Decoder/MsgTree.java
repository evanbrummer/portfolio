package edu.iastate.cs228.hw4;

/**
 * @author Evan Brummer
 */

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MsgTree {
	
	private static boolean WRITE_STATUS = false;
	
	public char payloadChar;
	public MsgTree left;
	public MsgTree right;
	
	private static int staticCharIdx = 0;
	private static int numPayloads = 0;
	private static double totalBits = 0;
	private static int totalChars = 0;
	
	private static ArrayList<Character> c;
	private static ArrayList<String> bin;
	
	private static DecimalFormat df;
	
	
	// For building a tree from a string
	public MsgTree(String encodingString) {
		
		while (staticCharIdx < encodingString.length()) {
			
			if (this.left != null && this.right != null) {
				status("LEFT and RIGHT are occupied, returning...");
				return;
			}
			
			if (MsgTree.staticCharIdx == 0) {
				if (encodingString.charAt(MsgTree.staticCharIdx) == '^') {
					MsgTree.staticCharIdx++;
					status("(Ignoring) first branch");
				} 
				else if (encodingString.charAt(MsgTree.staticCharIdx) != '^') {
					this.payloadChar = encodingString.charAt(staticCharIdx);
					this.left = null;
					this.right = null;
					
					System.out.println("WARNING: Leaf is a '^'!");
					
					return;
				}
			}

			else if (encodingString.charAt(MsgTree.staticCharIdx) == '^') {

				MsgTree.staticCharIdx++;

				if (this.left == null) {
					status("New LEFT branch '" +
							encodingString.charAt(MsgTree.staticCharIdx - 1) +
							"'");
					this.left = new MsgTree(encodingString);
				} else if (this.right == null) {
					status("New RIGHT branch '" +
							encodingString.charAt(MsgTree.staticCharIdx - 1) +
							"'");
					this.right = new MsgTree(encodingString);
				}

			}

			else if (encodingString.charAt(MsgTree.staticCharIdx) != '^') {
				if (this.left == null) {
					this.left = new MsgTree(encodingString.charAt(MsgTree.staticCharIdx));
					status("New LEFT payload '" +
							encodingString.charAt(MsgTree.staticCharIdx) +
							"'");
					
					MsgTree.numPayloads++;
				} else if (this.right == null) {
					this.right = new MsgTree(encodingString.charAt(MsgTree.staticCharIdx));
					status("New RIGHT payload '" +
							encodingString.charAt(MsgTree.staticCharIdx) +
							"'");
					
					MsgTree.numPayloads++;
				}

				MsgTree.staticCharIdx++;
			} 
			

		}
		
		if (MsgTree.staticCharIdx == encodingString.length()) {
			status("Construction complete; "
					+ "reached end of string, "
					+ "finalizing constructors...");
		}
	}
	
	// For a node with no children
	// (the leaves are the only one that get to hold data [i.e. the payloadChar])
	public MsgTree(char payloadChar) {
		this.payloadChar = payloadChar;
		this.left = null;
		this.right = null;
	}
	
	// prints all the tree's characters and their bit codes
	public static void printCodes(MsgTree root, String code) {
		MsgTree curr;
		String tempCode;
		
		int idx = 0;
		
		c = new ArrayList<Character>();
		bin = new ArrayList<String>();
		
		int tempIter = 0;
		
		while (idx < code.length() && c.size() != numPayloads) { 
									 /* ^^^ saves us a few iterations without having to re-write everything */
			curr = root;
			tempCode = "";
			
			while (curr.left != null && curr.right != null) { // while there is no payloadChar in curr...
				if (code.charAt(idx) == '0') {
					curr = curr.left;
					tempCode += "0";
				}
				else if (code.charAt(idx) == '1') {
					curr = curr.right;
					tempCode += "1";
				}
				
				idx++;
			}
			
			if (!c.contains(curr.payloadChar)) { // if c<> does NOT contain this payloadChar, add it
				c.add(curr.payloadChar);
				bin.add(tempCode);
			}
			
			tempIter++;
		}
		
		status("ITERATIONS: " + tempIter + "\n");
		
		for (int i = 0; i < c.size(); i++) {
			if (c.get(i) == '\n') {
				System.out.println("   \\n\t\t" + bin.get(i));
			} else {
				System.out.println("    " + c.get(i) + "\t\t" + bin.get(i));
			}
			
			MsgTree.totalBits += bin.get(i).length();
		}
		
	}
	
	// prints the real (decoded) message to console
	public void decode(MsgTree codes, String msg) { // given ROOT and 10101001 basically
		MsgTree curr = codes;
		
		for (int i = 0; i < msg.length(); i++) { // for every char in binMsg
			if (msg.charAt(i) == '0') { 					// if 0, go left
				if (curr.left != null) {
					curr = curr.left;
				} else {
					System.out.println("\nERROR: binMsg directions lead to null path!");
				}
			}
			else if (msg.charAt(i) == '1') { 				// if 1, go right
				if (curr.right != null) {
					curr = curr.right;
				} else {
					System.out.println("\nERROR: binMsg directions lead to null path!");
				}
			}
			
			if (curr.left == null && curr.right == null 
					&& Character.isDefined(curr.payloadChar) ) { 	// if curr has no options left, print payloadChar
				System.out.print(curr.payloadChar);
				
				totalChars++;
				
				curr = codes;									// and set curr back to beginning for next i
			}
		}
	}
	
	// prints the STATISTICS for 5% extra credit
	public static void printStats() {
		MsgTree.df = new DecimalFormat("#.#");
		
		System.out.println("\n\nSTATISTICS:");
		
		System.out.print("Avg bits/char:\t\t");
			System.out.println( df.format(MsgTree.totalBits / c.size()) );
			
			
			
		System.out.print("Total characters:\t");
			System.out.println(MsgTree.totalChars);
			
		System.out.print("Space savings:\t\t");
			System.out.println( 
					df.format((1 - ((MsgTree.totalBits / c.size()) / 16.0)) * 100) 
							    + "%");
	}
	
	private static void status(String s) {
		if (WRITE_STATUS) {
			System.out.println("\t*** " + s);
		}
	}
	
}
