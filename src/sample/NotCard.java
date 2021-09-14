package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class NotCard {
    private  SimpleStringProperty ID;
    private  SimpleStringProperty AssetNum;
    private  SimpleStringProperty EPCCode;
    private  SimpleStringProperty AssetName;
    private  SimpleStringProperty AssetType;
    private  SimpleStringProperty Department;
    private  SimpleStringProperty LiablePerson;
    private  SimpleStringProperty DepartmentId;
    private  SimpleStringProperty LiablePersonId;
    private  SimpleStringProperty AdminEmployeeName;
    private  SimpleStringProperty AdminEmployeeId;
    private  SimpleStringProperty StorageLocationName;
    private  SimpleStringProperty StorageLocationId;
//    private  SimpleStringProperty MakeCard;

    public String getID() {
        return ID.get();
    }
    public ObservableValue<String> IDProperty() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = new SimpleStringProperty(ID);
    }

    public String getAssetNum() {
        return AssetNum.get();
    }
    public ObservableValue<String> assetNumProperty() {
        return AssetNum;
    }
    public void setAssetNum(String assetNum) {
        this.AssetNum = new SimpleStringProperty(assetNum);;
    }

    public String getEPCCode() {
        return EPCCode.get();
    }
    public ObservableValue<String> EPCCodeProperty() {
        return EPCCode;
    }
    public void setEPCCode(String EPCCode) {
        this.EPCCode = new SimpleStringProperty(EPCCode);
    }

    public String getAssetName() {
        return AssetName.get();
    }
    public ObservableValue<String> assetNameProperty() {
        return AssetName;
    }
    public void setAssetName(String assetName) {
        this.AssetName = new SimpleStringProperty(assetName);
    }

    public String getAssetType() {
        return AssetType.get();
    }
    public ObservableValue<String> assetTypeProperty() {
        return AssetType;
    }
    public void setAssetType(String assetType) {
        this.AssetType = new SimpleStringProperty(assetType);
    }

    public String getDepartment() {
        return Department.get();
    }
    public ObservableValue<String> departmentProperty() {
        return Department;
    }
    public void setDepartment(String department) {
        this.Department = new SimpleStringProperty(department);
    }

    public String getDepartmentId() {
        return DepartmentId.get();
    }
    public ObservableValue<String> departmentIdProperty() {
        return DepartmentId;
    }
    public void setDepartmentId(String departmentId) {
        this.DepartmentId = new SimpleStringProperty(departmentId);
    }

    public String getLiablePerson() {
        return LiablePerson.get();
    }
    public ObservableValue<String> liablePersonProperty() {
        return LiablePerson;
    }
    public void setLiablePerson(String liablePerson) {
        this.LiablePerson = new SimpleStringProperty(liablePerson);
    }

    public String getLiablePersonId() {
        return LiablePersonId.get();
    }
    public ObservableValue<String> liablePersonIdProperty() {
        return LiablePersonId;
    }
    public void setLiablePersonId(String liablePersonId) {
        this.LiablePersonId = new SimpleStringProperty(liablePersonId);
    }

    public String getAdminEmployeeName() {
        return AdminEmployeeName.get();
    }
    public ObservableValue<String> adminEmployeeNameProperty() {
        return AdminEmployeeName;
    }
    public void setAdminEmployeeName(String AdminEmployeeName) {
        this.AdminEmployeeName = new SimpleStringProperty(AdminEmployeeName);
    }

    public String getAdminEmployeeId() {
        return AdminEmployeeId.get();
    }
    public ObservableValue<String> adminEmployeeIdProperty() {
        return AdminEmployeeId;
    }
    public void setAdminEmployeeId(String AdminEmployeeId) {
        this.AdminEmployeeId = new SimpleStringProperty(AdminEmployeeId);
    }

    public String getStorageLocationName() {
        return StorageLocationName.get();
    }
    public ObservableValue<String> storageLocationNameProperty() {
        return StorageLocationName;
    }
    public void setStorageLocationName(String StorageLocationName) {
        this.StorageLocationName = new SimpleStringProperty(StorageLocationName);
    }

    public String getStorageLocationId() {
        return StorageLocationId.get();
    }
    public ObservableValue<String> storageLocationIdProperty() {
        return StorageLocationId;
    }
    public void setStorageLocationId(String StorageLocationId) {
        this.StorageLocationId = new SimpleStringProperty(StorageLocationId);
    }

}
