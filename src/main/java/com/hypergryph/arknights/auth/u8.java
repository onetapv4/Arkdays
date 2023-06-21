/*     */ package BOOT-INF.classes.com.hypergryph.arknights.auth;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/u8"})
/*     */ public class u8
/*     */ {
/*     */   @PostMapping(value = {"/user/v1/getToken"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject GetToken(@RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  26 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  27 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /u8/user/v1/getToken");
/*     */     
/*  29 */     String secret = JsonBody.getJSONObject("extension").getString("access_token");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  35 */     if (!ArknightsApplication.enableServer) {
/*  36 */       JSONObject jSONObject = new JSONObject(true);
/*  37 */       jSONObject.put("result", Integer.valueOf(2));
/*  38 */       jSONObject.put("error", ArknightsApplication.serverConfig.getJSONObject("server").getString("closeMessage"));
/*  39 */       return jSONObject;
/*     */     } 
/*     */     
/*  42 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  43 */     if (Accounts.size() != 1) {
/*  44 */       JSONObject jSONObject = new JSONObject(true);
/*  45 */       jSONObject.put("result", Integer.valueOf(2));
/*  46 */       jSONObject.put("error", "无法查询到此账户");
/*  47 */       return jSONObject;
/*     */     } 
/*     */     
/*  50 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/*  52 */     JSONObject result = new JSONObject(true);
/*  53 */     result.put("result", Integer.valueOf(0));
/*  54 */     result.put("uid", uid);
/*  55 */     result.put("error", "");
/*  56 */     result.put("extension", "{\"isGuest\":false}");
/*  57 */     result.put("channelUid", uid);
/*  58 */     result.put("token", secret);
/*  59 */     result.put("isGuest", Integer.valueOf(0));
/*  60 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/user/verifyAccount"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject VerifyAccount(@RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  71 */     String secret = JsonBody.getJSONObject("extension").getString("access_token");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     if (!ArknightsApplication.enableServer) {
/*  78 */       JSONObject jSONObject = new JSONObject(true);
/*  79 */       jSONObject.put("result", Integer.valueOf(2));
/*  80 */       jSONObject.put("error", ArknightsApplication.serverConfig.getJSONObject("server").getString("closeMessage"));
/*  81 */       return jSONObject;
/*     */     } 
/*     */     
/*  84 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  85 */     if (Accounts.size() != 1) {
/*  86 */       JSONObject jSONObject = new JSONObject(true);
/*  87 */       jSONObject.put("result", Integer.valueOf(2));
/*  88 */       jSONObject.put("error", "无法查询到此账户");
/*  89 */       return jSONObject;
/*     */     } 
/*     */     
/*  92 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/*  94 */     JSONObject result = new JSONObject(true);
/*  95 */     result.put("result", Integer.valueOf(0));
/*  96 */     result.put("uid", uid);
/*  97 */     result.put("error", "");
/*  98 */     result.put("extension", "{\"isGuest\":false}");
/*  99 */     result.put("channelUid", uid);
/* 100 */     result.put("token", secret);
/* 101 */     result.put("isGuest", Integer.valueOf(0));
/* 102 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/pay/getAllProductList"})
/*     */   public JSONObject GetAllProductList(HttpServletResponse response, HttpServletRequest request) {
/* 112 */     if (!ArknightsApplication.enableServer) {
/* 113 */       response.setStatus(400);
/* 114 */       JSONObject result = new JSONObject(true);
/* 115 */       result.put("statusCode", Integer.valueOf(400));
/* 116 */       result.put("error", "Bad Request");
/* 117 */       result.put("message", "server is close");
/* 118 */       return result;
/*     */     } 
/* 120 */     return ArknightsApplication.AllProductList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/pay/confirmOrderState"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject confirmOrderState(HttpServletResponse response, HttpServletRequest request) {
/* 130 */     if (!ArknightsApplication.enableServer) {
/* 131 */       response.setStatus(400);
/* 132 */       JSONObject jSONObject = new JSONObject(true);
/* 133 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 134 */       jSONObject.put("error", "Bad Request");
/* 135 */       jSONObject.put("message", "server is close");
/* 136 */       return jSONObject;
/*     */     } 
/* 138 */     JSONObject result = new JSONObject(true);
/* 139 */     result.put("payState", Integer.valueOf(3));
/* 140 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\aut\\u8.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */