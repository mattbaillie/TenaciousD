package blackjack;

public class card {
	public int value;
	public String name;
	public String suit;
	
	public card(String name, String suit) {
		this.name = name;
		this.suit = suit;
		if(this.name.equals("K") || this.name.equals("Q") || this.name.equals("J")) {
			this.value = 10;
		} else if(this.name.equals("A")) {
			// By default
			this.value = 1;
		} else {
			this.value = Integer.parseInt(name);
		}
	}
	
	//Determine card value for aces considering current state of hand
	public int cardVal(int handValue) {
		if(this.name.equals("A")) {
			if(handValue + 11 < 22) {
				this.value = 11;
			} else {
				this.value = 1;
			}
		}
		return this.value;
	}
	
	public String printCard(int handValue) {
		String retString = "      ";
		retString += this.suit;
		retString += this.name;
		retString += " worth ";
		if(this.name.equals("A")) {
			retString += "1 or 11";
		} else {
			retString += this.value;
		}
		
		return retString;
	}
}
