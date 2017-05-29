/*
 * This class instantiates a player and dealer object, 
 * checks the boolean to see if the game is over and hit methods for the player and dealer to introduce additional cards.
 */
public class Game {
	public Player player;
	public Dealer dealer;
	public boolean isGameOver;

	public Game() {
		player = new Player();
		dealer = new Dealer();
		isGameOver = false;			
	}
	//Returns the card value given to the player after he hits
	public int hit() {
		Card card = new Card();
		player.cardScore = player.cardScore + card.getValue();
		return card.getValue();
	}  
	//Returns the card value given to the dealer after he hits
	public int dealerHit() {
		Card card = new Card();
		dealer.cardScore = dealer.cardScore + card.getValue();
		return card.getValue();
	}
}