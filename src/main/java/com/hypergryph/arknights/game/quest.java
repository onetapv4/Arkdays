/*      */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*      */ import com.alibaba.fastjson.JSON;
/*      */ import com.alibaba.fastjson.JSONArray;
/*      */ import com.alibaba.fastjson.JSONObject;
/*      */ import com.hypergryph.arknights.ArknightsApplication;
/*      */ import com.hypergryph.arknights.core.dao.userDao;
/*      */ import com.hypergryph.arknights.core.decrypt.Utils;
/*      */ import com.hypergryph.arknights.core.pojo.Account;
/*      */ import com.hypergryph.arknights.core.pojo.SearchAssistCharList;
/*      */ import com.hypergryph.arknights.core.pojo.UserInfo;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.stream.IntStream;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import org.springframework.web.bind.annotation.PostMapping;
/*      */ import org.springframework.web.bind.annotation.RequestBody;
/*      */ import org.springframework.web.bind.annotation.RequestHeader;
/*      */ import org.springframework.web.bind.annotation.RestController;
/*      */ 
/*      */ @RestController
/*      */ @RequestMapping({"/quest"})
/*      */ public class quest {
/*      */   @PostMapping(value = {"/battleStart"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject BattleStart(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*   29 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*   30 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /quest/battleStart");
/*      */     
/*   32 */     if (!ArknightsApplication.enableServer) {
/*   33 */       response.setStatus(400);
/*   34 */       JSONObject jSONObject = new JSONObject(true);
/*   35 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*   36 */       jSONObject.put("error", "Bad Request");
/*   37 */       jSONObject.put("message", "server is close");
/*   38 */       return jSONObject;
/*      */     } 
/*      */     
/*   41 */     String stageId = JsonBody.getString("stageId");
/*   42 */     int isReplay = JsonBody.getIntValue("isReplay");
/*   43 */     int startTs = JsonBody.getIntValue("startTs");
/*   44 */     int usePracticeTicket = JsonBody.getIntValue("usePracticeTicket");
/*      */ 
/*      */ 
/*      */     
/*   48 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*   49 */     if (Accounts.size() != 1) {
/*   50 */       JSONObject jSONObject = new JSONObject(true);
/*   51 */       jSONObject.put("result", Integer.valueOf(2));
/*   52 */       jSONObject.put("error", "无法查询到此账户");
/*   53 */       return jSONObject;
/*      */     } 
/*      */     
/*   56 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*   58 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*   59 */       response.setStatus(500);
/*   60 */       JSONObject jSONObject = new JSONObject(true);
/*   61 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*   62 */       jSONObject.put("error", "Bad Request");
/*   63 */       jSONObject.put("message", "error");
/*   64 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*   68 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*   69 */     JSONObject stage_table = ArknightsApplication.stageTable.getJSONObject(stageId);
/*      */     
/*   71 */     if (!UserSyncData.getJSONObject("dungeon").getJSONObject("stages").containsKey(stageId)) {
/*   72 */       JSONObject stagesData = new JSONObject(true);
/*   73 */       stagesData.put("completeTimes", Integer.valueOf(0));
/*   74 */       stagesData.put("hasBattleReplay", Integer.valueOf(0));
/*   75 */       stagesData.put("noCostCnt", Integer.valueOf(1));
/*   76 */       stagesData.put("practiceTimes", Integer.valueOf(0));
/*   77 */       stagesData.put("stageId", stageId);
/*   78 */       stagesData.put("startTimes", Integer.valueOf(0));
/*   79 */       stagesData.put("state", Integer.valueOf(0));
/*      */       
/*   81 */       UserSyncData.getJSONObject("dungeon").getJSONObject("stages").put(stageId, stagesData);
/*      */     } 
/*      */     
/*   84 */     if (usePracticeTicket == 1) {
/*   85 */       UserSyncData.getJSONObject("status").put("practiceTicket", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("practiceTicket") - 1));
/*   86 */       UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).put("practiceTimes", Integer.valueOf(1));
/*      */     } 
/*      */     
/*   89 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/*   91 */     JSONObject result = new JSONObject(true);
/*   92 */     JSONObject playerDataDelta = new JSONObject(true);
/*   93 */     JSONObject modified = new JSONObject(true);
/*   94 */     JSONObject dungeon = new JSONObject(true);
/*   95 */     JSONObject stages = new JSONObject(true);
/*      */     
/*   97 */     stages.put(stageId, UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId));
/*      */     
/*   99 */     dungeon.put("stages", stages);
/*  100 */     modified.put("dungeon", dungeon);
/*  101 */     modified.put("status", UserSyncData.getJSONObject("status"));
/*  102 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  103 */     playerDataDelta.put("modified", modified);
/*  104 */     result.put("playerDataDelta", playerDataDelta);
/*  105 */     result.put("result", Integer.valueOf(0));
/*  106 */     result.put("battleId", stageId);
/*      */     
/*  108 */     result.put("isApProtect", Integer.valueOf(0));
/*  109 */     result.put("apFailReturn", Integer.valueOf(stage_table.getIntValue("apFailReturn")));
/*      */     
/*  111 */     if (UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).getIntValue("noCostCnt") == 1) {
/*  112 */       result.put("isApProtect", Integer.valueOf(1));
/*  113 */       result.put("apFailReturn", Integer.valueOf(stage_table.getIntValue("apCost")));
/*      */     } 
/*      */     
/*  116 */     if (stage_table.getIntValue("apCost") == 0) {
/*  117 */       result.put("isApProtect", Integer.valueOf(0));
/*  118 */       result.put("apFailReturn", Integer.valueOf(0));
/*      */     } 
/*      */     
/*  121 */     if (usePracticeTicket == 1) {
/*  122 */       result.put("isApProtect", Integer.valueOf(0));
/*  123 */       result.put("apFailReturn", Integer.valueOf(0));
/*      */     } 
/*      */     
/*  126 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/battleFinish"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject BattleFinish(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  133 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  134 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /quest/battleFinish");
/*      */     
/*  136 */     if (!ArknightsApplication.enableServer) {
/*  137 */       response.setStatus(400);
/*  138 */       JSONObject jSONObject = new JSONObject(true);
/*  139 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  140 */       jSONObject.put("error", "Bad Request");
/*  141 */       jSONObject.put("message", "server is close");
/*  142 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  147 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  148 */     if (Accounts.size() != 1) {
/*  149 */       JSONObject jSONObject = new JSONObject(true);
/*  150 */       jSONObject.put("result", Integer.valueOf(2));
/*  151 */       jSONObject.put("error", "无法查询到此账户");
/*  152 */       return jSONObject;
/*      */     } 
/*      */     
/*  155 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/*  157 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  158 */       response.setStatus(500);
/*  159 */       JSONObject jSONObject = new JSONObject(true);
/*  160 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  161 */       jSONObject.put("error", "Bad Request");
/*  162 */       jSONObject.put("message", "error");
/*  163 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/*  167 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/*  169 */     JSONObject BattleData = Utils.BattleData_decrypt(JsonBody.getString("data"), UserSyncData.getJSONObject("pushFlags").getString("status"));
/*      */     
/*  171 */     String stageId = BattleData.getString("battleId");
/*      */     
/*  173 */     JSONObject stage_table = ArknightsApplication.stageTable.getJSONObject(stageId);
/*  174 */     JSONObject stageClear = new JSONObject();
/*      */     
/*  176 */     if (ArknightsApplication.mainStage.containsKey(stageId)) {
/*  177 */       stageClear = ArknightsApplication.mainStage.getJSONObject(stageId);
/*      */     } else {
/*  179 */       stageClear.put("next", null);
/*  180 */       stageClear.put("star", null);
/*  181 */       stageClear.put("sub", null);
/*  182 */       stageClear.put("hard", null);
/*      */     } 
/*      */ 
/*      */     
/*  186 */     if (UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).getIntValue("practiceTimes") == 1) {
/*      */       
/*  188 */       if (UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).getIntValue("state") == 0) {
/*  189 */         UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).put("state", Integer.valueOf(1));
/*      */       }
/*  191 */       UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).put("practiceTimes", Integer.valueOf(0));
/*      */       
/*  193 */       userDao.setUserData(uid, UserSyncData);
/*      */       
/*  195 */       JSONObject jSONObject1 = new JSONObject(true);
/*  196 */       JSONObject jSONObject2 = new JSONObject(true);
/*  197 */       JSONObject jSONObject3 = new JSONObject(true);
/*  198 */       JSONObject jSONObject4 = new JSONObject(true);
/*  199 */       JSONObject jSONObject5 = new JSONObject(true);
/*  200 */       jSONObject5.put(stageId, UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId));
/*  201 */       jSONObject4.put("stages", jSONObject5);
/*      */       
/*  203 */       jSONObject3.put("status", UserSyncData.getJSONObject("status"));
/*  204 */       jSONObject3.put("dungeon", jSONObject4);
/*  205 */       jSONObject2.put("deleted", new JSONObject(true));
/*  206 */       jSONObject2.put("modified", jSONObject3);
/*  207 */       jSONObject1.put("playerDataDelta", jSONObject2);
/*  208 */       jSONObject1.put("result", Integer.valueOf(0));
/*  209 */       return jSONObject1;
/*      */     } 
/*      */     
/*  212 */     JSONObject chars = UserSyncData.getJSONObject("troop").getJSONObject("chars");
/*  213 */     JSONObject troop = new JSONObject(true);
/*      */     
/*  215 */     JSONObject result = new JSONObject(true);
/*      */ 
/*      */     
/*  218 */     int DropRate = ArknightsApplication.serverConfig.getJSONObject("battle").getIntValue("dropRate");
/*      */     
/*  220 */     int completeState = BattleData.getIntValue("completeState");
/*      */     
/*  222 */     if (ArknightsApplication.serverConfig.getJSONObject("battle").getBooleanValue("debug")) {
/*  223 */       completeState = 3;
/*      */     }
/*      */     
/*  226 */     int apCost = stage_table.getIntValue("apCost");
/*  227 */     int expGain = stage_table.getIntValue("expGain");
/*  228 */     int goldGain = stage_table.getIntValue("goldGain");
/*      */     
/*  230 */     result.put("goldScale", Integer.valueOf(1));
/*  231 */     result.put("expScale", Integer.valueOf(1));
/*      */ 
/*      */     
/*  234 */     if (completeState == 3) {
/*  235 */       expGain = (int)(expGain * 1.2D);
/*  236 */       goldGain = (int)(goldGain * 1.2D);
/*  237 */       result.put("goldScale", Double.valueOf(1.2D));
/*  238 */       result.put("expScale", Double.valueOf(1.2D));
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  243 */     int nowTime = (int)((new Date()).getTime() / 1000L);
/*      */     
/*  245 */     int addAp = (nowTime - UserSyncData.getJSONObject("status").getIntValue("lastApAddTime")) / 360;
/*      */     
/*  247 */     if (UserSyncData.getJSONObject("status").getIntValue("ap") < UserSyncData.getJSONObject("status").getIntValue("maxAp")) {
/*  248 */       if (UserSyncData.getJSONObject("status").getIntValue("ap") + addAp >= UserSyncData.getJSONObject("status").getIntValue("maxAp")) {
/*  249 */         UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("maxAp")));
/*  250 */         UserSyncData.getJSONObject("status").put("lastApAddTime", Integer.valueOf(nowTime));
/*      */       }
/*  252 */       else if (addAp != 0) {
/*  253 */         UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") + addAp));
/*  254 */         UserSyncData.getJSONObject("status").put("lastApAddTime", Integer.valueOf(nowTime));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  259 */     UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") - apCost));
/*      */ 
/*      */ 
/*      */     
/*  263 */     if (completeState == 1) {
/*      */       
/*  265 */       int apFailReturn = stage_table.getIntValue("apFailReturn");
/*      */       
/*  267 */       if (UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).getIntValue("noCostCnt") == 1) {
/*  268 */         apFailReturn = stage_table.getIntValue("apCost");
/*  269 */         UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).put("noCostCnt", Integer.valueOf(0));
/*      */       } 
/*      */       
/*  272 */       nowTime = (int)((new Date()).getTime() / 1000L);
/*      */       
/*  274 */       addAp = (UserSyncData.getJSONObject("status").getIntValue("lastApAddTime") - nowTime) / 360;
/*      */       
/*  276 */       if (UserSyncData.getJSONObject("status").getIntValue("ap") < UserSyncData.getJSONObject("status").getIntValue("maxAp")) {
/*  277 */         if (UserSyncData.getJSONObject("status").getIntValue("ap") + addAp >= UserSyncData.getJSONObject("status").getIntValue("maxAp")) {
/*  278 */           UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("maxAp")));
/*  279 */           UserSyncData.getJSONObject("status").put("lastApAddTime", Integer.valueOf(nowTime));
/*      */         } else {
/*  281 */           UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") + addAp));
/*  282 */           UserSyncData.getJSONObject("status").put("lastApAddTime", Integer.valueOf(nowTime));
/*      */         } 
/*      */       }
/*      */       
/*  286 */       UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") + apFailReturn));
/*  287 */       UserSyncData.getJSONObject("status").put("lastApAddTime", Integer.valueOf(nowTime));
/*      */       
/*  289 */       userDao.setUserData(uid, UserSyncData);
/*      */       
/*  291 */       JSONObject jSONObject1 = new JSONObject(true);
/*  292 */       JSONObject jSONObject2 = new JSONObject(true);
/*  293 */       result.put("additionalRewards", new JSONArray());
/*  294 */       result.put("alert", new JSONArray());
/*  295 */       result.put("firstRewards", new JSONArray());
/*  296 */       result.put("furnitureRewards", new JSONArray());
/*  297 */       result.put("unlockStages", new JSONArray());
/*  298 */       result.put("unusualRewards", new JSONArray());
/*  299 */       result.put("rewards", new JSONArray());
/*  300 */       result.put("expScale", Integer.valueOf(0));
/*  301 */       result.put("goldScale", Integer.valueOf(0));
/*  302 */       result.put("apFailReturn", Integer.valueOf(apFailReturn));
/*      */       
/*  304 */       jSONObject2.put("status", UserSyncData.getJSONObject("status"));
/*      */       
/*  306 */       JSONObject jSONObject3 = new JSONObject(true);
/*  307 */       JSONObject jSONObject4 = new JSONObject(true);
/*  308 */       jSONObject4.put(stageId, UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId));
/*  309 */       jSONObject3.put("stages", jSONObject4);
/*  310 */       jSONObject2.put("dungeon", jSONObject3);
/*  311 */       jSONObject1.put("deleted", new JSONObject(true));
/*  312 */       jSONObject1.put("modified", jSONObject2);
/*  313 */       result.put("playerDataDelta", jSONObject1);
/*  314 */       result.put("result", Integer.valueOf(0));
/*  315 */       return result;
/*      */     } 
/*      */     
/*  318 */     if (UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).getIntValue("state") == 0) {
/*  319 */       UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).put("state", Integer.valueOf(1));
/*      */     }
/*      */     
/*  322 */     JSONObject stages_data = UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId);
/*      */ 
/*      */ 
/*      */     
/*  326 */     JSONArray unlockStages = new JSONArray();
/*  327 */     JSONArray unlockStagesObject = new JSONArray();
/*  328 */     JSONArray additionalRewards = new JSONArray();
/*  329 */     JSONArray unusualRewards = new JSONArray();
/*  330 */     JSONArray furnitureRewards = new JSONArray();
/*  331 */     JSONArray firstRewards = new JSONArray();
/*  332 */     JSONArray rewards = new JSONArray();
/*      */     
/*  334 */     result.put("result", Integer.valueOf(0));
/*  335 */     result.put("alert", new JSONArray());
/*  336 */     result.put("suggestFriend", Boolean.valueOf(false));
/*  337 */     result.put("apFailReturn", Integer.valueOf(0));
/*      */     
/*  339 */     Boolean FirstClear = Boolean.valueOf(false);
/*  340 */     if (stages_data.getIntValue("state") != 3 && 
/*  341 */       completeState == 3) {
/*  342 */       FirstClear = Boolean.valueOf(true);
/*      */     }
/*      */ 
/*      */     
/*  346 */     if (stages_data.getIntValue("state") == 3 && 
/*  347 */       completeState == 4) {
/*  348 */       FirstClear = Boolean.valueOf(true);
/*      */     }
/*      */ 
/*      */     
/*  352 */     if (stages_data.getIntValue("state") == 1 && (
/*  353 */       completeState == 3 || completeState == 2)) {
/*      */ 
/*      */ 
/*      */       
/*  357 */       if (stageId.equals("main_08-16")) {
/*  358 */         for (Map.Entry entry : UserSyncData.getJSONObject("troop").getJSONObject("chars").entrySet()) {
/*  359 */           JSONObject charData = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(entry.getKey().toString());
/*  360 */           String charId = charData.getString("charId");
/*  361 */           if (charId.equals("char_002_amiya")) {
/*      */             
/*  363 */             JSONArray amiya_skills = charData.getJSONArray("skills");
/*  364 */             String amiya_skin = charData.getString("skin");
/*  365 */             int amiya_defaultSkillIndex = charData.getIntValue("defaultSkillIndex");
/*      */             
/*  367 */             charData.put("skin", null);
/*  368 */             charData.put("defaultSkillIndex", Integer.valueOf(-1));
/*  369 */             charData.put("skills", new JSONArray());
/*      */             
/*  371 */             charData.put("currentTmpl", "char_1001_amiya2");
/*  372 */             JSONObject tmpl = new JSONObject(true);
/*      */             
/*  374 */             JSONObject amiya = new JSONObject(true);
/*  375 */             amiya.put("skinId", amiya_skin);
/*  376 */             amiya.put("defaultSkillIndex", Integer.valueOf(amiya_defaultSkillIndex));
/*  377 */             amiya.put("skills", amiya_skills);
/*  378 */             amiya.put("currentEquip", null);
/*  379 */             amiya.put("equip", new JSONObject(true));
/*      */             
/*  381 */             tmpl.put("char_002_amiya", amiya);
/*      */             
/*  383 */             JSONArray sword_amiya_skills = new JSONArray();
/*      */             
/*  385 */             JSONObject skchr_amiya2_1 = new JSONObject(true);
/*  386 */             skchr_amiya2_1.put("skillId", "skchr_amiya2_1");
/*  387 */             skchr_amiya2_1.put("unlock", Integer.valueOf(1));
/*  388 */             skchr_amiya2_1.put("state", Integer.valueOf(0));
/*  389 */             skchr_amiya2_1.put("specializeLevel", Integer.valueOf(0));
/*  390 */             skchr_amiya2_1.put("completeUpgradeTime", Integer.valueOf(-1));
/*      */             
/*  392 */             sword_amiya_skills.add(skchr_amiya2_1);
/*      */             
/*  394 */             JSONObject skchr_amiya2_2 = new JSONObject(true);
/*  395 */             skchr_amiya2_2.put("skillId", "skchr_amiya2_1");
/*  396 */             skchr_amiya2_2.put("unlock", Integer.valueOf(1));
/*  397 */             skchr_amiya2_2.put("state", Integer.valueOf(0));
/*  398 */             skchr_amiya2_2.put("specializeLevel", Integer.valueOf(0));
/*  399 */             skchr_amiya2_2.put("completeUpgradeTime", Integer.valueOf(-1));
/*      */             
/*  401 */             sword_amiya_skills.add(skchr_amiya2_2);
/*      */             
/*  403 */             JSONObject sword_amiya = new JSONObject(true);
/*  404 */             sword_amiya.put("skinId", "char_1001_amiya2#2");
/*  405 */             sword_amiya.put("defaultSkillIndex", Integer.valueOf(0));
/*  406 */             sword_amiya.put("skills", sword_amiya_skills);
/*  407 */             sword_amiya.put("currentEquip", null);
/*  408 */             sword_amiya.put("equip", new JSONObject(true));
/*      */             
/*  410 */             tmpl.put("char_1001_amiya2", sword_amiya);
/*      */             
/*  412 */             charData.put("tmpl", tmpl);
/*      */             
/*  414 */             JSONObject charinstId = new JSONObject(true);
/*  415 */             charinstId.put(entry.getKey().toString(), charData);
/*  416 */             troop.put("chars", charinstId);
/*      */             
/*  418 */             UserSyncData.getJSONObject("troop").getJSONObject("chars").put(entry.getKey().toString(), charData);
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       }
/*      */       
/*  425 */       if (stageClear.getString("next") != null) {
/*  426 */         String next = stageClear.getString("next");
/*  427 */         JSONObject unlockStage = new JSONObject(true);
/*  428 */         unlockStage.put("hasBattleReplay", Integer.valueOf(0));
/*  429 */         unlockStage.put("noCostCnt", Integer.valueOf(1));
/*  430 */         unlockStage.put("practiceTimes", Integer.valueOf(0));
/*  431 */         unlockStage.put("completeTimes", Integer.valueOf(0));
/*  432 */         unlockStage.put("state", Integer.valueOf(0));
/*  433 */         unlockStage.put("stageId", next);
/*  434 */         unlockStage.put("startTimes", Integer.valueOf(0));
/*  435 */         UserSyncData.getJSONObject("dungeon").getJSONObject("stages").put(next, unlockStage);
/*      */         
/*  437 */         if (stage_table.getString("stageType").equals("MAIN") || stage_table.getString("stageType").equals("SUB")) {
/*  438 */           UserSyncData.getJSONObject("status").put("mainStageProgress", next);
/*      */         }
/*  440 */         unlockStages.add(next);
/*  441 */         unlockStagesObject.add(unlockStage);
/*      */       } 
/*      */       
/*  444 */       if (stageClear.getString("sub") != null) {
/*  445 */         String sub = stageClear.getString("sub");
/*  446 */         JSONObject sub_unlockStage = new JSONObject(true);
/*  447 */         sub_unlockStage.put("hasBattleReplay", Integer.valueOf(0));
/*  448 */         sub_unlockStage.put("noCostCnt", Integer.valueOf(1));
/*  449 */         sub_unlockStage.put("practiceTimes", Integer.valueOf(0));
/*  450 */         sub_unlockStage.put("completeTimes", Integer.valueOf(0));
/*  451 */         sub_unlockStage.put("state", Integer.valueOf(0));
/*  452 */         sub_unlockStage.put("stageId", sub);
/*  453 */         sub_unlockStage.put("startTimes", Integer.valueOf(0));
/*      */         
/*  455 */         UserSyncData.getJSONObject("dungeon").getJSONObject("stages").put(sub, sub_unlockStage);
/*  456 */         unlockStages.add(sub);
/*  457 */         unlockStagesObject.add(sub_unlockStage);
/*      */       } 
/*      */       
/*  460 */       if (completeState == 3) {
/*  461 */         if (stageClear.getString("star") != null) {
/*  462 */           String star = stageClear.getString("star");
/*  463 */           JSONObject star_unlockStage = new JSONObject(true);
/*  464 */           star_unlockStage.put("hasBattleReplay", Integer.valueOf(0));
/*  465 */           star_unlockStage.put("noCostCnt", Integer.valueOf(0));
/*  466 */           star_unlockStage.put("practiceTimes", Integer.valueOf(0));
/*  467 */           star_unlockStage.put("completeTimes", Integer.valueOf(0));
/*  468 */           star_unlockStage.put("state", Integer.valueOf(0));
/*  469 */           star_unlockStage.put("stageId", star);
/*  470 */           star_unlockStage.put("startTimes", Integer.valueOf(0));
/*      */           
/*  472 */           UserSyncData.getJSONObject("dungeon").getJSONObject("stages").put(star, star_unlockStage);
/*  473 */           unlockStages.add(star);
/*  474 */           unlockStagesObject.add(star_unlockStage);
/*      */         } 
/*      */         
/*  477 */         if (stageClear.getString("hard") != null) {
/*  478 */           String hard = stageClear.getString("hard");
/*  479 */           JSONObject hard_unlockStage = new JSONObject(true);
/*  480 */           hard_unlockStage.put("hasBattleReplay", Integer.valueOf(0));
/*  481 */           hard_unlockStage.put("noCostCnt", Integer.valueOf(0));
/*  482 */           hard_unlockStage.put("practiceTimes", Integer.valueOf(0));
/*  483 */           hard_unlockStage.put("completeTimes", Integer.valueOf(0));
/*  484 */           hard_unlockStage.put("state", Integer.valueOf(0));
/*  485 */           hard_unlockStage.put("stageId", hard);
/*  486 */           hard_unlockStage.put("startTimes", Integer.valueOf(0));
/*      */           
/*  488 */           UserSyncData.getJSONObject("dungeon").getJSONObject("stages").put(hard, hard_unlockStage);
/*  489 */           unlockStages.add(hard);
/*  490 */           unlockStagesObject.add(hard_unlockStage);
/*      */         } 
/*      */       } 
/*      */       
/*  494 */       result.put("unlockStages", unlockStages);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  499 */     if (FirstClear.booleanValue() == true) {
/*      */ 
/*      */ 
/*      */       
/*  503 */       JSONArray jSONArray = stage_table.getJSONObject("stageDropInfo").getJSONArray("displayDetailRewards");
/*      */       
/*  505 */       for (int k = 0; k < jSONArray.size(); k++) {
/*  506 */         int dropType = jSONArray.getJSONObject(k).getIntValue("dropType");
/*  507 */         int reward_count = 1 * DropRate;
/*  508 */         String reward_id = jSONArray.getJSONObject(k).getString("id");
/*  509 */         String reward_type = jSONArray.getJSONObject(k).getString("type");
/*      */         
/*  511 */         if (dropType == 1 || dropType == 8)
/*      */         {
/*  513 */           if (reward_type.equals("CHAR")) {
/*  514 */             JSONObject charGet = new JSONObject(true);
/*  515 */             String randomCharId = reward_id;
/*  516 */             int repeatCharId = 0;
/*      */             
/*  518 */             for (int n = 0; n < UserSyncData.getJSONObject("troop").getJSONObject("chars").size(); n++) {
/*  519 */               if (UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(n + 1)).getString("charId").equals(randomCharId)) {
/*  520 */                 repeatCharId = n + 1;
/*      */                 break;
/*      */               } 
/*      */             } 
/*  524 */             if (repeatCharId == 0) {
/*      */               
/*  526 */               JSONObject get_char = new JSONObject(true);
/*      */ 
/*      */               
/*  529 */               JSONObject char_data = new JSONObject(true);
/*  530 */               JSONArray skilsArray = ArknightsApplication.characterJson.getJSONObject(randomCharId).getJSONArray("skills");
/*  531 */               JSONArray skils = new JSONArray();
/*      */               
/*  533 */               for (int m = 0; m < skilsArray.size(); m++) {
/*  534 */                 JSONObject new_skils = new JSONObject(true);
/*  535 */                 new_skils.put("skillId", skilsArray.getJSONObject(m).getString("skillId"));
/*  536 */                 new_skils.put("state", Integer.valueOf(0));
/*  537 */                 new_skils.put("specializeLevel", Integer.valueOf(0));
/*  538 */                 new_skils.put("completeUpgradeTime", Integer.valueOf(-1));
/*  539 */                 if (skilsArray.getJSONObject(m).getJSONObject("unlockCond").getIntValue("phase") == 0) {
/*  540 */                   new_skils.put("unlock", Integer.valueOf(1));
/*      */                 } else {
/*  542 */                   new_skils.put("unlock", Integer.valueOf(0));
/*      */                 } 
/*  544 */                 skils.add(new_skils);
/*      */               } 
/*      */               
/*  547 */               int instId = UserSyncData.getJSONObject("troop").getJSONObject("chars").size() + 1;
/*  548 */               char_data.put("instId", Integer.valueOf(instId));
/*  549 */               char_data.put("charId", randomCharId);
/*  550 */               char_data.put("favorPoint", Integer.valueOf(0));
/*  551 */               char_data.put("potentialRank", Integer.valueOf(0));
/*  552 */               char_data.put("mainSkillLvl", Integer.valueOf(1));
/*  553 */               char_data.put("skin", randomCharId + "#1");
/*  554 */               char_data.put("level", Integer.valueOf(1));
/*  555 */               char_data.put("exp", Integer.valueOf(0));
/*  556 */               char_data.put("evolvePhase", Integer.valueOf(0));
/*  557 */               char_data.put("gainTime", Long.valueOf((new Date()).getTime() / 1000L));
/*  558 */               char_data.put("skills", skils);
/*  559 */               char_data.put("voiceLan", ArknightsApplication.charwordTable.getJSONObject("charDefaultTypeDict").getString(randomCharId));
/*  560 */               if (skils == new JSONArray()) {
/*  561 */                 char_data.put("defaultSkillIndex", Integer.valueOf(-1));
/*      */               } else {
/*  563 */                 char_data.put("defaultSkillIndex", Integer.valueOf(0));
/*      */               } 
/*      */               
/*  566 */               String sub1 = randomCharId.substring(randomCharId.indexOf("_") + 1);
/*  567 */               String charName = sub1.substring(sub1.indexOf("_") + 1);
/*      */               
/*  569 */               if (ArknightsApplication.uniequipTable.containsKey("uniequip_001_" + charName)) {
/*  570 */                 JSONObject equip = new JSONObject(true);
/*  571 */                 JSONObject uniequip_001 = new JSONObject(true);
/*  572 */                 uniequip_001.put("hide", Integer.valueOf(0));
/*  573 */                 uniequip_001.put("locked", Integer.valueOf(0));
/*  574 */                 uniequip_001.put("level", Integer.valueOf(1));
/*  575 */                 JSONObject uniequip_002 = new JSONObject(true);
/*  576 */                 uniequip_002.put("hide", Integer.valueOf(0));
/*  577 */                 uniequip_002.put("locked", Integer.valueOf(0));
/*  578 */                 uniequip_002.put("level", Integer.valueOf(1));
/*  579 */                 equip.put("uniequip_001_" + charName, uniequip_001);
/*  580 */                 equip.put("uniequip_002_" + charName, uniequip_002);
/*  581 */                 char_data.put("equip", equip);
/*  582 */                 char_data.put("currentEquip", "uniequip_001_" + charName);
/*      */               } else {
/*  584 */                 char_data.put("currentEquip", null);
/*      */               } 
/*      */               
/*  587 */               UserSyncData.getJSONObject("troop").getJSONObject("chars").put(String.valueOf(instId), char_data);
/*      */               
/*  589 */               JSONObject charGroup = new JSONObject(true);
/*  590 */               charGroup.put("favorPoint", Integer.valueOf(0));
/*  591 */               UserSyncData.getJSONObject("troop").getJSONObject("charGroup").put(randomCharId, charGroup);
/*      */               
/*  593 */               get_char.put("charInstId", Integer.valueOf(instId));
/*  594 */               get_char.put("charId", randomCharId);
/*  595 */               get_char.put("isNew", Integer.valueOf(1));
/*      */               
/*  597 */               JSONArray itemGet = new JSONArray();
/*      */               
/*  599 */               JSONObject new_itemGet_1 = new JSONObject(true);
/*  600 */               new_itemGet_1.put("type", "HGG_SHD");
/*  601 */               new_itemGet_1.put("id", "4004");
/*  602 */               new_itemGet_1.put("count", Integer.valueOf(1));
/*  603 */               itemGet.add(new_itemGet_1);
/*      */               
/*  605 */               UserSyncData.getJSONObject("status").put("hggShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("hggShard") + 1));
/*      */               
/*  607 */               get_char.put("itemGet", itemGet);
/*  608 */               UserSyncData.getJSONObject("inventory").put("p_" + randomCharId, Integer.valueOf(0));
/*      */               
/*  610 */               charGet = get_char;
/*      */               
/*  612 */               JSONObject charinstId = new JSONObject(true);
/*  613 */               charinstId.put(String.valueOf(instId), char_data);
/*  614 */               chars.put(String.valueOf(instId), char_data);
/*  615 */               troop.put("chars", charinstId);
/*      */             } else {
/*      */               
/*  618 */               JSONObject get_char = new JSONObject(true);
/*      */ 
/*      */               
/*  621 */               get_char.put("charInstId", Integer.valueOf(repeatCharId));
/*  622 */               get_char.put("charId", randomCharId);
/*  623 */               get_char.put("isNew", Integer.valueOf(0));
/*      */               
/*  625 */               JSONObject repatChar = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(repeatCharId));
/*      */               
/*  627 */               int potentialRank = repatChar.getIntValue("potentialRank");
/*  628 */               int rarity = ArknightsApplication.characterJson.getJSONObject(randomCharId).getIntValue("rarity");
/*      */               
/*  630 */               String itemName = null;
/*  631 */               String itemType = null;
/*  632 */               String itemId = null;
/*  633 */               int itemCount = 0;
/*  634 */               if (rarity == 0) {
/*  635 */                 itemName = "lggShard";
/*  636 */                 itemType = "LGG_SHD";
/*  637 */                 itemId = "4005";
/*  638 */                 itemCount = 1;
/*      */               } 
/*  640 */               if (rarity == 1) {
/*  641 */                 itemName = "lggShard";
/*  642 */                 itemType = "LGG_SHD";
/*  643 */                 itemId = "4005";
/*  644 */                 itemCount = 1;
/*      */               } 
/*  646 */               if (rarity == 2) {
/*  647 */                 itemName = "lggShard";
/*  648 */                 itemType = "LGG_SHD";
/*  649 */                 itemId = "4005";
/*  650 */                 itemCount = 5;
/*      */               } 
/*  652 */               if (rarity == 3) {
/*  653 */                 itemName = "lggShard";
/*  654 */                 itemType = "LGG_SHD";
/*  655 */                 itemId = "4005";
/*  656 */                 itemCount = 30;
/*      */               } 
/*  658 */               if (rarity == 4) {
/*  659 */                 itemName = "hggShard";
/*  660 */                 itemType = "HGG_SHD";
/*  661 */                 itemId = "4004";
/*  662 */                 if (potentialRank != 5) {
/*  663 */                   itemCount = 5;
/*      */                 } else {
/*  665 */                   itemCount = 8;
/*      */                 } 
/*      */               } 
/*  668 */               if (rarity == 5) {
/*  669 */                 itemName = "hggShard";
/*  670 */                 itemType = "HGG_SHD";
/*  671 */                 itemId = "4004";
/*  672 */                 if (potentialRank != 5) {
/*  673 */                   itemCount = 10;
/*      */                 } else {
/*  675 */                   itemCount = 15;
/*      */                 } 
/*      */               } 
/*      */               
/*  679 */               JSONArray itemGet = new JSONArray();
/*  680 */               JSONObject new_itemGet_1 = new JSONObject(true);
/*  681 */               new_itemGet_1.put("type", itemType);
/*  682 */               new_itemGet_1.put("id", itemId);
/*  683 */               new_itemGet_1.put("count", Integer.valueOf(itemCount));
/*  684 */               itemGet.add(new_itemGet_1);
/*  685 */               UserSyncData.getJSONObject("status").put(itemName, Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue(itemName) + itemCount));
/*      */               
/*  687 */               JSONObject new_itemGet_3 = new JSONObject(true);
/*  688 */               new_itemGet_3.put("type", "MATERIAL");
/*  689 */               new_itemGet_3.put("id", "p_" + randomCharId);
/*  690 */               new_itemGet_3.put("count", Integer.valueOf(1));
/*  691 */               itemGet.add(new_itemGet_3);
/*  692 */               get_char.put("itemGet", itemGet);
/*  693 */               UserSyncData.getJSONObject("inventory").put("p_" + randomCharId, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("p_" + randomCharId) + 1));
/*      */               
/*  695 */               charGet = get_char;
/*      */               
/*  697 */               JSONObject charinstId = new JSONObject(true);
/*  698 */               charinstId.put(String.valueOf(repeatCharId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(repeatCharId)));
/*  699 */               chars.put(String.valueOf(repeatCharId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(repeatCharId)));
/*  700 */               troop.put("chars", charinstId);
/*      */             } 
/*      */             
/*  703 */             JSONObject first_reward = new JSONObject(true);
/*  704 */             first_reward.put("count", Integer.valueOf(1));
/*  705 */             first_reward.put("id", reward_id);
/*  706 */             first_reward.put("type", reward_type);
/*  707 */             first_reward.put("charGet", charGet);
/*  708 */             firstRewards.add(first_reward);
/*      */           } else {
/*      */             
/*  711 */             if (reward_type.equals("MATERIAL")) {
/*  712 */               UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */             }
/*  714 */             if (reward_type.equals("CARD_EXP")) {
/*  715 */               UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */             }
/*  717 */             if (reward_type.equals("DIAMOND")) {
/*  718 */               UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + reward_count));
/*  719 */               UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + reward_count));
/*      */             } 
/*  721 */             if (reward_type.equals("GOLD")) {
/*  722 */               UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + reward_count));
/*      */             }
/*  724 */             if (reward_type.equals("TKT_RECRUIT")) {
/*  725 */               UserSyncData.getJSONObject("status").put("recruitLicense", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("recruitLicense") + reward_count));
/*      */             }
/*  727 */             if (reward_type.equals("FURN")) {
/*      */               
/*  729 */               if (!UserSyncData.getJSONObject("building").getJSONObject("furniture").containsKey(reward_id)) {
/*  730 */                 JSONObject furniture = new JSONObject(true);
/*  731 */                 furniture.put("count", Integer.valueOf(1));
/*  732 */                 furniture.put("inUse", Integer.valueOf(0));
/*  733 */                 UserSyncData.getJSONObject("building").getJSONObject("furniture").put(reward_id, furniture);
/*      */               } 
/*  735 */               UserSyncData.getJSONObject("building").getJSONObject("furniture").getJSONObject(reward_id).put("count", Integer.valueOf(UserSyncData.getJSONObject("building").getJSONObject("furniture").getJSONObject(reward_id).getIntValue("count") + 1));
/*      */             } 
/*  737 */             JSONObject first_reward = new JSONObject(true);
/*  738 */             first_reward.put("count", Integer.valueOf(reward_count));
/*  739 */             first_reward.put("id", reward_id);
/*  740 */             first_reward.put("type", reward_type);
/*  741 */             firstRewards.add(first_reward);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  748 */     result.put("firstRewards", firstRewards);
/*      */     
/*  750 */     if (UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).getIntValue("state") != 3) {
/*  751 */       UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).put("state", Integer.valueOf(completeState));
/*      */     }
/*      */     
/*  754 */     if (completeState == 4) {
/*  755 */       UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).put("state", Integer.valueOf(completeState));
/*      */     }
/*      */     
/*  758 */     UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).put("completeTime", Integer.valueOf(BattleData.getJSONObject("battleData").getIntValue("completeTime")));
/*      */     
/*  760 */     JSONArray playerExpMap = JSON.parseArray("[500,800,1240,1320,1400,1480,1560,1640,1720,1800,1880,1960,2040,2120,2200,2280,2360,2440,2520,2600,2680,2760,2840,2920,3000,3080,3160,3240,3350,3460,3570,3680,3790,3900,4200,4500,4800,5100,5400,5700,6000,6300,6600,6900,7200,7500,7800,8100,8400,8700,9000,9500,10000,10500,11000,11500,12000,12500,13000,13500,14000,14500,15000,15500,16000,17000,18000,19000,20000,21000,22000,23000,24000,25000,26000,27000,28000,29000,30000,31000,32000,33000,34000,35000,36000,37000,38000,39000,40000,41000,42000,43000,44000,45000,46000,47000,48000,49000,50000,51000,52000,54000,56000,58000,60000,62000,64000,66000,68000,70000,73000,76000,79000,82000,85000,88000,91000,94000,97000,100000]");
/*  761 */     JSONArray playerApMap = JSON.parseArray("[82,84,86,88,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,120,120,120,120,121,121,121,121,121,122,122,122,122,122,123,123,123,123,123,124,124,124,124,124,125,125,125,125,125,126,126,126,126,126,127,127,127,127,127,128,128,128,128,128,129,129,129,129,129,130,130,130,130,130,130,130,130,130,130,130,130,130,130,130,130,131,131,131,131,132,132,132,132,133,133,133,133,134,134,134,134,135,135,135,135]");
/*  762 */     int gold = UserSyncData.getJSONObject("status").getIntValue("gold");
/*  763 */     int exp = UserSyncData.getJSONObject("status").getIntValue("exp");
/*  764 */     int level = UserSyncData.getJSONObject("status").getIntValue("level");
/*      */ 
/*      */     
/*  767 */     if (goldGain != 0) {
/*  768 */       UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(gold + goldGain));
/*      */       
/*  770 */       JSONObject rewards_gold = new JSONObject(true);
/*  771 */       rewards_gold.put("count", Integer.valueOf(goldGain));
/*  772 */       rewards_gold.put("id", Integer.valueOf(4001));
/*  773 */       rewards_gold.put("type", "GOLD");
/*  774 */       rewards.add(rewards_gold);
/*      */     } 
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
/*  804 */     if (level < 120 && 
/*  805 */       expGain != 0) {
/*  806 */       UserSyncData.getJSONObject("status").put("exp", Integer.valueOf(exp + expGain));
/*  807 */       for (int k = 0; k < playerExpMap.size(); k++) {
/*  808 */         if (level == k + 1) {
/*  809 */           if (Integer.valueOf(playerExpMap.get(k).toString()).intValue() - UserSyncData.getJSONObject("status").getIntValue("exp") <= 0) {
/*  810 */             if (k + 2 == 120) {
/*  811 */               UserSyncData.getJSONObject("status").put("level", Integer.valueOf(120));
/*  812 */               UserSyncData.getJSONObject("status").put("exp", Integer.valueOf(0));
/*  813 */               UserSyncData.getJSONObject("status").put("maxAp", playerApMap.get(k + 1));
/*  814 */               UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") + UserSyncData.getJSONObject("status").getIntValue("maxAp")));
/*      */             } else {
/*  816 */               UserSyncData.getJSONObject("status").put("level", Integer.valueOf(k + 2));
/*  817 */               UserSyncData.getJSONObject("status").put("exp", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("exp") - Integer.valueOf(playerExpMap.get(k).toString()).intValue()));
/*  818 */               UserSyncData.getJSONObject("status").put("maxAp", playerApMap.get(k + 1));
/*  819 */               UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") + UserSyncData.getJSONObject("status").getIntValue("maxAp")));
/*      */             } 
/*  821 */             UserSyncData.getJSONObject("status").put("lastApAddTime", Long.valueOf((new Date()).getTime() / 1000L));
/*      */           } 
/*      */ 
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  831 */     JSONArray displayDetailRewards = stage_table.getJSONObject("stageDropInfo").getJSONArray("displayDetailRewards");
/*      */     
/*  833 */     for (int i = 0; i < displayDetailRewards.size(); i++) {
/*      */       
/*  835 */       int occPercent = displayDetailRewards.getJSONObject(i).getIntValue("occPercent");
/*  836 */       int dropType = displayDetailRewards.getJSONObject(i).getIntValue("dropType");
/*  837 */       int reward_count = 1 * DropRate;
/*      */       
/*  839 */       String reward_id = displayDetailRewards.getJSONObject(i).getString("id");
/*  840 */       String reward_type = displayDetailRewards.getJSONObject(i).getString("type");
/*      */       
/*  842 */       int reward_rarity = 0;
/*  843 */       int Percent = 0;
/*  844 */       int addPercent = 0;
/*      */       
/*  846 */       if (completeState == 3 && 
/*  847 */         !reward_type.equals("FURN") && !reward_type.equals("CHAR")) {
/*  848 */         reward_rarity = ArknightsApplication.itemTable.getJSONObject(reward_id).getIntValue("rarity");
/*  849 */         if (reward_rarity == 0) {
/*      */           
/*  851 */           JSONArray dropArray = new JSONArray();
/*      */           
/*  853 */           IntStream.range(0, 70).forEach(n -> dropArray.add(Integer.valueOf(0)));
/*  854 */           IntStream.range(0, 20).forEach(n -> dropArray.add(Integer.valueOf(1)));
/*  855 */           IntStream.range(0, 10).forEach(n -> dropArray.add(Integer.valueOf(2)));
/*      */           
/*  857 */           Collections.shuffle((List<?>)dropArray);
/*      */           
/*  859 */           int random = dropArray.getIntValue((new Random()).nextInt(dropArray.size()));
/*      */           
/*  861 */           reward_count += random;
/*  862 */           Percent = 10;
/*  863 */           addPercent = 0;
/*      */         } 
/*  865 */         if (reward_rarity == 1) {
/*      */           
/*  867 */           JSONArray dropArray = new JSONArray();
/*      */           
/*  869 */           IntStream.range(0, 70).forEach(n -> dropArray.add(Integer.valueOf(0)));
/*  870 */           IntStream.range(0, 10).forEach(n -> dropArray.add(Integer.valueOf(1)));
/*  871 */           IntStream.range(0, 5).forEach(n -> dropArray.add(Integer.valueOf(2)));
/*      */           
/*  873 */           Collections.shuffle((List<?>)dropArray);
/*      */           
/*  875 */           int random = dropArray.getIntValue((new Random()).nextInt(dropArray.size()));
/*      */           
/*  877 */           reward_count += random;
/*      */           
/*  879 */           Percent = 5;
/*  880 */           addPercent = 0;
/*      */         } 
/*  882 */         if (reward_rarity == 2) {
/*  883 */           Percent = 0;
/*  884 */           addPercent = 110;
/*      */         } 
/*  886 */         if (reward_rarity == 3) {
/*  887 */           Percent = 0;
/*  888 */           addPercent = 120;
/*      */         } 
/*  890 */         if (reward_rarity == 4) {
/*  891 */           Percent = 0;
/*  892 */           addPercent = 130;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  897 */       if (completeState == 2 && 
/*  898 */         !reward_type.equals("FURN") && !reward_type.equals("CHAR")) {
/*  899 */         reward_rarity = ArknightsApplication.itemTable.getJSONObject(reward_id).getIntValue("rarity");
/*  900 */         if (reward_rarity == 0) {
/*      */           
/*  902 */           JSONArray dropArray = new JSONArray();
/*      */           
/*  904 */           IntStream.range(0, 90 + Percent).forEach(n -> dropArray.add(Integer.valueOf(0)));
/*  905 */           IntStream.range(0, 12 + Percent).forEach(n -> dropArray.add(Integer.valueOf(1)));
/*  906 */           IntStream.range(0, 8 + addPercent).forEach(n -> dropArray.add(Integer.valueOf(2)));
/*      */           
/*  908 */           Collections.shuffle((List<?>)dropArray);
/*      */           
/*  910 */           int random = dropArray.getIntValue((new Random()).nextInt(dropArray.size()));
/*      */           
/*  912 */           reward_count += random;
/*  913 */           Percent = 0;
/*  914 */           addPercent = 0;
/*      */         } 
/*  916 */         if (reward_rarity == 1) {
/*      */           
/*  918 */           JSONArray dropArray = new JSONArray();
/*      */           
/*  920 */           IntStream.range(0, 110 + Percent).forEach(n -> dropArray.add(Integer.valueOf(0)));
/*  921 */           IntStream.range(0, 8 + Percent).forEach(n -> dropArray.add(Integer.valueOf(1)));
/*  922 */           IntStream.range(0, 2 + addPercent).forEach(n -> dropArray.add(Integer.valueOf(2)));
/*      */           
/*  924 */           Collections.shuffle((List<?>)dropArray);
/*      */           
/*  926 */           int random = dropArray.getIntValue((new Random()).nextInt(dropArray.size()));
/*      */           
/*  928 */           reward_count += random;
/*      */           
/*  930 */           Percent = 0;
/*  931 */           addPercent = 0;
/*      */         } 
/*  933 */         if (reward_rarity == 2) {
/*  934 */           Percent = 0;
/*  935 */           addPercent = 120;
/*      */         } 
/*  937 */         if (reward_rarity == 3) {
/*  938 */           Percent = 0;
/*  939 */           addPercent = 140;
/*      */         } 
/*  941 */         if (reward_rarity == 4) {
/*  942 */           Percent = 0;
/*  943 */           addPercent = 160;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  949 */       if (occPercent == 0 && dropType == 2) {
/*      */         
/*  951 */         if (reward_type.equals("MATERIAL")) {
/*      */ 
/*      */ 
/*      */           
/*  955 */           if (stageId.equals("wk_toxic_1")) {
/*  956 */             if (completeState == 3) {
/*  957 */               reward_count = 4;
/*      */             } else {
/*  959 */               reward_count = 3;
/*      */             } 
/*      */           }
/*      */           
/*  963 */           if (stageId.equals("wk_toxic_2")) {
/*  964 */             if (completeState == 3) {
/*  965 */               reward_count = 7;
/*      */             } else {
/*  967 */               reward_count = 3;
/*      */             } 
/*      */           }
/*      */           
/*  971 */           if (stageId.equals("wk_toxic_3")) {
/*  972 */             if (completeState == 3) {
/*  973 */               reward_count = 11;
/*      */             } else {
/*  975 */               reward_count = 6;
/*      */             } 
/*      */           }
/*      */           
/*  979 */           if (stageId.equals("wk_toxic_4")) {
/*  980 */             if (completeState == 3) {
/*  981 */               reward_count = 15;
/*      */             } else {
/*  983 */               reward_count = 7;
/*      */             } 
/*      */           }
/*      */           
/*  987 */           if (stageId.equals("wk_toxic_5")) {
/*  988 */             if (completeState == 3) {
/*  989 */               reward_count = 21;
/*      */             } else {
/*  991 */               reward_count = 8;
/*      */             } 
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  997 */           if (stageId.equals("wk_fly_1")) {
/*  998 */             if (completeState == 3) {
/*  999 */               reward_count = 3;
/*      */             } else {
/* 1001 */               reward_count = 1;
/*      */             } 
/*      */           }
/*      */           
/* 1005 */           if (stageId.equals("wk_fly_2")) {
/* 1006 */             if (completeState == 3) {
/* 1007 */               reward_count = 5;
/*      */             } else {
/* 1009 */               reward_count = 3;
/*      */             } 
/*      */           }
/*      */           
/* 1013 */           if (stageId.equals("wk_fly_3")) {
/* 1014 */             if (completeState == 3) {
/*      */               
/* 1016 */               if (reward_rarity == 1) {
/* 1017 */                 reward_count = 1;
/*      */               }
/* 1019 */               if (reward_rarity == 2) {
/* 1020 */                 reward_count = 3;
/*      */               }
/*      */             } else {
/* 1023 */               if (reward_rarity == 1) {
/* 1024 */                 reward_count = 1;
/*      */               }
/* 1026 */               if (reward_rarity == 2) {
/* 1027 */                 reward_count = 1;
/*      */               }
/*      */             } 
/*      */           }
/*      */           
/* 1032 */           if (stageId.equals("wk_fly_4")) {
/* 1033 */             if (completeState == 3) {
/* 1034 */               if (reward_rarity == 1) {
/* 1035 */                 reward_count = 1;
/*      */               }
/* 1037 */               if (reward_rarity == 2) {
/* 1038 */                 reward_count = 1;
/*      */               }
/* 1040 */               if (reward_rarity == 3) {
/* 1041 */                 reward_count = 2;
/*      */               }
/*      */             } else {
/* 1044 */               if (reward_rarity == 1) {
/* 1045 */                 reward_count = 1;
/*      */               }
/* 1047 */               if (reward_rarity == 2) {
/* 1048 */                 reward_count = 1;
/*      */               }
/* 1050 */               if (reward_rarity == 3) {
/* 1051 */                 reward_count = 1;
/*      */               }
/*      */             } 
/*      */           }
/*      */           
/* 1056 */           if (stageId.equals("wk_fly_5")) {
/* 1057 */             if (completeState == 3) {
/* 1058 */               if (reward_rarity == 1) {
/* 1059 */                 reward_count = 1;
/*      */               }
/* 1061 */               if (reward_rarity == 2) {
/* 1062 */                 reward_count = 2;
/*      */               }
/* 1064 */               if (reward_rarity == 3) {
/* 1065 */                 reward_count = 3;
/*      */               }
/*      */             } else {
/* 1068 */               if (reward_rarity == 1) {
/* 1069 */                 reward_count = 1;
/*      */               }
/* 1071 */               if (reward_rarity == 2) {
/* 1072 */                 reward_count = 1;
/*      */               }
/* 1074 */               if (reward_rarity == 3) {
/* 1075 */                 reward_count = 2;
/*      */               }
/*      */             } 
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1082 */           UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */         } 
/* 1084 */         if (reward_type.equals("CARD_EXP")) {
/* 1085 */           UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */         }
/* 1087 */         if (reward_type.equals("DIAMOND")) {
/* 1088 */           UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + reward_count));
/* 1089 */           UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + reward_count));
/*      */         } 
/*      */         
/* 1092 */         if (reward_type.equals("TKT_RECRUIT")) {
/* 1093 */           UserSyncData.getJSONObject("status").put("recruitLicense", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("recruitLicense") + reward_count));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1098 */         if (reward_type.equals("GOLD")) {
/*      */           
/* 1100 */           if (stageId.equals("main_01-01")) {
/* 1101 */             if (completeState == 3) {
/* 1102 */               reward_count = 660;
/*      */             } else {
/* 1104 */               reward_count = 550;
/*      */             } 
/*      */           }
/*      */           
/* 1108 */           if (stageId.equals("main_02-07")) {
/* 1109 */             if (completeState == 3) {
/* 1110 */               reward_count = 1500;
/*      */             } else {
/* 1112 */               reward_count = 1250;
/*      */             } 
/*      */           }
/*      */           
/* 1116 */           if (stageId.equals("main_03-06")) {
/* 1117 */             if (completeState == 3) {
/* 1118 */               reward_count = 2040;
/*      */             } else {
/* 1120 */               reward_count = 1700;
/*      */             } 
/*      */           }
/*      */           
/* 1124 */           if (stageId.equals("main_04-01")) {
/* 1125 */             if (completeState == 3) {
/* 1126 */               reward_count = 2700;
/*      */             } else {
/* 1128 */               reward_count = 2250;
/*      */             } 
/*      */           }
/*      */           
/* 1132 */           if (stageId.equals("main_06-01")) {
/* 1133 */             if (completeState == 3) {
/* 1134 */               reward_count = 1216;
/*      */             } else {
/* 1136 */               reward_count = 1013;
/*      */             } 
/*      */           }
/*      */           
/* 1140 */           if (stageId.equals("main_07-02")) {
/* 1141 */             if (completeState == 3) {
/* 1142 */               reward_count = 1216;
/*      */             } else {
/* 1144 */               reward_count = 1013;
/*      */             } 
/*      */           }
/*      */           
/* 1148 */           if (stageId.equals("main_08-01")) {
/* 1149 */             if (completeState == 3) {
/* 1150 */               reward_count = 2700;
/*      */             } else {
/* 1152 */               reward_count = 2250;
/*      */             } 
/*      */           }
/*      */           
/* 1156 */           if (stageId.equals("main_08-04")) {
/* 1157 */             if (completeState == 3) {
/* 1158 */               reward_count = 1216;
/*      */             } else {
/* 1160 */               reward_count = 1013;
/*      */             } 
/*      */           }
/*      */           
/* 1164 */           if (stageId.equals("main_09-01")) {
/* 1165 */             if (completeState == 3) {
/* 1166 */               reward_count = 2700;
/*      */             } else {
/* 1168 */               reward_count = 2250;
/*      */             } 
/*      */           }
/*      */           
/* 1172 */           if (stageId.equals("main_09-02")) {
/* 1173 */             if (completeState == 3) {
/* 1174 */               reward_count = 1216;
/*      */             } else {
/* 1176 */               reward_count = 1013;
/*      */             } 
/*      */           }
/*      */           
/* 1180 */           if (stageId.equals("sub_02-02")) {
/* 1181 */             if (completeState == 3) {
/* 1182 */               reward_count = 1020;
/*      */             } else {
/* 1184 */               reward_count = 850;
/*      */             } 
/*      */           }
/*      */           
/* 1188 */           if (stageId.equals("sub_04-2-3")) {
/* 1189 */             if (completeState == 3) {
/* 1190 */               reward_count = 3480;
/*      */             } else {
/* 1192 */               reward_count = 2900;
/*      */             } 
/*      */           }
/*      */           
/* 1196 */           if (stageId.equals("sub_05-1-2")) {
/* 1197 */             if (completeState == 3) {
/* 1198 */               reward_count = 2700;
/*      */             } else {
/* 1200 */               reward_count = 2250;
/*      */             } 
/*      */           }
/*      */           
/* 1204 */           if (stageId.equals("sub_05-2-1")) {
/* 1205 */             if (completeState == 3) {
/* 1206 */               reward_count = 1216;
/*      */             } else {
/* 1208 */               reward_count = 1013;
/*      */             } 
/*      */           }
/*      */           
/* 1212 */           if (stageId.equals("sub_05-3-1")) {
/* 1213 */             if (completeState == 3) {
/* 1214 */               reward_count = 1216;
/*      */             } else {
/* 1216 */               reward_count = 1013;
/*      */             } 
/*      */           }
/*      */           
/* 1220 */           if (stageId.equals("sub_06-1-2")) {
/* 1221 */             if (completeState == 3) {
/* 1222 */               reward_count = 1216;
/*      */             } else {
/* 1224 */               reward_count = 1013;
/*      */             } 
/*      */           }
/*      */           
/* 1228 */           if (stageId.equals("sub_06-2-2")) {
/* 1229 */             if (completeState == 3) {
/* 1230 */               reward_count = 2700;
/*      */             } else {
/* 1232 */               reward_count = 2250;
/*      */             } 
/*      */           }
/*      */           
/* 1236 */           if (stageId.equals("sub_07-1-1")) {
/* 1237 */             if (completeState == 3) {
/* 1238 */               reward_count = 2700;
/*      */             } else {
/* 1240 */               reward_count = 2250;
/*      */             } 
/*      */           }
/*      */           
/* 1244 */           if (stageId.equals("sub_07-1-2")) {
/* 1245 */             if (completeState == 3) {
/* 1246 */               reward_count = 1216;
/*      */             } else {
/* 1248 */               reward_count = 1013;
/*      */             } 
/*      */           }
/*      */           
/* 1252 */           if (stageId.equals("wk_melee_1")) {
/* 1253 */             if (completeState == 3) {
/* 1254 */               reward_count = 1700;
/*      */             } else {
/* 1256 */               reward_count = 1416;
/*      */             } 
/*      */           }
/*      */           
/* 1260 */           if (stageId.equals("wk_melee_2")) {
/* 1261 */             if (completeState == 3) {
/* 1262 */               reward_count = 2800;
/*      */             } else {
/* 1264 */               reward_count = 2333;
/*      */             } 
/*      */           }
/*      */           
/* 1268 */           if (stageId.equals("wk_melee_3")) {
/* 1269 */             if (completeState == 3) {
/* 1270 */               reward_count = 4100;
/*      */             } else {
/* 1272 */               reward_count = 3416;
/*      */             } 
/*      */           }
/*      */           
/* 1276 */           if (stageId.equals("wk_melee_4")) {
/* 1277 */             if (completeState == 3) {
/* 1278 */               reward_count = 5700;
/*      */             } else {
/* 1280 */               reward_count = 4750;
/*      */             } 
/*      */           }
/*      */           
/* 1284 */           if (stageId.equals("wk_melee_5")) {
/* 1285 */             if (completeState == 3) {
/* 1286 */               reward_count = 7500;
/*      */             } else {
/* 1288 */               reward_count = 6250;
/*      */             } 
/*      */           }
/*      */           
/* 1292 */           UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + reward_count));
/*      */         } 
/*      */         
/* 1295 */         JSONObject normal_reward = new JSONObject(true);
/* 1296 */         normal_reward.put("count", Integer.valueOf(reward_count));
/* 1297 */         normal_reward.put("id", reward_id);
/* 1298 */         normal_reward.put("type", reward_type);
/* 1299 */         rewards.add(normal_reward);
/*      */       } 
/*      */ 
/*      */       
/* 1303 */       if (occPercent == 1 && dropType == 2) {
/*      */         
/* 1305 */         JSONArray dropArray = new JSONArray();
/*      */         
/* 1307 */         IntStream.range(0, 80 + Percent).forEach(n -> dropArray.add(Integer.valueOf(1)));
/* 1308 */         IntStream.range(0, 20 + addPercent).forEach(n -> dropArray.add(Integer.valueOf(0)));
/*      */         
/* 1310 */         Collections.shuffle((List<?>)dropArray);
/*      */         
/* 1312 */         int cur = dropArray.getIntValue((new Random()).nextInt(dropArray.size()));
/*      */         
/* 1314 */         if (cur == 1) {
/* 1315 */           if (reward_type.equals("MATERIAL")) {
/* 1316 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1318 */           if (reward_type.equals("CARD_EXP")) {
/* 1319 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1321 */           if (reward_type.equals("DIAMOND")) {
/* 1322 */             UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + reward_count));
/* 1323 */             UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + reward_count));
/*      */           } 
/* 1325 */           if (reward_type.equals("GOLD")) {
/* 1326 */             UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + reward_count));
/*      */           }
/* 1328 */           if (reward_type.equals("TKT_RECRUIT")) {
/* 1329 */             UserSyncData.getJSONObject("status").put("recruitLicense", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("recruitLicense") + reward_count));
/*      */           }
/* 1331 */           JSONObject normal_reward = new JSONObject(true);
/* 1332 */           normal_reward.put("count", Integer.valueOf(reward_count));
/* 1333 */           normal_reward.put("id", reward_id);
/* 1334 */           normal_reward.put("type", reward_type);
/* 1335 */           rewards.add(normal_reward);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1340 */       if (occPercent == 2 && dropType == 2) {
/*      */         
/* 1342 */         if (stageId.indexOf("pro_") != -1) {
/*      */           
/* 1344 */           JSONArray jSONArray = new JSONArray();
/*      */           
/* 1346 */           IntStream.range(0, 5).forEach(n -> dropArray.add(Integer.valueOf(1)));
/* 1347 */           IntStream.range(0, 5).forEach(n -> dropArray.add(Integer.valueOf(0)));
/*      */           
/* 1349 */           Collections.shuffle((List<?>)jSONArray);
/*      */           
/* 1351 */           int k = jSONArray.getIntValue((new Random()).nextInt(jSONArray.size()));
/*      */           
/* 1353 */           reward_id = displayDetailRewards.getJSONObject(k).getString("id");
/* 1354 */           reward_type = displayDetailRewards.getJSONObject(k).getString("type");
/*      */           
/* 1356 */           if (reward_type.equals("MATERIAL")) {
/* 1357 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/*      */           
/* 1360 */           JSONObject normal_reward = new JSONObject(true);
/* 1361 */           normal_reward.put("count", Integer.valueOf(reward_count));
/* 1362 */           normal_reward.put("id", reward_id);
/* 1363 */           normal_reward.put("type", reward_type);
/* 1364 */           rewards.add(normal_reward);
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 1369 */         JSONArray dropArray = new JSONArray();
/*      */         
/* 1371 */         IntStream.range(0, 50 + Percent).forEach(n -> dropArray.add(Integer.valueOf(1)));
/* 1372 */         IntStream.range(0, 50 + addPercent).forEach(n -> dropArray.add(Integer.valueOf(0)));
/*      */         
/* 1374 */         Collections.shuffle((List<?>)dropArray);
/*      */         
/* 1376 */         int cur = dropArray.getIntValue((new Random()).nextInt(dropArray.size()));
/*      */         
/* 1378 */         if (cur == 1) {
/* 1379 */           if (reward_type.equals("MATERIAL")) {
/* 1380 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1382 */           if (reward_type.equals("CARD_EXP")) {
/* 1383 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1385 */           if (reward_type.equals("DIAMOND")) {
/* 1386 */             UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + reward_count));
/* 1387 */             UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + reward_count));
/*      */           } 
/* 1389 */           if (reward_type.equals("GOLD")) {
/* 1390 */             UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + reward_count));
/*      */           }
/* 1392 */           if (reward_type.equals("TKT_RECRUIT")) {
/* 1393 */             UserSyncData.getJSONObject("status").put("recruitLicense", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("recruitLicense") + reward_count));
/*      */           }
/*      */           
/* 1396 */           JSONObject normal_reward = new JSONObject(true);
/* 1397 */           normal_reward.put("count", Integer.valueOf(reward_count));
/* 1398 */           normal_reward.put("id", reward_id);
/* 1399 */           normal_reward.put("type", reward_type);
/* 1400 */           rewards.add(normal_reward);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1405 */       if (occPercent == 3 && dropType == 2) {
/*      */         
/* 1407 */         JSONArray dropArray = new JSONArray();
/*      */         
/* 1409 */         IntStream.range(0, 15 + Percent).forEach(n -> dropArray.add(Integer.valueOf(1)));
/* 1410 */         IntStream.range(0, 90 + addPercent).forEach(n -> dropArray.add(Integer.valueOf(0)));
/*      */         
/* 1412 */         Collections.shuffle((List<?>)dropArray);
/*      */         
/* 1414 */         int cur = dropArray.getIntValue((new Random()).nextInt(dropArray.size()));
/*      */         
/* 1416 */         if (cur == 1) {
/* 1417 */           if (reward_type.equals("MATERIAL")) {
/* 1418 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1420 */           if (reward_type.equals("CARD_EXP")) {
/* 1421 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1423 */           if (reward_type.equals("DIAMOND")) {
/* 1424 */             UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + reward_count));
/* 1425 */             UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + reward_count));
/*      */           } 
/* 1427 */           if (reward_type.equals("GOLD")) {
/* 1428 */             UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + reward_count));
/*      */           }
/* 1430 */           if (reward_type.equals("TKT_RECRUIT")) {
/* 1431 */             UserSyncData.getJSONObject("status").put("recruitLicense", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("recruitLicense") + reward_count));
/*      */           }
/* 1433 */           if (reward_type.equals("FURN")) {
/*      */             
/* 1435 */             if (!UserSyncData.getJSONObject("building").getJSONObject("furniture").containsKey(reward_id)) {
/* 1436 */               JSONObject furniture = new JSONObject(true);
/* 1437 */               furniture.put("count", Integer.valueOf(1));
/* 1438 */               furniture.put("inUse", Integer.valueOf(0));
/* 1439 */               UserSyncData.getJSONObject("building").getJSONObject("furniture").put(reward_id, furniture);
/*      */             } 
/* 1441 */             UserSyncData.getJSONObject("building").getJSONObject("furniture").getJSONObject(reward_id).put("count", Integer.valueOf(UserSyncData.getJSONObject("building").getJSONObject("furniture").getJSONObject(reward_id).getIntValue("count") + 1));
/*      */           } 
/* 1443 */           JSONObject normal_reward = new JSONObject(true);
/* 1444 */           normal_reward.put("count", Integer.valueOf(reward_count));
/* 1445 */           normal_reward.put("id", reward_id);
/* 1446 */           normal_reward.put("type", reward_type);
/*      */           
/* 1448 */           if (!reward_type.equals("FURN")) {
/* 1449 */             rewards.add(normal_reward);
/*      */           } else {
/* 1451 */             furnitureRewards.add(normal_reward);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1457 */       if (occPercent == 4 && dropType == 2) {
/*      */         
/* 1459 */         JSONArray dropArray = new JSONArray();
/*      */         
/* 1461 */         IntStream.range(0, 10 + Percent).forEach(n -> dropArray.add(Integer.valueOf(1)));
/* 1462 */         IntStream.range(0, 90 + addPercent).forEach(n -> dropArray.add(Integer.valueOf(0)));
/*      */         
/* 1464 */         Collections.shuffle((List<?>)dropArray);
/*      */         
/* 1466 */         int cur = dropArray.getIntValue((new Random()).nextInt(dropArray.size()));
/*      */         
/* 1468 */         if (cur == 1) {
/* 1469 */           if (reward_type.equals("MATERIAL")) {
/* 1470 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1472 */           if (reward_type.equals("CARD_EXP")) {
/* 1473 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1475 */           if (reward_type.equals("DIAMOND")) {
/* 1476 */             UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + reward_count));
/* 1477 */             UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + reward_count));
/*      */           } 
/* 1479 */           if (reward_type.equals("GOLD")) {
/* 1480 */             UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + reward_count));
/*      */           }
/* 1482 */           if (reward_type.equals("TKT_RECRUIT")) {
/* 1483 */             UserSyncData.getJSONObject("status").put("recruitLicense", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("recruitLicense") + reward_count));
/*      */           }
/* 1485 */           if (reward_type.equals("FURN")) {
/*      */             
/* 1487 */             if (!UserSyncData.getJSONObject("building").getJSONObject("furniture").containsKey(reward_id)) {
/* 1488 */               JSONObject furniture = new JSONObject(true);
/* 1489 */               furniture.put("count", Integer.valueOf(1));
/* 1490 */               furniture.put("inUse", Integer.valueOf(0));
/* 1491 */               UserSyncData.getJSONObject("building").getJSONObject("furniture").put(reward_id, furniture);
/*      */             } 
/* 1493 */             UserSyncData.getJSONObject("building").getJSONObject("furniture").getJSONObject(reward_id).put("count", Integer.valueOf(UserSyncData.getJSONObject("building").getJSONObject("furniture").getJSONObject(reward_id).getIntValue("count") + 1));
/*      */           } 
/* 1495 */           JSONObject normal_reward = new JSONObject(true);
/* 1496 */           normal_reward.put("count", Integer.valueOf(reward_count));
/* 1497 */           normal_reward.put("id", reward_id);
/* 1498 */           normal_reward.put("type", reward_type);
/*      */           
/* 1500 */           if (!reward_type.equals("FURN")) {
/* 1501 */             rewards.add(normal_reward);
/*      */           } else {
/* 1503 */             furnitureRewards.add(normal_reward);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1510 */       if (occPercent == 0 && dropType == 3) {
/*      */         
/* 1512 */         if (reward_type.equals("MATERIAL")) {
/* 1513 */           UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */         }
/* 1515 */         if (reward_type.equals("CARD_EXP")) {
/* 1516 */           UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */         }
/* 1518 */         if (reward_type.equals("DIAMOND")) {
/* 1519 */           UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + reward_count));
/* 1520 */           UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + reward_count));
/*      */         } 
/* 1522 */         if (reward_type.equals("GOLD")) {
/* 1523 */           UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + reward_count));
/*      */         }
/* 1525 */         if (reward_type.equals("TKT_RECRUIT")) {
/* 1526 */           UserSyncData.getJSONObject("status").put("recruitLicense", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("recruitLicense") + reward_count));
/*      */         }
/* 1528 */         JSONObject normal_reward = new JSONObject(true);
/* 1529 */         normal_reward.put("count", Integer.valueOf(reward_count));
/* 1530 */         normal_reward.put("id", reward_id);
/* 1531 */         normal_reward.put("type", reward_type);
/* 1532 */         unusualRewards.add(normal_reward);
/*      */       } 
/*      */ 
/*      */       
/* 1536 */       if (occPercent == 3 && dropType == 3) {
/*      */         
/* 1538 */         JSONArray dropArray = new JSONArray();
/*      */         
/* 1540 */         IntStream.range(0, 5 + Percent).forEach(n -> dropArray.add(Integer.valueOf(1)));
/* 1541 */         IntStream.range(0, 95 + addPercent).forEach(n -> dropArray.add(Integer.valueOf(0)));
/*      */         
/* 1543 */         Collections.shuffle((List<?>)dropArray);
/*      */         
/* 1545 */         int cur = dropArray.getIntValue((new Random()).nextInt(dropArray.size()));
/*      */         
/* 1547 */         if (cur == 1) {
/* 1548 */           if (reward_type.equals("MATERIAL")) {
/* 1549 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1551 */           if (reward_type.equals("CARD_EXP")) {
/* 1552 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1554 */           if (reward_type.equals("DIAMOND")) {
/* 1555 */             UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + reward_count));
/* 1556 */             UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + reward_count));
/*      */           } 
/* 1558 */           if (reward_type.equals("GOLD")) {
/* 1559 */             UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + reward_count));
/*      */           }
/* 1561 */           if (reward_type.equals("TKT_RECRUIT")) {
/* 1562 */             UserSyncData.getJSONObject("status").put("recruitLicense", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("recruitLicense") + reward_count));
/*      */           }
/* 1564 */           JSONObject normal_reward = new JSONObject(true);
/* 1565 */           normal_reward.put("count", Integer.valueOf(reward_count));
/* 1566 */           normal_reward.put("id", reward_id);
/* 1567 */           normal_reward.put("type", reward_type);
/* 1568 */           unusualRewards.add(normal_reward);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1573 */       if (occPercent == 4 && dropType == 3) {
/*      */         
/* 1575 */         JSONArray dropArray = new JSONArray();
/*      */         
/* 1577 */         IntStream.range(0, 5 + Percent).forEach(n -> dropArray.add(Integer.valueOf(1)));
/* 1578 */         IntStream.range(0, 95 + addPercent).forEach(n -> dropArray.add(Integer.valueOf(0)));
/*      */         
/* 1580 */         Collections.shuffle((List<?>)dropArray);
/*      */         
/* 1582 */         int cur = dropArray.getIntValue((new Random()).nextInt(dropArray.size()));
/*      */         
/* 1584 */         if (cur == 1) {
/* 1585 */           if (reward_type.equals("MATERIAL")) {
/* 1586 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1588 */           if (reward_type.equals("CARD_EXP")) {
/* 1589 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1591 */           if (reward_type.equals("DIAMOND")) {
/* 1592 */             UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + reward_count));
/* 1593 */             UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + reward_count));
/*      */           } 
/* 1595 */           if (reward_type.equals("GOLD")) {
/* 1596 */             UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + reward_count));
/*      */           }
/* 1598 */           if (reward_type.equals("TKT_RECRUIT")) {
/* 1599 */             UserSyncData.getJSONObject("status").put("recruitLicense", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("recruitLicense") + reward_count));
/*      */           }
/* 1601 */           JSONObject normal_reward = new JSONObject(true);
/* 1602 */           normal_reward.put("count", Integer.valueOf(reward_count));
/* 1603 */           normal_reward.put("id", reward_id);
/* 1604 */           normal_reward.put("type", reward_type);
/* 1605 */           unusualRewards.add(normal_reward);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1610 */       if (occPercent == 3 && dropType == 4) {
/*      */         
/* 1612 */         JSONArray dropArray = new JSONArray();
/*      */         
/* 1614 */         IntStream.range(0, 5 + Percent).forEach(n -> dropArray.add(Integer.valueOf(1)));
/* 1615 */         IntStream.range(0, 95 + addPercent).forEach(n -> dropArray.add(Integer.valueOf(0)));
/*      */         
/* 1617 */         Collections.shuffle((List<?>)dropArray);
/*      */         
/* 1619 */         int cur = dropArray.getIntValue((new Random()).nextInt(dropArray.size()));
/*      */         
/* 1621 */         if (cur == 1) {
/* 1622 */           if (reward_type.equals("MATERIAL")) {
/* 1623 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1625 */           if (reward_type.equals("CARD_EXP")) {
/* 1626 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1628 */           if (reward_type.equals("DIAMOND")) {
/* 1629 */             UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + reward_count));
/* 1630 */             UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + reward_count));
/*      */           } 
/* 1632 */           if (reward_type.equals("GOLD")) {
/* 1633 */             UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + reward_count));
/*      */           }
/* 1635 */           if (reward_type.equals("TKT_RECRUIT")) {
/* 1636 */             UserSyncData.getJSONObject("status").put("recruitLicense", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("recruitLicense") + reward_count));
/*      */           }
/* 1638 */           JSONObject normal_reward = new JSONObject(true);
/* 1639 */           normal_reward.put("count", Integer.valueOf(reward_count));
/* 1640 */           normal_reward.put("id", reward_id);
/* 1641 */           normal_reward.put("type", reward_type);
/* 1642 */           additionalRewards.add(normal_reward);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1647 */       if (occPercent == 4 && dropType == 4) {
/*      */         
/* 1649 */         JSONArray dropArray = new JSONArray();
/*      */         
/* 1651 */         IntStream.range(0, 25 + Percent).forEach(n -> dropArray.add(Integer.valueOf(1)));
/* 1652 */         IntStream.range(0, 75 + addPercent).forEach(n -> dropArray.add(Integer.valueOf(0)));
/*      */         
/* 1654 */         Collections.shuffle((List<?>)dropArray);
/*      */         
/* 1656 */         int cur = dropArray.getIntValue((new Random()).nextInt(dropArray.size()));
/*      */         
/* 1658 */         if (cur == 1) {
/* 1659 */           if (reward_type.equals("MATERIAL")) {
/* 1660 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1662 */           if (reward_type.equals("CARD_EXP")) {
/* 1663 */             UserSyncData.getJSONObject("inventory").put(reward_id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(reward_id) + reward_count));
/*      */           }
/* 1665 */           if (reward_type.equals("DIAMOND")) {
/* 1666 */             UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + reward_count));
/* 1667 */             UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + reward_count));
/*      */           } 
/* 1669 */           if (reward_type.equals("GOLD")) {
/* 1670 */             UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + reward_count));
/*      */           }
/* 1672 */           if (reward_type.equals("TKT_RECRUIT")) {
/* 1673 */             UserSyncData.getJSONObject("status").put("recruitLicense", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("recruitLicense") + reward_count));
/*      */           }
/* 1675 */           JSONObject normal_reward = new JSONObject(true);
/* 1676 */           normal_reward.put("count", Integer.valueOf(reward_count));
/* 1677 */           normal_reward.put("id", reward_id);
/* 1678 */           normal_reward.put("type", reward_type);
/* 1679 */           additionalRewards.add(normal_reward);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1685 */     result.put("rewards", rewards);
/* 1686 */     result.put("additionalRewards", additionalRewards);
/* 1687 */     result.put("unusualRewards", unusualRewards);
/* 1688 */     result.put("furnitureRewards", furnitureRewards);
/*      */ 
/*      */ 
/*      */     
/* 1692 */     int completeFavor = stage_table.getIntValue("completeFavor");
/* 1693 */     int passFavor = stage_table.getIntValue("passFavor");
/*      */     
/* 1695 */     JSONObject charList = BattleData.getJSONObject("battleData").getJSONObject("stats").getJSONObject("charList");
/*      */     
/* 1697 */     for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)charList.entrySet()) {
/* 1698 */       String instId = entry.getKey();
/* 1699 */       if (UserSyncData.getJSONObject("troop").getJSONObject("chars").containsKey(instId)) {
/* 1700 */         JSONObject charData = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(instId);
/* 1701 */         String charId = charData.getString("charId");
/* 1702 */         int charFavor = charData.getIntValue("favorPoint");
/*      */         
/* 1704 */         if (completeState == 3 || completeState == 4) {
/* 1705 */           charData.put("favorPoint", Integer.valueOf(charFavor + completeFavor));
/* 1706 */           if (UserSyncData.getJSONObject("troop").getJSONObject("charGroup").containsKey(charId))
/* 1707 */             UserSyncData.getJSONObject("troop").getJSONObject("charGroup").getJSONObject(charId).put("favorPoint", Integer.valueOf(charFavor + completeFavor)); 
/*      */           continue;
/*      */         } 
/* 1710 */         charData.put("favorPoint", Integer.valueOf(charFavor + passFavor));
/* 1711 */         if (UserSyncData.getJSONObject("troop").getJSONObject("charGroup").containsKey(charId)) {
/* 1712 */           UserSyncData.getJSONObject("troop").getJSONObject("charGroup").getJSONObject(charId).put("favorPoint", Integer.valueOf(charFavor + passFavor));
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1718 */     JSONObject playerDataDelta = new JSONObject(true);
/* 1719 */     JSONObject modified = new JSONObject(true);
/* 1720 */     JSONObject dungeon = new JSONObject(true);
/* 1721 */     JSONObject stages = new JSONObject(true);
/*      */     
/* 1723 */     for (int j = 0; j < unlockStagesObject.size(); j++) {
/* 1724 */       String unlock_stageId = unlockStagesObject.getJSONObject(j).getString("stageId");
/* 1725 */       stages.put(unlock_stageId, UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(unlock_stageId));
/*      */     } 
/* 1727 */     stages.put(stageId, UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId));
/*      */     
/* 1729 */     dungeon.put("stages", stages);
/* 1730 */     modified.put("dungeon", dungeon);
/* 1731 */     modified.put("status", UserSyncData.getJSONObject("status"));
/* 1732 */     modified.put("troop", troop);
/* 1733 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 1734 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 1735 */     playerDataDelta.put("modified", modified);
/*      */     
/* 1737 */     result.put("playerDataDelta", playerDataDelta);
/*      */     
/* 1739 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 1741 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/squadFormation"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject SquadFormation(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 1748 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 1749 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /quest/squadFormation");
/*      */     
/* 1751 */     if (!ArknightsApplication.enableServer) {
/* 1752 */       response.setStatus(400);
/* 1753 */       JSONObject jSONObject = new JSONObject(true);
/* 1754 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 1755 */       jSONObject.put("error", "Bad Request");
/* 1756 */       jSONObject.put("message", "server is close");
/* 1757 */       return jSONObject;
/*      */     } 
/*      */     
/* 1760 */     String squadId = JsonBody.getString("squadId");
/*      */ 
/*      */ 
/*      */     
/* 1764 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 1765 */     if (Accounts.size() != 1) {
/* 1766 */       JSONObject jSONObject = new JSONObject(true);
/* 1767 */       jSONObject.put("result", Integer.valueOf(2));
/* 1768 */       jSONObject.put("error", "无法查询到此账户");
/* 1769 */       return jSONObject;
/*      */     } 
/*      */     
/* 1772 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 1774 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 1775 */       response.setStatus(500);
/* 1776 */       JSONObject jSONObject = new JSONObject(true);
/* 1777 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 1778 */       jSONObject.put("error", "Bad Request");
/* 1779 */       jSONObject.put("message", "error");
/* 1780 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/* 1784 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 1786 */     UserSyncData.getJSONObject("troop").getJSONObject("squads").getJSONObject(squadId).put("slots", JsonBody.getJSONArray("slots"));
/*      */     
/* 1788 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 1790 */     JSONObject result = new JSONObject(true);
/* 1791 */     JSONObject playerDataDelta = new JSONObject(true);
/* 1792 */     JSONObject modified = new JSONObject(true);
/* 1793 */     JSONObject troop = new JSONObject(true);
/* 1794 */     JSONObject squads = new JSONObject(true);
/* 1795 */     JSONObject squad = UserSyncData.getJSONObject("troop").getJSONObject("squads").getJSONObject(squadId);
/* 1796 */     squads.put(squadId, squad);
/* 1797 */     troop.put("squads", squads);
/* 1798 */     modified.put("troop", troop);
/* 1799 */     playerDataDelta.put("modified", modified);
/* 1800 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 1801 */     result.put("playerDataDelta", playerDataDelta);
/* 1802 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/saveBattleReplay"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject SaveBattleReplay(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 1809 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 1810 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /quest/saveBattleReplay");
/*      */     
/* 1812 */     if (!ArknightsApplication.enableServer) {
/* 1813 */       response.setStatus(400);
/* 1814 */       JSONObject jSONObject = new JSONObject(true);
/* 1815 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 1816 */       jSONObject.put("error", "Bad Request");
/* 1817 */       jSONObject.put("message", "server is close");
/* 1818 */       return jSONObject;
/*      */     } 
/*      */     
/* 1821 */     JSONObject BattleData = Utils.BattleReplay_decrypt(JsonBody.getString("battleReplay"));
/*      */ 
/*      */ 
/*      */     
/* 1825 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 1826 */     if (Accounts.size() != 1) {
/* 1827 */       JSONObject jSONObject = new JSONObject(true);
/* 1828 */       jSONObject.put("result", Integer.valueOf(2));
/* 1829 */       jSONObject.put("error", "无法查询到此账户");
/* 1830 */       return jSONObject;
/*      */     } 
/*      */     
/* 1833 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 1835 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 1836 */       response.setStatus(500);
/* 1837 */       JSONObject jSONObject = new JSONObject(true);
/* 1838 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 1839 */       jSONObject.put("error", "Bad Request");
/* 1840 */       jSONObject.put("message", "error");
/* 1841 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/* 1845 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */ 
/*      */     
/* 1848 */     String stageId = BattleData.getJSONObject("journal").getJSONObject("metadata").getString("stageId");
/* 1849 */     JSONObject stages_data = UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId);
/*      */     
/* 1851 */     stages_data.put("hasBattleReplay", Integer.valueOf(1));
/* 1852 */     stages_data.put("battleReplay", JsonBody.getString("battleReplay"));
/*      */     
/* 1854 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 1856 */     JSONObject result = new JSONObject(true);
/* 1857 */     JSONObject playerDataDelta = new JSONObject(true);
/* 1858 */     JSONObject modified = new JSONObject(true);
/* 1859 */     JSONObject dungeon = new JSONObject(true);
/* 1860 */     JSONObject stages = new JSONObject(true);
/* 1861 */     stages.put(stageId, UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId));
/*      */     
/* 1863 */     dungeon.put("stages", stages);
/* 1864 */     modified.put("dungeon", dungeon);
/* 1865 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 1866 */     playerDataDelta.put("modified", modified);
/* 1867 */     result.put("playerDataDelta", playerDataDelta);
/* 1868 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/getBattleReplay"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject GetBattleReplay(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 1875 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 1876 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /quest/getBattleReplay");
/*      */     
/* 1878 */     if (!ArknightsApplication.enableServer) {
/* 1879 */       response.setStatus(400);
/* 1880 */       JSONObject jSONObject = new JSONObject(true);
/* 1881 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 1882 */       jSONObject.put("error", "Bad Request");
/* 1883 */       jSONObject.put("message", "server is close");
/* 1884 */       return jSONObject;
/*      */     } 
/*      */     
/* 1887 */     String stageId = JsonBody.getString("stageId");
/*      */ 
/*      */ 
/*      */     
/* 1891 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 1892 */     if (Accounts.size() != 1) {
/* 1893 */       JSONObject jSONObject = new JSONObject(true);
/* 1894 */       jSONObject.put("result", Integer.valueOf(2));
/* 1895 */       jSONObject.put("error", "无法查询到此账户");
/* 1896 */       return jSONObject;
/*      */     } 
/*      */     
/* 1899 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 1901 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 1902 */       response.setStatus(500);
/* 1903 */       JSONObject jSONObject = new JSONObject(true);
/* 1904 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 1905 */       jSONObject.put("error", "Bad Request");
/* 1906 */       jSONObject.put("message", "error");
/* 1907 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/* 1911 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 1913 */     JSONObject result = new JSONObject(true);
/* 1914 */     JSONObject playerDataDelta = new JSONObject(true);
/* 1915 */     JSONObject modified = new JSONObject(true);
/*      */     
/* 1917 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 1918 */     playerDataDelta.put("modified", modified);
/* 1919 */     result.put("playerDataDelta", playerDataDelta);
/* 1920 */     result.put("battleReplay", UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).getString("battleReplay"));
/* 1921 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/changeSquadName"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject ChangeSquadName(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 1929 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 1930 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /quest/changeSquadName");
/*      */     
/* 1932 */     if (!ArknightsApplication.enableServer) {
/* 1933 */       response.setStatus(400);
/* 1934 */       JSONObject jSONObject = new JSONObject(true);
/* 1935 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 1936 */       jSONObject.put("error", "Bad Request");
/* 1937 */       jSONObject.put("message", "server is close");
/* 1938 */       return jSONObject;
/*      */     } 
/*      */     
/* 1941 */     String squadId = JsonBody.getString("squadId");
/* 1942 */     String name = JsonBody.getString("name");
/*      */ 
/*      */ 
/*      */     
/* 1946 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 1947 */     if (Accounts.size() != 1) {
/* 1948 */       JSONObject jSONObject = new JSONObject(true);
/* 1949 */       jSONObject.put("result", Integer.valueOf(2));
/* 1950 */       jSONObject.put("error", "无法查询到此账户");
/* 1951 */       return jSONObject;
/*      */     } 
/*      */     
/* 1954 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 1956 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 1957 */       response.setStatus(500);
/* 1958 */       JSONObject jSONObject = new JSONObject(true);
/* 1959 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 1960 */       jSONObject.put("error", "Bad Request");
/* 1961 */       jSONObject.put("message", "error");
/* 1962 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/* 1966 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 1968 */     UserSyncData.getJSONObject("troop").getJSONObject("squads").getJSONObject(squadId).put("name", name);
/*      */     
/* 1970 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 1972 */     JSONObject result = new JSONObject(true);
/* 1973 */     JSONObject playerDataDelta = new JSONObject(true);
/* 1974 */     JSONObject modified = new JSONObject(true);
/* 1975 */     JSONObject troop = new JSONObject(true);
/* 1976 */     JSONObject squads = new JSONObject(true);
/* 1977 */     JSONObject squad = UserSyncData.getJSONObject("troop").getJSONObject("squads").getJSONObject(squadId);
/* 1978 */     squads.put(squadId, squad);
/* 1979 */     troop.put("squads", squads);
/* 1980 */     modified.put("troop", troop);
/* 1981 */     playerDataDelta.put("modified", modified);
/* 1982 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 1983 */     result.put("playerDataDelta", playerDataDelta);
/* 1984 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/getAssistList"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject getAssistList(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 1991 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 1992 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /quest/getAssistList");
/*      */     
/* 1994 */     if (!ArknightsApplication.enableServer) {
/* 1995 */       response.setStatus(400);
/* 1996 */       JSONObject jSONObject = new JSONObject(true);
/* 1997 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 1998 */       jSONObject.put("error", "Bad Request");
/* 1999 */       jSONObject.put("message", "server is close");
/* 2000 */       return jSONObject;
/*      */     } 
/*      */     
/* 2003 */     String profession = JsonBody.getString("profession");
/*      */     
/* 2005 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 2006 */     if (Accounts.size() != 1) {
/* 2007 */       JSONObject jSONObject = new JSONObject(true);
/* 2008 */       jSONObject.put("result", Integer.valueOf(2));
/* 2009 */       jSONObject.put("error", "无法查询到此账户");
/* 2010 */       return jSONObject;
/*      */     } 
/*      */     
/* 2013 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 2014 */       response.setStatus(500);
/* 2015 */       JSONObject jSONObject = new JSONObject(true);
/* 2016 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 2017 */       jSONObject.put("error", "Bad Request");
/* 2018 */       jSONObject.put("message", "error");
/* 2019 */       return jSONObject;
/*      */     } 
/*      */     
/* 2022 */     JSONArray assistCharArray = new JSONArray();
/* 2023 */     JSONArray assistList = new JSONArray();
/*      */     
/* 2025 */     long uid = ((Account)Accounts.get(0)).getUid();
/* 2026 */     JSONArray FriendList = JSONObject.parseObject(((Account)Accounts.get(0)).getFriend()).getJSONArray("list");
/*      */     
/* 2028 */     JSONArray FriendArray = new JSONArray();
/*      */     
/* 2030 */     Collections.shuffle((List<?>)FriendList);
/* 2031 */     for (int i = 0; i < FriendList.size(); i++) {
/*      */       
/* 2033 */       if (assistList.size() == 6) {
/*      */         break;
/*      */       }
/*      */       
/* 2037 */       long friendUid = FriendList.getJSONObject(i).getLongValue("uid");
/* 2038 */       String friendAlias = FriendList.getJSONObject(i).getString("alias");
/*      */       
/* 2040 */       FriendArray.add(Long.valueOf(friendUid));
/*      */       
/* 2042 */       List<UserInfo> userInfo = userDao.queryUserInfo(friendUid);
/*      */       
/* 2044 */       JSONArray userSocialAssistCharList = JSONArray.parseArray(((UserInfo)userInfo.get(0)).getSocialAssistCharList());
/* 2045 */       JSONObject userAssistCharList = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getAssistCharList());
/* 2046 */       JSONObject userStatus = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getStatus());
/* 2047 */       JSONObject chars = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getChars());
/*      */       
/* 2049 */       if (userAssistCharList.containsKey(profession)) {
/*      */         
/* 2051 */         JSONArray charList = userAssistCharList.getJSONArray(profession);
/* 2052 */         Collections.shuffle((List<?>)charList);
/* 2053 */         JSONObject assistCharData = charList.getJSONObject(0);
/*      */         
/* 2055 */         String charId = assistCharData.getString("charId");
/* 2056 */         String charInstId = assistCharData.getString("charInstId");
/*      */         
/* 2058 */         if (!assistCharArray.contains(charId)) {
/* 2059 */           assistCharArray.add(charId);
/*      */           
/* 2061 */           JSONArray assistCharList = new JSONArray();
/*      */           
/* 2063 */           JSONObject assistInfo = new JSONObject();
/* 2064 */           assistInfo.put("aliasName", friendAlias);
/* 2065 */           assistInfo.put("avatarId", Integer.valueOf(userStatus.getIntValue("avatarId")));
/* 2066 */           assistInfo.put("avatar", userStatus.getJSONObject("avatar"));
/* 2067 */           assistInfo.put("canRequestFriend", Boolean.valueOf(false));
/* 2068 */           assistInfo.put("isFriend", Boolean.valueOf(true));
/* 2069 */           assistInfo.put("lastOnlineTime", Integer.valueOf(userStatus.getIntValue("lastOnlineTs")));
/* 2070 */           assistInfo.put("level", Integer.valueOf(userStatus.getIntValue("level")));
/* 2071 */           assistInfo.put("nickName", userStatus.getString("nickName"));
/* 2072 */           assistInfo.put("nickNumber", userStatus.getString("nickNumber"));
/* 2073 */           assistInfo.put("uid", Long.valueOf(friendUid));
/* 2074 */           assistInfo.put("powerScore", Integer.valueOf(140));
/*      */           
/* 2076 */           for (int m = 0; m < userSocialAssistCharList.size(); m++) {
/* 2077 */             if (userSocialAssistCharList.getJSONObject(m) != null) {
/* 2078 */               JSONObject charData = chars.getJSONObject(userSocialAssistCharList.getJSONObject(m).getString("charInstId"));
/* 2079 */               charData.put("skillIndex", Integer.valueOf(userSocialAssistCharList.getJSONObject(m).getIntValue("skillIndex")));
/* 2080 */               assistCharList.add(charData);
/* 2081 */               if (userSocialAssistCharList.getJSONObject(m).getString("charInstId").equals(charInstId)) {
/* 2082 */                 assistInfo.put("assistSlotIndex", Integer.valueOf(m));
/*      */               }
/*      */             } 
/*      */           } 
/* 2086 */           assistInfo.put("assistCharList", assistCharList);
/* 2087 */           assistList.add(assistInfo);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2092 */     List<SearchAssistCharList> searchAssist = userDao.SearchAssistCharList("$." + profession);
/*      */     int j;
/* 2094 */     for (j = 0; j < searchAssist.size(); j++) {
/* 2095 */       if (((SearchAssistCharList)searchAssist.get(j)).getUid() == uid) {
/* 2096 */         ((SearchAssistCharList)searchAssist.get(j)).setUid(-1L);
/*      */       }
/* 2098 */       if (FriendArray.contains(Long.valueOf(((SearchAssistCharList)searchAssist.get(j)).getUid()))) {
/* 2099 */         ((SearchAssistCharList)searchAssist.get(j)).setUid(-1L);
/*      */       }
/*      */     } 
/*      */     
/* 2103 */     Collections.shuffle(searchAssist);
/*      */     
/* 2105 */     for (j = 0; j < searchAssist.size(); j++) {
/*      */       
/* 2107 */       long friendUid = ((SearchAssistCharList)searchAssist.get(j)).getUid();
/*      */       
/* 2109 */       if (friendUid != -1L) {
/*      */         
/* 2111 */         if (assistList.size() == 9) {
/*      */           break;
/*      */         }
/*      */         
/* 2115 */         JSONArray userSocialAssistCharList = JSONArray.parseArray(((SearchAssistCharList)searchAssist.get(j)).getSocialAssistCharList());
/* 2116 */         JSONArray charList = JSONArray.parseArray(((SearchAssistCharList)searchAssist.get(j)).getAssistCharList());
/* 2117 */         JSONObject userStatus = JSONObject.parseObject(((SearchAssistCharList)searchAssist.get(j)).getStatus());
/* 2118 */         JSONObject chars = JSONObject.parseObject(((SearchAssistCharList)searchAssist.get(j)).getChars());
/*      */         
/* 2120 */         Collections.shuffle((List<?>)charList);
/* 2121 */         JSONObject assistCharData = charList.getJSONObject(0);
/*      */         
/* 2123 */         String charId = assistCharData.getString("charId");
/* 2124 */         String charInstId = assistCharData.getString("charInstId");
/*      */         
/* 2126 */         if (!assistCharArray.contains(charId)) {
/* 2127 */           assistCharArray.add(charId);
/*      */           
/* 2129 */           JSONArray assistCharList = new JSONArray();
/*      */           
/* 2131 */           JSONObject assistInfo = new JSONObject();
/* 2132 */           assistInfo.put("aliasName", "");
/* 2133 */           assistInfo.put("avatarId", Integer.valueOf(userStatus.getIntValue("avatarId")));
/* 2134 */           assistInfo.put("avatar", userStatus.getJSONObject("avatar"));
/* 2135 */           assistInfo.put("canRequestFriend", Boolean.valueOf(true));
/* 2136 */           assistInfo.put("isFriend", Boolean.valueOf(false));
/* 2137 */           assistInfo.put("lastOnlineTime", Integer.valueOf(userStatus.getIntValue("lastOnlineTs")));
/* 2138 */           assistInfo.put("level", Integer.valueOf(userStatus.getIntValue("level")));
/* 2139 */           assistInfo.put("nickName", userStatus.getString("nickName"));
/* 2140 */           assistInfo.put("nickNumber", userStatus.getString("nickNumber"));
/* 2141 */           assistInfo.put("uid", Long.valueOf(friendUid));
/* 2142 */           assistInfo.put("powerScore", Integer.valueOf(140));
/*      */           
/* 2144 */           for (int m = 0; m < userSocialAssistCharList.size(); m++) {
/* 2145 */             if (userSocialAssistCharList.getJSONObject(m) != null) {
/* 2146 */               JSONObject charData = chars.getJSONObject(userSocialAssistCharList.getJSONObject(m).getString("charInstId"));
/* 2147 */               charData.put("skillIndex", Integer.valueOf(userSocialAssistCharList.getJSONObject(m).getIntValue("skillIndex")));
/* 2148 */               assistCharList.add(charData);
/* 2149 */               if (userSocialAssistCharList.getJSONObject(m).getString("charInstId").equals(charInstId)) {
/* 2150 */                 assistInfo.put("assistSlotIndex", Integer.valueOf(m));
/*      */               }
/*      */             } 
/*      */           } 
/* 2154 */           assistInfo.put("assistCharList", assistCharList);
/* 2155 */           assistList.add(assistInfo);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2160 */     JSONObject result = new JSONObject(true);
/* 2161 */     JSONObject playerDataDelta = new JSONObject(true);
/* 2162 */     JSONObject modified = new JSONObject(true);
/* 2163 */     playerDataDelta.put("modified", modified);
/* 2164 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 2165 */     result.put("playerDataDelta", playerDataDelta);
/* 2166 */     result.put("allowAskTs", Integer.valueOf(1636483552));
/* 2167 */     result.put("assistList", assistList);
/* 2168 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PostMapping(value = {"/finishStoryStage"}, produces = {"application/json;charset=UTF-8"})
/*      */   public JSONObject finishStoryStage(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 2175 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 2176 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /quest/finishStoryStage");
/*      */     
/* 2178 */     if (!ArknightsApplication.enableServer) {
/* 2179 */       response.setStatus(400);
/* 2180 */       JSONObject jSONObject = new JSONObject(true);
/* 2181 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 2182 */       jSONObject.put("error", "Bad Request");
/* 2183 */       jSONObject.put("message", "server is close");
/* 2184 */       return jSONObject;
/*      */     } 
/*      */     
/* 2187 */     String stageId = JsonBody.getString("stageId");
/*      */ 
/*      */ 
/*      */     
/* 2191 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 2192 */     if (Accounts.size() != 1) {
/* 2193 */       JSONObject jSONObject = new JSONObject(true);
/* 2194 */       jSONObject.put("result", Integer.valueOf(2));
/* 2195 */       jSONObject.put("error", "无法查询到此账户");
/* 2196 */       return jSONObject;
/*      */     } 
/*      */     
/* 2199 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*      */     
/* 2201 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 2202 */       response.setStatus(500);
/* 2203 */       JSONObject jSONObject = new JSONObject(true);
/* 2204 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 2205 */       jSONObject.put("error", "Bad Request");
/* 2206 */       jSONObject.put("message", "error");
/* 2207 */       return jSONObject;
/*      */     } 
/*      */ 
/*      */     
/* 2211 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*      */     
/* 2213 */     int stage_state = UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).getIntValue("state");
/*      */     
/* 2215 */     JSONObject stageClear = ArknightsApplication.mainStage.getJSONObject(stageId);
/*      */     
/* 2217 */     JSONArray rewards = new JSONArray();
/* 2218 */     JSONArray unlockStages = new JSONArray();
/* 2219 */     JSONArray unlockStagesObject = new JSONArray();
/*      */     
/* 2221 */     JSONArray alert = new JSONArray();
/*      */ 
/*      */ 
/*      */     
/* 2225 */     int DropRate = ArknightsApplication.serverConfig.getJSONObject("battle").getIntValue("dropRate");
/*      */     
/* 2227 */     if (stage_state != 3) {
/* 2228 */       UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).put("state", Integer.valueOf(3));
/*      */ 
/*      */       
/* 2231 */       if (stageClear.getString("next") != null) {
/* 2232 */         String next = stageClear.getString("next");
/* 2233 */         JSONObject unlockStage = new JSONObject(true);
/* 2234 */         unlockStage.put("hasBattleReplay", Integer.valueOf(0));
/* 2235 */         unlockStage.put("noCostCnt", Integer.valueOf(0));
/* 2236 */         unlockStage.put("practiceTimes", Integer.valueOf(0));
/* 2237 */         unlockStage.put("completeTimes", Integer.valueOf(0));
/* 2238 */         unlockStage.put("state", Integer.valueOf(0));
/* 2239 */         unlockStage.put("stageId", next);
/* 2240 */         unlockStage.put("startTimes", Integer.valueOf(0));
/* 2241 */         UserSyncData.getJSONObject("dungeon").getJSONObject("stages").put(next, unlockStage);
/*      */         
/* 2243 */         unlockStages.add(next);
/* 2244 */         unlockStagesObject.add(unlockStage);
/*      */       } 
/*      */       
/* 2247 */       if (stageClear.getString("sub") != null) {
/* 2248 */         String sub = stageClear.getString("sub");
/* 2249 */         JSONObject sub_unlockStage = new JSONObject(true);
/* 2250 */         sub_unlockStage.put("hasBattleReplay", Integer.valueOf(0));
/* 2251 */         sub_unlockStage.put("noCostCnt", Integer.valueOf(0));
/* 2252 */         sub_unlockStage.put("practiceTimes", Integer.valueOf(0));
/* 2253 */         sub_unlockStage.put("completeTimes", Integer.valueOf(0));
/* 2254 */         sub_unlockStage.put("state", Integer.valueOf(0));
/* 2255 */         sub_unlockStage.put("stageId", sub);
/* 2256 */         sub_unlockStage.put("startTimes", Integer.valueOf(0));
/*      */         
/* 2258 */         UserSyncData.getJSONObject("dungeon").getJSONObject("stages").put(sub, sub_unlockStage);
/* 2259 */         unlockStages.add(sub);
/* 2260 */         unlockStagesObject.add(sub_unlockStage);
/*      */       } 
/*      */       
/* 2263 */       if (stageClear.getString("star") != null) {
/* 2264 */         String star = stageClear.getString("star");
/* 2265 */         JSONObject star_unlockStage = new JSONObject(true);
/* 2266 */         star_unlockStage.put("hasBattleReplay", Integer.valueOf(0));
/* 2267 */         star_unlockStage.put("noCostCnt", Integer.valueOf(0));
/* 2268 */         star_unlockStage.put("practiceTimes", Integer.valueOf(0));
/* 2269 */         star_unlockStage.put("completeTimes", Integer.valueOf(0));
/* 2270 */         star_unlockStage.put("state", Integer.valueOf(0));
/* 2271 */         star_unlockStage.put("stageId", star);
/* 2272 */         star_unlockStage.put("startTimes", Integer.valueOf(0));
/*      */         
/* 2274 */         UserSyncData.getJSONObject("dungeon").getJSONObject("stages").put(star, star_unlockStage);
/* 2275 */         unlockStages.add(star);
/* 2276 */         unlockStagesObject.add(star_unlockStage);
/*      */       } 
/*      */       
/* 2279 */       if (stageClear.getString("hard") != null) {
/* 2280 */         String hard = stageClear.getString("hard");
/* 2281 */         JSONObject hard_unlockStage = new JSONObject(true);
/* 2282 */         hard_unlockStage.put("hasBattleReplay", Integer.valueOf(0));
/* 2283 */         hard_unlockStage.put("noCostCnt", Integer.valueOf(0));
/* 2284 */         hard_unlockStage.put("practiceTimes", Integer.valueOf(0));
/* 2285 */         hard_unlockStage.put("completeTimes", Integer.valueOf(0));
/* 2286 */         hard_unlockStage.put("state", Integer.valueOf(0));
/* 2287 */         hard_unlockStage.put("stageId", hard);
/* 2288 */         hard_unlockStage.put("startTimes", Integer.valueOf(0));
/*      */         
/* 2290 */         UserSyncData.getJSONObject("dungeon").getJSONObject("stages").put(hard, hard_unlockStage);
/* 2291 */         unlockStages.add(hard);
/* 2292 */         unlockStagesObject.add(hard_unlockStage);
/*      */       } 
/*      */       
/* 2295 */       JSONObject reward = new JSONObject(true);
/*      */       
/* 2297 */       reward.put("type", "DIAMOND");
/* 2298 */       reward.put("id", "4002");
/* 2299 */       reward.put("count", Integer.valueOf(1 * DropRate));
/*      */       
/* 2301 */       rewards.add(reward);
/*      */       
/* 2303 */       UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + 1 * DropRate));
/* 2304 */       UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + 1 * DropRate));
/*      */     } 
/*      */     
/* 2307 */     JSONObject result = new JSONObject(true);
/* 2308 */     JSONObject playerDataDelta = new JSONObject(true);
/* 2309 */     JSONObject modified = new JSONObject(true);
/* 2310 */     JSONObject status = new JSONObject(true);
/* 2311 */     status.put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond")));
/* 2312 */     status.put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond")));
/*      */     
/* 2314 */     JSONObject dungeon = new JSONObject(true);
/* 2315 */     JSONObject stages = new JSONObject(true);
/*      */     
/* 2317 */     for (int i = 0; i < unlockStagesObject.size(); i++) {
/* 2318 */       String unlock_stageId = unlockStagesObject.getJSONObject(i).getString("stageId");
/* 2319 */       stages.put(unlock_stageId, UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(unlock_stageId));
/*      */     } 
/* 2321 */     stages.put(stageId, UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId));
/*      */     
/* 2323 */     dungeon.put("stages", stages);
/*      */     
/* 2325 */     modified.put("status", status);
/* 2326 */     modified.put("dungeon", dungeon);
/* 2327 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 2328 */     playerDataDelta.put("modified", modified);
/* 2329 */     result.put("playerDataDelta", playerDataDelta);
/* 2330 */     result.put("rewards", rewards);
/* 2331 */     result.put("unlockStages", unlockStages);
/* 2332 */     result.put("alert", alert);
/* 2333 */     result.put("result", Integer.valueOf(0));
/*      */     
/* 2335 */     userDao.setUserData(uid, UserSyncData);
/*      */     
/* 2337 */     return result;
/*      */   }
/*      */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\quest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */