package com.kps.kenziethebot.controller;

import com.kps.kenziethebot.model.DataSource;
import com.kps.kenziethebot.view.ViewFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    /**
     * This is the main class of the project.
     * Licenced to : Karan Pratap Singh.
     * @param primaryStage
     * @throws Exception
     */

    @Override
    public void start(Stage primaryStage) throws Exception {

        ViewFactory viewFactory = new ViewFactory();

        Scene scene = viewFactory.getSplashScene();

        primaryStage.setTitle("Splash");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);

        primaryStage.getIcons().add(viewFactory.getDefaultIcon());
        viewFactory.initMovablePlayer(primaryStage);
        primaryStage.show();
    }


    @Override
    public void init() throws Exception {

        super.init();

        if (DataSource.getInstance().open()) {

            System.out.println("[+] Database connection was established.");
        } else {

            System.out.println("[-] Database connection was not established.");
        }
    }

    @Override
    public void stop() throws Exception {

        super.stop();

        DataSource.getInstance().close();
    }

    public void openLink(String url) {

        getHostServices().showDocument(url);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
