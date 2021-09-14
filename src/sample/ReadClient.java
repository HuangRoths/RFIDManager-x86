package sample;

import cs.Conversion;
import uhf.AsyncSocketState;
import uhf.MultiLableCallBack;
import uhf.Reader;
import uhf.Types;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ReadClient {

    private CountDownLatch countDownLatch;
    private final Reader reader;
    private String result;
    private static String writeSuccFlag = "false";
    String flag = "0";

    public ReadClient(){
        reader = new Reader(new MultiLableCallBack() {
            @Override
            public void method(String data) {
                if (countDownLatch != null){
                    countDownLatch.countDown();
                }
                writeSuccFlag = "false";
                //»®·Ö×Ö·û´®
                String[] result = (data + "," + flag).split("\\,");
                String responseCode = result[3];
                System.out.println("responseCode: " + responseCode);
                byte type = Conversion.toBytes(result[1])[0];
                switch (type) {
                    case Types.READ_TAGS_RESPOND:
                        System.out.println("READ_TAGS--------------");
                        break;
                    case Types.WRITE_TAGS_RESPOND:
                        System.out.println("WRITE_TAGS-------------");
                        if ("1".equals(responseCode)) {
                            writeSuccFlag = "true";
                            System.out.println("cardFlag:  " + writeSuccFlag);
                        } else {
                            writeSuccFlag = "false";
                            System.out.println("writeSuccFlag:  " + writeSuccFlag);
                        }
                        break;
                }
            }

            @Override
            public void ReaderNotice(String s) {

            }
        });
    }

    public Map<String, String> WriteTags(AsyncSocketState state, String str_pwd, byte fliter, String fliterdata, byte bank, int startAdd, int datalen, String data) {
        Map<String, String> map = new HashMap<>();
        try {
            String s = reader.WriteTags(state, str_pwd, fliter, fliterdata, bank, startAdd, datalen, data);
            map.put("status", s);
            if ("0".equals(s)){
                countDownLatch = new CountDownLatch(1);
                boolean await = countDownLatch.await(5, TimeUnit.SECONDS);
                if (await) {
                    map.put("writeSuccFlag", writeSuccFlag);
                    return map;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return map;
    }
}
