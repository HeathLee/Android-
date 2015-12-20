package com.example.heath.hw_9;

import android.os.Handler;
import android.os.Message;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by heath on 15-12-18.
 */
public class Download implements Runnable {

    private Handler handler;

    public Download(Handler handler) {
        this.handler = handler;
    }

    private final static String NAMESPACE = "http://WebXml.com.cn/";
    private final static String METHODNAME = "enValidateByte";
    private final static String SOAPACTION = "http://WebXml.com" +
            ".cn/enValidateByte";
    private final static String URL = "http://webservice.webxml.com" +
            ".cn/WebServices/ValidateCodeWebService.asmx";

    private final static int UPDATE = 1, FAILED = 2;

    @Override
    public void run() {
        SoapObject request = new SoapObject(NAMESPACE, METHODNAME);
        request.addProperty("byString", MainActivity.str);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope
                (SoapEnvelope.VER10);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(URL);
        try {
            transportSE.call(SOAPACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            SoapPrimitive detail = (SoapPrimitive)result.getProperty
                    ("enValidateByteResult");
            Message msg = new Message();
            msg.what = UPDATE;
            msg.obj = detail;
            handler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Message msg = new Message();
            msg.what = FAILED;
            msg.obj = null;
            handler.sendMessage(msg);
        }

    }
}
