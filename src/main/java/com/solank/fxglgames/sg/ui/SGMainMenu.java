package com.solank.fxglgames.sg.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.binding.Bindings;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class SGMainMenu extends FXGLMenu {



    public SGMainMenu() {
        super(MenuType.MAIN_MENU);
        Texture bg = FXGL.getAssetLoader().loadTexture("mainmenu.png");
        bg.setFitWidth(getAppWidth());
        bg.setFitHeight(getAppHeight());

        ChronButton btnPlayGame = new ChronButton("Play Game", () -> {fireNewGame();});
        ChronButton btnOptions = new ChronButton("Options", () -> {});
        ChronButton btnQuit = new ChronButton("Exit Game", () -> {fireExit();});

        var box = new VBox(15,
            btnPlayGame,
            btnOptions,
            new ChronButton("placeholder1",()->{}),
            new ChronButton("placeholder2",()->{}),
            btnQuit,
            new Text(),
            new Separator(Orientation.HORIZONTAL),
            FXGL.getUIFactoryService().newText("PH description", Color.WHITE, 20.0));

        box.setTranslateX(100);
        box.setTranslateY(400);




        getContentRoot().getChildren().addAll(bg, box);
    }


    private static class ChronButton extends StackPane {
        private static final Color SELECTED_COLOR = Color.WHITE;
        private static final Color UNSELECTED_COLOR = Color.GRAY;
        private final String name;
        private final Runnable action;

        private Text text;
        private Rectangle selector;
        public ChronButton(String name, Runnable action) {
            this.name = name;
            this.action = action;
            selector = new Rectangle(5, 20, Color.WHITE);
            selector.setTranslateX(-25);
            selector.visibleProperty().bind(focusedProperty());
            //run action when enter is pressed
            setOnKeyPressed(e -> {
                if (e.getCode().getName().equals("Enter")) {
                    action.run();
                }
            });

            setOnMouseClicked(e -> action.run());


            text= FXGL.getUIFactoryService().newText(name, Color.WHITE, 20.0);
            setAlignment(Pos.CENTER_LEFT);
            setFocusTraversable(true);
            text.fillProperty().bind(Bindings.when(focusedProperty()).then(SELECTED_COLOR).otherwise(UNSELECTED_COLOR));
            getChildren().addAll(selector, text);

        }

    }
}
