package sample;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class DrawingTab {
    private static StackPane stackPane = new StackPane();
    private static int remainingPrizes = Data.prizeCount;
    private static String[] winner = new String[Data.prizeCount];
    private static int i = 0;

    public static StackPane generate() {
        String remainingTxt = "";

        //START Animation BTN
        stackPane.setId("black-background");
        Button startBtn = new Button("Gewinner Auslosen");
        startBtn.setId("drawing-button");

        //Remaining Prizes
        if (remainingPrizes > 0) {
            remainingTxt = "Verbleibende Preise: " + remainingPrizes;
            stackPane.getChildren().add(startBtn);
        } else {
            remainingTxt = "Alle Preise wurden verlost!";
        }
        Label remainingLbl = new Label(remainingTxt);
        remainingLbl.setId("remaining");
        remainingLbl.setTranslateY(350);
        stackPane.getChildren().add(remainingLbl);

        //StartBTN EventHandler
        startBtn.setOnAction(actionEvent -> {
            if (remainingPrizes == Data.prizeCount) {
                Data.pickWinners();
            }
            stackPane = startAnimation(stackPane);
            stackPane.getChildren().remove(remainingLbl);
            remainingPrizes--;
        });
        return stackPane;
    }

    private static StackPane startAnimation(StackPane stackPane) {
        Media media = new Media(new File("LesePreisUIAnimation720p.mp4").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaPlayer.setAutoPlay(true);
        stackPane.getChildren().add(mediaView);

        //DELAY to show Name
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(5100);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                showWinner(stackPane);
            }
        });
        new Thread(sleeper).start();

        //On End of Animation
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaView.setVisible(false);
            mediaPlayer.stop();
            mediaPlayer.dispose();
            stackPane.getChildren().clear();
            generate();
        });

        return stackPane;
    }

    private static void showWinner(StackPane stackPane) {
        winner[i] = Data.winners[i].getFirstName() + " " + Data.winners[i].getLastName() + "\n" + Data.winners[i].getGrade();
        Label winnerLbl = new Label("Gewinner:\n" + winner[i]);
        winnerLbl.setId("winner");
        winnerLbl.setTranslateY(-50);
        winnerLbl.setTranslateX(-70);
        stackPane.getChildren().add(winnerLbl);
        i++;
    }

}

