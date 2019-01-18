package com.itg.httpRequest;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class SceneryOnlineService {

	public static  String GetWsMsg1(String url,String soapAction,String methodName,String[] ParameterName,String[] Parames)
	{	
		String nameSpace= "http://tempuri.org/";
//		String nameSpace= "http://www.cgManage.com";
		org.ksoap2.transport.HttpTransportSE transport = new HttpTransportSE(url);
		transport.debug = true;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		if(Parames!=null){

			for (int i = 0; i < Parames.length; i++) {
				soapObject.addProperty(ParameterName[i], Parames[i]);
			}
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = transport;
		envelope.setOutputSoapObject(soapObject);
		try
		{
			transport.call(soapAction, envelope);
			String str = String.valueOf(envelope.getResponse());
			return str;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}


}
