package cs;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Conversion {

	//Demo�����л�����
    public static int EN = 0;
    public static int CN = 1;
    public static int Language = CN;
    
	public Conversion(int language)
	{
		Language = language;
		LoadDemoLanguage();
	}
	
	
    public static String createdate()
    {
    	Date date = new Date();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss SSS");
        String createdate = sdf.format(date);
        return createdate;
    }
    
    public static String NowTime()
    {
    	Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        String createdate = sdf.format(date);
        return createdate;
    }
       
    public static String AsciiToHex(String AsciiStr) 
	{
		byte Byt_Ascii[] = null;
		String StrHex = "";
		try {
			Byt_Ascii = AsciiStr.replace(" ","").getBytes("GBK");
			StrHex = byteArrayToHexString(Byt_Ascii);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Stringת��Ϊbyte[] 
		return StrHex;
	}
	
	public static String HexToAscii(String HexStr)
	{
		byte[] Byt_Hex = toBytes(HexStr);
		String StrAscii = "";
		try {
			StrAscii = new String(Byt_Hex,"GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return StrAscii;
	}
	
	
	
	  /**
     * byte[]����ת��Ϊ16���Ƶ��ַ���
     *
     * @param data Ҫת�����ֽ�����
     * @return ת����Ľ��
     */
    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }
    
    
    
 	/**
     * ��16�����ַ���ת��Ϊbyte[]
     * 
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    } 
    
       
	public static String GetToString()
	{
		if(Language == CN)
		{
			return "��ȡ";
		}
		else
		{
			return "Get";
		}
	}
	
	public static String SetToString()
	{
		if(Language == CN)
		{
			return "����";
		}
		else
		{
			return "Set";
		}
	}
	
	public static String OkToString()
	{
		if(Language == CN)
		{
			return "�ɹ�";
		}
		else
		{
			return "Successful";
		}
	}
	
	public static String FailToString()
	{
		if(Language == CN)
		{
			return "ʧ��";
		}
		else
		{
			return "Fail";
		}
	}
	
	public static String[] OpenSerialPortFailToString()
	{
		String[] result = new String[2];
		if(Language == CN)
		{
			result[0] = "���ڴ�ʧ��";
			result[1] = "�����Ƿ���������������ʹ�øô���";
		}
		else
		{
			result[0] = "Serial port failed to open";
			result[1] = "please check if other programs are using the serial port";
		}
		return result;
	}
    
	public static String[] OpenNetServerFailToString()
	{
		String[] result = new String[2];
		if(Language == CN)
		{
			result[0] = "��������ʧ��";
			result[1] = "�����Ƿ���������������ʹ�ø�IP���˿��Լ�ȷ�ϵ���IPV4��ַ�Ƿ�Ϊ��IP";
		}
		else
		{
			result[0] = "Server failed to open";
			result[1] = "Check to see if other programs are using the IP, port," + "<br/>" + "and verify that the computer's IPV4 address is this IP";
			
			result[1] = "<html>" + "Check to see if other programs are using the IP, port," + "<br>" + 
					    "and verify that the computer's IPV4 address is this IP"+ "</html>";
		}
		return result;
	}
	
	
	public static String[] PwdLenErrorString()
	{
		String[] result = new String[2];
		if(Language == CN)
		{
			result[0] = "���볤�ȴ���";
			result[1] = "";
		}
		else
		{
			result[0] = "Pwd Len Error";
			result[1] = "";
		}
		return result;
	}
	
	
	
    

	public static String lanNetInfo = " ����������� ";
	public static String lanNetName = " ���� ";
	public static String lan4G = " 4G�������� ";
	public static String lanwifi = " WiFi�������� ";
	public static String lanWorkMode = " ����ģʽ ";
	public static String lanContrastEPC = " EPC�ԱȲ��� ";
	public static String lanTimingModeTime = " ��ʱģʽʱ����� ";
	public static String lanMQTTConfig = " MQTT���ò��� ";
	public static String lanMQTTTheme = " MQTT���� ";		
	public static String lanMacAndDev = " MAC��ַ���豸�� ";	
	public static String lanGpioOutState = " GPIO���״̬ ";
	public static String lanPower = " ���� ";
	public static String lanArea = " Ƶ������ ";
	public static String lanSingleAntTime = " ��ͨ�����߹���ʱ�估���ʱ�� ";
	public static String lanMultiAntTime = " ��ͨ�����߹���ʱ�估���ʱ�� ";
	public static String lanMultiWorkAnt = " ��ͨ���������� ";
	public static String lanFastID = " FastID���� ";
	public static String lanEPCAndTID = " ͬʱ��ȡEPC��TID ";  
	public static String lanTagfocus = " Tagfocus ";
	public static String lanReadTags = " ��ȡ��ǩ���� ";
	public static String lanWriteTags = " д���ǩ���� ";
	public static String lanLockTags = " ������ǩ ";
	public static String lanKillTags = " ���ٱ�ǩ ";
	public static String lanTemp = " �¶� ";
	public static String lanCarrier = " Carrier ";
	public static String lanRFLink = " RF��· ";
	public static String lanDevConfig_24G = " 2.4G�豸���ò��� ";
	public static String lanMoudleVersion = " ģ��汾 ";
	public static String lanReadWriteOutTime = " ��д��ǩ��ʱʱ�� ";
	
    
    public void LoadDemoLanguage()
    {
    	if(Language != CN)
		{
    		lanNetInfo = " Net Config ";
    		lanNetName = " NetName ";
    		lan4G = " 4G Config ";    		   		
    		lanwifi = " WiFi Config ";   	   		
    		lanWorkMode = " WorkMode ";
    		lanContrastEPC = " EPC comparison parameter ";
    		lanTimingModeTime = " TimingMode Time ";
    		lanMQTTConfig = " MQTT Config ";
    		lanMQTTTheme = " MQTT Theme ";
    		lanMacAndDev = " DevMAC And DevID ";  	
    		lanGpioOutState = " GPIO Out State ";   
    		lanPower = " Power ";
    		lanArea = " RF Area ";
    		lanSingleAntTime = " Single channel antenna working time and interval time ";
    		lanMultiAntTime = " Multi channel antenna working time and interval time ";
    		lanMultiWorkAnt = " Multi-channel working antenna ";
    		lanFastID = " FastID Config ";
    		lanEPCAndTID = " EPC And TID ";   
    		lanTagfocus = " Tagfocus ";
    		lanReadTags = " Read Tags Data ";
    		lanWriteTags = " Write Tags Data ";
    		lanLockTags = " Lock Tags ";
    		lanKillTags = " Kill Tags ";
    		lanTemp = " Temp ";
    		lanRFLink = " RF Link ";
    		lanDevConfig_24G = " 2.4G DevConfig ";   
    		lanMoudleVersion = " MoudleVersion ";
    		lanReadWriteOutTime = " ReadAndWriteTimeOut ";
		}
    }
    
    
    public static String GetState(String clientstate)
    {
    	String StrState = "";
    	if(Language == CN)
    	{
    		if(clientstate.equals("OK")) StrState = "����";
    		else if(clientstate.equals("NG")) StrState = "����";
    	}
    	else
    	{
    		StrState = clientstate;
    	}
    	return StrState;
    }
    
    
    public static String GetDemoVersion()
    {
    	//UHF_Demo[V2.1.2]-����Ⱥ�����ù���(��ָ�������ǩ/Ⱥ��User/���¶ȱ�ǩ)������9200Rssi����
    	//UHF_Demo[V2.1.3]-user��ʼ��ַ�ɴ���255
    	
    	return "UHF_Demo[V2.1.3]";
    }
    
    
    
    
    
}
