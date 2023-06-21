/*     */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.admin;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestHeader;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/shop"})
/*     */ public class shop
/*     */ {
/*     */   @PostMapping(value = {"/getSkinGoodList"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject getSkinGoodList(@RequestBody JSONObject JsonBody) {
/*  27 */     JSONArray charIdList = JsonBody.getJSONArray("charIdList");
/*     */     
/*  29 */     JSONArray goodList = new JSONArray();
/*  30 */     if (charIdList.size() == 0) {
/*  31 */       return ArknightsApplication.skinGoodList;
/*     */     }
/*  33 */     for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)ArknightsApplication.skinTable.entrySet()) {
/*  34 */       String skinId = entry.getKey();
/*  35 */       if (skinId.indexOf(charIdList.getString(0)) != -1 && 
/*  36 */         skinId.indexOf("@") != -1) {
/*     */         
/*  38 */         JSONObject SkinData = ArknightsApplication.skinTable.getJSONObject(skinId);
/*  39 */         JSONObject SkinGood = new JSONObject(true);
/*  40 */         SkinGood.put("charId", SkinData.getString("charId"));
/*  41 */         SkinGood.put("skinId", SkinData.getString("skinId"));
/*  42 */         SkinGood.put("goodId", "SS_" + SkinData.getString("skinId"));
/*  43 */         SkinGood.put("slotId", Integer.valueOf(SkinData.getJSONObject("displaySkin").getIntValue("sortId")));
/*  44 */         SkinGood.put("skinName", SkinData.getJSONObject("displaySkin").getString("skinName"));
/*  45 */         SkinGood.put("discount", Integer.valueOf(0));
/*  46 */         SkinGood.put("originPrice", Integer.valueOf(18));
/*  47 */         SkinGood.put("price", Integer.valueOf(18));
/*  48 */         SkinGood.put("startDateTime", Integer.valueOf(-1));
/*  49 */         SkinGood.put("endDateTime", Integer.valueOf(-1));
/*  50 */         SkinGood.put("desc1", null);
/*  51 */         SkinGood.put("desc2", null);
/*  52 */         SkinGood.put("currencyUnit", "DIAMOND");
/*     */         
/*  54 */         goodList.add(SkinGood);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  60 */     JSONObject result = new JSONObject(true);
/*  61 */     JSONObject playerDataDelta = new JSONObject(true);
/*  62 */     playerDataDelta.put("modified", new JSONObject(true));
/*  63 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  64 */     result.put("playerDataDelta", playerDataDelta);
/*  65 */     result.put("goodList", goodList);
/*  66 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/buySkinGood"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject buySkinGood(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  73 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  74 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /shop/buySkinGood");
/*     */     
/*  76 */     if (!ArknightsApplication.enableServer) {
/*  77 */       response.setStatus(400);
/*  78 */       JSONObject jSONObject = new JSONObject(true);
/*  79 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  80 */       jSONObject.put("error", "Bad Request");
/*  81 */       jSONObject.put("message", "server is close");
/*  82 */       return jSONObject;
/*     */     } 
/*     */     
/*  85 */     String goodId = JsonBody.getString("goodId");
/*     */ 
/*     */ 
/*     */     
/*  89 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  90 */     if (Accounts.size() != 1) {
/*  91 */       JSONObject jSONObject = new JSONObject(true);
/*  92 */       jSONObject.put("result", Integer.valueOf(2));
/*  93 */       jSONObject.put("error", "无法查询到此账户");
/*  94 */       return jSONObject;
/*     */     } 
/*     */     
/*  97 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/*  99 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 100 */       response.setStatus(500);
/* 101 */       JSONObject jSONObject = new JSONObject(true);
/* 102 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 103 */       jSONObject.put("error", "Bad Request");
/* 104 */       jSONObject.put("message", "error");
/* 105 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 109 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 111 */     UserSyncData.getJSONObject("skin").getJSONObject("characterSkins").put(goodId.substring(3), Integer.valueOf(1));
/* 112 */     UserSyncData.getJSONObject("skin").getJSONObject("skinTs").put(goodId.substring(3), Long.valueOf((new Date()).getTime() / 1000L));
/* 113 */     UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") - 18));
/* 114 */     UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") - 18));
/*     */     
/* 116 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 118 */     JSONObject result = new JSONObject(true);
/* 119 */     JSONObject playerDataDelta = new JSONObject(true);
/* 120 */     JSONObject modified = new JSONObject(true);
/* 121 */     JSONObject status = new JSONObject(true);
/* 122 */     status.put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond")));
/* 123 */     status.put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond")));
/* 124 */     modified.put("skin", UserSyncData.getJSONObject("skin"));
/* 125 */     modified.put("status", status);
/* 126 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 127 */     playerDataDelta.put("modified", modified);
/* 128 */     result.put("playerDataDelta", playerDataDelta);
/* 129 */     result.put("result", Integer.valueOf(0));
/* 130 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/buyLowGood"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject buyLowGood(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 138 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 139 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /shop/buyLowGood");
/*     */     
/* 141 */     if (!ArknightsApplication.enableServer) {
/* 142 */       response.setStatus(400);
/* 143 */       JSONObject jSONObject = new JSONObject(true);
/* 144 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 145 */       jSONObject.put("error", "Bad Request");
/* 146 */       jSONObject.put("message", "server is close");
/* 147 */       return jSONObject;
/*     */     } 
/*     */     
/* 150 */     String goodId = JsonBody.getString("goodId");
/* 151 */     int count = JsonBody.getIntValue("count");
/*     */ 
/*     */ 
/*     */     
/* 155 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 156 */     if (Accounts.size() != 1) {
/* 157 */       JSONObject jSONObject = new JSONObject(true);
/* 158 */       jSONObject.put("result", Integer.valueOf(2));
/* 159 */       jSONObject.put("error", "无法查询到此账户");
/* 160 */       return jSONObject;
/*     */     } 
/*     */     
/* 163 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 165 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 166 */       response.setStatus(500);
/* 167 */       JSONObject jSONObject = new JSONObject(true);
/* 168 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 169 */       jSONObject.put("error", "Bad Request");
/* 170 */       jSONObject.put("message", "error");
/* 171 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 175 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 177 */     JSONArray items = new JSONArray();
/*     */     
/* 179 */     for (int i = 0; i < ArknightsApplication.LowGoodList.getJSONArray("goodList").size(); i++) {
/* 180 */       JSONObject lowGood = ArknightsApplication.LowGoodList.getJSONArray("goodList").getJSONObject(i);
/* 181 */       if (lowGood.getString("goodId").equals(goodId)) {
/* 182 */         UserSyncData.getJSONObject("status").put("lggShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("lggShard") - lowGood.getIntValue("price") * count));
/*     */         
/* 184 */         String reward_id = lowGood.getJSONObject("item").getString("id");
/* 185 */         String reward_type = lowGood.getJSONObject("item").getString("type");
/* 186 */         int reward_count = lowGood.getJSONObject("item").getIntValue("count") * count;
/* 187 */         admin.GM_GiveItem(UserSyncData, reward_id, reward_type, reward_count, items);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 192 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 194 */     JSONObject result = new JSONObject(true);
/* 195 */     JSONObject playerDataDelta = new JSONObject(true);
/* 196 */     JSONObject modified = new JSONObject(true);
/* 197 */     modified.put("skin", UserSyncData.getJSONObject("skin"));
/* 198 */     modified.put("status", UserSyncData.getJSONObject("status"));
/* 199 */     modified.put("shop", UserSyncData.getJSONObject("shop"));
/* 200 */     modified.put("troop", UserSyncData.getJSONObject("troop"));
/* 201 */     modified.put("skin", UserSyncData.getJSONObject("skin"));
/* 202 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 203 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 204 */     playerDataDelta.put("modified", modified);
/* 205 */     result.put("playerDataDelta", playerDataDelta);
/* 206 */     result.put("items", items);
/* 207 */     result.put("result", Integer.valueOf(0));
/* 208 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/buyHighGood"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject buyHighGood(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 215 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 216 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /shop/buyHighGood");
/*     */     
/* 218 */     if (!ArknightsApplication.enableServer) {
/* 219 */       response.setStatus(400);
/* 220 */       JSONObject jSONObject = new JSONObject(true);
/* 221 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 222 */       jSONObject.put("error", "Bad Request");
/* 223 */       jSONObject.put("message", "server is close");
/* 224 */       return jSONObject;
/*     */     } 
/*     */     
/* 227 */     String goodId = JsonBody.getString("goodId");
/* 228 */     int count = JsonBody.getIntValue("count");
/*     */ 
/*     */ 
/*     */     
/* 232 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 233 */     if (Accounts.size() != 1) {
/* 234 */       JSONObject jSONObject = new JSONObject(true);
/* 235 */       jSONObject.put("result", Integer.valueOf(2));
/* 236 */       jSONObject.put("error", "无法查询到此账户");
/* 237 */       return jSONObject;
/*     */     } 
/*     */     
/* 240 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 242 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 243 */       response.setStatus(500);
/* 244 */       JSONObject jSONObject = new JSONObject(true);
/* 245 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 246 */       jSONObject.put("error", "Bad Request");
/* 247 */       jSONObject.put("message", "error");
/* 248 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 252 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 254 */     JSONArray items = new JSONArray();
/*     */     
/* 256 */     for (int i = 0; i < ArknightsApplication.HighGoodList.getJSONArray("goodList").size(); i++) {
/* 257 */       JSONObject HighGood = ArknightsApplication.HighGoodList.getJSONArray("goodList").getJSONObject(i);
/* 258 */       if (HighGood.getString("goodId").equals(goodId)) {
/* 259 */         UserSyncData.getJSONObject("status").put("hggShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("hggShard") - HighGood.getIntValue("price") * count));
/*     */         
/* 261 */         String reward_id = HighGood.getJSONObject("item").getString("id");
/* 262 */         String reward_type = HighGood.getJSONObject("item").getString("type");
/* 263 */         int reward_count = HighGood.getJSONObject("item").getIntValue("count") * count;
/* 264 */         admin.GM_GiveItem(UserSyncData, reward_id, reward_type, reward_count, items);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 269 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 271 */     JSONObject result = new JSONObject(true);
/* 272 */     JSONObject playerDataDelta = new JSONObject(true);
/* 273 */     JSONObject modified = new JSONObject(true);
/* 274 */     modified.put("skin", UserSyncData.getJSONObject("skin"));
/* 275 */     modified.put("status", UserSyncData.getJSONObject("status"));
/* 276 */     modified.put("shop", UserSyncData.getJSONObject("shop"));
/* 277 */     modified.put("troop", UserSyncData.getJSONObject("troop"));
/* 278 */     modified.put("skin", UserSyncData.getJSONObject("skin"));
/* 279 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 280 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 281 */     playerDataDelta.put("modified", modified);
/* 282 */     result.put("playerDataDelta", playerDataDelta);
/* 283 */     result.put("items", items);
/* 284 */     result.put("result", Integer.valueOf(0));
/* 285 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/buyExtraGood"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject buyExtraGood(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 292 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 293 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /shop/buyExtraGood");
/*     */     
/* 295 */     if (!ArknightsApplication.enableServer) {
/* 296 */       response.setStatus(400);
/* 297 */       JSONObject jSONObject = new JSONObject(true);
/* 298 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 299 */       jSONObject.put("error", "Bad Request");
/* 300 */       jSONObject.put("message", "server is close");
/* 301 */       return jSONObject;
/*     */     } 
/*     */     
/* 304 */     String goodId = JsonBody.getString("goodId");
/* 305 */     int count = JsonBody.getIntValue("count");
/*     */ 
/*     */ 
/*     */     
/* 309 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 310 */     if (Accounts.size() != 1) {
/* 311 */       JSONObject jSONObject = new JSONObject(true);
/* 312 */       jSONObject.put("result", Integer.valueOf(2));
/* 313 */       jSONObject.put("error", "无法查询到此账户");
/* 314 */       return jSONObject;
/*     */     } 
/*     */     
/* 317 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 319 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 320 */       response.setStatus(500);
/* 321 */       JSONObject jSONObject = new JSONObject(true);
/* 322 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 323 */       jSONObject.put("error", "Bad Request");
/* 324 */       jSONObject.put("message", "error");
/* 325 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 329 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 331 */     JSONArray items = new JSONArray();
/*     */     
/* 333 */     for (int i = 0; i < ArknightsApplication.ExtraGoodList.getJSONArray("goodList").size(); i++) {
/* 334 */       JSONObject ExtraGood = ArknightsApplication.ExtraGoodList.getJSONArray("goodList").getJSONObject(i);
/* 335 */       if (ExtraGood.getString("goodId").equals(goodId)) {
/* 336 */         UserSyncData.getJSONObject("inventory").put("4006", Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("4006") - ExtraGood.getIntValue("price") * count));
/*     */         
/* 338 */         String reward_id = ExtraGood.getJSONObject("item").getString("id");
/* 339 */         String reward_type = ExtraGood.getJSONObject("item").getString("type");
/* 340 */         int reward_count = ExtraGood.getJSONObject("item").getIntValue("count") * count;
/* 341 */         admin.GM_GiveItem(UserSyncData, reward_id, reward_type, reward_count, items);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 346 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 348 */     JSONObject result = new JSONObject(true);
/* 349 */     JSONObject playerDataDelta = new JSONObject(true);
/* 350 */     JSONObject modified = new JSONObject(true);
/* 351 */     modified.put("skin", UserSyncData.getJSONObject("skin"));
/* 352 */     modified.put("status", UserSyncData.getJSONObject("status"));
/* 353 */     modified.put("shop", UserSyncData.getJSONObject("shop"));
/* 354 */     modified.put("troop", UserSyncData.getJSONObject("troop"));
/* 355 */     modified.put("skin", UserSyncData.getJSONObject("skin"));
/* 356 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 357 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 358 */     playerDataDelta.put("modified", modified);
/* 359 */     result.put("playerDataDelta", playerDataDelta);
/* 360 */     result.put("items", items);
/* 361 */     result.put("result", Integer.valueOf(0));
/* 362 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/decomposePotentialItem"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject decomposePotentialItem(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 370 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 371 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /shop/decomposePotentialItem");
/*     */     
/* 373 */     if (!ArknightsApplication.enableServer) {
/* 374 */       response.setStatus(400);
/* 375 */       JSONObject jSONObject = new JSONObject(true);
/* 376 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 377 */       jSONObject.put("error", "Bad Request");
/* 378 */       jSONObject.put("message", "server is close");
/* 379 */       return jSONObject;
/*     */     } 
/*     */     
/* 382 */     JSONArray charInstIdList = JsonBody.getJSONArray("charInstIdList");
/*     */ 
/*     */ 
/*     */     
/* 386 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 387 */     if (Accounts.size() != 1) {
/* 388 */       JSONObject jSONObject = new JSONObject(true);
/* 389 */       jSONObject.put("result", Integer.valueOf(2));
/* 390 */       jSONObject.put("error", "无法查询到此账户");
/* 391 */       return jSONObject;
/*     */     } 
/*     */     
/* 394 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 396 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 397 */       response.setStatus(500);
/* 398 */       JSONObject jSONObject = new JSONObject(true);
/* 399 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 400 */       jSONObject.put("error", "Bad Request");
/* 401 */       jSONObject.put("message", "error");
/* 402 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 406 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 408 */     JSONArray itemGet = new JSONArray();
/*     */     
/* 410 */     for (int i = 0; i < charInstIdList.size(); i++) {
/* 411 */       int lggShard = UserSyncData.getJSONObject("status").getIntValue("lggShard");
/* 412 */       int hggShard = UserSyncData.getJSONObject("status").getIntValue("hggShard");
/* 413 */       JSONObject chars = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstIdList.get(i)));
/* 414 */       String CharId = chars.getString("charId");
/* 415 */       int pcount = UserSyncData.getJSONObject("inventory").getIntValue("p_" + CharId);
/* 416 */       UserSyncData.getJSONObject("inventory").put("p_" + CharId, Integer.valueOf(0));
/* 417 */       int rarity = ArknightsApplication.characterJson.getJSONObject(CharId).getIntValue("rarity");
/* 418 */       JSONObject item = new JSONObject(true);
/* 419 */       if (rarity == 0) {
/* 420 */         item.put("type", "LGG_SHD");
/* 421 */         item.put("id", "4005");
/* 422 */         item.put("count", Integer.valueOf(pcount * 1));
/* 423 */         itemGet.add(item);
/* 424 */         UserSyncData.getJSONObject("status").put("lggShard", Integer.valueOf(lggShard + pcount * 1));
/* 425 */       } else if (rarity == 1) {
/* 426 */         item.put("type", "LGG_SHD");
/* 427 */         item.put("id", "4005");
/* 428 */         item.put("count", Integer.valueOf(pcount * 1));
/* 429 */         itemGet.add(item);
/* 430 */         UserSyncData.getJSONObject("status").put("lggShard", Integer.valueOf(lggShard + pcount * 1));
/* 431 */       } else if (rarity == 2) {
/* 432 */         item.put("type", "LGG_SHD");
/* 433 */         item.put("id", "4005");
/* 434 */         item.put("count", Integer.valueOf(pcount * 5));
/* 435 */         itemGet.add(item);
/* 436 */         UserSyncData.getJSONObject("status").put("lggShard", Integer.valueOf(lggShard + pcount * 5));
/* 437 */       } else if (rarity == 3) {
/* 438 */         item.put("type", "HGG_SHD");
/* 439 */         item.put("id", "4004");
/* 440 */         item.put("count", Integer.valueOf(pcount * 1));
/* 441 */         itemGet.add(item);
/* 442 */         UserSyncData.getJSONObject("status").put("hggShard", Integer.valueOf(hggShard + pcount * 1));
/* 443 */       } else if (rarity == 4) {
/* 444 */         item.put("type", "HGG_SHD");
/* 445 */         item.put("id", "4004");
/* 446 */         item.put("count", Integer.valueOf(pcount * 5));
/* 447 */         itemGet.add(item);
/* 448 */         UserSyncData.getJSONObject("status").put("hggShard", Integer.valueOf(hggShard + pcount * 5));
/* 449 */       } else if (rarity == 5) {
/* 450 */         item.put("type", "HGG_SHD");
/* 451 */         item.put("id", "4004");
/* 452 */         item.put("count", Integer.valueOf(pcount * 10));
/* 453 */         itemGet.add(item);
/* 454 */         UserSyncData.getJSONObject("status").put("hggShard", Integer.valueOf(hggShard + pcount * 10));
/*     */       } 
/*     */     } 
/*     */     
/* 458 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 460 */     JSONObject result = new JSONObject(true);
/* 461 */     JSONObject playerDataDelta = new JSONObject(true);
/* 462 */     JSONObject modified = new JSONObject(true);
/* 463 */     JSONObject status = new JSONObject(true);
/* 464 */     status.put("lggShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("lggShard")));
/* 465 */     status.put("hggShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("hggShard")));
/* 466 */     modified.put("status", status);
/* 467 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 468 */     playerDataDelta.put("modified", modified);
/* 469 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 470 */     result.put("items", itemGet);
/* 471 */     result.put("playerDataDelta", playerDataDelta);
/* 472 */     result.put("result", Integer.valueOf(0));
/* 473 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/getGoodPurchaseState"})
/*     */   public JSONObject getGoodPurchaseState() {
/* 480 */     JSONObject result = new JSONObject(true);
/* 481 */     JSONObject playerDataDelta = new JSONObject(true);
/* 482 */     playerDataDelta.put("modified", new JSONObject(true));
/* 483 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 484 */     result.put("playerDataDelta", playerDataDelta);
/* 485 */     result.put("result", new JSONObject(true));
/* 486 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/getCashGoodList"})
/*     */   public JSONObject getCashGoodList() {
/* 492 */     return ArknightsApplication.CashGoodList;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/getGPGoodList"})
/*     */   public JSONObject getGPGoodList() {
/* 498 */     return ArknightsApplication.GPGoodList;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/getLowGoodList"})
/*     */   public JSONObject getLowGoodList() {
/* 504 */     return ArknightsApplication.LowGoodList;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/getHighGoodList"})
/*     */   public JSONObject getHighGoodList() {
/* 510 */     return ArknightsApplication.HighGoodList;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/getExtraGoodList"})
/*     */   public JSONObject getExtraGoodList() {
/* 516 */     return ArknightsApplication.ExtraGoodList;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/getLMTGSGoodList"})
/*     */   public JSONObject getLMTGSGoodList() {
/* 522 */     return ArknightsApplication.LMTGSGoodList;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/getEPGSGoodList"})
/*     */   public JSONObject getEPGSGoodList() {
/* 528 */     return ArknightsApplication.EPGSGoodList;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/getRepGoodList"})
/*     */   public JSONObject getRepGoodList() {
/* 534 */     return ArknightsApplication.RepGoodList;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/getFurniGoodList"})
/*     */   public JSONObject getFurniGoodList() {
/* 540 */     return ArknightsApplication.FurniGoodList;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/getSocialGoodList"})
/*     */   public JSONObject getSocialGoodList() {
/* 546 */     return ArknightsApplication.SocialGoodList;
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\shop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */