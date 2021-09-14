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
    public AsyncSocketState currentclient;    //��ǰ�����ͻ��˶���
    public static Reader ReaderControllor;
    String flag = "0";
    Boolean cardFlag = false;

    static Device device = new Device();
    static Controller controller = new Controller();

    // ���Ӷ���
    Connection connection = null;
    // Ԥ������� PreparedStatement
    PreparedStatement preparedStatement = null;
    static String IP;
    static String depIDText;
    static String AssertNumberText;
    static int pageIndexText;
    static int pageSizeText;

    // �����
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

    //�������ݵļ���
    private List<NotCard> notCardList = new ArrayList<NotCard>();
    //javaFX �����ݼ���
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
     * ���ñ�񣬰󶨱���ÿ��
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
                    button1 = new Button("����");
                    button1.setStyle("-fx-background-color: #00bcff;-fx-text-fill: #ffffff");
                    button1.setOnMouseClicked((col) -> {
                        cardFlag = false;
                        device = controller.setSuccFlagFalse();
                        //��ȡlist�б��е�λ�ã�������ȡ�б��Ӧ����Ϣ����
                        NotCard notCard = list.get(getIndex());
                        System.out.println(notCard.getAssetName() + notCard.getAssetType());
                        byte bank = (byte)0x01;
                        int startadd = Integer.parseInt("2");
                        int datalen = Integer.parseInt("6");
                        byte fliter = (byte)0x00;                                                //������ʱ��Ч
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
                            System.out.println("����д���ǩָ��ɹ���");
                        } else {
                            System.out.println("����д���ǩָ��ʧ�ܣ�");
                            showAlert("����д���ǩָ��ʧ�ܣ�");
                        }
                        if (cardFlag) {
                            System.out.println("�󿨳ɹ���");
//                            String responseUrl =  "http://" + IP + "/card/asset/notify?assetId=" + notCard.getID();  //��һ��ӿ�
                            String responseUrl =  "http://" + IP + "/api/card/asset/notify?epc=" + notCard.getEPCCode();  //�ڶ���ӿ�
                            System.out.println("responseUrl:   " + responseUrl);
                            String responseInfo = HttpRequest.post(responseUrl).header("Accept", "*/*").execute().body();
                            JSONObject responseObj = JSONObject.fromObject(responseInfo);
                            String status = responseObj.getString("status");
                            System.out.println("status:   " + status);
                            if (status.equals("true")) {
                                System.out.println("���·�����Ϣ�ɹ���");
                                showAlert("���·�����Ϣ�ɹ���");
                                notCardList = getNotCards(pageIndexText, pageSizeText);
                                list.clear();
                                list.addAll(notCardList);
                                table.setItems(list);
                            } else {
                                showAlert("���·�����Ϣʧ�ܣ�");
                            }
                        } else {
                            System.out.println("���·�����Ϣʧ�ܣ�");
                            showAlert("���·�����Ϣʧ�ܣ�");
                        }
                    });
                    if (empty) {
                        //�������Ϊ��Ĭ�ϲ����Ԫ��
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
         * ��ʾ��ѯ����
         */
        private void showData () throws SQLException {
            notCardList = getNotCardData();
            list.addAll(notCardList);
            table.setItems(list);
        }

        /**
         * ������Ϣ����
         */
        private void showAlert(String msg) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("��Ϣ");
            alert.headerTextProperty().set(msg);
            alert.showAndWait();
        }

        /**
         * �������ݿ⣬�����ݶ�ȡ�����
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
         * ����̨�ӿڣ���ȡδ�����ʲ����ݣ������ݶ�ȡ�����
         */
        private List<NotCard> getNotCards(int pageIndex, int pageSize) {
        List<NotCard> notCardList = new ArrayList<NotCard>();
        if (IP.equals(null)) {
            showAlert("IPδ���ã�����ϵͳ���ý������ã�");
        }
        String url = "http://" + IP + "/" + "api/ussued/asset/list?page=" + pageIndex + "&rows=" + pageSize;  //�ڶ���ӿ�
//        String url = "http://" + IP + "/" + "api/card/asset/list?page=" + pageIndex + "&rows=" + pageSize;  //��һ��ӿ�
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

                //�����ѯ����
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

                //��һ��ӿ�(��ѯ����ֻ��ҳ��ҳ��)
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
            alert.titleProperty().set("��Ϣ");
            alert.headerTextProperty().set("��ȡδ��������ʧ�ܣ�response null");
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
