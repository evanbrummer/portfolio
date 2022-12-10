import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class prog2 {

	public static void main(String[] args) {
		String filename;
		Scanner run_sc = new Scanner(System.in);
		filename = run_sc.nextLine();
		System.out.println("Filename: " + filename);
		run_sc.close();

		ArrayList<String> result = new ArrayList<String>();

		try (FileInputStream fin = new FileInputStream(filename)) {
			DataInputStream din = new DataInputStream(fin);
			int next32bits;

			while (true) {
				next32bits = din.readInt();
				result.add(makeInstruction(next32bits));
			}
			

		} catch (IOException e) {
			result.add("");
			System.out.print("Error: Likely EOF (");
			System.out.println(e.toString() + ")");
		}

		String currLine;
		int dist;
		int labelNum = 0;
		for (int i = 0; i < result.size(); i++) {
			currLine = result.get(i);

			// B, BL, B., CBZ, CBNZ
			if (currLine.contains("B ") || currLine.contains("BL ") || 
					currLine.contains("B.") || currLine.contains("CBZ ") ||
					currLine.contains("CBNZ ")) {
				
				dist = getBranchDistance(currLine);

				if (!result.get(i + dist).contains("label")) {
					result.set((i + dist), 
						("label_" + labelNum + ":\n" + result.get(i + dist)));
						
					result.set(i, 
						(result.get(i).split(String.valueOf(dist))[0] + 
							"label_" + labelNum));
						labelNum++;
				} else {
					result.set(i, 
						(result.get(i).split(String.valueOf(dist))[0] + 
							result.get(i + dist).split(":")[0]));
				}
			}

		}

		for (int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i));
		}
		

	}

	public static String makeInstruction(int bin) {

		for (int i = 0; i < Instructions.getLength(); i++) {
			if ((bin & opcodeSizer(i)) == Instructions.getOpcode(i)) {

				String t = Instructions.getType(i);
				switch (t) {
					case "R":
					return Parsers.parse_R(Instructions.getName(i), bin);
					
					case "I":
					return Parsers.parse_I(Instructions.getName(i), bin);
					
					case "D":
					return Parsers.parse_D(Instructions.getName(i), bin);
					
					case "B":
					return Parsers.parse_B(Instructions.getName(i), bin);
					
					case "CB":
					return Parsers.parse_CB(Instructions.getName(i), bin);
		
					default:
					return ("Error: Instruction type " + t + " not found");
				}
			}
		}
		
		return ("Error: Instruction " + bin + " not found");
	}

	public static int opcodeSizer(int n) {
		String type = Instructions.getType(n);

		switch (type) {
			case "R":
			return (0b11111111111 << 21);

			case "I":
			return (0b1111111111 << 22);
			
			case "D":
			return (0b11111111111 << 21);
			
			case "B":
			return (0b111111 << 26);
			
			case "CB":
			return (0b11111111 << 24);

			default:
			return -1;
		}
	}

	public static int getBranchDistance(String inst) {
		// B, BL, B., CBZ, CBNZ

		String[] splitInst = inst.split(" ");
		return Integer.parseInt(splitInst[splitInst.length - 1]);
	}

}

class Parsers {
	public static String parse_R(String name, int bin) {
		String result = name;
		if (name == "HALT" || name == "PRNL") {
			return result;
		} 
		else if (name == "PRNT") {
			int Rd = bin & 0b11111;
			result += " " + formatRegister(Rd);
		}
		else if (name == "LSL" || name == "LSR") {
			int Rd = bin & 0b11111;
			result += " " + formatRegister(Rd) + ",";

			int Rn = (bin & 0b1111100000) >> 5;
			result += " " + formatRegister(Rn) + ",";
			
			int shamt = (bin & (0b111111 << 10)) >> 10;
			result += " #" + shamt;
		} 
		else if (name == "BR") {
			int Rn = (bin & 0b1111100000) >> 5;
			result += " " + formatRegister(Rn);
		} 
		else {
			int Rd = bin & 0b11111;
			result += " " + formatRegister(Rd) + ",";

			int Rn = (bin & 0b1111100000) >> 5;
			result += " " + formatRegister(Rn) + ",";

			int Rm = (bin & (0b11111 << 16)) >> 16;
			result += " " + formatRegister(Rm);
		}
		return result;
	}

	public static String parse_I(String name, int bin) {
		String result = name;

		int Rd = bin & 0b11111;
		result += " " + formatRegister(Rd) + ",";

		int Rn = (bin & 0b1111100000) >> 5;
		result += " " + formatRegister(Rn) + ",";

		int ALUImm = (bin & (0b111111111111 << 10)) >> 12;
		result += " #" + ALUImm;

		return result;
	}
	public static String parse_D(String name, int bin) {
		String result = name;
		
		int Rt = bin & 0b11111;
		result += " " + formatRegister(Rt) + ",";

		int Rn = (bin & 0b1111100000) >> 5;
		result += " [" + formatRegister(Rn) + ",";

		int DT_ad = (bin & 0b111111111 << 12) >> 12;
		result += " #" + DT_ad + "]";

		return result;
	}
	public static String parse_B(String name, int bin) {
		String result = name;

		int BR_addr = (bin & 0b11111111111111111111111111);
		if ((BR_addr & (0b1 << 25)) != 0) {
			BR_addr = BR_addr | (0b111111 << 26);
		}
		
		result += " " + BR_addr;
		
		return result;
	}
	public static String parse_CB(String name, int bin) {
		String result = name;

		if (name == "B.") {
			int Rt = (bin & 0b11111);
			result += Instructions.getSuffix(Rt);
			
		} else {
			int Rt = (bin & 0b11111);
			result += " " + formatRegister(Rt) + ",";
		}
			int COND_BR = (bin & 0b111111111111111111100000) >> 5;
			if((COND_BR & (0b1 << 18)) != 0) {
				COND_BR = COND_BR | (0b1111111111111 << 19);
			}
			result += " " + COND_BR;
			return result;
		
	}

	public static String formatRegister(int n) {
		if (n > 27) {
			switch (n) {
				case 28:
				return "SP";
				case 29:
				return "FP";
				case 30:
				return "LR";
				case 31:
				return "XZR";
				default:
				return "Error: Register number out of bounds";
			}
		} else {
			return "X" + n;
		}
	}
	
}

class Instructions {

	private static String[] name = {
		"ADD",
		"ADDI",
		"AND",
		"ANDI",
		"B.",
		"B",
		"BL",
		"BR",
		"CBNZ",
		"CBZ",
		"DUMP",
		"EOR",
		"EORI",
		"HALT",
		"LDUR",
		"LSL",
		"LSR",
		"MUL",
		"ORR",
		"ORRI",
		"PRNL",
		"PRNT",
		"STUR",
		"SUB",
		"SUBI",
		"SUBIS",
		"SUBS"
	};

	private static String[] type = {
		"R",
		"I",
        "R",
        "I",
        "CB",
        "B",
        "B",
        "R",
        "CB",
        "CB",
        "R",
        "R",
        "I",
        "R",
        "D",
        "R",
        "R",
        "R",
        "R",
        "I",
        "R",
        "R",
        "D",
        "R",
        "I",
        "I",
        "R"
	};

	private static int[] opcode = {
		(0b10001011000 << 21),
        (0b1001000100 << 22),  
        (0b10001010000 << 21), 
        (0b1001001000 << 22),  
        (0b01010100  << 24),
        (0b000101  << 26), 
        (0b100101 << 26), 
        (0b11010110000 << 21), 
        (0b10110101 << 24), 
        (0b10110100 << 24), 
        (0b11111111110 << 21), 
        (0b11001010000 << 21), 
        (0b1101001000  << 22), 
        (0b11111111111 << 21), 
        (0b11111000010 << 21), 
        (0b11010011011 << 21), 
        (0b11010011010 << 21),  
        (0b10011011000 << 21), 
        (0b10101010000 << 21), 
        (0b1011001000  << 22), 
        (0b11111111100 << 21), 
        (0b11111111101 << 21), 
        (0b11111000000 << 21), 
        (0b11001011000 << 21), 
        (0b1101000100  << 22), 
        (0b1111000100  << 22), 
        (0b11101011000 << 21)
	};

	private static String[] suffix = {
		"EQ",
		"NE",
		"HS",
		"LO",
		"MI",
		"PL",
		"VS",
		"VC",
		"HI",
		"LS",
		"GE",
		"LT",
		"GT",
		"LE"
	};

	public static String getName(int n) {
		return name[n];
	}

	public static String getType(int n) {
		return type[n];
	}

	public static int getOpcode(int n) {
		return opcode[n];
	}

	public static String getSuffix(int n){
		return suffix[n];
	}

	private static int NUM_INST = 27; // (name.length + type.length + opcode.length) / 3;
	public static int getLength() {
		return NUM_INST;
	}

}