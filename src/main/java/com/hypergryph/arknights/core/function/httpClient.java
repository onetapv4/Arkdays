/*     */ package BOOT-INF.classes.com.hypergryph.arknights.core.function;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.core.decrypt.Utils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class httpClient
/*     */ {
/*     */   public static JSONObject verifySmsCode(String phone, String smsCode) {
/*  48 */     String secKey = "FFFFFFFFFFFFFFFF";
/*  49 */     String encSecKey = "257348aecb5e556c066de214e531faadd1c55d814f9be95fd06d6bff9f4c7a41f831f6394d5a3fd2e3881736d94a02ca919d952872e7d0a50ebfa1769a7a62d512f5f1ca21aec60bc3819a9c3ffca5eca9a0dba6d6f7249b06f5965ecfff3695b54e1c28f3f624750ed39e7de08fc8493242e26dbc4484a01c76f739e135637c";
/*     */     
/*  51 */     String encText = Utils.aesEncrypt(Utils.aesEncrypt("{\"countrycode\":\"86\",\"phone\":\"" + phone + "\",\"rememberLogin\":\"true\",\"captcha\":\"" + smsCode + "\",\"checkToken\":\"\",\"csrf_token\":\"\"}", "0CoJUm6Qyw8W8jud"), secKey);
/*     */     
/*  53 */     PrintWriter out = null;
/*  54 */     BufferedReader in = null;
/*  55 */     String result = "";
/*     */     try {
/*  57 */       URL realUrl = new URL("https://music.163.com/weapi/login/cellphone?csrf_token=");
/*     */       
/*  59 */       URLConnection connection = realUrl.openConnection();
/*     */       
/*  61 */       connection.setRequestProperty("Referer", "https://music.163.com/");
/*     */       
/*  63 */       connection.setConnectTimeout(10000);
/*  64 */       connection.setReadTimeout(10000);
/*  65 */       connection.setDoOutput(true);
/*  66 */       connection.setDoInput(true);
/*     */       
/*  68 */       out = new PrintWriter(connection.getOutputStream());
/*     */       
/*  70 */       out.print("params=" + URLEncoder.encode(encText, "UTF-8") + "&encSecKey=" + encSecKey);
/*     */       
/*  72 */       out.flush();
/*     */       
/*  74 */       in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
/*     */       String line;
/*  76 */       while ((line = in.readLine()) != null) {
/*  77 */         result = result + line;
/*     */       }
/*  79 */     } catch (Exception e) {
/*  80 */       System.out.println("发送 POST 请求出现异常！" + e);
/*  81 */       e.printStackTrace();
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/*  86 */         if (out != null) {
/*  87 */           out.close();
/*     */         }
/*  89 */         if (in != null) {
/*  90 */           in.close();
/*     */         }
/*  92 */       } catch (IOException ex) {
/*  93 */         ex.printStackTrace();
/*     */       } 
/*     */     } 
/*  96 */     return JSONObject.parseObject(result);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static JSONObject sentSmsCode(String phone) {
/* 102 */     String secKey = "FFFFFFFFFFFFFFFF";
/* 103 */     String encSecKey = "257348aecb5e556c066de214e531faadd1c55d814f9be95fd06d6bff9f4c7a41f831f6394d5a3fd2e3881736d94a02ca919d952872e7d0a50ebfa1769a7a62d512f5f1ca21aec60bc3819a9c3ffca5eca9a0dba6d6f7249b06f5965ecfff3695b54e1c28f3f624750ed39e7de08fc8493242e26dbc4484a01c76f739e135637c";
/*     */     
/* 105 */     String encText = Utils.aesEncrypt(Utils.aesEncrypt("{\"cellphone\":\"" + phone + "\",\"ctcode\":\"86\",\"csrf_token\":\"\"}", "0CoJUm6Qyw8W8jud"), secKey);
/*     */     
/* 107 */     PrintWriter out = null;
/* 108 */     BufferedReader in = null;
/* 109 */     String result = "";
/*     */     try {
/* 111 */       URL realUrl = new URL("https://music.163.com/weapi/sms/captcha/sent?csrf_token=");
/*     */       
/* 113 */       URLConnection connection = realUrl.openConnection();
/*     */       
/* 115 */       connection.setRequestProperty("Referer", "https://music.163.com/");
/*     */       
/* 117 */       connection.setConnectTimeout(10000);
/* 118 */       connection.setReadTimeout(10000);
/* 119 */       connection.setDoOutput(true);
/* 120 */       connection.setDoInput(true);
/*     */       
/* 122 */       out = new PrintWriter(connection.getOutputStream());
/*     */       
/* 124 */       out.print("params=" + URLEncoder.encode(encText, "UTF-8") + "&encSecKey=" + encSecKey);
/*     */       
/* 126 */       out.flush();
/*     */       
/* 128 */       in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
/*     */       String line;
/* 130 */       while ((line = in.readLine()) != null) {
/* 131 */         result = result + line;
/*     */       }
/* 133 */     } catch (Exception e) {
/* 134 */       System.out.println("发送 POST 请求出现异常！" + e);
/* 135 */       e.printStackTrace();
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/* 140 */         if (out != null) {
/* 141 */           out.close();
/*     */         }
/* 143 */         if (in != null) {
/* 144 */           in.close();
/*     */         }
/* 146 */       } catch (IOException ex) {
/* 147 */         ex.printStackTrace();
/*     */       } 
/*     */     } 
/* 150 */     return JSONObject.parseObject(result);
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\core\function\httpClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */