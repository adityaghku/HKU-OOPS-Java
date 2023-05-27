import java.util.Collections;
import java.util.ArrayList;

/**
* This class handles an ArrayList of Cards which serves as
* the deck for the game to be played
*/

public class Deck {
	
    /**
   * The ArrayList of Cards on which all the operations are done
   */
	private ArrayList<Card> deck = new ArrayList<Card>();

	
    /**
   * Default constructor which initialises 52 standard playing cards
   */
	public Deck() {

		for (int i = 1; i < 5; i++) {
			for (int j = 1; j < 14; j++) {
				this.deck.add(new Card(i, j));
			}
		}

	}

    /**
   * Removes the top card of the deck and also returns the card to be used
   * @return Card the top card of the deck
   */
	public Card draw() {
		
		Card drawnCard = this.deck.remove(0);
		return drawnCard;
	}

    /**
   * Randomises the order of the ArrayList
   */
	public void shuffleDeck() {
		Collections.shuffle(this.deck);
	}
}
