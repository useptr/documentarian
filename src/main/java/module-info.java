module com.example.documentarian {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.documentarian to javafx.fxml;
    exports com.example.documentarian;
}