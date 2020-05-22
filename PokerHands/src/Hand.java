import java.util.Arrays;

public class Hand {

	public int highestCardInTie = 0;
	private final Card[] cards;
	private String pair1 = "";
	private String pair2 = "";
	private String threeOfKind = "";
	private String fourOfKind = "";
	private int straight = 0; 
	private boolean flush = false;
	private int rank = -1; 
	
	/**
	 * Constructor
	 * @param cards
	 */
	public Hand(Card[] cards) {
		this.cards = cards; 
	}
	
	/**
	 * Compares this hand to another hand, and determines who wins
	 * @param hand - other hand 
	 * @return
	 */
	public String compareTo(Hand hand) {
		
		if(calculateRank() < hand.calculateRank()) {
			return "loss"; 
		} else if(getRank() > hand.getRank()) {
			return "win"; 
		} else if(getRank() == hand.getRank()){
			if(getRank() > 700 || (getRank() > 400 && getRank() < 600)) { //the hands are tied between three of a kind, straight, straight flush, 3 of a kind, and 4 of a kind, it is a tie
				return "tie";
			} else if(getRank() > 600) { // if rank is between 700 and 600, there is a tie between flushes, which then follows highest card
				return this.highestCardBetween(hand);
			} else if(getRank() > 300) { // if rank is between 400 and 300, there is a tie of two pair
				return this.highestCardBetween(hand);
			} else if(getRank() > 200) { // if rank is between 300 and 200, there is a tie between a single pair 
				return this.resolveTwoPair(hand);
			} else if(getRank() > 100) { // if rank is between 200 and 100, there is a tie between highest card
				return this.highestCardBetween(hand);
			}
		}
		
		return "tie"; 
	}
	
	/**
	 * Used for resolving a tie between two hands with one pair each
	 * @param hand
	 * @return
	 */
	public String resolvePair(Hand hand) {
		int[] myCards = sortCards();
		int[] otherCards = hand.sortCards();
		
		for(int i = 0; i < 5; i++) {  				//This gets rid of the cards in the pairs, so we are only comparing numbers not in pairs
			if(myCards[i] == valueToInt(pair1)) {
				myCards[i] = 0; 
			}
		}
		
		for(int i = 0; i < 5; i++) {  				
			if(otherCards[i] == valueToInt(hand.pair1())) {
				otherCards[i] = 0; 
			}
		}
		
		//Sorting them again, so the two cards that were set to 0, will be the last compared 
		Arrays.sort(myCards);
		Arrays.sort(otherCards); 
		
		for(int i = 4; i >= 0; i--) {
			if(myCards[i] > otherCards[i]) {
				return "win";
			} else if(myCards[i] < otherCards[i]) {
				return "loss";
			}
		}
		
		return "tie"; 
	}
	
	/**
	 * Used for resolving a tie between two hands with two pairs each
	 * @param hand
	 * @return - win, loss, or tie 
	 */
	public String resolveTwoPair(Hand hand) {
		
		int myRemainingCard = 0;
		int otherRemainingCard = 0; 
		
		//Figure out the non pair cards
		for(Card c : cards) {
			if(c.value() != pair1 && c.value() != pair2) {
				myRemainingCard = valueToInt(c.value());
			}
		}
		
		for(Card c : hand.cards()) {
			if(c.value() != hand.pair1() && c.value() != hand.pair2()) {
				otherRemainingCard = valueToInt(c.value());
			}
		}
		
		//determine outcome by second pair
		//then by remaining card
		if(valueToInt(pair2) <  valueToInt(hand.pair2()))
			return "loss";
		else if(valueToInt(pair2) >  valueToInt(hand.pair2()))
			return "win";
		else if(myRemainingCard < otherRemainingCard)
			return "loss";
		else if(myRemainingCard > otherRemainingCard)
			return "win";
		else 
			return "tie";
					
	}
	
	/**
	 * Determines what kind and how many pairs there are 
	 */
	public void  determineMatching() {
		
		// Compares each card to every other card 
		for(Card c : cards) {
			int count = 0;
			for(Card compare : cards) {
				if (c.value().equals(compare.value())) {
					count++; 
				}
			}
			
			//if count is two, that is a pair
			if (count == 2) {
				if(pair1.equals("")) {
					pair1 = c.value();
				}
				//Determines if the new pair is greater than the other pair, and if there is already a pair detected
				else if(!pair1.equals("")) { 
					if(valueToInt(c.value()) > valueToInt(pair1) && valueToInt(pair1) != valueToInt(c.value())) { // This sets the first pair to the highest pair, which is useful for later when there is a tie
						pair2 = pair1;
						pair1 = c.value();
					} else if(valueToInt(pair1) != valueToInt(c.value())) {
						pair2 = c.value();
					}
				}
			
			//If count is 3, 3 of a kind
			} else if (count == 3) {
				threeOfKind = c.value(); 
			//If count is 4, 4 of a kind	
			} else if (count == 4) {
				fourOfKind = c.value();
				
			}
		}
		
	}
	
	/**
	 * determines if a straight is present
	 */
	public void determineStraight() {
		int[] sorted = sortCards();
		
		//checks if each number is incremented by 1
		for(int i = 0; i < 4; i++) {
			if(sorted[i] < sorted[i+1] && (sorted[i+1] - sorted[i]) == 1 ) {
				straight =  sorted[i+1];
			} else {
				straight = 0;
				break;
			}
		}
	}
	
	/**
	 * Sorts the cards into an array of ints in ascending order  (converting T, J, Q, K, A to ints)
	 * @return the array of values 
	 */
	public int[] sortCards() {
		int[] sorted = new int[5];
		for(int i = 0; i < 5; i++) {
			sorted[i] = valueToInt(cards[i].value());
		}
		
		Arrays.sort(sorted);
		
		return sorted; 
	}
	
	/**
	 * determines if a flush is present 
	 */
	public void  determineFlush() {
		String suit = cards[0].suit(); // only way for a flush is if all the suits are the same
		int count = 0;
		for(Card c : cards) {
			if(c.suit().equals(suit)) {
				count++;
			}
		}
		if(count == 5)
			flush = true;
		else 
			flush = false;
		
	}
	
	/**
	 * Finds the highest card in the hand
	 * @return
	 */
	public int highestCard() {
		int highest = 0; 
		for(Card c : cards) {
			if(highest < valueToInt(c.value())) {
				highest = valueToInt(c.value());
			} 
		}
		return highest; 
	}
	
	/**
	 * Determines the highest card between this hand and another hand
	 * @param otherHand
	 * @return
	 */
	public String highestCardBetween(Hand hand) {
		
		int[] myCards = sortCards();
		int[] otherCards = hand.sortCards();
		
		for(int i = 4; i >= 0; i--) {
			if(myCards[i] > otherCards[i]) {
				highestCardInTie = myCards[i]; 	//Saves the deciding number for printing later
				return "win";
			} else if(myCards[i] < otherCards[i]) {
				hand.highestCardInTie = otherCards[i];
				return "loss";
			}
		}
		
		return "tie"; 
	}
	
	/**
	 * Converts a card value to an int
	 * @param value - string of 2-9 and T, J, Q, K, A
	 * @return a value of 2-14 
	 */
	public int valueToInt(String value) {
		if(value.equals("T")) {
			return 10;
		} else if(value.equals("J")) {
			return 11;
		} else if(value.equals("Q")) {
			return 12;
		} else if(value.equals("K")) {
			return 13;
		} else if(value.equals("A")) {
			return 14;
		} else {
			return Integer.parseInt(value);
		} 
	}
	
	/**
	 * Covnerts ints to card values
	 * @param i - int in range 2-14
	 * @return
	 */
	public String intToValue(int i) {
		if(i == 11) {
			return "Jack";
		} else if(i == 12) {
			return "Queen";
		} else if(i == 13 ) {
			return "King";
		} else if(i == 14) {
			return "Ace";
		} else {
			return Integer.toString(i);
		} 
	}
	
	/**
	 * Determines the rank of the hand based on matches, if there is a flush, and if there is a straight
	 * @return - number between 100 and 914 (highest possible value)
	 */
	public int calculateRank() {
		
		//These set the members necessary for setting rank 
		determineMatching();
		determineFlush();
		determineStraight();
		
		//rank is a scale of 100-900, with the highest numbers added on 
		if (straight != 0 && flush) { 														//straight flush 
			rank = 900 + straight;  
		} else if (!fourOfKind.equals("")) {					 							//four of a kind 
			rank = 800 + valueToInt(fourOfKind); 
		} else if (!threeOfKind.equals("") && (!pair1.equals("") || !pair2.equals(""))) { 	//full house 
			rank = 700 + valueToInt(threeOfKind); 
		} else if (straight == 0 && flush) { 												//flush 
			rank = 600 + highestCard();
		} else if (straight != 0 && !flush) {						 						//straight 
			rank = 500 + straight; 	
		} else if (!threeOfKind.equals("")) { 												//three of a kind
			rank = 400 + valueToInt(threeOfKind); 
		} else if (!pair1.equals("") && !pair2.equals("")) { 								//two pair (possible tie)
			rank = 300 + Math.max(valueToInt(pair1), valueToInt(pair2)); 
		} else if(!pair1.equals("") && pair2.equals("")) { 									//one pair (possible tie)
			rank = 200 + valueToInt(pair1); 
		}    else {																			//high card (possible tie)
			rank = 100 + highestCard(); 
		} 
		
		return rank;
	}
	
	/**
	 * Gets the rank, if the rank has not been set, then will return -1
	 * @return
	 */
	public int getRank() {
		return rank; 
	}
	
	/**
	 * Helper method to print results 
	 */
	public String displayHandResult() {
		
		if(rank > 900) {
			return "Straight Flush: " + intToValue(straight) + " High" ; 
		} else if(rank > 800) {
			return "Four of a Kind: " + fourOfKind; 
		} else if(rank > 700) {
			return "Full House: " + threeOfKind + " over " + pair1;
		} else if(rank > 600) { //A tie in flush is determined by the highest card 
			if(highestCardInTie != 0) {
				return "Flush: " + cards[0].suit() + " and High Card: " + intToValue(highestCardInTie);
			} else {
				return "Flush: " + cards[0].suit() + " and High Card: " + intToValue(highestCard());
			}
		} else if(rank > 500) {
			return "Straight: " +  intToValue(straight) + " High";
		} else if(rank > 400) {
			return "Three of a Kind: " + threeOfKind;
		} else if(rank > 300) {
			return "Two Pair: " + pair1 + " and " + pair2;
		} else if(rank > 200) {
			return "Single Pair: " + pair1;
		} else if(rank > 100) {
			if(highestCardInTie != 0) {
				return "High card: " + intToValue(highestCardInTie);
			} else {
				return "High card: " + intToValue(highestCard());
			}
		} else {
			return "Rank not calculated "; //You must run the compareTo method or else this will be returned
		}
	}
	
	/**
	 * String representation of the hand's cards
	 */
	public String toString() {
		String result = ""; 
		
		for(Card c : cards) {
			result += c.toString() + " ";
		}
		
		return result; 
	}
	
	/**
	 * getter for the cards 
	 * @return cards 
	 */
	public Card[] cards() {
		return cards;
	}
	
	/**
	 * Getter for pair, used for tie resolving
	 * @return value of first/highest pair
	 */
	public String pair1() {
		return pair1; 
	}
	
	/**
	 * Getter for pair 2
	 * @return value of pair 2
	 */
	public String pair2() {
		return pair2; 
	}
	
}
