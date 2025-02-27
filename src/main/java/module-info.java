module ru.kursach.frontent {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires com.fasterxml.jackson.datatype.jsr310;


    opens ru.kursach.frontent.dto to com.fasterxml.jackson.databind, javafx.base;
    opens ru.kursach.frontent to  javafx.fxml;
    exports ru.kursach.frontent;
    exports ru.kursach.frontent.scnene;
    opens ru.kursach.frontent.scnene to javafx.fxml;
    opens ru.kursach.frontent.dto.enams to com.fasterxml.jackson.databind, javafx.base;

}