/*      */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*      */ import com.alibaba.fastjson.JSONArray;
/*      */ import com.alibaba.fastjson.JSONObject;
/*      */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*      */ import com.hypergryph.arknights.ArknightsApplication;
/*      */ import com.hypergryph.arknights.admin;
/*      */ import com.hypergryph.arknights.core.dao.userDao;
/*      */ import com.hypergryph.arknights.core.decrypt.Utils;
/*      */ import com.hypergryph.arknights.core.pojo.Account;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import org.springframework.web.bind.annotation.PostMapping;
/*      */ import org.springframework.web.bind.annotation.RequestBody;
/*      */ import org.springframework.web.bind.annotation.RequestHeader;
/*      */ 
/*      */ @RestController
/*      */ @RequestMapping({"/rlv2"})
/*      */ public class rlv2 {
/*   25 */   private static final Logger LOGGER = LogManager.getLogger();
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/createGame"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject createGame(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*   31 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*   32 */     LOGGER.info("[/" + clientIp + "] /rlv2/createGame");
/*      */     
/*   34 */     String theme = JsonBody.getString("theme");
/*   35 */     String predefinedId = JsonBody.getString("predefinedId");
/*   36 */     String mode = JsonBody.getString("mode");
/*      */     
/*   38 */     if (!ArknightsApplication.enableServer) {
/*   39 */       response.setStatus(400);
/*   40 */       JSONObject jSONObject = new JSONObject(true);
/*   41 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*   42 */       jSONObject.put("error", "Bad Request");
/*   43 */       jSONObject.put("message", "server is close");
/*   44 */       return jSONObject;
/*      */     } 
/*      */     
/*   47 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*   48 */     if (Accounts.size() != 1) {
/*   49 */       JSONObject jSONObject = new JSONObject(true);
/*   50 */       jSONObject.put("result", Integer.valueOf(2));
/*   51 */       jSONObject.put("error", "无法查询到此账户");
/*   52 */       return jSONObject;
/*      */     } 
/*      */     
/*   55 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*   57 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*   58 */       response.setStatus(500);
/*   59 */       JSONObject jSONObject = new JSONObject(true);
/*   60 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*   61 */       jSONObject.put("error", "Bad Request");
/*   62 */       jSONObject.put("message", "error");
/*   63 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*   67 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */ 
/*      */     
/*   70 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/*      */     
/*   72 */     current.put("buff", new JSONObject());
/*   73 */     current.put("game", new JSONObject());
/*   74 */     current.put("inventory", new JSONObject());
/*   75 */     current.put("map", new JSONObject());
/*   76 */     current.put("player", new JSONObject());
/*   77 */     current.put("record", new JSONObject());
/*   78 */     current.put("troop", new JSONObject());
/*      */     
/*   80 */     current.getJSONObject("buff").put("tmpHP", Integer.valueOf(0));
/*   81 */     current.getJSONObject("buff").put("capsule", null);
/*      */     
/*   83 */     JSONObject support = new JSONObject();
/*   84 */     support.put("support", Boolean.valueOf(false));
/*   85 */     current.getJSONObject("game").put("outer", support);
/*   86 */     current.getJSONObject("game").put("mode", mode);
/*   87 */     current.getJSONObject("game").put("predefined", predefinedId);
/*   88 */     current.getJSONObject("game").put("theme", theme);
/*   89 */     current.getJSONObject("game").put("start", Long.valueOf((new Date()).getTime() / 1000L));
/*      */     
/*   91 */     current.getJSONObject("inventory").put("relic", new JSONObject());
/*   92 */     current.getJSONObject("inventory").put("recruit", new JSONObject());
/*   93 */     current.getJSONObject("inventory").put("trap", null);
/*      */     
/*   95 */     current.getJSONObject("map").put("zones", new JSONObject());
/*      */     
/*   97 */     current.getJSONObject("record").put("brief", null);
/*      */     
/*   99 */     current.getJSONObject("troop").put("chars", new JSONObject());
/*      */     
/*  101 */     current.getJSONObject("player").put("chgEnding", Boolean.valueOf(false));
/*  102 */     current.getJSONObject("player").put("trace", new JSONArray());
/*  103 */     current.getJSONObject("player").put("toEnding", "ro_ending_1");
/*  104 */     current.getJSONObject("player").put("state", "INIT");
/*      */     
/*  106 */     JSONObject cursor = new JSONObject();
/*  107 */     cursor.put("position", null);
/*  108 */     cursor.put("zone", Integer.valueOf(1));
/*      */     
/*  110 */     current.getJSONObject("player").put("property", new JSONObject());
/*  111 */     current.getJSONObject("player").getJSONObject("property").put("population", new JSONObject());
/*  112 */     current.getJSONObject("player").put("status", new JSONObject());
/*  113 */     current.getJSONObject("player").put("pending", new JSONArray());
/*  114 */     current.getJSONObject("player").getJSONObject("property").put("capacity", Integer.valueOf(6));
/*  115 */     current.getJSONObject("player").getJSONObject("property").put("conPerfectBattle", Integer.valueOf(0));
/*  116 */     current.getJSONObject("player").getJSONObject("property").put("level", Integer.valueOf(1));
/*  117 */     current.getJSONObject("player").getJSONObject("property").put("exp", Integer.valueOf(0));
/*  118 */     current.getJSONObject("player").getJSONObject("property").put("hp", Integer.valueOf(6));
/*  119 */     current.getJSONObject("player").getJSONObject("property").put("gold", Integer.valueOf(8));
/*  120 */     current.getJSONObject("player").getJSONObject("property").getJSONObject("population").put("cost", Integer.valueOf(0));
/*  121 */     current.getJSONObject("player").getJSONObject("property").getJSONObject("population").put("max", Integer.valueOf(6));
/*      */     
/*  123 */     current.getJSONObject("player").getJSONObject("status").put("bankPut", Integer.valueOf(0));
/*      */     
/*  125 */     current.getJSONObject("player").put("cursor", cursor);
/*      */ 
/*      */     
/*  128 */     JSONObject buff = UserSyncData.getJSONObject("rlv2").getJSONObject("outer").getJSONObject(theme).getJSONObject("buff");
/*      */     
/*  130 */     for (Map.Entry entry : buff.getJSONObject("unlocked").entrySet()) {
/*      */       
/*  132 */       JSONArray buffDisplayInfo = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("developments").getJSONObject(entry.getKey().toString()).getJSONArray("buffDisplayInfo");
/*      */       
/*  134 */       for (int i = 0; i < buffDisplayInfo.size(); i++) {
/*  135 */         String displayType = buffDisplayInfo.getJSONObject(i).getString("displayType");
/*  136 */         String displayForm = buffDisplayInfo.getJSONObject(i).getString("displayForm");
/*  137 */         int displayNum = buffDisplayInfo.getJSONObject(i).getIntValue("displayNum");
/*      */         
/*  139 */         if (displayForm.equals("ABSOLUTE_VAL")) {
/*      */ 
/*      */           
/*  142 */           if (displayType.equals("display_gold")) {
/*  143 */             current.getJSONObject("player").getJSONObject("property").put("gold", Integer.valueOf(current.getJSONObject("player").getJSONObject("property").getIntValue("gold") + displayNum));
/*      */           }
/*      */           
/*  146 */           if (displayType.equals("display_hp")) {
/*  147 */             current.getJSONObject("player").getJSONObject("property").put("hp", Integer.valueOf(current.getJSONObject("player").getJSONObject("property").getIntValue("hp") + displayNum));
/*      */           }
/*      */           
/*  150 */           if (displayType.equals("display_squad_capacity")) {
/*  151 */             current.getJSONObject("player").getJSONObject("property").put("capacity", Integer.valueOf(current.getJSONObject("player").getJSONObject("property").getIntValue("capacity") + displayNum));
/*      */           }
/*      */           
/*  154 */           if (displayType.equals("display_temp_hp")) {
/*  155 */             current.getJSONObject("buff").put("tmpHP", Integer.valueOf(current.getJSONObject("buff").getIntValue("tmpHP") + displayNum));
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  161 */     JSONObject content = new JSONObject();
/*  162 */     JSONObject tmpObj = new JSONObject();
/*      */     
/*  164 */     JSONObject initModeRelic = new JSONObject();
/*  165 */     JSONArray items = new JSONArray();
/*      */     
/*  167 */     initModeRelic.put("step", JSONArray.parseArray("[1,4]"));
/*      */     
/*  169 */     JSONObject rlv2InitRelic = ArknightsApplication.roguelike.getJSONObject("rlv2").getJSONObject("initRelic");
/*  170 */     for (Map.Entry entry : rlv2InitRelic.entrySet()) {
/*  171 */       if (rlv2InitRelic.getBooleanValue(entry.getKey().toString())) {
/*  172 */         items.add(entry.getKey().toString());
/*      */       }
/*      */     } 
/*      */     
/*  176 */     initModeRelic.put("items", items);
/*      */     
/*  178 */     tmpObj.put("initModeRelic", initModeRelic);
/*      */     
/*  180 */     content.put("content", tmpObj);
/*  181 */     content.put("index", "e_0");
/*  182 */     content.put("type", "GAME_INIT_MODE_RELIC");
/*  183 */     current.getJSONObject("player").getJSONArray("pending").add(content);
/*      */ 
/*      */ 
/*      */     
/*  187 */     content = new JSONObject();
/*  188 */     tmpObj = new JSONObject();
/*      */     
/*  190 */     JSONObject initRelic = new JSONObject();
/*      */     
/*  192 */     initRelic.put("step", JSONArray.parseArray("[2,4]"));
/*  193 */     initRelic.put("items", JSONObject.parseObject("{\"0\":{\"id\":\"rogue_1_band_1\",\"count\":1},\"1\":{\"id\":\"rogue_1_band_2\",\"count\":1},\"2\":{\"id\":\"rogue_1_band_3\",\"count\":1},\"3\":{\"id\":\"rogue_1_band_4\",\"count\":1},\"4\":{\"id\":\"rogue_1_band_5\",\"count\":1},\"5\":{\"id\":\"rogue_1_band_6\",\"count\":1},\"6\":{\"id\":\"rogue_1_band_7\",\"count\":1},\"7\":{\"id\":\"rogue_1_band_8\",\"count\":1},\"8\":{\"id\":\"rogue_1_band_9\",\"count\":1},\"9\":{\"id\":\"rogue_1_band_10\",\"count\":1}}"));
/*      */     
/*  195 */     tmpObj.put("initRelic", initRelic);
/*      */     
/*  197 */     content.put("content", tmpObj);
/*  198 */     content.put("index", "e_1");
/*  199 */     content.put("type", "GAME_INIT_RELIC");
/*  200 */     current.getJSONObject("player").getJSONArray("pending").add(content);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  205 */     content = new JSONObject();
/*  206 */     tmpObj = new JSONObject();
/*      */     
/*  208 */     JSONObject initRecruitSet = new JSONObject();
/*      */     
/*  210 */     initRecruitSet.put("step", JSONArray.parseArray("[3,4]"));
/*  211 */     initRecruitSet.put("option", JSONArray.parseArray("[\"recruit_group_1\",\"recruit_group_2\",\"recruit_group_3\",\"recruit_group_random\"]"));
/*      */     
/*  213 */     tmpObj.put("initRecruitSet", initRecruitSet);
/*      */     
/*  215 */     content.put("content", tmpObj);
/*  216 */     content.put("index", "e_2");
/*  217 */     content.put("type", "GAME_INIT_RECRUIT_SET");
/*  218 */     current.getJSONObject("player").getJSONArray("pending").add(content);
/*      */ 
/*      */ 
/*      */     
/*  222 */     content = new JSONObject();
/*  223 */     tmpObj = new JSONObject();
/*      */     
/*  225 */     JSONObject initRecruit = new JSONObject();
/*      */     
/*  227 */     initRecruit.put("step", JSONArray.parseArray("[4,4]"));
/*  228 */     initRecruit.put("tickets", new JSONArray());
/*      */     
/*  230 */     tmpObj.put("initRecruit", initRecruit);
/*      */     
/*  232 */     content.put("content", tmpObj);
/*  233 */     content.put("index", "e_3");
/*  234 */     content.put("type", "GAME_INIT_RECRUIT");
/*  235 */     current.getJSONObject("player").getJSONArray("pending").add(content);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  240 */     if (mode.equals("MONTH_TEAM"));
/*      */ 
/*      */ 
/*      */     
/*  244 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  246 */     JSONObject result = new JSONObject(true);
/*  247 */     JSONObject playerDataDelta = new JSONObject(true);
/*  248 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  249 */     JSONObject modified = new JSONObject(true);
/*  250 */     JSONObject jSONObject1 = new JSONObject();
/*  251 */     jSONObject1.put("current", UserSyncData.getJSONObject("rlv2").getJSONObject("current"));
/*  252 */     JSONObject outer = new JSONObject();
/*  253 */     JSONObject rogue_1 = new JSONObject();
/*  254 */     rogue_1.put("record", UserSyncData.getJSONObject("rlv2").getJSONObject("outer").getJSONObject(theme).getJSONObject("record"));
/*  255 */     outer.put(theme, rogue_1);
/*  256 */     jSONObject1.put("outer", outer);
/*  257 */     modified.put("rlv2", jSONObject1);
/*  258 */     playerDataDelta.put("modified", modified);
/*  259 */     result.put("playerDataDelta", playerDataDelta);
/*  260 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/giveUpGame"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject giveUpGame(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  268 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  269 */     LOGGER.info("[/" + clientIp + "] /rlv2/giveUpGame");
/*      */     
/*  271 */     if (!ArknightsApplication.enableServer) {
/*  272 */       response.setStatus(400);
/*  273 */       JSONObject jSONObject = new JSONObject(true);
/*  274 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  275 */       jSONObject.put("error", "Bad Request");
/*  276 */       jSONObject.put("message", "server is close");
/*  277 */       return jSONObject;
/*      */     } 
/*      */     
/*  280 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  281 */     if (Accounts.size() != 1) {
/*  282 */       JSONObject jSONObject = new JSONObject(true);
/*  283 */       jSONObject.put("result", Integer.valueOf(2));
/*  284 */       jSONObject.put("error", "无法查询到此账户");
/*  285 */       return jSONObject;
/*      */     } 
/*      */     
/*  288 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  290 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  291 */       response.setStatus(500);
/*  292 */       JSONObject jSONObject = new JSONObject(true);
/*  293 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  294 */       jSONObject.put("error", "Bad Request");
/*  295 */       jSONObject.put("message", "error");
/*  296 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  300 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  302 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/*      */     
/*  304 */     current.put("buff", null);
/*  305 */     current.put("game", null);
/*  306 */     current.put("inventory", null);
/*  307 */     current.put("map", null);
/*  308 */     current.put("player", null);
/*  309 */     current.put("record", null);
/*  310 */     current.put("troop", null);
/*      */     
/*  312 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  314 */     JSONObject result = new JSONObject(true);
/*  315 */     JSONObject playerDataDelta = new JSONObject(true);
/*  316 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  317 */     JSONObject modified = new JSONObject(true);
/*  318 */     JSONObject jSONObject1 = new JSONObject();
/*  319 */     jSONObject1.put("current", current);
/*  320 */     modified.put("rlv2", jSONObject1);
/*  321 */     playerDataDelta.put("modified", modified);
/*  322 */     result.put("playerDataDelta", playerDataDelta);
/*  323 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/chooseInitialRelic"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject chooseInitialRelic(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  331 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  332 */     LOGGER.info("[/" + clientIp + "] /rlv2/chooseInitialRelic");
/*      */     
/*  334 */     String select = String.valueOf(JsonBody.getIntValue("select") + 1);
/*      */     
/*  336 */     if (!ArknightsApplication.enableServer) {
/*  337 */       response.setStatus(400);
/*  338 */       JSONObject jSONObject = new JSONObject(true);
/*  339 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  340 */       jSONObject.put("error", "Bad Request");
/*  341 */       jSONObject.put("message", "server is close");
/*  342 */       return jSONObject;
/*      */     } 
/*      */     
/*  345 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  346 */     if (Accounts.size() != 1) {
/*  347 */       JSONObject jSONObject = new JSONObject(true);
/*  348 */       jSONObject.put("result", Integer.valueOf(2));
/*  349 */       jSONObject.put("error", "无法查询到此账户");
/*  350 */       return jSONObject;
/*      */     } 
/*      */     
/*  353 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  355 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  356 */       response.setStatus(500);
/*  357 */       JSONObject jSONObject = new JSONObject(true);
/*  358 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  359 */       jSONObject.put("error", "Bad Request");
/*  360 */       jSONObject.put("message", "error");
/*  361 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  365 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  367 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/*  368 */     String theme = current.getJSONObject("game").getString("theme");
/*  369 */     JSONObject selectRelics = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("relics").getJSONObject(theme + "_band_" + select);
/*  370 */     JSONObject roguelikeItems = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("items");
/*  371 */     JSONObject r_0 = new JSONObject();
/*  372 */     r_0.put("count", Integer.valueOf(1));
/*  373 */     r_0.put("id", theme + "_band_" + select);
/*  374 */     r_0.put("index", "r_0");
/*  375 */     r_0.put("ts", Long.valueOf((new Date()).getTime() / 1000L));
/*  376 */     current.getJSONObject("inventory").getJSONObject("relic").put("r_0", r_0);
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
/*  392 */     JSONArray buffs = selectRelics.getJSONArray("buffs");
/*      */     
/*  394 */     JSONObject property = current.getJSONObject("player").getJSONObject("property");
/*      */     
/*  396 */     for (int i = 0; i < buffs.size(); i++) {
/*  397 */       String key = buffs.getJSONObject(i).getString("key");
/*  398 */       JSONArray blackboard = buffs.getJSONObject(i).getJSONArray("blackboard");
/*      */ 
/*      */       
/*  401 */       if (key.equals("level_life_point_add")) {
/*  402 */         current.getJSONObject("buff").put("tmpHP", Integer.valueOf(current.getJSONObject("buff").getIntValue("tmpHP") + blackboard.getJSONObject(0).getIntValue("value")));
/*      */       }
/*      */ 
/*      */       
/*  406 */       if (key.equals("immediate_reward")) {
/*      */         
/*  408 */         String valueStr = blackboard.getJSONObject(0).getString("valueStr");
/*  409 */         String type = roguelikeItems.getJSONObject(valueStr).getString("type");
/*  410 */         int count = blackboard.getJSONObject(1).getIntValue("value");
/*      */         
/*  412 */         if (type.equals("HP")) {
/*  413 */           property.put("hp", Integer.valueOf(property.getIntValue("hp") + count));
/*      */         }
/*  415 */         if (type.equals("GOLD")) {
/*  416 */           property.put("gold", Integer.valueOf(property.getIntValue("gold") + count));
/*      */         }
/*  418 */         if (type.equals("SQUAD_CAPACITY")) {
/*  419 */           property.put("capacity", Integer.valueOf(property.getIntValue("capacity") + count));
/*      */         }
/*  421 */         if (type.equals("POPULATION")) {
/*  422 */           property.getJSONObject("population").put("max", Integer.valueOf(property.getJSONObject("population").getIntValue("max") + count));
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  427 */       if (key.equals("item_cover_set")) {
/*  428 */         String valueStr = blackboard.getJSONObject(0).getString("valueStr");
/*  429 */         String type = roguelikeItems.getJSONObject(valueStr).getString("type");
/*  430 */         int count = blackboard.getJSONObject(1).getIntValue("value");
/*      */         
/*  432 */         if (type.equals("HP")) {
/*  433 */           property.put("hp", Integer.valueOf(count));
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  439 */     current.getJSONObject("player").getJSONArray("pending").remove(0);
/*      */     
/*  441 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  443 */     JSONObject result = new JSONObject(true);
/*  444 */     JSONObject playerDataDelta = new JSONObject(true);
/*  445 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  446 */     JSONObject modified = new JSONObject(true);
/*  447 */     JSONObject jSONObject1 = new JSONObject();
/*  448 */     jSONObject1.put("current", current);
/*  449 */     modified.put("rlv2", jSONObject1);
/*  450 */     playerDataDelta.put("modified", modified);
/*  451 */     result.put("playerDataDelta", playerDataDelta);
/*  452 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/chooseInitialRecruitSet"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject chooseInitialRecruitSet(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  460 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  461 */     LOGGER.info("[/" + clientIp + "] /rlv2/chooseInitialRecruitSet");
/*      */     
/*  463 */     String select = JsonBody.getString("select");
/*      */     
/*  465 */     if (!ArknightsApplication.enableServer) {
/*  466 */       response.setStatus(400);
/*  467 */       JSONObject jSONObject = new JSONObject(true);
/*  468 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  469 */       jSONObject.put("error", "Bad Request");
/*  470 */       jSONObject.put("message", "server is close");
/*  471 */       return jSONObject;
/*      */     } 
/*      */     
/*  474 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  475 */     if (Accounts.size() != 1) {
/*  476 */       JSONObject jSONObject = new JSONObject(true);
/*  477 */       jSONObject.put("result", Integer.valueOf(2));
/*  478 */       jSONObject.put("error", "无法查询到此账户");
/*  479 */       return jSONObject;
/*      */     } 
/*      */     
/*  482 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  484 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  485 */       response.setStatus(500);
/*  486 */       JSONObject jSONObject = new JSONObject(true);
/*  487 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  488 */       jSONObject.put("error", "Bad Request");
/*  489 */       jSONObject.put("message", "error");
/*  490 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  494 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  496 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/*      */     
/*  498 */     if (select.equals("recruit_group_1")) {
/*  499 */       current.getJSONObject("inventory").put("recruit", JSONObject.parseObject("{\"t_1\":{\"index\":\"t_1\",\"id\":\"rogue_1_recruit_ticket_pioneer\",\"state\":0,\"list\":[],\"result\":null,\"ts\":1641721789,\"from\":\"initial\",\"mustExtra\":0,\"needAssist\":false},\"t_2\":{\"index\":\"t_2\",\"id\":\"rogue_1_recruit_ticket_pioneer\",\"state\":0,\"list\":[],\"result\":null,\"ts\":1641721789,\"from\":\"initial\",\"mustExtra\":0,\"needAssist\":false},\"t_3\":{\"index\":\"t_3\",\"id\":\"rogue_1_recruit_ticket_special\",\"state\":0,\"list\":[],\"result\":null,\"ts\":1641721789,\"from\":\"initial\",\"mustExtra\":0,\"needAssist\":false}}"));
/*      */     }
/*  501 */     if (select.equals("recruit_group_2")) {
/*  502 */       current.getJSONObject("inventory").put("recruit", JSONObject.parseObject("{\"t_1\":{\"index\":\"t_1\",\"id\":\"rogue_1_recruit_ticket_tank\",\"state\":0,\"list\":[],\"result\":null,\"ts\":1641722549,\"from\":\"initial\",\"mustExtra\":0,\"needAssist\":false},\"t_2\":{\"index\":\"t_2\",\"id\":\"rogue_1_recruit_ticket_caster\",\"state\":0,\"list\":[],\"result\":null,\"ts\":1641722549,\"from\":\"initial\",\"mustExtra\":0,\"needAssist\":false},\"t_3\":{\"index\":\"t_3\",\"id\":\"rogue_1_recruit_ticket_sniper\",\"state\":0,\"list\":[],\"result\":null,\"ts\":1641722549,\"from\":\"initial\",\"mustExtra\":0,\"needAssist\":false}}"));
/*      */     }
/*  504 */     if (select.equals("recruit_group_3")) {
/*  505 */       current.getJSONObject("inventory").put("recruit", JSONObject.parseObject("{\"t_1\":{\"index\":\"t_1\",\"id\":\"rogue_1_recruit_ticket_warrior\",\"state\":0,\"list\":[],\"result\":null,\"ts\":1641722628,\"from\":\"initial\",\"mustExtra\":0,\"needAssist\":false},\"t_2\":{\"index\":\"t_2\",\"id\":\"rogue_1_recruit_ticket_support\",\"state\":0,\"list\":[],\"result\":null,\"ts\":1641722628,\"from\":\"initial\",\"mustExtra\":0,\"needAssist\":false},\"t_3\":{\"index\":\"t_3\",\"id\":\"rogue_1_recruit_ticket_medic\",\"state\":0,\"list\":[],\"result\":null,\"ts\":1641722628,\"from\":\"initial\",\"mustExtra\":0,\"needAssist\":false}}"));
/*      */     }
/*  507 */     if (select.equals("recruit_group_random")) {
/*  508 */       current.getJSONObject("inventory").put("recruit", JSONObject.parseObject("{\"t_1\":{\"index\":\"t_1\",\"id\":\"rogue_1_recruit_ticket_medic_sp\",\"state\":0,\"list\":[],\"result\":null,\"ts\":1641722698,\"from\":\"initial\",\"mustExtra\":0,\"needAssist\":false},\"t_2\":{\"index\":\"t_2\",\"id\":\"rogue_1_recruit_ticket_all\",\"state\":0,\"list\":[],\"result\":null,\"ts\":1641722698,\"from\":\"initial\",\"mustExtra\":0,\"needAssist\":false},\"t_3\":{\"index\":\"t_3\",\"id\":\"rogue_1_recruit_ticket_medic\",\"state\":0,\"list\":[],\"result\":null,\"ts\":1641722698,\"from\":\"initial\",\"mustExtra\":0,\"needAssist\":false}}"));
/*      */     }
/*      */ 
/*      */     
/*  512 */     current.getJSONObject("player").getJSONArray("pending").remove(0);
/*      */     
/*  514 */     current.getJSONObject("player").getJSONArray("pending").getJSONObject(0).getJSONObject("content").getJSONObject("initRecruit").put("tickets", JSONArray.parseArray("[\"t_1\",\"t_2\",\"t_3\"]"));
/*      */     
/*  516 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  518 */     JSONObject result = new JSONObject(true);
/*  519 */     JSONObject playerDataDelta = new JSONObject(true);
/*  520 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  521 */     JSONObject modified = new JSONObject(true);
/*  522 */     JSONObject jSONObject1 = new JSONObject();
/*  523 */     jSONObject1.put("current", current);
/*  524 */     modified.put("rlv2", jSONObject1);
/*  525 */     playerDataDelta.put("modified", modified);
/*  526 */     result.put("playerDataDelta", playerDataDelta);
/*  527 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/activeRecruitTicket"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject activeRecruitTicket(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  535 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  536 */     LOGGER.info("[/" + clientIp + "] /rlv2/activeRecruitTicket");
/*      */     
/*  538 */     String id = JsonBody.getString("id");
/*      */     
/*  540 */     if (!ArknightsApplication.enableServer) {
/*  541 */       response.setStatus(400);
/*  542 */       JSONObject jSONObject = new JSONObject(true);
/*  543 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  544 */       jSONObject.put("error", "Bad Request");
/*  545 */       jSONObject.put("message", "server is close");
/*  546 */       return jSONObject;
/*      */     } 
/*      */     
/*  549 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  550 */     if (Accounts.size() != 1) {
/*  551 */       JSONObject jSONObject = new JSONObject(true);
/*  552 */       jSONObject.put("result", Integer.valueOf(2));
/*  553 */       jSONObject.put("error", "无法查询到此账户");
/*  554 */       return jSONObject;
/*      */     } 
/*      */     
/*  557 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  559 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  560 */       response.setStatus(500);
/*  561 */       JSONObject jSONObject = new JSONObject(true);
/*  562 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  563 */       jSONObject.put("error", "Bad Request");
/*  564 */       jSONObject.put("message", "error");
/*  565 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  569 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  571 */     JSONArray list = new JSONArray();
/*      */     
/*  573 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/*  574 */     String theme = current.getJSONObject("game").getString("theme");
/*  575 */     JSONObject recruitTickets = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("recruitTickets");
/*  576 */     current.getJSONObject("inventory").getJSONObject("recruit").getJSONObject(id).put("state", Integer.valueOf(1));
/*      */     
/*  578 */     String ticketId = current.getJSONObject("inventory").getJSONObject("recruit").getJSONObject(id).getString("id");
/*      */     
/*  580 */     JSONObject tempChar = JSONObject.parseObject("{\"instId\":\"0\",\"charId\":\"temp\",\"type\":\"THIRD_LOW\",\"evolvePhase\":1,\"level\":55,\"exp\":0,\"favorPoint\":25570,\"potentialRank\":0,\"mainSkillLvl\":7,\"skills\":[],\"defaultSkillIndex\":0,\"skin\":\"temp\",\"upgradeLimited\":false,\"upgradePhase\":0,\"isUpgrade\":false,\"population\":0}");
/*      */     
/*  582 */     JSONObject ticket = recruitTickets.getJSONObject(ticketId);
/*      */     
/*  584 */     JSONArray professionList = ticket.getJSONArray("professionList");
/*  585 */     JSONArray rarityList = ticket.getJSONArray("rarityList");
/*  586 */     JSONArray extraFreeRarity = ticket.getJSONArray("extraFreeRarity");
/*  587 */     JSONArray extraCharIds = ticket.getJSONArray("extraCharIds");
/*  588 */     JSONArray extraFreeList = new JSONArray();
/*  589 */     for (int i = 0; i < extraCharIds.size(); i++) {
/*  590 */       tempChar.put("instId", Integer.valueOf(list.size() + 1));
/*  591 */       tempChar.put("charId", extraCharIds.getString(i));
/*  592 */       tempChar.put("skin", extraCharIds.getString(i) + "#1");
/*  593 */       list.add(JSONObject.parseObject(JSONObject.toJSONString(tempChar, new SerializerFeature[] { SerializerFeature.DisableCircularReferenceDetect })));
/*      */     } 
/*      */     
/*  596 */     String relicsId = current.getJSONObject("inventory").getJSONObject("relic").getJSONObject("r_0").getString("id");
/*  597 */     JSONObject selectRelics = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("relics").getJSONObject(relicsId);
/*  598 */     JSONArray relicsBuffs = selectRelics.getJSONArray("buffs");
/*      */     
/*  600 */     JSONArray dynamicUpdateList = new JSONArray();
/*      */     int j;
/*  602 */     for (j = 0; j < relicsBuffs.size(); j++) {
/*  603 */       if (relicsBuffs.getJSONObject(j).getString("key").equals("dynamic_update")) {
/*  604 */         String band = relicsBuffs.getJSONObject(j).getJSONArray("blackboard").getJSONObject(0).getString("valueStr").substring("recruit_upgrade_".length()).toUpperCase(Locale.ROOT);
/*  605 */         dynamicUpdateList.add(band);
/*      */       } 
/*      */     } 
/*      */     
/*  609 */     for (Map.Entry entry : UserSyncData.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/*  610 */       JSONObject originalChar = JSONObject.parseObject(entry.getValue().toString());
/*  611 */       JSONObject userChar = JSONObject.parseObject(entry.getValue().toString());
/*  612 */       String charId = userChar.getString("charId");
/*  613 */       String charProfession = ArknightsApplication.characterJson.getJSONObject(charId).getString("profession");
/*      */       
/*  615 */       if (professionList.contains(charProfession)) {
/*      */         
/*  617 */         int charRarity = ArknightsApplication.characterJson.getJSONObject(charId).getIntValue("rarity");
/*      */         
/*  619 */         if (rarityList.contains(Integer.valueOf(charRarity))) {
/*      */           
/*  621 */           int charPopulation = 0;
/*      */           
/*  623 */           if (originalChar.getIntValue("evolvePhase") != 0 && 
/*  624 */             originalChar.getIntValue("evolvePhase") == 2) {
/*  625 */             userChar.put("evolvePhase", Integer.valueOf(1));
/*      */           }
/*      */           
/*  628 */           if (userChar.getIntValue("evolvePhase") == 1) {
/*  629 */             if (userChar.getJSONArray("skills").size() == 1) {
/*  630 */               userChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/*      */             }
/*  632 */             if (userChar.getJSONArray("skills").size() == 2) {
/*  633 */               userChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/*  634 */               userChar.getJSONArray("skills").getJSONObject(1).put("specializeLevel", Integer.valueOf(0));
/*      */             } 
/*  636 */             if (userChar.getJSONArray("skills").size() == 3) {
/*  637 */               userChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/*  638 */               userChar.getJSONArray("skills").getJSONObject(1).put("specializeLevel", Integer.valueOf(0));
/*  639 */               userChar.getJSONArray("skills").getJSONObject(2).put("specializeLevel", Integer.valueOf(0));
/*  640 */               userChar.getJSONArray("skills").getJSONObject(2).put("unlock", Integer.valueOf(0));
/*      */             } 
/*      */           } 
/*      */           
/*  644 */           if (charRarity == 3) {
/*  645 */             charPopulation = 2;
/*  646 */             if (originalChar.getIntValue("level") > 60) {
/*  647 */               userChar.put("level", Integer.valueOf(60));
/*      */             }
/*      */           } 
/*  650 */           if (charRarity == 4) {
/*  651 */             charPopulation = 3;
/*  652 */             if (originalChar.getIntValue("level") > 70) {
/*  653 */               userChar.put("level", Integer.valueOf(70));
/*      */             }
/*      */           } 
/*  656 */           if (charRarity == 5) {
/*  657 */             charPopulation = 6;
/*  658 */             if (originalChar.getIntValue("level") > 80) {
/*  659 */               userChar.put("level", Integer.valueOf(80));
/*      */             }
/*      */           } 
/*      */           
/*  663 */           userChar.put("isUpgrade", Boolean.valueOf(false));
/*  664 */           userChar.put("upgradePhase", Integer.valueOf(0));
/*  665 */           userChar.put("upgradeLimited", Boolean.valueOf(true));
/*  666 */           if (charRarity >= 3) {
/*  667 */             userChar.put("upgradeLimited", Boolean.valueOf(false));
/*  668 */             if (dynamicUpdateList.contains(charProfession)) {
/*  669 */               userChar.put("upgradeLimited", Boolean.valueOf(true));
/*      */               
/*  671 */               if (originalChar.getIntValue("evolvePhase") != 0 && 
/*  672 */                 originalChar.getIntValue("evolvePhase") == 2) {
/*  673 */                 userChar.put("evolvePhase", Integer.valueOf(2));
/*      */               }
/*      */ 
/*      */               
/*  677 */               userChar.put("skills", originalChar.getJSONArray("skills"));
/*  678 */               if (userChar.getIntValue("evolvePhase") == 1) {
/*  679 */                 if (userChar.getJSONArray("skills").size() == 1) {
/*  680 */                   userChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/*      */                 }
/*  682 */                 if (userChar.getJSONArray("skills").size() == 2) {
/*  683 */                   userChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/*  684 */                   userChar.getJSONArray("skills").getJSONObject(1).put("specializeLevel", Integer.valueOf(0));
/*      */                 } 
/*  686 */                 if (userChar.getJSONArray("skills").size() == 3) {
/*  687 */                   userChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/*  688 */                   userChar.getJSONArray("skills").getJSONObject(1).put("specializeLevel", Integer.valueOf(0));
/*  689 */                   userChar.getJSONArray("skills").getJSONObject(2).put("specializeLevel", Integer.valueOf(0));
/*  690 */                   userChar.getJSONArray("skills").getJSONObject(2).put("unlock", Integer.valueOf(0));
/*      */                 } 
/*      */               } 
/*  693 */               userChar.put("level", Integer.valueOf(originalChar.getIntValue("level")));
/*  694 */               userChar.put("upgradePhase", Integer.valueOf(originalChar.getIntValue("evolvePhase")));
/*      */             } 
/*      */           } 
/*      */           
/*  698 */           userChar.put("rarity", Integer.valueOf(charRarity));
/*  699 */           userChar.put("originalId", originalChar.getString("instId"));
/*  700 */           userChar.put("profession", charProfession);
/*  701 */           userChar.put("instId", Integer.valueOf(list.size() + 1));
/*  702 */           userChar.put("population", Integer.valueOf(charPopulation));
/*  703 */           userChar.put("type", "NORMAL");
/*      */           
/*  705 */           Boolean upgrade = Boolean.valueOf(false);
/*  706 */           for (Map.Entry Entry : current.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/*  707 */             JSONObject troopChar = JSONObject.parseObject(Entry.getValue().toString());
/*      */             
/*  709 */             if (troopChar.getString("charId").equals(charId)) {
/*      */               
/*  711 */               if (!troopChar.getBooleanValue("upgradeLimited")) {
/*      */                 
/*  713 */                 if (charRarity == 3) {
/*  714 */                   charPopulation = 1;
/*      */                 }
/*  716 */                 if (charRarity == 4) {
/*  717 */                   charPopulation = 2;
/*      */                 }
/*  719 */                 if (charRarity == 5) {
/*  720 */                   charPopulation = 3;
/*      */                 }
/*      */                 
/*  723 */                 if (originalChar.getIntValue("evolvePhase") != 0 && 
/*  724 */                   originalChar.getIntValue("evolvePhase") == 2) {
/*  725 */                   userChar.put("evolvePhase", Integer.valueOf(2));
/*      */                 }
/*      */                 
/*  728 */                 userChar.put("isUpgrade", Boolean.valueOf(true));
/*  729 */                 userChar.put("upgradeLimited", Boolean.valueOf(true));
/*  730 */                 userChar.put("population", Integer.valueOf(charPopulation));
/*  731 */                 userChar.put("skills", originalChar.getJSONArray("skills"));
/*  732 */                 userChar.put("level", Integer.valueOf(originalChar.getIntValue("level")));
/*  733 */                 userChar.put("upgradePhase", Integer.valueOf(originalChar.getIntValue("evolvePhase")));
/*      */                 continue;
/*      */               } 
/*  736 */               upgrade = Boolean.valueOf(true);
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/*  741 */           if (!upgrade.booleanValue()) {
/*  742 */             list.add(userChar);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  748 */     for (j = 0; j < list.size(); j++) {
/*  749 */       if (extraFreeRarity.contains(Integer.valueOf(list.getJSONObject(j).getIntValue("rarity")))) {
/*  750 */         extraFreeList.add(Integer.valueOf(j));
/*      */       }
/*      */     } 
/*  753 */     Collections.shuffle((List<?>)extraFreeList);
/*      */     
/*  755 */     if (extraFreeList.size() != 0) {
/*      */       
/*  757 */       JSONObject userChar = list.getJSONObject(extraFreeList.getIntValue(0));
/*  758 */       int charRarity = userChar.getIntValue("rarity");
/*      */       
/*  760 */       userChar.put("population", Integer.valueOf(0));
/*  761 */       if (charRarity == 3) {
/*  762 */         userChar.put("level", Integer.valueOf(60));
/*      */       }
/*  764 */       if (charRarity == 4) {
/*  765 */         userChar.put("level", Integer.valueOf(70));
/*      */       }
/*  767 */       if (charRarity == 5) {
/*  768 */         userChar.put("level", Integer.valueOf(80));
/*      */       }
/*  770 */       userChar.put("potentialRank", Integer.valueOf(5));
/*  771 */       userChar.put("mainSkillLvl", Integer.valueOf(7));
/*  772 */       userChar.put("favorPoint", Integer.valueOf(25570));
/*  773 */       userChar.put("evolvePhase", Integer.valueOf(1));
/*  774 */       userChar.put("type", "FREE");
/*      */     } 
/*      */     
/*  777 */     current.getJSONObject("inventory").getJSONObject("recruit").getJSONObject(id).put("list", list);
/*      */ 
/*      */ 
/*      */     
/*  781 */     JSONObject pending = new JSONObject();
/*      */ 
/*      */ 
/*      */     
/*  785 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  787 */     JSONObject result = new JSONObject(true);
/*  788 */     JSONObject playerDataDelta = new JSONObject(true);
/*  789 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  790 */     JSONObject modified = new JSONObject(true);
/*  791 */     JSONObject jSONObject1 = new JSONObject();
/*  792 */     jSONObject1.put("current", current);
/*  793 */     modified.put("rlv2", jSONObject1);
/*  794 */     playerDataDelta.put("modified", modified);
/*  795 */     result.put("playerDataDelta", playerDataDelta);
/*  796 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/recruitChar"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject recruitChar(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  804 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  805 */     LOGGER.info("[/" + clientIp + "] /rlv2/recruitChar");
/*      */     
/*  807 */     String ticketIndex = JsonBody.getString("ticketIndex");
/*  808 */     int optionId = JsonBody.getIntValue("optionId");
/*      */     
/*  810 */     if (!ArknightsApplication.enableServer) {
/*  811 */       response.setStatus(400);
/*  812 */       JSONObject jSONObject = new JSONObject(true);
/*  813 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  814 */       jSONObject.put("error", "Bad Request");
/*  815 */       jSONObject.put("message", "server is close");
/*  816 */       return jSONObject;
/*      */     } 
/*      */     
/*  819 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  820 */     if (Accounts.size() != 1) {
/*  821 */       JSONObject jSONObject = new JSONObject(true);
/*  822 */       jSONObject.put("result", Integer.valueOf(2));
/*  823 */       jSONObject.put("error", "无法查询到此账户");
/*  824 */       return jSONObject;
/*      */     } 
/*      */     
/*  827 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  829 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  830 */       response.setStatus(500);
/*  831 */       JSONObject jSONObject = new JSONObject(true);
/*  832 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  833 */       jSONObject.put("error", "Bad Request");
/*  834 */       jSONObject.put("message", "error");
/*  835 */       return jSONObject;
/*      */     } 
/*      */     
/*  838 */     JSONArray chars = new JSONArray();
/*      */ 
/*      */     
/*  841 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  843 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/*  844 */     String theme = current.getJSONObject("game").getString("theme");
/*      */     
/*  846 */     JSONObject ticket = current.getJSONObject("inventory").getJSONObject("recruit").getJSONObject(ticketIndex);
/*      */     
/*  848 */     JSONObject optChar = ticket.getJSONArray("list").getJSONObject(optionId - 1);
/*      */     
/*  850 */     if (optChar.getBooleanValue("isUpgrade")) {
/*  851 */       for (Map.Entry Entry : current.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/*  852 */         JSONObject troopChar = JSONObject.parseObject(Entry.getValue().toString());
/*  853 */         if (troopChar.getString("charId").equals(optChar.getString("charId"))) {
/*  854 */           optChar.put("instId", Integer.valueOf(troopChar.getIntValue("instId")));
/*  855 */           current.getJSONObject("troop").getJSONObject("chars").put(troopChar.getString("instId"), optChar);
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } else {
/*  860 */       optChar.put("instId", Integer.valueOf(current.getJSONObject("troop").getJSONObject("chars").size() + 1));
/*  861 */       current.getJSONObject("troop").getJSONObject("chars").put(optChar.getString("instId"), optChar);
/*      */     } 
/*      */     
/*  864 */     chars.add(optChar);
/*      */     
/*  866 */     ticket.put("list", new JSONArray());
/*  867 */     ticket.put("result", JSONObject.parseObject(JSONObject.toJSONString(optChar, new SerializerFeature[] { SerializerFeature.DisableCircularReferenceDetect })));
/*  868 */     ticket.put("state", Integer.valueOf(2));
/*      */     
/*  870 */     current.getJSONObject("player").getJSONObject("property").getJSONObject("population").put("cost", Integer.valueOf(current.getJSONObject("player").getJSONObject("property").getJSONObject("population").getIntValue("cost") + optChar.getIntValue("population")));
/*      */     
/*  872 */     JSONArray pending = current.getJSONObject("player").getJSONArray("pending");
/*  873 */     JSONArray newPending = new JSONArray();
/*  874 */     Iterator<Object> iterator = pending.iterator();
/*      */     
/*  876 */     while (iterator.hasNext()) {
/*  877 */       JSONObject event = (JSONObject)iterator.next();
/*      */       
/*  879 */       if (event.getString("type").equals("RECRUIT")) {
/*  880 */         if (event.getJSONObject("content").getJSONObject("recruit").getString("ticket").equals(ticketIndex)) {
/*  881 */           int pendingIndex = event.getJSONObject("content").getJSONObject("recruit").getIntValue("pendingIndex");
/*  882 */           int rewardsIndex = event.getJSONObject("content").getJSONObject("recruit").getIntValue("rewardsIndex");
/*  883 */           if (pending.getJSONObject(pendingIndex).getString("type").equals("BATTLE_REWARD")) {
/*  884 */             JSONObject battleReward = pending.getJSONObject(pendingIndex).getJSONObject("content").getJSONObject("battleReward");
/*      */ 
/*      */             
/*  887 */             if (battleReward.getJSONArray("rewards").getJSONObject(rewardsIndex).getBooleanValue("isRelic")) {
/*  888 */               JSONObject relic = new JSONObject();
/*  889 */               String relicIndex = "r_" + current.getJSONObject("inventory").getJSONObject("relic").size() + '\001';
/*  890 */               relic.put("count", Integer.valueOf(1));
/*  891 */               relic.put("id", battleReward.getJSONArray("rewards").getJSONObject(rewardsIndex).getString("relicId"));
/*  892 */               relic.put("index", relicIndex);
/*  893 */               relic.put("ts", Long.valueOf((new Date()).getTime() / 1000L));
/*  894 */               current.getJSONObject("inventory").getJSONObject("relic").put(relicIndex, relic);
/*      */             } 
/*      */           }  continue;
/*      */         } 
/*  898 */         current.getJSONObject("inventory").getJSONObject("recruit").remove(event.getJSONObject("content").getJSONObject("recruit").getString("ticket"));
/*      */         continue;
/*      */       } 
/*  901 */       newPending.add(event);
/*      */     } 
/*      */ 
/*      */     
/*  905 */     current.getJSONObject("player").put("pending", newPending);
/*      */ 
/*      */ 
/*      */     
/*  909 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  911 */     JSONObject result = new JSONObject(true);
/*  912 */     JSONObject playerDataDelta = new JSONObject(true);
/*  913 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  914 */     JSONObject modified = new JSONObject(true);
/*  915 */     JSONObject jSONObject1 = new JSONObject();
/*  916 */     jSONObject1.put("current", current);
/*  917 */     modified.put("rlv2", jSONObject1);
/*  918 */     playerDataDelta.put("modified", modified);
/*  919 */     result.put("playerDataDelta", playerDataDelta);
/*  920 */     result.put("chars", chars);
/*  921 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/closeRecruitTicket"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject closeRecruitTicket(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  929 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  930 */     LOGGER.info("[/" + clientIp + "] /rlv2/closeRecruitTicket");
/*      */     
/*  932 */     String ticketIndex = JsonBody.getString("id");
/*      */     
/*  934 */     if (!ArknightsApplication.enableServer) {
/*  935 */       response.setStatus(400);
/*  936 */       JSONObject jSONObject = new JSONObject(true);
/*  937 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  938 */       jSONObject.put("error", "Bad Request");
/*  939 */       jSONObject.put("message", "server is close");
/*  940 */       return jSONObject;
/*      */     } 
/*      */     
/*  943 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  944 */     if (Accounts.size() != 1) {
/*  945 */       JSONObject jSONObject = new JSONObject(true);
/*  946 */       jSONObject.put("result", Integer.valueOf(2));
/*  947 */       jSONObject.put("error", "无法查询到此账户");
/*  948 */       return jSONObject;
/*      */     } 
/*      */     
/*  951 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  953 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  954 */       response.setStatus(500);
/*  955 */       JSONObject jSONObject = new JSONObject(true);
/*  956 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  957 */       jSONObject.put("error", "Bad Request");
/*  958 */       jSONObject.put("message", "error");
/*  959 */       return jSONObject;
/*      */     } 
/*      */     
/*  962 */     JSONArray chars = new JSONArray();
/*      */ 
/*      */     
/*  965 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*  966 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/*  967 */     String theme = current.getJSONObject("game").getString("theme");
/*  968 */     JSONObject ticket = current.getJSONObject("inventory").getJSONObject("recruit").getJSONObject(ticketIndex);
/*  969 */     JSONArray pending = current.getJSONObject("player").getJSONArray("pending");
/*      */     
/*  971 */     ticket.put("state", Integer.valueOf(2));
/*  972 */     ticket.put("result", null);
/*  973 */     ticket.put("list", new JSONArray());
/*      */     
/*  975 */     for (int i = 0; i < pending.size(); i++) {
/*  976 */       if (pending.getJSONObject(i).getString("type").equals("RECRUIT") && 
/*  977 */         pending.getJSONObject(i).getJSONObject("content").getJSONObject("recruit").getString("ticket").equals(ticketIndex)) {
/*  978 */         pending.remove(i);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  985 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*  987 */     JSONObject result = new JSONObject(true);
/*  988 */     JSONObject playerDataDelta = new JSONObject(true);
/*  989 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  990 */     JSONObject modified = new JSONObject(true);
/*  991 */     JSONObject jSONObject1 = new JSONObject();
/*  992 */     jSONObject1.put("current", current);
/*  993 */     modified.put("rlv2", jSONObject1);
/*  994 */     playerDataDelta.put("modified", modified);
/*  995 */     result.put("playerDataDelta", playerDataDelta);
/*  996 */     result.put("chars", chars);
/*  997 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/finishEvent"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject finishEvent(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 1005 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 1006 */     LOGGER.info("[/" + clientIp + "] /rlv2/finishEvent");
/*      */ 
/*      */     
/* 1009 */     if (!ArknightsApplication.enableServer) {
/* 1010 */       response.setStatus(400);
/* 1011 */       JSONObject jSONObject = new JSONObject(true);
/* 1012 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 1013 */       jSONObject.put("error", "Bad Request");
/* 1014 */       jSONObject.put("message", "server is close");
/* 1015 */       return jSONObject;
/*      */     } 
/*      */     
/* 1018 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 1019 */     if (Accounts.size() != 1) {
/* 1020 */       JSONObject jSONObject = new JSONObject(true);
/* 1021 */       jSONObject.put("result", Integer.valueOf(2));
/* 1022 */       jSONObject.put("error", "无法查询到此账户");
/* 1023 */       return jSONObject;
/*      */     } 
/*      */     
/* 1026 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 1028 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 1029 */       response.setStatus(500);
/* 1030 */       JSONObject jSONObject = new JSONObject(true);
/* 1031 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 1032 */       jSONObject.put("error", "Bad Request");
/* 1033 */       jSONObject.put("message", "error");
/* 1034 */       return jSONObject;
/*      */     } 
/*      */     
/* 1037 */     JSONArray chars = new JSONArray();
/*      */     
/* 1039 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 1041 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/*      */     
/* 1043 */     for (int n = 0; n < current.getJSONObject("player").getJSONArray("pending").size(); n++) {
/*      */       
/* 1045 */       if (current.getJSONObject("player").getJSONArray("pending").size() == 1 && 
/* 1046 */         current.getJSONObject("player").getJSONArray("pending").getJSONObject(n).getString("type").equals("GAME_INIT_RECRUIT")) {
/* 1047 */         current.getJSONObject("player").getJSONObject("cursor").put("zone", Integer.valueOf(1));
/* 1048 */         current.getJSONObject("player").put("pending", new JSONArray());
/* 1049 */         current.getJSONObject("player").put("state", "WAIT_MOVE");
/*      */         
/* 1051 */         current.getJSONObject("map").put("zones", ArknightsApplication.roguelike.getJSONObject("rlv2").getJSONObject("customMap").getJSONObject("zones"));
/*      */       } 
/*      */ 
/*      */       
/* 1055 */       if (current.getJSONObject("player").getJSONArray("pending").size() != 0 && 
/* 1056 */         current.getJSONObject("player").getJSONArray("pending").getJSONObject(n).getString("type").equals("GAME_INIT_MODE_RELIC")) {
/*      */         
/* 1058 */         String theme = current.getJSONObject("game").getString("theme");
/* 1059 */         JSONObject property = current.getJSONObject("player").getJSONObject("property");
/* 1060 */         JSONObject roguelikeItems = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("items");
/*      */         
/* 1062 */         JSONArray items = current.getJSONObject("player").getJSONArray("pending").getJSONObject(n).getJSONObject("content").getJSONObject("initModeRelic").getJSONArray("items");
/*      */         
/* 1064 */         for (int i = 0; i < items.size(); i++) {
/*      */           
/* 1066 */           Boolean UPGRADE = Boolean.valueOf(false);
/*      */           
/* 1068 */           String itemId = items.getString(i);
/*      */ 
/*      */           
/* 1071 */           if (ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("relics").containsKey(itemId)) {
/*      */ 
/*      */ 
/*      */             
/* 1075 */             JSONObject selectRelics = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("relics").getJSONObject(itemId);
/*      */             
/* 1077 */             JSONArray buffs = selectRelics.getJSONArray("buffs");
/*      */             
/* 1079 */             for (int m = 0; m < buffs.size(); m++) {
/* 1080 */               String key = buffs.getJSONObject(m).getString("key");
/* 1081 */               JSONArray blackboard = buffs.getJSONObject(m).getJSONArray("blackboard");
/*      */ 
/*      */               
/* 1084 */               if (key.equals("level_life_point_add")) {
/* 1085 */                 current.getJSONObject("buff").put("tmpHP", Integer.valueOf(current.getJSONObject("buff").getIntValue("tmpHP") + blackboard.getJSONObject(0).getIntValue("value")));
/*      */               }
/*      */ 
/*      */               
/* 1089 */               if (key.equals("immediate_reward")) {
/*      */                 
/* 1091 */                 String valueStr = blackboard.getJSONObject(0).getString("valueStr");
/* 1092 */                 String buffType = roguelikeItems.getJSONObject(valueStr).getString("type");
/* 1093 */                 int count = blackboard.getJSONObject(1).getIntValue("value");
/*      */                 
/* 1095 */                 if (buffType.equals("HP")) {
/* 1096 */                   property.put("hp", Integer.valueOf(property.getIntValue("hp") + count));
/*      */                 }
/* 1098 */                 if (buffType.equals("GOLD")) {
/* 1099 */                   property.put("gold", Integer.valueOf(property.getIntValue("gold") + count));
/*      */                 }
/* 1101 */                 if (buffType.equals("SQUAD_CAPACITY")) {
/* 1102 */                   property.put("capacity", Integer.valueOf(property.getIntValue("capacity") + count));
/*      */                 }
/* 1104 */                 if (buffType.equals("POPULATION")) {
/* 1105 */                   property.getJSONObject("population").put("max", Integer.valueOf(property.getJSONObject("population").getIntValue("max") + count));
/*      */                 }
/*      */ 
/*      */                 
/* 1109 */                 if (buffType.equals("UPGRADE_TICKET")) {
/* 1110 */                   UPGRADE = Boolean.valueOf(true);
/*      */                   
/* 1112 */                   int recruitIndex = current.getJSONObject("inventory").getJSONObject("recruit").size() + 1;
/* 1113 */                   String id = "t_" + recruitIndex;
/* 1114 */                   JSONObject recruit = new JSONObject();
/* 1115 */                   recruit.put("from", "buff");
/* 1116 */                   recruit.put("id", valueStr);
/* 1117 */                   recruit.put("index", "t_" + recruitIndex);
/* 1118 */                   recruit.put("mustExtra", Integer.valueOf(0));
/* 1119 */                   recruit.put("needAssist", Boolean.valueOf(false));
/* 1120 */                   recruit.put("state", Integer.valueOf(0));
/* 1121 */                   recruit.put("result", null);
/* 1122 */                   recruit.put("ts", Long.valueOf((new Date()).getTime() / 1000L));
/* 1123 */                   current.getJSONObject("inventory").getJSONObject("recruit").put("t_" + recruitIndex, recruit);
/*      */                   
/* 1125 */                   JSONArray list = new JSONArray();
/*      */                   
/* 1127 */                   JSONObject upgradeTickets = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("upgradeTickets");
/* 1128 */                   current.getJSONObject("inventory").getJSONObject("recruit").getJSONObject(id).put("state", Integer.valueOf(1));
/*      */                   
/* 1130 */                   String ticketId = current.getJSONObject("inventory").getJSONObject("recruit").getJSONObject(id).getString("id");
/*      */                   
/* 1132 */                   JSONObject ticket = upgradeTickets.getJSONObject(ticketId);
/*      */                   
/* 1134 */                   JSONArray professionList = ticket.getJSONArray("professionList");
/* 1135 */                   JSONArray rarityList = ticket.getJSONArray("rarityList");
/*      */                   
/* 1137 */                   for (Map.Entry Entry : current.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 1138 */                     JSONObject troopChar = JSONObject.parseObject(Entry.getValue().toString());
/* 1139 */                     String profession = troopChar.getString("profession");
/* 1140 */                     int rarity = troopChar.getIntValue("rarity");
/*      */                     
/* 1142 */                     if (professionList.contains(profession) && 
/* 1143 */                       rarityList.contains(Integer.valueOf(rarity)) && 
/* 1144 */                       !troopChar.getBooleanValue("upgradeLimited")) {
/*      */                       
/* 1146 */                       JSONObject originalChar = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(troopChar.getString("originalId"));
/*      */                       
/* 1148 */                       if (originalChar.getIntValue("evolvePhase") != 0 && 
/* 1149 */                         originalChar.getIntValue("evolvePhase") == 2) {
/* 1150 */                         troopChar.put("evolvePhase", Integer.valueOf(2));
/*      */                       }
/*      */                       
/* 1153 */                       troopChar.put("isUpgrade", Boolean.valueOf(true));
/* 1154 */                       troopChar.put("upgradeLimited", Boolean.valueOf(true));
/* 1155 */                       troopChar.put("population", Integer.valueOf(0));
/* 1156 */                       troopChar.put("skills", originalChar.getJSONArray("skills"));
/* 1157 */                       troopChar.put("level", Integer.valueOf(originalChar.getIntValue("level")));
/* 1158 */                       troopChar.put("upgradePhase", Integer.valueOf(originalChar.getIntValue("evolvePhase")));
/*      */                       
/* 1160 */                       list.add(troopChar);
/*      */                     } 
/*      */                   } 
/*      */ 
/*      */ 
/*      */                   
/* 1166 */                   recruit.put("list", list);
/*      */                   
/* 1168 */                   JSONObject RECRUIT = new JSONObject();
/* 1169 */                   JSONObject content = new JSONObject();
/*      */                   
/* 1171 */                   JSONObject recruitTicket = new JSONObject();
/* 1172 */                   recruitTicket.put("ticket", id);
/* 1173 */                   recruitTicket.put("pendingIndex", Integer.valueOf(0));
/* 1174 */                   recruitTicket.put("rewardsIndex", Integer.valueOf(0));
/* 1175 */                   recruitTicket.put("isRelic", Boolean.valueOf(true));
/* 1176 */                   recruitTicket.put("relicId", itemId);
/*      */                   
/* 1178 */                   content.put("recruit", recruitTicket);
/* 1179 */                   RECRUIT.put("content", content);
/* 1180 */                   RECRUIT.put("index", "e_" + current.getJSONObject("player").getJSONArray("pending").size() + '\001');
/* 1181 */                   RECRUIT.put("type", "RECRUIT");
/* 1182 */                   current.getJSONObject("player").getJSONArray("pending").add(0, RECRUIT);
/*      */                 } 
/*      */                 
/* 1185 */                 if (!UPGRADE.booleanValue()) {
/* 1186 */                   JSONObject relic = new JSONObject();
/* 1187 */                   String relicIndex = "r_" + current.getJSONObject("inventory").getJSONObject("relic").size() + '\001';
/* 1188 */                   relic.put("count", Integer.valueOf(1));
/* 1189 */                   relic.put("id", itemId);
/* 1190 */                   relic.put("index", relicIndex);
/* 1191 */                   relic.put("ts", Long.valueOf((new Date()).getTime() / 1000L));
/* 1192 */                   current.getJSONObject("inventory").getJSONObject("relic").put(relicIndex, relic);
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/* 1197 */         }  current.getJSONObject("player").getJSONArray("pending").remove(0);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1202 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 1204 */     JSONObject result = new JSONObject(true);
/* 1205 */     JSONObject playerDataDelta = new JSONObject(true);
/* 1206 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 1207 */     JSONObject modified = new JSONObject(true);
/* 1208 */     JSONObject jSONObject1 = new JSONObject();
/* 1209 */     jSONObject1.put("current", current);
/* 1210 */     modified.put("rlv2", jSONObject1);
/* 1211 */     playerDataDelta.put("modified", modified);
/* 1212 */     result.put("playerDataDelta", playerDataDelta);
/* 1213 */     result.put("chars", chars);
/* 1214 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/normal/unlockBuff"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject unlockBuff(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 1221 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 1222 */     LOGGER.info("[/" + clientIp + "] /rlv2/normal/unlockBuff");
/*      */     
/* 1224 */     String theme = JsonBody.getString("theme");
/* 1225 */     String buff = JsonBody.getString("buff");
/*      */     
/* 1227 */     if (!ArknightsApplication.enableServer) {
/* 1228 */       response.setStatus(400);
/* 1229 */       JSONObject jSONObject = new JSONObject(true);
/* 1230 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 1231 */       jSONObject.put("error", "Bad Request");
/* 1232 */       jSONObject.put("message", "server is close");
/* 1233 */       return jSONObject;
/*      */     } 
/*      */     
/* 1236 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 1237 */     if (Accounts.size() != 1) {
/* 1238 */       JSONObject jSONObject = new JSONObject(true);
/* 1239 */       jSONObject.put("result", Integer.valueOf(2));
/* 1240 */       jSONObject.put("error", "无法查询到此账户");
/* 1241 */       return jSONObject;
/*      */     } 
/*      */     
/* 1244 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 1246 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 1247 */       response.setStatus(500);
/* 1248 */       JSONObject jSONObject = new JSONObject(true);
/* 1249 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 1250 */       jSONObject.put("error", "Bad Request");
/* 1251 */       jSONObject.put("message", "error");
/* 1252 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/* 1256 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 1258 */     JSONObject outer = UserSyncData.getJSONObject("rlv2").getJSONObject("outer").getJSONObject(theme);
/*      */     
/* 1260 */     JSONObject outBuff = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("developments").getJSONObject(buff);
/*      */     
/* 1262 */     outer.getJSONObject("buff").put("pointCost", Integer.valueOf(outer.getJSONObject("buff").getIntValue("pointCost") + outBuff.getIntValue("tokenCost")));
/*      */     
/* 1264 */     if (outer.getJSONObject("buff").getIntValue("pointCost") > outer.getJSONObject("buff").getIntValue("pointOwned")) {
/* 1265 */       JSONObject jSONObject = new JSONObject(true);
/* 1266 */       jSONObject.put("msg", "error");
/* 1267 */       return jSONObject;
/*      */     } 
/*      */     
/* 1270 */     outer.getJSONObject("buff").getJSONObject("unlocked").put(buff, Integer.valueOf(1));
/*      */     
/* 1272 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 1274 */     JSONObject result = new JSONObject(true);
/* 1275 */     JSONObject playerDataDelta = new JSONObject(true);
/* 1276 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 1277 */     JSONObject modified = new JSONObject(true);
/* 1278 */     JSONObject jSONObject1 = new JSONObject();
/* 1279 */     JSONObject tmp = new JSONObject();
/* 1280 */     JSONObject tmp2 = new JSONObject();
/* 1281 */     tmp2.put("buff", outer.getJSONObject("buff"));
/* 1282 */     tmp.put(theme, tmp2);
/* 1283 */     jSONObject1.put("outer", tmp);
/* 1284 */     modified.put("rlv2", jSONObject1);
/* 1285 */     playerDataDelta.put("modified", modified);
/* 1286 */     result.put("playerDataDelta", playerDataDelta);
/* 1287 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/moveAndBattleStart"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject moveAndBattleStart(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 1295 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 1296 */     LOGGER.info("[/" + clientIp + "] /rlv2/moveAndBattleStart");
/*      */     
/* 1298 */     JSONObject moveTo = JsonBody.getJSONObject("to");
/*      */     
/* 1300 */     if (!ArknightsApplication.enableServer) {
/* 1301 */       response.setStatus(400);
/* 1302 */       JSONObject jSONObject = new JSONObject(true);
/* 1303 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 1304 */       jSONObject.put("error", "Bad Request");
/* 1305 */       jSONObject.put("message", "server is close");
/* 1306 */       return jSONObject;
/*      */     } 
/*      */     
/* 1309 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 1310 */     if (Accounts.size() != 1) {
/* 1311 */       JSONObject jSONObject = new JSONObject(true);
/* 1312 */       jSONObject.put("result", Integer.valueOf(2));
/* 1313 */       jSONObject.put("error", "无法查询到此账户");
/* 1314 */       return jSONObject;
/*      */     } 
/*      */     
/* 1317 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 1319 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 1320 */       response.setStatus(500);
/* 1321 */       JSONObject jSONObject = new JSONObject(true);
/* 1322 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 1323 */       jSONObject.put("error", "Bad Request");
/* 1324 */       jSONObject.put("message", "error");
/* 1325 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/* 1329 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 1331 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/*      */     
/* 1333 */     current.getJSONObject("player").put("pending", new JSONArray());
/* 1334 */     current.getJSONObject("player").put("state", "PENDING");
/* 1335 */     current.getJSONObject("player").getJSONObject("cursor").put("position", moveTo);
/* 1336 */     current.getJSONObject("player").getJSONArray("trace").add(current.getJSONObject("player").getJSONObject("cursor"));
/*      */     
/* 1338 */     JSONObject tmp = new JSONObject();
/* 1339 */     JSONObject content = new JSONObject();
/* 1340 */     JSONObject battle = new JSONObject();
/*      */     
/* 1342 */     battle.put("chestCnt", Integer.valueOf(10));
/* 1343 */     battle.put("goldTrapCnt", Integer.valueOf(10));
/* 1344 */     battle.put("state", Integer.valueOf(1));
/* 1345 */     battle.put("tmpChar", new JSONArray());
/*      */     
/* 1347 */     JSONArray unKeepBuff = new JSONArray();
/*      */ 
/*      */     
/* 1350 */     JSONArray customBuff = ArknightsApplication.roguelike.getJSONObject("rlv2").getJSONArray("customRelic");
/* 1351 */     for (int i = 0; i < customBuff.size(); i++) {
/* 1352 */       if (customBuff.getJSONObject(i).getBooleanValue("enableRelic")) {
/* 1353 */         JSONArray buffs = customBuff.getJSONObject(i).getJSONArray("buffs");
/* 1354 */         for (int n = 0; n < buffs.size(); n++) {
/* 1355 */           unKeepBuff.add(buffs.getJSONObject(n));
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1360 */     battle.put("unKeepBuff", unKeepBuff);
/*      */     
/* 1362 */     content.put("battle", battle);
/*      */     
/* 1364 */     tmp.put("content", content);
/* 1365 */     tmp.put("index", "e_" + current.getJSONObject("player").getJSONArray("pending").size() + '\001');
/* 1366 */     tmp.put("type", "BATTLE");
/*      */     
/* 1368 */     current.getJSONObject("player").getJSONArray("pending").add(tmp);
/*      */     
/* 1370 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 1372 */     JSONObject result = new JSONObject(true);
/* 1373 */     JSONObject playerDataDelta = new JSONObject(true);
/* 1374 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 1375 */     JSONObject modified = new JSONObject(true);
/* 1376 */     modified.put("rlv2", UserSyncData.getJSONObject("rlv2"));
/* 1377 */     playerDataDelta.put("modified", modified);
/* 1378 */     result.put("playerDataDelta", playerDataDelta);
/* 1379 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/battleFinish"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject battleFinish(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 1387 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 1388 */     LOGGER.info("[/" + clientIp + "] /rlv2/battleFinish");
/*      */     
/* 1390 */     if (!ArknightsApplication.enableServer) {
/* 1391 */       response.setStatus(400);
/* 1392 */       JSONObject jSONObject = new JSONObject(true);
/* 1393 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 1394 */       jSONObject.put("error", "Bad Request");
/* 1395 */       jSONObject.put("message", "server is close");
/* 1396 */       return jSONObject;
/*      */     } 
/*      */     
/* 1399 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 1400 */     if (Accounts.size() != 1) {
/* 1401 */       JSONObject jSONObject = new JSONObject(true);
/* 1402 */       jSONObject.put("result", Integer.valueOf(2));
/* 1403 */       jSONObject.put("error", "无法查询到此账户");
/* 1404 */       return jSONObject;
/*      */     } 
/*      */     
/* 1407 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 1409 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 1410 */       response.setStatus(500);
/* 1411 */       JSONObject jSONObject = new JSONObject(true);
/* 1412 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 1413 */       jSONObject.put("error", "Bad Request");
/* 1414 */       jSONObject.put("message", "error");
/* 1415 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/* 1419 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 1421 */     JSONObject BattleData = Utils.BattleData_decrypt(JsonBody.getString("data"), UserSyncData.getJSONObject("pushFlags").getString("status"));
/*      */     
/* 1423 */     int completeState = BattleData.getIntValue("completeState");
/*      */     
/* 1425 */     if (ArknightsApplication.serverConfig.getJSONObject("battle").getBooleanValue("debug")) {
/* 1426 */       completeState = 3;
/*      */     }
/* 1428 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/*      */     
/* 1430 */     current.getJSONObject("player").put("pending", new JSONArray());
/*      */     
/* 1432 */     if (completeState == 1) {
/*      */       
/* 1434 */       JSONObject jSONObject1 = new JSONObject();
/*      */       
/* 1436 */       jSONObject1.put("detailStr", "");
/* 1437 */       jSONObject1.put("popReport", Boolean.valueOf(false));
/* 1438 */       jSONObject1.put("success", Integer.valueOf(0));
/* 1439 */       jSONObject1.put("result", JSONObject.parseObject("{\"brief\":{\"level\":4,\"over\":true,\"success\":0,\"ending\":\"\",\"theme\":\"rogue_1\",\"mode\":\"EASY\",\"predefined\":null,\"band\":\"rogue_1_band_3\",\"startTs\":1642943654,\"endTs\":1642945001,\"endZoneId\":\"zone_3\",\"endProperty\":{\"hp\":0,\"gold\":16,\"populationCost\":19,\"populationMax\":29}},\"record\":{\"cntZone\":3,\"cntBattleNormal\":5,\"cntBattleElite\":1,\"cntBattleBoss\":0,\"cntArrivedNode\":13,\"cntRecruitChar\":8,\"cntUpgradeChar\":2,\"cntKillEnemy\":217,\"cntShopBuy\":2,\"cntPerfectBattle\":6,\"cntProtectBox\":4,\"cntRecruitFree\":0,\"cntRecruitAssist\":2,\"cntRecruitNpc\":3,\"cntRecruitProfession\":{\"SNIPER\":1,\"CASTER\":1,\"PIONEER\":2,\"TANK\":1,\"WARRIOR\":2,\"MEDIC\":1},\"troopChars\":[{\"charId\":\"char_1013_chen2\",\"type\":\"ASSIST\",\"upgradePhase\":1,\"evolvePhase\":2,\"level\":90},{\"charId\":\"char_328_cammou\",\"type\":\"ASSIST\",\"upgradePhase\":0,\"evolvePhase\":1,\"level\":60},{\"charId\":\"char_504_rguard\",\"type\":\"THIRD_LOW\",\"upgradePhase\":0,\"evolvePhase\":1,\"level\":55},{\"charId\":\"char_201_moeshd\",\"type\":\"NORMAL\",\"upgradePhase\":0,\"evolvePhase\":1,\"level\":70},{\"charId\":\"char_504_rguard\",\"type\":\"THIRD_LOW\",\"upgradePhase\":0,\"evolvePhase\":1,\"level\":55},{\"charId\":\"char_143_ghost\",\"type\":\"NORMAL\",\"upgradePhase\":0,\"evolvePhase\":1,\"level\":70},{\"charId\":\"char_208_melan\",\"type\":\"NORMAL\",\"upgradePhase\":0,\"evolvePhase\":1,\"level\":55},{\"charId\":\"char_510_amedic\",\"type\":\"THIRD\",\"upgradePhase\":1,\"evolvePhase\":2,\"level\":80}],\"cntArrivedNodeType\":{\"BATTLE_NORMAL\":4,\"INCIDENT\":4,\"SHOP\":1,\"BATTLE_ELITE\":2,\"TREASURE\":1,\"REST\":1},\"relicList\":[\"rogue_1_relic_a01\",\"rogue_1_relic_r09\",\"rogue_1_relic_q02\",\"rogue_1_relic_a45\",\"rogue_1_relic_a11\"],\"capsuleList\":[\"rogue_1_capsule_3\",\"rogue_1_capsule_7\",\"rogue_1_capsule_8\"],\"activeToolList\":[],\"zones\":[{\"index\":1,\"zoneId\":\"zone_1\",\"variation\":[]},{\"index\":2,\"zoneId\":\"zone_2\",\"variation\":[]},{\"index\":3,\"zoneId\":\"zone_3\",\"variation\":[]}]}}"));
/*      */       
/* 1441 */       JSONObject jSONObject2 = new JSONObject();
/* 1442 */       jSONObject2.put("content", jSONObject1);
/* 1443 */       jSONObject2.put("index", "e_" + current.getJSONObject("player").getJSONArray("pending").size() + '\001');
/* 1444 */       jSONObject2.put("type", "GAME_SETTLE");
/*      */       
/* 1446 */       current.getJSONObject("player").getJSONArray("pending").add(jSONObject2);
/*      */     } 
/*      */     
/* 1449 */     JSONObject content = new JSONObject();
/* 1450 */     JSONObject battleReward = new JSONObject();
/*      */     
/* 1452 */     JSONObject earn = new JSONObject();
/* 1453 */     earn.put("damage", Integer.valueOf(0));
/* 1454 */     earn.put("exp", Integer.valueOf(0));
/* 1455 */     earn.put("hp", Integer.valueOf(0));
/* 1456 */     earn.put("populationMax", Integer.valueOf(4));
/* 1457 */     earn.put("squadCapacity", Integer.valueOf(1));
/*      */     
/* 1459 */     current.getJSONObject("player").getJSONObject("property").put("capacity", Integer.valueOf(current.getJSONObject("player").getJSONObject("property").getIntValue("capacity") + 1));
/*      */     
/* 1461 */     current.getJSONObject("player").getJSONObject("property").getJSONObject("population").put("max", Integer.valueOf(current.getJSONObject("player").getJSONObject("property").getJSONObject("population").getIntValue("max") + 4));
/*      */     
/* 1463 */     JSONArray items = new JSONArray();
/* 1464 */     JSONObject item = new JSONObject();
/*      */     
/* 1466 */     item.put("count", Integer.valueOf(1));
/* 1467 */     item.put("id", "rogue_1_recruit_ticket_all");
/* 1468 */     item.put("sub", Integer.valueOf(0));
/* 1469 */     items.add(item);
/*      */     
/* 1471 */     JSONArray rewards = new JSONArray();
/* 1472 */     JSONObject reward = new JSONObject();
/*      */     
/* 1474 */     reward.put("done", Integer.valueOf(0));
/* 1475 */     reward.put("index", Integer.valueOf(0));
/* 1476 */     reward.put("items", items);
/* 1477 */     rewards.add(reward);
/*      */     
/* 1479 */     battleReward.put("earn", earn);
/* 1480 */     battleReward.put("rewards", rewards);
/* 1481 */     battleReward.put("show", Integer.valueOf(1));
/* 1482 */     content.put("battleReward", battleReward);
/* 1483 */     JSONObject event = new JSONObject();
/* 1484 */     event.put("content", content);
/* 1485 */     event.put("index", "e_" + current.getJSONObject("player").getJSONArray("pending").size() + '\001');
/* 1486 */     event.put("type", "BATTLE_REWARD");
/*      */     
/* 1488 */     current.getJSONObject("player").getJSONArray("pending").add(event);
/*      */     
/* 1490 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 1492 */     JSONObject result = new JSONObject(true);
/* 1493 */     JSONObject playerDataDelta = new JSONObject(true);
/* 1494 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 1495 */     JSONObject modified = new JSONObject(true);
/* 1496 */     modified.put("rlv2", UserSyncData.getJSONObject("rlv2"));
/* 1497 */     playerDataDelta.put("modified", modified);
/* 1498 */     result.put("playerDataDelta", playerDataDelta);
/* 1499 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/chooseBattleReward"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject chooseBattleReward(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 1507 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 1508 */     LOGGER.info("[/" + clientIp + "] /rlv2/chooseBattleReward");
/*      */     
/* 1510 */     int index = JsonBody.getIntValue("index");
/* 1511 */     int sub = JsonBody.getIntValue("sub");
/*      */     
/* 1513 */     if (!ArknightsApplication.enableServer) {
/* 1514 */       response.setStatus(400);
/* 1515 */       JSONObject jSONObject = new JSONObject(true);
/* 1516 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 1517 */       jSONObject.put("error", "Bad Request");
/* 1518 */       jSONObject.put("message", "server is close");
/* 1519 */       return jSONObject;
/*      */     } 
/*      */     
/* 1522 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 1523 */     if (Accounts.size() != 1) {
/* 1524 */       JSONObject jSONObject = new JSONObject(true);
/* 1525 */       jSONObject.put("result", Integer.valueOf(2));
/* 1526 */       jSONObject.put("error", "无法查询到此账户");
/* 1527 */       return jSONObject;
/*      */     } 
/*      */     
/* 1530 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 1532 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 1533 */       response.setStatus(500);
/* 1534 */       JSONObject jSONObject = new JSONObject(true);
/* 1535 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 1536 */       jSONObject.put("error", "Bad Request");
/* 1537 */       jSONObject.put("message", "error");
/* 1538 */       return jSONObject;
/*      */     } 
/*      */     
/* 1541 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 1543 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/* 1544 */     String theme = current.getJSONObject("game").getString("theme");
/* 1545 */     JSONArray pending = current.getJSONObject("player").getJSONArray("pending");
/* 1546 */     JSONObject property = current.getJSONObject("player").getJSONObject("property");
/* 1547 */     JSONObject roguelikeItems = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("items");
/*      */     
/* 1549 */     JSONObject BATTLE_REWARD = new JSONObject();
/* 1550 */     JSONArray items = new JSONArray();
/* 1551 */     int pendingIndex = 0;
/* 1552 */     int rewardsIndex = 0;
/*      */     
/* 1554 */     for (int i = 0; i < pending.size(); i++) {
/* 1555 */       if (pending.getJSONObject(i).getString("type").equals("BATTLE_REWARD")) {
/* 1556 */         BATTLE_REWARD = pending.getJSONObject(i).getJSONObject("content").getJSONObject("battleReward");
/* 1557 */         pendingIndex = i;
/*      */       } 
/*      */     } 
/*      */     
/* 1561 */     JSONObject reward = new JSONObject();
/*      */     int j;
/* 1563 */     for (j = 0; j < BATTLE_REWARD.getJSONArray("rewards").size(); j++) {
/*      */       
/* 1565 */       int rewardDone = BATTLE_REWARD.getJSONArray("rewards").getJSONObject(j).getIntValue("done");
/* 1566 */       int rewardIndex = BATTLE_REWARD.getJSONArray("rewards").getJSONObject(j).getIntValue("index");
/*      */       
/* 1568 */       if (rewardIndex == index) {
/* 1569 */         reward = BATTLE_REWARD.getJSONArray("rewards").getJSONObject(j);
/* 1570 */         rewardsIndex = j;
/* 1571 */         if (rewardDone == 0) {
/* 1572 */           items = reward.getJSONArray("items");
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1577 */     for (j = 0; j < items.size(); j++) {
/*      */       
/* 1579 */       int itemCount = items.getJSONObject(j).getIntValue("count");
/* 1580 */       int itemSub = items.getJSONObject(j).getIntValue("sub");
/* 1581 */       String itemId = items.getJSONObject(j).getString("id");
/*      */       
/* 1583 */       String type = roguelikeItems.getJSONObject(itemId).getString("type");
/*      */       
/* 1585 */       reward.put("done", Integer.valueOf(1));
/*      */       
/* 1587 */       if (type.equals("HP")) {
/* 1588 */         property.put("hp", Integer.valueOf(property.getIntValue("hp") + itemCount));
/*      */       }
/*      */       
/* 1591 */       if (type.equals("GOLD")) {
/* 1592 */         property.put("gold", Integer.valueOf(property.getIntValue("gold") + itemCount));
/*      */       }
/*      */       
/* 1595 */       if (type.equals("SQUAD_CAPACITY")) {
/* 1596 */         property.put("capacity", Integer.valueOf(property.getIntValue("capacity") + itemCount));
/*      */       }
/*      */       
/* 1599 */       if (type.equals("POPULATION")) {
/* 1600 */         property.getJSONObject("population").put("max", Integer.valueOf(property.getJSONObject("population").getIntValue("max") + itemCount));
/*      */       }
/*      */       
/* 1603 */       if (type.equals("EXP")) {
/* 1604 */         property.put("exp", Integer.valueOf(property.getIntValue("exp") + itemCount));
/*      */       }
/*      */       
/* 1607 */       if (type.equals("RELIC")) {
/*      */         
/* 1609 */         Boolean UPGRADE = Boolean.valueOf(false);
/*      */         
/* 1611 */         JSONObject selectRelics = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("relics").getJSONObject(itemId);
/*      */         
/* 1613 */         JSONArray buffs = selectRelics.getJSONArray("buffs");
/*      */         
/* 1615 */         for (int m = 0; m < buffs.size(); m++) {
/* 1616 */           String key = buffs.getJSONObject(m).getString("key");
/* 1617 */           JSONArray blackboard = buffs.getJSONObject(m).getJSONArray("blackboard");
/*      */ 
/*      */           
/* 1620 */           if (key.equals("level_life_point_add")) {
/* 1621 */             current.getJSONObject("buff").put("tmpHP", Integer.valueOf(current.getJSONObject("buff").getIntValue("tmpHP") + blackboard.getJSONObject(0).getIntValue("value")));
/*      */           }
/*      */ 
/*      */           
/* 1625 */           if (key.equals("immediate_reward")) {
/*      */             
/* 1627 */             String valueStr = blackboard.getJSONObject(0).getString("valueStr");
/* 1628 */             String buffType = roguelikeItems.getJSONObject(valueStr).getString("type");
/* 1629 */             int count = blackboard.getJSONObject(1).getIntValue("value");
/*      */             
/* 1631 */             if (buffType.equals("HP")) {
/* 1632 */               property.put("hp", Integer.valueOf(property.getIntValue("hp") + count));
/*      */             }
/* 1634 */             if (buffType.equals("GOLD")) {
/* 1635 */               property.put("gold", Integer.valueOf(property.getIntValue("gold") + count));
/*      */             }
/* 1637 */             if (buffType.equals("SQUAD_CAPACITY")) {
/* 1638 */               property.put("capacity", Integer.valueOf(property.getIntValue("capacity") + count));
/*      */             }
/* 1640 */             if (buffType.equals("POPULATION")) {
/* 1641 */               property.getJSONObject("population").put("max", Integer.valueOf(property.getJSONObject("population").getIntValue("max") + count));
/*      */             }
/*      */ 
/*      */             
/* 1645 */             if (buffType.equals("UPGRADE_TICKET")) {
/* 1646 */               UPGRADE = Boolean.valueOf(true);
/*      */               
/* 1648 */               int recruitIndex = current.getJSONObject("inventory").getJSONObject("recruit").size() + 1;
/* 1649 */               String id = "t_" + recruitIndex;
/* 1650 */               JSONObject recruit = new JSONObject();
/* 1651 */               recruit.put("from", "buff");
/* 1652 */               recruit.put("id", valueStr);
/* 1653 */               recruit.put("index", "t_" + recruitIndex);
/* 1654 */               recruit.put("mustExtra", Integer.valueOf(0));
/* 1655 */               recruit.put("needAssist", Boolean.valueOf(false));
/* 1656 */               recruit.put("state", Integer.valueOf(0));
/* 1657 */               recruit.put("result", null);
/* 1658 */               recruit.put("ts", Long.valueOf((new Date()).getTime() / 1000L));
/* 1659 */               current.getJSONObject("inventory").getJSONObject("recruit").put("t_" + recruitIndex, recruit);
/*      */               
/* 1661 */               JSONArray list = new JSONArray();
/*      */               
/* 1663 */               JSONObject upgradeTickets = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("upgradeTickets");
/* 1664 */               current.getJSONObject("inventory").getJSONObject("recruit").getJSONObject(id).put("state", Integer.valueOf(1));
/*      */               
/* 1666 */               String ticketId = current.getJSONObject("inventory").getJSONObject("recruit").getJSONObject(id).getString("id");
/*      */               
/* 1668 */               JSONObject ticket = upgradeTickets.getJSONObject(ticketId);
/*      */               
/* 1670 */               JSONArray professionList = ticket.getJSONArray("professionList");
/* 1671 */               JSONArray rarityList = ticket.getJSONArray("rarityList");
/*      */               
/* 1673 */               for (Map.Entry Entry : current.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 1674 */                 JSONObject troopChar = JSONObject.parseObject(Entry.getValue().toString());
/* 1675 */                 String profession = troopChar.getString("profession");
/* 1676 */                 int rarity = troopChar.getIntValue("rarity");
/*      */                 
/* 1678 */                 if (professionList.contains(profession) && 
/* 1679 */                   rarityList.contains(Integer.valueOf(rarity)) && 
/* 1680 */                   !troopChar.getBooleanValue("upgradeLimited")) {
/*      */                   
/* 1682 */                   JSONObject originalChar = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(troopChar.getString("originalId"));
/*      */                   
/* 1684 */                   if (originalChar.getIntValue("evolvePhase") != 0 && 
/* 1685 */                     originalChar.getIntValue("evolvePhase") == 2) {
/* 1686 */                     troopChar.put("evolvePhase", Integer.valueOf(2));
/*      */                   }
/*      */                   
/* 1689 */                   troopChar.put("isUpgrade", Boolean.valueOf(true));
/* 1690 */                   troopChar.put("upgradeLimited", Boolean.valueOf(true));
/* 1691 */                   troopChar.put("population", Integer.valueOf(0));
/* 1692 */                   troopChar.put("skills", originalChar.getJSONArray("skills"));
/* 1693 */                   troopChar.put("level", Integer.valueOf(originalChar.getIntValue("level")));
/* 1694 */                   troopChar.put("upgradePhase", Integer.valueOf(originalChar.getIntValue("evolvePhase")));
/*      */                   
/* 1696 */                   list.add(troopChar);
/*      */                 } 
/*      */               } 
/*      */ 
/*      */ 
/*      */               
/* 1702 */               recruit.put("list", list);
/*      */               
/* 1704 */               JSONObject RECRUIT = new JSONObject();
/* 1705 */               JSONObject content = new JSONObject();
/*      */               
/* 1707 */               JSONObject recruitTicket = new JSONObject();
/* 1708 */               recruitTicket.put("ticket", id);
/* 1709 */               recruitTicket.put("pendingIndex", Integer.valueOf(pendingIndex));
/* 1710 */               recruitTicket.put("rewardsIndex", Integer.valueOf(rewardsIndex));
/* 1711 */               recruitTicket.put("isRelic", Boolean.valueOf(true));
/* 1712 */               recruitTicket.put("relicId", itemId);
/*      */               
/* 1714 */               content.put("recruit", recruitTicket);
/* 1715 */               RECRUIT.put("content", content);
/* 1716 */               RECRUIT.put("index", "e_" + current.getJSONObject("player").getJSONArray("pending").size() + '\001');
/* 1717 */               RECRUIT.put("type", "RECRUIT");
/* 1718 */               current.getJSONObject("player").getJSONArray("pending").add(0, RECRUIT);
/*      */             } 
/*      */             
/* 1721 */             if (!UPGRADE.booleanValue()) {
/* 1722 */               JSONObject relic = new JSONObject();
/* 1723 */               String relicIndex = "r_" + current.getJSONObject("inventory").getJSONObject("relic").size() + '\001';
/* 1724 */               relic.put("count", Integer.valueOf(itemCount));
/* 1725 */               relic.put("id", itemId);
/* 1726 */               relic.put("index", relicIndex);
/* 1727 */               relic.put("ts", Long.valueOf((new Date()).getTime() / 1000L));
/* 1728 */               current.getJSONObject("inventory").getJSONObject("relic").put(relicIndex, relic);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1735 */       if (type.equals("UPGRADE_TICKET")) {
/*      */         
/* 1737 */         int relicIndex = current.getJSONObject("inventory").getJSONObject("recruit").size() + 1;
/* 1738 */         String id = "t_" + relicIndex;
/* 1739 */         JSONObject relic = new JSONObject();
/* 1740 */         relic.put("from", "buff");
/* 1741 */         relic.put("id", itemId);
/* 1742 */         relic.put("index", "t_" + relicIndex);
/* 1743 */         relic.put("mustExtra", Integer.valueOf(0));
/* 1744 */         relic.put("needAssist", Boolean.valueOf(false));
/* 1745 */         relic.put("state", Integer.valueOf(0));
/* 1746 */         relic.put("result", null);
/* 1747 */         relic.put("ts", Long.valueOf((new Date()).getTime() / 1000L));
/* 1748 */         current.getJSONObject("inventory").getJSONObject("recruit").put("t_" + relicIndex, relic);
/*      */         
/* 1750 */         JSONArray list = new JSONArray();
/*      */         
/* 1752 */         JSONObject upgradeTickets = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("upgradeTickets");
/* 1753 */         current.getJSONObject("inventory").getJSONObject("recruit").getJSONObject(id).put("state", Integer.valueOf(1));
/*      */         
/* 1755 */         String ticketId = current.getJSONObject("inventory").getJSONObject("recruit").getJSONObject(id).getString("id");
/*      */         
/* 1757 */         JSONObject ticket = upgradeTickets.getJSONObject(ticketId);
/*      */         
/* 1759 */         JSONArray professionList = ticket.getJSONArray("professionList");
/* 1760 */         JSONArray rarityList = ticket.getJSONArray("rarityList");
/*      */         
/* 1762 */         for (Map.Entry Entry : current.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 1763 */           JSONObject troopChar = JSONObject.parseObject(Entry.getValue().toString());
/* 1764 */           String profession = troopChar.getString("profession");
/* 1765 */           int rarity = troopChar.getIntValue("rarity");
/*      */           
/* 1767 */           if (professionList.contains(profession) && 
/* 1768 */             rarityList.contains(Integer.valueOf(rarity)) && 
/* 1769 */             !troopChar.getBooleanValue("upgradeLimited")) {
/*      */             
/* 1771 */             JSONObject originalChar = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(troopChar.getString("originalId"));
/*      */             
/* 1773 */             if (originalChar.getIntValue("evolvePhase") != 0 && 
/* 1774 */               originalChar.getIntValue("evolvePhase") == 2) {
/* 1775 */               troopChar.put("evolvePhase", Integer.valueOf(2));
/*      */             }
/*      */             
/* 1778 */             troopChar.put("isUpgrade", Boolean.valueOf(true));
/* 1779 */             troopChar.put("upgradeLimited", Boolean.valueOf(true));
/* 1780 */             troopChar.put("population", Integer.valueOf(0));
/* 1781 */             troopChar.put("skills", originalChar.getJSONArray("skills"));
/* 1782 */             if (troopChar.getIntValue("evolvePhase") == 1) {
/* 1783 */               if (troopChar.getJSONArray("skills").size() == 1) {
/* 1784 */                 troopChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/*      */               }
/* 1786 */               if (troopChar.getJSONArray("skills").size() == 2) {
/* 1787 */                 troopChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/* 1788 */                 troopChar.getJSONArray("skills").getJSONObject(1).put("specializeLevel", Integer.valueOf(0));
/*      */               } 
/* 1790 */               if (troopChar.getJSONArray("skills").size() == 3) {
/* 1791 */                 troopChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/* 1792 */                 troopChar.getJSONArray("skills").getJSONObject(1).put("specializeLevel", Integer.valueOf(0));
/* 1793 */                 troopChar.getJSONArray("skills").getJSONObject(2).put("specializeLevel", Integer.valueOf(0));
/* 1794 */                 troopChar.getJSONArray("skills").getJSONObject(2).put("unlock", Integer.valueOf(0));
/*      */               } 
/*      */             } 
/* 1797 */             troopChar.put("level", Integer.valueOf(originalChar.getIntValue("level")));
/* 1798 */             troopChar.put("upgradePhase", Integer.valueOf(originalChar.getIntValue("evolvePhase")));
/*      */             
/* 1800 */             list.add(troopChar);
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1806 */         relic.put("list", list);
/*      */         
/* 1808 */         JSONObject RECRUIT = new JSONObject();
/* 1809 */         JSONObject content = new JSONObject();
/*      */         
/* 1811 */         JSONObject recruit = new JSONObject();
/* 1812 */         recruit.put("ticket", id);
/* 1813 */         recruit.put("pendingIndex", Integer.valueOf(pendingIndex));
/* 1814 */         recruit.put("rewardsIndex", Integer.valueOf(rewardsIndex));
/* 1815 */         recruit.put("isRelic", Boolean.valueOf(false));
/* 1816 */         recruit.put("relicId", null);
/*      */         
/* 1818 */         content.put("recruit", recruit);
/* 1819 */         RECRUIT.put("content", content);
/* 1820 */         RECRUIT.put("index", "e_" + current.getJSONObject("player").getJSONArray("pending").size() + '\001');
/* 1821 */         RECRUIT.put("type", "RECRUIT");
/* 1822 */         current.getJSONObject("player").getJSONArray("pending").add(0, RECRUIT);
/*      */       } 
/*      */ 
/*      */       
/* 1826 */       if (type.equals("RECRUIT_TICKET")) {
/*      */         
/* 1828 */         int relicIndex = current.getJSONObject("inventory").getJSONObject("recruit").size() + 1;
/* 1829 */         String id = "t_" + relicIndex;
/* 1830 */         JSONObject relic = new JSONObject();
/* 1831 */         relic.put("from", "battle");
/* 1832 */         relic.put("id", itemId);
/* 1833 */         relic.put("index", "t_" + relicIndex);
/* 1834 */         relic.put("mustExtra", Integer.valueOf(0));
/* 1835 */         relic.put("needAssist", Boolean.valueOf(false));
/* 1836 */         relic.put("state", Integer.valueOf(0));
/* 1837 */         relic.put("result", null);
/* 1838 */         relic.put("ts", Long.valueOf((new Date()).getTime() / 1000L));
/* 1839 */         current.getJSONObject("inventory").getJSONObject("recruit").put("t_" + relicIndex, relic);
/*      */         
/* 1841 */         JSONArray list = new JSONArray();
/*      */         
/* 1843 */         JSONObject recruitTickets = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("recruitTickets");
/* 1844 */         current.getJSONObject("inventory").getJSONObject("recruit").getJSONObject(id).put("state", Integer.valueOf(1));
/*      */         
/* 1846 */         String ticketId = current.getJSONObject("inventory").getJSONObject("recruit").getJSONObject(id).getString("id");
/*      */         
/* 1848 */         JSONObject tempChar = JSONObject.parseObject("{\"instId\":\"0\",\"charId\":\"temp\",\"type\":\"THIRD_LOW\",\"evolvePhase\":1,\"level\":55,\"exp\":0,\"favorPoint\":25570,\"potentialRank\":0,\"mainSkillLvl\":7,\"skills\":[],\"defaultSkillIndex\":0,\"skin\":\"temp\",\"upgradeLimited\":false,\"upgradePhase\":0,\"isUpgrade\":false,\"population\":0}");
/*      */         
/* 1850 */         JSONObject ticket = recruitTickets.getJSONObject(ticketId);
/*      */         
/* 1852 */         JSONArray professionList = ticket.getJSONArray("professionList");
/* 1853 */         JSONArray rarityList = ticket.getJSONArray("rarityList");
/* 1854 */         JSONArray extraFreeRarity = ticket.getJSONArray("extraFreeRarity");
/* 1855 */         JSONArray extraCharIds = ticket.getJSONArray("extraCharIds");
/* 1856 */         JSONArray extraFreeList = new JSONArray();
/* 1857 */         for (int m = 0; m < extraCharIds.size(); m++) {
/* 1858 */           tempChar.put("instId", Integer.valueOf(m));
/* 1859 */           tempChar.put("charId", extraCharIds.getString(m));
/* 1860 */           tempChar.put("skin", extraCharIds.getString(m) + "#1");
/* 1861 */           list.add(JSONObject.parseObject(JSONObject.toJSONString(tempChar, new SerializerFeature[] { SerializerFeature.DisableCircularReferenceDetect })));
/*      */         } 
/*      */         
/* 1864 */         String relicsId = current.getJSONObject("inventory").getJSONObject("relic").getJSONObject("r_0").getString("id");
/* 1865 */         JSONObject selectRelics = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONObject("relics").getJSONObject(relicsId);
/* 1866 */         JSONArray relicsBuffs = selectRelics.getJSONArray("buffs");
/*      */         
/* 1868 */         JSONArray dynamicUpdateList = new JSONArray();
/*      */         int k;
/* 1870 */         for (k = 0; k < relicsBuffs.size(); k++) {
/* 1871 */           if (relicsBuffs.getJSONObject(k).getString("key").equals("dynamic_update")) {
/* 1872 */             String band = relicsBuffs.getJSONObject(k).getJSONArray("blackboard").getJSONObject(0).getString("valueStr").substring("recruit_upgrade_".length()).toUpperCase(Locale.ROOT);
/* 1873 */             dynamicUpdateList.add(band);
/*      */           } 
/*      */         } 
/*      */         
/* 1877 */         for (Map.Entry entry : UserSyncData.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 1878 */           JSONObject originalChar = JSONObject.parseObject(entry.getValue().toString());
/* 1879 */           JSONObject userChar = JSONObject.parseObject(entry.getValue().toString());
/* 1880 */           String charId = userChar.getString("charId");
/* 1881 */           String charProfession = ArknightsApplication.characterJson.getJSONObject(charId).getString("profession");
/*      */           
/* 1883 */           if (professionList.contains(charProfession)) {
/*      */             
/* 1885 */             int charRarity = ArknightsApplication.characterJson.getJSONObject(charId).getIntValue("rarity");
/*      */             
/* 1887 */             if (rarityList.contains(Integer.valueOf(charRarity))) {
/*      */               
/* 1889 */               int charPopulation = 0;
/*      */               
/* 1891 */               if (originalChar.getIntValue("evolvePhase") != 0 && 
/* 1892 */                 originalChar.getIntValue("evolvePhase") == 2) {
/* 1893 */                 userChar.put("evolvePhase", Integer.valueOf(1));
/*      */               }
/*      */               
/* 1896 */               if (userChar.getIntValue("evolvePhase") == 1) {
/* 1897 */                 if (userChar.getJSONArray("skills").size() == 1) {
/* 1898 */                   userChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/*      */                 }
/* 1900 */                 if (userChar.getJSONArray("skills").size() == 2) {
/* 1901 */                   userChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/* 1902 */                   userChar.getJSONArray("skills").getJSONObject(1).put("specializeLevel", Integer.valueOf(0));
/*      */                 } 
/* 1904 */                 if (userChar.getJSONArray("skills").size() == 3) {
/* 1905 */                   userChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/* 1906 */                   userChar.getJSONArray("skills").getJSONObject(1).put("specializeLevel", Integer.valueOf(0));
/* 1907 */                   userChar.getJSONArray("skills").getJSONObject(2).put("specializeLevel", Integer.valueOf(0));
/* 1908 */                   userChar.getJSONArray("skills").getJSONObject(2).put("unlock", Integer.valueOf(0));
/*      */                 } 
/*      */               } 
/*      */               
/* 1912 */               if (charRarity == 3) {
/* 1913 */                 charPopulation = 2;
/* 1914 */                 if (originalChar.getIntValue("level") > 60) {
/* 1915 */                   userChar.put("level", Integer.valueOf(60));
/*      */                 }
/*      */               } 
/* 1918 */               if (charRarity == 4) {
/* 1919 */                 charPopulation = 3;
/* 1920 */                 if (originalChar.getIntValue("level") > 70) {
/* 1921 */                   userChar.put("level", Integer.valueOf(70));
/*      */                 }
/*      */               } 
/* 1924 */               if (charRarity == 5) {
/* 1925 */                 charPopulation = 6;
/* 1926 */                 if (originalChar.getIntValue("level") > 80) {
/* 1927 */                   userChar.put("level", Integer.valueOf(80));
/*      */                 }
/*      */               } 
/*      */               
/* 1931 */               userChar.put("isUpgrade", Boolean.valueOf(false));
/* 1932 */               userChar.put("upgradePhase", Integer.valueOf(0));
/* 1933 */               userChar.put("upgradeLimited", Boolean.valueOf(true));
/* 1934 */               if (charRarity >= 3) {
/* 1935 */                 userChar.put("upgradeLimited", Boolean.valueOf(false));
/* 1936 */                 if (dynamicUpdateList.contains(charProfession)) {
/* 1937 */                   userChar.put("upgradeLimited", Boolean.valueOf(true));
/*      */                   
/* 1939 */                   if (originalChar.getIntValue("evolvePhase") != 0 && 
/* 1940 */                     originalChar.getIntValue("evolvePhase") == 2) {
/* 1941 */                     userChar.put("evolvePhase", Integer.valueOf(2));
/*      */                   }
/*      */ 
/*      */                   
/* 1945 */                   userChar.put("skills", originalChar.getJSONArray("skills"));
/* 1946 */                   if (userChar.getIntValue("evolvePhase") == 1) {
/* 1947 */                     if (userChar.getJSONArray("skills").size() == 1) {
/* 1948 */                       userChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/*      */                     }
/* 1950 */                     if (userChar.getJSONArray("skills").size() == 2) {
/* 1951 */                       userChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/* 1952 */                       userChar.getJSONArray("skills").getJSONObject(1).put("specializeLevel", Integer.valueOf(0));
/*      */                     } 
/* 1954 */                     if (userChar.getJSONArray("skills").size() == 3) {
/* 1955 */                       userChar.getJSONArray("skills").getJSONObject(0).put("specializeLevel", Integer.valueOf(0));
/* 1956 */                       userChar.getJSONArray("skills").getJSONObject(1).put("specializeLevel", Integer.valueOf(0));
/* 1957 */                       userChar.getJSONArray("skills").getJSONObject(2).put("specializeLevel", Integer.valueOf(0));
/* 1958 */                       userChar.getJSONArray("skills").getJSONObject(2).put("unlock", Integer.valueOf(0));
/*      */                     } 
/*      */                   } 
/* 1961 */                   userChar.put("level", Integer.valueOf(originalChar.getIntValue("level")));
/* 1962 */                   userChar.put("upgradePhase", Integer.valueOf(originalChar.getIntValue("evolvePhase")));
/*      */                 } 
/*      */               } 
/*      */               
/* 1966 */               userChar.put("rarity", Integer.valueOf(charRarity));
/* 1967 */               userChar.put("originalId", originalChar.getString("instId"));
/* 1968 */               userChar.put("profession", charProfession);
/* 1969 */               userChar.put("instId", Integer.valueOf(list.size()));
/* 1970 */               userChar.put("population", Integer.valueOf(charPopulation));
/* 1971 */               userChar.put("type", "NORMAL");
/*      */               
/* 1973 */               Boolean upgrade = Boolean.valueOf(false);
/* 1974 */               for (Map.Entry Entry : current.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/* 1975 */                 JSONObject troopChar = JSONObject.parseObject(Entry.getValue().toString());
/*      */                 
/* 1977 */                 if (troopChar.getString("charId").equals(charId)) {
/*      */                   
/* 1979 */                   if (!troopChar.getBooleanValue("upgradeLimited")) {
/*      */                     
/* 1981 */                     if (charRarity == 3) {
/* 1982 */                       charPopulation = 1;
/*      */                     }
/* 1984 */                     if (charRarity == 4) {
/* 1985 */                       charPopulation = 2;
/*      */                     }
/* 1987 */                     if (charRarity == 5) {
/* 1988 */                       charPopulation = 3;
/*      */                     }
/*      */                     
/* 1991 */                     if (originalChar.getIntValue("evolvePhase") != 0 && 
/* 1992 */                       originalChar.getIntValue("evolvePhase") == 2) {
/* 1993 */                       userChar.put("evolvePhase", Integer.valueOf(2));
/*      */                     }
/*      */                     
/* 1996 */                     userChar.put("isUpgrade", Boolean.valueOf(true));
/* 1997 */                     userChar.put("upgradeLimited", Boolean.valueOf(true));
/* 1998 */                     userChar.put("population", Integer.valueOf(charPopulation));
/* 1999 */                     userChar.put("skills", originalChar.getJSONArray("skills"));
/* 2000 */                     userChar.put("level", Integer.valueOf(originalChar.getIntValue("level")));
/* 2001 */                     userChar.put("upgradePhase", Integer.valueOf(originalChar.getIntValue("evolvePhase")));
/*      */                     continue;
/*      */                   } 
/* 2004 */                   upgrade = Boolean.valueOf(true);
/*      */                 } 
/*      */               } 
/*      */ 
/*      */               
/* 2009 */               if (!upgrade.booleanValue()) {
/* 2010 */                 list.add(userChar);
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/* 2016 */         for (k = 0; k < list.size(); k++) {
/* 2017 */           if (extraFreeRarity.contains(Integer.valueOf(list.getJSONObject(k).getIntValue("rarity")))) {
/* 2018 */             extraFreeList.add(Integer.valueOf(k));
/*      */           }
/*      */         } 
/* 2021 */         Collections.shuffle((List<?>)extraFreeList);
/*      */         
/* 2023 */         if (extraFreeList.size() != 0) {
/*      */           
/* 2025 */           JSONObject userChar = list.getJSONObject(extraFreeList.getIntValue(0));
/* 2026 */           int charRarity = userChar.getIntValue("rarity");
/*      */           
/* 2028 */           userChar.put("population", Integer.valueOf(0));
/* 2029 */           if (charRarity == 3) {
/* 2030 */             userChar.put("level", Integer.valueOf(60));
/*      */           }
/* 2032 */           if (charRarity == 4) {
/* 2033 */             userChar.put("level", Integer.valueOf(70));
/*      */           }
/* 2035 */           if (charRarity == 5) {
/* 2036 */             userChar.put("level", Integer.valueOf(80));
/*      */           }
/* 2038 */           userChar.put("potentialRank", Integer.valueOf(5));
/* 2039 */           userChar.put("mainSkillLvl", Integer.valueOf(7));
/* 2040 */           userChar.put("favorPoint", Integer.valueOf(25570));
/* 2041 */           userChar.put("evolvePhase", Integer.valueOf(1));
/* 2042 */           userChar.put("type", "FREE");
/*      */         } 
/*      */         
/* 2045 */         relic.put("list", list);
/*      */         
/* 2047 */         JSONObject RECRUIT = new JSONObject();
/* 2048 */         JSONObject content = new JSONObject();
/*      */         
/* 2050 */         JSONObject recruit = new JSONObject();
/* 2051 */         recruit.put("ticket", id);
/* 2052 */         recruit.put("pendingIndex", Integer.valueOf(pendingIndex));
/* 2053 */         recruit.put("rewardsIndex", Integer.valueOf(rewardsIndex));
/* 2054 */         recruit.put("isRelic", Boolean.valueOf(false));
/* 2055 */         recruit.put("relicId", null);
/*      */         
/* 2057 */         content.put("recruit", recruit);
/* 2058 */         RECRUIT.put("content", content);
/* 2059 */         RECRUIT.put("index", "e_" + current.getJSONObject("player").getJSONArray("pending").size() + '\001');
/* 2060 */         RECRUIT.put("type", "RECRUIT");
/* 2061 */         current.getJSONObject("player").getJSONArray("pending").add(0, RECRUIT);
/*      */       } 
/*      */     } 
/*      */     
/* 2065 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 2067 */     JSONObject result = new JSONObject(true);
/* 2068 */     JSONObject playerDataDelta = new JSONObject(true);
/* 2069 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 2070 */     JSONObject modified = new JSONObject(true);
/* 2071 */     modified.put("rlv2", UserSyncData.getJSONObject("rlv2"));
/* 2072 */     playerDataDelta.put("modified", modified);
/* 2073 */     result.put("playerDataDelta", playerDataDelta);
/* 2074 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/finishBattleReward"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject finishBattleReward(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 2081 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 2082 */     LOGGER.info("[/" + clientIp + "] /rlv2/finishBattleReward");
/*      */     
/* 2084 */     if (!ArknightsApplication.enableServer) {
/* 2085 */       response.setStatus(400);
/* 2086 */       JSONObject jSONObject = new JSONObject(true);
/* 2087 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 2088 */       jSONObject.put("error", "Bad Request");
/* 2089 */       jSONObject.put("message", "server is close");
/* 2090 */       return jSONObject;
/*      */     } 
/*      */     
/* 2093 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 2094 */     if (Accounts.size() != 1) {
/* 2095 */       JSONObject jSONObject = new JSONObject(true);
/* 2096 */       jSONObject.put("result", Integer.valueOf(2));
/* 2097 */       jSONObject.put("error", "无法查询到此账户");
/* 2098 */       return jSONObject;
/*      */     } 
/*      */     
/* 2101 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 2103 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 2104 */       response.setStatus(500);
/* 2105 */       JSONObject jSONObject = new JSONObject(true);
/* 2106 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 2107 */       jSONObject.put("error", "Bad Request");
/* 2108 */       jSONObject.put("message", "error");
/* 2109 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/* 2113 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 2115 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/*      */     
/* 2117 */     current.getJSONObject("player").put("pending", new JSONArray());
/* 2118 */     current.getJSONObject("player").put("state", "WAIT_MOVE");
/*      */     
/* 2120 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 2122 */     JSONObject result = new JSONObject(true);
/* 2123 */     JSONObject playerDataDelta = new JSONObject(true);
/* 2124 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 2125 */     JSONObject modified = new JSONObject(true);
/* 2126 */     modified.put("rlv2", UserSyncData.getJSONObject("rlv2"));
/* 2127 */     playerDataDelta.put("modified", modified);
/* 2128 */     result.put("playerDataDelta", playerDataDelta);
/* 2129 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/gameSettle"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject gameSettle(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 2135 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 2136 */     LOGGER.info("[/" + clientIp + "] /rlv2/gameSettle");
/*      */     
/* 2138 */     if (!ArknightsApplication.enableServer) {
/* 2139 */       response.setStatus(400);
/* 2140 */       JSONObject jSONObject = new JSONObject(true);
/* 2141 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 2142 */       jSONObject.put("error", "Bad Request");
/* 2143 */       jSONObject.put("message", "server is close");
/* 2144 */       return jSONObject;
/*      */     } 
/*      */     
/* 2147 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 2148 */     if (Accounts.size() != 1) {
/* 2149 */       JSONObject jSONObject = new JSONObject(true);
/* 2150 */       jSONObject.put("result", Integer.valueOf(2));
/* 2151 */       jSONObject.put("error", "无法查询到此账户");
/* 2152 */       return jSONObject;
/*      */     } 
/*      */     
/* 2155 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 2157 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 2158 */       response.setStatus(500);
/* 2159 */       JSONObject jSONObject = new JSONObject(true);
/* 2160 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 2161 */       jSONObject.put("error", "Bad Request");
/* 2162 */       jSONObject.put("message", "error");
/* 2163 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/* 2167 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 2169 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/*      */     
/* 2171 */     current.put("buff", null);
/* 2172 */     current.put("game", null);
/* 2173 */     current.put("inventory", null);
/* 2174 */     current.put("map", null);
/* 2175 */     current.put("player", null);
/* 2176 */     current.put("record", null);
/* 2177 */     current.put("troop", null);
/*      */     
/* 2179 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 2181 */     JSONObject result = new JSONObject(true);
/* 2182 */     JSONObject playerDataDelta = new JSONObject(true);
/* 2183 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 2184 */     JSONObject modified = new JSONObject(true);
/* 2185 */     modified.put("rlv2", UserSyncData.getJSONObject("rlv2"));
/* 2186 */     playerDataDelta.put("modified", modified);
/* 2187 */     result.put("playerDataDelta", playerDataDelta);
/* 2188 */     result.put("outer", JSONObject.parseObject("{\"mission\":{\"before\":[{\"type\":\"A\",\"tmpl\":\"CerternItem\",\"id\":\"rogue_1_task_25\",\"state\":1,\"target\":6,\"value\":6},{\"type\":\"B\",\"tmpl\":\"PassNodeType\",\"id\":\"rogue_1_task_13\",\"state\":1,\"target\":24,\"value\":24},{\"type\":\"C\",\"tmpl\":\"UsePopulation\",\"id\":\"rogue_1_task_11\",\"state\":1,\"target\":75,\"value\":76},{\"type\":\"C\",\"tmpl\":\"KillEnemy\",\"id\":\"rogue_1_task_3\",\"state\":1,\"target\":500,\"value\":641}],\"after\":[{\"type\":\"A\",\"tmpl\":\"CerternItem\",\"id\":\"rogue_1_task_25\",\"state\":1,\"target\":6,\"value\":6},{\"type\":\"B\",\"tmpl\":\"PassNodeType\",\"id\":\"rogue_1_task_13\",\"state\":1,\"target\":24,\"value\":24},{\"type\":\"C\",\"tmpl\":\"UsePopulation\",\"id\":\"rogue_1_task_11\",\"state\":1,\"target\":75,\"value\":76},{\"type\":\"C\",\"tmpl\":\"KillEnemy\",\"id\":\"rogue_1_task_3\",\"state\":1,\"target\":500,\"value\":641}]},\"missionBp\":{\"cnt\":0,\"from\":7594,\"to\":7594},\"relicBp\":{\"cnt\":0,\"from\":7594,\"to\":7594},\"relicUnlock\":[],\"gp\":0}"));
/* 2189 */     result.put("game", JSONObject.parseObject("{\"brief\":{\"level\":4,\"over\":true,\"success\":0,\"ending\":\"\",\"theme\":\"rogue_1\",\"mode\":\"EASY\",\"predefined\":null,\"band\":\"rogue_1_band_3\",\"startTs\":1642943654,\"endTs\":1642945001,\"endZoneId\":\"zone_3\",\"endProperty\":{\"hp\":0,\"gold\":16,\"populationCost\":19,\"populationMax\":29}},\"record\":{\"cntZone\":3,\"cntBattleNormal\":5,\"cntBattleElite\":1,\"cntBattleBoss\":0,\"cntArrivedNode\":13,\"cntRecruitChar\":8,\"cntUpgradeChar\":2,\"cntKillEnemy\":217,\"cntShopBuy\":2,\"cntPerfectBattle\":6,\"cntProtectBox\":4,\"cntRecruitFree\":0,\"cntRecruitAssist\":2,\"cntRecruitNpc\":3,\"cntRecruitProfession\":{\"SNIPER\":1,\"CASTER\":1,\"PIONEER\":2,\"TANK\":1,\"WARRIOR\":2,\"MEDIC\":1},\"troopChars\":[{\"charId\":\"char_1013_chen2\",\"type\":\"ASSIST\",\"upgradePhase\":1,\"evolvePhase\":2,\"level\":90},{\"charId\":\"char_328_cammou\",\"type\":\"ASSIST\",\"upgradePhase\":0,\"evolvePhase\":1,\"level\":60},{\"charId\":\"char_504_rguard\",\"type\":\"THIRD_LOW\",\"upgradePhase\":0,\"evolvePhase\":1,\"level\":55},{\"charId\":\"char_201_moeshd\",\"type\":\"NORMAL\",\"upgradePhase\":0,\"evolvePhase\":1,\"level\":70},{\"charId\":\"char_504_rguard\",\"type\":\"THIRD_LOW\",\"upgradePhase\":0,\"evolvePhase\":1,\"level\":55},{\"charId\":\"char_143_ghost\",\"type\":\"NORMAL\",\"upgradePhase\":0,\"evolvePhase\":1,\"level\":70},{\"charId\":\"char_208_melan\",\"type\":\"NORMAL\",\"upgradePhase\":0,\"evolvePhase\":1,\"level\":55},{\"charId\":\"char_510_amedic\",\"type\":\"THIRD\",\"upgradePhase\":1,\"evolvePhase\":2,\"level\":80}],\"cntArrivedNodeType\":{\"BATTLE_NORMAL\":4,\"INCIDENT\":4,\"SHOP\":1,\"BATTLE_ELITE\":2,\"TREASURE\":1,\"REST\":1},\"relicList\":[\"rogue_1_relic_a01\",\"rogue_1_relic_r09\",\"rogue_1_relic_q02\",\"rogue_1_relic_a45\",\"rogue_1_relic_a11\"],\"capsuleList\":[\"rogue_1_capsule_3\",\"rogue_1_capsule_7\",\"rogue_1_capsule_8\"],\"activeToolList\":[],\"zones\":[{\"index\":1,\"zoneId\":\"zone_1\",\"variation\":[]},{\"index\":2,\"zoneId\":\"zone_2\",\"variation\":[]},{\"index\":3,\"zoneId\":\"zone_3\",\"variation\":[]}]},\"score\":{\"detail\":[[2,80],[13,13],[5,50],[1,20],[0,0],[7,35],[7,14]],\"scoreFactor\":0.5,\"score\":106,\"buff\":1.08,\"bp\":{\"cnt\":114,\"from\":7480,\"to\":7594},\"gp\":11}}"));
/* 2190 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/moveTo"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject moveTo(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 2198 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 2199 */     LOGGER.info("[/" + clientIp + "] /rlv2/gameSettle");
/*      */     
/* 2201 */     JSONObject moveTo = JsonBody.getJSONObject("to");
/*      */     
/* 2203 */     if (!ArknightsApplication.enableServer) {
/* 2204 */       response.setStatus(400);
/* 2205 */       JSONObject jSONObject = new JSONObject(true);
/* 2206 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 2207 */       jSONObject.put("error", "Bad Request");
/* 2208 */       jSONObject.put("message", "server is close");
/* 2209 */       return jSONObject;
/*      */     } 
/*      */     
/* 2212 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 2213 */     if (Accounts.size() != 1) {
/* 2214 */       JSONObject jSONObject = new JSONObject(true);
/* 2215 */       jSONObject.put("result", Integer.valueOf(2));
/* 2216 */       jSONObject.put("error", "无法查询到此账户");
/* 2217 */       return jSONObject;
/*      */     } 
/*      */     
/* 2220 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 2222 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 2223 */       response.setStatus(500);
/* 2224 */       JSONObject jSONObject = new JSONObject(true);
/* 2225 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 2226 */       jSONObject.put("error", "Bad Request");
/* 2227 */       jSONObject.put("message", "error");
/* 2228 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/* 2232 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 2234 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/* 2235 */     JSONObject zones = current.getJSONObject("map").getJSONObject("zones");
/*      */     
/* 2237 */     String zoneIndex = current.getJSONObject("player").getJSONObject("cursor").getString("zone");
/*      */     
/* 2239 */     JSONObject nodes = zones.getJSONObject(zoneIndex).getJSONObject("nodes");
/*      */     
/* 2241 */     for (Map.Entry entry : nodes.entrySet()) {
/* 2242 */       if (nodes.getJSONObject(entry.getKey().toString()).getJSONObject("pos") == moveTo);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2247 */     current.getJSONObject("player").getJSONObject("cursor").put("position", moveTo);
/* 2248 */     current.getJSONObject("player").getJSONArray("trace").add(current.getJSONObject("player").getJSONObject("cursor"));
/*      */     
/* 2250 */     JSONObject pending = new JSONObject();
/* 2251 */     JSONObject content = new JSONObject();
/*      */     
/* 2253 */     JSONObject scene = new JSONObject();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2258 */     content.put("scene", scene);
/*      */     
/* 2260 */     pending.put("content", content);
/* 2261 */     pending.put("index", "e_" + current.getJSONObject("player").getJSONArray("pending").size() + '\001');
/* 2262 */     pending.put("type", "SCENE");
/*      */     
/* 2264 */     current.getJSONObject("player").getJSONArray("pending").add(pending);
/*      */ 
/*      */     
/* 2267 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 2269 */     JSONObject result = new JSONObject(true);
/* 2270 */     JSONObject playerDataDelta = new JSONObject(true);
/* 2271 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 2272 */     JSONObject modified = new JSONObject(true);
/* 2273 */     modified.put("rlv2", UserSyncData.getJSONObject("rlv2"));
/* 2274 */     playerDataDelta.put("modified", modified);
/* 2275 */     result.put("playerDataDelta", playerDataDelta);
/* 2276 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/battlePass/getReward"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject getReward(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 2284 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 2285 */     LOGGER.info("[/" + clientIp + "] /rlv2/battlePass/getReward");
/*      */     
/* 2287 */     String theme = JsonBody.getString("theme");
/* 2288 */     JSONArray rewards = JsonBody.getJSONArray("rewards");
/*      */     
/* 2290 */     if (!ArknightsApplication.enableServer) {
/* 2291 */       response.setStatus(400);
/* 2292 */       JSONObject jSONObject = new JSONObject(true);
/* 2293 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 2294 */       jSONObject.put("error", "Bad Request");
/* 2295 */       jSONObject.put("message", "server is close");
/* 2296 */       return jSONObject;
/*      */     } 
/*      */     
/* 2299 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 2300 */     if (Accounts.size() != 1) {
/* 2301 */       JSONObject jSONObject = new JSONObject(true);
/* 2302 */       jSONObject.put("result", Integer.valueOf(2));
/* 2303 */       jSONObject.put("error", "无法查询到此账户");
/* 2304 */       return jSONObject;
/*      */     } 
/*      */     
/* 2307 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 2309 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 2310 */       response.setStatus(500);
/* 2311 */       JSONObject jSONObject = new JSONObject(true);
/* 2312 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 2313 */       jSONObject.put("error", "Bad Request");
/* 2314 */       jSONObject.put("message", "error");
/* 2315 */       return jSONObject;
/*      */     } 
/*      */     
/* 2318 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/* 2319 */     JSONObject bp = UserSyncData.getJSONObject("rlv2").getJSONObject("outer").getJSONObject(theme).getJSONObject("bp");
/*      */     
/* 2321 */     JSONArray milestones = ArknightsApplication.roguelikeTable.getJSONObject("details").getJSONObject(theme).getJSONArray("milestones");
/*      */     
/* 2323 */     JSONArray items = new JSONArray();
/*      */     
/* 2325 */     Boolean isChar = Boolean.valueOf(false);
/* 2326 */     for (int i = 0; i < rewards.size(); i++) {
/* 2327 */       String bp_level = rewards.getString(i);
/* 2328 */       int index = Integer.valueOf(bp_level.substring(9)).intValue() - 1;
/*      */       
/* 2330 */       String itemID = milestones.getJSONObject(index).getString("itemID");
/* 2331 */       String itemType = milestones.getJSONObject(index).getString("itemType");
/* 2332 */       int itemCount = milestones.getJSONObject(index).getIntValue("itemCount");
/*      */       
/* 2334 */       if (itemType.equals("CHAR")) {
/* 2335 */         isChar = Boolean.valueOf(true);
/*      */       }
/* 2337 */       admin.GM_GiveItem(UserSyncData, itemID, itemType, itemCount, items);
/*      */       
/* 2339 */       if (!ArknightsApplication.serverConfig.getJSONObject("roguelike").getBooleanValue("unlimitedMilestones")) {
/* 2340 */         bp.getJSONObject("reward").put(bp_level, Integer.valueOf(1));
/*      */       }
/*      */     } 
/*      */     
/* 2344 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 2346 */     JSONObject result = new JSONObject(true);
/*      */     
/* 2348 */     JSONObject playerDataDelta = new JSONObject(true);
/* 2349 */     JSONObject modified = new JSONObject(true);
/* 2350 */     if (!ArknightsApplication.serverConfig.getJSONObject("roguelike").getBooleanValue("unlimitedMilestones")) {
/* 2351 */       modified.put("rlv2", UserSyncData.getJSONObject("rlv2"));
/*      */     }
/* 2353 */     modified.put("status", UserSyncData.getJSONObject("status"));
/* 2354 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 2355 */     if (isChar.booleanValue()) {
/* 2356 */       modified.put("troop", UserSyncData.getJSONObject("troop"));
/*      */     }
/* 2358 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 2359 */     playerDataDelta.put("modified", modified);
/* 2360 */     result.put("playerDataDelta", playerDataDelta);
/* 2361 */     result.put("items", items);
/* 2362 */     result.put("result", Integer.valueOf(0));
/* 2363 */     return result;
/*      */   }
/*      */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\rlv2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */