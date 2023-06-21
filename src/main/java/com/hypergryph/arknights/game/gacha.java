/*      */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*      */ 
/*      */ import com.alibaba.fastjson.JSONArray;
/*      */ import com.alibaba.fastjson.JSONObject;
/*      */ import com.hypergryph.arknights.ArknightsApplication;
/*      */ import com.hypergryph.arknights.core.dao.userDao;
/*      */ import com.hypergryph.arknights.core.file.IOTools;
/*      */ import com.hypergryph.arknights.core.pojo.Account;
/*      */ import java.io.File;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.stream.IntStream;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import org.springframework.web.bind.annotation.PostMapping;
/*      */ import org.springframework.web.bind.annotation.RequestBody;
/*      */ import org.springframework.web.bind.annotation.RequestHeader;
/*      */ import org.springframework.web.bind.annotation.RequestMapping;
/*      */ import org.springframework.web.bind.annotation.RestController;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @RestController
/*      */ @RequestMapping({"/gacha"})
/*      */ public class gacha
/*      */ {
/*      */   @PostMapping({"/syncNormalGacha"})
/*      */   public JSONObject SyncNormalGacha(@RequestHeader("secret") String secret, HttpServletResponse response, HttpServletRequest request) {
/*   38 */     if (!ArknightsApplication.enableServer) {
/*   39 */       response.setStatus(400);
/*   40 */       JSONObject jSONObject = new JSONObject(true);
/*   41 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*   42 */       jSONObject.put("error", "Bad Request");
/*   43 */       jSONObject.put("message", "server is close");
/*   44 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*   49 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*   50 */     if (Accounts.size() != 1) {
/*   51 */       JSONObject jSONObject = new JSONObject(true);
/*   52 */       jSONObject.put("result", Integer.valueOf(2));
/*   53 */       jSONObject.put("error", "无法查询到此账户");
/*   54 */       return jSONObject;
/*      */     } 
/*      */     
/*   57 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*   59 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*   60 */       response.setStatus(500);
/*   61 */       JSONObject jSONObject = new JSONObject(true);
/*   62 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*   63 */       jSONObject.put("error", "Bad Request");
/*   64 */       jSONObject.put("message", "error");
/*   65 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*   69 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*   71 */     JSONObject result = new JSONObject(true);
/*   72 */     JSONObject playerDataDelta = new JSONObject(true);
/*   73 */     JSONObject modified = new JSONObject(true);
/*   74 */     modified.put("recruit", UserSyncData.getJSONObject("recruit"));
/*   75 */     playerDataDelta.put("modified", modified);
/*   76 */     playerDataDelta.put("deleted", new JSONObject(true));
/*   77 */     result.put("playerDataDelta", playerDataDelta);
/*   78 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping({"/normalGacha"})
/*      */   public JSONObject normalGacha(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*   85 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*   86 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /gacha/normalGacha");
/*      */     
/*   88 */     if (!ArknightsApplication.enableServer) {
/*   89 */       response.setStatus(400);
/*   90 */       JSONObject jSONObject = new JSONObject(true);
/*   91 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*   92 */       jSONObject.put("error", "Bad Request");
/*   93 */       jSONObject.put("message", "server is close");
/*   94 */       return jSONObject;
/*      */     } 
/*      */     
/*   97 */     String slotId = JsonBody.getString("slotId");
/*   98 */     JSONArray tagList = JsonBody.getJSONArray("tagList");
/*      */ 
/*      */ 
/*      */     
/*  102 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  103 */     if (Accounts.size() != 1) {
/*  104 */       JSONObject jSONObject = new JSONObject(true);
/*  105 */       jSONObject.put("result", Integer.valueOf(2));
/*  106 */       jSONObject.put("error", "无法查询到此账户");
/*  107 */       return jSONObject;
/*      */     } 
/*      */     
/*  110 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  112 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  113 */       response.setStatus(500);
/*  114 */       JSONObject jSONObject = new JSONObject(true);
/*  115 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  116 */       jSONObject.put("error", "Bad Request");
/*  117 */       jSONObject.put("message", "error");
/*  118 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  122 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  124 */     UserSyncData.getJSONObject("recruit").getJSONObject("normal").getJSONObject("slots").getJSONObject(String.valueOf(slotId)).put("state", Integer.valueOf(2));
/*      */     
/*  126 */     JSONArray selectTags = new JSONArray();
/*  127 */     for (int i = 0; i < tagList.size(); i++) {
/*  128 */       JSONObject selectTag = new JSONObject(true);
/*  129 */       selectTag.put("pick", Integer.valueOf(1));
/*  130 */       selectTag.put("tagId", Integer.valueOf(tagList.getIntValue(i)));
/*  131 */       selectTags.add(selectTag);
/*      */     } 
/*      */     
/*  134 */     UserSyncData.getJSONObject("recruit").getJSONObject("normal").getJSONObject("slots").getJSONObject(String.valueOf(slotId)).put("selectTags", selectTags);
/*  135 */     UserSyncData.getJSONObject("status").put("recruitLicense", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("recruitLicense") - 1));
/*      */     
/*  137 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  139 */     JSONObject result = new JSONObject(true);
/*  140 */     JSONObject playerDataDelta = new JSONObject(true);
/*  141 */     JSONObject modified = new JSONObject(true);
/*  142 */     modified.put("recruit", UserSyncData.getJSONObject("recruit"));
/*  143 */     modified.put("status", UserSyncData.getJSONObject("status"));
/*  144 */     playerDataDelta.put("modified", modified);
/*  145 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  146 */     result.put("playerDataDelta", playerDataDelta);
/*  147 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping({"/finishNormalGacha"})
/*      */   public JSONObject finishNormalGacha(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  154 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  155 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /gacha/finishNormalGacha");
/*      */     
/*  157 */     if (!ArknightsApplication.enableServer) {
/*  158 */       response.setStatus(400);
/*  159 */       JSONObject jSONObject = new JSONObject(true);
/*  160 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  161 */       jSONObject.put("error", "Bad Request");
/*  162 */       jSONObject.put("message", "server is close");
/*  163 */       return jSONObject;
/*      */     } 
/*      */     
/*  166 */     String slotId = JsonBody.getString("slotId");
/*      */     
/*  168 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  169 */     if (Accounts.size() != 1) {
/*  170 */       JSONObject jSONObject = new JSONObject(true);
/*  171 */       jSONObject.put("result", Integer.valueOf(2));
/*  172 */       jSONObject.put("error", "无法查询到此账户");
/*  173 */       return jSONObject;
/*      */     } 
/*      */     
/*  176 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  178 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  179 */       response.setStatus(500);
/*  180 */       JSONObject jSONObject = new JSONObject(true);
/*  181 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  182 */       jSONObject.put("error", "Bad Request");
/*  183 */       jSONObject.put("message", "error");
/*  184 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  188 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  190 */     JSONObject chars = UserSyncData.getJSONObject("troop").getJSONObject("chars");
/*  191 */     JSONObject buildingChars = UserSyncData.getJSONObject("building").getJSONObject("chars");
/*  192 */     JSONArray availCharInfo = ArknightsApplication.normalGachaData.getJSONObject("detailInfo").getJSONObject("availCharInfo").getJSONArray("perAvailList");
/*      */ 
/*      */     
/*  195 */     JSONArray randomRankArray = new JSONArray();
/*      */     
/*  197 */     for (int i = 0; i < availCharInfo.size(); i++) {
/*      */       
/*  199 */       int totalPercent = (int)(availCharInfo.getJSONObject(i).getFloat("totalPercent").floatValue() * 100.0F);
/*  200 */       int rarityRank = availCharInfo.getJSONObject(i).getIntValue("rarityRank");
/*      */       
/*  202 */       JSONObject randomRankObject = new JSONObject(true);
/*  203 */       randomRankObject.put("rarityRank", Integer.valueOf(rarityRank));
/*  204 */       randomRankObject.put("index", Integer.valueOf(i));
/*      */ 
/*      */       
/*  207 */       IntStream.range(0, totalPercent).forEach(n -> randomRankArray.add(randomRankObject));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  213 */     Collections.shuffle((List<?>)randomRankArray);
/*      */ 
/*      */     
/*  216 */     JSONObject randomRank = randomRankArray.getJSONObject((new Random()).nextInt(randomRankArray.size()));
/*      */ 
/*      */     
/*  219 */     JSONArray randomCharArray = availCharInfo.getJSONObject(randomRank.getIntValue("index")).getJSONArray("charIdList");
/*      */ 
/*      */     
/*  222 */     Collections.shuffle((List<?>)randomCharArray);
/*      */ 
/*      */     
/*  225 */     String randomCharId = randomCharArray.getString((new Random()).nextInt(randomCharArray.size()));
/*      */     
/*  227 */     int repeatCharId = 0;
/*      */     
/*  229 */     for (int j = 0; j < UserSyncData.getJSONObject("troop").getJSONObject("chars").size(); j++) {
/*  230 */       if (UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(j + 1)).getString("charId").equals(randomCharId)) {
/*  231 */         repeatCharId = j + 1;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  236 */     JSONArray itemGet = new JSONArray();
/*  237 */     int isNew = 0;
/*  238 */     int charinstId = repeatCharId;
/*  239 */     if (repeatCharId == 0) {
/*      */ 
/*      */       
/*  242 */       JSONObject char_data = new JSONObject(true);
/*      */       
/*  244 */       JSONArray skilsArray = ArknightsApplication.characterJson.getJSONObject(randomCharId).getJSONArray("skills");
/*  245 */       JSONArray skils = new JSONArray();
/*      */       
/*  247 */       for (int k = 0; k < skilsArray.size(); k++) {
/*  248 */         JSONObject new_skils = new JSONObject(true);
/*  249 */         new_skils.put("skillId", skilsArray.getJSONObject(k).getString("skillId"));
/*  250 */         new_skils.put("state", Integer.valueOf(0));
/*  251 */         new_skils.put("specializeLevel", Integer.valueOf(0));
/*  252 */         new_skils.put("completeUpgradeTime", Integer.valueOf(-1));
/*  253 */         if (skilsArray.getJSONObject(k).getJSONObject("unlockCond").getIntValue("phase") == 0) {
/*  254 */           new_skils.put("unlock", Integer.valueOf(1));
/*      */         } else {
/*  256 */           new_skils.put("unlock", Integer.valueOf(0));
/*      */         } 
/*  258 */         skils.add(new_skils);
/*      */       } 
/*      */       
/*  261 */       int instId = UserSyncData.getJSONObject("troop").getJSONObject("chars").size() + 1;
/*  262 */       charinstId = instId;
/*  263 */       char_data.put("instId", Integer.valueOf(instId));
/*  264 */       char_data.put("charId", randomCharId);
/*  265 */       char_data.put("favorPoint", Integer.valueOf(0));
/*  266 */       char_data.put("potentialRank", Integer.valueOf(0));
/*  267 */       char_data.put("mainSkillLvl", Integer.valueOf(1));
/*  268 */       char_data.put("skin", randomCharId + "#1");
/*  269 */       char_data.put("level", Integer.valueOf(1));
/*  270 */       char_data.put("exp", Integer.valueOf(0));
/*  271 */       char_data.put("evolvePhase", Integer.valueOf(0));
/*  272 */       char_data.put("gainTime", Long.valueOf((new Date()).getTime() / 1000L));
/*  273 */       char_data.put("skills", skils);
/*  274 */       char_data.put("equip", new JSONObject(true));
/*  275 */       char_data.put("voiceLan", ArknightsApplication.charwordTable.getJSONObject("charDefaultTypeDict").getString(randomCharId));
/*  276 */       if (skils == new JSONArray()) {
/*  277 */         char_data.put("defaultSkillIndex", Integer.valueOf(-1));
/*      */       } else {
/*  279 */         char_data.put("defaultSkillIndex", Integer.valueOf(0));
/*      */       } 
/*      */       
/*  282 */       String sub1 = randomCharId.substring(randomCharId.indexOf("_") + 1);
/*  283 */       String charName = sub1.substring(sub1.indexOf("_") + 1);
/*      */       
/*  285 */       if (ArknightsApplication.uniequipTable.containsKey("uniequip_001_" + charName)) {
/*  286 */         JSONObject equip = new JSONObject(true);
/*  287 */         JSONObject uniequip_001 = new JSONObject(true);
/*  288 */         uniequip_001.put("hide", Integer.valueOf(0));
/*  289 */         uniequip_001.put("locked", Integer.valueOf(0));
/*  290 */         uniequip_001.put("level", Integer.valueOf(1));
/*  291 */         JSONObject uniequip_002 = new JSONObject(true);
/*  292 */         uniequip_002.put("hide", Integer.valueOf(0));
/*  293 */         uniequip_002.put("locked", Integer.valueOf(0));
/*  294 */         uniequip_002.put("level", Integer.valueOf(1));
/*  295 */         equip.put("uniequip_001_" + charName, uniequip_001);
/*  296 */         equip.put("uniequip_002_" + charName, uniequip_002);
/*  297 */         char_data.put("equip", equip);
/*  298 */         char_data.put("currentEquip", "uniequip_001_" + charName);
/*      */       } else {
/*  300 */         char_data.put("currentEquip", null);
/*      */       } 
/*      */       
/*  303 */       UserSyncData.getJSONObject("troop").getJSONObject("chars").put(String.valueOf(instId), char_data);
/*      */       
/*  305 */       JSONObject charGroup = new JSONObject(true);
/*  306 */       charGroup.put("favorPoint", Integer.valueOf(0));
/*  307 */       UserSyncData.getJSONObject("troop").getJSONObject("charGroup").put(randomCharId, charGroup);
/*      */       
/*  309 */       JSONObject buildingChar = new JSONObject(true);
/*  310 */       buildingChar.put("charId", randomCharId);
/*  311 */       buildingChar.put("lastApAddTime", Long.valueOf((new Date()).getTime() / 1000L));
/*  312 */       buildingChar.put("ap", Integer.valueOf(8640000));
/*  313 */       buildingChar.put("roomSlotId", "");
/*  314 */       buildingChar.put("index", Integer.valueOf(-1));
/*  315 */       buildingChar.put("changeScale", Integer.valueOf(0));
/*  316 */       JSONObject bubble = new JSONObject(true);
/*  317 */       JSONObject normal = new JSONObject(true);
/*  318 */       normal.put("add", Integer.valueOf(-1));
/*  319 */       normal.put("ts", Integer.valueOf(0));
/*  320 */       bubble.put("normal", normal);
/*  321 */       JSONObject assist = new JSONObject(true);
/*  322 */       assist.put("add", Integer.valueOf(-1));
/*  323 */       assist.put("ts", Integer.valueOf(-1));
/*  324 */       bubble.put("assist", assist);
/*  325 */       buildingChar.put("bubble", bubble);
/*  326 */       buildingChar.put("workTime", Integer.valueOf(0));
/*      */       
/*  328 */       buildingChars.put(String.valueOf(instId), buildingChar);
/*  329 */       chars.put(String.valueOf(instId), char_data);
/*      */       
/*  331 */       JSONObject SHD = new JSONObject(true);
/*  332 */       SHD.put("type", "HGG_SHD");
/*  333 */       SHD.put("id", "4004");
/*  334 */       SHD.put("count", Integer.valueOf(1));
/*  335 */       itemGet.add(SHD);
/*      */       
/*  337 */       isNew = 1;
/*      */       
/*  339 */       UserSyncData.getJSONObject("status").put("hggShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("hggShard") + 1));
/*      */     }
/*      */     else {
/*      */       
/*  343 */       JSONObject repatChar = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(repeatCharId));
/*  344 */       int potentialRank = repatChar.getIntValue("potentialRank");
/*  345 */       int rarity = ArknightsApplication.characterJson.getJSONObject(randomCharId).getIntValue("rarity");
/*      */       
/*  347 */       String itemName = null;
/*  348 */       String itemType = null;
/*  349 */       String itemId = null;
/*      */       
/*  351 */       int itemCount = 0;
/*  352 */       if (rarity == 0) {
/*  353 */         itemName = "lggShard";
/*  354 */         itemType = "LGG_SHD";
/*  355 */         itemId = "4005";
/*  356 */         itemCount = 1;
/*      */       } 
/*  358 */       if (rarity == 1) {
/*  359 */         itemName = "lggShard";
/*  360 */         itemType = "LGG_SHD";
/*  361 */         itemId = "4005";
/*  362 */         itemCount = 1;
/*      */       } 
/*  364 */       if (rarity == 2) {
/*  365 */         itemName = "lggShard";
/*  366 */         itemType = "LGG_SHD";
/*  367 */         itemId = "4005";
/*  368 */         itemCount = 5;
/*      */       } 
/*  370 */       if (rarity == 3) {
/*  371 */         itemName = "lggShard";
/*  372 */         itemType = "LGG_SHD";
/*  373 */         itemId = "4005";
/*  374 */         itemCount = 30;
/*      */       } 
/*  376 */       if (rarity == 4) {
/*  377 */         itemName = "hggShard";
/*  378 */         itemType = "HGG_SHD";
/*  379 */         itemId = "4004";
/*  380 */         if (potentialRank != 5) {
/*  381 */           itemCount = 5;
/*      */         } else {
/*  383 */           itemCount = 8;
/*      */         } 
/*      */       } 
/*  386 */       if (rarity == 5) {
/*  387 */         itemName = "hggShard";
/*  388 */         itemType = "HGG_SHD";
/*  389 */         itemId = "4004";
/*  390 */         if (potentialRank != 5) {
/*  391 */           itemCount = 10;
/*      */         } else {
/*  393 */           itemCount = 15;
/*      */         } 
/*      */       } 
/*      */       
/*  397 */       JSONObject SHD = new JSONObject(true);
/*  398 */       SHD.put("type", itemType);
/*  399 */       SHD.put("id", itemId);
/*  400 */       SHD.put("count", Integer.valueOf(itemCount));
/*  401 */       itemGet.add(SHD);
/*      */       
/*  403 */       JSONObject potential = new JSONObject(true);
/*  404 */       potential.put("type", "MATERIAL");
/*  405 */       potential.put("id", "p_" + randomCharId);
/*  406 */       potential.put("count", Integer.valueOf(1));
/*  407 */       itemGet.add(potential);
/*      */       
/*  409 */       UserSyncData.getJSONObject("status").put(itemName, Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue(itemName) + itemCount));
/*      */       
/*  411 */       UserSyncData.getJSONObject("inventory").put("p_" + randomCharId, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("p_" + randomCharId) + 1));
/*      */       
/*  413 */       chars.put(String.valueOf(repeatCharId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(repeatCharId)));
/*      */     } 
/*  415 */     UserSyncData.getJSONObject("troop").put("chars", chars);
/*      */ 
/*      */     
/*  418 */     UserSyncData.getJSONObject("recruit").getJSONObject("normal").getJSONObject("slots").getJSONObject(String.valueOf(slotId)).put("state", Integer.valueOf(1));
/*  419 */     UserSyncData.getJSONObject("recruit").getJSONObject("normal").getJSONObject("slots").getJSONObject(String.valueOf(slotId)).put("selectTags", new JSONArray());
/*      */     
/*  421 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  423 */     JSONObject charGet = new JSONObject(true);
/*  424 */     charGet.put("itemGet", itemGet);
/*  425 */     charGet.put("charId", randomCharId);
/*  426 */     charGet.put("charInstId", Integer.valueOf(charinstId));
/*  427 */     charGet.put("isNew", Integer.valueOf(isNew));
/*      */     
/*  429 */     JSONObject result = new JSONObject(true);
/*  430 */     JSONObject playerDataDelta = new JSONObject(true);
/*  431 */     JSONObject modified = new JSONObject(true);
/*  432 */     modified.put("recruit", UserSyncData.getJSONObject("recruit"));
/*  433 */     modified.put("status", UserSyncData.getJSONObject("status"));
/*  434 */     modified.put("troop", UserSyncData.getJSONObject("troop"));
/*  435 */     playerDataDelta.put("modified", modified);
/*  436 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  437 */     result.put("playerDataDelta", playerDataDelta);
/*  438 */     result.put("charGet", charGet);
/*  439 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping({"/getPoolDetail"})
/*      */   public JSONObject GetPoolDetail(@RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  446 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  447 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /gacha/getPoolDetail");
/*      */     
/*  449 */     if (!ArknightsApplication.enableServer) {
/*  450 */       response.setStatus(400);
/*  451 */       JSONObject result = new JSONObject(true);
/*  452 */       result.put("statusCode", Integer.valueOf(400));
/*  453 */       result.put("error", "Bad Request");
/*  454 */       result.put("message", "server is close");
/*  455 */       return result;
/*      */     } 
/*      */ 
/*      */     
/*  459 */     String poolId = JsonBody.getString("poolId");
/*      */ 
/*      */     
/*  462 */     String PoolPath = System.getProperty("user.dir") + "/data/gacha/" + poolId + ".json";
/*      */ 
/*      */     
/*  465 */     if (!(new File(PoolPath)).exists()) {
/*  466 */       JSONObject result = new JSONObject(true);
/*  467 */       JSONObject detailInfo = new JSONObject();
/*  468 */       JSONObject availCharInfo = new JSONObject();
/*  469 */       availCharInfo.put("perAvailList", new JSONArray());
/*  470 */       detailInfo.put("availCharInfo", availCharInfo);
/*  471 */       detailInfo.put("limitedChar", null);
/*  472 */       detailInfo.put("weightUpCharInfo", null);
/*  473 */       JSONArray gachaObjList = new JSONArray();
/*  474 */       JSONObject Text0 = new JSONObject();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  505 */       JSONObject Text7 = new JSONObject();
/*  506 */       Text7.put("gachaObject", "TEXT");
/*  507 */       Text7.put("type", Integer.valueOf(7));
/*  508 */       Text7.put("param", poolId);
/*      */       
/*  510 */       JSONObject Text8 = new JSONObject();
/*  511 */       Text8.put("gachaObject", "TEXT");
/*  512 */       Text8.put("type", Integer.valueOf(5));
/*  513 */       Text8.put("param", "该卡池尚未实装，无法获取详细信息");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  523 */       gachaObjList.add(Text7);
/*  524 */       gachaObjList.add(Text8);
/*  525 */       detailInfo.put("gachaObjList", gachaObjList);
/*  526 */       result.put("detailInfo", detailInfo);
/*  527 */       return result;
/*      */     } 
/*      */     
/*  530 */     return IOTools.ReadJsonFile(PoolPath);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping({"/advancedGacha"})
/*      */   public JSONObject advancedGacha(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  537 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  538 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /gacha/advancedGacha");
/*      */     
/*  540 */     if (!ArknightsApplication.enableServer) {
/*  541 */       response.setStatus(400);
/*  542 */       JSONObject result = new JSONObject(true);
/*  543 */       result.put("statusCode", Integer.valueOf(400));
/*  544 */       result.put("error", "Bad Request");
/*  545 */       result.put("message", "server is close");
/*  546 */       return result;
/*      */     } 
/*      */     
/*  549 */     if (JsonBody.getString("poolId").equals("BOOT_0_1_1")) {
/*  550 */       return Gacha("gachaTicket", 380, secret, JsonBody);
/*      */     }
/*  552 */     return Gacha("gachaTicket", 600, secret, JsonBody);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping({"/tenAdvancedGacha"})
/*      */   public JSONObject tenAdvancedGacha(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  560 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  561 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /gacha/tenAdvancedGacha");
/*      */     
/*  563 */     if (!ArknightsApplication.enableServer) {
/*  564 */       response.setStatus(400);
/*  565 */       JSONObject result = new JSONObject(true);
/*  566 */       result.put("statusCode", Integer.valueOf(400));
/*  567 */       result.put("error", "Bad Request");
/*  568 */       result.put("message", "server is close");
/*  569 */       return result;
/*      */     } 
/*      */     
/*  572 */     if (JsonBody.getString("poolId").equals("BOOT_0_1_1")) {
/*  573 */       return Gacha("tenGachaTicket", 3800, secret, JsonBody);
/*      */     }
/*  575 */     return Gacha("tenGachaTicket", 6000, secret, JsonBody);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject Gacha(String type, int useDiamondShard, String secret, JSONObject JsonBody) {
/*  582 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  583 */     if (Accounts.size() != 1) {
/*  584 */       JSONObject jSONObject = new JSONObject(true);
/*  585 */       jSONObject.put("result", Integer.valueOf(2));
/*  586 */       jSONObject.put("error", "无法查询到此账户");
/*  587 */       return jSONObject;
/*      */     } 
/*      */     
/*  590 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  592 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */ 
/*      */     
/*  595 */     String poolId = JsonBody.getString("poolId");
/*  596 */     String poolPath = System.getProperty("user.dir") + "/data/gacha/" + poolId + ".json";
/*      */     
/*  598 */     int useTkt = JsonBody.getIntValue("useTkt");
/*      */     
/*  600 */     if (!(new File(poolPath)).exists()) {
/*  601 */       JSONObject jSONObject = new JSONObject(true);
/*  602 */       jSONObject.put("result", Integer.valueOf(1));
/*  603 */       jSONObject.put("errMsg", "该当前干员寻访无法使用，详情请关注官方公告");
/*  604 */       return jSONObject;
/*      */     } 
/*      */     
/*  607 */     JSONObject poolJson = IOTools.ReadJsonFile(poolPath);
/*      */     
/*  609 */     JSONArray gachaResultList = new JSONArray();
/*  610 */     JSONArray newChars = new JSONArray();
/*  611 */     JSONObject charGet = new JSONObject(true);
/*  612 */     JSONObject troop = new JSONObject(true);
/*  613 */     JSONObject chars = UserSyncData.getJSONObject("troop").getJSONObject("chars");
/*      */     
/*  615 */     int usedimmond = 0;
/*  616 */     if (JsonBody.getString("poolId").equals("BOOT_0_1_1")) {
/*  617 */       usedimmond = useDiamondShard / 380;
/*      */     } else {
/*  619 */       usedimmond = useDiamondShard / 600;
/*      */     } 
/*      */     
/*  622 */     for (int count = 0; count < usedimmond; count++) {
/*      */ 
/*      */ 
/*      */       
/*  626 */       if (useTkt == 1 || useTkt == 2) {
/*      */         
/*  628 */         if (UserSyncData.getJSONObject("status").getIntValue(type) <= 0) {
/*  629 */           JSONObject jSONObject = new JSONObject(true);
/*  630 */           jSONObject.put("result", Integer.valueOf(2));
/*  631 */           jSONObject.put("errMsg", "剩余寻访凭证不足");
/*  632 */           return jSONObject;
/*      */         }
/*      */       
/*      */       }
/*  636 */       else if (UserSyncData.getJSONObject("status").getIntValue("diamondShard") < useDiamondShard) {
/*  637 */         JSONObject jSONObject = new JSONObject(true);
/*  638 */         jSONObject.put("result", Integer.valueOf(3));
/*  639 */         jSONObject.put("errMsg", "剩余合成玉不足");
/*  640 */         return jSONObject;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  647 */       Boolean Minimum = Boolean.valueOf(false);
/*  648 */       String poolObjecName = null;
/*  649 */       JSONObject Pool = new JSONObject(true);
/*      */       
/*  651 */       if (JsonBody.getString("poolId").equals("BOOT_0_1_1")) {
/*  652 */         poolObjecName = "newbee";
/*      */         
/*  654 */         Pool = UserSyncData.getJSONObject("gacha").getJSONObject(poolObjecName);
/*  655 */         int cnt = Pool.getIntValue("cnt") - 1;
/*      */ 
/*      */         
/*  658 */         UserSyncData.getJSONObject("gacha").getJSONObject(poolObjecName).put("cnt", Integer.valueOf(cnt));
/*  659 */         UserSyncData.getJSONObject("status").put("gachaCount", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gachaCount") + 1));
/*      */         
/*  661 */         if (cnt == 0) {
/*  662 */           UserSyncData.getJSONObject("gacha").getJSONObject(poolObjecName).put("openFlag", Integer.valueOf(0));
/*      */         }
/*      */       } else {
/*  665 */         poolObjecName = "normal";
/*      */ 
/*      */         
/*  668 */         if (!UserSyncData.getJSONObject("gacha").getJSONObject(poolObjecName).containsKey(poolId)) {
/*      */           
/*  670 */           JSONObject PoolJson = new JSONObject(true);
/*  671 */           PoolJson.put("cnt", Integer.valueOf(0));
/*  672 */           PoolJson.put("maxCnt", Integer.valueOf(10));
/*  673 */           PoolJson.put("rarity", Integer.valueOf(4));
/*  674 */           PoolJson.put("avail", Boolean.valueOf(true));
/*  675 */           UserSyncData.getJSONObject("gacha").getJSONObject(poolObjecName).put(poolId, PoolJson);
/*      */         } 
/*      */ 
/*      */         
/*  679 */         Pool = UserSyncData.getJSONObject("gacha").getJSONObject(poolObjecName).getJSONObject(poolId);
/*  680 */         int cnt = Pool.getIntValue("cnt") + 1;
/*      */ 
/*      */         
/*  683 */         UserSyncData.getJSONObject("gacha").getJSONObject(poolObjecName).getJSONObject(poolId).put("cnt", Integer.valueOf(cnt));
/*  684 */         UserSyncData.getJSONObject("status").put("gachaCount", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gachaCount") + 1));
/*      */ 
/*      */         
/*  687 */         if (cnt == 10 && Pool.getBoolean("avail").booleanValue()) {
/*  688 */           UserSyncData.getJSONObject("gacha").getJSONObject(poolObjecName).getJSONObject(poolId).put("avail", Boolean.valueOf(false));
/*  689 */           Minimum = Boolean.valueOf(true);
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  697 */       JSONArray availCharInfo = poolJson.getJSONObject("detailInfo").getJSONObject("availCharInfo").getJSONArray("perAvailList");
/*      */       
/*  699 */       JSONArray upCharInfo = poolJson.getJSONObject("detailInfo").getJSONObject("upCharInfo").getJSONArray("perCharList");
/*      */ 
/*      */       
/*  702 */       JSONArray randomRankArray = new JSONArray();
/*      */       
/*  704 */       for (int i = 0; i < availCharInfo.size(); i++) {
/*      */         
/*  706 */         int totalPercent = (int)(availCharInfo.getJSONObject(i).getFloat("totalPercent").floatValue() * 200.0F);
/*  707 */         int rarityRank = availCharInfo.getJSONObject(i).getIntValue("rarityRank");
/*      */ 
/*      */ 
/*      */         
/*  711 */         if (rarityRank == 5) {
/*  712 */           totalPercent += (UserSyncData.getJSONObject("status").getIntValue("gachaCount") + 50) / 50 * 2;
/*      */         }
/*      */ 
/*      */         
/*  716 */         if (!Minimum.booleanValue() || 
/*  717 */           rarityRank >= Pool.getIntValue("rarity")) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  722 */           JSONObject randomRankObject = new JSONObject(true);
/*  723 */           randomRankObject.put("rarityRank", Integer.valueOf(rarityRank));
/*  724 */           randomRankObject.put("index", Integer.valueOf(i));
/*      */ 
/*      */           
/*  727 */           IntStream.range(0, totalPercent).forEach(n -> randomRankArray.add(randomRankObject));
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  733 */       Collections.shuffle((List<?>)randomRankArray);
/*      */ 
/*      */       
/*  736 */       JSONObject randomRank = randomRankArray.getJSONObject((new Random()).nextInt(randomRankArray.size()));
/*      */ 
/*      */ 
/*      */       
/*  740 */       if (!JsonBody.getString("poolId").equals("BOOT_0_1_1") && 
/*  741 */         randomRank.getIntValue("rarityRank") >= Pool.getIntValue("rarity")) {
/*  742 */         UserSyncData.getJSONObject("gacha").getJSONObject(poolObjecName).getJSONObject(poolId).put("avail", Boolean.valueOf(false));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  747 */       if (randomRank.getIntValue("rarityRank") == 5) {
/*  748 */         UserSyncData.getJSONObject("status").put("gachaCount", Integer.valueOf(0));
/*      */       }
/*      */ 
/*      */       
/*  752 */       JSONArray randomCharArray = availCharInfo.getJSONObject(randomRank.getIntValue("index")).getJSONArray("charIdList");
/*      */       
/*  754 */       for (int j = 0; j < upCharInfo.size(); j++) {
/*      */         
/*  756 */         if (upCharInfo.getJSONObject(j).getIntValue("rarityRank") == randomRank.getIntValue("rarityRank")) {
/*      */           
/*  758 */           int percent = (int)(upCharInfo.getJSONObject(j).getFloat("percent").floatValue() * 100.0F) - 15;
/*      */           
/*  760 */           JSONArray upCharIdList = upCharInfo.getJSONObject(j).getJSONArray("charIdList");
/*      */           
/*  762 */           for (int n = 0; n < upCharIdList.size(); n++) {
/*  763 */             String charId = upCharIdList.getString(n);
/*  764 */             IntStream.range(0, percent).forEach(p -> randomCharArray.add(charId));
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  771 */       Collections.shuffle((List<?>)randomCharArray);
/*      */ 
/*      */       
/*  774 */       String randomCharId = randomCharArray.getString((new Random()).nextInt(randomCharArray.size()));
/*      */ 
/*      */       
/*  777 */       int repeatCharId = 0;
/*      */       
/*  779 */       for (int k = 0; k < UserSyncData.getJSONObject("troop").getJSONObject("chars").size(); k++) {
/*  780 */         if (UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(k + 1)).getString("charId").equals(randomCharId)) {
/*  781 */           repeatCharId = k + 1;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  786 */       if (repeatCharId == 0) {
/*      */         
/*  788 */         JSONObject get_char = new JSONObject(true);
/*      */ 
/*      */         
/*  791 */         JSONObject char_data = new JSONObject(true);
/*  792 */         JSONArray skilsArray = ArknightsApplication.characterJson.getJSONObject(randomCharId).getJSONArray("skills");
/*  793 */         JSONArray skils = new JSONArray();
/*      */         
/*  795 */         for (int m = 0; m < skilsArray.size(); m++) {
/*  796 */           JSONObject new_skils = new JSONObject(true);
/*  797 */           new_skils.put("skillId", skilsArray.getJSONObject(m).getString("skillId"));
/*  798 */           new_skils.put("state", Integer.valueOf(0));
/*  799 */           new_skils.put("specializeLevel", Integer.valueOf(0));
/*  800 */           new_skils.put("completeUpgradeTime", Integer.valueOf(-1));
/*  801 */           if (skilsArray.getJSONObject(m).getJSONObject("unlockCond").getIntValue("phase") == 0) {
/*  802 */             new_skils.put("unlock", Integer.valueOf(1));
/*      */           } else {
/*  804 */             new_skils.put("unlock", Integer.valueOf(0));
/*      */           } 
/*  806 */           skils.add(new_skils);
/*      */         } 
/*      */         
/*  809 */         int instId = UserSyncData.getJSONObject("troop").getJSONObject("chars").size() + 1;
/*  810 */         char_data.put("instId", Integer.valueOf(instId));
/*  811 */         char_data.put("charId", randomCharId);
/*  812 */         char_data.put("favorPoint", Integer.valueOf(0));
/*  813 */         char_data.put("potentialRank", Integer.valueOf(0));
/*  814 */         char_data.put("mainSkillLvl", Integer.valueOf(1));
/*  815 */         char_data.put("skin", randomCharId + "#1");
/*  816 */         char_data.put("level", Integer.valueOf(1));
/*  817 */         char_data.put("exp", Integer.valueOf(0));
/*  818 */         char_data.put("evolvePhase", Integer.valueOf(0));
/*  819 */         char_data.put("gainTime", Long.valueOf((new Date()).getTime() / 1000L));
/*  820 */         char_data.put("skills", skils);
/*      */         
/*  822 */         char_data.put("voiceLan", ArknightsApplication.charwordTable.getJSONObject("charDefaultTypeDict").getString(randomCharId));
/*      */         
/*  824 */         if (skils == new JSONArray()) {
/*  825 */           char_data.put("defaultSkillIndex", Integer.valueOf(-1));
/*      */         } else {
/*  827 */           char_data.put("defaultSkillIndex", Integer.valueOf(0));
/*      */         } 
/*      */ 
/*      */         
/*  831 */         String sub1 = randomCharId.substring(randomCharId.indexOf("_") + 1);
/*  832 */         String charName = sub1.substring(sub1.indexOf("_") + 1);
/*      */         
/*  834 */         if (ArknightsApplication.uniequipTable.containsKey("uniequip_001_" + charName)) {
/*  835 */           JSONObject equip = new JSONObject(true);
/*  836 */           JSONObject uniequip_001 = new JSONObject(true);
/*  837 */           uniequip_001.put("hide", Integer.valueOf(0));
/*  838 */           uniequip_001.put("locked", Integer.valueOf(0));
/*  839 */           uniequip_001.put("level", Integer.valueOf(1));
/*  840 */           JSONObject uniequip_002 = new JSONObject(true);
/*  841 */           uniequip_002.put("hide", Integer.valueOf(0));
/*  842 */           uniequip_002.put("locked", Integer.valueOf(0));
/*  843 */           uniequip_002.put("level", Integer.valueOf(1));
/*  844 */           equip.put("uniequip_001_" + charName, uniequip_001);
/*  845 */           equip.put("uniequip_002_" + charName, uniequip_002);
/*  846 */           char_data.put("equip", equip);
/*  847 */           char_data.put("currentEquip", "uniequip_001_" + charName);
/*      */         } else {
/*  849 */           char_data.put("currentEquip", null);
/*      */         } 
/*      */         
/*  852 */         UserSyncData.getJSONObject("troop").getJSONObject("chars").put(String.valueOf(instId), char_data);
/*      */         
/*  854 */         JSONObject charGroup = new JSONObject(true);
/*  855 */         charGroup.put("favorPoint", Integer.valueOf(0));
/*  856 */         UserSyncData.getJSONObject("troop").getJSONObject("charGroup").put(randomCharId, charGroup);
/*      */         
/*  858 */         JSONObject buildingChar = new JSONObject(true);
/*  859 */         buildingChar.put("charId", randomCharId);
/*  860 */         buildingChar.put("lastApAddTime", Long.valueOf((new Date()).getTime() / 1000L));
/*  861 */         buildingChar.put("ap", Integer.valueOf(8640000));
/*  862 */         buildingChar.put("roomSlotId", "");
/*  863 */         buildingChar.put("index", Integer.valueOf(-1));
/*  864 */         buildingChar.put("changeScale", Integer.valueOf(0));
/*  865 */         JSONObject bubble = new JSONObject(true);
/*  866 */         JSONObject normal = new JSONObject(true);
/*  867 */         normal.put("add", Integer.valueOf(-1));
/*  868 */         normal.put("ts", Integer.valueOf(0));
/*  869 */         bubble.put("normal", normal);
/*  870 */         JSONObject assist = new JSONObject(true);
/*  871 */         assist.put("add", Integer.valueOf(-1));
/*  872 */         assist.put("ts", Integer.valueOf(-1));
/*  873 */         bubble.put("assist", assist);
/*  874 */         buildingChar.put("bubble", bubble);
/*  875 */         buildingChar.put("workTime", Integer.valueOf(0));
/*      */         
/*  877 */         UserSyncData.getJSONObject("building").getJSONObject("chars").put(String.valueOf(instId), buildingChar);
/*      */         
/*  879 */         get_char.put("charInstId", Integer.valueOf(instId));
/*  880 */         get_char.put("charId", randomCharId);
/*  881 */         get_char.put("isNew", Integer.valueOf(1));
/*      */         
/*  883 */         JSONArray itemGet = new JSONArray();
/*      */         
/*  885 */         JSONObject new_itemGet_1 = new JSONObject(true);
/*  886 */         new_itemGet_1.put("type", "HGG_SHD");
/*  887 */         new_itemGet_1.put("id", "4004");
/*  888 */         new_itemGet_1.put("count", Integer.valueOf(1));
/*  889 */         itemGet.add(new_itemGet_1);
/*      */         
/*  891 */         UserSyncData.getJSONObject("status").put("hggShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("hggShard") + 1));
/*      */         
/*  893 */         get_char.put("itemGet", itemGet);
/*  894 */         UserSyncData.getJSONObject("inventory").put("p_" + randomCharId, Integer.valueOf(0));
/*  895 */         gachaResultList.add(get_char);
/*  896 */         newChars.add(get_char);
/*  897 */         charGet = get_char;
/*      */         
/*  899 */         JSONObject charinstId = new JSONObject(true);
/*  900 */         charinstId.put(String.valueOf(instId), char_data);
/*  901 */         chars.put(String.valueOf(instId), char_data);
/*  902 */         troop.put("chars", charinstId);
/*      */       }
/*      */       else {
/*      */         
/*  906 */         JSONObject get_char = new JSONObject(true);
/*      */ 
/*      */         
/*  909 */         get_char.put("charInstId", Integer.valueOf(repeatCharId));
/*  910 */         get_char.put("charId", randomCharId);
/*  911 */         get_char.put("isNew", Integer.valueOf(0));
/*      */         
/*  913 */         JSONObject repatChar = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(repeatCharId));
/*      */         
/*  915 */         int potentialRank = repatChar.getIntValue("potentialRank");
/*  916 */         int rarity = randomRank.getIntValue("rarityRank");
/*      */         
/*  918 */         String itemName = null;
/*  919 */         String itemType = null;
/*  920 */         String itemId = null;
/*  921 */         int itemCount = 0;
/*  922 */         if (rarity == 0) {
/*  923 */           itemName = "lggShard";
/*  924 */           itemType = "LGG_SHD";
/*  925 */           itemId = "4005";
/*  926 */           itemCount = 1;
/*      */         } 
/*  928 */         if (rarity == 1) {
/*  929 */           itemName = "lggShard";
/*  930 */           itemType = "LGG_SHD";
/*  931 */           itemId = "4005";
/*  932 */           itemCount = 1;
/*      */         } 
/*  934 */         if (rarity == 2) {
/*  935 */           itemName = "lggShard";
/*  936 */           itemType = "LGG_SHD";
/*  937 */           itemId = "4005";
/*  938 */           itemCount = 5;
/*      */         } 
/*  940 */         if (rarity == 3) {
/*  941 */           itemName = "lggShard";
/*  942 */           itemType = "LGG_SHD";
/*  943 */           itemId = "4005";
/*  944 */           itemCount = 30;
/*      */         } 
/*  946 */         if (rarity == 4) {
/*  947 */           itemName = "hggShard";
/*  948 */           itemType = "HGG_SHD";
/*  949 */           itemId = "4004";
/*  950 */           if (potentialRank != 5) {
/*  951 */             itemCount = 5;
/*      */           } else {
/*  953 */             itemCount = 8;
/*      */           } 
/*      */         } 
/*  956 */         if (rarity == 5) {
/*  957 */           itemName = "hggShard";
/*  958 */           itemType = "HGG_SHD";
/*  959 */           itemId = "4004";
/*  960 */           if (potentialRank != 5) {
/*  961 */             itemCount = 10;
/*      */           } else {
/*  963 */             itemCount = 15;
/*      */           } 
/*      */         } 
/*      */         
/*  967 */         JSONArray itemGet = new JSONArray();
/*  968 */         JSONObject new_itemGet_1 = new JSONObject(true);
/*  969 */         new_itemGet_1.put("type", itemType);
/*  970 */         new_itemGet_1.put("id", itemId);
/*  971 */         new_itemGet_1.put("count", Integer.valueOf(itemCount));
/*  972 */         itemGet.add(new_itemGet_1);
/*      */         
/*  974 */         UserSyncData.getJSONObject("status").put(itemName, Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue(itemName) + count));
/*      */         
/*  976 */         JSONObject new_itemGet_3 = new JSONObject(true);
/*  977 */         new_itemGet_3.put("type", "MATERIAL");
/*  978 */         new_itemGet_3.put("id", "p_" + randomCharId);
/*  979 */         new_itemGet_3.put("count", Integer.valueOf(1));
/*  980 */         itemGet.add(new_itemGet_3);
/*  981 */         get_char.put("itemGet", itemGet);
/*  982 */         UserSyncData.getJSONObject("inventory").put("p_" + randomCharId, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("p_" + randomCharId) + 1));
/*      */         
/*  984 */         gachaResultList.add(get_char);
/*  985 */         charGet = get_char;
/*      */         
/*  987 */         JSONObject charinstId = new JSONObject(true);
/*  988 */         charinstId.put(String.valueOf(repeatCharId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(repeatCharId)));
/*  989 */         chars.put(String.valueOf(repeatCharId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(repeatCharId)));
/*  990 */         troop.put("chars", charinstId);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  995 */     if (useTkt == 1 || useTkt == 2) {
/*      */       
/*  997 */       UserSyncData.getJSONObject("status").put(type, Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue(type) - 1));
/*      */     } else {
/*      */       
/* 1000 */       UserSyncData.getJSONObject("status").put("diamondShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("diamondShard") - useDiamondShard));
/*      */     } 
/*      */     
/* 1003 */     UserSyncData.getJSONObject("troop").put("chars", chars);
/*      */     
/* 1005 */     JSONObject playerDataDelta = new JSONObject(true);
/* 1006 */     JSONObject modified = new JSONObject(true);
/*      */     
/* 1008 */     modified.put("troop", UserSyncData.getJSONObject("troop"));
/* 1009 */     modified.put("consumable", UserSyncData.getJSONObject("consumable"));
/* 1010 */     modified.put("status", UserSyncData.getJSONObject("status"));
/* 1011 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 1012 */     modified.put("gacha", UserSyncData.getJSONObject("gacha"));
/* 1013 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 1014 */     playerDataDelta.put("modified", modified);
/*      */     
/* 1016 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 1018 */     JSONObject result = new JSONObject(true);
/* 1019 */     result.put("result", Integer.valueOf(0));
/* 1020 */     result.put("charGet", charGet);
/* 1021 */     result.put("gachaResultList", gachaResultList);
/* 1022 */     result.put("playerDataDelta", playerDataDelta);
/* 1023 */     return result;
/*      */   }
/*      */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\gacha.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */