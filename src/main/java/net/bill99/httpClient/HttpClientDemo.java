package net.bill99.httpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class HttpClientDemo {
	
	private static JSONObject getResponse(String content) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = JSONObject.fromObject(content);
		} catch (Exception e) {
			jsonObject.put("content", content);
			jsonObject.put("code", "404");
		}
		return jsonObject;
	}

	
	@Test
	public void test1(){
		//创建默认的httpClient实例
		CloseableHttpClient httpClient=HttpClients.createDefault();
		HttpPost httpPost=new HttpPost("http://192.168.14.88:8088/cap-mock/loginServlet");
		List<NameValuePair> formparams=new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("username", "admin"));
		formparams.add(new BasicNameValuePair("password", "ateMock"));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity=new UrlEncodedFormEntity(formparams, "utf-8");
			httpPost.setEntity(uefEntity);
			System.out.println("executing request:"+httpPost.getURI());
			CloseableHttpResponse  response = httpClient.execute(httpPost);
			System.out.println(response);
			HttpEntity httpEntity = response.getEntity();
			String content=EntityUtils.toString(httpEntity, "utf-8");
			System.out.println("content:"+content);
			Document document = Jsoup.parse(content.toString());
			if(document.title().contains("ATE test page")){
				System.out.println("登录成功");
				String url="http://192.168.14.88:8088/cap-mock/mdpCenter.jsp";
				
				if(httpGet(httpClient, url)){
					httpPost(httpClient, getEntity(map()));
					
				}
				
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	public static boolean httpGet(CloseableHttpClient httpClient,String url){
		HttpGet httpGet=new HttpGet(url);
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "utf-8");
			if(content.contains("支付接口--FunctionCode=10")){
				System.out.println("成功进入支付页面");
				return true;
				
			}else{
				System.out.println("请添加请求头信息");
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally{
			httpGet.releaseConnection();
		}
		return false;
		
	}
	
	public static void httpPost(CloseableHttpClient httpClient,List<NameValuePair> formparams){
		HttpPost httpPost=new HttpPost("http://192.168.14.88:8088/cap-mock/orderMdpBankProcess.jsp");
		try {
			UrlEncodedFormEntity uefEntity=new UrlEncodedFormEntity(formparams, "utf-8");
			httpPost.setEntity(uefEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			String content = EntityUtils.toString(httpEntity, "utf-8");
			if(content.contains("成功")){
				System.out.println("交易成功，响应体："+content);
			}else
				System.out.println("交易失败");
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	
	public static List<NameValuePair> getEntity(Map<String, String> map){
		
		if(map!=null){
			List<NameValuePair> formparams=new ArrayList<NameValuePair>();
			
			for(String key:map.keySet()){
				formparams.add(new BasicNameValuePair(key, map.get(key)));
			}
			return formparams;
			
		}
		return null;
		
	}
	
	public static Map<String, String> map(){
		Map<String, String> map=new HashMap<String, String>();
		map.put("functionCode", "10");
		//map.put("hessianUrl", "");
		//map.put("sendcont", "");
		map.put("appId", "101");
		map.put("merchantCode", "10011478572");
		//map.put("outTradeNo", "");
		map.put("channelType", "25");
		map.put("orderType", "250004");
		map.put("outOrderType", "10");
		map.put("orderAmount", "1000");
		map.put("payAmount", "1000");
		//map.put("payMedia", "");
		map.put("equityFlag", "0");
		//map.put("equityCode", "");
		//map.put("equityAmount", "");
		//map.put("equityDesc", "");
		//map.put("preferentialProp", "");
		map.put("stlMerchantCode", "10013527714");
		//map.put("pinNum", "");
		map.put("payMode", "11");
		map.put("bankAcctId", "6225882112365478");
		map.put("bankAcctType", "0002");
		//map.put("bankAcctName", "");
		//map.put("billOrderId", "");
		//map.put("origMerOrderId", "");
		//map.put("merchantId", "");
		//map.put("txnType", "");
		//map.put("initiativeFlag", "");
		//map.put("secureType", "");
		//map.put("rtmsValidateElement", "");
		//map.put("sysVersion", "");
		//map.put("token", "");
		//map.put("validCode", "");
		//map.put("currencyCode", "");
		//map.put("txnTimeStart", "");
		//map.put("txnTimeExpire", "");
		//map.put("authCode", "");
		map.put("memberCode", "10013527714");
		//map.put("memberMark", "");
		//map.put("productTag", "");
		//map.put("productDesc", "");
		//map.put("orderCount", "");
		//map.put("subMerchantId", "");
		//map.put("subMerchantName", "");
		//map.put("memberBankId", "");
		//map.put("txnSendIp", "");
		//map.put("subTerminalId", "");
		//map.put("deviceInfo", "");
		//map.put("outEquityCode", "");
		//map.put("outEquityAmount", "");
		//map.put("memo", "");
		//map.put("bgUrl", "");
		//map.put("notifyMode", "");
		//map.put("pageUrl", "");
		map.put("inputCharset", "1");
		//map.put("gpsInfo", "");
		//map.put("rtmsValidateElement", "");
		//map.put("sysVersion", "");
		//map.put("rtmsJson", "");
		//map.put("stageInfo", "");
		//map.put("ext2", "");
		//map.put("txnMemberCode", "");
		return map;
	}

	private static Map<String, String> sliceResponse(String response) {
        Map<String, String> responseData = new HashMap<String, String>();
        String regEx="errorInfo\":\"(.*?)\".*?billOrderNo\":\"(.*?)\".*?tradeId\":\"(.*?)\".*?outTradeNo\":\"(.*?)\".*?数据库实例为(.*?) 订单表为.*?流水表为 (.*?)<";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            String errorInfo = matcher.group(1);
            if (errorInfo.equals("成功")) {
            	System.out.println("交易成功");
                responseData.put("billOrderNo", matcher.group(2));
                responseData.put("tradeId", matcher.group(3));
                responseData.put("outTradeNo", matcher.group(4));
                responseData.put("数据库实例", matcher.group(5));
                responseData.put("流水表", matcher.group(6));
                
            } else {
                System.out.println("ATE 3.0绑卡交易请求失败,请检查.");
            }
        }
        Reporter.log(responseData.toString());
        return responseData;
    }	
}
