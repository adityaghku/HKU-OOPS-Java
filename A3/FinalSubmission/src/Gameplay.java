
public class Gameplay {

    /**
   * The dealer in the game
   */
	private Player dealer;
	
    /**
   * The user of the game (opponent of the dealer)
   */
	private Player user;
	
    /**
   * The deck of cards for any round
   */
	private Deck deck;
	
    /**
   * The bet placed by the user
   */
	private int bet;
	
    /**
   * Check how many cards have been replaced by the user
   */
	private int maxMoves;
	
	
    /**
   * Default constructor for Gameplay which creates a player and user,
   * and sets maxMoves to 0
   */
	public Gameplay() {
		this.dealer = new Player();
		this.user = new Player();
		this.maxMoves = 0;
	}

    /**
   * Getter for user
   * @return Player user
   */
	public Player getUser() {
		return this.user;
	}
	
    /**
   * Getter for dealer
   * @return Player user
   */
	public Player getDealer() {
		return this.dealer;
	}
	
    /**
   * Setter for bet
   * @param int bet which is the value to set the current bet to
   */
	public void setBet(int bet) {this.bet = bet;}
	
    /**
   * Getter for bet
   * @return int bet
   */
	public int getBet() {return this.bet;}
	
    /**
   * Getter for maxMoves
   * @return int maxMoves
   */
	public int getMaxMoves() {return this.maxMoves; }
	
	
    /**
   * Setter for maxMoves
   * @param int maxMoves which is the value to set the current maxMoves to
   */
	public void setMaxMoves(int maxMoves) {this.maxMoves = maxMoves; }
	
    /**
   * This method gives the dealer and user three new cards to play the next round
   */
	public void dealCards() {
		
		this.user.clearCards();
		this.dealer.clearCards();
		
		this.deck = new Deck();
		this.deck.shuffleDeck();
		
		for (int i = 0; i < 3; i++) {
			this.user.addCard(this.deck.draw());
			this.dealer.addCard(this.deck.draw());
		}
	}
	
    /**
   * This method checks who wins based on the three rules of the game
   */
	public boolean userWins() {
		if (user.countSpecialCards() > dealer.countSpecialCards()) {
			return true;
		}

		if (user.countSpecialCards() < dealer.countSpecialCards()) {
			return false;
		}

		if (user.cardsSum() > dealer.cardsSum()) {
			return true;
		}

		if (user.cardsSum() < dealer.cardsSum()) {
			return false;
		}

		return false;
	}
	
    /**
   * When the replace button is used, this method replaces the 
   * card in hand with a new card
   * @param int i which is the index of the card to be replaced
   */
	public void replaceUserCards(int i) {
		this.user.getCards().set(i, this.deck.draw());
	}
	
    /**
   * This method checks whether the input is valid or not (if it is a
   * positive integer)
   * @param String strNum which is the text input by the user
   * @return boolean which tells us whether the input is valid or not
   */
	public boolean validBet(String strNum) {
		
	    try {
	        int i = Integer.parseInt(strNum);
	        
	        if (i <= 0) {
	        	return false;
	        }
	    }
	    
	    catch (NumberFormatException nfe) {
	        return false;
	    }
	    
	    return true;
	}

}
