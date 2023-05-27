/**
* This class is used to handle the card object for the game
*/

public class Card {

    /**
   * The suit of the character where 11, 12, 13 are J, Q, K
   */
	private int suit;
	
    /**
   * The value of the card from 1 to 14 (inclusive)
   */
	private int value;

    /**
   * default constructor
   */
	public Card(int suit, int value) {

		this.suit = suit;
		this.value = value;

	}

    /**
   * Getter for suit
   * @return int the suit
   */
	public int getSuit() {
		return this.suit;
	}

    /**
   * Getter for value
   * @return int the value
   */
	public int getValue() {
		return this.value;
	}

    /**
   * Checks if the value of the card is > 10
   * @return boolean whether the card is special
   */
	public boolean special() {
		return this.value > 10;
	}

}
