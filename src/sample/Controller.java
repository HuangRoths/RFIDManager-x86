package sample;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.fazecast.jSerialComm.SerialPort;
import cs.Conversion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import uhf.AsyncSocketState;
import uhf.MultiLableCallBack;
import uhf.Reader;
import uhf.Types;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Controller{

    public static CountDownLatch countDownLatch;
    //��ȡ��ǰ���ô���
    SerialPort[] portList;
    ArrayList<String> portNameList;
    //ѡ��Ĵ���
    String ComName = "";
    static String IP;
    static Boolean writeSuccFlag = false;

    static Device device = new Device();
    public static Reader ReaderControllor;
    List<AsyncSocketState> clients;           //�ͻ�����Ϣ
    public AsyncSocketState currentclient;    //��ǰ�����ͻ��˶���
    String flag = "0";
    int power = 10;
    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
    Date date = new Date(System.currentTimeMillis());

    @FXML
    private Button openButton;

    @FXML
    private TextField ipOne;
    @FXML
    private TextField ipTwo;
    @FXML
    private TextField ipThree;
    @FXML
    private TextField ipFour;
    @FXML
    private TextField ipPort;
    @FXML
    private TextField epcText;
    @FXML
    private TextField assetNumber;
    @FXML
    private TextField assetName;
    @FXML
    private TextField department;
    @FXML
    private TextField manufacturer;
    @FXML
    private TextField storageLocation;
    @FXML
    private TextField user;

    @FXML
    private ComboBox comboBox;
    @FXML
    private ComboBox comboBox2;


    //���Ӵ���
    @FXML
    public void openButtonAction(javafx.event.ActionEvent actionEvent) {
        connectCom();
    }

    //ˢ�´����б�
    @FXML
    public void refreshComs(ActionEvent actionEvent) {
        comboBox.getItems().clear();
        GetCom();
        comboBox.getItems().addAll(portNameList);
    }

    //���÷��书��
    @FXML
    public void setPower(ActionEvent actionEvent) {
        power = gainPower();
        if (clients.size() == 1) currentclient = clients.get(0);// ֻ��һ̨���ӵ�ʱ��ֱ��Ĭ��ѡ����һ̨
        ReaderControllor.SetPower(currentclient,(byte)power,(byte)power);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set("��Ϣ");
        alert.headerTextProperty().set("���书�����óɹ���");
        alert.showAndWait();
    }

    //���÷�����IP��ַ�Ͷ˿�
    @FXML
    public void setIP(ActionEvent actionEvent) {
        String ip1 = ipOne.getText();
        String ip2 = ipTwo.getText();
        String ip3 = ipThree.getText();
        String ip4 = ipFour.getText();
        String ipport = ipPort.getText();
        IP = ip1 + "." + ip2 +"." + ip3 +"." + ip4 + ":" +ipport;
        System.out.println("ip:   " + IP);
        this.device.setIP(IP);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set("��Ϣ");
        alert.headerTextProperty().set("���óɹ�");
        alert.showAndWait();
    }

    public void setIP() {
        System.out.println("New Set ip:   " + IP);
        this.device.setIP(IP);
    }

    @FXML
    public void initialize() {
        GetCom();
        ipOne.setText("10");
        ipTwo.setText("36");
        ipThree.setText("16");
        ipFour.setText("113");
        ipPort.setText("8181");
        comboBox.getItems().addAll(portNameList);
        comboBox2.setValue("10");
        comboBox2.getItems().addAll("5","6","7","8","9","10",
                                                "11","12","13","14","15","16","17","18","19","20",
                                                "21","22","23","24","25","26","27","28","29","30");
        ReaderControllor = new Reader(new MultiLableCallBack() {
            @Override
            public void method(String data) {
                if (countDownLatch != null){
                    countDownLatch.countDown();
                }
                writeSuccFlag = false;
                device.setWriteSuccFlag(false);
                //�����ַ���
                String[] result = (data + "," + flag).split("\\,");
                String responseCode = result[2];
                System.out.println("responseCode:  " + responseCode);
                byte type = Conversion.toBytes(result[1])[0];
                device = setConnInfo();
                switch (type) {
                    case Types.READ_TAGS_RESPOND:
                        System.out.println("READ_TAGS--------------");
                        epcText.setText(result[3]);
                        String EPCText = result[3];
//                        List<String> EPCList = new ArrayList<String>();
//                        EPCList.add(EPCText);
                        String queryEPCUrl = "http://" + IP + "/" + "api/assetInfo/epcs";
                        String EPCInfo = HttpRequest.post(queryEPCUrl).body(JSONUtil.toJsonStr(new String[]{EPCText})).header("Accept", "*/*").execute().body();
                        System.out.println("EPCInfo:    " + EPCInfo);
                        JSONObject j = JSONObject.fromObject(EPCInfo);
                        JSONArray records = j.getJSONArray("records");
                        if (!records.isEmpty()) {
                            JSONObject key1 = (JSONObject)records.get(0);
                            assetName.setText(key1.getString("assetName"));
                            assetNumber.setText(key1.getString("assetNumber"));
                            department.setText(key1.getString("department"));
                            manufacturer.setText(key1.getString("manufacturer"));
                            storageLocation.setText(key1.getString("storageLocation"));
                            user.setText(key1.getString("user"));
                        } else {
                            assetName.setText("");
                            assetNumber.setText("");
                            department.setText("");
                            manufacturer.setText("");
                            storageLocation.setText("");
                            user.setText("");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.titleProperty().set("��Ϣ");
                            alert.headerTextProperty().set("û�в鵽�������");
                            alert.showAndWait();
                        }

//                        EpcInfo epcInfo = new EpcInfo();
//                        epcInfo.setAssetName(key1.getString("assetName"));
//                        epcInfo.setAssetNumber(key1.getString("assetNumber"));
//                        epcInfo.setDepartment(key1.getString("department"));
//                        epcInfo.setManufacturer(key1.getString("manufacturer"));
//                        epcInfo.setStorageLocation(key1.getString("storageLocation"));
//                        epcInfo.setUser(key1.getString("user"));
                        break;
                    case Types.WRITE_TAGS_RESPOND:
                        System.out.println("WRITE_TAGS-------------");
                        if ("1".equals(responseCode)) {
                            writeSuccFlag = true;
                            System.out.println("writeSuccFlag:  " + writeSuccFlag);
                            device = setConnInfo();
                        } else {
                            writeSuccFlag = false;
                            System.out.println("writeSuccFlag:  " + writeSuccFlag);
                            device.setWriteSuccFlag(false);
                            device = setSuccFlagFalse();
                        }
                        break;
                }
            }

            @Override
            public void ReaderNotice(String s) {

            }
        });
    }

    /**
     * ��ȡ��ǰ���п��ô���
     * */
//    public void GetCom()
//    {
//        System.out.println(System.getProperty("java.library.path"));
//        portNameList = new ArrayList<>();
//        portList = SerialPort.getCommPorts();
//        for(int i = 0;i<portList.length;i++)
//        {
//            String name = portList[i].getSystemPortName();
//            portNameList.add(name);
//            System.out.println(name);
//        }
//    }

    /**
     * ��ȡ��ǰ���п��ô���(Linux)
     * */
    public void GetCom()
    {
        portNameList = new ArrayList<>();
        portList = SerialPort.getCommPorts();
        for(int i = 0;i<portList.length;i++)
        {
            String name = portList[i].getSystemPortName();
            System.out.println("portName:  " + name);
            portNameList.add(name);
            System.out.println(name);
        }
    }

    /**
     * ��or�ر� ����
     * */
    public void connectCom() {
        ComName = comboBox.getValue().toString().replace("[","").replace("]","");
        System.out.println("COMName:    " + ComName);
        ReaderControllor.IsLinuxSerial = true;
        boolean result = ReaderControllor.ComStart(ComName, 115200);
        System.out.println("open Com or not:    " + result);
        if (openButton.getText().equals("��")) {
            if (result == true) {
                openButton.setText("�ر�");
                clients = ReaderControllor.GetClientInfo();
                System.out.println("clients sizes:   " + clients.size());
                if (clients.size() == 1) currentclient = clients.get(0);// ֻ��һ̨���ӵ�ʱ��ֱ��Ĭ��ѡ����һ̨
                for(int i = 0;i<clients.size();i++)
                {
                    ReaderControllor.GetMACDev(clients.get(i));
                    ReaderControllor.GetWorkMode(clients.get(i));
                }

            } else {
                ShowPromptView("COM");
            }
        } else {
            ReaderControllor.SerialPortClose();
            openButton.setText("��");
        }
        device = setConnInfo();
    }

    public Device setConnInfo() {
        device.setCurrentclient(currentclient);
        device.setReaderControllor(ReaderControllor);
        device.setIP(IP);
        device.setWriteSuccFlag(writeSuccFlag);
        return device;
    }

    public Device setSuccFlagFalse() {
        device.setCurrentclient(currentclient);
        device.setReaderControllor(ReaderControllor);
        device.setIP(IP);
        writeSuccFlag = false;
        device.setWriteSuccFlag(writeSuccFlag);
        return device;
    }

    //�򿪴�����ʧ����ʾ����
    public void ShowPromptView(String OpenFailType)
    {
        if(OpenFailType.equals("COM")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("��Ϣ");
            alert.headerTextProperty().set("���ڴ�ʧ�ܣ������Ƿ�����������ʹ�ô���");
            alert.showAndWait();
        }
        else if(OpenFailType.equals("NET")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("��Ϣ");
            alert.headerTextProperty().set("��������ʧ�ܣ������Ƿ�����������ʹ�ö˿�");
            alert.showAndWait();
        }
    }

    //��ȡ���书��ֵ
    public int gainPower() {
        power = Integer.parseInt(comboBox2.getValue().toString());
        return power;
    }

    @FXML
    public void readCard(ActionEvent actionEvent) {
        byte bank = (byte)0x01;
        int startadd = Integer.parseInt("2");
        int readlen = Integer.parseInt("6");
        byte fliter = (byte)0x00;                                                //������ʱ��Ч
        String fliterdata = "";
        String pwd = "00000000";
        if (clients.size() == 1) currentclient = clients.get(0);// ֻ��һ̨���ӵ�ʱ��ֱ��Ĭ��ѡ����һ̨
        String readCardResult = ReaderControllor.ReadTags(currentclient, pwd, fliter, fliterdata, bank, startadd, readlen);
        if (readCardResult.equals("0")) {
            System.out.println("���Ͷ�ȡ��ǩָ��ɹ���");
        } else {
            System.out.println("���Ͷ�ȡ��ǩָ��ʧ�ܣ�");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("��Ϣ");
            alert.headerTextProperty().set("���Ͷ�ȡ��ǩָ��ʧ�ܣ�");
            alert.showAndWait();
        }
    }

    public void writeCard(ActionEvent actionEvent) {
        byte bank = (byte)0x01;
        int startadd = Integer.parseInt("2");
        int datalen = Integer.parseInt("6");
        byte fliter = (byte)0x00;                                                //������ʱ��Ч
        String fliterdata = "";
        String pwd = "00000000";
        String writedata = epcText.getText();
        if (clients.size() == 1) currentclient = clients.get(0);// ֻ��һ̨���ӵ�ʱ��ֱ��Ĭ��ѡ����һ̨
        String readCardResult = ReaderControllor.WriteTags(currentclient, pwd, fliter, fliterdata, bank, startadd, datalen,writedata);
        if (readCardResult.equals("0")) {
            System.out.println("����д���ǩָ��ɹ���");
        } else {
            System.out.println("����д���ǩָ��ʧ�ܣ�");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("��Ϣ");
            alert.headerTextProperty().set("����д���ǩָ��ʧ�ܣ�");
            alert.showAndWait();
        }
    }

//    //������־����
//    public List<MyLog> generateLogList(String type, String logText) {
//        List<MyLog> myLogList = new ArrayList<>();
//        MyLog log = new MyLog(formatter.format(date), type, logText);
//        myLogList.add(log);
//        return myLogList;
//    }
}
