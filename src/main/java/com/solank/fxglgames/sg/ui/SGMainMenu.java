package com.solank.fxglgames.sg.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.solank.fxglgames.sg.SGApp;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static com.solank.fxglgames.sg.manager.StaticStrings.TITLE;
import static com.solank.fxglgames.sg.manager.StaticStrings.VERSION;

public class SGMainMenu extends FXGLMenu {
    private static final Color SELECTED_COLOR = Color.WHITE;
    private static final Color UNSELECTED_COLOR = Color.GRAY;
    private final ObjectProperty<sgButton> selectedButton;

    public SGMainMenu() {
        super(MenuType.MAIN_MENU);
        Texture bg = FXGL.getAssetLoader().loadTexture("mainmenu/mainmenu.png");
        bg.setFitWidth(getAppWidth());
        bg.setFitHeight(getAppHeight());

        sgButton btnPlayGame = new sgButton("Play Game", "Start new Game", () -> {
            fireNewGame();
        });
        sgButton btnOptions = new sgButton("Options", "Adjust in-game options", () -> {
        });
        sgButton btnQuit = new sgButton("Exit Game", "Exit to desktop", () -> {
            fireExit();
        });
        selectedButton = new SimpleObjectProperty<>(btnPlayGame);


        var textDescription = FXGL.getUIFactoryService().newText("", Color.LIGHTGRAY, 14.0);
        textDescription.textProperty().bind(
                Bindings.createStringBinding(() -> selectedButton.get().description, selectedButton));

        var box = new VBox(15,
                btnPlayGame,
                btnOptions,
                new sgButton("placeholder1", "", () -> {
                }),
                new sgButton("placeholder2", "", () -> {
                }),
                btnQuit,
                new Text(),
                new Lineseperator(),
                textDescription);


        box.setTranslateX(100);
        box.setTranslateY(400);

        Text version = FXGL.getUIFactoryService().newText(VERSION, Color.BLACK, 12.0);
        version.setX(670);
        version.setY(588);
        version.setRotate(-8);
        Texture logo = FXGL.getAssetLoader().loadTexture("mainmenu/logo.png");
        logo.setScaleX(0.05);
        logo.setScaleY(0.05);
        logo.setTranslateX(+190);
        logo.setTranslateY(-370);

        Text gameName = FXGL.getUIFactoryService().newText(TITLE, Color.WHITE, 100.0);
        // Text gameNameBackground = FXGL.getUIFactoryService().newText(SGApp.TITLE,Color.LIGHTGRAY, 115.0);

        StackPane stackPane = new StackPane(gameName);
        stackPane.setLayoutX(100);
        stackPane.setLayoutY(100);


        getContentRoot().getChildren().addAll(bg, box, version, logo, stackPane);

    }

    private static class Lineseperator extends Parent {
        private final Rectangle line = new Rectangle(300, 2, Color.WHITE);

        public Lineseperator() {
            var gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE
                    , new javafx.scene.paint.Stop(0, Color.WHITE)
                    , new javafx.scene.paint.Stop(0.2, Color.LIGHTGRAY)
                    , new javafx.scene.paint.Stop(1, Color.TRANSPARENT)
            );

            line.setFill(gradient);
            getChildren().add(line);

        }
    }

    private class sgButton extends StackPane {

        private final String name;
        private final String description;
        private final Runnable action;

        private final Text text;
        private final Rectangle selector;

        public sgButton(String name, String description, Runnable action) {
            this.name = name;
            this.action = action;
            this.description = description;
            selector = new Rectangle(5, 20, Color.WHITE);
            selector.setTranslateX(-25);
            selector.visibleProperty().bind(focusedProperty());

            setOnKeyPressed(e -> {
                if (e.getCode().getName().equals("Enter")) {
                    action.run();
                }
            });

            setOnMouseClicked(e -> action.run());


            text = FXGL.getUIFactoryService().newText(name, Color.WHITE, 20.0);
            text.fillProperty().bind(Bindings.when(focusedProperty()).then(SELECTED_COLOR).otherwise(UNSELECTED_COLOR));
            text.strokeProperty()
                    .bind(Bindings.when(focusedProperty()).then(SELECTED_COLOR).otherwise(UNSELECTED_COLOR));

            text.setStrokeWidth(0.5);

            focusedProperty().addListener((obs, old, isFocused) -> {
                if (isFocused) {
                    selectedButton.setValue(this);
                }
            });

            setAlignment(Pos.CENTER_LEFT);
            setFocusTraversable(true);
            getChildren().addAll(selector, text);

        }

    }
}
