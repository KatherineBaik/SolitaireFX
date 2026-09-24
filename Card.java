public class Card {
    //Enums
    public static enum Suite{
        HEART, DIAMOND, CLOVER, CLUB
    }

    //Members
    private Suite suite;
    private int value; //1 ace, 11 jack, 12 queen, 13 king
    private boolean visible;

    //Constructor
    public Card(Suite suite, int value){
        if(value < 1 || value > 13){
            throw new IllegalArgumentException("Invalid card value!");
        }
        this.suite = suite;
        this.value = value;

        visible = false;
    }

    //Getters
    public Suite getSuite(){
        return suite;
    }

    public int getValue(){
        return value;
    }

    public boolean isVisible(){
        return visible;
    }

    //Setters
    public void setVisible(boolean visible){
        this.visible = visible;
    }

    //Functions

    /** 
     * Returns the difference between this card's value and the other card's value.
     * If this card is higher value, returns positive. If lower, then return negative.
     * @param other The card to compare with
     */
    public int compareValue(Card other){
        return value - other.value;
    }

    /** If this card has the same suite as the other card, return 0. 
    If they have the same color, return 1. If they have different colors, return -1. */
    public int compareSuite(Card other){
        if(suite == other.suite){
            return 0;
        }
        else if((suite == Suite.HEART || suite == Suite.DIAMOND) && (other.suite == Suite.HEART || other.suite == Suite.DIAMOND)){
            return 1;
        }
        else if((suite == Suite.CLOVER || suite == Suite.CLUB) && (other.suite == Suite.CLOVER || other.suite == Suite.CLUB)){
            return 1;
        }
        else{
            return -1;
        }
    }

    //Debug and Testing
    @Override
    public String toString(){
        String str = "";
        switch (value) {
            case 1:
                str += "A";
                break;
            case 11:
                str += "J";
                break;
            case 12:
                str += "Q";
                break;
            case 13:
                str += "K";
                break;
            default:
                str += value;
                break;
        }

        str += " of " + suite.name();

        return str;
    }

}
