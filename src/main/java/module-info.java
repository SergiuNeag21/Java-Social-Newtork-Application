module com.example.socialnetworkjava2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.socialnetworkjava2 to javafx.fxml;
    opens com.example.socialnetworkjava2.domain to javafx.base;
    exports com.example.socialnetworkjava2;
}