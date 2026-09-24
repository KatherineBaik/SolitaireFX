import javafx.geometry.Orientation;

public class InteractableDeck extends Deck{
    public double x, y; //x y of first card

    public static double cardWidth = 75, cardHeight = 105;
    public double cardOffset = 30;

    private Orientation orientation = Orientation.VERTICAL;

    public InteractableDeck(double x, double y){
        super();

        this.x = x;
        this.y = y;
    }

    public InteractableDeck(double x, double y, int numCards){
        super(numCards);

        this.x = x;
        this.y = y;
    }
    //Getters
    public double getOffset(){
        return cardOffset;
    }

    //Setters
    public void setOrientation(Orientation orientation){
        this.orientation = orientation;
    }

    public void setOffset(double offset){
        cardOffset = offset;
    }

    //Functions
    public boolean checkMouseInBounds(double mouseX, double mouseY, int cardIndex){
        double[] bounds = generateBounds(cardIndex);

        if(mouseX > bounds[0] && mouseX < bounds[2] && mouseY > bounds[1] && mouseY < bounds[3]){
            return true;
        }
        return false;
    }

    /** NOTE: Checks last card */
    public boolean checkMouseInBounds(double mouseX, double mouseY){ //last card
        double[] bounds;
        if(size() == 0){
            bounds = generateBounds(0);
        }
        else{
            bounds = generateBounds(size() - 1);
        }

        if(mouseX > bounds[0] && mouseX < bounds[2] && mouseY > bounds[1] && mouseY < bounds[3]){
            return true;
        }
        return false;
    }

    /** NOTE: does not check if the card actually exists */
    public double[] getXY(int cardIndex){
        double[] coords = new double[2];

        if(orientation == Orientation.VERTICAL){
            double frontY = y + (cardOffset * cardIndex);

            coords[0] = x;
            coords[1] = frontY;
        }
        else{
            double frontX = x + (cardOffset * cardIndex);

            coords[0] = frontX;
            coords[1] = y;
        }
        
        return coords;
    }

    //Helper methods
    private double[] generateBounds(int cardIndex){
        double[] bounds = new double[4];

        if(orientation == Orientation.VERTICAL){
            double frontY = y + (cardOffset * cardIndex);

            bounds[0] = x;
            bounds[1] = frontY;
            bounds[2] = x + cardWidth;
            bounds[3] = frontY + cardHeight;
        }
        else{
            double frontX = x + (cardOffset * cardIndex);

            bounds[0] = frontX;
            bounds[1] = y;
            bounds[2] = frontX + cardWidth;
            bounds[3] = y + cardHeight;
        }

        return bounds;
    }
}
