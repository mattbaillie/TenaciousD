package blackjack;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class blackjackTest extends TestCase {

	public void testNumCards() {
		List<card> deck = blackjack.createDeck();
		
		assertEquals(52, deck.size());
	}
	
	public void testShuffle() {
		List<card> deck = blackjack.createDeck();
		List<card> deckToShuffle = blackjack.createDeck();
		//The above two decks are identical.
		deckToShuffle = blackjack.shuffleDeck(deckToShuffle);
		//Now they aren't!
		
		boolean isEqual = (deck.get(0).printCard(0).equals(deckToShuffle.get(0).printCard(0)));
		boolean isEqual2 = (deck.get(1).printCard(0).equals(deckToShuffle.get(1).printCard(0)));
		boolean decksDifferent = (!isEqual || !isEqual2);
		assertEquals(true, decksDifferent);
	}
	
	// Test split for both player and dealer
	public void testSplit() {
		List<card> hand = new ArrayList<card>();
		card card1 = new card("K", "S");
		card card2 = new card("K", "H");
		hand.add(card1);
		hand.add(card2);
		
		assertEquals(true, blackjack.canSplit(hand));
	}
	
	public void testConsoleInput() {
		// This is necessary to mock input
	    String input = "test";
	    InputStream in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);
	    
	    // Test input
	    Scanner sc = new Scanner(System.in);
        String output = sc.nextLine();

	    assertEquals("test", output);
	}
	
	public void testPrintPlayerHand() {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outStream));
		
		List<card> hand = new ArrayList<card>();
		card card1 = new card("K", "S");
		card card2 = new card("K", "H");
		hand.add(card1);
		hand.add(card2);
		
		blackjack.printPlayerHand(hand, 20);
		
		String consoleOutput = outStream.toString();
		
		boolean outputContains = consoleOutput.contains("HK worth 10");
		
		assertEquals(true, outputContains);
	}
	
	public void testPrintDealerHand() {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outStream));
		
		List<card> hand = new ArrayList<card>();
		card card1 = new card("K", "S");
		card card2 = new card("K", "H");
		hand.add(card1);
		hand.add(card2);
		
		blackjack.printDealerHand(hand, 20);
		
		String consoleOutput = outStream.toString();
		
		boolean outputContains = consoleOutput.contains("One unknown card");
		
		assertEquals(true, outputContains);
	}
	
	public static void testPlayerCanHit() {
		List<card> playerHand = new ArrayList<card>();
		List<card> deck = blackjack.createDeck();
		//Hit
		playerHand.add(deck.get(0));
		deck.remove(0);
		
		assertEquals(1, playerHand.size());
	}
	
	public static void testPlayerCanHitTwice() {
		List<card> playerHand = new ArrayList<card>();
		List<card> deck = blackjack.createDeck();
		//Hit
		playerHand.add(deck.get(0));
		deck.remove(0);
		playerHand.add(deck.get(0));
		deck.remove(0);
		
		assertEquals(2, playerHand.size());
	}
	
	public static void testPlayerCanBust() {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outStream));
		
		List<card> hand = new ArrayList<card>();
		card card1 = new card("K", "S");
		card card2 = new card("K", "H");
		hand.add(card1);
		hand.add(card2);
		
		card card3 = new card("2", "S");
		hand.add(card3);
		//Reevaluate hand value
		int handValue = blackjack.calcHandValue(hand);
		
		//Bust
		if(handValue > 21) {
			System.out.println("You have busted with a value of " + handValue + ".");
		}
		
		String consoleOutput = outStream.toString();
		
		boolean outputContains = consoleOutput.contains("You have busted");
		
		assertEquals(true, outputContains);
	}
	
	public static void testAces() {
		List<card> hand = new ArrayList<card>();
		card card1 = new card("A", "S");
		card card2 = new card("A", "H");
		card card3 = new card("A", "C");
		hand.add(card1);
		hand.add(card2);
		hand.add(card3);
		int val = blackjack.calcHandValue(hand);
		
		assertEquals(13, val);
		
		//Note that the ace of clubs is currently worth 1.
		
		hand.remove(card1);
		hand.remove(card2);
		
		//Now, the ace of clubs is the only remaining card.
		//If it is worth 11, we know that it can be 1 and 11.
		
		int val2 = blackjack.calcHandValue(hand);
		
		assertEquals(11, val2);
	}
	
	public static void testFaces() {
		List<card> hand = new ArrayList<card>();
		card card1 = new card("K", "S");
		card card2 = new card("Q", "H");
		card card3 = new card("J", "C");
		hand.add(card1);
		assertEquals(10, blackjack.calcHandValue(hand));
		hand.remove(card1);
		hand.add(card2);
		assertEquals(10, blackjack.calcHandValue(hand));
		hand.remove(card2);
		hand.add(card3);
		assertEquals(10, blackjack.calcHandValue(hand));
	}
	
	public static void testPlayerHandScoring() {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outStream));
		
		List<card> hand = new ArrayList<card>();
		card card1 = new card("K", "S");
		card card2 = new card("Q", "H");
		hand.add(card1);
		hand.add(card2);
		int handValue = blackjack.calcHandValue(hand);
		System.out.println("Player hand: " + handValue);
		for(int x = 0; x < hand.size(); x++) {
			System.out.println(hand.get(x).printCard(handValue));
		}
		
		String consoleOutput = outStream.toString();
		
		boolean outputContains = consoleOutput.contains("20");
		boolean outputContains1 = consoleOutput.contains("SK");
		boolean outputContains2 = consoleOutput.contains("HQ");
		
		assertEquals(true, outputContains);
		assertEquals(true, outputContains1);
		assertEquals(true, outputContains2);
	}
	
	public static void testDealerHandScoring() {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outStream));
		
		List<card> hand = new ArrayList<card>();
		card card1 = new card("K", "S");
		card card2 = new card("Q", "H");
		hand.add(card1);
		hand.add(card2);
		int handValue = blackjack.calcHandValue(hand);
		System.out.println("Dealer hand: " + handValue);
		for(int x = 0; x < hand.size(); x++) {
			System.out.println(hand.get(x).printCard(handValue));
		}
		
		String consoleOutput = outStream.toString();
		
		boolean outputContains = consoleOutput.contains("20");
		boolean outputContains1 = consoleOutput.contains("SK");
		boolean outputContains2 = consoleOutput.contains("HQ");
		
		assertEquals(true, outputContains);
		assertEquals(true, outputContains1);
		assertEquals(true, outputContains2);
	}
	
	public static void testDealerBlackjackWin() {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outStream));
		
		int dealerHandValue = 21;
		int playerHandValue = 21;
		//Check for wins
		if(dealerHandValue == 21) {
			System.out.println("Dealer blackjack! Dealer wins.");
		} else if(playerHandValue == 21 && dealerHandValue != 21) {
			System.out.println("Player blackjack! Player wins.");
		}
		
		String consoleOutput = outStream.toString();
		
		boolean outputContains = consoleOutput.contains("Dealer wins");
		
		assertEquals(true, outputContains);
	}
	
	public static void testPlayerBlackjackWin() {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outStream));
		
		int dealerHandValue = 20;
		int playerHandValue = 21;
		//Check for wins
		if(dealerHandValue == 21) {
			System.out.println("Dealer blackjack! Dealer wins.");
		} else if(playerHandValue == 21 && dealerHandValue != 21) {
			System.out.println("Player blackjack! Player wins.");
		}
		
		String consoleOutput = outStream.toString();
		
		boolean outputContains = consoleOutput.contains("Player wins");
		
		assertEquals(true, outputContains);
	}
	
	public static void testDealerCanHit() {
		int dealerHandValue = 0;
		int numAces = 0;
		List<card> dealerHand = new ArrayList<card>();
		List<card> deck = blackjack.createDeck();
		//Dealer hit logic
		while(dealerHandValue < 16 || (numAces >= 1 && dealerHandValue == 17)) {
			numAces = 0;
			
			card newCard = deck.get(0);
			dealerHand.add(deck.get(0));
			deck.remove(0);
			
			//Reevaluate hand value
			dealerHandValue = blackjack.calcHandValue(dealerHand);
			System.out.println("Dealer pulls " + newCard.name + newCard.suit + " for a new hand value of " + dealerHandValue);
			
			//Determine number of aces
			for(int x = 0; x < dealerHand.size(); x++) {
				if(dealerHand.get(x).name == "A") {
					numAces++;
				}
			}
		}
		
		boolean dealerDidHit = (dealerHandValue > 0);
		
		assertEquals(true, dealerDidHit);
	}
	
	public static void testDealerHitsOnSoft17() {
		int dealerHandValue = 17;
		int numAces = 1;
		List<card> dealerHand = new ArrayList<card>();
		List<card> deck = blackjack.createDeck();
		//Dealer hit logic
		while(dealerHandValue < 16 || (numAces >= 1 && dealerHandValue == 17)) {
			numAces = 0;
			
			card newCard = deck.get(0);
			dealerHand.add(deck.get(0));
			deck.remove(0);
			
			//Reevaluate hand value
			dealerHandValue = blackjack.calcHandValue(dealerHand);
			System.out.println("Dealer pulls " + newCard.name + newCard.suit + " for a new hand value of " + dealerHandValue);
			
			//Determine number of aces
			for(int x = 0; x < dealerHand.size(); x++) {
				if(dealerHand.get(x).name == "A") {
					numAces++;
				}
			}
		}
		
		//This means that the dealer took a card
		boolean dealerDidHit = (dealerHandValue != 17);
		
		assertEquals(true, dealerDidHit);
	}
	
	public static void testPlayerWinOnDealerBust() {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outStream));
		
		int dealerHandValue = 23;
		//Decide winner
		if(dealerHandValue <= 21) {
			//Compare to player hand
//					if(playingWithSplit) {
//						if(dealerHandValue >= playerSplitValue) {
//							System.out.println("Dealer wins with a score of " + dealerHandValue + " versus player score of " + playerSplitValue);
//						} else {
//							System.out.println("Player wins with a score of " + playerSplitValue + " versus dealer score of " + dealerHandValue);
//						}
//					} else {
//						if(dealerHandValue >= playerHandValue) {
//							System.out.println("Dealer wins with a score of " + dealerHandValue + " versus player score of " + playerHandValue);
//						} else {
//							System.out.println("Player wins with a score of " + playerHandValue + " versus dealer score of " + dealerHandValue);
//						}
//					}
		} else {
			//Dealer bust
			System.out.println("Dealer bust! Player wins.");
		}
		
		String consoleOutput = outStream.toString();
		
		boolean outputContains = consoleOutput.contains("Player wins");
		
		assertEquals(true, outputContains);
	}
	
	public static void testPlayerWinWhenMoreThanDealer() {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outStream));
		
		int dealerHandValue = 19;
		int playerHandValue = 20;
		//Decide winner
		if(dealerHandValue <= 21) {
			//Compare to player hand
//			if(playingWithSplit) {
//				if(dealerHandValue >= playerSplitValue) {
//					System.out.println("Dealer wins with a score of " + dealerHandValue + " versus player score of " + playerSplitValue);
//				} else {
//					System.out.println("Player wins with a score of " + playerSplitValue + " versus dealer score of " + dealerHandValue);
//				}
//			} else {
				if(dealerHandValue >= playerHandValue) {
					System.out.println("Dealer wins with a score of " + dealerHandValue + " versus player score of " + playerHandValue);
				} else {
					System.out.println("Player wins with a score of " + playerHandValue + " versus dealer score of " + dealerHandValue);
				}
//			}
		} else {
			//Dealer bust
			System.out.println("Dealer bust! Player wins.");
		}
		
		String consoleOutput = outStream.toString();
		
		boolean outputContains = consoleOutput.contains("Player wins");
		
		assertEquals(true, outputContains);
	}
	
	public static void testDealerWinWhenEqualToPlayer() {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outStream));
		
		int dealerHandValue = 20;
		int playerHandValue = 20;
		//Decide winner
		if(dealerHandValue <= 21) {
			//Compare to player hand
//			if(playingWithSplit) {
//				if(dealerHandValue >= playerSplitValue) {
//					System.out.println("Dealer wins with a score of " + dealerHandValue + " versus player score of " + playerSplitValue);
//				} else {
//					System.out.println("Player wins with a score of " + playerSplitValue + " versus dealer score of " + dealerHandValue);
//				}
//			} else {
				if(dealerHandValue >= playerHandValue) {
					System.out.println("Dealer wins with a score of " + dealerHandValue + " versus player score of " + playerHandValue);
				} else {
					System.out.println("Player wins with a score of " + playerHandValue + " versus dealer score of " + dealerHandValue);
				}
//			}
		} else {
			//Dealer bust
			System.out.println("Dealer bust! Player wins.");
		}
		
		String consoleOutput = outStream.toString();
		
		boolean outputContains = consoleOutput.contains("Dealer wins");
		
		assertEquals(true, outputContains);
	}
}
