package edu.mobileappdevii.exercises.blackjack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlackJack extends AppCompatActivity {
    private static final int ACE = 1;
    private static final int CONFIRM_DIALOG = 1;
    private static final int OK_DIALOG = 2;
    private static final int DECK_MINIMUM = 10;
    private static final int BLACK_JACK = 21;
    private Button newGameButton;
    private Button hitButton;
    private Button stayButton;
    private LinearLayout dealersHandLinearLayout;
    private LinearLayout playersHandLinearLayout;
    private ArrayList<Card> deck;
    private ArrayList<Card> dealersHand;
    private ArrayList<Card> playersHand;
    private int currentHand;
    private TableLayout handsTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_jack);

        handsTableLayout = (TableLayout) findViewById(R.id.handsTableLayout);
        newGameButton = (Button) findViewById(R.id.newGameButton);
        hitButton = (Button) findViewById(R.id.hitButton);
        stayButton = (Button) findViewById(R.id.stayButton);

        newGameButton.setOnClickListener(newGameButtonListener);
        hitButton.setOnClickListener(hitButtonListener);
        stayButton.setOnClickListener(stayButtonListener);
        deck = new ArrayList<>();

        startNewGame();
    }

    // Deals two cards each to the dealer and player at the beginning of each new hand
    private void dealNewHand() {
        if (deck.size() > 4) {
            // Draw two cards for Dealer
            dealersHand = new ArrayList<>();
            dealersHand.add(deck.remove(0));
            dealersHand.add(deck.remove(0));

            // Draw two cards for Player
            playersHand = new ArrayList<>();
            playersHand.add(deck.remove(0));
            playersHand.add(deck.remove(0));

            // Update the view
            // Inflate the hand_view.xml file
            LayoutInflater inflater =
                    (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View newHandView = inflater.inflate(R.layout.hand_view, null);
            TextView handTextView = (TextView) newHandView.findViewById(R.id.handTextView);
            handTextView.setText("Hand " + currentHand);

            // Deal two cards to the dealer
            dealersHandLinearLayout = (LinearLayout) newHandView.findViewById(R.id.dealersHandLinearLayout);
            TextView cardTextView = new TextView(this);
            cardTextView.setPadding(10, 0, 10, 0);
            cardTextView.setBackgroundResource(R.drawable.card_face_up);
            cardTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                    this.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.text_size));
            cardTextView.setText(dealersHand.get(0).toString());
            dealersHandLinearLayout.addView(cardTextView);
            cardTextView = new TextView(this);
            cardTextView.setPadding(10, 0, 10, 0);
            cardTextView.setBackgroundResource(R.drawable.card_face_down);
            dealersHandLinearLayout.addView(cardTextView);

            // Deal two cards to the player
            playersHandLinearLayout = (LinearLayout) newHandView.findViewById(R.id.playersHandLinearLayout);
            cardTextView = new TextView(this);
            cardTextView.setPadding(10, 0, 10, 0);
            cardTextView.setBackgroundResource(R.drawable.card_face_up);
            cardTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                    this.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.text_size));
            cardTextView.setText(playersHand.get(0).toString());
            playersHandLinearLayout.addView(cardTextView);
            cardTextView = new TextView(this);
            cardTextView.setPadding(10, 0, 10, 0);
            cardTextView.setBackgroundResource(R.drawable.card_face_up);
            cardTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                    this.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.text_size));
            cardTextView.setText(playersHand.get(1).toString());
            playersHandLinearLayout.addView(cardTextView);

            handsTableLayout.addView(newHandView);
        }
        else {
            showAlertDialog("Out of cards", "Out of cards", OK_DIALOG);
        }
    }

    // Starts a new game
    private void startNewGame() {
        currentHand = 1;

        // Clear the view
        handsTableLayout.removeAllViews();

        if (deck.size() < DECK_MINIMUM) {
            // Initialize the deck of cards
            initializeDeck();
        }

        // Deal the dealer and player new hands
        dealNewHand();
    }

    // Initializes a new deck with shuffled cards
    private void initializeDeck() {
        deck = new ArrayList<>();
        List<String> suits = Arrays.asList("s", "h", "c", "d");

        // Load the deck with the numeric cards
        for (int value = 2; value < 11; value++) {
            for (String suit : suits) {
                deck.add(new Card(String.valueOf(value), suit));
            }
        }

        // Load the deck with the face cards
        for (String suit : suits) {
            deck.add(new Card("J", suit));
            deck.add(new Card("Q", suit));
            deck.add(new Card("K", suit));
            deck.add(new Card("A", suit));
        }

        // Shuffle the deck
        Collections.shuffle(deck);
    }

    // Deals a new card to the dealer or the player
    private void dealCard(View v, ArrayList<Card> hand, LinearLayout handLinearLayout) {
        if (deck.size() > 0) {
            Card card = deck.remove(0);
            hand.add(card);
            TextView cardTextView = new TextView(v.getContext());
            cardTextView.setPadding(10, 0, 10, 0);
            cardTextView.setBackgroundResource(R.drawable.card_face_up);
            cardTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                    this.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.text_size));
            cardTextView.setText(card.toString());
            handLinearLayout.addView(cardTextView);
        }
        else {
            showAlertDialog("Out of cards", "Out of cards", OK_DIALOG);
        }
    }

    // Reveals the dealer's second card
    private void revealDealerCard() {
        // Reveal the dealer's second card
        TextView cardTextView = (TextView) dealersHandLinearLayout.getChildAt(2);
        cardTextView.setBackgroundResource(R.drawable.card_face_up);
        cardTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                this.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.text_size));
        cardTextView.setText(dealersHand.get(1).toString());
    }

    // Counts all cards in the dealer's or player's hand
    private int countCards(ArrayList<Card> cards) {
        int value;
        int total = 0;
        int totalAces = 0;

        // First count all the non-aces
        for (Card card : cards) {
            value = card.getNumericalValue();

            // Aces are a special case in BlackJack and can hold the value 1 or 11
            if (value == ACE) {
                // Keep track of the ace
                totalAces++;
            }
            else {
                total += value;
            }
        }

        // Did we go bust?
        if (total > BLACK_JACK) {
            // Simply return the total
            return total;
        }

        // We did not go bust
        // Did we find any aces that can help improve our total?
        if (totalAces > 0) {
            for (int i = 1; i <= totalAces; i++) {
                if (total < 11) {
                    total += 11;
                }
                else {
                    total += 1;
                }
            }
        }
        return total;
    }

    // Convenience method for showing AlertDialogs in the game
    private void showAlertDialog(String dialog_title, String dialog_message, int dialogType) {
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setCancelable(false);

        // Show a confirmation dialog
        if (dialogType == CONFIRM_DIALOG) {
            // Add the buttons
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User clicked confirm button to start new game
                    startNewGame();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User cancelled the dialog and does not wish to proceed with the action,
                    // so do nothing
                }
            });
        }
        else {
            // Show an OK dialog
            // Add the buttons
            if (dialog_title.equals("Out of cards")) {
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // We ran out of cards, start a new game
                        startNewGame();
                    }
                });
            }
            else {
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // The hand ended in either win, lose or push, deal new hand
                        currentHand++;
                        dealNewHand();
                    }
                });
            }
        }

        // Set the AlertDialog's title and message
        builder.setMessage(dialog_message)
                .setTitle(dialog_title);

        // Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Handles the New Game button click
    public OnClickListener newGameButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            showAlertDialog("Start New Game", "End the current game?", CONFIRM_DIALOG);
        }
    };

    // Handles the Hit-Me button click
    public OnClickListener hitButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // Check if the deck has cards
            if (deck.size() > 0) {
                // Deal a card from the deck
                dealCard(v, playersHand, playersHandLinearLayout);

                // Count the number of cards in the player's hand
                int playerTotal = countCards(playersHand);

                // Does player have BlackJack?
                if (playerTotal == BLACK_JACK) {
                    // Reveal dealer's second card
                    revealDealerCard();

                    // Count cards in dealer's hand
                    int dealerTotal = countCards(dealersHand);

                    // While count is less than or equal to 16, deal a card and recount hand
                    while (dealerTotal <= 16) {
                        dealCard(v, dealersHand, dealersHandLinearLayout);
                        dealerTotal = countCards(dealersHand);
                    }

                    // If dealer's count does not equal BlackJack, player wins
                    if (dealerTotal != BLACK_JACK) {
                        showAlertDialog("BLACKJACK!", "Player wins hand!", OK_DIALOG);
                    }
                    // If count is equal to player's count, push (tie)
                    else {
                        showAlertDialog("Push", "Hand ended in a push", OK_DIALOG);
                    }
                }
                // Did the player go bust?
                else if (playerTotal > BLACK_JACK) {
                    showAlertDialog("Dealer Wins", "Dealer wins hand", OK_DIALOG);
                }
            }
            else {
                showAlertDialog("Out of cards", "Out of cards", OK_DIALOG);
            }
        }
    };

    // Handles the Stay button click
    public OnClickListener stayButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // Check if deck has cards
            if (deck.size() > 0) {
                // Reveal dealer's second card
                revealDealerCard();

                // Count cards in dealer's hand
                int dealerTotal = countCards(dealersHand);

                // While count is less than or equal to 16, deal a card and recount hand
                while (dealerTotal <= 16) {
                    dealCard(v, dealersHand, dealersHandLinearLayout);
                    dealerTotal = countCards(dealersHand);
                }

                // Count the cards in player's hand
                int playerTotal = countCards(playersHand);

                // If dealer's count exceeds BlackJack or is less than player's count, player wins
                if (dealerTotal > BLACK_JACK || dealerTotal < playerTotal) {
                    showAlertDialog("Player Wins!", "Player wins hand!", OK_DIALOG);
                }
                // If count is greater than player's count, dealer wins
                else if (dealerTotal > playerTotal) {
                    showAlertDialog("Dealer Wins", "Dealer wins hand", OK_DIALOG);
                }
                // If count is equal to player's count, push (tie)
                else {
                    // REPLACE WITH DIALOG BOX
                    showAlertDialog("Push", "Hand ended in a push", OK_DIALOG);
                }
            }
            else {
                showAlertDialog("Out of cards", "Out of cards", OK_DIALOG);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_black_jack, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
