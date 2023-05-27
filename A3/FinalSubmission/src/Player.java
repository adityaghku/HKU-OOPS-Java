import java.util.ArrayList;

/**
* Player class which handles the dealer and the user
*/
public class Player {
	
    /**
   * The amount of money the player has, redundant
   * variable for the dealer
   */
	
	private int wallet;
	
    /**
   * The three cards the player can have in one round
   */
	private ArrayList<Card> cards = new ArrayList<Card>();
	
    /**
   * Default constructor which initialises the wallet to 100
   */
	public Player() {
		this.wallet = 100;
		
	}

    /**
   * Getter for wallet
   * @return int wallet
   */
	public int getWallet() { return this.wallet; }
	
	
    /**
   * Getter for cards
   * @return ArrayList<Card> cards
   */
	public ArrayList<Card> getCards() { return this.cards;}
	
    /**
   * Adds a card to the current hand of the player during the deal
   * @param card card dealt to the player
   */
	public void addCard(Card card) {
		this.cards.add(card);
	}
	
    /**
   * Reinitialises cards for a new round
   */
	public void clearCards() {
		this.cards = new ArrayList<Card>();
	}
	
	
    /**
   * Counts the number of special cards in the players hand
   * @return int count the number of special cards
   */
	public int countSpecialCards() {
		int count = 0;
				
		for (int i = 0; i < 3; i++) {
			if (this.cards.get(i).special()) {
				count++;
			}
		}
		
		return count;
	}
	
    /**
   * Sums the value of non-special cards in the player hands and gets the
   * remainder mod 10
   * @return int the score of the player
   */
	public int cardsSum() {
		
		int s = 0;
		
		for (int i = 0; i < 3; i++) {
			if (!this.cards.get(i).special()) {
				s += cards.get(i).getValue();
			}
		}
		
		return s%10;
	}
	
    /**
   * Updates the wallet depending on if the player loses
   * @param int amount which tells us how much money is to be added or deducted
   * @param boolean win which tells us whether to add or subtract the amount
   */
	public void updateWallet(int amount, boolean win) {
		if (win) {
			this.wallet += amount;
		}
		
		else {
			this.wallet -= amount;
		}
	}
	
    /**
   * Gets the path of the image as a string for a particular card
   * @param int i which tells us which card to get the path for (of the three
   * 	in the players hand)
   * @return String the relative path to the image
   */
	public String getImagePath(int i) {
		
		return String.format( "Images/card_%s%s.gif",  this.cards.get(i).getSuit(), this.cards.get(i).getValue());
	}
}
