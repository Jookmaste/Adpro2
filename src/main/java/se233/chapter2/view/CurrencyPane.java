package se233.chapter2.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import se233.chapter2.controller.AllEventHandlers;
import se233.chapter2.controller.FetchData;
import se233.chapter2.controller.draw.DrawCurrencyInfoTask;
import se233.chapter2.controller.draw.DrawGraphTask;
import se233.chapter2.controller.draw.DrawTopAreaTask;
import se233.chapter2.model.Currency;
import se233.chapter2.model.CurrencyEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CurrencyPane extends BorderPane {
    private Currency currency;
    private Button watch;
    private Button unwatch;
    private Button delete;
    public CurrencyPane(Currency currency) {
        this.watch = new Button("Watch");
        this.unwatch = new Button("UnWatch");
        this.delete = new Button("Delete");
        this.watch. setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandlers.onWatch(currency.getShortCode());
            }
        });
        this.delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandlers.onDelete(currency.getShortCode());
            }
        });
        this.unwatch. setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandlers.onUnwatch(currency.getShortCode());
            }
        });
        this.setPadding(new Insets(0));
        this.setPrefSize(640, 300);
        this.setStyle("-fx-border-color: black");
        try {
            this.refreshPane(currency);
        } catch (ExecutionException e) {
            System. out.println("Encountered an execution exception.");
        } catch (InterruptedException e) {
            System.out.println("Encountered an interupted exception.");
        }
    }
    public void refreshPane(Currency currency) throws ExecutionException,InterruptedException {
        this.currency = currency;

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Future<Pane> infoFuture = executor.submit(new DrawCurrencyInfoTask(currency));
        Future<HBox> topFuture = executor.submit(new DrawTopAreaTask(watch, unwatch, delete));
        Future<VBox> graphFuture = executor.submit(new DrawGraphTask(currency));

//        Pane currencyInfo = genInfoPane();
//        ExecutorService executor = Executors. newSingleThreadExecutor();
//        executor.execute(futureTask);
//        VBox currencyGraph = (VBox) futureTask.get();
//        Pane topArea = genTopArea();

        Pane currencyInfo = infoFuture.get();
        HBox topArea = topFuture.get();
        VBox currencyGraph = graphFuture.get();


        this.setTop(topArea);
        this.setLeft(currencyInfo);
        this.setCenter(currencyGraph);

        executor.shutdown();

    }
    private Pane genInfoPane() {
        VBox currencyInfoPane = new VBox(10);
        currencyInfoPane.setPadding(new Insets(5, 25, 5, 25));
        currencyInfoPane.setAlignment(Pos.CENTER);
        Label exchangeString = new Label("");
        Label watchString = new Label("");
        exchangeString.setStyle("-fx-font-size: 20;");
        watchString. setStyle("-fx-font-size: 14;");
        if (this.currency != null) {
            exchangeString.setText(String.format("%s: %.4f", this.currency.getShortCode(), this.currency.getCurrent().getRate()));
            if (this.currency.getWatch() == true) {
                watchString.setText(String.format("(Watch @%.4f)", this.currency.getWatchRate()));
            } else {
                watchString.setText("");
            }
        }
        currencyInfoPane.getChildren().addAll(exchangeString, watchString);
        return currencyInfoPane;
    }
    private HBox genTopArea() {
        HBox topArea = new HBox(10);
        topArea.setPadding(new Insets(5));
        topArea.getChildren().addAll(watch, unwatch, delete);
        ((HBox) topArea).setAlignment(Pos.CENTER_RIGHT);
        return topArea;
    }
}
