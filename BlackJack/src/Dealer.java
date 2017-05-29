/*
 * This class initializes the dealer's card's initial point value, 
 * but does not give him a defined number of chips, since this version of blackjack only tracks player chips.
 */
public class Dealer {
	public int cardScore;
	public int chips;
	
	public Dealer() {
		cardScore = 0;
	}
}