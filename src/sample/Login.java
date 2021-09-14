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

    //这个是准备的数据  用来模拟登录使用
    @Override
    public void init() throws Exception {

        map = new HashMap<String,String>();
        map.put("user", "pw");
    }

    @Override
    public void start(Stage stage) {

        Text name = new Text("用户名：");
        Text password = new Text("密码：");
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
        e_password.setPromptText("请输入密码：");

        Button clear = new Button("重新输入");
        Button login = new Button("登录");

        //网格布局
        GridPane gridPane = new GridPane();
        //设置网格位置
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

        //设置单独组件的上下左右的间距
        GridPane.setMargin(login, new Insets(0,0,0,115));
        gridPane.setAlignment(Pos.CENTER);
        //设置垂直间距
        gridPane.setVgap(10);
        //设置水平间距
        gridPane.setHgap(5);

        Scene scene = new Scene(gridPane);

        stage.setScene(scene);
        stage.setTitle("RFID发卡客户端");
        stage.setHeight(800);
        stage.setWidth(800);
        scene.getStylesheets().add(Login.class.getResource("Login.css").toExternalForm());
        stage.show();

        //绑定监听事件 clear清除
        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //单击清除用户名和密码
                e_name.setText("");
                e_password.setText("");
            }
        });

        //对登录按钮绑定监听事件
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
                    alert.titleProperty().set("信息");
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
                    alert.titleProperty().set("信息");
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
