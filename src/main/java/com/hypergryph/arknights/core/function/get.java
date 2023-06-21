/*     */ package BOOT-INF.classes.com.hypergryph.arknights.core.function;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class get
/*     */ {
/*     */   public static Boolean GM_ItemGet(Long uid, int count, String itemType, String itemId) {
/*  22 */     List<Account> Accounts = userDao.queryAccountByUid(uid.longValue());
/*     */     
/*  24 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */ 
/*     */     
/*  27 */     if (itemType.equals("TKT_TRY")) {
/*  28 */       UserSyncData.getJSONObject("status").put("practiceTicket", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("practiceTicket") + count));
/*     */     }
/*  30 */     if (itemType.equals("DIAMOND")) {
/*  31 */       UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + count));
/*  32 */       UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + count));
/*     */     } 
/*  34 */     if (itemType.equals("DIAMOND_SHD")) {
/*  35 */       UserSyncData.getJSONObject("status").put("diamondShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("diamondShard") + count));
/*     */     }
/*  37 */     if (itemType.equals("GOLD")) {
/*  38 */       UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + count));
/*     */     }
/*  40 */     if (itemType.equals("TKT_RECRUIT")) {
/*  41 */       UserSyncData.getJSONObject("status").put("recruitLicense", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("recruitLicense") + count));
/*     */     }
/*  43 */     if (itemType.equals("TKT_INST_FIN")) {
/*  44 */       UserSyncData.getJSONObject("status").put("instantFinishTicket", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("instantFinishTicket") + count));
/*     */     }
/*  46 */     if (itemType.equals("LGG_SHD")) {
/*  47 */       UserSyncData.getJSONObject("status").put("lggShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("lggShard") + count));
/*     */     }
/*  49 */     if (itemType.equals("HGG_SHD")) {
/*  50 */       UserSyncData.getJSONObject("status").put("hggShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("hggShard") + count));
/*     */     }
/*  52 */     if (itemType.equals("TKT_GACHA")) {
/*  53 */       UserSyncData.getJSONObject("status").put("gachaTicket", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gachaTicket") + count));
/*     */     }
/*  55 */     if (itemType.equals("TKT_GACHA_10")) {
/*  56 */       UserSyncData.getJSONObject("status").put("tenGachaTicket", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("tenGachaTicket") + count));
/*     */     }
/*  58 */     if (itemType.equals("VOUCHER_PICK")) {
/*  59 */       if (!UserSyncData.getJSONObject("consumable").containsKey(itemId)) {
/*  60 */         JSONObject consumables = new JSONObject(true);
/*  61 */         JSONObject item = new JSONObject(true);
/*  62 */         item.put("ts", Integer.valueOf(-1));
/*  63 */         item.put("count", Integer.valueOf(0));
/*  64 */         consumables.put("0", item);
/*  65 */         UserSyncData.getJSONObject("consumable").put(itemId, consumables);
/*     */       } 
/*  67 */       UserSyncData.getJSONObject("consumable").getJSONObject(itemId).getJSONObject("0").put("count", Integer.valueOf(UserSyncData.getJSONObject("consumable").getJSONObject(itemId).getJSONObject("0").getIntValue("count") + count));
/*     */     } 
/*  69 */     if (itemType.equals("VOUCHER_ELITE_II_5")) {
/*  70 */       if (!UserSyncData.getJSONObject("consumable").containsKey(itemId)) {
/*  71 */         JSONObject consumables = new JSONObject(true);
/*  72 */         JSONObject item = new JSONObject(true);
/*  73 */         item.put("ts", Integer.valueOf(-1));
/*  74 */         item.put("count", Integer.valueOf(0));
/*  75 */         consumables.put("0", item);
/*  76 */         UserSyncData.getJSONObject("consumable").put(itemId, consumables);
/*     */       } 
/*  78 */       UserSyncData.getJSONObject("consumable").getJSONObject(itemId).getJSONObject("0").put("count", Integer.valueOf(UserSyncData.getJSONObject("consumable").getJSONObject(itemId).getJSONObject("0").getIntValue("count") + count));
/*     */     } 
/*  80 */     if (itemType.equals("VOUCHER_SKIN")) {
/*  81 */       if (!UserSyncData.getJSONObject("consumable").containsKey(itemId)) {
/*  82 */         JSONObject consumables = new JSONObject(true);
/*  83 */         JSONObject item = new JSONObject(true);
/*  84 */         item.put("ts", Integer.valueOf(-1));
/*  85 */         item.put("count", Integer.valueOf(0));
/*  86 */         consumables.put("0", item);
/*  87 */         UserSyncData.getJSONObject("consumable").put(itemId, consumables);
/*     */       } 
/*  89 */       UserSyncData.getJSONObject("consumable").getJSONObject(itemId).getJSONObject("0").put("count", Integer.valueOf(UserSyncData.getJSONObject("consumable").getJSONObject(itemId).getJSONObject("0").getIntValue("count") + count));
/*     */     } 
/*  91 */     if (itemType.equals("VOUCHER_CGACHA")) {
/*  92 */       if (!UserSyncData.getJSONObject("consumable").containsKey(itemId)) {
/*  93 */         JSONObject consumables = new JSONObject(true);
/*  94 */         JSONObject item = new JSONObject(true);
/*  95 */         item.put("ts", Integer.valueOf(-1));
/*  96 */         item.put("count", Integer.valueOf(0));
/*  97 */         consumables.put("0", item);
/*  98 */         UserSyncData.getJSONObject("consumable").put(itemId, consumables);
/*     */       } 
/* 100 */       UserSyncData.getJSONObject("consumable").getJSONObject(itemId).getJSONObject("0").put("count", Integer.valueOf(UserSyncData.getJSONObject("consumable").getJSONObject(itemId).getJSONObject("0").getIntValue("count") + count));
/*     */     } 
/* 102 */     if (itemType.equals("VOUCHER_MGACHA")) {
/* 103 */       if (!UserSyncData.getJSONObject("consumable").containsKey(itemId)) {
/* 104 */         JSONObject consumables = new JSONObject(true);
/* 105 */         JSONObject item = new JSONObject(true);
/* 106 */         item.put("ts", Integer.valueOf(-1));
/* 107 */         item.put("count", Integer.valueOf(0));
/* 108 */         consumables.put("0", item);
/* 109 */         UserSyncData.getJSONObject("consumable").put(itemId, consumables);
/*     */       } 
/* 111 */       UserSyncData.getJSONObject("consumable").getJSONObject(itemId).getJSONObject("0").put("count", Integer.valueOf(UserSyncData.getJSONObject("consumable").getJSONObject(itemId).getJSONObject("0").getIntValue("count") + count));
/*     */     } 
/* 113 */     if (itemType.equals("ACTIVITY_ITEM")) {
/* 114 */       if (!UserSyncData.getJSONObject("consumable").containsKey(itemId)) {
/* 115 */         JSONObject consumables = new JSONObject(true);
/* 116 */         JSONObject item = new JSONObject(true);
/* 117 */         item.put("ts", Integer.valueOf(-1));
/* 118 */         item.put("count", Integer.valueOf(0));
/* 119 */         consumables.put("0", item);
/* 120 */         UserSyncData.getJSONObject("consumable").put(itemId, consumables);
/*     */       } 
/* 122 */       UserSyncData.getJSONObject("consumable").getJSONObject(itemId).getJSONObject("0").put("count", Integer.valueOf(UserSyncData.getJSONObject("consumable").getJSONObject(itemId).getJSONObject("0").getIntValue("count") + count));
/*     */     } 
/* 124 */     if (itemType.equals("AP_SUPPLY")) {
/* 125 */       if (!UserSyncData.getJSONObject("consumable").containsKey(itemId)) {
/* 126 */         JSONObject consumables = new JSONObject(true);
/* 127 */         JSONObject item = new JSONObject(true);
/* 128 */         item.put("ts", Integer.valueOf(-1));
/* 129 */         item.put("count", Integer.valueOf(0));
/* 130 */         consumables.put("0", item);
/* 131 */         UserSyncData.getJSONObject("consumable").put(itemId, consumables);
/*     */       } 
/* 133 */       UserSyncData.getJSONObject("consumable").getJSONObject(itemId).getJSONObject("0").put("count", Integer.valueOf(UserSyncData.getJSONObject("consumable").getJSONObject(itemId).getJSONObject("0").getIntValue("count") + count));
/*     */     } 
/* 135 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 137 */     return Boolean.valueOf(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static JSONObject GM_CharGet(Long uid, String charId) {
/* 143 */     List<Account> Accounts = userDao.queryAccountByUid(uid.longValue());
/*     */     
/* 145 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 147 */     JSONObject chars = UserSyncData.getJSONObject("troop").getJSONObject("chars");
/* 148 */     JSONObject buildingChars = UserSyncData.getJSONObject("building").getJSONObject("chars");
/* 149 */     JSONObject voiceLangDict = ArknightsApplication.charwordTable.getJSONObject("voiceLangDict");
/*     */     
/* 151 */     int repeatCharId = 0;
/*     */     
/* 153 */     for (int i = 0; i < UserSyncData.getJSONObject("troop").getJSONObject("chars").size(); i++) {
/* 154 */       if (UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(i + 1)).getString("charId").equals(charId)) {
/* 155 */         repeatCharId = i + 1;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 160 */     JSONArray itemGet = new JSONArray();
/* 161 */     int isNew = 0;
/* 162 */     int charinstId = repeatCharId;
/* 163 */     if (repeatCharId == 0) {
/*     */ 
/*     */       
/* 166 */       JSONObject char_data = new JSONObject(true);
/*     */       
/* 168 */       JSONArray skilsArray = ArknightsApplication.characterJson.getJSONObject(charId).getJSONArray("skills");
/* 169 */       JSONArray skils = new JSONArray();
/*     */       
/* 171 */       for (int j = 0; j < skilsArray.size(); j++) {
/* 172 */         JSONObject new_skils = new JSONObject(true);
/* 173 */         new_skils.put("skillId", skilsArray.getJSONObject(j).getString("skillId"));
/* 174 */         new_skils.put("state", Integer.valueOf(0));
/* 175 */         new_skils.put("specializeLevel", Integer.valueOf(0));
/* 176 */         new_skils.put("completeUpgradeTime", Integer.valueOf(-1));
/* 177 */         if (skilsArray.getJSONObject(j).getJSONObject("unlockCond").getIntValue("phase") == 0) {
/* 178 */           new_skils.put("unlock", Integer.valueOf(1));
/*     */         } else {
/* 180 */           new_skils.put("unlock", Integer.valueOf(0));
/*     */         } 
/* 182 */         skils.add(new_skils);
/*     */       } 
/*     */       
/* 185 */       int instId = UserSyncData.getJSONObject("troop").getJSONObject("chars").size() + 1;
/* 186 */       charinstId = instId;
/* 187 */       char_data.put("instId", Integer.valueOf(instId));
/* 188 */       char_data.put("charId", charId);
/* 189 */       char_data.put("favorPoint", Integer.valueOf(0));
/* 190 */       char_data.put("potentialRank", Integer.valueOf(0));
/* 191 */       char_data.put("mainSkillLvl", Integer.valueOf(1));
/* 192 */       char_data.put("skin", charId + "#1");
/* 193 */       char_data.put("level", Integer.valueOf(1));
/* 194 */       char_data.put("exp", Integer.valueOf(0));
/* 195 */       char_data.put("evolvePhase", Integer.valueOf(0));
/* 196 */       char_data.put("gainTime", Long.valueOf((new Date()).getTime() / 1000L));
/* 197 */       char_data.put("skills", skils);
/* 198 */       char_data.put("equip", new JSONObject(true));
/*     */       
/* 200 */       char_data.put("voiceLan", ArknightsApplication.charwordTable.getJSONObject("charDefaultTypeDict").getString(charId));
/*     */       
/* 202 */       if (skils == new JSONArray()) {
/* 203 */         char_data.put("defaultSkillIndex", Integer.valueOf(-1));
/*     */       } else {
/* 205 */         char_data.put("defaultSkillIndex", Integer.valueOf(0));
/*     */       } 
/*     */       
/* 208 */       String sub1 = charId.substring(charId.indexOf("_") + 1);
/* 209 */       String charName = sub1.substring(sub1.indexOf("_") + 1);
/*     */       
/* 211 */       if (ArknightsApplication.uniequipTable.containsKey("uniequip_001_" + charName)) {
/* 212 */         JSONObject equip = new JSONObject(true);
/* 213 */         JSONObject uniequip_001 = new JSONObject(true);
/* 214 */         uniequip_001.put("hide", Integer.valueOf(0));
/* 215 */         uniequip_001.put("locked", Integer.valueOf(0));
/* 216 */         uniequip_001.put("level", Integer.valueOf(1));
/* 217 */         JSONObject uniequip_002 = new JSONObject(true);
/* 218 */         uniequip_002.put("hide", Integer.valueOf(0));
/* 219 */         uniequip_002.put("locked", Integer.valueOf(0));
/* 220 */         uniequip_002.put("level", Integer.valueOf(1));
/* 221 */         equip.put("uniequip_001_" + charName, uniequip_001);
/* 222 */         equip.put("uniequip_002_" + charName, uniequip_002);
/* 223 */         char_data.put("equip", equip);
/* 224 */         char_data.put("currentEquip", "uniequip_001_" + charName);
/*     */       } else {
/* 226 */         char_data.put("currentEquip", null);
/*     */       } 
/*     */       
/* 229 */       UserSyncData.getJSONObject("troop").getJSONObject("chars").put(String.valueOf(instId), char_data);
/*     */       
/* 231 */       JSONObject charGroup = new JSONObject(true);
/* 232 */       charGroup.put("favorPoint", Integer.valueOf(0));
/* 233 */       UserSyncData.getJSONObject("troop").getJSONObject("charGroup").put(charId, charGroup);
/*     */       
/* 235 */       JSONObject buildingChar = new JSONObject(true);
/* 236 */       buildingChar.put("charId", charId);
/* 237 */       buildingChar.put("lastApAddTime", Long.valueOf((new Date()).getTime() / 1000L));
/* 238 */       buildingChar.put("ap", Integer.valueOf(8640000));
/* 239 */       buildingChar.put("roomSlotId", "");
/* 240 */       buildingChar.put("index", Integer.valueOf(-1));
/* 241 */       buildingChar.put("changeScale", Integer.valueOf(0));
/* 242 */       JSONObject bubble = new JSONObject(true);
/* 243 */       JSONObject normal = new JSONObject(true);
/* 244 */       normal.put("add", Integer.valueOf(-1));
/* 245 */       normal.put("ts", Integer.valueOf(0));
/* 246 */       bubble.put("normal", normal);
/* 247 */       JSONObject assist = new JSONObject(true);
/* 248 */       assist.put("add", Integer.valueOf(-1));
/* 249 */       assist.put("ts", Integer.valueOf(-1));
/* 250 */       bubble.put("assist", assist);
/* 251 */       buildingChar.put("bubble", bubble);
/* 252 */       buildingChar.put("workTime", Integer.valueOf(0));
/*     */       
/* 254 */       buildingChars.put(String.valueOf(instId), buildingChar);
/* 255 */       chars.put(String.valueOf(instId), char_data);
/*     */       
/* 257 */       JSONObject SHD = new JSONObject(true);
/* 258 */       SHD.put("type", "HGG_SHD");
/* 259 */       SHD.put("id", "4004");
/* 260 */       SHD.put("count", Integer.valueOf(1));
/* 261 */       itemGet.add(SHD);
/*     */       
/* 263 */       isNew = 1;
/*     */       
/* 265 */       UserSyncData.getJSONObject("status").put("hggShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("hggShard") + 1));
/*     */     }
/*     */     else {
/*     */       
/* 269 */       JSONObject repatChar = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(repeatCharId));
/* 270 */       int potentialRank = repatChar.getIntValue("potentialRank");
/* 271 */       int rarity = ArknightsApplication.characterJson.getJSONObject(charId).getIntValue("rarity");
/*     */       
/* 273 */       String itemName = null;
/* 274 */       String itemType = null;
/* 275 */       String itemId = null;
/*     */       
/* 277 */       int itemCount = 0;
/* 278 */       if (rarity == 0) {
/* 279 */         itemName = "lggShard";
/* 280 */         itemType = "LGG_SHD";
/* 281 */         itemId = "4005";
/* 282 */         itemCount = 1;
/*     */       } 
/* 284 */       if (rarity == 1) {
/* 285 */         itemName = "lggShard";
/* 286 */         itemType = "LGG_SHD";
/* 287 */         itemId = "4005";
/* 288 */         itemCount = 1;
/*     */       } 
/* 290 */       if (rarity == 2) {
/* 291 */         itemName = "lggShard";
/* 292 */         itemType = "LGG_SHD";
/* 293 */         itemId = "4005";
/* 294 */         itemCount = 5;
/*     */       } 
/* 296 */       if (rarity == 3) {
/* 297 */         itemName = "lggShard";
/* 298 */         itemType = "LGG_SHD";
/* 299 */         itemId = "4005";
/* 300 */         itemCount = 30;
/*     */       } 
/* 302 */       if (rarity == 4) {
/* 303 */         itemName = "hggShard";
/* 304 */         itemType = "HGG_SHD";
/* 305 */         itemId = "4004";
/* 306 */         if (potentialRank != 5) {
/* 307 */           itemCount = 5;
/*     */         } else {
/* 309 */           itemCount = 8;
/*     */         } 
/*     */       } 
/* 312 */       if (rarity == 5) {
/* 313 */         itemName = "hggShard";
/* 314 */         itemType = "HGG_SHD";
/* 315 */         itemId = "4004";
/* 316 */         if (potentialRank != 5) {
/* 317 */           itemCount = 10;
/*     */         } else {
/* 319 */           itemCount = 15;
/*     */         } 
/*     */       } 
/*     */       
/* 323 */       JSONObject SHD = new JSONObject(true);
/* 324 */       SHD.put("type", itemType);
/* 325 */       SHD.put("id", itemId);
/* 326 */       SHD.put("count", Integer.valueOf(itemCount));
/* 327 */       itemGet.add(SHD);
/*     */       
/* 329 */       JSONObject potential = new JSONObject(true);
/* 330 */       potential.put("type", "MATERIAL");
/* 331 */       potential.put("id", "p_" + charId);
/* 332 */       potential.put("count", Integer.valueOf(1));
/* 333 */       itemGet.add(potential);
/*     */       
/* 335 */       UserSyncData.getJSONObject("status").put(itemName, Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue(itemName) + itemCount));
/*     */       
/* 337 */       UserSyncData.getJSONObject("inventory").put("p_" + charId, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("p_" + charId) + 1));
/*     */       
/* 339 */       chars.put(String.valueOf(repeatCharId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(repeatCharId)));
/*     */     } 
/* 341 */     UserSyncData.getJSONObject("troop").put("chars", chars);
/*     */     
/* 343 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 345 */     JSONObject result = new JSONObject(true);
/* 346 */     result.put("itemGet", itemGet);
/* 347 */     result.put("charId", charId);
/* 348 */     result.put("charInstId", Integer.valueOf(charinstId));
/* 349 */     result.put("isNew", Integer.valueOf(isNew));
/* 350 */     return result;
/*     */   }
/*     */   
/*     */   public static void GM_FuncGet() {}
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\core\function\get.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */