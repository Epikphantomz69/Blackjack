
public class Deck {
	
	private ArrayList<Card> deck;
	private int size;
	
	public Deck() {
	    deck = new ArrayList<Card>();
	    for(int i = 0; i < 4; i++) {
	        for(int j = 1; j <= 13; j++) {
	            deck.add(new Card(i,j));
	        }
	    }
	    size = cards.size;
	    shuffle();
	}
	
	public void shuffle() {
		for (int k = cards.size() - 1; k > 0; k--) {
			int position = (int) (Math.random() * (k + 1));
			Card temp = cards.get(k);
			cards.set(k, cards.get(position));
			cards.set(position, temp);
		}
		size = cards.size();
	}
	
	
	
	public Card removeCard() {
		return deck.remove(0);
	}

}
