import java.util.*;
import java.lang.Math;

public class Main {

    public static void main(String[] args) {
    	
		System.out.println("Simplifying Minterms using Tabular Method");
		System.out.println("by: Cedric Caquilala & Nichol John Famadico");
		System.out.println();
    	// Handling user input
    	ArrayList<String> literalsAL = new ArrayList<String>();
    	String userInput = "0";
    	
    	System.out.println("Input literals to be used.");
    	System.out.println("Input '-1' or '' to end.");
    	
		Scanner sc;

    	do {
    		sc = new Scanner(System.in);
    		userInput = sc.nextLine();
    		if(userInput.compareTo("") == 0 || userInput.compareTo("-1") == 0) {
    			break;
    		} else if(!Character.isLetter(userInput.charAt(0)) || userInput.length() > 1) {
    			System.out.println("Error. Please input an alphabetic character.");
    		} else if(literalsAL.contains(userInput)) {    			
    			System.out.println("Error. Char is already in use.");
    		} else {
    			literalsAL.add(userInput); 			    			
    		}
    	}while(userInput.compareTo("-1") != 0);
    	
    	
    	// Transfers all contents of the literals ArrayList
    	// to the literals array
    	
    	int variables = literalsAL.size();
    	char[] literals = new char[variables];
		Collections.sort(literalsAL);
    	for(int i = 0; i < literalsAL.size(); i++){
    		literals[i] = literalsAL.get(i).charAt(0);
    	}
    	
    	
    	System.out.println("You input:");
    	for(int i = 0; i < literals.length; i++){
    		System.out.print(literals[i] + " ");
    	}
    	System.out.println();
    	System.out.println();
    	
    	ArrayList<Integer> inputAL = new ArrayList<Integer>();
    	
    	System.out.println("Input minterms to be used.");
    	System.out.println("Input '-1' or '' to end.");
    	
    	do {
    		sc = new Scanner(System.in);
    		userInput = sc.nextLine();
    		if(userInput.compareTo("") == 0) {
    			break;
    		}

    		try {
    			int intUserInput = Integer.parseInt(userInput);				
    			if(intUserInput == -1) {
    				break;
    			} else if(intUserInput >= Math.pow(2, variables) || intUserInput < 0) {
    				System.out.println("Error. Minterm is out of bounds.");
    			} else if (inputAL.contains(intUserInput)) {    				
    				System.out.println("Error. Minterm is already in use.");
    			} else {    				
    				inputAL.add(intUserInput); 			    			
    			}
			} catch (NumberFormatException e) {
				System.out.println("Error. Please input an integer.");
			}

    	}while(userInput.compareTo("-1") != 0);
    	
    	sc.close();

    	// Transfers all contents of the literals ArrayList
    	// to the literals array
    	int[] input = new int[inputAL.size()];
    	Collections.sort(inputAL);
    	for(int i = 0; i < inputAL.size(); i++){
    		input[i] = inputAL.get(i);
    	}
    	
    	
    	System.out.println("You input:");
    	for(int i = 0; i < input.length; i++){
    		System.out.print(input[i] + " ");
    	}
    	System.out.println();
    	System.out.println();
    	
    	System.out.println("Starting calculation...");

		String canonical = "F(";

		for (int i = 0; i < literals.length; i++) {
			canonical += literals[i];
			if(i < literals.length - 1) {
				canonical += ", ";
			}
		}

		canonical += ") = m(";

		for (int i = 0; i < input.length; i++) {
			canonical += input[i];
			if(i < input.length - 1) {
				canonical += ", ";
			}
		}
		
		canonical += ")";

		System.out.println(canonical);

        LinkedList<Minterm> initialTable = new LinkedList<Minterm>();
        for(int i = 0; i < input.length; i++) {
            int n = input[i];
            String binary = convert(n, variables);
            initialTable.add(new Minterm(n, binary));
        }

        LinkedList<Minterm> primeImplicants = new LinkedList<Minterm>(); //stores the prime implicants

        System.out.println();
        System.out.println();
   
        // converts the obtained array into an ArrayList to remove empty groups
        ArrayList<LinkedList<Minterm>> groups = group(initialTable, variables);
 
        
        System.out.println();
        
        checkBits(groups, primeImplicants);

        System.out.println();
        System.out.println();
        System.out.println("--------------------------------");
        System.out.println("Prime Implicants:");
        for(int i = 0; i < primeImplicants.size(); i++) {
        	System.out.println(primeImplicants.get(i));        	
        }
        System.out.println("--------------------------------");
		System.out.println();
		System.out.println();

		LinkedList<Minterm> finalAnswer = checkBitsAgain(primeImplicants, input);
		System.out.println();
		System.out.println();
		System.out.println("Final Answer: ");
		
		for(int i = 0; i < finalAnswer.size(); i++) {
			System.out.println(finalAnswer.get(i));
		}

		System.out.println();
		System.out.println();
		System.out.println("Canonical Form:");
		System.out.println(canonical);
		System.out.println();
		System.out.println();
		System.out.println("In Standard Form: ");
		System.out.println(toStandard(finalAnswer, literals));

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Simplifying Minterms using Tabular Method");
		System.out.println("by: Cedric Caquilala & Nichol John Famadico");
		System.out.println();

    }

	public static String toStandard(LinkedList<Minterm> finalAnswer, char[] literals) { // converts the prime implicants to standard form
		String standardForm = "F(";
		
		for (int i = 0; i < literals.length; i++) {
			standardForm += literals[i];
			if (i < literals.length - 1) {
				standardForm += ", ";
			}
		}

		standardForm += ") = ";
		
		for (int i = 0; i < finalAnswer.size(); i++) {
			String bits = finalAnswer.get(i).getBits();
			for (int j = 0; j < bits.length(); j++) {
				if(bits.charAt(j) == '1') {
					standardForm += literals[j];
				}
				if(bits.charAt(j) == '0') {
					standardForm += literals[j] + "'";
				}
			}
			if(i < finalAnswer.size() - 1) {
				standardForm += " + ";
			}
		}

		return standardForm;
	}

	public static LinkedList<Minterm> checkBitsAgain(LinkedList<Minterm> primeImplicants, int[] input) { //part 2 table

		LinkedList<Integer> inputs = new LinkedList<Integer>();

		for(int i = 0; i < input.length; i++) {
			inputs.add(input[i]);
		}

		Collections.sort(inputs);

		char[][] marks = new char[primeImplicants.size()][inputs.size()];
		int[] counter = new int[inputs.size()];
		char[] markers = new char[inputs.size()];

		for(int i = 0; i < counter.length; i++) {
			counter[i] = 0 ;
		}

		for(int i = 0; i < marks.length; i++) { //creates table 2
			ArrayList<Integer> minterms = (ArrayList<Integer>) primeImplicants.get(i).getName();
			for (int j = 0; j < marks[i].length; j++) {
				if(minterms.contains(inputs.get(j))) {
					marks[i][j] = 'x';
					counter[j]++;
				}
				else {
					marks[i][j] = ' ';
				}
			}
		}

		for(int i = 0; i < inputs.size(); i++) { //marks columns
			if(counter[i] == 1) {
				markers[i] = '/';
				for (int j = 0; j < primeImplicants.size(); j++) {
					ArrayList<Integer> minterms = (ArrayList<Integer>) primeImplicants.get(j).getName();
					if(minterms.contains(inputs.get(i))) {
						primeImplicants.get(j).setMark(true);
						for(int k = 0; k < minterms.size(); k++) {
							markers[inputs.indexOf(minterms.get(k))] = '/';
						}
					}
				}
			}
		}

		System.out.println();

		LinkedList<Minterm> remainingImplicants = new LinkedList<Minterm>();
		for(int i = 0; i < primeImplicants.size(); i++) {
			if(primeImplicants.get(i).getMark() == false) {
				remainingImplicants.add(primeImplicants.get(i));
			}
		}

		ArrayList<Integer> remainingInputs = new ArrayList<Integer>();

		for(int j = 0; j < inputs.size(); j++) {
			if(markers[j] != '/') {
				remainingInputs.add(inputs.get(j));
			}
		}

		while(remainingInputs.size() > 0) {
				
			Minterm winner = remainingImplicants.get(0);
			int highscore = 0;

			for(int i = 0; i < remainingImplicants.size(); i++) {
				int score = 0;
				ArrayList <Integer> minterms = (ArrayList<Integer>) remainingImplicants.get(i).getName();
				for(int j = 0; j < remainingInputs.size(); j++) {
					if(minterms.contains(remainingInputs.get(j))) {
						score++;
					}
				}
				if(score > highscore) {
					winner = remainingImplicants.get(i);
					highscore = score;
				}
			}

			winner.setMark(true);

			ArrayList<Integer> m = (ArrayList<Integer>) winner.getName();

			for(int i = 0; i < m.size(); i++) {
				markers[inputs.indexOf(m.get(i))] = '/';
			}

			remainingImplicants = new LinkedList<Minterm>();
			for(int i = 0; i < primeImplicants.size(); i++) {
				if(primeImplicants.get(i).getMark() == false) {
					remainingImplicants.add(primeImplicants.get(i));
				}
			}

			remainingInputs = new ArrayList<Integer>();
			for(int j = 0; j < inputs.size(); j++) {
				if(markers[j] != '/') {
					remainingInputs.add(inputs.get(j));
				}
			}
		}

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Table 2");
		for(int i = 0; i < inputs.size(); i++) {
			System.out.print(inputs.get(i) + "\t");
		}

		System.out.println();

		for(int i = 0; i < marks.length; i++) {
			for (int j = 0; j < marks[i].length; j++) {
				System.out.print(marks[i][j] + "\t");
				if(j + 1  == marks[i].length) {
					System.out.print(primeImplicants.get(i).getName());
				}
			}
			System.out.println();
		}

		System.out.println();

		for(int i = 0; i < markers.length; i++) {
			System.out.print(markers[i] + "\t");
		}

		System.out.println();

		LinkedList<Minterm> finalAnswer = new LinkedList<Minterm>();

		for(int i = 0; i < primeImplicants.size(); i++) {
			if(primeImplicants.get(i).getMark() == true) {
				finalAnswer.add(primeImplicants.get(i));
			}
		}

		return finalAnswer;

	}
    
    public static void checkBits(ArrayList<LinkedList<Minterm>> groups, LinkedList<Minterm> prime){ //part 1 table
    	// a = first division
    	// b = second division
    	// ax = current of division a
    	// bx = current of division b
    	// l = bit of ax and bx being compared
    	// comp = new bits after comparison
    	
    	if(groups.size() == 0) {
    		return;
    	}
    	
    	ArrayList<LinkedList<Minterm>> newGroups = new ArrayList<LinkedList<Minterm>>();
    	
    	for(int i = 0; i < groups.size() - 1; i++) {
    		LinkedList<Minterm> a = groups.get(i);
    		LinkedList<Minterm> b = groups.get(i+1);
    		
    		newGroups.add(new LinkedList<>());
    		
			for(int j = 0; j < a.size(); j++) {
				for(int k = 0; k < b.size(); k++) {
					// Compare binaries if they are matched
					Minterm ax = a.get(j);
					Minterm bx = b.get(k);
					String comp = "";
					
					int mismatch = 0;
					for(int l = 0; l < ax.getBits().length(); l++) {
						if(ax.getBits().charAt(l) != bx.getBits().charAt(l)) {
							comp += "-";
							mismatch++;
						} else {
							comp += ax.getBits().charAt(l);
						}
					}
					
					// If there are less than two differences
					if(mismatch < 2) {
						
						ax.setMark(true);
						bx.setMark(true);
						
						ArrayList<Integer> newName = new ArrayList<Integer>();
						for(int l = 0; l < ax.getName().size(); l++) {
							newName.add(ax.getName().get(l));
							newName.add(bx.getName().get(l));
						}
						Collections.sort(newName);
						newGroups.get(i).add(new Minterm(newName, comp));
					}
				}
			}
    	}
    	
    	// displays the current groupings and whether they are marked
    	System.out.println();
    	System.out.println("Grouping:");
    	for(int i = 0; i < groups.size(); i++) {
    		if(!groups.get(i).isEmpty()) {
    			System.out.println("-----------------");
    		}
    		for(int j = 0; j < groups.get(i).size(); j++) {
    			System.out.println(groups.get(i).get(j));
    		}
    	}
    	System.out.println("-----------------");
    	
    	// adds all unmarked minterms from the old group to the prime list
    	for(int i = 0; i < groups.size(); i++) {
    		for(int j = 0; j < groups.get(i).size(); j++) {
    			Minterm curr = groups.get(i).get(j);
    			if(curr.getMark() == false) {
    				prime.add(curr);
    			}
    		}
    	}
    	
    	// removes empty groups in the new merged grouping
    	for(int i = 0; i < newGroups.size(); i++) {
    		if(newGroups.get(i).isEmpty()) {
    			newGroups.remove(i--);
    		}
    	}
    	
    	// removes duplicates in the new merged grouping
    	newGroups = removeDupe(newGroups);
    	
    	System.out.println();
    	System.out.println();
    	checkBits(newGroups, prime);
    	// loops recursively until there are no matches left
    	
    	
    }
    
    public static ArrayList<LinkedList<Minterm>> removeDupe(ArrayList<LinkedList<Minterm>> groups){
    	ArrayList<LinkedList<Minterm>> noDupe = new ArrayList<LinkedList<Minterm>>();
    	for(int i = 0; i < groups.size(); i++) {
    		noDupe.add(new LinkedList<Minterm>());
    		LinkedList<Minterm> currND = noDupe.get(i);
    		LinkedList<Minterm> currGP = groups.get(i);
    		for(int j = 0; j < currGP.size(); j++) {
    			Minterm currGPMin = currGP.get(j);
    			boolean hasDupe = false;
    			for(int k = 0; k < currND.size(); k++) {
    				if(currGPMin.getBits().equals(currND.get(k).getBits())) {
    					hasDupe = true;
    				}
    			}
    			if(hasDupe == false) {
    				currND.add(currGP.get(j));    				
    			}
    		}
    	}

    	return noDupe;
    }

    public static ArrayList<LinkedList<Minterm>> group(LinkedList<Minterm> input, int variables) { //groups minterms according to their number of 1 bits
        ArrayList<LinkedList<Minterm>> group = new ArrayList<LinkedList<Minterm>>();
        for(int i = 0; i < variables + 1; i++) {
            group.add(i, new LinkedList<Minterm>());
        }

        for(int i = 0; i < input.size(); i++) {
            Minterm pointer = input.get(i);
            group.get(pointer.countOnes()).add(pointer);
        }

        return group;
    }

    static String convert(int n, int size) { //converts a decimal to a binary
        String binary = "";
        char[] bits = new char[size];
        int counter = bits.length - 1;
        while(n != 0) {
            if(n % 2 == 1) {
                bits[counter] = '1';
            }
            else {
                bits[counter] = '0';
            }
            n = n / 2;
            counter--;
        }

        while (counter >= 0) {
            bits[counter] = '0';
            counter--;
        }

        for(int i = 0; i < bits.length; i++) {
            binary += bits[i];
        }
        return binary;
    }

	static class Minterm {
	//    private LinkedList<Integer> name; //decimal name
		private ArrayList<Integer> name; //decimal name
		private String bits; //equivalent binary form
		private boolean mark; //kung na-checkan na ba or hindi pa
	
		public Minterm (int name, String bits) {
	//        this.name = new LinkedList<Integer>();
			this.name = new ArrayList<Integer>();
			this.name.add(name);
			this.bits = bits;
			this.mark = false;
		}
	
		public Minterm (ArrayList<Integer> name, String bits) {
			this.name = name;
			this.bits = bits;
			this.mark = false;
		}
	
		public List<Integer> getName() {
			return this.name;
		}
	
		public String printName() {
			String name = "( ";
	
			for (int i = 0; i < this.name.size(); i++) {
				name += this.name.get(i) + " ";
			}
	
			return name + " )";
		}
	
		public String getBits() {
			return this.bits;
		}
	
		public void setMark (boolean mark) {
			this.mark = mark;
		}
	
		public int countOnes() {
			int counter = 0;
			
			for(int i = 0; i < bits.length(); i++) {
				if (bits.charAt(i) == '1') {
					counter++;
				}
			}
			return counter;
		}
	
		public boolean getMark() {
			return this.mark;
		}
	
		public String toString() {
			return printName() + "\t\t\t" + getBits() + "\t\t" + countOnes() + "\t\t" + getMark();
		}
	
	}
}