package se233.chapter2.controller.draw;

import java.util.concurrent.Callable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import se233.chapter2.model.Currency;

public class DrawCurrencyInfoTask implements Callable<Pane> {
    private final Currency currency;
    public DrawCurrencyInfoTask(Currency currency) {
        this.currency = currency;
    }
    @Override
    public Pane call() {
        VBox currencyInfoPane = new VBox(10);
        currencyInfoPane.setPadding(new Insets(5, 25, 5, 25));
        currencyInfoPane.setAlignment(Pos.CENTER);
        Label exchangeString = new Label("");
        Label watchString = new Label("");
        exchangeString.setStyle("-fx-font-size: 20;");
        watchString.setStyle("-fx-font-size: 14;");
        if (currency != null) {
            exchangeString.setText(String.format("%s: %.4f", currency.getShortCode(), currency.getCurrent().getRate()));
            if (currency.getWatch() == true) {
                watchString.setText(String.format("(Watch @%.4f)", currency.getWatchRate()));
            }
        }
        currencyInfoPane.getChildren().addAll(exchangeString, watchString);
        return currencyInfoPane;
    }
}