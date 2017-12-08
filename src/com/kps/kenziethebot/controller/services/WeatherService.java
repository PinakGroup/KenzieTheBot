package com.kps.kenziethebot.controller.services;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import com.kps.kenziethebot.model.ResponseProcessing;
import com.kps.kenziethebot.model.TTS;
import com.kps.kenziethebot.model.Webdriver;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class WeatherService {
    /**
     * Class for processing dynamic weather requests.
     */

    private YahooWeatherService weatherService;
    private Channel channel;

    public void speakWeather() {

        new Thread(

                () -> {

                    try {


                        weatherService = new YahooWeatherService();
                        channel = weatherService.getForecast("2295019", DegreeUnit.CELSIUS); // for CITY ID

                        String simpleInfo = "The weather of your current location is "
                                + channel.getItem().getCondition().getTemp()
                                + " degrees "
                                + channel.getUnits().getTemperature().toString().toLowerCase()
                                + " with "
                                + channel.getAtmosphere().getHumidity()
                                + "% humidity, pressure of "
                                + String.format("%.0f", channel.getAtmosphere().getPressure() / 100)
                                + " bar and "
                                + String.format("%.0f", channel.getAtmosphere().getVisibility())
                                + "% visibility.";

                        String additionalInfo = "The expected sunrise and sunset time is : "
                                + channel.getAstronomy().getSunrise().getHours()
                                + ":"
                                + channel.getAstronomy().getSunrise().getMinutes()
                                + " am and "
                                + channel.getAstronomy().getSunset().getHours()
                                + ":"
                                + channel.getAstronomy().getSunset().getMinutes()
                                + " pm.";

                        String windInformation = "The current wind speed is "
                                + channel.getWind().getSpeed()
                                + " kilometres per hour"
                                + " with " + channel.getWind().getChill()
                                + "% chill and flowing in "
                                + calculateWindDirection()
                                + " direction.";

                        System.out.println("{@} Opening link : " + channel.getLink().substring(63));

                        Webdriver.webDriverGet(channel.getLink().substring(63));

                        TTS.speak("Loading weather info. sir");
                        Thread.sleep(5000);

                        TTS.speak(simpleInfo);
                        Thread.sleep(11000);

                        TTS.speak("Would you like advance info. sir ?");
                        Thread.sleep(12000);

                        if (ResponseProcessing.referenceResponse.toLowerCase().contains("yes")) {

                            TTS.speak(additionalInfo);
                            Thread.sleep(6000);
                            TTS.speak(windInformation);
                        } else if (ResponseProcessing.referenceResponse.toLowerCase().contains("no")) {

                            TTS.speak("ok sir.");
                        }


                    } catch (JAXBException | IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

        ).start();
    }

    private String calculateWindDirection() {

        String wind_direction = null;

        if (channel.getWind().getDirection() >= 0 && channel.getWind().getDirection() <= 45) {

            wind_direction = "north";
        } else if (channel.getWind().getDirection() >= 45 && channel.getWind().getDirection() <= 90) {

            wind_direction = "north east";
        } else if (channel.getWind().getDirection() >= 90 && channel.getWind().getDirection() <= 135) {

            wind_direction = "east";
        } else if (channel.getWind().getDirection() >= 135 && channel.getWind().getDirection() <= 180) {

            wind_direction = "south east";
        } else if (channel.getWind().getDirection() >= 180 && channel.getWind().getDirection() <= 225) {

            wind_direction = "south";
        } else if (channel.getWind().getDirection() >= 225 && channel.getWind().getDirection() <= 270) {

            wind_direction = "south west";
        } else if (channel.getWind().getDirection() >= 270 && channel.getWind().getDirection() <= 315) {

            wind_direction = "west";
        } else if (channel.getWind().getDirection() >= 315 && channel.getWind().getDirection() <= 360) {

            wind_direction = "north west";
        }

        return wind_direction;
    }

}
