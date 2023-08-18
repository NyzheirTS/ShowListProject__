module com.warnercloud.showlistproject__ {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires annotations;
    requires java.prefs;
    requires java.net.http;


    opens com.warnercloud.showlistproject__ to javafx.fxml;
    exports com.warnercloud.showlistproject__;
}