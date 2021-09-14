package sample;

import cn.hutool.http.HttpRequest;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class Login extends Application {

    private Map map = null;

    static String IPAddr;
    static String result;

    //�����׼��������  ����ģ���¼ʹ��
    @Override
    public void init() throws Exception {

        map = new HashMap<String,String>();
        map.put("user", "pw");
    }

    @Override
    public void start(Stage stage) {

        Text name = new Text("�û�����");
        Text password = new Text("���룺");
        Text ip = new Text("IP: ");
        Text port = new Text("port: ");
        TextField e_name = new TextField();
        e_name.setPrefWidth(150);
        TextField e_ip = new TextField();
        e_ip.setText("10.36.16.113");
        e_ip.setPrefWidth(150);
        TextField e_port = new TextField();
        e_port.setText("8181");
        e_port.setPrefWidth(150);
        PasswordField e_password = new PasswordField();
        e_password.setPromptText("���������룺");

        Button clear = new Button("��������");
        Button login = new Button("��¼");

        //���񲼾�
        GridPane gridPane = new GridPane();
        //��������λ��
        gridPane.add(name, 0, 0);
        gridPane.add(e_name, 1,0);
        gridPane.add(password, 0, 1);
        gridPane.add(e_password, 1, 1);
        gridPane.add(ip, 0, 2);
        gridPane.add(e_ip, 1, 2);
        gridPane.add(port, 0, 3);
        gridPane.add(e_port, 1, 3);
        gridPane.add(clear, 0,4);
        gridPane.add(login, 1,4);

        //���õ���������������ҵļ��
        GridPane.setMargin(login, new Insets(0,0,0,115));
        gridPane.setAlignment(Pos.CENTER);
        //���ô�ֱ���
        gridPane.setVgap(10);
        //����ˮƽ���
        gridPane.setHgap(5);

        Scene scene = new Scene(gridPane);

        stage.setScene(scene);
        stage.setTitle("RFID�����ͻ���");
        stage.setHeight(800);
        stage.setWidth(800);
        scene.getStylesheets().add(Login.class.getResource("Login.css").toExternalForm());
        stage.show();

        //�󶨼����¼� clear���
        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //��������û���������
                e_name.setText("");
                e_password.setText("");
            }
        });

        //�Ե�¼��ť�󶨼����¼�
        login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IPAddr = "http://" + e_ip.getText() + ":" + e_port.getText() + "/" + "login/ajax";
                System.out.println("loginUrl:     " + IPAddr);
                String username = e_name.getText();
                String password = e_password.getText();
                String requestStr = "{" + "\"username\":" + "\""+ username + "\"" + "," + "\"password\":" + "\"" + password + "\"" +"}";
                System.out.println("requestStr:   " + requestStr);
                try {
                    result = HttpRequest.post(IPAddr).body(requestStr).execute().body();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.titleProperty().set("��Ϣ");
                    alert.headerTextProperty().set(String.valueOf(e));
                    alert.showAndWait();
                }
                System.out.println("info: " + result);
                JSONObject jsResult = JSONObject.fromObject(result);
                String status = jsResult.getString("status");
                String reason = jsResult.getString("reason");
                System.out.println("status:  " + status);
                if (status.equals("true")) {
                    stage.close();
                    NewPage newPage = new NewPage();
                    try {
                        newPage.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.titleProperty().set("��Ϣ");
                    alert.headerTextProperty().set(reason);
                    alert.showAndWait();
                    e_name.setText("");
                    e_password.setText("");
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
