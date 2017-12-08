package com.kps.kenziethebot.controller;


import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.kps.kenziethebot.controller.services.MusicDatasource;
import com.kps.kenziethebot.controller.services.MusicMetaData;
import com.kps.kenziethebot.model.ResponseProcessing;
import com.kps.kenziethebot.model.TTS;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sourceforge.javaflacencoder.FLACFileWriter;

import javax.sound.sampled.LineUnavailableException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements GSpeechResponseListener, Initializable {

    /**
     * This class is the main controller of the whole project.
     */
    public static final String API_KEY = "$$ENTER YOUR API KEY HERE$$"; // API_KEY can be generated via google developer console.
    private static Main mainInstance = new Main();
    private final String DEFAULT_BACKGROUND_PATH = getClass().getResource("../view/assets/media/default_background.mp4").toString();
    private static final String DEFAULT_BEEP_PATH = MainController.class.getResource("../view/assets/media/default_beep.mp3").toString();
    private final Microphone mic = new Microphone(FLACFileWriter.FLAC);
    private int clickCount = 0;
    private static MediaPlayer default_player;
    private GSpeechDuplex gSpeechDuplex = new GSpeechDuplex(API_KEY);


    @FXML
    private MediaView backMedia;

    @FXML
    private JFXTextField recTextView;

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton minimizeButton;

    @FXML
    private JFXButton startButton;

    @FXML
    private JFXHamburger hamAnimation;

    @FXML
    private VBox drawer;

    @FXML
    private JFXButton menu; //Deprecated.

    @FXML
    private JFXButton userIdNavButton;

    @FXML
    private JFXButton cloudNavButton;

    @FXML
    private JFXButton aboutNavButton;

    @FXML
    private JFXButton exitNavButton;

    @FXML
    private StackPane backgroundStackPane;

    @FXML
    private HBox musicHbox;

    @FXML
    private ImageView musicImage;

    @FXML
    private Label musicLabel;


    //++NAVIGATION DRAWER IMPLEMENTATION++

    @FXML //Deprecated method.
    private void drawerAction() {

        TranslateTransition openNav = new TranslateTransition(
                new Duration(500), drawer);
        openNav.setToX(0);

        TranslateTransition closeNav = new TranslateTransition(
                new Duration(500), drawer);
        menu.setOnAction(
                (ActionEvent evt) -> {

                    if (drawer.getTranslateX() != 0) {

                        openNav.play();
                    } else {

                        closeNav.setToX(-(drawer.getWidth()));
                        closeNav.play();
                    }
                });
    }

    @FXML
    private void userIdNavButtonClicked(ActionEvent event) {


        System.out.println("{*} UserID button clicked.");

        mainInstance.openLink("https://github.com/karanpratapsingh");

    }

    @FXML
    private void cloudNavButtonClicked(ActionEvent event) {


        System.out.println("{*} Cloud button clicked.");

    }

    @FXML
    private void aboutNavButtonClicked(ActionEvent actionEvent) {

        System.out.println("{*} About button clicked.");

        BoxBlur boxBlur = new BoxBlur(2, 2, 3);

        JFXDialogLayout aboutDialogLayout = new JFXDialogLayout();

        aboutDialogLayout.setHeading(new Text("About"));
        aboutDialogLayout.setBody(new Text("Made by : Karan Pratap Singh\n\nLicenced to : @KPS"));

        aboutDialogLayout.setOpacity(1);
        aboutDialogLayout.setStyle("-fx-background-color: rgba(250,250,250,0.15)");
        aboutDialogLayout.setStyle("-fx-background-radius: 20%");
        aboutDialogLayout.applyCss();


        JFXDialog aboutDialog = new JFXDialog(backgroundStackPane,
                aboutDialogLayout,
                JFXDialog.DialogTransition.TOP);
        backgroundStackPane.setVisible(true);


        JFXButton aboutDialogButton = new JFXButton("ok");

        aboutDialog.setOnDialogOpened(
                (aboutDialogOpened) -> {

                    System.out.println("{^} About dialog box opened.");
                    aboutDialogButton.setOnAction((event) -> {

                        aboutDialog.close();
                        backgroundStackPane.setVisible(false);
                    });

                    aboutDialogLayout.setActions(aboutDialogButton);

                    root.setStyle("-fx-border-color: black");
                    backMedia.setEffect(boxBlur);
                    recTextView.setEffect(boxBlur);
                    startButton.setEffect(boxBlur);
                });

        aboutDialog.setOnDialogClosed(
                (aboutDialogClosed) -> {

                    System.out.println("{^} About dialog box closed.");
                    backMedia.setEffect(null);
                    recTextView.setEffect(null);
                    startButton.setEffect(null);
                });


        aboutDialog.show();
    }

    @FXML
    private void exitNavButtonClicked(ActionEvent event) {

        System.out.println("[*] Exiting application via exit button.");
        Platform.exit();
    }


    // ++STANDARD BUTTONS++


    @FXML
    private void minimizeProgram(ActionEvent event) {

        System.out.println("{*} Program Minimized.");
        (
                (Stage)
                        (
                                (Button) event.getSource()).getScene().getWindow()
        ).setIconified(true);
    }

    @FXML
    private void maximizeProgram(ActionEvent event) {

        clickCount++;

        if (clickCount % 2 == 0) {

            System.out.println("{*} Going Windowed Mode.");
            (
                    (Stage)
                            (
                                    (Button) event.getSource()).getScene().getWindow()
            ).setFullScreen(false);

        } else {

            System.out.println("{*} Going FullScreen Mode.");
            (
                    (Stage)
                            (
                                    (Button) event.getSource()).getScene().getWindow()
            ).setFullScreen(true);
        }


    }

    @FXML
    void closeProgram(ActionEvent event) {

        stopSpeechRecognition();
        Platform.exit();
    }


    //++THE START BUTTON++


    @FXML
    private void startButton(ActionEvent actionEvent) {


        new Thread(
                () -> {

                    default_player = new MediaPlayer(new Media(DEFAULT_BEEP_PATH));
                    default_player.play();
                    TTS.speak("Hello , i am kenzie your personal assistant");
                }
        ).start();

        startRec();
    }

    //++THE METHOD THAT STARTs IT ALL++
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        musicHbox.setOnMouseClicked(
                (mouseEvent) -> {


                    TranslateTransition openNav = new TranslateTransition(new Duration(500), musicHbox);
                    openNav.setToX(0);

                    TranslateTransition closeNav = new TranslateTransition(new Duration(500), musicHbox);

                    if (musicHbox.getTranslateX() != 0) {

                        openNav.play();

                    } else {

                        new Thread(
                                () -> {
                                    Platform.runLater(() -> {


                                        if (MusicDatasource.isPlaying) {

                                            musicLabel.setText("\t    Now Playing : \n" + " " + MusicMetaData.instance.getName());
                                        } else {

                                            musicLabel.setText("  No music currently playing.");
                                        }


                                    });
                                }
                        ).start();


                        closeNav.setToX(-(musicHbox.getWidth()));
                        closeNav.play();

                    }

                });

        backgroundStackPane.setVisible(false);
        backgroundStackPane.setOpacity(0.8);
        backgroundStackPane.setOnMouseClicked(
                (mouseEvent) -> {
                    // false on close ||Write this||
                    backgroundStackPane.setVisible(false);
                });


        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamAnimation);
        transition.setRate(-1);

        hamAnimation.addEventHandler(MouseEvent.MOUSE_CLICKED,
                (event) -> {

                    transition.setRate(transition.getRate() * (-1));
                    transition.play();

                    TranslateTransition openNav = new TranslateTransition(new Duration(500), drawer);
                    openNav.setToX(0);

                    TranslateTransition closeNav = new TranslateTransition(new Duration(500), drawer);

                    if (drawer.getTranslateX() != 0) {

                        System.out.println("{+} Navigation drawer opened.");
                        openNav.play();

                    } else {

                        System.out.println("{-} Navigation drawer closed.");
                        closeNav.setToX(-(drawer.getWidth()));
                        closeNav.play();

                    }
                });


        recTextView.setStyle("-fx-text-inner-color: #ffffff");
        recTextView.setEditable(false);

        Thread backgroundThread = new Thread(
                () -> {

                    Media media = new Media(DEFAULT_BACKGROUND_PATH);
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setRate(1.5f);

                    backMedia.setSmooth(true);
                    backMedia.setPreserveRatio(false);

                    backMedia.fitWidthProperty().bind(root.widthProperty());
                    backMedia.fitHeightProperty().bind(root.heightProperty());


                    backMedia.setMediaPlayer(mediaPlayer);

                    mediaPlayer.play();

                });
        backgroundThread.setName("Background Animation Thread");
        backgroundThread.start();
    }


    private void startRec() {

        System.out.println("{*} Speech Recognition Service Started.");

        gSpeechDuplex.setLanguage("en");

        gSpeechDuplex.addResponseListener(new GSpeechResponseListener() {

            String old_text = "";

            @Override
            public void onResponse(GoogleResponse googleResponse) {

                String output = "";
                output = googleResponse.getResponse();

                if (googleResponse.getResponse() == null) {

                    this.old_text = recTextView.getText();

                    if (this.old_text.contains("(")) {

                        this.old_text = this.old_text.substring(0, this.old_text.indexOf('('));
                    }
                    System.out.println("Paragraph Line Added");

                    this.old_text = (recTextView.getText() + "\n");
                    this.old_text = this.old_text.replace(")", "").replace("( "
                            , "");

                    recTextView.setText(this.old_text);
                    return;
                }
                if (output.contains("(")) {

                    output = output.substring(0, output.indexOf('('));
                }
                if (!googleResponse.getOtherPossibleResponses().isEmpty()) {

                    output = output + " (" + (String) googleResponse.getOtherPossibleResponses().get(0) + ")";
                }

                System.out.println("||-> " + output);
                recTextView.setText("");

                recTextView.appendText(this.old_text);
                recTextView.appendText(output);

                if (googleResponse.isFinalResponse()) {

                    try {

                        System.out.println("|Final Response|-> " + output
                                + " :: Confidence level : "
                                + Float.parseFloat(googleResponse.getConfidence()) * 100 + "%");

                        ResponseProcessing.processResponse(output);
                    } catch (InterruptedException ie) {

                        System.out.println("{X} Exception while processing response.");
                        System.out.println(ie.getMessage());
                    }

                }
            }
        });
        startSpeechRecognition();
    }


    private void startSpeechRecognition() {

        new Thread(
                () -> {
                    try {

                        gSpeechDuplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());

                    } catch (InterruptedException | LineUnavailableException exception) {

                        System.out.println("Exception caused : " + exception.getMessage());
                    }
                }).start();
    }

    private void stopSpeechRecognition() {

        mic.close();
    }

    @Override
    public void onResponse(GoogleResponse googleResponse) {

        // Overridden method, does nothing.
    }
}
