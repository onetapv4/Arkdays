/*      */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*      */ 
/*      */ import com.alibaba.fastjson.JSONArray;
/*      */ import com.alibaba.fastjson.JSONObject;
/*      */ import com.hypergryph.arknights.ArknightsApplication;
/*      */ import com.hypergryph.arknights.core.dao.userDao;
/*      */ import com.hypergryph.arknights.core.decrypt.Utils;
/*      */ import com.hypergryph.arknights.core.pojo.Account;
/*      */ import java.text.DecimalFormat;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.Map;
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
/*      */ @RestController
/*      */ @RequestMapping({"/charBuild"})
/*      */ public class charBuild
/*      */ {
/*      */   @PostMapping(value = {"/upgradeChar"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject UpgradeChar(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*   31 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*   32 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /charBuild/upgradeChar");
/*      */     
/*   34 */     int charInstId = JsonBody.getIntValue("charInstId");
/*   35 */     JSONArray expMats = JsonBody.getJSONArray("expMats");
/*      */ 
/*      */     
/*   38 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*   39 */     if (Accounts.size() != 1) {
/*   40 */       JSONObject jSONObject = new JSONObject(true);
/*   41 */       jSONObject.put("result", Integer.valueOf(2));
/*   42 */       jSONObject.put("error", "无法查询到此账户");
/*   43 */       return jSONObject;
/*      */     } 
/*      */     
/*   46 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*   48 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*   49 */       response.setStatus(500);
/*   50 */       JSONObject jSONObject = new JSONObject(true);
/*   51 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*   52 */       jSONObject.put("error", "Bad Request");
/*   53 */       jSONObject.put("message", "error");
/*   54 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*   58 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*   60 */     JSONObject chars = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId));
/*      */     
/*   62 */     String charid = chars.getString("charId");
/*   63 */     int evolvePhase = chars.getIntValue("evolvePhase");
/*   64 */     int level = chars.getIntValue("level");
/*   65 */     int exp = chars.getIntValue("exp");
/*      */     
/*   67 */     JSONObject inventory = new JSONObject(true);
/*   68 */     int AddExp = 0;
/*   69 */     for (int s = 0; s < expMats.size(); s++) {
/*   70 */       String itemid = expMats.getJSONObject(s).getString("id");
/*   71 */       int count = expMats.getJSONObject(s).getInteger("count").intValue();
/*   72 */       if (itemid.equals("2001")) {
/*   73 */         AddExp += 200 * count;
/*   74 */       } else if (itemid.equals("2002")) {
/*   75 */         AddExp += 400 * count;
/*   76 */       } else if (itemid.equals("2003")) {
/*   77 */         AddExp += 1000 * count;
/*   78 */       } else if (itemid.equals("2004")) {
/*   79 */         AddExp += 2000 * count;
/*      */       } 
/*      */       
/*   82 */       UserSyncData.getJSONObject("inventory").put(itemid, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(itemid) - count));
/*   83 */       inventory.put(itemid, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(itemid)));
/*      */     } 
/*      */     
/*   86 */     int rarity = ArknightsApplication.characterJson.getJSONObject(charid).getIntValue("rarity");
/*   87 */     String evolve_0 = "[0,100,117,134,151,168,185,202,219,236,253,270,287,304,321,338,355,372,389,406,423,440,457,474,491,508,525,542,559,574,589,605,621,637,653,669,685,701,716,724,739,749,759,770,783,804,820,836,852,888,-1]";
/*   88 */     String e_0_cost = "[0,30,36,43,50,57,65,73,81,90,99,108,118,128,138,149,160,182,206,231,258,286,315,346,378,411,446,482,520,557,595,635,677,720,764,809,856,904,952,992,1042,1086,1131,1178,1229,1294,1353,1413,1474,1572,-1]";
/*   89 */     String evolve_1 = "[0,120,172,224,276,328,380,432,484,536,588,640,692,744,796,848,900,952,1004,1056,1108,1160,1212,1264,1316,1368,1420,1472,1524,1576,1628,1706,1784,1862,1940,2018,2096,2174,2252,2330,2408,2584,2760,2936,3112,3288,3464,3640,3816,3992,4168,4344,4520,4696,4890,5326,6019,6312,6505,6838,7391,7657,7823,8089,8355,8621,8887,9153,9419,9605,9951,10448,10945,11442,11939,12436,12933,13430,13927,14549,-1]";
/*   90 */     String e_1_cost = "[0,48,71,95,120,146,173,201,231,262,293,326,361,396,432,470,508,548,589,631,675,719,765,811,859,908,958,1010,1062,1116,1171,1245,1322,1400,1480,1562,1645,1731,1817,1906,1996,2171,2349,2531,2717,2907,3100,3298,3499,3705,3914,4127,4344,4565,4807,5294,6049,6413,6681,7098,7753,8116,8378,8752,9132,9518,9909,10306,10709,11027,11533,12224,12926,13639,14363,15097,15843,16599,17367,18303,-1]";
/*   91 */     String evolve_2 = "[0,191,303,415,527,639,751,863,975,1087,1199,1311,1423,1535,1647,1759,1871,1983,2095,2207,2319,2431,2543,2655,2767,2879,2991,3103,3215,3327,3439,3602,3765,3928,4091,4254,4417,4580,4743,4906,5069,5232,5395,5558,5721,5884,6047,6210,6373,6536,6699,6902,7105,7308,7511,7714,7917,8120,8323,8526,8729,9163,9597,10031,10465,10899,11333,11767,12201,12729,13069,13747,14425,15103,15781,16459,17137,17815,18493,19171,19849,21105,22361,23617,24873,26129,27385,28641,29897,31143,-1]";
/*   92 */     String e_2_cost = "[0,76,124,173,225,279,334,392,451,513,577,642,710,780,851,925,1001,1079,1159,1240,1324,1410,1498,1588,1680,1773,1869,1967,2067,2169,2273,2413,2556,2702,2851,3003,3158,3316,3477,3640,3807,3976,4149,4324,4502,4684,4868,5055,5245,5438,5634,5867,6103,6343,6587,6835,7086,7340,7599,7861,8127,8613,9108,9610,10120,10637,11163,11696,12238,12882,13343,14159,14988,15828,16681,17545,18422,19311,20213,21126,22092,23722,25380,27065,28778,30519,32287,34083,35906,37745,-1]";
/*   93 */     JSONArray characterExpMap = new JSONArray();
/*   94 */     JSONArray characterUpgradeCostMap = new JSONArray();
/*   95 */     int List = 0;
/*      */     
/*   97 */     int lv = 0;
/*   98 */     int ep = 0;
/*   99 */     if (evolvePhase == 0) {
/*  100 */       characterExpMap = JSONArray.parseArray(evolve_0);
/*  101 */       characterUpgradeCostMap = JSONArray.parseArray(e_0_cost);
/*  102 */     } else if (evolvePhase == 1) {
/*  103 */       characterExpMap = JSONArray.parseArray(evolve_1);
/*  104 */       characterUpgradeCostMap = JSONArray.parseArray(e_1_cost);
/*  105 */     } else if (evolvePhase == 2) {
/*  106 */       characterExpMap = JSONArray.parseArray(evolve_2);
/*  107 */       characterUpgradeCostMap = JSONArray.parseArray(e_2_cost);
/*      */     } 
/*  109 */     if (rarity == 5) {
/*  110 */       int levelexp = 0;
/*  111 */       for (int k = 0; k < characterExpMap.size(); k++) {
/*  112 */         List += characterExpMap.getIntValue(k);
/*  113 */         if (level == k + 1) {
/*  114 */           levelexp = List;
/*      */           break;
/*      */         } 
/*      */       } 
/*  118 */       List = 0;
/*  119 */       int AddedExp = levelexp + exp + AddExp;
/*  120 */       for (int m = 0; m < characterExpMap.size(); m++) {
/*  121 */         List += characterExpMap.getIntValue(m);
/*  122 */         if (AddedExp < List) {
/*  123 */           lv = m;
/*  124 */           ep = characterExpMap.getIntValue(m) - List - AddedExp;
/*  125 */           chars.put("level", Integer.valueOf(lv));
/*  126 */           chars.put("exp", Integer.valueOf(ep)); break;
/*      */         } 
/*  128 */         if (evolvePhase == 0) {
/*  129 */           if (AddedExp >= 24400) {
/*  130 */             lv = 50;
/*  131 */             ep = 0;
/*  132 */             chars.put("level", Integer.valueOf(lv));
/*  133 */             chars.put("exp", Integer.valueOf(ep));
/*      */             break;
/*      */           } 
/*  136 */         } else if (evolvePhase == 1) {
/*  137 */           if (AddedExp >= 337000) {
/*  138 */             lv = 80;
/*  139 */             ep = 0;
/*  140 */             chars.put("level", Integer.valueOf(lv));
/*  141 */             chars.put("exp", Integer.valueOf(ep));
/*      */             break;
/*      */           } 
/*  144 */         } else if (evolvePhase == 2 && 
/*  145 */           AddedExp >= 750000) {
/*  146 */           lv = 90;
/*  147 */           ep = 0;
/*  148 */           chars.put("level", Integer.valueOf(lv));
/*  149 */           chars.put("exp", Integer.valueOf(ep));
/*      */ 
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  155 */     } else if (rarity == 4) {
/*  156 */       int levelexp = 0;
/*  157 */       for (int k = 0; k < characterExpMap.size(); k++) {
/*  158 */         List += characterExpMap.getIntValue(k);
/*  159 */         if (level == k + 1) {
/*  160 */           levelexp = List;
/*      */           break;
/*      */         } 
/*      */       } 
/*  164 */       List = 0;
/*  165 */       int AddedExp = levelexp + exp + AddExp;
/*  166 */       for (int m = 0; m < characterExpMap.size(); m++) {
/*  167 */         List += characterExpMap.getIntValue(m);
/*  168 */         if (AddedExp < List) {
/*  169 */           lv = m;
/*  170 */           ep = characterExpMap.getIntValue(m) - List - AddedExp;
/*  171 */           chars.put("level", Integer.valueOf(lv));
/*  172 */           chars.put("exp", Integer.valueOf(ep)); break;
/*      */         } 
/*  174 */         if (evolvePhase == 0) {
/*  175 */           if (AddedExp >= 24400) {
/*  176 */             lv = 50;
/*  177 */             ep = 0;
/*  178 */             chars.put("level", Integer.valueOf(lv));
/*  179 */             chars.put("exp", Integer.valueOf(ep));
/*      */             break;
/*      */           } 
/*  182 */         } else if (evolvePhase == 1) {
/*  183 */           if (AddedExp >= 215000) {
/*  184 */             lv = 70;
/*  185 */             ep = 0;
/*  186 */             chars.put("level", Integer.valueOf(lv));
/*  187 */             chars.put("exp", Integer.valueOf(ep));
/*      */             break;
/*      */           } 
/*  190 */         } else if (evolvePhase == 2 && 
/*  191 */           AddedExp >= 495000) {
/*  192 */           lv = 80;
/*  193 */           ep = 0;
/*  194 */           chars.put("level", Integer.valueOf(lv));
/*  195 */           chars.put("exp", Integer.valueOf(ep));
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  200 */     } else if (rarity == 3) {
/*  201 */       int levelexp = 0;
/*  202 */       for (int k = 0; k < characterExpMap.size(); k++) {
/*  203 */         List += characterExpMap.getIntValue(k);
/*  204 */         if (level == k + 1) {
/*  205 */           levelexp = List;
/*      */           break;
/*      */         } 
/*      */       } 
/*  209 */       List = 0;
/*  210 */       int AddedExp = levelexp + exp + AddExp;
/*  211 */       for (int m = 0; m < characterExpMap.size(); m++) {
/*  212 */         List += characterExpMap.getIntValue(m);
/*  213 */         if (AddedExp < List) {
/*  214 */           lv = m;
/*  215 */           ep = characterExpMap.getIntValue(m) - List - AddedExp;
/*  216 */           chars.put("level", Integer.valueOf(lv));
/*  217 */           chars.put("exp", Integer.valueOf(ep)); break;
/*      */         } 
/*  219 */         if (evolvePhase == 0) {
/*  220 */           if (AddedExp >= 20200) {
/*  221 */             lv = 45;
/*  222 */             ep = 0;
/*  223 */             chars.put("level", Integer.valueOf(lv));
/*  224 */             chars.put("exp", Integer.valueOf(ep));
/*      */             break;
/*      */           } 
/*  227 */         } else if (evolvePhase == 1) {
/*  228 */           if (AddedExp >= 130000) {
/*  229 */             lv = 60;
/*  230 */             ep = 0;
/*  231 */             chars.put("level", Integer.valueOf(lv));
/*  232 */             chars.put("exp", Integer.valueOf(ep));
/*      */             break;
/*      */           } 
/*  235 */         } else if (evolvePhase == 2 && 
/*  236 */           AddedExp >= 333800) {
/*  237 */           lv = 70;
/*  238 */           ep = 0;
/*  239 */           chars.put("level", Integer.valueOf(lv));
/*  240 */           chars.put("exp", Integer.valueOf(ep));
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  245 */     } else if (rarity == 2) {
/*  246 */       int levelexp = 0;
/*  247 */       for (int k = 0; k < characterExpMap.size(); k++) {
/*  248 */         List += characterExpMap.getIntValue(k);
/*  249 */         if (level == k + 1) {
/*  250 */           levelexp = List;
/*      */           break;
/*      */         } 
/*      */       } 
/*  254 */       List = 0;
/*  255 */       int AddedExp = levelexp + exp + AddExp;
/*  256 */       for (int m = 0; m < characterExpMap.size(); m++) {
/*  257 */         List += characterExpMap.getIntValue(m);
/*  258 */         if (AddedExp < List) {
/*  259 */           lv = m;
/*  260 */           ep = characterExpMap.getIntValue(m) - List - AddedExp;
/*  261 */           chars.put("level", Integer.valueOf(lv));
/*  262 */           chars.put("exp", Integer.valueOf(ep)); break;
/*      */         } 
/*  264 */         if (evolvePhase == 0) {
/*  265 */           if (AddedExp >= 16400) {
/*  266 */             lv = 40;
/*  267 */             ep = 0;
/*  268 */             chars.put("level", Integer.valueOf(lv));
/*  269 */             chars.put("exp", Integer.valueOf(ep));
/*      */             break;
/*      */           } 
/*  272 */         } else if (evolvePhase == 1 && 
/*  273 */           AddedExp >= 99000) {
/*  274 */           lv = 55;
/*  275 */           ep = 0;
/*  276 */           chars.put("level", Integer.valueOf(lv));
/*  277 */           chars.put("exp", Integer.valueOf(ep));
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  282 */     } else if (rarity == 1) {
/*  283 */       int levelexp = 0;
/*  284 */       for (int k = 0; k < characterExpMap.size(); k++) {
/*  285 */         List += characterExpMap.getIntValue(k);
/*  286 */         if (level == k + 1) {
/*  287 */           levelexp = List;
/*      */           break;
/*      */         } 
/*      */       } 
/*  291 */       List = 0;
/*  292 */       int AddedExp = levelexp + exp + AddExp;
/*  293 */       for (int m = 0; m < characterExpMap.size(); m++) {
/*  294 */         List += characterExpMap.getIntValue(m);
/*  295 */         if (AddedExp < List) {
/*  296 */           lv = m;
/*  297 */           ep = characterExpMap.getIntValue(m) - List - AddedExp;
/*  298 */           chars.put("level", Integer.valueOf(lv));
/*  299 */           chars.put("exp", Integer.valueOf(ep)); break;
/*      */         } 
/*  301 */         if (evolvePhase == 0 && 
/*  302 */           AddedExp >= 9800) {
/*  303 */           lv = 30;
/*  304 */           ep = 0;
/*  305 */           chars.put("level", Integer.valueOf(lv));
/*  306 */           chars.put("exp", Integer.valueOf(ep));
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  311 */     } else if (rarity == 0) {
/*  312 */       int levelexp = 0;
/*  313 */       for (int k = 0; k < characterExpMap.size(); k++) {
/*  314 */         List += characterExpMap.getIntValue(k);
/*  315 */         if (level == k + 1) {
/*  316 */           levelexp = List;
/*      */           break;
/*      */         } 
/*      */       } 
/*  320 */       List = 0;
/*  321 */       int AddedExp = levelexp + exp + AddExp;
/*  322 */       for (int m = 0; m < characterExpMap.size(); m++) {
/*  323 */         List += characterExpMap.getIntValue(m);
/*  324 */         if (AddedExp < List) {
/*  325 */           lv = m;
/*  326 */           ep = characterExpMap.getIntValue(m) - List - AddedExp;
/*  327 */           chars.put("level", Integer.valueOf(lv));
/*  328 */           chars.put("exp", Integer.valueOf(ep)); break;
/*      */         } 
/*  330 */         if (evolvePhase == 0 && 
/*  331 */           AddedExp >= 9800) {
/*  332 */           lv = 30;
/*  333 */           ep = 0;
/*  334 */           chars.put("level", Integer.valueOf(lv));
/*  335 */           chars.put("exp", Integer.valueOf(ep));
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  342 */     int baseCost = 0;
/*  343 */     int expCost = Math.round(Float.parseFloat((new DecimalFormat("0.00")).format((characterUpgradeCostMap.getIntValue(level) * exp / characterExpMap.getIntValue(level)))));
/*      */     
/*  345 */     for (int i = 0; i < level; i++) {
/*  346 */       baseCost += characterUpgradeCostMap.getIntValue(i);
/*      */     }
/*      */     
/*  349 */     int addBaseCost = 0;
/*  350 */     int addExpCost = Math.round(Float.parseFloat((new DecimalFormat("0.00")).format((characterUpgradeCostMap.getIntValue(chars.getIntValue("level")) * chars.getIntValue("exp") / characterExpMap.getIntValue(chars.getIntValue("level"))))));
/*      */     
/*  352 */     for (int j = 0; j < chars.getIntValue("level"); j++) {
/*  353 */       addBaseCost += characterUpgradeCostMap.getIntValue(j);
/*      */     }
/*      */     
/*  356 */     int UpgradeCost = addBaseCost + addExpCost - baseCost + expCost;
/*      */     
/*  358 */     UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") - UpgradeCost));
/*      */     
/*  360 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  362 */     JSONObject result = new JSONObject(true);
/*  363 */     JSONObject modified = new JSONObject(true);
/*  364 */     JSONObject playerDataDelta = new JSONObject(true);
/*  365 */     JSONObject troop = new JSONObject(true);
/*  366 */     JSONObject charss = new JSONObject(true);
/*  367 */     JSONObject chars_id = new JSONObject(true);
/*  368 */     JSONObject status = new JSONObject(true);
/*  369 */     status.put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold")));
/*  370 */     chars_id.put("exp", Integer.valueOf(ep));
/*  371 */     chars_id.put("level", Integer.valueOf(lv));
/*  372 */     chars_id.put("favorPoint", Integer.valueOf(chars.getIntValue("favorPoint")));
/*  373 */     charss.put(String.valueOf(charInstId), chars_id);
/*  374 */     troop.put("chars", charss);
/*  375 */     modified.put("troop", troop);
/*  376 */     modified.put("status", status);
/*  377 */     modified.put("inventory", inventory);
/*  378 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  379 */     playerDataDelta.put("modified", modified);
/*  380 */     result.put("playerDataDelta", playerDataDelta);
/*  381 */     result.put("result", Integer.valueOf(0));
/*  382 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/upgradeSkill"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject UpgradeSkill(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  388 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  389 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /charBuild/upgradeSkill");
/*      */     
/*  391 */     int charInstId = JsonBody.getIntValue("charInstId");
/*  392 */     int targetLevel = JsonBody.getIntValue("targetLevel");
/*      */ 
/*      */     
/*  395 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  396 */     if (Accounts.size() != 1) {
/*  397 */       JSONObject jSONObject = new JSONObject(true);
/*  398 */       jSONObject.put("result", Integer.valueOf(2));
/*  399 */       jSONObject.put("error", "无法查询到此账户");
/*  400 */       return jSONObject;
/*      */     } 
/*      */     
/*  403 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  405 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  406 */       response.setStatus(500);
/*  407 */       JSONObject jSONObject = new JSONObject(true);
/*  408 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  409 */       jSONObject.put("error", "Bad Request");
/*  410 */       jSONObject.put("message", "error");
/*  411 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  416 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  418 */     JSONObject inventory = new JSONObject(true);
/*  419 */     JSONObject chars = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId));
/*  420 */     String charid = chars.getString("charId");
/*  421 */     JSONArray allSkillLvlup = ArknightsApplication.characterJson.getJSONObject(String.valueOf(charid)).getJSONArray("allSkillLvlup");
/*  422 */     for (int i = 0; i < allSkillLvlup.size(); i++) {
/*  423 */       if (targetLevel - 2 == i) {
/*  424 */         JSONArray lvlUpCost = allSkillLvlup.getJSONObject(i).getJSONArray("lvlUpCost");
/*  425 */         for (int l = 0; l < lvlUpCost.size(); l++) {
/*  426 */           String itemid = lvlUpCost.getJSONObject(l).getString("id");
/*  427 */           int count = lvlUpCost.getJSONObject(l).getIntValue("count");
/*  428 */           UserSyncData.getJSONObject("inventory").put(itemid, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(itemid) - count));
/*  429 */           inventory.put(itemid, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(itemid)));
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  434 */     UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)).put("mainSkillLvl", Integer.valueOf(targetLevel));
/*      */     
/*  436 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  438 */     JSONObject result = new JSONObject(true);
/*  439 */     JSONObject modified = new JSONObject(true);
/*  440 */     JSONObject playerDataDelta = new JSONObject(true);
/*  441 */     JSONObject troop = new JSONObject(true);
/*  442 */     JSONObject charss = new JSONObject(true);
/*  443 */     JSONObject chars_id = new JSONObject(true);
/*  444 */     chars_id.put("mainSkillLvl", Integer.valueOf(targetLevel));
/*  445 */     charss.put(String.valueOf(charInstId), chars_id);
/*  446 */     troop.put("chars", charss);
/*  447 */     modified.put("troop", troop);
/*  448 */     modified.put("inventory", inventory);
/*  449 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  450 */     playerDataDelta.put("modified", modified);
/*  451 */     result.put("playerDataDelta", playerDataDelta);
/*  452 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/setDefaultSkill"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject SetDefaultSkill(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  458 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  459 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /charBuild/setDefaultSkill");
/*      */     
/*  461 */     int charInstId = JsonBody.getIntValue("charInstId");
/*  462 */     int defaultSkillIndex = JsonBody.getInteger("defaultSkillIndex").intValue();
/*      */ 
/*      */     
/*  465 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  466 */     if (Accounts.size() != 1) {
/*  467 */       JSONObject jSONObject = new JSONObject(true);
/*  468 */       jSONObject.put("result", Integer.valueOf(2));
/*  469 */       jSONObject.put("error", "无法查询到此账户");
/*  470 */       return jSONObject;
/*      */     } 
/*      */     
/*  473 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  475 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  476 */       response.setStatus(500);
/*  477 */       JSONObject jSONObject = new JSONObject(true);
/*  478 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  479 */       jSONObject.put("error", "Bad Request");
/*  480 */       jSONObject.put("message", "error");
/*  481 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  485 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  487 */     UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)).put("defaultSkillIndex", Integer.valueOf(defaultSkillIndex));
/*      */     
/*  489 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  491 */     JSONObject result = new JSONObject(true);
/*  492 */     JSONObject playerDataDelta = new JSONObject(true);
/*  493 */     JSONObject modified = new JSONObject(true);
/*  494 */     JSONObject troop = new JSONObject(true);
/*  495 */     JSONObject chars = new JSONObject(true);
/*  496 */     chars.put(String.valueOf(charInstId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)));
/*  497 */     troop.put("chars", chars);
/*  498 */     modified.put("troop", troop);
/*  499 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  500 */     playerDataDelta.put("modified", modified);
/*  501 */     result.put("playerDataDelta", playerDataDelta);
/*  502 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/boostPotential"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject BoostPotential(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  509 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  510 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /charBuild/boostPotential");
/*      */     
/*  512 */     int charInstId = JsonBody.getIntValue("charInstId");
/*  513 */     int targetRank = JsonBody.getInteger("targetRank").intValue();
/*  514 */     String itemId = JsonBody.getString("itemId");
/*      */ 
/*      */     
/*  517 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  518 */     if (Accounts.size() != 1) {
/*  519 */       JSONObject jSONObject = new JSONObject(true);
/*  520 */       jSONObject.put("result", Integer.valueOf(2));
/*  521 */       jSONObject.put("error", "无法查询到此账户");
/*  522 */       return jSONObject;
/*      */     } 
/*      */     
/*  525 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  527 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  528 */       response.setStatus(500);
/*  529 */       JSONObject jSONObject = new JSONObject(true);
/*  530 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  531 */       jSONObject.put("error", "Bad Request");
/*  532 */       jSONObject.put("message", "error");
/*  533 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  537 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  539 */     if (itemId.indexOf("_char_") != -1) {
/*      */       
/*  541 */       UserSyncData.getJSONObject("inventory").put(itemId, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(itemId) - 1));
/*      */     } else {
/*      */       
/*  544 */       UserSyncData.getJSONObject("inventory").put(itemId, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(itemId) - 4));
/*      */     } 
/*      */     
/*  547 */     UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)).put("potentialRank", Integer.valueOf(targetRank));
/*      */ 
/*      */     
/*  550 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  552 */     JSONObject result = new JSONObject(true);
/*  553 */     JSONObject modified = new JSONObject(true);
/*  554 */     JSONObject playerDataDelta = new JSONObject(true);
/*  555 */     JSONObject inventory = new JSONObject(true);
/*  556 */     JSONObject troop = new JSONObject(true);
/*  557 */     JSONObject chars = new JSONObject(true);
/*  558 */     JSONObject charid = new JSONObject(true);
/*  559 */     charid.put("potentialRank", Integer.valueOf(targetRank));
/*  560 */     chars.put(String.valueOf(charInstId), charid);
/*  561 */     troop.put("chars", chars);
/*  562 */     inventory.put(itemId, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(itemId)));
/*  563 */     modified.put("inventory", inventory);
/*  564 */     modified.put("troop", troop);
/*  565 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  566 */     playerDataDelta.put("modified", modified);
/*  567 */     result.put("playerDataDelta", playerDataDelta);
/*  568 */     result.put("result", Integer.valueOf(1));
/*  569 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/evolveChar"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject EvolveChar(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  576 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  577 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /charBuild/evolveChar");
/*      */     
/*  579 */     int charInstId = JsonBody.getIntValue("charInstId");
/*  580 */     int destEvolvePhase = JsonBody.getIntValue("destEvolvePhase");
/*      */ 
/*      */     
/*  583 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  584 */     if (Accounts.size() != 1) {
/*  585 */       JSONObject jSONObject = new JSONObject(true);
/*  586 */       jSONObject.put("result", Integer.valueOf(2));
/*  587 */       jSONObject.put("error", "无法查询到此账户");
/*  588 */       return jSONObject;
/*      */     } 
/*      */     
/*  591 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  593 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  594 */       response.setStatus(500);
/*  595 */       JSONObject jSONObject = new JSONObject(true);
/*  596 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  597 */       jSONObject.put("error", "Bad Request");
/*  598 */       jSONObject.put("message", "error");
/*  599 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  604 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  606 */     JSONObject chars = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId));
/*      */     
/*  608 */     JSONObject inventory = new JSONObject(true);
/*      */     
/*  610 */     String charid = chars.getString("charId");
/*  611 */     int rarity = ArknightsApplication.characterJson.getJSONObject(String.valueOf(charid)).getIntValue("rarity");
/*  612 */     JSONArray phases = ArknightsApplication.characterJson.getJSONObject(String.valueOf(charid)).getJSONArray("phases");
/*  613 */     JSONArray skills = ArknightsApplication.characterJson.getJSONObject(String.valueOf(charid)).getJSONArray("skills");
/*  614 */     for (int i = 0; i < phases.size(); i++) {
/*  615 */       if (destEvolvePhase == i) {
/*  616 */         JSONArray evolveCost = phases.getJSONObject(i).getJSONArray("evolveCost");
/*  617 */         for (int l = 0; l < evolveCost.size(); l++) {
/*  618 */           String itemid = evolveCost.getJSONObject(l).getString("id");
/*  619 */           int count = evolveCost.getJSONObject(l).getIntValue("count");
/*  620 */           UserSyncData.getJSONObject("inventory").put(itemid, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(itemid) - count));
/*  621 */           inventory.put(itemid, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(itemid)));
/*      */         } 
/*      */       } 
/*      */     } 
/*  625 */     int gold = UserSyncData.getJSONObject("status").getIntValue("gold");
/*  626 */     JSONObject tchar = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId));
/*  627 */     String charId = tchar.getString("charId");
/*  628 */     JSONArray charskills = tchar.getJSONArray("skills");
/*  629 */     if (destEvolvePhase == 1) {
/*  630 */       if (rarity == 2) {
/*  631 */         gold -= 10000;
/*  632 */       } else if (rarity == 3) {
/*  633 */         gold -= 15000;
/*  634 */       } else if (rarity == 4) {
/*  635 */         gold -= 20000;
/*  636 */       } else if (rarity == 5) {
/*  637 */         gold -= 30000;
/*      */       } 
/*  639 */       for (int j = 0; j < charskills.size(); j++) {
/*  640 */         if (charskills.getJSONObject(j).getString("skillId").equals(skills.getJSONObject(j).getString("skillId")) && 
/*  641 */           skills.getJSONObject(j).getJSONObject("unlockCond").getIntValue("phase") == 1) {
/*  642 */           charskills.getJSONObject(j).put("unlock", Integer.valueOf(1));
/*      */         }
/*      */       }
/*      */     
/*  646 */     } else if (destEvolvePhase == 2) {
/*  647 */       if (rarity == 3) {
/*  648 */         gold -= 60000;
/*  649 */       } else if (rarity == 4) {
/*  650 */         gold -= 120000;
/*  651 */       } else if (rarity == 5) {
/*  652 */         gold -= 180000;
/*      */       } 
/*  654 */       if (tchar.getString("skin").equals(charId + "#1")) {
/*  655 */         tchar.put("skin", charId + "#2");
/*      */       }
/*  657 */       for (int j = 0; j < charskills.size(); j++) {
/*  658 */         if (charskills.getJSONObject(j).getString("skillId").equals(skills.getJSONObject(j).getString("skillId")) && 
/*  659 */           skills.getJSONObject(j).getJSONObject("unlockCond").getIntValue("phase") == 2) {
/*  660 */           charskills.getJSONObject(j).put("unlock", Integer.valueOf(1));
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  665 */     UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(gold));
/*  666 */     tchar.put("evolvePhase", Integer.valueOf(destEvolvePhase));
/*  667 */     tchar.put("level", Integer.valueOf(1));
/*      */     
/*  669 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  671 */     JSONObject result = new JSONObject(true);
/*  672 */     JSONObject modified = new JSONObject(true);
/*  673 */     JSONObject playerDataDelta = new JSONObject(true);
/*  674 */     JSONObject troop = new JSONObject(true);
/*  675 */     JSONObject status = new JSONObject(true);
/*  676 */     status.put("gold", Integer.valueOf(gold));
/*  677 */     JSONObject charss = new JSONObject(true);
/*  678 */     charss.put(String.valueOf(charInstId), tchar);
/*  679 */     troop.put("chars", charss);
/*  680 */     modified.put("troop", troop);
/*  681 */     modified.put("inventory", inventory);
/*  682 */     modified.put("status", status);
/*  683 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  684 */     playerDataDelta.put("modified", modified);
/*  685 */     result.put("playerDataDelta", playerDataDelta);
/*  686 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/changeCharSkin"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject ChangeCharSkin(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  693 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  694 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /charBuild/changeCharSkin");
/*      */     
/*  696 */     int charInstId = JsonBody.getIntValue("charInstId");
/*  697 */     String skinId = JsonBody.getString("skinId");
/*      */ 
/*      */     
/*  700 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  701 */     if (Accounts.size() != 1) {
/*  702 */       JSONObject jSONObject = new JSONObject(true);
/*  703 */       jSONObject.put("result", Integer.valueOf(2));
/*  704 */       jSONObject.put("error", "无法查询到此账户");
/*  705 */       return jSONObject;
/*      */     } 
/*      */     
/*  708 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  710 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  711 */       response.setStatus(500);
/*  712 */       JSONObject jSONObject = new JSONObject(true);
/*  713 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  714 */       jSONObject.put("error", "Bad Request");
/*  715 */       jSONObject.put("message", "error");
/*  716 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  720 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  722 */     UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)).put("skin", skinId);
/*      */     
/*  724 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  726 */     JSONObject result = new JSONObject(true);
/*  727 */     JSONObject modified = new JSONObject(true);
/*  728 */     JSONObject playerDataDelta = new JSONObject(true);
/*  729 */     JSONObject troop = new JSONObject(true);
/*  730 */     JSONObject chars = new JSONObject(true);
/*  731 */     chars.put(String.valueOf(charInstId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)));
/*  732 */     troop.put("chars", chars);
/*  733 */     modified.put("troop", troop);
/*  734 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  735 */     playerDataDelta.put("modified", modified);
/*  736 */     result.put("playerDataDelta", playerDataDelta);
/*  737 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/setCharVoiceLan"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject setCharVoiceLan(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  744 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  745 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /charBuild/setCharVoiceLan");
/*      */     
/*  747 */     JSONArray charList = JsonBody.getJSONArray("charList");
/*  748 */     String voiceLan = JsonBody.getString("voiceLan");
/*      */ 
/*      */     
/*  751 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  752 */     if (Accounts.size() != 1) {
/*  753 */       JSONObject jSONObject = new JSONObject(true);
/*  754 */       jSONObject.put("result", Integer.valueOf(2));
/*  755 */       jSONObject.put("error", "无法查询到此账户");
/*  756 */       return jSONObject;
/*      */     } 
/*      */     
/*  759 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  761 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  762 */       response.setStatus(500);
/*  763 */       JSONObject jSONObject = new JSONObject(true);
/*  764 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  765 */       jSONObject.put("error", "Bad Request");
/*  766 */       jSONObject.put("message", "error");
/*  767 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  771 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  773 */     for (int i = 0; i < charList.size(); i++) {
/*  774 */       UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(charList.getString(i)).put("voiceLan", voiceLan);
/*      */     }
/*      */     
/*  777 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  779 */     JSONObject result = new JSONObject(true);
/*  780 */     JSONObject modified = new JSONObject(true);
/*  781 */     JSONObject playerDataDelta = new JSONObject(true);
/*  782 */     JSONObject troop = new JSONObject(true);
/*  783 */     troop.put("chars", UserSyncData.getJSONObject("troop").getJSONObject("chars"));
/*  784 */     modified.put("troop", troop);
/*  785 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  786 */     playerDataDelta.put("modified", modified);
/*  787 */     result.put("playerDataDelta", playerDataDelta);
/*  788 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/batchSetCharVoiceLan"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject batchSetCharVoiceLan(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  795 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  796 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /charBuild/batchSetCharVoiceLan");
/*      */     
/*  798 */     String voiceLan = JsonBody.getString("voiceLan");
/*      */ 
/*      */     
/*  801 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  802 */     if (Accounts.size() != 1) {
/*  803 */       JSONObject jSONObject = new JSONObject(true);
/*  804 */       jSONObject.put("result", Integer.valueOf(2));
/*  805 */       jSONObject.put("error", "无法查询到此账户");
/*  806 */       return jSONObject;
/*      */     } 
/*      */     
/*  809 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  811 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  812 */       response.setStatus(500);
/*  813 */       JSONObject jSONObject = new JSONObject(true);
/*  814 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  815 */       jSONObject.put("error", "Bad Request");
/*  816 */       jSONObject.put("message", "error");
/*  817 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  821 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*  822 */     JSONObject voiceLangDict = ArknightsApplication.charwordTable.getJSONObject("voiceLangDict");
/*      */     
/*  824 */     JSONObject chars = UserSyncData.getJSONObject("troop").getJSONObject("chars");
/*      */     
/*  826 */     for (Map.Entry entry : chars.entrySet()) {
/*  827 */       String charId = chars.getJSONObject(entry.getKey().toString()).getString("charId");
/*  828 */       if (voiceLangDict.containsKey(charId)) {
/*  829 */         JSONObject cvDictionary = voiceLangDict.getJSONObject(charId).getJSONObject("dict");
/*  830 */         if (cvDictionary.containsKey(voiceLan)) {
/*  831 */           chars.getJSONObject(entry.getKey().toString()).put("voiceLan", voiceLan);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  836 */     UserSyncData.getJSONObject("status").put("globalVoiceLan", voiceLan);
/*      */     
/*  838 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  840 */     JSONObject result = new JSONObject(true);
/*  841 */     JSONObject modified = new JSONObject(true);
/*  842 */     JSONObject playerDataDelta = new JSONObject(true);
/*  843 */     JSONObject troop = new JSONObject(true);
/*  844 */     troop.put("chars", chars);
/*  845 */     modified.put("troop", troop);
/*  846 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  847 */     playerDataDelta.put("modified", modified);
/*  848 */     result.put("playerDataDelta", playerDataDelta);
/*  849 */     result.put("result", Integer.valueOf(0));
/*  850 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/unlockEquipment"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject unlockEquipment(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  857 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  858 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /charBuild/unlockEquipment");
/*      */     
/*  860 */     int charInstId = JsonBody.getIntValue("charInstId");
/*  861 */     String templateId = JsonBody.getString("templateId");
/*  862 */     String equipId = JsonBody.getString("equipId");
/*      */ 
/*      */     
/*  865 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  866 */     if (Accounts.size() != 1) {
/*  867 */       JSONObject jSONObject = new JSONObject(true);
/*  868 */       jSONObject.put("result", Integer.valueOf(2));
/*  869 */       jSONObject.put("error", "无法查询到此账户");
/*  870 */       return jSONObject;
/*      */     } 
/*      */     
/*  873 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  875 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  876 */       response.setStatus(500);
/*  877 */       JSONObject jSONObject = new JSONObject(true);
/*  878 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  879 */       jSONObject.put("error", "Bad Request");
/*  880 */       jSONObject.put("message", "error");
/*  881 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  885 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  887 */     UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)).getJSONObject("equip").getJSONObject(equipId).put("locked", Integer.valueOf(0));
/*      */     
/*  889 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  891 */     JSONObject result = new JSONObject(true);
/*  892 */     JSONObject modified = new JSONObject(true);
/*  893 */     JSONObject playerDataDelta = new JSONObject(true);
/*  894 */     JSONObject troop = new JSONObject(true);
/*  895 */     JSONObject chars = new JSONObject(true);
/*  896 */     chars.put(String.valueOf(charInstId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)));
/*  897 */     troop.put("chars", chars);
/*  898 */     modified.put("troop", troop);
/*  899 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  900 */     playerDataDelta.put("modified", modified);
/*  901 */     result.put("playerDataDelta", playerDataDelta);
/*  902 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/setEquipment"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject setEquipment(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  909 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  910 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /charBuild/setEquipment");
/*      */     
/*  912 */     int charInstId = JsonBody.getIntValue("charInstId");
/*  913 */     String templateId = JsonBody.getString("templateId");
/*  914 */     String equipId = JsonBody.getString("equipId");
/*      */ 
/*      */     
/*  917 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  918 */     if (Accounts.size() != 1) {
/*  919 */       JSONObject jSONObject = new JSONObject(true);
/*  920 */       jSONObject.put("result", Integer.valueOf(2));
/*  921 */       jSONObject.put("error", "无法查询到此账户");
/*  922 */       return jSONObject;
/*      */     } 
/*      */     
/*  925 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  927 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  928 */       response.setStatus(500);
/*  929 */       JSONObject jSONObject = new JSONObject(true);
/*  930 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  931 */       jSONObject.put("error", "Bad Request");
/*  932 */       jSONObject.put("message", "error");
/*  933 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  937 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  939 */     UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)).put("currentEquip", equipId);
/*      */     
/*  941 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  943 */     JSONObject result = new JSONObject(true);
/*  944 */     JSONObject modified = new JSONObject(true);
/*  945 */     JSONObject playerDataDelta = new JSONObject(true);
/*  946 */     JSONObject troop = new JSONObject(true);
/*  947 */     JSONObject chars = new JSONObject(true);
/*  948 */     chars.put(String.valueOf(charInstId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)));
/*  949 */     troop.put("chars", chars);
/*  950 */     modified.put("troop", troop);
/*  951 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  952 */     playerDataDelta.put("modified", modified);
/*  953 */     result.put("playerDataDelta", playerDataDelta);
/*  954 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/addonStory/unlock"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject addonStoryUnlock(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  961 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  962 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /charBuild/addonStory/unlock");
/*      */     
/*  964 */     String charId = JsonBody.getString("charId");
/*  965 */     String storyId = JsonBody.getString("storyId");
/*      */ 
/*      */     
/*  968 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  969 */     if (Accounts.size() != 1) {
/*  970 */       JSONObject jSONObject = new JSONObject(true);
/*  971 */       jSONObject.put("result", Integer.valueOf(2));
/*  972 */       jSONObject.put("error", "无法查询到此账户");
/*  973 */       return jSONObject;
/*      */     } 
/*      */     
/*  976 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  978 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  979 */       response.setStatus(500);
/*  980 */       JSONObject jSONObject = new JSONObject(true);
/*  981 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  982 */       jSONObject.put("error", "Bad Request");
/*  983 */       jSONObject.put("message", "error");
/*  984 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  988 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  990 */     JSONObject story = new JSONObject(true);
/*  991 */     story.put("fts", Long.valueOf((new Date()).getTime() / 1000L));
/*  992 */     story.put("rts", Long.valueOf((new Date()).getTime() / 1000L));
/*      */     
/*  994 */     JSONObject addon = UserSyncData.getJSONObject("troop").getJSONObject("addon");
/*  995 */     JSONObject char_data = new JSONObject(true);
/*      */     
/*  997 */     if (addon.containsKey(charId)) {
/*      */       
/*  999 */       if (addon.getJSONObject(charId).containsKey("story")) {
/* 1000 */         addon.getJSONObject(charId).getJSONObject("story").put(storyId, story);
/* 1001 */         UserSyncData.getJSONObject("troop").put("addon", addon);
/*      */       } else {
/* 1003 */         JSONObject story_1 = new JSONObject(true);
/* 1004 */         story_1.put(storyId, story);
/* 1005 */         char_data = addon.getJSONObject(charId);
/* 1006 */         char_data.put("story", story_1);
/* 1007 */         addon.put(charId, char_data);
/*      */         
/* 1009 */         UserSyncData.getJSONObject("troop").put("addon", addon);
/*      */       } 
/*      */     } else {
/*      */       
/* 1013 */       JSONObject story_1 = new JSONObject(true);
/* 1014 */       story_1.put(storyId, story);
/* 1015 */       char_data.put("story", story_1);
/* 1016 */       addon.put(charId, char_data);
/*      */       
/* 1018 */       UserSyncData.getJSONObject("troop").put("addon", addon);
/*      */     } 
/*      */ 
/*      */     
/* 1022 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 1024 */     JSONObject result = new JSONObject(true);
/* 1025 */     JSONObject playerDataDelta = new JSONObject(true);
/* 1026 */     JSONObject modified = new JSONObject(true);
/* 1027 */     JSONObject troop = new JSONObject(true);
/* 1028 */     troop.put("addon", addon);
/* 1029 */     modified.put("troop", troop);
/* 1030 */     playerDataDelta.put("modified", modified);
/* 1031 */     result.put("playerDataDelta", playerDataDelta);
/* 1032 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/addonStage/battleStart"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject addonStageBattleStart(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 1039 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 1040 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /charBuild/addonStage/battleStart");
/*      */     
/* 1042 */     String charId = JsonBody.getString("charId");
/* 1043 */     String stageId = JsonBody.getString("stageId");
/*      */ 
/*      */     
/* 1046 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 1047 */     if (Accounts.size() != 1) {
/* 1048 */       JSONObject jSONObject = new JSONObject(true);
/* 1049 */       jSONObject.put("result", Integer.valueOf(2));
/* 1050 */       jSONObject.put("error", "无法查询到此账户");
/* 1051 */       return jSONObject;
/*      */     } 
/*      */     
/* 1054 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 1056 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 1057 */       response.setStatus(500);
/* 1058 */       JSONObject jSONObject = new JSONObject(true);
/* 1059 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 1060 */       jSONObject.put("error", "Bad Request");
/* 1061 */       jSONObject.put("message", "error");
/* 1062 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/* 1066 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 1068 */     JSONObject story = new JSONObject(true);
/* 1069 */     story.put("fts", Integer.valueOf(-1));
/* 1070 */     story.put("rts", Integer.valueOf(-1));
/* 1071 */     story.put("startTime", null);
/* 1072 */     story.put("startTimes", Integer.valueOf(0));
/* 1073 */     story.put("state", Integer.valueOf(0));
/*      */     
/* 1075 */     JSONObject addon = UserSyncData.getJSONObject("troop").getJSONObject("addon");
/* 1076 */     JSONObject char_data = new JSONObject(true);
/*      */     
/* 1078 */     if (!UserSyncData.getJSONObject("troop").getJSONObject("addon").containsKey(charId)) {
/*      */       
/* 1080 */       JSONObject stage = new JSONObject(true);
/* 1081 */       stage.put(stageId, story);
/* 1082 */       char_data.put("stage", stage);
/* 1083 */       UserSyncData.getJSONObject("troop").getJSONObject("addon").put(charId, char_data);
/*      */       
/* 1085 */       UserSyncData.getJSONObject("troop").put("addon", addon);
/*      */     } 
/*      */     
/* 1088 */     if (!UserSyncData.getJSONObject("troop").getJSONObject("addon").getJSONObject(charId).containsKey("stage")) {
/*      */       
/* 1090 */       JSONObject stage = new JSONObject(true);
/* 1091 */       stage.put(stageId, story);
/* 1092 */       char_data.put("stage", stage);
/* 1093 */       UserSyncData.getJSONObject("troop").getJSONObject("addon").put(charId, char_data);
/*      */       
/* 1095 */       UserSyncData.getJSONObject("troop").put("addon", addon);
/*      */     } 
/*      */     
/* 1098 */     if (!UserSyncData.getJSONObject("troop").getJSONObject("addon").getJSONObject(charId).getJSONObject("stage").containsKey(stageId)) {
/*      */       
/* 1100 */       JSONObject stage = new JSONObject(true);
/* 1101 */       stage.put(stageId, story);
/* 1102 */       char_data.put("stage", stage);
/* 1103 */       UserSyncData.getJSONObject("troop").getJSONObject("addon").put(charId, char_data);
/*      */       
/* 1105 */       UserSyncData.getJSONObject("troop").put("addon", addon);
/*      */     } 
/*      */     
/* 1108 */     if (UserSyncData.getJSONObject("troop").getJSONObject("addon").getJSONObject(charId).getJSONObject("stage").getJSONObject(stageId).getIntValue("state") != 3) {
/* 1109 */       UserSyncData.getJSONObject("troop").getJSONObject("addon").getJSONObject(charId).getJSONObject("stage").getJSONObject(stageId).put("state", Integer.valueOf(1));
/*      */     }
/*      */     
/* 1112 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 1114 */     JSONObject result = new JSONObject(true);
/* 1115 */     JSONObject playerDataDelta = new JSONObject(true);
/* 1116 */     JSONObject modified = new JSONObject(true);
/* 1117 */     JSONObject troop = new JSONObject(true);
/* 1118 */     troop.put("addon", addon);
/* 1119 */     modified.put("troop", troop);
/* 1120 */     playerDataDelta.put("modified", modified);
/* 1121 */     result.put("playerDataDelta", playerDataDelta);
/* 1122 */     result.put("battleId", charId + "&" + stageId);
/* 1123 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/addonStage/battleFinish"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject addonStageBattleFinish(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 1130 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 1131 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /charBuild/addonStage/battleFinish");
/*      */     
/* 1133 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 1134 */     if (Accounts.size() != 1) {
/* 1135 */       JSONObject jSONObject = new JSONObject(true);
/* 1136 */       jSONObject.put("result", Integer.valueOf(2));
/* 1137 */       jSONObject.put("error", "无法查询到此账户");
/* 1138 */       return jSONObject;
/*      */     } 
/*      */     
/* 1141 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 1143 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 1144 */       response.setStatus(500);
/* 1145 */       JSONObject jSONObject = new JSONObject(true);
/* 1146 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 1147 */       jSONObject.put("error", "Bad Request");
/* 1148 */       jSONObject.put("message", "error");
/* 1149 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/* 1153 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 1155 */     JSONObject BattleData = Utils.BattleData_decrypt(JsonBody.getString("data"), UserSyncData.getJSONObject("pushFlags").getString("status"));
/*      */     
/* 1157 */     String stageId = BattleData.getString("battleId").substring(BattleData.getString("battleId").indexOf("&") + 1);
/* 1158 */     String charId = BattleData.getString("battleId").substring(0, BattleData.getString("battleId").indexOf("&"));
/*      */ 
/*      */ 
/*      */     
/* 1162 */     JSONObject stageData = UserSyncData.getJSONObject("troop").getJSONObject("addon").getJSONObject(charId).getJSONObject("stage").getJSONObject(stageId);
/*      */ 
/*      */     
/* 1165 */     int DropRate = ArknightsApplication.serverConfig.getJSONObject("battle").getIntValue("dropRate");
/*      */     
/* 1167 */     int completeState = BattleData.getIntValue("completeState");
/*      */     
/* 1169 */     if (ArknightsApplication.serverConfig.getJSONObject("battle").getBooleanValue("debug")) {
/* 1170 */       completeState = 3;
/*      */     }
/*      */     
/* 1173 */     JSONArray firstRewards = new JSONArray();
/*      */     
/* 1175 */     if (completeState == 1) {
/* 1176 */       if (stageData.getIntValue("state") != 3) {
/* 1177 */         stageData.put("state", Integer.valueOf(completeState));
/*      */       }
/* 1179 */       JSONObject jSONObject1 = new JSONObject(true);
/* 1180 */       JSONObject jSONObject2 = new JSONObject(true);
/* 1181 */       JSONObject jSONObject3 = new JSONObject(true);
/* 1182 */       JSONObject jSONObject4 = new JSONObject(true);
/* 1183 */       JSONObject jSONObject5 = new JSONObject(true);
/* 1184 */       jSONObject5.put(charId, UserSyncData.getJSONObject("troop").getJSONObject("addon").getJSONObject(charId));
/* 1185 */       jSONObject4.put("addon", jSONObject5);
/* 1186 */       jSONObject3.put("troop", jSONObject4);
/* 1187 */       jSONObject2.put("modified", jSONObject3);
/* 1188 */       jSONObject1.put("playerDataDelta", jSONObject2);
/* 1189 */       jSONObject1.put("firstRewards", firstRewards);
/* 1190 */       jSONObject1.put("result", Integer.valueOf(0));
/* 1191 */       return jSONObject1;
/*      */     } 
/*      */ 
/*      */     
/* 1195 */     if (stageData.getIntValue("state") == 3) {
/* 1196 */       JSONObject jSONObject1 = new JSONObject(true);
/* 1197 */       JSONObject jSONObject2 = new JSONObject(true);
/* 1198 */       JSONObject jSONObject3 = new JSONObject(true);
/* 1199 */       JSONObject jSONObject4 = new JSONObject(true);
/* 1200 */       JSONObject jSONObject5 = new JSONObject(true);
/* 1201 */       jSONObject5.put(charId, UserSyncData.getJSONObject("troop").getJSONObject("addon").getJSONObject(charId));
/* 1202 */       jSONObject4.put("addon", jSONObject5);
/* 1203 */       jSONObject3.put("troop", jSONObject4);
/* 1204 */       jSONObject2.put("modified", jSONObject3);
/* 1205 */       jSONObject1.put("playerDataDelta", jSONObject2);
/* 1206 */       jSONObject1.put("firstRewards", firstRewards);
/* 1207 */       jSONObject1.put("result", Integer.valueOf(0));
/* 1208 */       return jSONObject1;
/*      */     } 
/* 1210 */     JSONObject diamondShard = new JSONObject(true);
/*      */     
/* 1212 */     diamondShard.put("count", Integer.valueOf(200 * DropRate));
/* 1213 */     diamondShard.put("id", "4003");
/* 1214 */     diamondShard.put("type", "DIAMOND_SHD");
/*      */     
/* 1216 */     firstRewards.add(diamondShard);
/*      */     
/* 1218 */     UserSyncData.getJSONObject("status").put("diamondShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("diamondShard") + 200 * DropRate));
/*      */     
/* 1220 */     stageData.put("state", Integer.valueOf(completeState));
/* 1221 */     stageData.put("fts", Long.valueOf((new Date()).getTime() / 1000L));
/* 1222 */     stageData.put("rts", Long.valueOf((new Date()).getTime() / 1000L));
/*      */     
/* 1224 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 1226 */     JSONObject result = new JSONObject(true);
/* 1227 */     JSONObject playerDataDelta = new JSONObject(true);
/* 1228 */     JSONObject modified = new JSONObject(true);
/* 1229 */     JSONObject troop = new JSONObject(true);
/* 1230 */     JSONObject addon = new JSONObject(true);
/* 1231 */     addon.put(charId, UserSyncData.getJSONObject("troop").getJSONObject("addon").getJSONObject(charId));
/* 1232 */     troop.put("addon", addon);
/* 1233 */     modified.put("troop", troop);
/* 1234 */     playerDataDelta.put("modified", modified);
/* 1235 */     result.put("playerDataDelta", playerDataDelta);
/* 1236 */     result.put("firstRewards", firstRewards);
/* 1237 */     result.put("result", Integer.valueOf(0));
/* 1238 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/changeCharTemplate"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject changeCharTemplate(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 1245 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 1246 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /charBuild/changeCharTemplate");
/*      */     
/* 1248 */     int charInstId = JsonBody.getIntValue("charInstId");
/* 1249 */     String templateId = JsonBody.getString("templateId");
/*      */ 
/*      */     
/* 1252 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 1253 */     if (Accounts.size() != 1) {
/* 1254 */       JSONObject jSONObject = new JSONObject(true);
/* 1255 */       jSONObject.put("result", Integer.valueOf(2));
/* 1256 */       jSONObject.put("error", "无法查询到此账户");
/* 1257 */       return jSONObject;
/*      */     } 
/*      */     
/* 1260 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 1262 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 1263 */       response.setStatus(500);
/* 1264 */       JSONObject jSONObject = new JSONObject(true);
/* 1265 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 1266 */       jSONObject.put("error", "Bad Request");
/* 1267 */       jSONObject.put("message", "error");
/* 1268 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/* 1272 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 1274 */     UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)).put("currentTmpl", templateId);
/*      */     
/* 1276 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 1278 */     JSONObject result = new JSONObject(true);
/* 1279 */     JSONObject modified = new JSONObject(true);
/* 1280 */     JSONObject playerDataDelta = new JSONObject(true);
/* 1281 */     JSONObject troop = new JSONObject(true);
/* 1282 */     JSONObject chars = new JSONObject(true);
/* 1283 */     chars.put(String.valueOf(charInstId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)));
/* 1284 */     troop.put("chars", chars);
/* 1285 */     modified.put("troop", troop);
/* 1286 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 1287 */     playerDataDelta.put("modified", modified);
/* 1288 */     result.put("playerDataDelta", playerDataDelta);
/* 1289 */     return result;
/*      */   }
/*      */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\charBuild.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */