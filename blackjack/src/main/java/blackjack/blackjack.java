package blackjack;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class blackjack {
	
	//Consts
	public static String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
	public static String[] suits = {"H", "D", "S", "C"};
	
	public static void main(String[] arg) {
		int playerHandValue;
		int dealerHandValue;
		boolean playerSplit = false;
		int playerSplitValue = 0;
		boolean playingWithSplit = false;
		int dealerSplitValue = 0;
		List<card> playerHand = new ArrayList<card>();
		List<card> dealerHand = new ArrayList<card>();
		
		Scanner console = new Scanner(System.in);
		
		//Create deck
		List<card> deck = createDeck();
		deck = shuffleDeck(deck);
		
		//Deck is now shuffled, distribute cards
		for(int x = 0; x < 2; x++) {
			playerHand.add(deck.get(0));
			deck.remove(0);
		}
		for(int x = 0; x < 2; x++) {
			dealerHand.add(deck.get(0));
			deck.remove(0);
		}
		
		//Evaluate initial hand values
		playerHandValue = playerHand.get(0).cardVal(0);
		playerHandValue += playerHand.get(1).cardVal(playerHandValue);
		
		dealerHandValue = dealerHand.get(0).cardVal(0);
		dealerHandValue += dealerHand.get(1).cardVal(dealerHandValue);
		
		printPlayerHand(playerHand, playerHandValue);
		printDealerHand(dealerHand, dealerHandValue);
		
		//Check for wins
		if(dealerHandValue == 21) {
			System.out.println("Dealer blackjack! Dealer wins.");
			return;
		} else if(playerHandValue == 21 && dealerHandValue != 21) {
			System.out.println("Player blackjack! Player wins.");
			return;
		}
		
		//Player plays
		System.out.println("Your current hand value is " + playerHandValue + ". Do you hit or stand?");
		// If the player's 2 cards are the same value, they can split
		if(canSplit(playerHand)) {
			System.out.println("You also have the option of splitting by pressing 'D'.");
			playerSplit = true;
		}
		boolean validInput = false;
		while(validInput == false) {
			String input = console.next();
			if(input.equals("H")) {
				System.out.println("Player hits");
				//Hit
				playerHand.add(deck.get(0));
				deck.remove(0);
				
				//Reevaluate hand value
				playerHandValue = calcHandValue(playerHand);
				
				//Bust
				if(playerHandValue > 21) {
					if(playerSplitValue > 0) {
						System.out.println("You busted, but your split hand value is " + playerSplitValue + ".");
						playingWithSplit = true;
					} else {
						System.out.println("You have busted with a hand value of " + playerHandValue + ".");
						System.out.println("Dealer wins.");
						return;
					}
					validInput = true;
				} else {
					System.out.println("Your current hand value is " + playerHandValue + ". Do you hit or stand?");
				}
			} else if (input.equals("S")) {
				//Stand
				validInput = true;
				
				if(playerHandValue < playerSplitValue) {
					System.out.println("This hand had a value of "+ playerHandValue + ", but your split hand had a value of " + playerSplitValue + ". Your split hand value will be used.");
					playingWithSplit = true;
				}
				if(playingWithSplit) {
					System.out.println("Player hand value: " + playerSplitValue);
				} else {
					System.out.println("Player hand:" + playerHandValue);
					for(int x = 0; x < playerHand.size(); x++) {
						System.out.println(playerHand.get(x).printCard(playerHandValue));
					}
				}
			} else if (input.equals("D") && playerSplit == true) {
				//Split
				playerSplitValue = playPlayerSplit(playerHand.get(0), deck, console);
				//Prevent them from splitting again
				playerSplit = false;
				//Take split card out of player hand
				playerHand.remove(0);
				playerHandValue = calcHandValue(playerHand);
				System.out.println("Your split hand resulted in a value of " + playerSplitValue + ". You may now play your other hand.");
				System.out.println("Player hand:" + playerHandValue);
				for(int x = 0; x < playerHand.size(); x++) {
					System.out.println(playerHand.get(x).printCard(playerHandValue));
				}
				System.out.println("Your current hand value is " + playerHandValue + ". Do you hit or stand?");
			} else {
				//Get input again
				if(playerSplit == true) {
					System.out.println("Invalid input. Valid inputs are 'D', 'H', or 'S'.");
				} else {
					System.out.println("Invalid input. Valid inputs are 'H' or 'S'.");
				}
			}
		}
		
		//Dealer plays
		System.out.println("Dealer hand: " + dealerHandValue);
		for(int x = 0; x < dealerHand.size(); x++) {
			System.out.println(dealerHand.get(x).printCard(dealerHandValue));
		}
		
		//Dealer split
		if(canSplit(dealerHand)) {
			dealerSplitValue = playDealerSplit(dealerHand.get(0), deck);
			dealerHand.remove(0);
		}
		
		//Dealer decision-making
		int numAces = 0;
		//Hit until satisfied
		while(dealerHandValue < 16 || (numAces >= 1 && dealerHandValue == 17)) {
			numAces = 0;
			
			card newCard = deck.get(0);
			dealerHand.add(deck.get(0));
			deck.remove(0);
			
			//Reevaluate hand value
			dealerHandValue = calcHandValue(dealerHand);
			System.out.println("Dealer pulls " + newCard.name + newCard.suit + " for a new hand value of " + dealerHandValue);
			
			//Determine number of aces
			for(int x = 0; x < dealerHand.size(); x++) {
				if(dealerHand.get(x).name == "A") {
					numAces++;
				}
			}
		}
		
		//Print dealer hand again
		System.out.println("Dealer hand: " + dealerHandValue);
		for(int x = 0; x < dealerHand.size(); x++) {
			System.out.println(dealerHand.get(x).printCard(dealerHandValue));
		}
		
		//Use dealer's split hand value if it is higher
		if(dealerSplitValue > dealerHandValue) {
			dealerHandValue = dealerSplitValue;
		}
		
		//Decide winner
		if(dealerHandValue <= 21) {
			//Compare to player hand
			if(playingWithSplit) {
				if(dealerHandValue >= playerSplitValue) {
					System.out.println("Dealer wins with a score of " + dealerHandValue + " versus player score of " + playerSplitValue);
				} else {
					System.out.println("Player wins with a score of " + playerSplitValue + " versus dealer score of " + dealerHandValue);
				}
			} else {
				if(dealerHandValue >= playerHandValue) {
					System.out.println("Dealer wins with a score of " + dealerHandValue + " versus player score of " + playerHandValue);
				} else {
					System.out.println("Player wins with a score of " + playerHandValue + " versus dealer score of " + dealerHandValue);
				}
			}
		} else {
			//Dealer bust
			System.out.println("Dealer bust! Player wins.");
		}
		
		return;
	}
	
	public static int calcHandValue(List<card> hand) {
		int val = 0;
		List<card> aces = new ArrayList<card>();
		for(int x = 0; x < hand.size(); x++) {
			if(hand.get(x).name.equals("A")) {
				aces.add(hand.get(x));
				//separate the ace for later evaluation
			} else {
				val += hand.get(x).value;
			}
		}
		
		for(int x = 0; x < aces.size(); x++) {
			if(val + 11 <= 21) {
				val += 11;
			} else {
				val += 1;
			}
		}		
		return val;
	}
	
	public static int playDealerSplit(card firstCard, List<card> deck) {
		int handValue = 0;
		List<card> hand = new ArrayList<card>();
		hand.add(firstCard);
		hand.add(deck.get(0));
		deck.remove(0);
		handValue = calcHandValue(hand);
		
		//Dealer decision-making
		int numAces = 0;
		//Hit until satisfied
		while(handValue < 16 || (numAces >= 1 && handValue == 17)) {
			numAces = 0;
			
			hand.add(deck.get(0));
			deck.remove(0);
			
			//Reevaluate hand value
			handValue = calcHandValue(hand);
			
			//Determine number of aces
			for(int x = 0; x < hand.size(); x++) {
				if(hand.get(x).name == "A") {
					numAces++;
				}
			}
		}
		
		if(handValue > 21) {
			handValue = 0;
		}
		
		System.out.println("Dealer's split hand has a value of " + handValue);
		return handValue;
	}
	
	public static int playPlayerSplit(card firstCard, List<card> deck, Scanner console) {
		int handValue = 0;
		// Add first card to this hand
		List<card> hand = new ArrayList<card>();
		hand.add(firstCard);
		hand.add(deck.get(0));
		deck.remove(0);
		handValue = calcHandValue(hand);
		System.out.println("Split hand 1:");
		for(int x = 0; x < hand.size(); x++) {
			System.out.println(hand.get(x).printCard(handValue));
		}
		
		//Player plays
		System.out.println("Your current split hand value is " + handValue + ". Do you hit or stand?");
		boolean validInput = false;
		while(validInput == false) {
			String input = console.next();
			if(input.equals("H")) {
				//Hit
				hand.add(deck.get(0));
				deck.remove(0);
				
				//Reevaluate hand value
				handValue = calcHandValue(hand);
				
				//Bust
				if(handValue > 21) {
					System.out.println("You have busted this split with a value of " + handValue + ".");
					validInput = true;
					return 0;
				}
				System.out.println("Your current split hand value is " + handValue + ". Do you hit or stand?");
				
			} else if (input.equals("S")) {
				//Stand
				validInput = true;
			} else {
				//Get input again
				System.out.println("Invalid input. Valid inputs are 'H' or 'S'.");
			}
		}
		
		return handValue;
	}
	
	public static List<card> createDeck(){
		List<card> deck = new ArrayList<card>();
		//Create deck
		for(int x = 0; x < values.length; x++) {
			for(int y = 0; y < suits.length; y++) {
				deck.add(new card(values[x], suits[y]));
			}
		}
		return deck;
	}
	
	public static List<card> shuffleDeck(List<card> deck){
		//Shuffle deck
		List<card> shuffledDeck = new ArrayList<card>();
		while(shuffledDeck.size() < 52) {
			Random r = new Random();
			int index;
			index = r.nextInt(deck.size());
			shuffledDeck.add(deck.get(index));
			deck.remove(index);
		}
		return shuffledDeck;	
	}
	
	public static boolean canSplit(List <card> hand) {
		return hand.get(0).name.equals(hand.get(1).name);
	}
	
	public static void printPlayerHand(List <card> hand, int handValue) {
		System.out.println("Player hand:");
		for(int x = 0; x < hand.size(); x++) {
			System.out.println(hand.get(x).printCard(handValue));
		}
	}
	
	public static void printDealerHand(List <card> hand, int handValue) {
		System.out.println("Dealer hand:");
		System.out.println(hand.get(0).printCard(handValue));
		System.out.println("      One unknown card");
	}
}