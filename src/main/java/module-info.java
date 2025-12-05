module JogoForca {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.jogoforca to javafx.fxml;
    exports com.jogoforca.model;
    exports com.jogoforca;
}