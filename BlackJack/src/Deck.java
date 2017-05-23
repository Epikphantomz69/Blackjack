import java.util.*;
public class Deck {
	
	private ArrayList<Card> cards = new ArrayList<Card>();
	
	public Deck() {
		String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
		int[] values = {11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};
		String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
		for(int i = 0; i < ranks.length; i++) {
			for(int j = 0; j < suits.length; j++)  {
				cards.add(new Card(ranks[i], suits[j], values[i]));
			}
		}
	    shuffle();
	}
	
	public void shuffle() {
		for (int k = cards.size() - 1; k > 0; k--) {
			int position = (int) (Math.random() * (k + 1));
			Card temp = cards.get(k);
			cards.set(k, cards.get(position));
			cards.set(position, temp);
		}
	}
	
	public ArrayList<Card> getList(){
		return cards;
	}
	
	//Removes card from top of ArrayList and returns it	
	public Card removeCard() {
		Card temp = cards.get(cards.size() - 1);
		cards.remove(cards.size() - 1);
		return temp;
	}	

}
