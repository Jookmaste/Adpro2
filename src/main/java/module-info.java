module se233.chapter2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;
    requires org.json;
    requires javafx.web;
    requires java.desktop;
    requires java.logging;
    requires org.slf4j;

    opens se233.chapter2 to javafx.fxml;
    exports se233.chapter2;
}