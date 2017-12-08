package com.kps.kenziethebot.view;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ViewFactory {

    /**
     * This class links *.fxml files to other packages, With various extra features.
     */

    private final String MAIN_LAYOUT_FXML_PATH = "MainLayout.fxml";
    private final String SPLASH_LAYOUT_FXML_PATH = "SplashLayout.fxml";
    private final String DEFAULT_CSS_PATH = "main-css.css";
    private final String DEFAULT_ICON_PATH = "assets/images/default_icon.png";
    private Point2D anchorPt;
    private Point2D previousLocation;


    private Scene initializeScene(String fxmlPath, int width, int height) {

        Parent parent;
        FXMLLoader loader;
        Scene scene;

        try {

            loader = new FXMLLoader(getClass().getResource(fxmlPath));
            parent = loader.load();


        } catch (IOException ioe) {

            System.out.println("[X] Exception in ViewFactory.java class." +
                    "Cannot load *.fxml file.");

            System.out.println(ioe.getMessage());
            return null;
        }

        scene = new Scene(parent, width, height);
        scene.getStylesheets().add(getClass().getResource(DEFAULT_CSS_PATH).toExternalForm());

        return scene;
    }

    public Scene getMainScene() {

        System.out.println("{+(*)+} Main scene was initialized.");
        return initializeScene(MAIN_LAYOUT_FXML_PATH, 1280, 710);
    }

    public Scene getSplashScene() {

        System.out.println("{+(*)+} Splash scene was initialized.");
        return initializeScene(SPLASH_LAYOUT_FXML_PATH, 800, 445);
    }

    public Image getDefaultIcon() {

        return new Image(getClass().getResource(DEFAULT_ICON_PATH).toString());
    }

    public void initMovablePlayer(Stage primaryStage) {

        Scene scene = primaryStage.getScene();
        Pane root = (Pane) scene.getRoot();
        root.setPickOnBounds(true);

        // starting initial anchor point
        root.setOnMousePressed(
                (mouseEvent) -> {

                    anchorPt = new Point2D(mouseEvent.getScreenX(),
                            mouseEvent.getScreenY());
                    primaryStage.setOpacity(1);
                }
        );

        // Dragging the stage by moving its x,y
        // based on the previous location.
        root.setOnMouseDragged(
                (mouseEvent) -> {

                    if (anchorPt != null && previousLocation != null) {

                        primaryStage.setOpacity(0.855f);

                        primaryStage.setX(previousLocation.getX()
                                + mouseEvent.getScreenX()
                                - anchorPt.getX());

                        primaryStage.setY(previousLocation.getY()
                                + mouseEvent.getScreenY()
                                - anchorPt.getY());
                    }
                });

        // Set the new previous to the current mouse x,y coordinate
        root.setOnMouseReleased(
                (mouseEvent) -> {

                    previousLocation = new Point2D(primaryStage.getX(),
                            primaryStage.getY());
                    primaryStage.setOpacity(1);
                }
        );

        // Initialize previousLocation after Stage is shown
        primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWN,
                (WindowEvent t) -> {

                    previousLocation = new Point2D(primaryStage.getX(),
                            primaryStage.getY());
                });
    }
}
