
public class PokerMain {

	public static void main(String[] args) {
		
		String[] tests = new String[16];
																   //These should matcht he output 
		tests[0] = "Black: 2D 3D 4D 5D 6D  White: 2D 3H 5C 9S KH"; //black wins with straight flush
		tests[1] = "Black: 2H 2D 2S 5C 5D  White: KC KH KS KC AH"; //white wins 4 of a kind
		tests[2] = "Black: 2H 4S 4C 2D 4H  White: 2S 8S AS QS 3S"; //black wins full house
		tests[3] = "Black: 2D TD 4D 8D KD  White: 2C 3H 4H 8D KH"; //black wins with flush
		tests[4] = "Black: 2H 3S 4C 5D 6H  White: 2S 2C QS QH 3S"; //black wins straight
		tests[5] = "Black: 2H 4S TC 2D 6H  White: 2S 8C AH AC AS"; //white wins with 3 of a kind
		tests[6] = "Black: 2H 7S 4C 2D 4H  White: 2S 2C AS QS 3S"; //black wins 2 pairs
		tests[7] = "Black: AH 7S 5C 2D 3H  White: AC 8S AS QS 3S"; //white wins with 1 pair
		tests[8] = "Black: 2H TD 4S 8C KD  White: 2C 3H 4H 8D KH"; //black wins with 10 high
		
		//Ties / special situations
		tests[9] = "Black: 2H 3S 4C 5D 6H  White: 2C 3D 4H 5C 6C"; 	//tie of straights
		tests[10] = "Black: 2H 7S 4C 2D 4H  White: 2S 2C 4D 7C 4S"; //tie of 2 pairs
		tests[11] = "Black: 2H 8S 4C 2D 4H  White: 2S 2C 4D 7C 4S"; //tie of 2 pairs, decided by final card
		tests[12] = "Black: 7H 8S 4C 7D 4H  White: 2S 4C 2D 7C 4S"; //2 pairs, decided by higher pair
		tests[13] = "Black: 3H 8S 4C 3D 4H  White: 2S 4C 2D 7C 4S"; //tie of 2 pairs, decided by higher second pair
		tests[14] = "Black: 2H 3D 4S 8C KD  White: 2C 3H 4H 8D KH"; //tie of high cards
		tests[15] = "Black: 2D 3D 4D 8D KD  White: 2H 3H 4H 9H KH"; //tie of flushes, determined by high card 9
		
		
		for(String input : tests) {
			//Parsing the input
			String black = input.substring(7,21); 
			String white = input.substring(30, 44);
			String[] blackCards = black.split(" "); 
			String[] whiteCards = white.split(" "); 
			Card[] bCards = new Card[5];
			Card[] wCards = new Card[5];
	
			//creating card arrays to make hands
			int i = 0; 
			for(String c : blackCards) {
				bCards[i] = new Card(c.substring(0, 1), c.substring(1, 2));  
				i++;
			}
			i = 0;
			for(String c : whiteCards) {
				wCards[i] = new Card(c.substring(0, 1), c.substring(1, 2));  
				i++;
			}
			
			//constructing hands 
			Hand blackHand = new Hand(bCards);
			Hand whiteHand = new Hand(wCards);
	
			/***************************** Printing Results *************************/
			
			//Prints out the cards being compared and their ranks 
			//Comment this out if you want, I left it in because I think it makes it more readable and understandable 
			System.out.println("\n" + blackHand.toString() + " v.s. " + whiteHand.toString());
			System.out.println("Black: " + blackHand.calculateRank());
			System.out.println("White: " + whiteHand.calculateRank());
			
			//Determines the winner and prints the appropriate message
			if(blackHand.compareTo(whiteHand).equals("win")) {
				System.out.println("Black wins - with " + blackHand.displayHandResult());
			} else if(blackHand.compareTo(whiteHand).equals("loss")) {
				System.out.println("White wins - with " + whiteHand.displayHandResult());
			} else {
				System.out.println("Tie");
			}
		}
	
	}

}
