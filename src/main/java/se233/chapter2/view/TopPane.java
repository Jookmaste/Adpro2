package se233.chapter2.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import se233.chapter2.Launcher;
import se233.chapter2.controller.AllEventHandlers;

import javax.swing.*;
import java.time.LocalDateTime;

public class TopPane extends FlowPane {
    private Button refresh;
    private Button add;
    private Label update;
    public TopPane() {
        this.setPadding(new Insets(10));
        this.setHgap(10);
        this.setPrefSize(640, 20);
        add = new Button("Add");
        refresh = new Button("Refresh");
        ComboBox<String> baseCurrencyBox = new ComboBox<>();
        baseCurrencyBox.getItems().addAll("THB","USD","EUR","JPY","KRW");
        baseCurrencyBox.setValue(Launcher.getBaseCurrency());

        refresh.setOnAction(event -> AllEventHandlers.onRefresh());
        add.setOnAction(event -> AllEventHandlers.onAdd());

        baseCurrencyBox.setOnAction(event -> {
                String newBase = baseCurrencyBox.getValue();
                Launcher.setBaseCurrency(newBase);
                Launcher.reloadCurrencyList();
                AllEventHandlers.onRefresh();
        });
        update = new Label();
        refreshPane();
        this.getChildren().addAll(refresh, add, baseCurrencyBox, update);
    }
    public void refreshPane() {
        update.setText(String.format("Last update: %s", LocalDateTime.now().toString()));
    }
}