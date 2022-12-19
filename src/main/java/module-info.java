/* doesn't work with source level 1.8:
module org.openjfx.cafeteria {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.openjfx.cafeteria to javafx.fxml;
    exports org.openjfx.cafeteria;
}
*/
