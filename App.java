import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application{
    //Application stuff
    Renderer renderer;
    Timer timer;

    private double mouseX, mouseY;

    //Game stuff
    Game game;

    @Override
    public void start(Stage stage){
        stage.setTitle("SolitaireFX");

        game = new Game();

        mouseX = 0; mouseY = 0;

        Canvas canvas = new Canvas(game.screenWidth, game.screenHeight);
        renderer = new Renderer(canvas.getGraphicsContext2D(), game.screenWidth, game.screenHeight);

        timer = new Timer();

        VBox layout = new VBox(canvas);
        Scene mainScene = new Scene(layout);

        //handle mouse events
        mainScene.setOnMouseMoved(this::handleMouseMoved);
        mainScene.setOnMousePressed(this::handleMousePressed);
        mainScene.setOnMouseDragged(this::handleMouseMoved);

        mainScene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e){
                //System.out.println("Mouse released");
                game.tryTransferCardsFromHand(mouseX, mouseY);
            }
        });

        stage.setScene(mainScene);
        stage.show();

        timer.start();
    }

    //Event Handlers
    void handleMousePressed(MouseEvent e){
        game.tryDrawCards(mouseX, mouseY);
        if(game.resetButton.checkMouseInBounds(mouseX, mouseY)){
            game.reset();
        }

        game.tryTranferCardsToHand(mouseX, mouseY);

        if(game.gameOver){
            if(game.playAgainButton.checkMouseInBounds(mouseX, mouseY)){
                game = new Game();
            }
        }

        //System.out.println("Mouse pressed");
    }

    void handleMouseMoved(MouseEvent e){
        mouseX = e.getSceneX();
        mouseY = e.getSceneY();
    }

    //Rendering
    void renderAll(){
        renderer.clearCanvas();

        for(int i = 0; i < game.unsortedDecks.length; ++i){
            renderer.renderDeckPosition(game.unsortedDecks[i]);
            renderer.renderDeck(game.unsortedDecks[i]);
        }
        for(int i = 0; i < game.solDecks.length; ++i){
            renderer.renderDeckPosition(game.solDecks[i]);
            renderer.renderDeck(game.solDecks[i]);
        }
        renderer.renderDeckPosition(game.drawPile);
        renderer.renderDeck(game.drawPile);

        renderer.renderDeckPosition(game.drawNotVisible);
        renderer.renderDeck(game.drawNotVisible);
        renderer.renderDeck(game.draw);

        renderer.renderDeck(game.hand);
        
        renderer.renderButton(game.resetButton);
        renderer.renderResetCount(game.resetCount, game.resetCountX, game.resetCountY);
    }

    void renderGameOver(){
        renderer.renderGameOver();
        renderer.renderButton(game.playAgainButton);
    }

    class Timer extends AnimationTimer{
        @Override
        public void handle(long t){
            //Game loop
            if(game.gameOver){
                renderGameOver();
            }
            else{
                game.followMouse(mouseX, mouseY);
                game.update();
                renderAll();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
