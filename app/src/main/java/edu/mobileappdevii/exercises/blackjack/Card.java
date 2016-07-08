package edu.mobileappdevii.exercises.blackjack;

/**
 * The Card class represents a card in BlackJack
 */
public class Card {
    protected String value; // Holds the face value of the card
    protected String suit; // Holds the suit of the card

    // Creates a new card
    public Card(String value, String suit) {
        this.value = value;
        this.suit = suit;
    }

    // Returns the face value of the card
    public String getValue() { return this.value; }

    // Gets the numerical value for the given card
    // Face cards (Jack, Queen, King) have the value 10
    // The Ace can hold a value of 1 or 11, depending on
    // which is most beneficial to the player
    // All other cards hold the value that is printed on them
    public int getNumericalValue() {
        switch (value) {
            case "K":
            case "Q":
            case "J":
                // Face cards have a value of 10
                return 10;
            case "A":
                // We use the value 1 as a flag to indicate the presence of the Ace
                return 1;
            default:
                // All other cards have the value that is printed on them
                return Integer.parseInt(value);
        }
    }

    // Returns the suit of the card
    public String getSuit() {
        return this.suit;
    }

    // Returns a string representation of the card for displaying in the game
    @Override
    public String toString() {
        return (getValue() + getSuit());
    }
}
