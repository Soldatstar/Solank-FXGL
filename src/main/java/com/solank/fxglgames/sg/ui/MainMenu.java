package com.solank.fxglgames.sg.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;

public class MainMenu extends FXGLMenu {
    public MainMenu() {
        super(MenuType.MAIN_MENU);
        Texture bg = FXGL.getAssetLoader().loadTexture("mainmenu.png");
        bg.setFitWidth(getAppWidth());
        bg.setFitHeight(getAppHeight());




        Texture selectButton = FXGL.getAssetLoader().loadTexture("red-button-icon-single.png", 68, 68);
        Button btnPlay = new Button("Spiel starten");
        btnPlay.getStyleClass().add("main_menu_button");
        VBox selectHBox = new VBox(selectButton, btnPlay);
        // fireNewGame() clears the Scene and calls initGame(), to spawn all entities.
        btnPlay.setOnAction(e -> fireNewGame());
        selectHBox.setAlignment(Pos.CENTER);
        selectHBox.setSpacing(0);
        selectHBox.setTranslateX(100);
        selectHBox.setTranslateY(100);


        getContentRoot().getChildren().addAll(bg, selectHBox);
    }
}
