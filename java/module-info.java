module com.example.sixquiprend_xie_xu_yuan {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sixquiprend_xie_xu_yuan to javafx.fxml;
    exports com.example.sixquiprend_xie_xu_yuan;
}