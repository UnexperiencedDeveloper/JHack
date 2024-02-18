module com.timprogrammiert.jhack {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;


    opens com.timprogrammiert.jhack to javafx.fxml;
    exports com.timprogrammiert.jhack;
    exports com.timprogrammiert.jhack.gui;
    opens com.timprogrammiert.jhack.gui to javafx.fxml;
}