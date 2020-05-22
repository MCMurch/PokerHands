
public class Card {

	private final String value; 
	private final String suit; 
	
	public Card(String value, String suit) {
		this.value = value; 
		this.suit = suit; 
	}
	
	public String suit() {
		return suit; 
	}
	
	public String value() {
		return value; 
	}
	
	public String toString() { 
		return value + suit + ""; 
	}
	
}
