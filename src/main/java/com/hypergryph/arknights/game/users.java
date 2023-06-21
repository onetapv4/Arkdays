/*     */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/user"})
/*     */ public class users
/*     */ {
/*  26 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/bindNickName"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject bindNickName(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  32 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  33 */     LOGGER.info("[/" + clientIp + "] /user/bindNickName");
/*     */     
/*  35 */     if (!ArknightsApplication.enableServer) {
/*  36 */       response.setStatus(400);
/*  37 */       JSONObject jSONObject = new JSONObject(true);
/*  38 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  39 */       jSONObject.put("error", "Bad Request");
/*  40 */       jSONObject.put("message", "server is close");
/*  41 */       return jSONObject;
/*     */     } 
/*     */     
/*  44 */     String nickName = JsonBody.getString("nickName");
/*     */     
/*  46 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  47 */     if (Accounts.size() != 1) {
/*  48 */       JSONObject jSONObject = new JSONObject(true);
/*  49 */       jSONObject.put("result", Integer.valueOf(2));
/*  50 */       jSONObject.put("error", "无法查询到此账户");
/*  51 */       return jSONObject;
/*     */     } 
/*     */     
/*  54 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/*  56 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  57 */       response.setStatus(500);
/*  58 */       JSONObject jSONObject = new JSONObject(true);
/*  59 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  60 */       jSONObject.put("error", "Bad Request");
/*  61 */       jSONObject.put("message", "error");
/*  62 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/*  66 */     if (nickName.length() > 8) {
/*  67 */       JSONObject jSONObject = new JSONObject(true);
/*  68 */       jSONObject.put("result", Integer.valueOf(1));
/*  69 */       return jSONObject;
/*     */     } 
/*     */     
/*  72 */     if (nickName.indexOf("/") != -1) {
/*  73 */       JSONObject jSONObject = new JSONObject(true);
/*  74 */       jSONObject.put("result", Integer.valueOf(2));
/*  75 */       return jSONObject;
/*     */     } 
/*     */     
/*  78 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/*  80 */     String nickNumber = String.format("%04d", new Object[] { Integer.valueOf(userDao.queryNickName(nickName).size() + 1) });
/*     */     
/*  82 */     UserSyncData.getJSONObject("status").put("nickNumber", nickNumber);
/*  83 */     UserSyncData.getJSONObject("status").put("uid", uid);
/*  84 */     UserSyncData.getJSONObject("status").put("nickName", nickName);
/*     */     
/*  86 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/*  88 */     JSONObject result = new JSONObject(true);
/*  89 */     JSONObject playerDataDelta = new JSONObject(true);
/*  90 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  91 */     JSONObject modified = new JSONObject(true);
/*  92 */     JSONObject status = new JSONObject(true);
/*  93 */     status.put("nickName", nickName);
/*  94 */     modified.put("status", status);
/*  95 */     playerDataDelta.put("modified", modified);
/*  96 */     result.put("playerDataDelta", playerDataDelta);
/*     */ 
/*     */     
/*  99 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/rebindNickName"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject reBindNickName(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 106 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 107 */     LOGGER.info("[/" + clientIp + "] /user/rebindNickName");
/*     */     
/* 109 */     if (!ArknightsApplication.enableServer) {
/* 110 */       response.setStatus(400);
/* 111 */       JSONObject jSONObject = new JSONObject(true);
/* 112 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 113 */       jSONObject.put("error", "Bad Request");
/* 114 */       jSONObject.put("message", "server is close");
/* 115 */       return jSONObject;
/*     */     } 
/*     */     
/* 118 */     String nickName = JsonBody.getString("nickName");
/*     */     
/* 120 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 121 */     if (Accounts.size() != 1) {
/* 122 */       JSONObject jSONObject = new JSONObject(true);
/* 123 */       jSONObject.put("result", Integer.valueOf(2));
/* 124 */       jSONObject.put("error", "无法查询到此账户");
/* 125 */       return jSONObject;
/*     */     } 
/*     */     
/* 128 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 130 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 131 */       response.setStatus(500);
/* 132 */       JSONObject jSONObject = new JSONObject(true);
/* 133 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 134 */       jSONObject.put("error", "Bad Request");
/* 135 */       jSONObject.put("message", "error");
/* 136 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 140 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 142 */     UserSyncData.getJSONObject("status").put("nickName", nickName);
/*     */     
/* 144 */     UserSyncData.getJSONObject("inventory").put("renamingCard", Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("renamingCard") - 1));
/*     */     
/* 146 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 148 */     JSONObject result = new JSONObject(true);
/* 149 */     JSONObject playerDataDelta = new JSONObject(true);
/* 150 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 151 */     JSONObject modified = new JSONObject(true);
/* 152 */     JSONObject status = new JSONObject(true);
/* 153 */     JSONObject inventory = new JSONObject(true);
/* 154 */     inventory.put("renamingCard", Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("renamingCard")));
/* 155 */     status.put("nickName", nickName);
/* 156 */     modified.put("status", status);
/* 157 */     modified.put("inventory", inventory);
/* 158 */     playerDataDelta.put("modified", modified);
/* 159 */     result.put("playerDataDelta", playerDataDelta);
/* 160 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/exchangeDiamondShard"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject exchangeDiamondShard(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 167 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 168 */     LOGGER.info("[/" + clientIp + "] /user/exchangeDiamondShard");
/*     */     
/* 170 */     if (!ArknightsApplication.enableServer) {
/* 171 */       response.setStatus(400);
/* 172 */       JSONObject jSONObject = new JSONObject(true);
/* 173 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 174 */       jSONObject.put("error", "Bad Request");
/* 175 */       jSONObject.put("message", "server is close");
/* 176 */       return jSONObject;
/*     */     } 
/*     */     
/* 179 */     int count = JsonBody.getIntValue("count");
/*     */ 
/*     */ 
/*     */     
/* 183 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 184 */     if (Accounts.size() != 1) {
/* 185 */       JSONObject jSONObject = new JSONObject(true);
/* 186 */       jSONObject.put("result", Integer.valueOf(2));
/* 187 */       jSONObject.put("error", "无法查询到此账户");
/* 188 */       return jSONObject;
/*     */     } 
/*     */     
/* 191 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 193 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 194 */       response.setStatus(500);
/* 195 */       JSONObject jSONObject = new JSONObject(true);
/* 196 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 197 */       jSONObject.put("error", "Bad Request");
/* 198 */       jSONObject.put("message", "error");
/* 199 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 203 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 205 */     if (UserSyncData.getJSONObject("status").getIntValue("androidDiamond") < count) {
/* 206 */       JSONObject jSONObject = new JSONObject(true);
/* 207 */       jSONObject.put("result", Integer.valueOf(1));
/* 208 */       jSONObject.put("errMsg", "剩余源石无法兑换合成玉");
/* 209 */       return jSONObject;
/*     */     } 
/*     */     
/* 212 */     UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") - count));
/* 213 */     UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") - count));
/* 214 */     UserSyncData.getJSONObject("status").put("diamondShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("diamondShard") + count * 180));
/*     */     
/* 216 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 218 */     JSONObject result = new JSONObject(true);
/* 219 */     JSONObject playerDataDelta = new JSONObject(true);
/* 220 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 221 */     JSONObject modified = new JSONObject(true);
/* 222 */     JSONObject status = new JSONObject(true);
/* 223 */     status.put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond")));
/* 224 */     status.put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond")));
/* 225 */     status.put("diamondShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("diamondShard")));
/* 226 */     modified.put("status", status);
/* 227 */     playerDataDelta.put("modified", modified);
/* 228 */     result.put("playerDataDelta", playerDataDelta);
/* 229 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/changeResume"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject changeResume(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 236 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 237 */     LOGGER.info("[/" + clientIp + "] /user/changeResume");
/*     */     
/* 239 */     if (!ArknightsApplication.enableServer) {
/* 240 */       response.setStatus(400);
/* 241 */       JSONObject jSONObject = new JSONObject(true);
/* 242 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 243 */       jSONObject.put("error", "Bad Request");
/* 244 */       jSONObject.put("message", "server is close");
/* 245 */       return jSONObject;
/*     */     } 
/*     */     
/* 248 */     String resume = JsonBody.getString("resume");
/*     */ 
/*     */ 
/*     */     
/* 252 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 253 */     if (Accounts.size() != 1) {
/* 254 */       JSONObject jSONObject = new JSONObject(true);
/* 255 */       jSONObject.put("result", Integer.valueOf(2));
/* 256 */       jSONObject.put("error", "无法查询到此账户");
/* 257 */       return jSONObject;
/*     */     } 
/*     */     
/* 260 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 262 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 263 */       response.setStatus(500);
/* 264 */       JSONObject jSONObject = new JSONObject(true);
/* 265 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 266 */       jSONObject.put("error", "Bad Request");
/* 267 */       jSONObject.put("message", "error");
/* 268 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 272 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 274 */     UserSyncData.getJSONObject("status").put("resume", resume);
/*     */     
/* 276 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 278 */     JSONObject result = new JSONObject(true);
/* 279 */     JSONObject playerDataDelta = new JSONObject(true);
/* 280 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 281 */     JSONObject modified = new JSONObject(true);
/* 282 */     JSONObject status = new JSONObject(true);
/* 283 */     status.put("resume", UserSyncData.getJSONObject("status").getString("resume"));
/* 284 */     modified.put("status", status);
/* 285 */     playerDataDelta.put("modified", modified);
/* 286 */     result.put("playerDataDelta", playerDataDelta);
/* 287 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/changeSecretary"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject changeSecretary(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 294 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 295 */     LOGGER.info("[/" + clientIp + "] /user/changeSecretary");
/*     */     
/* 297 */     if (!ArknightsApplication.enableServer) {
/* 298 */       response.setStatus(400);
/* 299 */       JSONObject jSONObject = new JSONObject(true);
/* 300 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 301 */       jSONObject.put("error", "Bad Request");
/* 302 */       jSONObject.put("message", "server is close");
/* 303 */       return jSONObject;
/*     */     } 
/*     */     
/* 306 */     int charInstId = JsonBody.getIntValue("charInstId");
/* 307 */     String skinId = JsonBody.getString("skinId");
/*     */ 
/*     */ 
/*     */     
/* 311 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 312 */     if (Accounts.size() != 1) {
/* 313 */       JSONObject jSONObject = new JSONObject(true);
/* 314 */       jSONObject.put("result", Integer.valueOf(2));
/* 315 */       jSONObject.put("error", "无法查询到此账户");
/* 316 */       return jSONObject;
/*     */     } 
/*     */     
/* 319 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 321 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 322 */       response.setStatus(500);
/* 323 */       JSONObject jSONObject = new JSONObject(true);
/* 324 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 325 */       jSONObject.put("error", "Bad Request");
/* 326 */       jSONObject.put("message", "error");
/* 327 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 331 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 333 */     UserSyncData.getJSONObject("status").put("secretary", UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)).getString("charId"));
/* 334 */     UserSyncData.getJSONObject("status").put("secretarySkinId", skinId);
/*     */     
/* 336 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 338 */     JSONObject result = new JSONObject(true);
/* 339 */     JSONObject playerDataDelta = new JSONObject(true);
/* 340 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 341 */     JSONObject modified = new JSONObject(true);
/* 342 */     JSONObject status = new JSONObject(true);
/* 343 */     status.put("secretary", UserSyncData.getJSONObject("status").getString("secretary"));
/* 344 */     status.put("secretarySkinId", UserSyncData.getJSONObject("status").getString("secretarySkinId"));
/* 345 */     modified.put("status", status);
/* 346 */     playerDataDelta.put("modified", modified);
/* 347 */     result.put("playerDataDelta", playerDataDelta);
/* 348 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/buyAp"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject buyAp(@RequestHeader("secret") String secret, HttpServletResponse response, HttpServletRequest request) {
/* 355 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 356 */     LOGGER.info("[/" + clientIp + "] /user/buyAp");
/*     */     
/* 358 */     if (!ArknightsApplication.enableServer) {
/* 359 */       response.setStatus(400);
/* 360 */       JSONObject jSONObject = new JSONObject(true);
/* 361 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 362 */       jSONObject.put("error", "Bad Request");
/* 363 */       jSONObject.put("message", "server is close");
/* 364 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 369 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 370 */     if (Accounts.size() != 1) {
/* 371 */       JSONObject jSONObject = new JSONObject(true);
/* 372 */       jSONObject.put("result", Integer.valueOf(2));
/* 373 */       jSONObject.put("error", "无法查询到此账户");
/* 374 */       return jSONObject;
/*     */     } 
/*     */     
/* 377 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 379 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 380 */       response.setStatus(500);
/* 381 */       JSONObject jSONObject = new JSONObject(true);
/* 382 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 383 */       jSONObject.put("error", "Bad Request");
/* 384 */       jSONObject.put("message", "error");
/* 385 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 389 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 391 */     int nowTime = (int)((new Date()).getTime() / 1000L);
/*     */     
/* 393 */     int addAp = (nowTime - UserSyncData.getJSONObject("status").getIntValue("lastApAddTime")) / 360;
/*     */     
/* 395 */     if (UserSyncData.getJSONObject("status").getIntValue("ap") < UserSyncData.getJSONObject("status").getIntValue("maxAp")) {
/* 396 */       if (UserSyncData.getJSONObject("status").getIntValue("ap") + addAp >= UserSyncData.getJSONObject("status").getIntValue("maxAp")) {
/* 397 */         UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("maxAp")));
/* 398 */         UserSyncData.getJSONObject("status").put("lastApAddTime", Integer.valueOf(nowTime));
/*     */       }
/* 400 */       else if (addAp != 0) {
/* 401 */         UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") + addAp));
/* 402 */         UserSyncData.getJSONObject("status").put("lastApAddTime", Integer.valueOf(nowTime));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 407 */     UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") - 1));
/* 408 */     UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") - 1));
/* 409 */     UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") + UserSyncData.getJSONObject("status").getIntValue("maxAp")));
/* 410 */     UserSyncData.getJSONObject("status").put("lastApAddTime", Integer.valueOf(nowTime));
/* 411 */     UserSyncData.getJSONObject("status").put("buyApRemainTimes", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("buyApRemainTimes")));
/*     */     
/* 413 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 415 */     JSONObject result = new JSONObject(true);
/* 416 */     JSONObject playerDataDelta = new JSONObject(true);
/* 417 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 418 */     JSONObject modified = new JSONObject(true);
/* 419 */     JSONObject status = new JSONObject(true);
/* 420 */     status.put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond")));
/* 421 */     status.put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond")));
/* 422 */     status.put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap")));
/* 423 */     status.put("lastApAddTime", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("lastApAddTime")));
/* 424 */     status.put("buyApRemainTimes", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("buyApRemainTimes")));
/* 425 */     modified.put("status", status);
/* 426 */     playerDataDelta.put("modified", modified);
/* 427 */     result.put("playerDataDelta", playerDataDelta);
/* 428 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/changeAvatar"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject changeAvatar(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 435 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 436 */     LOGGER.info("[/" + clientIp + "] /user/changeAvatar");
/*     */     
/* 438 */     if (!ArknightsApplication.enableServer) {
/* 439 */       response.setStatus(400);
/* 440 */       JSONObject jSONObject = new JSONObject(true);
/* 441 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 442 */       jSONObject.put("error", "Bad Request");
/* 443 */       jSONObject.put("message", "server is close");
/* 444 */       return jSONObject;
/*     */     } 
/*     */     
/* 447 */     String id = JsonBody.getString("id");
/* 448 */     String type = JsonBody.getString("type");
/*     */ 
/*     */ 
/*     */     
/* 452 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 453 */     if (Accounts.size() != 1) {
/* 454 */       JSONObject jSONObject = new JSONObject(true);
/* 455 */       jSONObject.put("result", Integer.valueOf(2));
/* 456 */       jSONObject.put("error", "无法查询到此账户");
/* 457 */       return jSONObject;
/*     */     } 
/*     */     
/* 460 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 462 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 463 */       response.setStatus(500);
/* 464 */       JSONObject jSONObject = new JSONObject(true);
/* 465 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 466 */       jSONObject.put("error", "Bad Request");
/* 467 */       jSONObject.put("message", "error");
/* 468 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 472 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 474 */     UserSyncData.getJSONObject("status").getJSONObject("avatar").put("id", id);
/* 475 */     UserSyncData.getJSONObject("status").getJSONObject("avatar").put("type", type);
/*     */     
/* 477 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 479 */     JSONObject result = new JSONObject(true);
/* 480 */     JSONObject playerDataDelta = new JSONObject(true);
/* 481 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 482 */     JSONObject modified = new JSONObject(true);
/* 483 */     JSONObject status = new JSONObject(true);
/* 484 */     status.put("avatar", UserSyncData.getJSONObject("status").getJSONObject("avatar"));
/* 485 */     modified.put("status", status);
/* 486 */     playerDataDelta.put("modified", modified);
/* 487 */     result.put("playerDataDelta", playerDataDelta);
/* 488 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\gam\\users.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */