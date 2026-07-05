import java.io.*;
import java.util.*;

public class Assembler {

    // Lookup table for comp mnemonics
    static HashMap<String, String> compMap = new HashMap<>();

    // Lookup table for dest mnemonics
    static HashMap<String, String> destMap = new HashMap<>();

    // Lookup table for jump mnemonics
    static HashMap<String, String> jumpMap = new HashMap<>();
    
    // RAM
    static HashMap<String, Integer> symbolTable = new HashMap<>();

    public static void main(String[] args) throws IOException {

        initializeTables();
        initializeSymbolTable();

        BufferedReader br1 = new BufferedReader(new FileReader("Add.asm"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("Add.hack"));

        firstPass(br1);   // for assigning labels
        br1.close();

        BufferedReader br2 = new BufferedReader(new FileReader("Add.asm"));

        String line;
        int nextRamAddress = 16;


        while ((line = br2.readLine()) != null) {

            // Remove comments
            if (line.contains("//")) {
                line = line.substring(0, line.indexOf("//"));
            }

            // Trim
            line = line.trim();

            // Skip empty lines
            if (line.isEmpty())
                continue;
            
            if(line.startsWith("(")) continue;  // if label is encountered

            // A-instruction
            if (line.startsWith("@")) {
                String symbol = line.substring(1);
                int value = -1;
                try {
                    // i) Normal A-instr. (Eg: @2)
                    value = Integer.parseInt(symbol);

                } catch(NumberFormatException e) {
                    // ii) Symbolic A-instr.  (Eg : @R1)
                    if(symbolTable.containsKey(symbol)) {
                        value = symbolTable.get(symbol);
                    } else {
                        // new variable
                        symbolTable.put(symbol, nextRamAddress);
                        value = nextRamAddress;
                        nextRamAddress++;
                    }
                }

                String binary = Integer.toBinaryString(value);

                while (binary.length() < 16)
                    binary = "0" + binary;

                bw.write(binary);
                bw.newLine();
                
            }
            // C-instruction
            else  {

                String dest = "";
                String comp = "";
                String jump = "";

                if (line.contains("=")) {
                    String[] parts = line.split("=");
                    dest = parts[0];
                    line = parts[1];
                }

                if (line.contains(";")) {
                    String[] parts = line.split(";");
                    comp = parts[0];
                    jump = parts[1];
                } else {
                    comp = line;
                }

                String machineCode = "111"
                        + compMap.get(comp)
                        + destMap.get(dest)
                        + jumpMap.get(jump);

                bw.write(machineCode);
                bw.newLine();
            }
        }

        br2.close();
        bw.close();

        System.out.println("Assembly completed.");
    }

    static void initializeTables() {

        // dest
        destMap.put("", "000");
        destMap.put("M", "001");
        destMap.put("D", "010");
        destMap.put("MD", "011");
        destMap.put("A", "100");
        destMap.put("AM", "101");
        destMap.put("AD", "110");
        destMap.put("AMD", "111");

        // jump
        jumpMap.put("", "000");
        jumpMap.put("JGT", "001");
        jumpMap.put("JEQ", "010");
        jumpMap.put("JGE", "011");
        jumpMap.put("JLT", "100");
        jumpMap.put("JNE", "101");
        jumpMap.put("JLE", "110");
        jumpMap.put("JMP", "111");

        // comp
        compMap.put("0", "0101010");
        compMap.put("1", "0111111");
        compMap.put("-1", "0111010");
        compMap.put("D", "0001100");
        compMap.put("A", "0110000");
        compMap.put("!D", "0001101");
        compMap.put("!A", "0110001");
        compMap.put("-D", "0001111");
        compMap.put("-A", "0110011");
        compMap.put("D+1", "0011111");
        compMap.put("A+1", "0110111");
        compMap.put("D-1", "0001110");
        compMap.put("A-1", "0110010");
        compMap.put("D+A", "0000010");
        compMap.put("D-A", "0010011");
        compMap.put("A-D", "0000111");
        compMap.put("D&A", "0000000");
        compMap.put("D|A", "0010101");

        compMap.put("M", "1110000");
        compMap.put("!M", "1110001");
        compMap.put("-M", "1110011");
        compMap.put("M+1", "1110111");
        compMap.put("M-1", "1110010");
        compMap.put("D+M", "1000010");
        compMap.put("D-M", "1010011");
        compMap.put("M-D", "1000111");
        compMap.put("D&M", "1000000");
        compMap.put("D|M", "1010101");
    }

    static void initializeSymbolTable() {
        symbolTable.put("R0", 0);
        symbolTable.put("R1", 1);
        symbolTable.put("R2", 2);
        symbolTable.put("R3", 3);
        symbolTable.put("R4", 4);
        symbolTable.put("R5", 5);
        symbolTable.put("R6", 6);
        symbolTable.put("R7", 7);
        symbolTable.put("R8", 8);
        symbolTable.put("R9", 9);
        symbolTable.put("R10", 10);
        symbolTable.put("R11", 11);
        symbolTable.put("R12", 12);
        symbolTable.put("R13", 13);
        symbolTable.put("R14", 14);
        symbolTable.put("R15", 15);

        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);

        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);
    }


    static void firstPass(BufferedReader br) throws IOException {
        String line;
        int romAddress = 0;

        while((line = br.readLine()) != null) {
            // Remove comments
            if (line.contains("//")) {
                line = line.substring(0, line.indexOf("//"));
            }

            // Trim
            line = line.trim();

            // Skip empty lines
            if (line.isEmpty())
                continue;

            if(line.startsWith("(")) {
                String label = line.substring(1, line.indexOf(")"));
                symbolTable.put(label, romAddress);
                continue;
            }
            romAddress++;
        }
    }
}
