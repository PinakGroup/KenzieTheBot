package com.kps.kenziethebot.model;

import com.kps.kenziethebot.controller.services.MusicDatasource;
import com.kps.kenziethebot.controller.services.MusicMetaData;
import com.kps.kenziethebot.controller.services.WeatherService;
import javafx.application.Platform;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;

public class ResponseProcessing {

    /**
     * This class processes any response from the user.
     */

    private static String response = null;
    public static String referenceResponse = null;
    private static int adminPermission = 0;


    public static void processResponse(String finalOutput) throws InterruptedException {

        referenceResponse = finalOutput.toLowerCase().trim();

        Thread responseProcessingThread = new Thread(
                () -> {

                    try {

                        Connection connection = DataSource.getInstance().getCentralConnection();
                        Statement statement = connection.createStatement();

                        if (finalOutput.toLowerCase().trim().contains("play my music")
                                || finalOutput.toLowerCase().trim().contains("play my playlist")
                                || finalOutput.toLowerCase().trim().contains("shuffle my playlist")
                                || finalOutput.toLowerCase().trim().contains("play my playlist shuffled")
                                || finalOutput.toLowerCase().trim().contains("how about some music")
                                || finalOutput.toLowerCase().trim().contains("resume the music")
                                || finalOutput.toLowerCase().trim().contains("resume music")
                                || finalOutput.toLowerCase().trim().contains("pause the music")
                                || finalOutput.toLowerCase().trim().contains("pause music")
                                || finalOutput.toLowerCase().trim().contains("stop the music")
                                || finalOutput.toLowerCase().trim().contains("stop music")
                                || finalOutput.toLowerCase().trim().contains("next track")
                                || finalOutput.toLowerCase().trim().contains("previous track")
                                || finalOutput.toLowerCase().trim().contains("turn up the volume")
                                || finalOutput.toLowerCase().trim().contains("increase the volume")
                                || finalOutput.toLowerCase().trim().contains("increase volume")
                                || finalOutput.toLowerCase().trim().contains("turn down the volume")
                                || finalOutput.toLowerCase().trim().contains("decrease the volume")
                                || finalOutput.toLowerCase().trim().contains("decrease volume")) {


                            if (finalOutput.toLowerCase().trim().contains("shuffle")
                                    || finalOutput.toLowerCase().trim().contains("shuffled")
                                    || finalOutput.toLowerCase().trim().contains("shuffle my playlist")
                                    || finalOutput.toLowerCase().trim().contains("how about some music")) {


                                String shuffledMusicQuery = "SELECT * FROM playlist";
                                ResultSet shuffledMusicResultSet = statement.executeQuery(shuffledMusicQuery);

                                while (shuffledMusicResultSet.next()) {

                                    MusicDatasource.musicMetaDataList.add(

                                            MusicMetaData.instance = new MusicMetaData(
                                                    shuffledMusicResultSet.getInt("_id"),
                                                    shuffledMusicResultSet.getString("name"),
                                                    shuffledMusicResultSet.getString("path")
                                            ));

                                }
                                MusicMetaData.instance = MusicDatasource.musicMetaDataList.get(
                                     new Random().nextInt(18)
                                );
                               
                                TTS.speak("playing music from your playlist, sir");
                                Thread.sleep(4000);

                                MusicDatasource.playShuffled(MusicMetaData.instance.getPath());

                            } else if (finalOutput.toLowerCase().trim().contains("resume the music")
                                    || finalOutput.toLowerCase().trim().contains("resume music")) {

                                MusicDatasource.resumeMusic();

                            } else if (finalOutput.toLowerCase().trim().contains("pause the music")
                                    || finalOutput.toLowerCase().trim().contains("pause music")) {


                                MusicDatasource.pauseMusic();

                            } else if (finalOutput.toLowerCase().trim().contains("stop the music")
                                    || finalOutput.toLowerCase().trim().contains("stop music")) {

                                if (MusicDatasource.stopMusic()) {

                                    TTS.speak("music playback stopped,sir");
                                } else {

                                    TTS.speak("there is no music playing, sir");
                                }

                            } else if (finalOutput.toLowerCase().trim().contains("next track")) {

                                MusicDatasource.nextSong();

                            } else if (finalOutput.toLowerCase().trim().contains("previous track")) {

                                MusicDatasource.previousSong();
                            } else if (finalOutput.toLowerCase().trim().contains("turn up the volume")
                                    || finalOutput.toLowerCase().trim().contains("increase the volume")
                                    || finalOutput.toLowerCase().trim().contains("increase volume")) {

                                MusicDatasource.increaseVolume();
                            } else if (finalOutput.toLowerCase().trim().contains("turn down the volume")
                                    || finalOutput.toLowerCase().trim().contains("decrease the volume")
                                    || finalOutput.toLowerCase().trim().contains("decrease volume")) {

                                MusicDatasource.decreaseVolume();
                            }

                        } else if (finalOutput.toLowerCase().trim().contains("get me weather data")
                                || finalOutput.toLowerCase().trim().contains("what is the weather")
                                || finalOutput.toLowerCase().trim().contains("what is the temperature today")
                                || finalOutput.toLowerCase().trim().contains("weather report")) {

                            WeatherService weatherService = new WeatherService();
                            weatherService.speakWeather();

                        } else if (finalOutput.toLowerCase().trim().contains("goodbye")
                                || finalOutput.toLowerCase().trim().contains("exit application")) {

                            response = "Exiting ,have a nice day";

                            TTS.speak(response);
                            Thread.sleep(2000);
                            Platform.exit();

                        } else if (finalOutput.toLowerCase().trim().contains("who is your owner")
                                || finalOutput.toLowerCase().trim().contains("who made you")
                                || finalOutput.toLowerCase().trim().contains("who is your creator")) {

                            ResultSet githubAccountResultSet = statement.executeQuery("SELECT * FROM functionality WHERE  me=\""
                                    + "github account"
                                    + "\"");

                            while (githubAccountResultSet.next()) {

                                response = "opening my creator's github page";

                                TTS.speak(response);

                                System.out.println("{@} Opening link : " + githubAccountResultSet.getString("ai"));
                                Webdriver.webDriverGet(githubAccountResultSet.getString("ai"));
                            }

                        } else if (finalOutput.toLowerCase().trim().contains("code 1026")
                                || finalOutput.toLowerCase().trim().contains("identity code 4488")) {


                            response = "Identity verified!, Sufficient permissions acquired";
                            TTS.speak(response);
                            adminPermission = 1;

                        } else if (finalOutput.toLowerCase().trim().contains("log me in")
                                || finalOutput.toLowerCase().trim().contains("sign into gmail")
                                || finalOutput.toLowerCase().trim().contains("sign me in")
                                || finalOutput.toLowerCase().trim().contains("sign me into google")) {

                            if (adminPermission == 1) {

                                ResultSet signInResultSet = statement.executeQuery("SELECT * FROM emailDetails WHERE  site=\""
                                        + "gmail.com"
                                        + "\"");

                                while (signInResultSet.next()) {

                                    TTS.speak("valid code found");

                                    System.out.println("{@} Opening link : https://gmail.com");
                                    Webdriver.webDriverSignIn(signInResultSet.getString("email"), signInResultSet.getString("password"));

                                    Thread.sleep(2000);
                                    TTS.speak("signing you in sir.");
                                }

                            } else {

                                TTS.speak("you do not have sufficient permissions to log in.");
                            }

                        } else if (finalOutput.toLowerCase().trim().contains("open youtube")
                                || finalOutput.toLowerCase().trim().contains("youtube video")) {

                            response = "sure thing, opening";
                            TTS.speak(response);

                            System.out.println("{@} Opening link : https://youtube.com");
                            Webdriver.webDriverGet("https://youtube.com");

                        } else if (finalOutput.toLowerCase().trim().contains("what is the time")
                                || finalOutput.toLowerCase().trim().contains("current time")
                                || finalOutput.toLowerCase().trim().contains("what time is it")) {


                            Date date = new Date();
                            TTS.speak("Current time is : "
                                    + date.getHours() + " hours "
                                    + date.getMinutes() + " minutes "
                                    + date.getSeconds() + " seconds");

                        } else if (finalOutput.toLowerCase().trim().contains("show me memes")
                                || finalOutput.toLowerCase().trim().contains("spicy memes")
                                || finalOutput.toLowerCase().trim().contains("open 9gag")
                                || finalOutput.toLowerCase().trim().contains("dank memes")) {

                            TTS.speak("opening some dank memes");
                            Webdriver.webDriverGet("http://9gag.com");

                        } else if (finalOutput.toLowerCase().trim().contains("search")) {


                            String searchQuery = finalOutput
                                    .toLowerCase()
                                    .substring(finalOutput.toLowerCase().trim().indexOf("search") + 7);

                            if (searchQuery.isEmpty()) {

                                TTS.speak("Please specify the search query.");

                            } else {

                                TTS.speak("one search result coming up");

                                System.out.println("{@} Searching for : " + searchQuery.trim());
                                String url = "https://www.google.co.in/search?q=" + searchQuery.trim();
                                Webdriver.webDriverGet(url);
                            }

                        } else if (finalOutput.toLowerCase().trim().contains("close window")
                                || finalOutput.toLowerCase().trim().contains("close the window")) {

                            Webdriver.closeWindow();
                        } else {

                            if (finalOutput.toLowerCase().trim().contains("yes")
                                    || finalOutput.toLowerCase().trim().contains("no")) {

                                return;
                            }

                            String x = null;

                            try {

                                String mainQuerry = "SELECT * FROM response WHERE  me=\""
                                        + finalOutput.toLowerCase().trim()
                                        + "\"OR me LIKE '%"
                                        + finalOutput.toLowerCase().trim()
                                        + "%'";

                                ResultSet mainResultSet = statement.executeQuery(mainQuerry);

                                while (mainResultSet.next()) {

                                    System.out.println("{<>} Retrieving data from the central database.");
                                    x = mainResultSet.getString("ai");
                                }

                                if (x == null) {

                                    Random randomResponse = new Random();
                                    int randomIndex = randomResponse.nextInt(5);

                                    String genericQuerry = "SELECT * FROM genericResponse WHERE me="
                                            + randomIndex;

                                    ResultSet genericResponseResultSet = statement.executeQuery(genericQuerry);

                                    while (genericResponseResultSet.next()) {

                                        x = genericResponseResultSet.getString("ai");
                                    }
                                    TTS.speak(x);

                                } else {

                                    TTS.speak(x);
                                }

                            } catch (Exception e) {

                                System.out.println("{X} Exception while processing response from database.");
                                System.out.println(e.getMessage());

                                x = "This is not in the central database";
                                TTS.speak(x);
                            }
                        }

                    } catch (Exception exception) {

                        System.out.println("[X] Exception in the main response processing thread.");
                        exception.printStackTrace();
                    }
                });

        responseProcessingThread.start();
    }
}
