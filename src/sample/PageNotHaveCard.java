package sample;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import uhf.AsyncSocketState;
import uhf.Reader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PageNotHaveCard {
    public AsyncSocketState currentclient;    //当前操作客户端对象
    public static Reader ReaderControllor;
    String flag = "0";
    Boolean cardFlag = false;

    static Device device = new Device();
    static Controller controller = new Controller();

    // 连接对象
    Connection connection = null;
    // 预编译对象 PreparedStatement
    PreparedStatement preparedStatement = null;
    static String IP;
    static String depIDText;
    static String AssertNumberText;
    static int pageIndexText;
    static int pageSizeText;

    // 结果集
    ResultSet resultSet = null;
    String queryString = "SELECT ID,RFID_NUMBER,NAME,CATEGORY_ID,ADMINISTRATIVE_DEPARTMENT_ID,ADMINISTRATOR_ID FROM ASSET_BASE_INFO ";

    private Button button1;

    @FXML
    private TableView<NotCard> table;
    @FXML
    private TableColumn<NotCard, String> AssetNum;
    @FXML
    private TableColumn<NotCard, String> EPCCode;
    @FXML
    private TableColumn<NotCard, String> AssetName;
    @FXML
    private TableColumn<NotCard, String> AssetType;
    @FXML
    private TableColumn<NotCard, String> Department;
    @FXML
    private TableColumn<NotCard, String> DepartmentId;
    @FXML
    private TableColumn<NotCard, String> LiablePerson;
    @FXML
    private TableColumn<NotCard, String> LiablePersonId;
    @FXML
    private TableColumn<NotCard, String> AdminEmployeeName;
    @FXML
    private TableColumn<NotCard, String> AdminEmployeeId;
    @FXML
    private TableColumn<NotCard, String> StorageLocationName;
    @FXML
    private TableColumn<NotCard, String> StorageLocationId;
    @FXML
    private TableColumn<NotCard, String> MakeCard;
    @FXML
    public TextField depID;
    @FXML
    private TextField AssetNumber;
    @FXML
    private TextField pageIndex;
    @FXML
    private TextField pageSize;
    @FXML
    private TextField userID;
    @FXML
    private TextField adminID;
    @FXML
    private TextField locationID;

    //放置数据的集合
    private List<NotCard> notCardList = new ArrayList<NotCard>();
    //javaFX 的数据集合
    private ObservableList<NotCard> list = FXCollections.observableArrayList();

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() throws SQLException {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        configTableView();
        pageIndex.setText("1");
        pageSize.setText("15");

//        showData();
    }

    /**
     * 配置表格，绑定表格的每列
     */
    private void configTableView() {
        AssetNum.setCellValueFactory(cellData -> cellData.getValue().assetNumProperty());
        EPCCode.setCellValueFactory(cellData -> cellData.getValue().EPCCodeProperty());
        AssetName.setCellValueFactory(cellData -> cellData.getValue().assetNameProperty());
        AssetType.setCellValueFactory(cellData -> cellData.getValue().assetTypeProperty());
        Department.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        DepartmentId.setCellValueFactory(cellData -> cellData.getValue().departmentIdProperty());
        LiablePerson.setCellValueFactory(cellData -> cellData.getValue().liablePersonProperty());
        LiablePersonId.setCellValueFactory(cellData -> cellData.getValue().liablePersonIdProperty());
        AdminEmployeeName.setCellValueFactory(cellData -> cellData.getValue().adminEmployeeNameProperty());
        AdminEmployeeId.setCellValueFactory(cellData -> cellData.getValue().adminEmployeeIdProperty());
        StorageLocationName.setCellValueFactory(cellData -> cellData.getValue().storageLocationNameProperty());
        StorageLocationId.setCellValueFactory(cellData -> cellData.getValue().storageLocationIdProperty());

        MakeCard.setCellFactory((col) -> {
            TableCell<NotCard, String> cell = new TableCell<NotCard, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    button1 = new Button("发卡");
                    button1.setStyle("-fx-background-color: #00bcff;-fx-text-fill: #ffffff");
                    button1.setOnMouseClicked((col) -> {
                        cardFlag = false;
                        device = controller.setSuccFlagFalse();
                        //获取list列表中的位置，进而获取列表对应的信息数据
                        NotCard notCard = list.get(getIndex());
                        System.out.println(notCard.getAssetName() + notCard.getAssetType());
                        byte bank = (byte)0x01;
                        int startadd = Integer.parseInt("2");
                        int datalen = Integer.parseInt("6");
                        byte fliter = (byte)0x00;                                                //不过滤时无效
                        String fliterdata = "";
                        String pwd = "00000000";
                        String writedata = notCard.getEPCCode();
                        System.out.println("writedata:  " + writedata);
                        String readCardResult = ReaderControllor.WriteTags(currentclient, pwd, fliter, fliterdata, bank, startadd, datalen,writedata);
                        try {
                            Thread.currentThread().sleep(1000);
                            device = controller.setConnInfo();
                            cardFlag = device.getWriteSuccFlag();
                            System.out.println("cardFlag:  " + cardFlag);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (readCardResult.equals("0")) {
                            System.out.println("发送写入标签指令成功！");
                        } else {
                            System.out.println("发送写入标签指令失败！");
                            showAlert("发送写入标签指令失败！");
                        }
                        if (cardFlag) {
                            System.out.println("绑卡成功！");
//                            String responseUrl =  "http://" + IP + "/card/asset/notify?assetId=" + notCard.getID();  //第一版接口
                            String responseUrl =  "http://" + IP + "/api/card/asset/notify?epc=" + notCard.getEPCCode();  //第二版接口
                            System.out.println("responseUrl:   " + responseUrl);
                            String responseInfo = HttpRequest.post(responseUrl).header("Accept", "*/*").execute().body();
                            JSONObject responseObj = JSONObject.fromObject(responseInfo);
                            String status = responseObj.getString("status");
                            System.out.println("status:   " + status);
                            if (status.equals("true")) {
                                System.out.println("更新发卡信息成功！");
                                showAlert("更新发卡信息成功！");
                                notCardList = getNotCards(pageIndexText, pageSizeText);
                                list.clear();
                                list.addAll(notCardList);
                                table.setItems(list);
                            } else {
                                showAlert("更新发卡信息失败！");
                            }
                        } else {
                            System.out.println("更新发卡信息失败！");
                            showAlert("更新发卡信息失败！");
                        }
                    });
                    if (empty) {
                        //如果此列为空默认不添加元素
                        setText(null);
                        setGraphic(null);
                    } else {
                        setGraphic(button1);
                    }
                }
            };
            return cell;
        });

    }

        /**
         * 显示查询数据
         */
        private void showData () throws SQLException {
            notCardList = getNotCardData();
            list.addAll(notCardList);
            table.setItems(list);
        }

        /**
         * 警告信息弹窗
         */
        private void showAlert(String msg) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("信息");
            alert.headerTextProperty().set(msg);
            alert.showAndWait();
        }

        /**
         * 连接数据库，将数据读取到表格
         */
        private List<NotCard> getNotCardData () throws SQLException {
            List<NotCard> notCardList = new ArrayList<NotCard>();
            connection = Connect.getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                NotCard notCard = new NotCard();
                String ID = String.valueOf(resultSet.getInt("ID"));
                String RFID_NUMBER = resultSet.getString("RFID_NUMBER");
                String NAME = resultSet.getString("NAME");
                String CATEGORY_ID = String.valueOf(resultSet.getInt("CATEGORY_ID"));
                String ADMINISTRATIVE_DEPARTMENT_ID = String.valueOf(resultSet.getInt("ADMINISTRATIVE_DEPARTMENT_ID"));
                String ADMINISTRATOR_ID = String.valueOf(resultSet.getInt("ADMINISTRATOR_ID"));
                notCard.setID(ID);
                notCard.setAssetNum(ID);
                notCard.setEPCCode(RFID_NUMBER);
                notCard.setAssetName(NAME);
                notCard.setAssetType(CATEGORY_ID);
                notCard.setDepartment(ADMINISTRATIVE_DEPARTMENT_ID);
                notCard.setLiablePerson(ADMINISTRATOR_ID);
                notCardList.add(notCard);
            }
            return notCardList;
        }

        /**
         * 调后台接口，获取未发卡资产数据，将数据读取到表格
         */
        private List<NotCard> getNotCards(int pageIndex, int pageSize) {
        List<NotCard> notCardList = new ArrayList<NotCard>();
        if (IP.equals(null)) {
            showAlert("IP未设置，请于系统设置界面设置！");
        }
        String url = "http://" + IP + "/" + "api/ussued/asset/list?page=" + pageIndex + "&rows=" + pageSize;  //第二版接口
//        String url = "http://" + IP + "/" + "api/card/asset/list?page=" + pageIndex + "&rows=" + pageSize;  //第一版接口
        depIDText = depID.getText();
        if (StringUtils.isNotBlank(depIDText)) {
            url = url + "&useOrgId=" + depIDText;
        }
        AssertNumberText = AssetNumber.getText();
        if (StringUtils.isNotBlank(AssertNumberText)) {
            url = url + "&assetNumber=" + AssertNumberText;
        }
        String userIDText = userID.getText();
        if (StringUtils.isNotBlank(userIDText)) {
            url = url + "&userEmployeeId=" + userIDText;
        }
        String adminIDText = adminID.getText();
        if (StringUtils.isNotBlank(adminIDText)) {
            url = url + "&administratorId=" + adminIDText;
        }
        String locationIDText = locationID.getText();
        if (StringUtils.isNotBlank(locationIDText)) {
            url = url + "&locationId" + locationIDText;
        }
        System.out.println("url:     " + url);
        String info = HttpUtil.get(url);
        System.out.println("info:    " + info);
//        JSONObject object = JSONObject.fromObject(info);
//        showAlert(object.getString("reason"));
        try {
            JSONObject j = JSONObject.fromObject(info);
            JSONArray records = j.getJSONArray("records");
            System.out.println("data size:  " + records.size());
            for (int i = 0; i< records.size(); i++) {

                //多个查询条件
                JSONObject key1 = (JSONObject)records.get(i);
                NotCard notCard = new NotCard();
                notCard.setID(key1.getString("id"));
                notCard.setAssetNum(key1.getString("assetNumber"));
                notCard.setEPCCode(key1.getString("rfidNumber"));
                notCard.setAssetName(key1.getString("assetName"));
                notCard.setAssetType(key1.getString("categoryId"));
                notCard.setDepartment(key1.getString("usingDepartmentName"));
                notCard.setDepartmentId((key1.getString("usingDepartmentId")));
                notCard.setLiablePerson(key1.getString("usingEmployeeName"));
                notCard.setLiablePersonId(key1.getString("usingEmployeeId"));
                notCard.setAdminEmployeeName(key1.getString("adminEmployeeName"));
                notCard.setAdminEmployeeId((key1.getString("adminEmployeeId")));
                notCard.setStorageLocationName(key1.getString("storageLocationName"));
                notCard.setStorageLocationId(key1.getString("storageLocationId"));
                notCardList.add(notCard);

                //第一版接口(查询条件只有页码页数)
//                JSONObject key1 = (JSONObject)records.get(i);
//                NotCard notCard = new NotCard();
//                notCard.setID(key1.getString("id"));
//                notCard.setAssetNum(key1.getString("assetNumber"));
//                notCard.setEPCCode(key1.getString("rfidNumber"));
//                notCard.setAssetName(key1.getString("name"));
//                notCard.setAssetType(key1.getString("categoryId"));
//                notCard.setDepartment(key1.getString("userDepartmentCode"));
//                notCard.setLiablePerson(key1.getString("userEmployeeCode"));
//                notCardList.add(notCard);
            }
        } catch (Exception e) {
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("信息");
            alert.headerTextProperty().set("获取未发卡数据失败，response null");
            alert.showAndWait();
        }
        return notCardList;
    }

    @FXML
    public void queryNotCard(ActionEvent actionEvent) {
        list.clear();
        table.setItems(null);
        controller.setIP();
        device = controller.setConnInfo();
        ReaderControllor =  device.ReaderControllor;
        currentclient = ReaderControllor.GetClientInfo().get(0);
        IP = device.getIP();
        pageIndexText = Integer.parseInt(pageIndex.getText());
        pageSizeText = Integer.parseInt(pageSize.getText());
        notCardList = getNotCards(pageIndexText, pageSizeText);
        list.addAll(notCardList);
//        table.setEditable(true);
        table.setItems(list);
    }

}
