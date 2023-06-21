/*     */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.admin;
/*     */ import com.hypergryph.arknights.core.dao.mailDao;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import com.hypergryph.arknights.core.pojo.Mail;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestHeader;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/mail"})
/*     */ public class mail
/*     */ {
/*     */   @PostMapping(value = {"/getMetaInfoList"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject getMetaInfoList(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  26 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  27 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /mail/getMetaInfoList");
/*     */     
/*  29 */     if (!ArknightsApplication.enableServer) {
/*  30 */       response.setStatus(400);
/*  31 */       JSONObject jSONObject = new JSONObject(true);
/*  32 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  33 */       jSONObject.put("error", "Bad Request");
/*  34 */       jSONObject.put("message", "server is close");
/*  35 */       return jSONObject;
/*     */     } 
/*     */     
/*  38 */     int from = JsonBody.getIntValue("from");
/*     */     
/*  40 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  41 */     if (Accounts.size() != 1) {
/*  42 */       JSONObject jSONObject = new JSONObject(true);
/*  43 */       jSONObject.put("result", Integer.valueOf(2));
/*  44 */       jSONObject.put("error", "无法查询到此账户");
/*  45 */       return jSONObject;
/*     */     } 
/*     */     
/*  48 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/*  50 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  51 */       response.setStatus(500);
/*  52 */       JSONObject jSONObject = new JSONObject(true);
/*  53 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  54 */       jSONObject.put("error", "Bad Request");
/*  55 */       jSONObject.put("message", "error");
/*  56 */       return jSONObject;
/*     */     } 
/*     */     
/*  59 */     JSONArray resultMail = new JSONArray();
/*     */     
/*  61 */     JSONArray listMailBox = JSONArray.parseArray(((Account)Accounts.get(0)).getMails());
/*  62 */     for (int i = 0; i < listMailBox.size(); i++) {
/*  63 */       if (mailDao.queryMailById(listMailBox.getJSONObject(i).getIntValue("mailId")).size() == 1)
/*     */       {
/*  65 */         if (from <= listMailBox.getJSONObject(i).getIntValue("expireAt") && 
/*  66 */           listMailBox.getJSONObject(i).getIntValue("remove") == 0) {
/*  67 */           resultMail.add(listMailBox.getJSONObject(i));
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/*  72 */     JSONObject result = new JSONObject(true);
/*  73 */     result.put("result", resultMail);
/*  74 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/listMailBox"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject listMailBox(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  81 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  82 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /mail/listMailBox");
/*     */     
/*  84 */     if (!ArknightsApplication.enableServer) {
/*  85 */       response.setStatus(400);
/*  86 */       JSONObject jSONObject = new JSONObject(true);
/*  87 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  88 */       jSONObject.put("error", "Bad Request");
/*  89 */       jSONObject.put("message", "server is close");
/*  90 */       return jSONObject;
/*     */     } 
/*     */     
/*  93 */     JSONArray sysMailIdList = JsonBody.getJSONArray("sysMailIdList");
/*  94 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  95 */     if (Accounts.size() != 1) {
/*  96 */       JSONObject jSONObject = new JSONObject(true);
/*  97 */       jSONObject.put("result", Integer.valueOf(2));
/*  98 */       jSONObject.put("error", "无法查询到此账户");
/*  99 */       return jSONObject;
/*     */     } 
/*     */     
/* 102 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 104 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 105 */       response.setStatus(500);
/* 106 */       JSONObject jSONObject = new JSONObject(true);
/* 107 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 108 */       jSONObject.put("error", "Bad Request");
/* 109 */       jSONObject.put("message", "error");
/* 110 */       return jSONObject;
/*     */     } 
/*     */     
/* 113 */     JSONArray mailList = new JSONArray();
/* 114 */     JSONArray listMail = JSONArray.parseArray(((Account)Accounts.get(0)).getMails());
/*     */     
/* 116 */     for (int i = 0; i < sysMailIdList.size(); i++) {
/* 117 */       List<Mail> mails = mailDao.queryMailById(sysMailIdList.getIntValue(i));
/* 118 */       if (mails.size() == 1) {
/* 119 */         JSONObject MailBox = (JSONObject)JSONObject.toJSON(mails.get(0));
/* 120 */         MailBox.put("items", JSONArray.parseArray(MailBox.getString("items")));
/* 121 */         for (int n = 0; n < listMail.size(); n++) {
/* 122 */           if (listMail.getJSONObject(n).getIntValue("mailId") == sysMailIdList.getIntValue(i)) {
/* 123 */             JSONObject Mail = listMail.getJSONObject(n);
/*     */             
/* 125 */             MailBox.put("mailId", Integer.valueOf(Mail.getIntValue("mailId")));
/* 126 */             MailBox.put("createAt", Integer.valueOf(Mail.getIntValue("createAt")));
/* 127 */             MailBox.put("expireAt", Integer.valueOf(Mail.getIntValue("expireAt")));
/* 128 */             MailBox.put("state", Integer.valueOf(Mail.getIntValue("state")));
/* 129 */             MailBox.put("type", Integer.valueOf(Mail.getIntValue("type")));
/* 130 */             MailBox.put("hasItem", Integer.valueOf(Mail.getIntValue("hasItem")));
/*     */           } 
/*     */         } 
/* 133 */         mailList.add(MailBox);
/*     */       } 
/*     */     } 
/* 136 */     JSONObject result = new JSONObject(true);
/* 137 */     result.put("mailList", mailList);
/* 138 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/removeAllReceivedMail"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject removeAllReceivedMail(@RequestHeader("secret") String secret, HttpServletResponse response, HttpServletRequest request) {
/* 145 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 146 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /mail/removeAllReceivedMail");
/*     */     
/* 148 */     if (!ArknightsApplication.enableServer) {
/* 149 */       response.setStatus(400);
/* 150 */       JSONObject jSONObject = new JSONObject(true);
/* 151 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 152 */       jSONObject.put("error", "Bad Request");
/* 153 */       jSONObject.put("message", "server is close");
/* 154 */       return jSONObject;
/*     */     } 
/*     */     
/* 157 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 158 */     if (Accounts.size() != 1) {
/* 159 */       JSONObject jSONObject = new JSONObject(true);
/* 160 */       jSONObject.put("result", Integer.valueOf(2));
/* 161 */       jSONObject.put("error", "无法查询到此账户");
/* 162 */       return jSONObject;
/*     */     } 
/*     */     
/* 165 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 167 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 168 */       response.setStatus(500);
/* 169 */       JSONObject jSONObject = new JSONObject(true);
/* 170 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 171 */       jSONObject.put("error", "Bad Request");
/* 172 */       jSONObject.put("message", "error");
/* 173 */       return jSONObject;
/*     */     } 
/*     */     
/* 176 */     JSONArray listMail = JSONArray.parseArray(((Account)Accounts.get(0)).getMails());
/*     */     
/* 178 */     for (int i = 0; i < listMail.size(); i++) {
/* 179 */       int state = listMail.getJSONObject(i).getIntValue("state");
/* 180 */       if (state == 1) {
/* 181 */         listMail.getJSONObject(i).put("remove", Integer.valueOf(1));
/*     */       }
/*     */     } 
/*     */     
/* 185 */     userDao.setMailsData(uid, listMail);
/*     */     
/* 187 */     JSONObject result = new JSONObject(true);
/* 188 */     JSONObject playerDataDelta = new JSONObject(true);
/* 189 */     playerDataDelta.put("modified", new JSONObject(true));
/* 190 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 191 */     result.put("playerDataDelta", playerDataDelta);
/* 192 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/receiveMail"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject receiveMail(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 198 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 199 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /mail/receiveMail");
/*     */     
/* 201 */     if (!ArknightsApplication.enableServer) {
/* 202 */       response.setStatus(400);
/* 203 */       JSONObject jSONObject = new JSONObject(true);
/* 204 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 205 */       jSONObject.put("error", "Bad Request");
/* 206 */       jSONObject.put("message", "server is close");
/* 207 */       return jSONObject;
/*     */     } 
/*     */     
/* 210 */     int mailId = JsonBody.getIntValue("mailId");
/*     */     
/* 212 */     List<Mail> mailList = mailDao.queryMailById(mailId);
/* 213 */     if (mailList.size() != 1) {
/* 214 */       response.setStatus(500);
/* 215 */       JSONObject jSONObject = new JSONObject(true);
/* 216 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 217 */       jSONObject.put("error", "Bad Request");
/* 218 */       jSONObject.put("message", "error");
/* 219 */       return jSONObject;
/*     */     } 
/*     */     
/* 222 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 223 */     if (Accounts.size() != 1) {
/* 224 */       JSONObject jSONObject = new JSONObject(true);
/* 225 */       jSONObject.put("result", Integer.valueOf(2));
/* 226 */       jSONObject.put("error", "无法查询到此账户");
/* 227 */       return jSONObject;
/*     */     } 
/*     */     
/* 230 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 232 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 233 */       response.setStatus(500);
/* 234 */       JSONObject jSONObject = new JSONObject(true);
/* 235 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 236 */       jSONObject.put("error", "Bad Request");
/* 237 */       jSONObject.put("message", "error");
/* 238 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 242 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 244 */     JSONArray listMail = JSONArray.parseArray(((Account)Accounts.get(0)).getMails());
/* 245 */     JSONArray items = new JSONArray();
/* 246 */     for (int n = 0; n < listMail.size(); n++) {
/* 247 */       if (listMail.getJSONObject(n).getIntValue("mailId") == mailId) {
/* 248 */         JSONArray mailItems = JSONArray.parseArray(((Mail)mailList.get(0)).getItems());
/* 249 */         for (int j = 0; j < mailItems.size(); j++) {
/*     */           
/* 251 */           String reward_id = mailItems.getJSONObject(j).getString("id");
/* 252 */           String reward_type = mailItems.getJSONObject(j).getString("type");
/* 253 */           int reward_count = mailItems.getJSONObject(j).getIntValue("count");
/*     */           
/* 255 */           admin.GM_GiveItem(UserSyncData, reward_id, reward_type, reward_count, items);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     int i;
/* 260 */     for (i = 0; i < listMail.size(); i++) {
/* 261 */       if (listMail.getJSONObject(i).getIntValue("mailId") == mailId) {
/* 262 */         listMail.getJSONObject(i).put("state", Integer.valueOf(1));
/* 263 */         UserSyncData.getJSONObject("pushFlags").put("hasGifts", Integer.valueOf(0));
/*     */         break;
/*     */       } 
/*     */     } 
/* 267 */     for (i = 0; i < listMail.size(); i++) {
/* 268 */       if (listMail.getJSONObject(i).getIntValue("state") == 0) {
/* 269 */         UserSyncData.getJSONObject("pushFlags").put("hasGifts", Integer.valueOf(1));
/*     */         break;
/*     */       } 
/*     */     } 
/* 273 */     userDao.setMailsData(uid, listMail);
/* 274 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 276 */     JSONObject result = new JSONObject(true);
/*     */     
/* 278 */     JSONObject playerDataDelta = new JSONObject(true);
/* 279 */     JSONObject modified = new JSONObject(true);
/*     */     
/* 281 */     modified.put("status", UserSyncData.getJSONObject("status"));
/* 282 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 283 */     modified.put("troop", UserSyncData.getJSONObject("troop"));
/* 284 */     modified.put("skin", UserSyncData.getJSONObject("skin"));
/* 285 */     modified.put("pushFlags", UserSyncData.getJSONObject("pushFlags"));
/* 286 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 287 */     playerDataDelta.put("modified", modified);
/* 288 */     result.put("playerDataDelta", playerDataDelta);
/* 289 */     result.put("items", items);
/* 290 */     result.put("result", Integer.valueOf(0));
/* 291 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/receiveAllMail"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject receiveAllMail(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 299 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 300 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /mail/receiveAllMail");
/*     */     
/* 302 */     if (!ArknightsApplication.enableServer) {
/* 303 */       response.setStatus(400);
/* 304 */       JSONObject jSONObject = new JSONObject(true);
/* 305 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 306 */       jSONObject.put("error", "Bad Request");
/* 307 */       jSONObject.put("message", "server is close");
/* 308 */       return jSONObject;
/*     */     } 
/*     */     
/* 311 */     JSONArray sysMailIdList = JsonBody.getJSONArray("sysMailIdList");
/*     */ 
/*     */ 
/*     */     
/* 315 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 316 */     if (Accounts.size() != 1) {
/* 317 */       JSONObject jSONObject = new JSONObject(true);
/* 318 */       jSONObject.put("result", Integer.valueOf(2));
/* 319 */       jSONObject.put("error", "无法查询到此账户");
/* 320 */       return jSONObject;
/*     */     } 
/*     */     
/* 323 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 325 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 326 */       response.setStatus(500);
/* 327 */       JSONObject jSONObject = new JSONObject(true);
/* 328 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 329 */       jSONObject.put("error", "Bad Request");
/* 330 */       jSONObject.put("message", "error");
/* 331 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 335 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/* 336 */     JSONArray listMail = JSONArray.parseArray(((Account)Accounts.get(0)).getMails());
/*     */     
/* 338 */     JSONArray items = new JSONArray();
/* 339 */     for (int n = 0; n < sysMailIdList.size(); n++) {
/*     */       JSONArray mailItems;
/* 341 */       int mailId = sysMailIdList.getIntValue(n);
/* 342 */       List<Mail> mailList = mailDao.queryMailById(mailId);
/*     */ 
/*     */       
/* 345 */       if (mailList.size() != 1) { mailItems = new JSONArray(); }
/* 346 */       else { mailItems = JSONArray.parseArray(((Mail)mailList.get(0)).getItems()); }
/*     */       
/* 348 */       for (int j = 0; j < mailItems.size(); j++) {
/*     */         
/* 350 */         String reward_id = mailItems.getJSONObject(j).getString("id");
/* 351 */         String reward_type = mailItems.getJSONObject(j).getString("type");
/* 352 */         int reward_count = mailItems.getJSONObject(j).getIntValue("count");
/*     */         
/* 354 */         admin.GM_GiveItem(UserSyncData, reward_id, reward_type, reward_count, items);
/*     */       } 
/*     */     } 
/*     */     int i;
/* 358 */     for (i = 0; i < sysMailIdList.size(); i++) {
/* 359 */       int mailId = sysMailIdList.getIntValue(i);
/* 360 */       for (int j = 0; j < listMail.size(); j++) {
/* 361 */         if (listMail.getJSONObject(j).getIntValue("mailId") == mailId) {
/* 362 */           listMail.getJSONObject(j).put("state", Integer.valueOf(1));
/* 363 */           UserSyncData.getJSONObject("pushFlags").put("hasGifts", Integer.valueOf(0));
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 369 */     for (i = 0; i < listMail.size(); i++) {
/* 370 */       if (listMail.getJSONObject(i).getIntValue("state") == 0) {
/* 371 */         UserSyncData.getJSONObject("pushFlags").put("hasGifts", Integer.valueOf(1));
/*     */         break;
/*     */       } 
/*     */     } 
/* 375 */     userDao.setMailsData(uid, listMail);
/* 376 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 378 */     JSONObject result = new JSONObject(true);
/*     */     
/* 380 */     JSONObject playerDataDelta = new JSONObject(true);
/* 381 */     JSONObject modified = new JSONObject(true);
/*     */     
/* 383 */     modified.put("status", UserSyncData.getJSONObject("status"));
/* 384 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 385 */     modified.put("troop", UserSyncData.getJSONObject("troop"));
/* 386 */     modified.put("skin", UserSyncData.getJSONObject("skin"));
/* 387 */     modified.put("pushFlags", UserSyncData.getJSONObject("pushFlags"));
/* 388 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 389 */     playerDataDelta.put("modified", modified);
/* 390 */     result.put("playerDataDelta", playerDataDelta);
/* 391 */     result.put("items", items);
/* 392 */     result.put("result", Integer.valueOf(0));
/* 393 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\mail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */