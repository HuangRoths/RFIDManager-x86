package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class NewPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
    Date date = new Date(System.currentTimeMillis());

    private ObservableList<MyLog> data =
            FXCollections.observableArrayList(
                    new MyLog(formatter.format(date), "Smith", "jacob.smith@example.com"),
                    new MyLog(formatter.format(date), "Johnson", "isabella.johnson@example.com"),
                    new MyLog(formatter.format(date), "Williams", "ethan.williams@example.com")
            );

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane border = new BorderPane();

        HBox hbox = addHBox();
        border.setTop(hbox);

        VBox vBoxBottom = addTableView();
//        border.setBottom(vBoxBottom);

        TabPane tabPane = addTabPane();
        border.setCenter(tabPane);

        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setTitle("RFID资产管理系统客户端");
        stage.setHeight(1000);
        stage.setWidth(1600);
        stage.show();
    }

    public HBox addHBox(){
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12)); //节点到边缘的距离
        hbox.setSpacing(10); //节点之间的间距
        hbox.setStyle("-fx-background-color:#4693E7FF"); //背景色
        Label label = new Label("RFID资产管理系统客户端");
        label.setFont(Font.font ("Verdana", 20));
        label.setTextFill(Color.WHITE);
        hbox.getChildren().add(label);
        return hbox;
    }

    /*
     * Creates a VBox with a list of links for the left region
     */
    private VBox addVBox() {

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8);              // Gap between nodes

        vbox.setStyle("-fx-background-color: #828588FF");
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    private TabPane addTabPane() throws IOException {
        TabPane tabPane = new TabPane();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PageTwo.fxml")));
        Tab tab1 = new Tab("系统设置",root);
        tab1.setClosable(false);

        //给tb2设置垂直布局面板
        Parent root2 = FXMLLoader.load(getClass().getResource("pageNotHaveCard.fxml"));
        Tab tab2 = new Tab("未发卡",root2);
        tab2.setClosable(false);

        tabPane.getTabs().addAll(tab1,tab2);
        return tabPane;
    }

    private VBox addTableView() {
        final Label label = new Label("日志信息");
        label.setFont(new Font("Arial", 20));
        TableView table = new TableView();
        TableColumn timeCol = new TableColumn("日志时间");
        TableColumn typeCol = new TableColumn("日志类型");
        TableColumn textCol = new TableColumn("日志内容");
        timeCol.setMinWidth(200);
        typeCol.setMinWidth(200);
        textCol.setMinWidth(400);
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timeCol"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeCol"));
        textCol.setCellValueFactory(new PropertyValueFactory<>("textCol"));
        table.setItems(data);
        table.getColumns().addAll(timeCol, typeCol, textCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setPrefHeight(200);
        vbox.getChildren().addAll(label, table);
        return vbox;
    }
}
