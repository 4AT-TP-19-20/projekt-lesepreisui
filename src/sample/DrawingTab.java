package sample;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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

class DrawingTab extends StackPane {
    private final Label lbl_remainingPrizes;
    private final Label lbl_winner;
    private final Button btn_start;
    private final MediaPlayer mediaPlayer;
    private final MediaView mediaView;

    private Contestant[] winners;
    private int remainingPrizes;
    private int currentWinner;

    DrawingTab() {
        //Start button
        btn_start = new Button("Gewinner Auslosen");
        btn_start.setId("drawing-button");
        btn_start.setOnAction(this::start);
        this.getChildren().add(btn_start);

        //Remaining prizes
        lbl_remainingPrizes = new Label();
        lbl_remainingPrizes.setId("remaining");
        lbl_remainingPrizes.setTranslateY(350);
        this.getChildren().add(lbl_remainingPrizes);

        //Media Player and Viewer
        mediaPlayer = new MediaPlayer(new Media(new File("LesePreisUIAnimation720p.mp4").toURI().toString()));
        mediaView = new MediaView(mediaPlayer);
        mediaView.setVisible(false);
        this.getChildren().add(mediaView);

        lbl_winner = new Label();
        lbl_winner.setId("winner");
        lbl_winner.setTranslateY(-50);
        lbl_winner.setTranslateX(-70);
        lbl_winner.setVisible(false);
        this.getChildren().add(lbl_winner);

        remainingPrizes = Data.settings.getPrizeCount();

        Data.settings.prizeCountProperty().addListener((observable, oldValue, newValue) -> {
            remainingPrizes = (int) newValue;
            currentWinner = 0;
            prizeCountUpdate();
        });

        this.setId("black-background");
        prizeCountUpdate();
    }

    private void start(ActionEvent event) {
        if (remainingPrizes == Data.settings.getPrizeCount()) {
            try {
                pickWinners();
            } catch (IllegalArgumentException ex) {
                lbl_remainingPrizes.setText(ex.getMessage());
                return;
            }
        }
        startAnimation();
        lbl_remainingPrizes.setVisible(false);
        remainingPrizes--;
    }

    private void prizeCountUpdate() {
        String remainingPrizesText;
        if (remainingPrizes > 0) {
            remainingPrizesText = "Verbleibende Preise: " + remainingPrizes;
        } else {
            remainingPrizesText = "Alle Preise wurden verlost!";
        }
        lbl_remainingPrizes.setText(remainingPrizesText);
        btn_start.setVisible(remainingPrizes != 0);
    }

    private void startAnimation() {
        mediaView.setVisible(true);
        mediaPlayer.play();

        //DELAY to show Name
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(5100);
                } catch (InterruptedException ignore) {}
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> showWinner());
        new Thread(sleeper).start();

        //On End of Animation
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaView.setVisible(false);
            mediaPlayer.stop();
            lbl_winner.setVisible(false);
            lbl_remainingPrizes.setVisible(true);
            prizeCountUpdate();
        });
    }

    private void showWinner() {
        lbl_winner.setText("Gewinner:\n" + winners[currentWinner].getFirstName() + " " + winners[currentWinner].getLastName() + "\n" + winners[currentWinner].getGrade());
        lbl_winner.setVisible(true);
        currentWinner++;
    }

    private void pickWinners() {
        int totalPoints = 0;
        ArrayList<Map.Entry<Contestant, Integer>> qualifiedContestants = new ArrayList<>();

        for(Contestant contestant : Data.contestants) {
            if(contestant.getBookCount() >= Data.settings.getMinBookCount()) {
                qualifiedContestants.add(new AbstractMap.SimpleEntry<>(contestant, 0));
                totalPoints += contestant.getPoints();
            }
        }

        if(Data.settings.getPrizeCount() >= qualifiedContestants.size() * 3) {
            throw new IllegalArgumentException("Jeder gewinnt! (Zu viele Preise)");
        }

        Random random = new Random();
        winners = new Contestant[Data.settings.getPrizeCount()];
        for(int i = 0; i < Data.settings.getPrizeCount(); i++) {
            int winningEntry = random.nextInt(totalPoints);
            int currentPos = 0;
            for(Map.Entry<Contestant, Integer> contestant : qualifiedContestants) {
                if(winningEntry < currentPos + contestant.getKey().getPoints()) {
                    if(contestant.getValue() < 3) {
                        winners[i] = contestant.getKey();
                        contestant.setValue(contestant.getValue() + 1);
                    }
                    else {
                        i--;
                    }
                    break;
                }
                else {
                    currentPos += contestant.getKey().getPoints();
                }
            }
        }
    }
}
