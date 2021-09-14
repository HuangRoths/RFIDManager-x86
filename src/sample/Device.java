package sample;

import uhf.AsyncSocketState;
import uhf.Reader;

public class Device {
    public Reader ReaderControllor;
    public AsyncSocketState currentclient;    //当前操作客户端对象
    public String IP;
    public Boolean writeSuccFlag;

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public Reader getReaderControllor() {
        return ReaderControllor;
    }

    public void setReaderControllor(Reader readerControllor) {
        ReaderControllor = readerControllor;
    }

    public AsyncSocketState getCurrentclient() {
        return currentclient;
    }

    public void setCurrentclient(AsyncSocketState currentclient) {
        this.currentclient = currentclient;
    }

    public Boolean getWriteSuccFlag() {
        return writeSuccFlag;
    }

    public void setWriteSuccFlag(Boolean writeSuccFlag) {
        this.writeSuccFlag = writeSuccFlag;
    }
}
