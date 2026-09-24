
/* import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
 */
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Renderer {
    private GraphicsContext gc;
    private int canvasWidth,canvasHeight;

    private Image spriteSheet;

    public Renderer(GraphicsContext gc, int canvasWidth, int canvasHeight){
        this.gc = gc;
        gc.setFont(new Font(20));

        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        
        spriteSheet = new Image(getClass().getResourceAsStream("/cards.png"));
    }

    void renderDeck(InteractableDeck deck){
        double[] coords = deck.getXY(0);
        
        for(int i = 0; i < deck.size(); ++i){
            coords = deck.getXY(i);
            renderCard(coords, InteractableDeck.cardWidth, InteractableDeck.cardHeight, deck.getCard(i));
        }
        
    }

    void renderDeckPosition(InteractableDeck deck){
        double[] coords = deck.getXY(0);
        double offset = 2;
        //draw the position of deck
        gc.setStroke(Color.BLACK);
        gc.strokeRect(coords[0] + offset, coords[1] + offset, 
            InteractableDeck.cardWidth - offset*2, InteractableDeck.cardHeight - offset*2);
    }

    void renderCard(double[] coords, double w, double h, Card card){
        double sourceX = 0;
        double sourceY = h * (card.getValue() - 1);
        if(card.isVisible()){
            switch (card.getSuite()) {
                case HEART:
                    sourceX = 0;
                    break;
                case DIAMOND:
                    sourceX = w + 1;
                    break;
                case CLUB:
                    sourceX = (w*2) + 1;
                    break;
                case CLOVER:
                    sourceX = (w*3) + 1;
                default:
                    break;
            }
            gc.drawImage(spriteSheet, sourceX, sourceY, w, h, coords[0], coords[1], w, h);
        }
        else{
            gc.setFill(Color.DARKRED);
            gc.fillRect(coords[0], coords[1], w, h);
        }
    }

    public void renderButton(Game.RenderedButton button){
        gc.setFill(Color.TAN);
        gc.setStroke(Color.BLACK);

        gc.fillRect(button.x, button.y, button.w, button.h);
        gc.strokeRect(button.x, button.y, button.w, button.h);

        gc.setFill(Color.BLACK);
        gc.fillText(button.label, button.x + 10, button.y + button.h - 10);
    }

    public void renderResetCount(int number, double x, double y){
        gc.setFill(Color.BLACK);
        gc.fillText("Reset Count:\n\t" + Integer.toString(number), x, y, 100);
    }

    public void renderGameOver(){
        gc.setFill(Color.PINK);
        gc.setStroke(Color.BLACK);
        
        double w = 200, h = 100;
        double x = canvasWidth/2 - w/2;
        double y = canvasHeight/2 - h/2;

        gc.fillRect(x, y, w, h);
        gc.strokeRect(x, y, w, h);
        
        gc.setFill(Color.BLACK);
        gc.fillText("You did it!", x + w/2 - 50, y +h/2 + 10);
    }

    void clearCanvas(){
        gc.setFill(Color.WHEAT);
        gc.fillRect(0,0,canvasWidth,canvasHeight);
    }
}
