module com.amisam.jdbmusicarchive {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires transitive javafx.graphics;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.base;

    opens com.amisam.jdbmusicarchive to javafx.fxml;
    exports com.amisam.jdbmusicarchive;
    exports com.amisam.jdbmusicarchive.engine;
    exports com.amisam.jdbmusicarchive.datamodel;
    exports com.amisam.jdbmusicarchive.model;
}