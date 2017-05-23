public class Card {
	
	private String suit;
	private String rank;
	private int value;
	
	public Card(String cardSuit, String cardRank, int cardValue) {
		suit = cardSuit;
		rank = cardRank;
		value = cardValue;
	}
	
	public String getSuit() {
		return suit;
	}
	
	public String getRank() {
		return rank;
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean matches(Card otherCard) {
		return otherCard.getSuit().equals(this.getSuit())
			&& otherCard.getRank().equals(this.getRank())
			&& otherCard.getValue() == this.getValue();
	}
	
	public String toString() {
		return rank + " of " + suit + " (point value = " + value + ")";	
	}
}
	
