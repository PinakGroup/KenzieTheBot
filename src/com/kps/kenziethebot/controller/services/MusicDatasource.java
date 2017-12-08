package com.kps.kenziethebot.controller.services;

import com.kps.kenziethebot.controller.Updatable;
import com.kps.kenziethebot.model.TTS;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

public class MusicDatasource implements Updatable {

    /**
     * This class controls the retrieval of the music *.mp3 files and
     * their path from he database.
     */
    private static MediaPlayer mediaPlayer;
    public static boolean isPlaying = false;
    public static List<MusicMetaData> musicMetaDataList = new ArrayList<>();

    @Override
    public void setText(String text) {


    }

    public static void playShuffled(String path) {


        new Thread(
                () -> {

                    Media media = new Media(processPath(path));
                    mediaPlayer = new MediaPlayer(media);


                    if (!isPlaying) {


                        System.out.println("{||} Now playing : " + MusicMetaData.instance.getName());
                        //    Platform.runLater(()->{

//                            FXMLLoader loader = new FXMLLoader();
//                            try {
//                                Parent root = loader.load(ViewFactory.class.getResource("MainLayout.fxml"));
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            MainController controller = loader.getController();
//                            controller.musicLabel.setText("xyz");


                        new Thread(() -> {
                            Platform.runLater(() -> {
                                //  MainController.mainControllerInstance.setMusicLabel("xyz");
                                // MainController.updateLabel("new label");

//                                        FXMLLoader fxmlLoader = new FXMLLoader();
//                                        fxmlLoader.setLocation(ViewFactory.class.getResource("MainLayout.fxml"));
//
//                                        try {
//                                            fxmlLoader.load();
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                        Updatable updatable = fxmlLoader.getController();
//                                        System.out.println("updated");
//                                        updatable.setText("xyz");
//                                       Updatable updatable;

                            });
                        }).start();


                        // });

                        mediaPlayer.play();
                        isPlaying = true;
                    } else {

                        TTS.speak("music is already playing, sir");
                    }


                }).start();
    }

    public static void resumeMusic() {

        try {

            if (!isPlaying) {

                TTS.speak("resuming music playback ,sir");
                Thread.sleep(4000);
                mediaPlayer.play();

                isPlaying = true;
            } else {

                TTS.speak("there is no music playing, sir");
            }
        } catch (InterruptedException ie) {

            System.out.println("{X} Exception while resuming music.");
            System.out.println(ie.getMessage());
        }


    }

    public static void pauseMusic() {

        try {

            if (isPlaying) {

                mediaPlayer.pause();
                Thread.sleep(2000);
                TTS.speak("music playback paused,sir");

                isPlaying = false;
            } else {

                TTS.speak("there is no music playing, sir");
            }
        } catch (InterruptedException ie) {

            System.out.println("{X} Exception while pausing music.");
            System.out.println(ie.getMessage());
        }
    }

    public static boolean stopMusic() {

        if (isPlaying) {

            mediaPlayer.stop();

            isPlaying = false;
            return true;
        } else {

            return false;
        }

    }

    //||++Music Control functionality++||

    public static void nextSong() throws InterruptedException {

        if (isPlaying) {

            if (MusicMetaData.instance.get_id() >= 0 && MusicMetaData.instance.get_id() < musicMetaDataList.size() - 1) {
                stopMusic();

                MusicMetaData.instance = musicMetaDataList.get(MusicMetaData.instance.get_id() + 1);

                playShuffled(MusicMetaData.instance.getPath());

            } else {

                mediaPlayer.setVolume(0.25);

                TTS.speak("end of the playlist was reached, no more songs to play");
                Thread.sleep(5000);

                mediaPlayer.setVolume(1);
            }

        } else {

            TTS.speak("there is no music playing, sir");
        }

    }

    public static void previousSong() throws InterruptedException {

        if (isPlaying) {

            if (MusicMetaData.instance.get_id() > 0 && MusicMetaData.instance.get_id() < musicMetaDataList.size() - 1) {
                stopMusic();

                MusicMetaData.instance = musicMetaDataList.get(MusicMetaData.instance.get_id() - 1);

                playShuffled(MusicMetaData.instance.getPath());

            } else {

                mediaPlayer.setVolume(0.25);

                TTS.speak("reached at the start of the playlist");
                Thread.sleep(4000);

                mediaPlayer.setVolume(1);
            }

        } else {

            TTS.speak("there is no music playing, sir");
        }


    }

    public static void increaseVolume() {

        if (isPlaying) {

            mediaPlayer.setVolume(mediaPlayer.getVolume() + 0.5);
            System.out.println("{+} Volume increased to : " + mediaPlayer.getVolume() * 100 + "%");
            TTS.speak("volume level increased to : " + mediaPlayer.getVolume() * 100 + "%");
        } else {

            TTS.speak("there is no music playing, sir");
        }
    }

    public static void decreaseVolume() {

        if (isPlaying) {

            mediaPlayer.setVolume(mediaPlayer.getVolume() - 0.5);
            System.out.println("{+} Volume decreased to : " + mediaPlayer.getVolume() * 100 + "%");
            TTS.speak("volume level decreased to : " + mediaPlayer.getVolume() * 100 + "%");
        } else {

            TTS.speak("there is no music playing, sir");
        }
    }

    private static String processPath(String path) {

        return MusicDatasource.class.getResource(path).toString();
    }


}
