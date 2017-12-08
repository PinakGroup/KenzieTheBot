package com.kps.kenziethebot.controller;

import com.jfoenix.controls.JFXRippler;
import com.kps.kenziethebot.view.ViewFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    /**
     * Controller for splash animation.
     */

    private final String LOADING_SCREEN_PATH = getClass().getResource("../view/assets/media/loading_screen.mp4").toString();

    @FXML
    public AnchorPane rootPane;

    @FXML
    private MediaView loadingMedia;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        new SplashScreenService().start();


        Media media = new Media(LOADING_SCREEN_PATH);

        MediaPlayer mediaPlayer = new MediaPlayer(media);

        loadingMedia.setMediaPlayer(mediaPlayer);
        loadingMedia.setSmooth(true);
        loadingMedia.setPreserveRatio(true);

        JFXRippler rippler = new JFXRippler(loadingMedia);
        rippler.setRipplerFill(Paint.valueOf("#404040"));
        rootPane.getChildren().add(rippler);

        mediaPlayer.play();
    }


    class SplashScreenService extends Thread {

        private ViewFactory viewFactory;

        @Override
        public void run() {

            // Processing on the main ui Thread :
            try {

                Thread.sleep(10_500); // Should be 10_500 milliseconds.
                Platform.runLater(
                        () -> {

                            viewFactory = new ViewFactory();
                            Stage mainStage = new Stage();

                            mainStage.setTitle("Main Layout");
                            mainStage.initStyle(StageStyle.UNDECORATED);

                            Scene scene = viewFactory.getMainScene();

                            mainStage.setScene(scene);
                            mainStage.getIcons().add(viewFactory.getDefaultIcon());
                            viewFactory.initMovablePlayer(mainStage);
                            mainStage.show();

                            rootPane.getScene().getWindow().hide();
                        });

            } catch (InterruptedException ie) {

                System.out.println("[X] Exception while loading the Main Layout Screen.");
                System.out.println(ie.getMessage());
                ie.printStackTrace();
            }
        }
    }
}