package sample;

import javafx.concurrent.Task;
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
    private int remainingPrizes;
    private Contestant[] winners;
    private int currentWinner;
    private Label lbl_remainingPrices;
    private Label lbl_winner;
    private Button btn_start;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    DrawingTab() {
        remainingPrizes = Data.settings.getPrizeCount();
        String remainingPricesText;
        currentWinner = 0;

        //BTN START Animation
        this.setId("black-background");
        btn_start = new Button("Gewinner Auslosen");
        btn_start.setId("drawing-button");

        //Remaining Prizes
        if (remainingPrizes > 0) {
            remainingPricesText = "Verbleibende Preise: " + remainingPrizes;
            this.getChildren().add(btn_start);
        } else {
            remainingPricesText = "Alle Preise wurden verlost!";
        }
        lbl_remainingPrices = new Label(remainingPricesText);
        lbl_remainingPrices.setId("remaining");
        lbl_remainingPrices.setTranslateY(350);
        this.getChildren().add(lbl_remainingPrices);

        //btn_start EventHandler
        btn_start.setOnAction(actionEvent -> {
            if (remainingPrizes == Data.settings.getPrizeCount()) {
                pickWinners();
            }
            startAnimation();
            lbl_remainingPrices.setVisible(false);
            remainingPrizes--;
        });

        //Media Player and Viewer initialisation
        mediaPlayer = new MediaPlayer(new Media(new File("LesePreisUIAnimation720p.mp4").toURI().toString()));
        mediaView = new MediaView(mediaPlayer);
        mediaView.setVisible(false);
        this.getChildren().add(mediaView);

        //Label winner initialisation
        lbl_winner = new Label();
        lbl_winner.setId("winner");
        lbl_winner.setTranslateY(-50);
        lbl_winner.setTranslateX(-70);
        lbl_winner.setVisible(false);
        this.getChildren().add(lbl_winner);
    }

    private void startAnimation() {
        mediaView.setVisible(true);
        mediaPlayer.play();

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
        sleeper.setOnSucceeded(event -> showWinner());
        new Thread(sleeper).start();

        //On End of Animation
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaView.setVisible(false);
            mediaPlayer.stop();
            lbl_winner.setVisible(false);
            lbl_remainingPrices.setVisible(true);
            if (remainingPrizes > 0) {
                lbl_remainingPrices.setText("Verbleibende Preise: " + remainingPrizes);
            } else {
                lbl_remainingPrices.setText("Alle Preise wurden verlost!");
                btn_start.setVisible(false);
            }
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

