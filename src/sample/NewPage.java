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
        stage.setTitle("RFID�ʲ�����ϵͳ�ͻ���");
        stage.setHeight(1000);
        stage.setWidth(1600);
        stage.show();
    }

    public HBox addHBox(){
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12)); //�ڵ㵽��Ե�ľ���
        hbox.setSpacing(10); //�ڵ�֮��ļ��
        hbox.setStyle("-fx-background-color:#4693E7FF"); //����ɫ
        Label label = new Label("RFID�ʲ�����ϵͳ�ͻ���");
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
        Tab tab1 = new Tab("ϵͳ����",root);
        tab1.setClosable(false);

        //��tb2���ô�ֱ�������
        Parent root2 = FXMLLoader.load(getClass().getResource("pageNotHaveCard.fxml"));
        Tab tab2 = new Tab("δ����",root2);
        tab2.setClosable(false);

        tabPane.getTabs().addAll(tab1,tab2);
        return tabPane;
    }

    private VBox addTableView() {
        final Label label = new Label("��־��Ϣ");
        label.setFont(new Font("Arial", 20));
        TableView table = new TableView();
        TableColumn timeCol = new TableColumn("��־ʱ��");
        TableColumn typeCol = new TableColumn("��־����");
        TableColumn textCol = new TableColumn("��־����");
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
