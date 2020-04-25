package sample;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class DrawingTab {
        public static StackPane stackPane = new StackPane();


        public static StackPane generate() {
            stackPane.setId("black-background");
            Button startBtn = new Button("Gewinner Auslosen");
            startBtn.setId("drawing-button");
            stackPane.getChildren().add(startBtn);
            //StartBTN EventHandler
            startBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    stackPane = startAnimation(stackPane);
                }
            });
            return stackPane;
        }

        public static StackPane startAnimation(StackPane stackPane){
            Media media = new Media(new File("LesePreisUIAnimation720p.mp4").toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaPlayer.setAutoPlay(true);
            stackPane.getChildren().add(mediaView);

            //On End of Animation
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaView.setVisible(false);
                mediaPlayer.stop();
                mediaPlayer.dispose();
                generate();
            });

            //DELAY to show Name
            Task<Void> sleeper = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        Thread.sleep(5000);
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

            return stackPane;
        }


        public static void showWinner(StackPane stackPane) {
            Data.pickWinners();
            String winnerName = "JEFF";

            Label winner = new Label("Gewinner:\n"+ winnerName.toUpperCase());
            winner.setId("winner");
            winner.setTranslateY(-50);
            winner.setTranslateX(-50);
            stackPane.getChildren().add(winner);
        }

}

