package com.kps.kenziethebot.model;

import com.darkprograms.speech.synthesiser.SynthesiserV2;
import com.kps.kenziethebot.controller.MainController;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;

public class TTS {

    /**
     * This class is used for TTS [ Text to Speech ] conversion.
     */

    private static SynthesiserV2 synthesiserV2 = new SynthesiserV2(MainController.API_KEY);

    public static synchronized void speak(String text) {
        /*
        Thread safe method for Text to Speech.
         */

        new Thread(
                () -> {

                    try {

                        synthesiserV2.setSpeed(0.955f);
                        System.out.println("{->} Speaking : " + text);

                        AdvancedPlayer advancedPlayer = new AdvancedPlayer(synthesiserV2.getMP3Data(text));
                        advancedPlayer.play();

                    } catch (IOException | JavaLayerException exception) {

                        System.out.println("{X} Exception in TTS. ");
                        System.out.println(exception.getMessage());
                    }

                }
        ).start();
    }
}
