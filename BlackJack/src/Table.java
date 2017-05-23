import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionListener.*;
import java.awt.event.ActionEvent;

public class Table extends JFrame implements ActionListener {
	private static Deck deck;
	private static ArrayList<Card> dealerCards; 
	private static ArrayList<Card> playerCards; 
	private static Card dealerHiddenCard;
	private static JButton newGameButton;
	private static JButton endGameButton;
	private static JButton dealButton;
	private static JLabel dealerLabel;
	private static JLabel playerLabel;
	private static JButton hitButton;
	private static JButton standButton;
	private static JLabel infoLabel;
	private static JButton continueButton;
	private static JLabel shuffleInfoLabel = null;
	private static int roundCounter = 0;

	public static void initGui() {
		JFrame frame = new JFrame();
		newGameButton = new JButton("New Game");
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newGame();
			}
		});
		
		newGameButton.setBounds(20, 610, 99, 50);
		frame.getContentPane().add(newGameButton);

		endGameButton = new JButton("End Game"); //End game button - removes all GUI objects and restarts
		endGameButton.setEnabled(false);
		endGameButton.setBounds(121, 610, 99, 50);
		endGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll(); //Remove all objects from screen
				frame.repaint(); //Repaint to show update
				initGui(); //Restart game and display New Game menu
			}
		});
		frame.getContentPane().add(endGameButton);
	}
	
	public static void showDealGui() {
		JFrame frame = new JFrame();
		endGameButton.setEnabled(true);
		infoLabel = new JLabel("Click Deal");
		infoLabel.setBackground(Color.ORANGE);
		infoLabel.setOpaque(false);
		infoLabel.setForeground(Color.ORANGE);
		infoLabel.setFont(new Font("Arial", Font.BOLD, 16));
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		infoLabel.setBounds(290, 482, 320, 28);
		frame.getContentPane().add(infoLabel);
		
		//Deal button
		dealButton = new JButton("Deal");
		dealButton.setBounds(679, 610, 200, 50);
		dealButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deal();
			}
		});
		frame.getContentPane().add(dealButton);
		dealButton.requestFocus();

		frame.repaint();
	}
	
	public static void deal() {
		JFrame frame = new JFrame();
		if (shuffleInfoLabel != null) {
			frame.getContentPane().remove(shuffleInfoLabel);
		}
		//Initialize dealer/player card arrays
		dealerCards = new ArrayList<Card>();
		playerCards = new ArrayList<Card>();
		for(int card = 0; card < 2; card++){
			dealerCards.add(deck.removeCard());
			playerCards.add(deck.removeCard());
		}
		

		dealButton.setEnabled(false);
		infoLabel.setText("Please Hit or Stand");

		//Dealer label
		dealerLabel = new JLabel("Dealer");
		dealerLabel.setForeground(Color.WHITE);
		dealerLabel.setFont(new Font("Arial Black", Font.BOLD, 20));
		dealerLabel.setBounds(415, 158, 82, 28);
		frame.getContentPane().add(dealerLabel);

		playerLabel = new JLabel("Player"); // Player label
		playerLabel.setForeground(Color.WHITE);
		playerLabel.setFont(new Font("Arial Black", Font.BOLD, 20));
		playerLabel.setBounds(415, 266, 82, 28);
		frame.getContentPane().add(playerLabel);

		//Hit button
		hitButton = new JButton("Hit");
		hitButton.setBounds(290, 515, 140, 35);
		hitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hit();
			}
		});
		frame.getContentPane().add(hitButton);
		hitButton.requestFocus();

		//Stand button
		standButton = new JButton("Stand");
		standButton.setBounds(470, 515, 140, 35);
		standButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stand();
			}
		});
		frame.getContentPane().add(standButton);

		continueButton = new JButton("Continue"); //When the final outcome is reached, press this to accept and continue the game
		continueButton.setEnabled(false);
		continueButton.setVisible(false);
		continueButton.setBounds(290, 444, 320, 35);
		continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				acceptOutcome(); //Accept outcome
			}
		});
		frame.getContentPane().add(continueButton);
		frame.repaint(); //Redraw frame to show changes

		//dealerHiddenCard = deck.removeCard(); //Take a card from top of deck for dealer but hide it
		//dealerCards.add(new Card("", "", 0)); //Add turned over card to dealer's cards

		simpleOutcomes(); //Checks for any automatic outcomes (e.x. immediate blackjack)
	}
	
	public static void hit() { //Add another card to player cards, show the new card and check for any outcomes
		//ArrayList<Card> playerCards = deck.getList(); //Accesses player's cards
		playerCards.add(deck.removeCard());
		simpleOutcomes();
	}
	
	public static boolean simpleOutcomes() { //Runs automatically whenever deal is pressed or the player hits
		boolean outcomeHasHappened = false;
		//ArrayList<Card> dealerCards = deck.getList(); //Accesses dealer's cards
		int playerScore = getTotalValue(playerCards); //Get player score in the form of total card value
		int dealerScore = getTotalValue(dealerCards);
		if (playerScore > 21 && numAces(playerCards) > 0) { //If player has at least one ace and would otherwise lose (> 21), subtract 10
			playerScore -= 10;
		}
		
		if (playerScore == 21) { //Potential player blackjack
			dealerCards.set(0, dealerHiddenCard); //Replace hidden dealer's card with actual card
			if (dealerScore == 21) { //If dealer ALSO gets a blackjack
				infoLabel.setText("Push!"); // Push
			} else {
				//Player gets a blackjack only
				infoLabel.setText("Player gets Blackjack!");
			}
			outcomeHasHappened = true;
			outcomeHappened(); // If something's happened, round is over - results of round and continue button are both shown
		} else if (playerScore > 21) { // If player goes bust
			infoLabel.setText("Player goes Bust!");
			dealerCards.set(0, dealerHiddenCard); //Replaces hidden dealer's card with actual card
			outcomeHasHappened = true;
			outcomeHappened(); //If something's happened, the round is over - results of round and continue button are both shown
		}
		return outcomeHasHappened;
	}
	
	public static void stand() { // When stand button is pressed
		//ArrayList<Card> dealerCards = deck.getList(); //Accesses dealer's cards
		if (simpleOutcomes()) // Check for any normal outcomes. If so, we don't need to do anything here so return.
			return;
 
		int playerScore = getTotalValue(playerCards); // Get player score as total of cards he has
		if (playerScore > 21 && numAces(playerCards) > 0) // If player has at least one ace and would otherwise lose (> 21), subtract 10
			playerScore -= 10;

		dealerCards.set(0, dealerHiddenCard); // Replace hidden dealer's card with actual card

		int dealerScore = getTotalValue(dealerCards); // Get dealer score as total of cards he has

		while (dealerScore < 16) { // If dealer's hand is < 16, he needs to get more cards until hand is > 16
			dealerCards.add(deck.removeCard()); // Take a card from top of deck and add
			if (dealerScore > 21 && numAces(dealerCards) > 0) // If there's an ace and total > 21, subtract 10
				dealerScore -= 10;
		}
		//Determine final outcomes
		//Player wins
		if (playerScore > dealerScore) {
			infoLabel.setText("Player wins!");
		// Dealer has blackjack
		} else if (dealerScore == 21) {
			infoLabel.setText("Dealer gets Blackjack!");
		//Dealer busts
		} else if (dealerScore > 21) {
			infoLabel.setText("Dealer busts!");
		//Push
		} else if (playerScore == dealerScore) {
			infoLabel.setText("Push!");
		} else { //Otherwise - dealer wins
			infoLabel.setText("Dealer Wins!");
		}
		outcomeHappened(); //Round is over
	}
	
	public static void outcomeHappened() { //Round is over - results of round are shown and continue button is displayed
		hitButton.setEnabled(false);
		standButton.setEnabled(false);
		infoLabel.setOpaque(true);
		infoLabel.setForeground(Color.RED);
	}
	
	public static void acceptOutcome() { //Outcome has been reached
		JFrame frame = new JFrame();
		infoLabel.setOpaque(false);
		infoLabel.setForeground(Color.ORANGE);
		
		//Remove deal objects
		frame.getContentPane().remove(dealerLabel);
		frame.getContentPane().remove(playerLabel);
		frame.getContentPane().remove(hitButton);
		frame.getContentPane().remove(standButton);
		frame.getContentPane().remove(continueButton);
		infoLabel.setText("Click Deal");
		dealButton.setEnabled(true);
		dealButton.requestFocus();
		frame.repaint();
		frame.getContentPane().removeAll();
		frame.repaint();
		initGui();

		roundCounter += 1; 
		// If >= 5 rounds, reshuffle the deck to prevent running out of cards
		if (roundCounter >= 5) {
			deck.shuffle();
			shuffleInfoLabel = new JLabel("Deck has been replenished and reshuffled!");
			shuffleInfoLabel.setForeground(Color.ORANGE);
			shuffleInfoLabel.setFont(new Font("Arial", Font.BOLD, 20));
			shuffleInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
			shuffleInfoLabel.setBounds(235, 307, 430, 42);
			frame.getContentPane().add(shuffleInfoLabel);
			roundCounter = 0;
		}
	}
	//Returns total value of hand
	public static int getTotalValue(ArrayList<Card> cards) {
		int totalValue = 0;
		for(int i = 0; i < cards.size(); i++) {
			totalValue += cards.get(i).getValue();
		}
		return totalValue;
	}
	//Returns number of aces in hand
	public static int numAces(ArrayList<Card> cards) { //Ace function
		int numAces = 0;
		for(int i = 0; i < cards.size(); i++) {
			if(cards.get(i).equals("Ace")) {
				numAces += 1;
			}
		}
		return numAces;
	}
	//Starts new game
	public static void newGame() {
		newGameButton.setEnabled(false);		
		showDealGui();
		roundCounter = 0;
		deck = new Deck();
		deck.shuffle();
	}

	public static void main(String args[]) {
		JFrame frame = new JFrame();
		initGui();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
     			
}  

