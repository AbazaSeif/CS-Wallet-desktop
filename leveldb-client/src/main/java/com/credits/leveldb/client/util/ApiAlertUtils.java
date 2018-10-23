package com.credits.leveldb.client.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

public class ApiAlertUtils {
    public static void showAlertWarning(String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Warning");
            alert.setHeaderText("Warning");
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public static void showAlertError(String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public static void showAlertInfo(String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Info");
            alert.setHeaderText("Info");
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}
