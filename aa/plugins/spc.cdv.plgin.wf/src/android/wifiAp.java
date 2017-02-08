package spc.cdv.plgin.wf;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;

import android.content.Context;
import android.telephony.TelephonyManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import java.lang.reflect.Method;
import android.app.Activity;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

// This class echoes a string called from JavaScript.

public class wifiAp extends CordovaPlugin 
{
	//public static final String wfAP;  
	private WifiManager wifiManager;
	private Method method,method2,method3,method4;
	private WifiConfiguration apConfig,config;
	private String flg,user,pw,ecpt;
	public void initialize(CordovaInterface cordova, CordovaWebView webView) 
	{  //初始化 
		super.initialize(cordova, webView);  
		Context context = this.cordova.getActivity().getApplicationContext();  
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); //获取wifi管理服务
		flg=""; 
		user="";
		pw="";
		ecpt="";                     
	} 	  
	@Override  
	//args是页面传入的参数，支持String， JsonArray，CordovaArgs 等三种不同的类型。
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException   
	{ 		  	
		if(action.equals("wfAP"))	//开关wifi热点功能		  			  		  							
		{	
			flg=args.getString(0);
			user=args.getString(1);
			pw=args.getString(2);
			ecpt=args.getString(3);
			if(flg.equals("enb"))  //开启热点		
			{	
				if(isWifiApEnabled())
				{
					callbackContext.success("WifiAP already open position"+"\n");
					return true;
				}else	
				{ wifiManager.setWifiEnabled(false);	// 关闭默认的wifi站点功能
					try
					{	WifiConfiguration apConfig = new WifiConfiguration();	// 热点配置
						apConfig.SSID = user;								    // 配置热点的用户名
						apConfig.preSharedKey=pw;							    // 配置热点的密码
						apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);//安全加密
						method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);// 通过反射调用设置热点
						callbackContext.success("HotWifi setting success!"+"\n"); 
						return (Boolean)method.invoke(wifiManager, apConfig,true);// 返回打开状态
					}
					catch (Exception e)
					{	callbackContext.error("WifiAP setting exception!"+"\n"); 
						return false;
					}
				}
			}
			else 
			{ 
			//	String aa =wifiManager.getConnectionInfo().getBSSID();
				List<WifiConfiguration> aa =wifiManager.getConfiguredNetworks();
				closeWifiAp();				
				wifiManager.setWifiEnabled(true);										
			//	callbackContext.success("WifiAP setting success!"+"\n");   
				callbackContext.success(aa);

				return true;							
			}				          
		}
		return false;
	}  
    // 热点开关是否打开  
	public boolean isWifiApEnabled() 
	{    
		try 
		{    
			method2 = wifiManager.getClass().getMethod("isWifiApEnabled");    
			method2.setAccessible(true);    
			return (Boolean) method2.invoke(wifiManager);    
		} catch (NoSuchMethodException e) 
		{    
			e.printStackTrace();    
		} catch (Exception e) 
		{    
			e.printStackTrace();    
		}    
		return false;    
	};           
	//关闭WiFi热点   
	public void closeWifiAp() 
	{       
		if (isWifiApEnabled()) 
		{    
			try 
			{    
				method3 = wifiManager.getClass().getMethod("getWifiApConfiguration");    
				method3.setAccessible(true);    
				config = (WifiConfiguration) method3.invoke(wifiManager);    
				method4 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);    
				method4.invoke(wifiManager, config, false);    
			} catch (Exception e) 
			{    
				e.printStackTrace();    
			}   
		}   
	};            
}
