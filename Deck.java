import java.util.ArrayList;

public class Deck {
    //Static Members
    private static ArrayList<Card> fullDeck; //note: 52 cards
    private static boolean fullDeckInitialized = false;

    //Members
    private ArrayList<Card> cardList;

    //Constructors
    public Deck(){
        //Constructs an empty deck

        cardList = new ArrayList<>();
        initializeDeck();
    }

    public Deck(int numCards){
        //Constructs a deck with random cards up to the number specified
        cardList = new ArrayList<>();
        initializeDeck();

        if(numCards > fullDeck.size()){ //check if there is not enough cards in the fullDeck
            numCards = fullDeck.size();
            //System.out.println("Not enough cards from full deck, adding " + numCards + " cards.");
        }

        for(int counter = 0; counter < numCards; ++counter){
            int randomIndex = (int)(Math.random() * fullDeck.size()); //get a random card from the fullDeck
            cardList.add(fullDeck.get(randomIndex)); //add card to this deck
            fullDeck.remove(randomIndex); //remove card from the fullDeck
        }
    }

    //Setters
    /** Set visibility for all cards in deck */
    public void setVisibility(boolean visibility){
        for(Card c : cardList){
            c.setVisible(visibility);
        }
    }

    //Getters
    public ArrayList<Card> getCardList(){
        return cardList;
    }

    public int size(){
        return cardList.size();
    }

    public Card getFirstCard(){
        if(size() == 0){
            return null;
        }
        return cardList.getFirst();
    }

    public Card getLastCard(){
        if(size() == 0){
            return null;
        }
        return cardList.getLast();
    }

    /** NOTE: bottom card is first card, front card is last card */
    public Card getCard(int index){
        if(index < 0 || index >= cardList.size()){
            throw new ArrayIndexOutOfBoundsException("Index is out of bounds for this deck!");
        }
        return cardList.get(index);
    }

    //Methods

    /** Transfers cards from this deck to target, starting from the index to its last card.
     *  Cards are tranferred starting with the backmost card.
     */
    public void transferCards(int index, Deck targetDeck){
        if(targetDeck.equals(this)){
            throw new IllegalArgumentException("Cannot transfer cards to self!");
        }
        if(isEmpty()){
            return; //Do nothing
        }
        else if(index < 0 || index >= cardList.size()){
            throw new ArrayIndexOutOfBoundsException("Index is out of bounds for this deck!");
        }

        while(index < cardList.size()){
            targetDeck.getCardList().add(getCard(index)); //get card and append to target deck
            cardList.remove(index); //remove card from this deck
        }
    }

    /** Transfers cards from this deck to target, from the index to its last card.
     *  Cards are tranferred starting with the frontmost card.
     */
    public void transferCardsBackwards(int index, Deck targetDeck){
        if(targetDeck.equals(this)){
            throw new IllegalArgumentException("Cannot transfer cards to self!");
        }
        if(isEmpty()){
            return; //Do nothing
        }
        else if(index < 0 || index >= cardList.size()){
            throw new ArrayIndexOutOfBoundsException("Index is out of bounds for this deck!");
        }

        int lastIndex = cardList.size() - 1;
        while(lastIndex >= index){
            targetDeck.getCardList().add(getCard(lastIndex)); //get card and append to target deck
            cardList.remove(lastIndex); //remove card from this deck

            lastIndex = cardList.size() - 1;
        }
    }

    /** Inserts a card from this deck to target. */
    public void insertCard(int sourceIndex, Deck targetDeck, int targetIndex){
        if(targetDeck.equals(this)){
            throw new IllegalArgumentException("Cannot transfer cards to self!");
        }
        if(isEmpty()){
            return; //Do nothing
        }
        else if(sourceIndex < 0 || sourceIndex >= cardList.size()){
            throw new ArrayIndexOutOfBoundsException("Index is out of bounds for source deck!");
        }
        else if(targetIndex < 0 || targetIndex > targetDeck.cardList.size()){
            throw new ArrayIndexOutOfBoundsException("Index is out of bounds for target deck!");
        }

        targetDeck.getCardList().add(targetIndex, getCard(sourceIndex));
        cardList.remove(sourceIndex);
    }

    public boolean isEmpty(){
        return cardList.isEmpty();
    }

    public boolean isSorted(Card.Suite suite){
        if(cardList.size() != 13){
            return false; //must be a full deck
        }
        for(int currVal = 1; currVal <= 13; ++currVal){ //check each card for...
            if(cardList.get(currVal - 1).getSuite() != suite){ //suite
                return false;
            }
            if(cardList.get(currVal - 1).getValue() != currVal){ //sequential order
                return false;
            }
        }
        return true;
    }
    
    public static void restartFullDeck(){
        fullDeckInitialized = false;
        initializeDeck();
    }

    //Helper methods
    private static void initializeDeck(){
        //Note: no jokers
        if(fullDeckInitialized){
            return; //do nothing if already initialized
        }
        fullDeck = new ArrayList<>();
        for(int currVal = 1; currVal <= 13; ++currVal){
            for(Card.Suite currSuite: Card.Suite.values()){
                fullDeck.add(new Card(currSuite, currVal));
            }
        }
        fullDeckInitialized = true;
    }

    //Debug and Testing
    @Override
    public String toString(){
        String str = "{";
        for(Card currCard : cardList){
            str += currCard.toString() + ", ";
        }
        str += "}";
        return str;
    }
}
