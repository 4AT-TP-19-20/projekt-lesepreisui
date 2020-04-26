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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class DrawingTab {
    private static StackPane stackPane = new StackPane();
    private static int remainingPrizes = SettingsTab.getPrizeCount();
    private static Contestant[] winners;
    private static int currentWinner = 0;

    public static StackPane generate() {
        String remainingPricesText;

        //START Animation BTN
        stackPane.setId("black-background");
        Button startBtn = new Button("Gewinner Auslosen");
        startBtn.setId("drawing-button");

        //Remaining Prizes
        if (remainingPrizes > 0) {
            remainingPricesText = "Verbleibende Preise: " + remainingPrizes;
            stackPane.getChildren().add(startBtn);
        } else {
            remainingPricesText = "Alle Preise wurden verlost!";
        }
        Label lbl_remainingPrices = new Label(remainingPricesText);
        lbl_remainingPrices.setId("remaining");
        lbl_remainingPrices.setTranslateY(350);
        stackPane.getChildren().add(lbl_remainingPrices);

        //StartBTN EventHandler
        startBtn.setOnAction(actionEvent -> {
            if (remainingPrizes == SettingsTab.getPrizeCount()) {
                pickWinners();
            }
            stackPane = startAnimation(stackPane);
            stackPane.getChildren().remove(lbl_remainingPrices);
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
                } catch (InterruptedException ignore) {}
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
        String winnerText = winners[currentWinner].getFirstName() + " " + winners[currentWinner].getLastName() + "\n" + winners[currentWinner].getGrade();
        Label lbl_winner = new Label("Gewinner:\n" + winnerText);
        lbl_winner.setId("winner");
        lbl_winner.setTranslateY(-50);
        lbl_winner.setTranslateX(-70);
        stackPane.getChildren().add(lbl_winner);
        currentWinner++;
    }

    private static void pickWinners() {
        int totalPoints = 0;
        ArrayList<Map.Entry<Contestant, Integer>> qualifiedContestants = new ArrayList<>();

        for(Contestant contestant : Data.contestants) {
            if(contestant.getBookCount() >= SettingsTab.getMinBookCount()) {
                qualifiedContestants.add(new AbstractMap.SimpleEntry<>(contestant, 0));
                totalPoints += contestant.getPoints();
            }
        }

        Random random = new Random();
        winners = new Contestant[SettingsTab.getPrizeCount()];
        for(int i = 0; i < SettingsTab.getPrizeCount(); i++) {
            int winningEntry = random.nextInt(totalPoints);
            int currentPos = 0;
            for(Map.Entry<Contestant, Integer> contestant : qualifiedContestants) {
                if(winningEntry < currentPos + contestant.getKey().getPoints()) {
                    if(contestant.getValue() < 3) {
                        winners[i] = contestant.getKey();
                        contestant.setValue(contestant.getValue() + 1);
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else {
                    currentPos += contestant.getKey().getPoints();
                }
            }
        }
    }
}

