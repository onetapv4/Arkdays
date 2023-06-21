/*     */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestHeader;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/account"})
/*     */ public class account {
/*  22 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/login"}, produces = {"application/json; charset=utf-8"})
/*     */   public JSONObject Login(@RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  28 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  29 */     LOGGER.info("[/" + clientIp + "] /account/login");
/*     */     
/*  31 */     String secret = JsonBody.getString("token");
/*  32 */     String assetsVersion = JsonBody.getString("assetsVersion");
/*  33 */     String clientVersion = JsonBody.getString("clientVersion");
/*     */     
/*  35 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  36 */     if (Accounts.size() != 1) {
/*  37 */       JSONObject jSONObject = new JSONObject(true);
/*  38 */       jSONObject.put("result", Integer.valueOf(2));
/*  39 */       jSONObject.put("error", "无法查询到此账户");
/*  40 */       return jSONObject;
/*     */     } 
/*     */     
/*  43 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/*  45 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  46 */       JSONObject jSONObject = new JSONObject(true);
/*  47 */       jSONObject.put("result", Integer.valueOf(1));
/*  48 */       jSONObject.put("error", "您已被此服务器封禁");
/*  49 */       return jSONObject;
/*     */     } 
/*     */     
/*  52 */     if (!clientVersion.equals(ArknightsApplication.serverConfig.getJSONObject("version").getJSONObject("android").getString("clientVersion"))) {
/*  53 */       JSONObject jSONObject = new JSONObject(true);
/*  54 */       jSONObject.put("result", Integer.valueOf(2));
/*  55 */       jSONObject.put("error", "客户端版本需要更新");
/*  56 */       return jSONObject;
/*     */     } 
/*     */     
/*  59 */     if (!assetsVersion.equals(ArknightsApplication.serverConfig.getJSONObject("version").getJSONObject("android").getString("resVersion")));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     if (((Account)Accounts.get(0)).getUser().equals("{}")) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  72 */       ArknightsApplication.DefaultSyncData.getJSONObject("status").put("registerTs", Long.valueOf((new Date()).getTime() / 1000L));
/*  73 */       ArknightsApplication.DefaultSyncData.getJSONObject("status").put("lastApAddTime", Long.valueOf((new Date()).getTime() / 1000L));
/*     */       
/*  75 */       userDao.setUserData(uid, ArknightsApplication.DefaultSyncData);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  80 */     JSONObject result = new JSONObject(true);
/*  81 */     result.put("result", Integer.valueOf(0));
/*  82 */     result.put("uid", uid);
/*  83 */     result.put("secret", secret);
/*  84 */     result.put("serviceLicenseVersion", Integer.valueOf(0));
/*  85 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/syncData"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject SyncData(@RequestHeader("secret") String secret, HttpServletResponse response, HttpServletRequest request) {
/*  92 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  93 */     LOGGER.info("[/" + clientIp + "] /account/syncData");
/*     */     
/*  95 */     if (!ArknightsApplication.enableServer) {
/*  96 */       response.setStatus(400);
/*  97 */       JSONObject jSONObject = new JSONObject(true);
/*  98 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  99 */       jSONObject.put("error", "Bad Request");
/* 100 */       jSONObject.put("message", "server is close");
/* 101 */       return jSONObject;
/*     */     } 
/*     */     
/* 104 */     Long ts = Long.valueOf(ArknightsApplication.getTimestamp());
/*     */     
/* 106 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 107 */     if (Accounts.size() != 1) {
/* 108 */       JSONObject jSONObject = new JSONObject(true);
/* 109 */       jSONObject.put("result", Integer.valueOf(2));
/* 110 */       jSONObject.put("error", "无法查询到此账户");
/* 111 */       return jSONObject;
/*     */     } 
/*     */     
/* 114 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 116 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 117 */       response.setStatus(500);
/* 118 */       JSONObject jSONObject = new JSONObject(true);
/* 119 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 120 */       jSONObject.put("error", "Bad Request");
/* 121 */       jSONObject.put("message", "error");
/* 122 */       return jSONObject;
/*     */     } 
/*     */     
/* 125 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 127 */     UserSyncData.getJSONObject("status").put("lastOnlineTs", Long.valueOf((new Date()).getTime() / 1000L));
/* 128 */     UserSyncData.getJSONObject("status").put("lastRefreshTs", ts);
/*     */     
/* 130 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 132 */     JSONObject result = new JSONObject(true);
/* 133 */     result.put("result", Integer.valueOf(0));
/* 134 */     result.put("user", UserSyncData);
/*     */     
/* 136 */     result.put("ts", ts);
/* 137 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/syncStatus"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject SyncStatus(@RequestHeader("secret") String secret, HttpServletResponse response, HttpServletRequest request) {
/* 147 */     if (!ArknightsApplication.enableServer) {
/* 148 */       response.setStatus(400);
/* 149 */       JSONObject jSONObject = new JSONObject(true);
/* 150 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 151 */       jSONObject.put("error", "Bad Request");
/* 152 */       jSONObject.put("message", "server is close");
/* 153 */       return jSONObject;
/*     */     } 
/*     */     
/* 156 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 157 */     if (Accounts.size() != 1) {
/* 158 */       JSONObject jSONObject = new JSONObject(true);
/* 159 */       jSONObject.put("result", Integer.valueOf(2));
/* 160 */       jSONObject.put("error", "无法查询到此账户");
/* 161 */       return jSONObject;
/*     */     } 
/*     */     
/* 164 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 166 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 167 */       response.setStatus(500);
/* 168 */       JSONObject jSONObject = new JSONObject(true);
/* 169 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 170 */       jSONObject.put("error", "Bad Request");
/* 171 */       jSONObject.put("message", "error");
/* 172 */       return jSONObject;
/*     */     } 
/*     */     
/* 175 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 177 */     UserSyncData.getJSONObject("status").put("lastOnlineTs", Long.valueOf((new Date()).getTime() / 1000L));
/* 178 */     UserSyncData.getJSONObject("status").put("lastRefreshTs", Long.valueOf(ArknightsApplication.getTimestamp()));
/* 179 */     UserSyncData.getJSONObject("pushFlags").put("hasGifts", Integer.valueOf(0));
/* 180 */     UserSyncData.getJSONObject("pushFlags").put("hasFriendRequest", Integer.valueOf(0));
/*     */     
/* 182 */     JSONArray listMailBox = JSONArray.parseArray(((Account)Accounts.get(0)).getMails());
/*     */     
/* 184 */     for (int i = 0; i < listMailBox.size(); i++) {
/* 185 */       if (listMailBox.getJSONObject(i).getIntValue("state") == 0) {
/* 186 */         if ((new Date()).getTime() / 1000L <= listMailBox.getJSONObject(i).getLongValue("expireAt")) {
/* 187 */           UserSyncData.getJSONObject("pushFlags").put("hasGifts", Integer.valueOf(1));
/*     */           break;
/*     */         } 
/* 190 */         listMailBox.getJSONObject(i).put("remove", Integer.valueOf(1));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 195 */     JSONArray FriendRequest = JSONObject.parseObject(((Account)Accounts.get(0)).getFriend()).getJSONArray("request");
/*     */     
/* 197 */     if (FriendRequest.size() != 0) {
/* 198 */       UserSyncData.getJSONObject("pushFlags").put("hasFriendRequest", Integer.valueOf(1));
/*     */     }
/*     */     
/* 201 */     userDao.setMailsData(uid, listMailBox);
/* 202 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 204 */     JSONObject result = new JSONObject(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     JSONObject playerDataDelta = new JSONObject(true);
/* 211 */     JSONObject modified = new JSONObject(true);
/* 212 */     modified.put("status", UserSyncData.getJSONObject("status"));
/* 213 */     modified.put("gacha", UserSyncData.getJSONObject("gacha"));
/* 214 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 215 */     modified.put("pushFlags", UserSyncData.getJSONObject("pushFlags"));
/* 216 */     modified.put("consumable", UserSyncData.getJSONObject("consumable"));
/* 217 */     modified.put("rlv2", UserSyncData.getJSONObject("rlv2"));
/* 218 */     playerDataDelta.put("modified", modified);
/* 219 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 220 */     result.put("playerDataDelta", playerDataDelta);
/* 221 */     JSONObject result_announcement = new JSONObject(true);
/* 222 */     result_announcement.put("4", ArknightsApplication.serverConfig.getJSONObject("announce").getJSONObject("status"));
/* 223 */     result.put("result", result_announcement);
/* 224 */     result.put("ts", Long.valueOf(ArknightsApplication.getTimestamp()));
/* 225 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\account.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */