import javafx.geometry.Orientation;

public class Game {
    /* 7 decks of increasing number of cards, starting from 1 card
     * 
     * Las Vegas Rules (not cumulative). 3 draw, but unlimited
     */

    InteractableDeck[] unsortedDecks;
    InteractableDeck[] solDecks;

    InteractableDeck drawPile;
    InteractableDeck draw;

    InteractableDeck drawNotVisible;

    InteractableDeck hand;

    private double offsetMouseX, offsetMouseY;
    private InteractableDeck tempDeck; //for when card in hand must return to its deck
    public boolean toHand = true;

    public int screenWidth, screenHeight;
    public double resetCountX, resetCountY;

    public boolean gameOver;
    public int resetCount;

    RenderedButton resetButton;
    RenderedButton playAgainButton;

    private final int NUM_DRAW_CARDS = 3; //change

    //Constructor
    public Game(){
        screenWidth = 800; screenHeight = 800;

        unsortedDecks = new InteractableDeck[7]; //initialize arrays
        solDecks = new InteractableDeck[4];

        gameOver = false;
        resetCount = 0;

        initialize();

        offsetMouseX = InteractableDeck.cardWidth / 2;
        offsetMouseY = InteractableDeck.cardHeight / 2;
        
        resetButton = new RenderedButton("RESET", screenWidth - 90, 20, 70, 40);
        resetCountX = screenWidth - 110;
        resetCountY = 90;

        playAgainButton = new RenderedButton("Play Again?", screenWidth/2 -60, screenHeight/2 + 40, 120, 40);
    }

    //IMPORTANT!
    /** Initialize game by creating all decks. NOTE: keeps resetCount as the same as it was before.
     * ALSO: will return an error if arrays are not initialized first.
     */
    public void initialize(){
        Deck.restartFullDeck();

        //solution decks
        double xStartPos = 320, yStartPos = 20;
        double xOffset = 20;

        for(int i = 0; i < solDecks.length; ++i){
            double xPos = xStartPos + ((InteractableDeck.cardWidth + xOffset) * i);
            solDecks[i] = new InteractableDeck(xPos, yStartPos);
            solDecks[i].setOffset(0);
        }

        int cardsLeft = 52;

        //unsorted decks
        xStartPos = 50; yStartPos = 160;

        for(int i = 0; i < unsortedDecks.length; ++i){
            double xPos = xStartPos + ((InteractableDeck.cardWidth + xOffset) * i);
            unsortedDecks[i] = new InteractableDeck(xPos, yStartPos ,i + 1);
            unsortedDecks[i].getLastCard().setVisible(true); //make the last card visible

            cardsLeft -= (i + 1);
        }

        //other decks
        hand = new InteractableDeck(0,0);

        xStartPos = 20; yStartPos = 20;
        xOffset = 20;

        drawPile = new InteractableDeck(xStartPos,yStartPos,cardsLeft); //create draw pile
        drawPile.setOffset(0);

        draw = new InteractableDeck(xStartPos*2 + InteractableDeck.cardWidth, yStartPos);
        draw.setOrientation(Orientation.HORIZONTAL);
        draw.setOffset(40);

        drawNotVisible = new InteractableDeck(draw.x, draw.y);
        drawNotVisible.setOffset(0);
    }

    public void reset(){
        initialize();
        resetCount++;
    }

    //Game mechanics

    /** Check if mouse is in bounds of last card in deck to tranfer to, for all decks */
    public void tryTransferCardsFromHand(double mouseX, double mouseY){
        if(toHand || tempDeck == null){
            return;
        }
        for(InteractableDeck currDeck : unsortedDecks){
            if(currDeck.checkMouseInBounds(mouseX, mouseY)){
                //case if unsorted deck is empty
                if(currDeck.isEmpty() && hand.getFirstCard().getValue() == 13){
                    hand.transferCards(0, currDeck);

                    updateDraw();
                    if(!tempDeck.isEmpty()) tempDeck.getLastCard().setVisible(true);

                    toHand = true;
                    return;
                }
                else if(!currDeck.isEmpty() && hand.getFirstCard().compareSuite(currDeck.getLastCard()) < 0 && 
                        currDeck.getLastCard().getValue() == hand.getFirstCard().getValue() + 1){
                    hand.transferCards(0, currDeck);

                    updateDraw();
                    if(!tempDeck.isEmpty()) tempDeck.getLastCard().setVisible(true);

                    toHand = true;
                    return;
                }
            }
        }
        //for solution decks, hand must be one card
        if(hand.size() != 1){
            hand.transferCards(0, tempDeck);
            toHand = true;
            return;
        }
        for(InteractableDeck currDeck : solDecks){
            if(currDeck.checkMouseInBounds(mouseX, mouseY)){
                //case if solDeck is empty
                if(currDeck.isEmpty() && hand.getFirstCard().getValue() == 1){
                    hand.transferCards(0, currDeck);

                    updateDraw();
                    if(!tempDeck.isEmpty()) tempDeck.getLastCard().setVisible(true);

                    toHand = true;
                    return;
                }
                else if(!currDeck.isEmpty() && hand.getFirstCard().compareSuite(currDeck.getLastCard()) == 0 && 
                        currDeck.getLastCard().getValue() == hand.getFirstCard().getValue() - 1){
                    hand.transferCards(0, currDeck);

                    updateDraw();
                    if(!tempDeck.isEmpty()) tempDeck.getLastCard().setVisible(true);

                    toHand = true;
                    return;
                }
            } 
            //Prevent two solution decks of the same suite.
            //Check if another solution deck already has the same suite. 
            else if(!currDeck.isEmpty() && currDeck.getLastCard().compareSuite(hand.getFirstCard()) == 0){
                break;
            }
        }
        //if card can't be placed, returns to original place
        hand.transferCards(0, tempDeck);
        toHand = true;
        return;
    }

    public void tryTranferCardsToHand(double mouseX, double mouseY){
        if(!toHand){
            return;
        }
        //Check draw
        if(draw.checkMouseInBounds(mouseX, mouseY)){
            draw.transferCards(draw.size() - 1, hand);

            toHand = false;
            tempDeck = draw;
            return;
        }
        for(InteractableDeck currDeck : unsortedDecks){
            for(int i = currDeck.size() - 1; i >=0; --i){ //start from last card, if size is 0 then loop wont activate
                if(currDeck.checkMouseInBounds(mouseX, mouseY, i)){
                    if(currDeck.getCard(i).isVisible()){
                        currDeck.transferCards(i, hand);

                        toHand = false;
                        tempDeck = currDeck;
                        return;
                    }
                }
            }
        }
        //for solution decks, only transfer one card
        for(InteractableDeck currDeck : solDecks){
            if(currDeck.size() <= 0){
                continue; //skip if this deck has no cards
            }
            if(currDeck.checkMouseInBounds(mouseX, mouseY)){
                currDeck.transferCards(currDeck.size() - 1, hand);

                toHand = false;
                tempDeck = currDeck;
                return;
            }
        }
        return;
    }

    public void tryDrawCards(double mouseX, double mouseY){
        if(drawPile.checkMouseInBounds(mouseX, mouseY)){
            if(drawPile.isEmpty()){ //case where drawPile is empty
                draw.transferCards(0, drawNotVisible); //make one pile
                drawNotVisible.setVisibility(false);
                drawNotVisible.transferCardsBackwards(0, drawPile);
            }
            else{
                int index = drawPile.size() - NUM_DRAW_CARDS;
                if(index < 0) index = 0;
                drawPile.transferCardsBackwards(index, draw);

                updateDraw();
            }
        }
    }

    //Game state
    public void checkWinState(){
        for(Deck curr : solDecks){
            if(curr.getLastCard() == null){
                return;
            }
            if(!curr.isSorted(curr.getLastCard().getSuite())){
                return;
            }
        }
        gameOver = true;
    }

    private void updateDraw(){
        //Keep draw deck with 3 cards
        while(draw.size() > NUM_DRAW_CARDS){
            draw.insertCard(0, drawNotVisible, drawNotVisible.size());
        }
        while(!drawNotVisible.isEmpty() && draw.size() < NUM_DRAW_CARDS){
            int index = drawNotVisible.size() - 1;
            if(index < 0) index = 0;
            drawNotVisible.insertCard(index, draw, 0);
        }
        draw.setVisibility(true);
        drawNotVisible.setVisibility(false);
    }

    //Event handling

    public void followMouse(double mouseX, double mouseY){
        hand.x = mouseX - offsetMouseX;
        hand.y = mouseY - offsetMouseY;
    }

    //IMPORTANT!!!
    public void update(){
        checkWinState();
    }
    
    //Nested class, is this a good idea?
    class RenderedButton{
        public double x,y,w,h;
        public String label;

        public RenderedButton(String label, double x, double y, double w, double h){
            this.label = label;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public boolean checkMouseInBounds(double mouseX, double mouseY){
            if(mouseX > x && mouseX < x+w && mouseY > y && mouseY < y+h){
                return true;
            }
            return false;
        }
    }
}
