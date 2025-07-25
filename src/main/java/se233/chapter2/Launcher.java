package se233.chapter2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import se233.chapter2.controller.FetchData;
import se233.chapter2.controller.Initialize;
import se233.chapter2.controller.RefreshTask;
import se233.chapter2.model.Currency;
import se233.chapter2.model.CurrencyEntity;
import se233.chapter2.view.CurrencyPane;
import se233.chapter2.view.CurrencyParentPane;
import se233.chapter2.view.TopPane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Launcher extends Application {
    private static Stage primaryStage;
    private static FlowPane mainPane;
    private static TopPane topPane;
    private static CurrencyParentPane currencyParentPane;
    private static List<Currency> currencyList;

    @Override
    public void start(Stage stage) throws ExecutionException, InterruptedException {
        primaryStage = stage;
        primaryStage.setTitle("Currency Watcher");
        primaryStage.setResizable(false);
        currencyList = Initialize.initializeApp();
        initMainPane();
        Scene mainScene = new Scene(mainPane);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        RefreshTask r = new RefreshTask();
        Thread th = new Thread(r);
        th.setDaemon(true);
        th.start();
    }

    public void initMainPane() throws ExecutionException, InterruptedException {
        mainPane = new FlowPane();
        topPane = new TopPane();
        currencyParentPane = new CurrencyParentPane(currencyList);
        mainPane.getChildren().add(topPane);
        mainPane.getChildren().add(currencyParentPane);
    }

    public static void refreshPane() throws InterruptedException, ExecutionException {
        topPane.refreshPane();
        currencyParentPane.refreshPane(currencyList);
        primaryStage. sizeToScene();
    }

    public static void reloadCurrencyList() {
        List<Currency> newList = new ArrayList<>();
        for (Currency c : currencyList) {
            Currency cc = new Currency(c.getShortCode());
            List<CurrencyEntity> cList = FetchData.fetchRange(cc.getShortCode(), 30);
            cc.setHistorical(cList);
            cc.setCurrent(cList.get(cList.size() - 1));
            cc.setWatch(c.getWatch());
            cc.setWatchRate(c.getWatchRate());
            newList.add(cc);
        }
        setCurrencyList(newList);
    }

    public static List<Currency> getCurrencyList() {
        return currencyList;
    }

    public static void setCurrencyList(List<Currency> currencyList) {
        Launcher.currencyList = currencyList;
    }

    public static CurrencyParentPane getCurrencyParentPane() {
        return currencyParentPane;
    }

    public static void setCurrencyParentPane(CurrencyParentPane currencyParentPane) {
        Launcher.currencyParentPane = currencyParentPane;
    }

    public static TopPane getTopPane() {
        return topPane;
    }

    public static void setTopPane(TopPane topPane) {
        Launcher.topPane = topPane;
    }

    public static FlowPane getMainPane() {
        return mainPane;
    }

    public static void setMainPane(FlowPane mainPane) {
        Launcher.mainPane = mainPane;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    private static String baseCurrency = "THB";

    public static String getBaseCurrency() {
        return baseCurrency;
    }

    public static void setBaseCurrency(String newBase) {
        baseCurrency = newBase;
    }

    public static void refreshAllCurrencyData() {

    }

    public static void setPrimaryStage(Stage primaryStage) {
        Launcher.primaryStage = primaryStage;
    }
}
