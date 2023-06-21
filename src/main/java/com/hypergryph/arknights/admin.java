/*     */ package BOOT-INF.classes.com.hypergryph.arknights;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.web.bind.annotation.RequestHeader;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/admin"})
/*     */ public class admin
/*     */ {
/*     */   public static void GM_GiveItem(JSONObject UserSyncData, String reward_id, String reward_type, int reward_count, JSONArray items) {
/*  26 */     JSONObject chars = UserSyncData.getJSONObject("troop").getJSONObject("chars");
/*  27 */     JSONObject troop = new JSONObject(true);
/*     */     
/*  29 */     if (reward_type.equals("CHAR")) {
/*  30 */       JSONObject item = new JSONObject(true);
/*     */ 
/*     */       
/*  33 */       String randomCharId = reward_id;
/*  34 */       int repeatCharId = 0;
/*     */       
/*  36 */       for (int j = 0; j < UserSyncData.getJSONObject("troop").getJSONObject("chars").size(); j++) {
/*  37 */         if (UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(j + 1)).getString("charId").equals(randomCharId)) {
/*  38 */           repeatCharId = j + 1;
/*     */           break;
/*     */         } 
/*     */       } 
/*  42 */       if (repeatCharId == 0) {
/*     */         
/*  44 */         JSONObject get_char = new JSONObject(true);
/*     */ 
/*     */         
/*  47 */         JSONObject char_data = new JSONObject(true);
/*  48 */         JSONArray skilsArray = ArknightsApplication.characterJson.getJSONObject(randomCharId).getJSONArray("skills");
/*  49 */         JSONArray skils = new JSONArray();
/*     */         
/*  51 */         for (int m = 0; m < skilsArray.size(); m++) {
/*  52 */           JSONObject new_skils = new JSONObject(true);
/*  53 */           new_skils.put("skillId", skilsArray.getJSONObject(m).getString("skillId"));
/*  54 */           new_skils.put("state", Integer.valueOf(0));
/*  55 */           new_skils.put("specializeLevel", Integer.valueOf(0));
/*  56 */           new_skils.put("completeUpgradeTime", Integer.valueOf(-1));
/*  57 */           if (skilsArray.getJSONObject(m).getJSONObject("unlockCond").getIntValue("phase") == 0) {
/*  58 */             new_skils.put("unlock", Integer.valueOf(1));
/*     */           } else {
/*  60 */             new_skils.put("unlock", Integer.valueOf(0));
/*     */           } 
/*  62 */           skils.add(new_skils);
/*     */         } 
/*     */         
/*  65 */         int instId = UserSyncData.getJSONObject("troop").getJSONObject("chars").size() + 1;
/*  66 */         char_data.put("instId", Integer.valueOf(instId));
/*  67 */         char_data.put("charId", randomCharId);
/*  68 */         char_data.put("favorPoint", Integer.valueOf(0));
/*  69 */         char_data.put("potentialRank", Integer.valueOf(0));
/*  70 */         char_data.put("mainSkillLvl", Integer.valueOf(1));
/*  71 */         char_data.put("skin", randomCharId + "#1");
/*  72 */         char_data.put("level", Integer.valueOf(1));
/*  73 */         char_data.put("exp", Integer.valueOf(0));
/*  74 */         char_data.put("evolvePhase", Integer.valueOf(0));
/*  75 */         char_data.put("gainTime", Long.valueOf((new Date()).getTime() / 1000L));
/*  76 */         char_data.put("skills", skils);
/*  77 */         char_data.put("voiceLan", ArknightsApplication.charwordTable.getJSONObject("charDefaultTypeDict").getString(randomCharId));
/*  78 */         if (skils == new JSONArray()) {
/*  79 */           char_data.put("defaultSkillIndex", Integer.valueOf(-1));
/*     */         } else {
/*  81 */           char_data.put("defaultSkillIndex", Integer.valueOf(0));
/*     */         } 
/*     */         
/*  84 */         String sub1 = randomCharId.substring(randomCharId.indexOf("_") + 1);
/*  85 */         String charName = sub1.substring(sub1.indexOf("_") + 1);
/*     */         
/*  87 */         if (ArknightsApplication.uniequipTable.containsKey("uniequip_001_" + charName)) {
/*  88 */           JSONObject equip = new JSONObject(true);
/*  89 */           JSONObject uniequip_001 = new JSONObject(true);
/*  90 */           uniequip_001.put("hide", Integer.valueOf(0));
/*  91 */           uniequip_001.put("locked", Integer.valueOf(0));
/*  92 */           uniequip_001.put("level", Integer.valueOf(1));
/*  93 */           JSONObject uniequip_002 = new JSONObject(true);
/*  94 */           uniequip_002.put("hide", Integer.valueOf(0));
/*  95 */           uniequip_002.put("locked", Integer.valueOf(0));
/*  96 */           uniequip_002.put("level", Integer.valueOf(1));
/*  97 */           equip.put("uniequip_001_" + charName, uniequip_001);
/*  98 */           equip.put("uniequip_002_" + charName, uniequip_002);
/*  99 */           char_data.put("equip", equip);
/* 100 */           char_data.put("currentEquip", "uniequip_001_" + charName);
/*     */         } else {
/* 102 */           char_data.put("currentEquip", null);
/*     */         } 
/*     */         
/* 105 */         UserSyncData.getJSONObject("troop").getJSONObject("chars").put(String.valueOf(instId), char_data);
/*     */         
/* 107 */         UserSyncData.getJSONObject("troop").put("curCharInstId", Integer.valueOf(instId + 1));
/* 108 */         JSONObject charGroup = new JSONObject(true);
/* 109 */         charGroup.put("favorPoint", Integer.valueOf(0));
/* 110 */         UserSyncData.getJSONObject("troop").getJSONObject("charGroup").put(randomCharId, charGroup);
/*     */         
/* 112 */         JSONObject buildingChar = new JSONObject(true);
/* 113 */         buildingChar.put("charId", randomCharId);
/* 114 */         buildingChar.put("lastApAddTime", Long.valueOf((new Date()).getTime() / 1000L));
/* 115 */         buildingChar.put("ap", Integer.valueOf(8640000));
/* 116 */         buildingChar.put("roomSlotId", "");
/* 117 */         buildingChar.put("index", Integer.valueOf(-1));
/* 118 */         buildingChar.put("changeScale", Integer.valueOf(0));
/* 119 */         JSONObject bubble = new JSONObject(true);
/* 120 */         JSONObject normal = new JSONObject(true);
/* 121 */         normal.put("add", Integer.valueOf(-1));
/* 122 */         normal.put("ts", Integer.valueOf(0));
/* 123 */         bubble.put("normal", normal);
/* 124 */         JSONObject assist = new JSONObject(true);
/* 125 */         assist.put("add", Integer.valueOf(-1));
/* 126 */         assist.put("ts", Integer.valueOf(-1));
/* 127 */         bubble.put("assist", assist);
/* 128 */         buildingChar.put("bubble", bubble);
/* 129 */         buildingChar.put("workTime", Integer.valueOf(0));
/*     */         
/* 131 */         UserSyncData.getJSONObject("building").getJSONObject("chars").put(String.valueOf(instId), buildingChar);
/*     */         
/* 133 */         get_char.put("charInstId", Integer.valueOf(instId));
/* 134 */         get_char.put("charId", randomCharId);
/*     */ 
/*     */         
/* 137 */         get_char.put("isNew", Integer.valueOf(1));
/*     */         
/* 139 */         JSONArray itemGet = new JSONArray();
/*     */         
/* 141 */         JSONObject new_itemGet_1 = new JSONObject(true);
/* 142 */         new_itemGet_1.put("type", "HGG_SHD");
/* 143 */         new_itemGet_1.put("id", "4004");
/* 144 */         new_itemGet_1.put("count", Integer.valueOf(1));
/* 145 */         itemGet.add(new_itemGet_1);
/*     */         
/* 147 */         UserSyncData.getJSONObject("status").put("hggShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("hggShard") + 1));
/*     */         
/* 149 */         get_char.put("itemGet", itemGet);
/* 150 */         UserSyncData.getJSONObject("inventory").put("p_" + randomCharId, Integer.valueOf(0));
/*     */         
/* 152 */         JSONObject charGet = get_char;
/*     */         
/* 154 */         JSONObject charinstId = new JSONObject(true);
/* 155 */         charinstId.put(String.valueOf(instId), char_data);
/* 156 */         chars.put(String.valueOf(instId), char_data);
/* 157 */         troop.put("chars", charinstId);
/*     */         
/* 159 */         item.put("id", randomCharId);
/* 160 */         item.put("type", reward_type);
/* 161 */         item.put("charGet", charGet);
/* 162 */         items.add(item);
/*     */       } else {
/*     */         
/* 165 */         JSONObject get_char = new JSONObject(true);
/*     */ 
/*     */         
/* 168 */         get_char.put("charInstId", Integer.valueOf(repeatCharId));
/* 169 */         get_char.put("charId", randomCharId);
/* 170 */         get_char.put("isNew", Integer.valueOf(0));
/*     */         
/* 172 */         JSONObject repatChar = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(repeatCharId));
/*     */         
/* 174 */         int potentialRank = repatChar.getIntValue("potentialRank");
/* 175 */         int rarity = ArknightsApplication.characterJson.getJSONObject(randomCharId).getIntValue("rarity");
/*     */         
/* 177 */         String itemName = null;
/* 178 */         String itemType = null;
/* 179 */         String itemId = null;
/* 180 */         int itemCount = 0;
/* 181 */         if (rarity == 0) {
/* 182 */           itemName = "lggShard";
/* 183 */           itemType = "LGG_SHD";
/* 184 */           itemId = "4005";
/* 185 */           itemCount = 1;
/*     */         } 
/* 187 */         if (rarity == 1) {
/* 188 */           itemName = "lggShard";
/* 189 */           itemType = "LGG_SHD";
/* 190 */           itemId = "4005";
/* 191 */           itemCount = 1;
/*     */         } 
/* 193 */         if (rarity == 2) {
/* 194 */           itemName = "lggShard";
/* 195 */           itemType = "LGG_SHD";
/* 196 */           itemId = "4005";
/* 197 */           itemCount = 5;
/*     */         } 
/* 199 */         if (rarity == 3) {
/* 200 */           itemName = "lggShard";
/* 201 */           itemType = "LGG_SHD";
/* 202 */           itemId = "4005";
/* 203 */           itemCount = 30;
/*     */         } 
/* 205 */         if (rarity == 4) {
/* 206 */           itemName = "hggShard";
/* 207 */           itemType = "HGG_SHD";
/* 208 */           itemId = "4004";
/* 209 */           if (potentialRank != 5) {
/* 210 */             itemCount = 5;
/*     */           } else {
/* 212 */             itemCount = 8;
/*     */           } 
/*     */         } 
/* 215 */         if (rarity == 5) {
/* 216 */           itemName = "hggShard";
/* 217 */           itemType = "HGG_SHD";
/* 218 */           itemId = "4004";
/* 219 */           if (potentialRank != 5) {
/* 220 */             itemCount = 10;
/*     */           } else {
/* 222 */             itemCount = 15;
/*     */           } 
/*     */         } 
/*     */         
/* 226 */         JSONArray itemGet = new JSONArray();
/* 227 */         JSONObject new_itemGet_1 = new JSONObject(true);
/* 228 */         new_itemGet_1.put("type", itemType);
/* 229 */         new_itemGet_1.put("id", itemId);
/* 230 */         new_itemGet_1.put("count", Integer.valueOf(itemCount));
/* 231 */         itemGet.add(new_itemGet_1);
/* 232 */         UserSyncData.getJSONObject("status").put(itemName, Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue(itemName) + itemCount));
/*     */         
/* 234 */         JSONObject new_itemGet_3 = new JSONObject(true);
/* 235 */         new_itemGet_3.put("type", "MATERIAL");
/* 236 */         new_itemGet_3.put("id", "p_" + randomCharId);
/* 237 */         new_itemGet_3.put("count", Integer.valueOf(1));
/* 238 */         itemGet.add(new_itemGet_3);
/* 239 */         get_char.put("itemGet", itemGet);
/* 240 */         UserSyncData.getJSONObject("inventory").put("p_" + randomCharId, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("p_" + randomCharId) + 1));
/*     */         
/* 242 */         JSONObject charGet = get_char;
/*     */         
/* 244 */         JSONObject charinstId = new JSONObject(true);
/* 245 */         charinstId.put(String.valueOf(repeatCharId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(repeatCharId)));
/* 246 */         chars.put(String.valueOf(repeatCharId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(repeatCharId)));
/* 247 */         troop.put("chars", charinstId);
/*     */         
/* 249 */         item.put("type", reward_type);
/* 250 */         item.put("id", randomCharId);
/* 251 */         item.put("charGet", charGet);
/* 252 */         items.add(item);
/*     */       } 
/*     */     } 
/*     */     
/* 256 */     if (reward_type.equals("HGG_SHD")) {
/* 257 */       UserSyncData.getJSONObject("status").put("practiceTicket", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("hggShard") + reward_count));
/*     */     }
/* 259 */     if (reward_type.equals("LGG_SHD")) {
/* 260 */       UserSyncData.getJSONObject("status").put("practiceTicket", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("lggShard") + reward_count));
/*     */     }
/* 262 */     if (reward_type.equals("MATERIAL")) {
/* 263 */       UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*     */     }
/* 265 */     if (reward_type.equals("CARD_EXP")) {
/* 266 */       UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*     */     }
/* 268 */     if (reward_type.equals("SOCIAL_PT")) {
/* 269 */       UserSyncData.getJSONObject("status").put("socialPoint", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("socialPoint") + reward_count));
/*     */     }
/* 271 */     if (reward_type.equals("AP_GAMEPLAY")) {
/* 272 */       UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") + reward_count));
/*     */     }
/* 274 */     if (reward_type.equals("AP_ITEM")) {
/* 275 */       if (reward_id.contains("60")) {
/* 276 */         UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") + 60));
/* 277 */       } else if (reward_id.contains("200")) {
/* 278 */         UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") + 200));
/*     */       } else {
/* 280 */         UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") + 100));
/*     */       } 
/*     */     }
/* 283 */     if (reward_type.equals("TKT_TRY")) {
/* 284 */       UserSyncData.getJSONObject("status").put("practiceTicket", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("practiceTicket") + reward_count));
/*     */     }
/* 286 */     if (reward_type.equals("DIAMOND")) {
/* 287 */       UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + reward_count));
/* 288 */       UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + reward_count));
/*     */     } 
/* 290 */     if (reward_type.equals("DIAMOND_SHD")) {
/* 291 */       UserSyncData.getJSONObject("status").put("diamondShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("diamondShard") + reward_count));
/*     */     }
/* 293 */     if (reward_type.equals("GOLD")) {
/* 294 */       UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + reward_count));
/*     */     }
/* 296 */     if (reward_type.equals("TKT_RECRUIT")) {
/* 297 */       UserSyncData.getJSONObject("status").put("recruitLicense", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("recruitLicense") + reward_count));
/*     */     }
/* 299 */     if (reward_type.equals("TKT_INST_FIN")) {
/* 300 */       UserSyncData.getJSONObject("status").put("instantFinishTicket", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("instantFinishTicket") + reward_count));
/*     */     }
/* 302 */     if (reward_type.equals("TKT_GACHA_PRSV")) {
/* 303 */       UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*     */     }
/* 305 */     if (reward_type.equals("RENAMING_CARD")) {
/* 306 */       UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*     */     }
/* 308 */     if (reward_type.equals("RETRO_COIN")) {
/* 309 */       UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*     */     }
/* 311 */     if (reward_type.equals("AP_SUPPLY")) {
/* 312 */       UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*     */     }
/* 314 */     if (reward_type.equals("TKT_GACHA_10")) {
/* 315 */       UserSyncData.getJSONObject("status").put("tenGachaTicket", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("tenGachaTicket") + reward_count));
/*     */     }
/* 317 */     if (reward_type.equals("TKT_GACHA")) {
/* 318 */       UserSyncData.getJSONObject("status").put("gachaTicket", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gachaTicket") + reward_count));
/*     */     }
/* 320 */     if (reward_type.indexOf("VOUCHER") != -1) {
/* 321 */       if (!UserSyncData.getJSONObject("consumable").containsKey(reward_id)) {
/* 322 */         JSONObject consumables = new JSONObject(true);
/* 323 */         JSONObject consumable = new JSONObject(true);
/* 324 */         consumable.put("ts", Integer.valueOf(-1));
/* 325 */         consumable.put("count", Integer.valueOf(0));
/* 326 */         consumables.put("0", consumable);
/* 327 */         UserSyncData.getJSONObject("consumable").put(reward_id, consumables);
/*     */       } 
/* 329 */       UserSyncData.getJSONObject("consumable").getJSONObject(reward_id).getJSONObject("0").put("count", Integer.valueOf(UserSyncData.getJSONObject("consumable").getJSONObject(reward_id).getJSONObject("0").getIntValue("count") + reward_count));
/*     */     } 
/* 331 */     if (reward_type.equals("CHAR_SKIN")) {
/* 332 */       UserSyncData.getJSONObject("skin").getJSONObject("characterSkins").put(reward_id, Integer.valueOf(1));
/* 333 */       UserSyncData.getJSONObject("skin").getJSONObject("skinTs").put(reward_id, Long.valueOf((new Date()).getTime() / 1000L));
/*     */     } 
/*     */     
/* 336 */     if (!reward_type.equals("CHAR")) {
/* 337 */       JSONObject item = new JSONObject(true);
/* 338 */       item.put("id", reward_id);
/* 339 */       item.put("type", reward_type);
/* 340 */       item.put("count", Integer.valueOf(reward_count));
/* 341 */       items.add(item);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/send/character"})
/*     */   public JSONObject character(@RequestHeader("GMKey") String GMKey, @RequestParam Long uid, @RequestParam String charId) {
/* 349 */     if (!ArknightsApplication.serverConfig.getJSONObject("server").getString("GMKey").equals(GMKey)) {
/* 350 */       JSONObject jSONObject = new JSONObject(true);
/* 351 */       jSONObject.put("code", Integer.valueOf(401));
/* 352 */       jSONObject.put("msg", "Unauthorized");
/* 353 */       return jSONObject;
/*     */     } 
/*     */     
/* 356 */     List<Account> user = userDao.queryAccountByUid(uid.longValue());
/*     */     
/* 358 */     if (user.size() != 1) {
/* 359 */       JSONObject jSONObject = new JSONObject(true);
/* 360 */       jSONObject.put("code", Integer.valueOf(404));
/* 361 */       jSONObject.put("msg", "无法找到该玩家的存档");
/* 362 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 366 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)user.get(0)).getUser());
/*     */     
/* 368 */     JSONArray items = new JSONArray();
/* 369 */     GM_GiveItem(UserSyncData, charId, "CHAR", 1, items);
/*     */     
/* 371 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 373 */     JSONObject result = new JSONObject(true);
/* 374 */     result.put("code", Integer.valueOf(200));
/* 375 */     result.put("items", items);
/* 376 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/send/item"})
/*     */   public JSONObject item(@RequestHeader("GMKey") String GMKey, @RequestParam Long uid, @RequestParam String itemType, @RequestParam String itemId, @RequestParam int count) {
/* 383 */     if (!ArknightsApplication.serverConfig.getJSONObject("server").getString("GMKey").equals(GMKey)) {
/* 384 */       JSONObject jSONObject = new JSONObject(true);
/* 385 */       jSONObject.put("code", Integer.valueOf(401));
/* 386 */       jSONObject.put("msg", "Unauthorized");
/* 387 */       return jSONObject;
/*     */     } 
/*     */     
/* 390 */     List<Account> user = userDao.queryAccountByUid(uid.longValue());
/*     */     
/* 392 */     if (user.size() != 1) {
/* 393 */       JSONObject jSONObject = new JSONObject(true);
/* 394 */       jSONObject.put("code", Integer.valueOf(404));
/* 395 */       jSONObject.put("msg", "无法找到该玩家的存档");
/* 396 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 400 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)user.get(0)).getUser());
/*     */     
/* 402 */     JSONArray items = new JSONArray();
/* 403 */     GM_GiveItem(UserSyncData, itemId, itemType, count, items);
/*     */     
/* 405 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 407 */     JSONObject result = new JSONObject(true);
/* 408 */     result.put("code", Integer.valueOf(200));
/* 409 */     result.put("items", items);
/* 410 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/charBuild/changeLevel"})
/*     */   public JSONObject level(@RequestHeader("GMKey") String GMKey, @RequestParam Long uid, @RequestParam String charId, @RequestParam int value) {
/* 417 */     if (!ArknightsApplication.serverConfig.getJSONObject("server").getString("GMKey").equals(GMKey)) {
/* 418 */       JSONObject jSONObject = new JSONObject(true);
/* 419 */       jSONObject.put("code", Integer.valueOf(401));
/* 420 */       jSONObject.put("msg", "Unauthorized");
/* 421 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 425 */     List<Account> Accounts = userDao.queryAccountByUid(uid.longValue());
/*     */     
/* 427 */     if (Accounts.size() != 1) {
/* 428 */       JSONObject jSONObject = new JSONObject(true);
/* 429 */       jSONObject.put("code", Integer.valueOf(404));
/* 430 */       jSONObject.put("msg", "无法找到该玩家的存档");
/* 431 */       return jSONObject;
/*     */     } 
/*     */     
/* 434 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 436 */     for (Map.Entry entry : UserSyncData.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 437 */       JSONObject charData = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(entry.getKey().toString());
/* 438 */       if (charData.getString("charId").equals(charId)) {
/*     */         
/* 440 */         charData.put("level", Integer.valueOf(value));
/* 441 */         UserSyncData.getJSONObject("troop").getJSONObject("chars").put(entry.getKey().toString(), charData);
/*     */         
/* 443 */         userDao.setUserData(uid, UserSyncData);
/*     */         
/* 445 */         JSONObject jSONObject = new JSONObject(true);
/* 446 */         jSONObject.put("code", Integer.valueOf(200));
/* 447 */         jSONObject.put("msg", "已改变 " + charId + " 的等级为" + value);
/* 448 */         return jSONObject;
/*     */       } 
/*     */     } 
/*     */     
/* 452 */     JSONObject result = new JSONObject(true);
/* 453 */     result.put("code", Integer.valueOf(404));
/* 454 */     result.put("msg", "该玩家尚未拥有 " + charId);
/* 455 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/charBuild/changeFavorPoint"})
/*     */   public JSONObject favorPoint(@RequestHeader("GMKey") String GMKey, @RequestParam Long uid, @RequestParam String charId, @RequestParam int value) {
/* 462 */     if (!ArknightsApplication.serverConfig.getJSONObject("server").getString("GMKey").equals(GMKey)) {
/* 463 */       JSONObject jSONObject = new JSONObject(true);
/* 464 */       jSONObject.put("code", Integer.valueOf(401));
/* 465 */       jSONObject.put("msg", "Unauthorized");
/* 466 */       return jSONObject;
/*     */     } 
/*     */     
/* 469 */     List<Account> Accounts = userDao.queryAccountByUid(uid.longValue());
/*     */     
/* 471 */     if (Accounts.size() != 1) {
/* 472 */       JSONObject jSONObject = new JSONObject(true);
/* 473 */       jSONObject.put("code", Integer.valueOf(404));
/* 474 */       jSONObject.put("msg", "无法找到该玩家的存档");
/* 475 */       return jSONObject;
/*     */     } 
/*     */     
/* 478 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 480 */     for (Map.Entry entry : UserSyncData.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 481 */       JSONObject charData = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(entry.getKey().toString());
/* 482 */       if (charData.getString("charId").equals(charId)) {
/*     */         
/* 484 */         charData.put("favorPoint", Integer.valueOf(value));
/* 485 */         UserSyncData.getJSONObject("troop").getJSONObject("chars").put(entry.getKey().toString(), charData);
/*     */         
/* 487 */         userDao.setUserData(uid, UserSyncData);
/*     */         
/* 489 */         JSONObject jSONObject = new JSONObject(true);
/* 490 */         jSONObject.put("code", Integer.valueOf(200));
/* 491 */         jSONObject.put("msg", "已改变 " + charId + " 的信赖为" + value);
/* 492 */         return jSONObject;
/*     */       } 
/*     */     } 
/*     */     
/* 496 */     JSONObject result = new JSONObject(true);
/* 497 */     result.put("code", Integer.valueOf(404));
/* 498 */     result.put("msg", "该玩家尚未拥有 " + charId);
/* 499 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/charBuild/changePotentialRank"})
/*     */   public JSONObject PotentialRank(@RequestHeader("GMKey") String GMKey, @RequestParam Long uid, @RequestParam String charId, @RequestParam int value) {
/* 507 */     if (!ArknightsApplication.serverConfig.getJSONObject("server").getString("GMKey").equals(GMKey)) {
/* 508 */       JSONObject jSONObject = new JSONObject(true);
/* 509 */       jSONObject.put("code", Integer.valueOf(401));
/* 510 */       jSONObject.put("msg", "Unauthorized");
/* 511 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 516 */     List<Account> Accounts = userDao.queryAccountByUid(uid.longValue());
/*     */     
/* 518 */     if (Accounts.size() != 1) {
/* 519 */       JSONObject jSONObject = new JSONObject(true);
/* 520 */       jSONObject.put("code", Integer.valueOf(404));
/* 521 */       jSONObject.put("msg", "无法找到该玩家的存档");
/* 522 */       return jSONObject;
/*     */     } 
/*     */     
/* 525 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 527 */     for (Map.Entry entry : UserSyncData.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 528 */       JSONObject charData = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(entry.getKey().toString());
/* 529 */       if (charData.getString("charId").equals(charId)) {
/*     */         
/* 531 */         charData.put("potentialRank", Integer.valueOf(value));
/* 532 */         UserSyncData.getJSONObject("troop").getJSONObject("chars").put(entry.getKey().toString(), charData);
/*     */         
/* 534 */         userDao.setUserData(uid, UserSyncData);
/*     */         
/* 536 */         JSONObject jSONObject = new JSONObject(true);
/* 537 */         jSONObject.put("code", Integer.valueOf(200));
/* 538 */         jSONObject.put("msg", "已改变 " + charId + " 的潜能为 " + value);
/* 539 */         return jSONObject;
/*     */       } 
/*     */     } 
/*     */     
/* 543 */     JSONObject result = new JSONObject(true);
/* 544 */     result.put("code", Integer.valueOf(404));
/* 545 */     result.put("msg", "该玩家尚未拥有 " + charId);
/* 546 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/charBuild/changeMainSkillLvl"})
/*     */   public JSONObject mainSkillLvl(@RequestHeader("GMKey") String GMKey, @RequestParam Long uid, @RequestParam String charId, @RequestParam int value) {
/* 554 */     if (!ArknightsApplication.serverConfig.getJSONObject("server").getString("GMKey").equals(GMKey)) {
/* 555 */       JSONObject jSONObject = new JSONObject(true);
/* 556 */       jSONObject.put("code", Integer.valueOf(401));
/* 557 */       jSONObject.put("msg", "Unauthorized");
/* 558 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 563 */     List<Account> Accounts = userDao.queryAccountByUid(uid.longValue());
/*     */     
/* 565 */     if (Accounts.size() != 1) {
/* 566 */       JSONObject jSONObject = new JSONObject(true);
/* 567 */       jSONObject.put("code", Integer.valueOf(404));
/* 568 */       jSONObject.put("msg", "无法找到该玩家的存档");
/* 569 */       return jSONObject;
/*     */     } 
/*     */     
/* 572 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 574 */     for (Map.Entry entry : UserSyncData.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 575 */       JSONObject charData = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(entry.getKey().toString());
/* 576 */       if (charData.getString("charId").equals(charId)) {
/*     */         
/* 578 */         charData.put("mainSkillLvl", Integer.valueOf(value));
/* 579 */         UserSyncData.getJSONObject("troop").getJSONObject("chars").put(entry.getKey().toString(), charData);
/*     */         
/* 581 */         userDao.setUserData(uid, UserSyncData);
/*     */         
/* 583 */         JSONObject jSONObject = new JSONObject(true);
/* 584 */         jSONObject.put("code", Integer.valueOf(200));
/* 585 */         jSONObject.put("msg", "已改变 " + charId + " 的技能等级为 " + value);
/* 586 */         return jSONObject;
/*     */       } 
/*     */     } 
/*     */     
/* 590 */     JSONObject result = new JSONObject(true);
/* 591 */     result.put("code", Integer.valueOf(404));
/* 592 */     result.put("msg", "该玩家尚未拥有 " + charId);
/* 593 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/charBuild/changeExp"})
/*     */   public JSONObject Exp(@RequestHeader("GMKey") String GMKey, @RequestParam Long uid, @RequestParam String charId, @RequestParam int value) {
/* 600 */     if (!ArknightsApplication.serverConfig.getJSONObject("server").getString("GMKey").equals(GMKey)) {
/* 601 */       JSONObject jSONObject = new JSONObject(true);
/* 602 */       jSONObject.put("code", Integer.valueOf(401));
/* 603 */       jSONObject.put("msg", "Unauthorized");
/* 604 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 609 */     List<Account> Accounts = userDao.queryAccountByUid(uid.longValue());
/*     */     
/* 611 */     if (Accounts.size() != 1) {
/* 612 */       JSONObject jSONObject = new JSONObject(true);
/* 613 */       jSONObject.put("code", Integer.valueOf(404));
/* 614 */       jSONObject.put("msg", "无法找到该玩家的存档");
/* 615 */       return jSONObject;
/*     */     } 
/*     */     
/* 618 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 620 */     for (Map.Entry entry : UserSyncData.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 621 */       JSONObject charData = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(entry.getKey().toString());
/* 622 */       if (charData.getString("charId").equals(charId)) {
/*     */         
/* 624 */         charData.put("exp", Integer.valueOf(value));
/* 625 */         UserSyncData.getJSONObject("troop").getJSONObject("chars").put(entry.getKey().toString(), charData);
/*     */         
/* 627 */         userDao.setUserData(uid, UserSyncData);
/*     */         
/* 629 */         JSONObject jSONObject = new JSONObject(true);
/* 630 */         jSONObject.put("code", Integer.valueOf(200));
/* 631 */         jSONObject.put("msg", "已改变 " + charId + " 的经验为 " + value);
/* 632 */         return jSONObject;
/*     */       } 
/*     */     } 
/*     */     
/* 636 */     JSONObject result = new JSONObject(true);
/* 637 */     result.put("code", Integer.valueOf(404));
/* 638 */     result.put("msg", "该玩家尚未拥有 " + charId);
/* 639 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/charBuild/changeEvolvePhase"})
/*     */   public JSONObject evolvePhase(@RequestHeader("GMKey") String GMKey, @RequestParam Long uid, @RequestParam String charId, @RequestParam int value) {
/* 645 */     if (!ArknightsApplication.serverConfig.getJSONObject("server").getString("GMKey").equals(GMKey)) {
/* 646 */       JSONObject jSONObject = new JSONObject(true);
/* 647 */       jSONObject.put("code", Integer.valueOf(401));
/* 648 */       jSONObject.put("msg", "Unauthorized");
/* 649 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 654 */     List<Account> Accounts = userDao.queryAccountByUid(uid.longValue());
/*     */     
/* 656 */     if (Accounts.size() != 1) {
/* 657 */       JSONObject jSONObject = new JSONObject(true);
/* 658 */       jSONObject.put("code", Integer.valueOf(404));
/* 659 */       jSONObject.put("msg", "无法找到该玩家的存档");
/* 660 */       return jSONObject;
/*     */     } 
/*     */     
/* 663 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 665 */     for (Map.Entry entry : UserSyncData.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 666 */       JSONObject charData = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(entry.getKey().toString());
/* 667 */       if (charData.getString("charId").equals(charId)) {
/*     */         
/* 669 */         charData.put("evolvePhase", Integer.valueOf(value));
/* 670 */         UserSyncData.getJSONObject("troop").getJSONObject("chars").put(entry.getKey().toString(), charData);
/*     */         
/* 672 */         userDao.setUserData(uid, UserSyncData);
/*     */         
/* 674 */         JSONObject jSONObject = new JSONObject(true);
/* 675 */         jSONObject.put("code", Integer.valueOf(200));
/* 676 */         jSONObject.put("msg", "已改变 " + charId + " 的精英化等级为 " + value);
/* 677 */         return jSONObject;
/*     */       } 
/*     */     } 
/*     */     
/* 681 */     JSONObject result = new JSONObject(true);
/* 682 */     result.put("code", Integer.valueOf(404));
/* 683 */     result.put("msg", "该玩家尚未拥有 " + charId);
/* 684 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/charBuild/changeDefaultSkillIndex"})
/*     */   public JSONObject defaultSkillIndex(@RequestHeader("GMKey") String GMKey, @RequestParam Long uid, @RequestParam String charId, @RequestParam int value) {
/* 690 */     if (!ArknightsApplication.serverConfig.getJSONObject("server").getString("GMKey").equals(GMKey)) {
/* 691 */       JSONObject jSONObject = new JSONObject(true);
/* 692 */       jSONObject.put("code", Integer.valueOf(401));
/* 693 */       jSONObject.put("msg", "Unauthorized");
/* 694 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 699 */     List<Account> Accounts = userDao.queryAccountByUid(uid.longValue());
/*     */     
/* 701 */     if (Accounts.size() != 1) {
/* 702 */       JSONObject jSONObject = new JSONObject(true);
/* 703 */       jSONObject.put("code", Integer.valueOf(404));
/* 704 */       jSONObject.put("msg", "无法找到该玩家的存档");
/* 705 */       return jSONObject;
/*     */     } 
/*     */     
/* 708 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 710 */     for (Map.Entry entry : UserSyncData.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 711 */       JSONObject charData = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(entry.getKey().toString());
/* 712 */       if (charData.getString("charId").equals(charId)) {
/*     */         
/* 714 */         charData.put("defaultSkillIndex", Integer.valueOf(value));
/* 715 */         UserSyncData.getJSONObject("troop").getJSONObject("chars").put(entry.getKey().toString(), charData);
/*     */         
/* 717 */         userDao.setUserData(uid, UserSyncData);
/*     */         
/* 719 */         JSONObject jSONObject = new JSONObject(true);
/* 720 */         jSONObject.put("code", Integer.valueOf(200));
/* 721 */         jSONObject.put("msg", "已改变 " + charId + " 的默认技能为 " + value);
/* 722 */         return jSONObject;
/*     */       } 
/*     */     } 
/*     */     
/* 726 */     JSONObject result = new JSONObject(true);
/* 727 */     result.put("code", Integer.valueOf(404));
/* 728 */     result.put("msg", "该玩家尚未拥有 " + charId);
/* 729 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/charBuild/changeSkin"})
/*     */   public JSONObject skin(@RequestHeader("GMKey") String GMKey, @RequestParam Long uid, @RequestParam String charId, @RequestParam String value) {
/* 735 */     if (!ArknightsApplication.serverConfig.getJSONObject("server").getString("GMKey").equals(GMKey)) {
/* 736 */       JSONObject jSONObject = new JSONObject(true);
/* 737 */       jSONObject.put("code", Integer.valueOf(401));
/* 738 */       jSONObject.put("msg", "Unauthorized");
/* 739 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 744 */     List<Account> Accounts = userDao.queryAccountByUid(uid.longValue());
/*     */     
/* 746 */     if (Accounts.size() != 1) {
/* 747 */       JSONObject jSONObject = new JSONObject(true);
/* 748 */       jSONObject.put("code", Integer.valueOf(404));
/* 749 */       jSONObject.put("msg", "无法找到该玩家的存档");
/* 750 */       return jSONObject;
/*     */     } 
/*     */     
/* 753 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 755 */     for (Map.Entry entry : UserSyncData.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 756 */       JSONObject charData = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(entry.getKey().toString());
/* 757 */       if (charData.getString("charId").equals(charId)) {
/*     */         
/* 759 */         charData.put("skin", value);
/* 760 */         UserSyncData.getJSONObject("troop").getJSONObject("chars").put(entry.getKey().toString(), charData);
/*     */         
/* 762 */         userDao.setUserData(uid, UserSyncData);
/*     */         
/* 764 */         JSONObject jSONObject = new JSONObject(true);
/* 765 */         jSONObject.put("code", Integer.valueOf(200));
/* 766 */         jSONObject.put("msg", "已改变 " + charId + " 的默认皮肤为 " + value);
/* 767 */         return jSONObject;
/*     */       } 
/*     */     } 
/*     */     
/* 771 */     JSONObject result = new JSONObject(true);
/* 772 */     result.put("code", Integer.valueOf(404));
/* 773 */     result.put("msg", "该玩家尚未拥有 " + charId);
/* 774 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/charBuild/unlockAllSkills"})
/*     */   public JSONObject unlockAllSkills(@RequestHeader("GMKey") String GMKey, @RequestParam Long uid, @RequestParam String charId) {
/* 780 */     if (!ArknightsApplication.serverConfig.getJSONObject("server").getString("GMKey").equals(GMKey)) {
/* 781 */       JSONObject jSONObject = new JSONObject(true);
/* 782 */       jSONObject.put("code", Integer.valueOf(401));
/* 783 */       jSONObject.put("msg", "Unauthorized");
/* 784 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 789 */     List<Account> Accounts = userDao.queryAccountByUid(uid.longValue());
/*     */     
/* 791 */     if (Accounts.size() != 1) {
/* 792 */       JSONObject jSONObject = new JSONObject(true);
/* 793 */       jSONObject.put("code", Integer.valueOf(404));
/* 794 */       jSONObject.put("msg", "无法找到该玩家的存档");
/* 795 */       return jSONObject;
/*     */     } 
/*     */     
/* 798 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 800 */     for (Map.Entry entry : UserSyncData.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 801 */       JSONObject charData = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(entry.getKey().toString());
/* 802 */       if (charData.getString("charId").equals(charId)) {
/*     */         
/* 804 */         for (int i = 0; i < charData.getJSONArray("skills").size(); i++) {
/* 805 */           charData.getJSONArray("skills").getJSONObject(i).put("unlock", Integer.valueOf(1));
/*     */         }
/*     */         
/* 808 */         UserSyncData.getJSONObject("troop").getJSONObject("chars").put(entry.getKey().toString(), charData);
/*     */         
/* 810 */         userDao.setUserData(uid, UserSyncData);
/*     */         
/* 812 */         JSONObject jSONObject = new JSONObject(true);
/* 813 */         jSONObject.put("code", Integer.valueOf(200));
/* 814 */         jSONObject.put("msg", "已解锁 " + charId + " 的所有技能");
/* 815 */         return jSONObject;
/*     */       } 
/*     */     } 
/*     */     
/* 819 */     JSONObject result = new JSONObject(true);
/* 820 */     result.put("code", Integer.valueOf(404));
/* 821 */     result.put("msg", "该玩家尚未拥有 " + charId);
/* 822 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/charBuild/changeSpecializeLevel"})
/*     */   public JSONObject UpSpecializeLevel(@RequestHeader("GMKey") String GMKey, @RequestParam Long uid, @RequestParam String charId, @RequestParam int value) {
/* 828 */     if (!ArknightsApplication.serverConfig.getJSONObject("server").getString("GMKey").equals(GMKey)) {
/* 829 */       JSONObject jSONObject = new JSONObject(true);
/* 830 */       jSONObject.put("code", Integer.valueOf(401));
/* 831 */       jSONObject.put("msg", "Unauthorized");
/* 832 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 837 */     List<Account> Accounts = userDao.queryAccountByUid(uid.longValue());
/*     */     
/* 839 */     if (Accounts.size() != 1) {
/* 840 */       JSONObject jSONObject = new JSONObject(true);
/* 841 */       jSONObject.put("code", Integer.valueOf(404));
/* 842 */       jSONObject.put("msg", "无法找到该玩家的存档");
/* 843 */       return jSONObject;
/*     */     } 
/*     */     
/* 846 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 848 */     for (Map.Entry entry : UserSyncData.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 849 */       JSONObject charData = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(entry.getKey().toString());
/* 850 */       if (charData.getString("charId").equals(charId)) {
/*     */         
/* 852 */         for (int i = 0; i < charData.getJSONArray("skills").size(); i++) {
/* 853 */           charData.getJSONArray("skills").getJSONObject(i).put("specializeLevel", Integer.valueOf(value));
/*     */         }
/*     */         
/* 856 */         UserSyncData.getJSONObject("troop").getJSONObject("chars").put(entry.getKey().toString(), charData);
/*     */         
/* 858 */         userDao.setUserData(uid, UserSyncData);
/*     */         
/* 860 */         JSONObject jSONObject = new JSONObject(true);
/* 861 */         jSONObject.put("code", Integer.valueOf(200));
/* 862 */         jSONObject.put("msg", "已把 " + charId + " 的所有技能专精提升至 " + value);
/* 863 */         return jSONObject;
/*     */       } 
/*     */     } 
/*     */     
/* 867 */     JSONObject result = new JSONObject(true);
/* 868 */     result.put("code", Integer.valueOf(404));
/* 869 */     result.put("msg", "该玩家尚未拥有 " + charId);
/* 870 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\admin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */