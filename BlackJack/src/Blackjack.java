import java.util.Scanner;
public class Blackjack {
	public static void main(String[] args) {
		Game newGame = new Game();
		do { //Game is guaranteed to execute at least once
			newGame.player.cardScore = 0;
			newGame.dealer.cardScore = 0;

			//Player is welcomed to game and bets	        
			System.out.println("Welcome to Blackjack! You currently have " + newGame.player.chips + " chip(s) available to bet.");
			System.out.print("Enter the number of chips you would like to bet: ");
			Scanner input = new Scanner(System.in);
			int bet = input.nextInt();

			//Protection against invalid bet entry     
			while(bet > newGame.player.chips) {
				System.out.println("You only have " + newGame.player.chips + " chip(s).");
				System.out.println("Enter an amount less than or equal to " + newGame.player.chips + " chip(s): ");
				bet = input.nextInt();
			}
			while(bet < 1) {
				System.out.println("You must bet at least 1 chip each round. Enter a value between 1 and " + newGame.player.chips + ": ");
				bet = input.nextInt();
			}
			System.out.println("You have bet " + bet + " chip(s).");

			//Player is dealt two cards
			newGame.hit();
			newGame.hit();
			//Checks if player was dealt aces     
			while(newGame.player.cardScore > 21) {
				System.out.println("You have been dealt two aces!");
				newGame.player.cardScore = newGame.player.cardScore - 10;
				System.out.println("In order to prevent busting, one of the aces has been set to 1.");
			}
			//Checks for immediate blackjack
			if(newGame.player.cardScore == 21) {
				System.out.println("You were dealt a total of " + newGame.player.cardScore + ".");
				System.out.println("IMMEDIATE BLACKJACK!");
			}
			else {
				System.out.println("Your hand has a value of " + newGame.player.cardScore + ".");
			}

			//Dealer is dealt two cards, with one card instantiated (represents the card facing up) and the other "facing down" 
			newGame.dealerHit();
			System.out.println("The dealer's visible card has a value of " + newGame.dealer.cardScore + ".");

			//Player's turn: Hit or Stand? Not necessary to ask if player has immediate blackjack. 
			if(newGame.player.cardScore == 21) {
				System.out.println("Let's see the dealer's second card...");
			}
			else {
				System.out.println("Would you like to hit or stand? h/s");
				Scanner typedLine = new Scanner(System.in);
				String hitOrStand = typedLine.nextLine();

				//Player hits 
				while(hitOrStand.equals("h")) {
					System.out.println("Your new card has been dealt.");
					Card newCard = new Card();
					newGame.player.cardScore = newGame.player.cardScore + newCard.getValue();

					//Checks if new card dealt is an ace. 
					if(newCard.getValue() == 11 && newGame.player.cardScore > 21) {
						System.out.println("Your new card is an ace, which holds a value of " + newCard.getValue() + "."); 
						newGame.player.cardScore = newGame.player.cardScore - 10;
						System.out.println("In order to prevent busting, the ace has been set to 1.");
						System.out.println("Your hand total is now " + newGame.player.cardScore + "."); 
					}
					else {
						System.out.println("Your hand total is now " + newGame.player.cardScore + ".");
					}
					//Check if player busted, has blackjack or needs to be prompted again to either hit or stand
					if(newGame.player.cardScore > 21) {
						System.out.println("You have busted. Better luck next time!"); 
						break;
					}
					else if(newGame.player.cardScore == 21) {
						System.out.println("BLACKJACK!");
						break;
					}
					else {    
						System.out.println("Would you like to hit or stand? h/s");
						hitOrStand = typedLine.nextLine(); 
					} 
				}
				//Player stands    
				if(typedLine.equals("s")) {
					System.out.println("Your hand total remains put at " + newGame.player.cardScore + ".");
				}
			}
			//Dealer's turn         
			newGame.dealerHit();
			System.out.println("The dealer flips the face-down card.");
			//Checks if dealer's new card is an ace  
			if(newGame.dealer.cardScore > 21) {
				System.out.println("The dealer now has two aces and makes one of them worth 1 point without busting.");
				newGame.dealer.cardScore -= 10;
			}
			System.out.println("The dealer's hand total is now " + newGame.dealer.cardScore + ".");

			//Player busts, dealer wins  
			if(newGame.player.cardScore > 21) {
				System.out.println("The dealer won since you busted. You have lost the " + bet + " chips that you bet.");
				newGame.player.chips -= bet;    
				System.out.println("You now have " + newGame.player.chips + " chips remaining.");
			}
			else {
				//Dealer continually draws cards while player stands      
				while(newGame.dealer.cardScore < 17 && newGame.dealer.cardScore < newGame.player.cardScore) {
					System.out.println("The dealer receives a new card.");
					int dealerCardValue = newGame.dealerHit();
					if(newGame.dealer.cardScore > 21 && dealerCardValue == 11) {
						System.out.println("The dealer was dealt an ace. \nTo prevent himself from busting, the dealer makes this ace worth 1 point.");
						newGame.dealer.cardScore -= 10;
					}
					//Dealer busts
					else if(newGame.dealer.cardScore > 21) {
						System.out.println("The dealer busts. You have won " + bet + " chips!");
						newGame.player.chips += bet;
						System.out.println("You have " + newGame.player.chips + " chips remaining.");
					}
					System.out.println("The dealer's hand total is now " + newGame.dealer.cardScore + "."); 
				}
				//Player wins
				if(newGame.player.cardScore > newGame.dealer.cardScore) {
					System.out.println("You win! You have won " + bet + " chips!");
					newGame.player.chips += bet;
					System.out.println("You now have a total of " + newGame.player.chips + " chips.");
				}
				//Dealer has blackjack
				else if(newGame.dealer.cardScore == 21) {
					System.out.println("The dealer has BLACKJACK! You have lost the " + bet + " chips you've bet.");
					newGame.player.chips -= bet;
				}
				//Dealer wins otherwise
				else if(newGame.dealer.cardScore > newGame.player.cardScore && newGame.dealer.cardScore <= 21) {
					System.out.println("The dealer wins! You have lost the " + bet + " chips you've bet.");
					newGame.player.chips -= bet;
					//Check if there are player chips remaining
					if(newGame.player.chips <= 0) {
						System.out.println("You must not have been paying much attention - you lost all your chips!");
					}
					else {
						System.out.println("You have " + newGame.player.chips + " chips remaining.");        
					}
				}
				//Push - the scenario in which both the player's and dealer's cards match in value at the end of the round
				else if(newGame.player.cardScore == newGame.dealer.cardScore) {
					System.out.println("This round resulted in a push. You get your chips back.");
					System.out.println("You still have " + newGame.player.chips + " chips remaining.");
				}
			}
			//Check if player wants to continue playing         
			System.out.println("Would you like to continue playing? y/n");
			Scanner playAgain = new Scanner(System.in);
			String decision = playAgain.nextLine();
			if (decision.equals("y")) {
				//If the user still tries to play without any chips, the game ends because they do not have any remaining chips to bet
				if(newGame.player.chips <= 0) { 
					System.out.println("You're all out of chips. Think twice before betting it all next time. Thanks for playing!");
					newGame.isGameOver = false;
				}
				else { //Otherwise, since the player still has chips to bet, start a new round!
					newGame.isGameOver = false;
				}
			}
			else if(decision.equals("n")) { //Quit game and print message
				newGame.isGameOver = true;
				System.out.println("Thanks for playing!");
			}
		}
		//Do while loop condition - while player chips remain and the game is not over, run the game again!
		while(newGame.player.chips > 0 && newGame.isGameOver == false);  	
	}
}