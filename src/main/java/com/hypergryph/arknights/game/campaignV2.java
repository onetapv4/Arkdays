/*     */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.decrypt.Utils;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestHeader;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/campaignV2"})
/*     */ public class campaignV2
/*     */ {
/*     */   @PostMapping(value = {"/battleStart"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject BattleStart(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/*  29 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  30 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /campaignV2/battleStart");
/*     */     
/*  32 */     if (!ArknightsApplication.enableServer) {
/*  33 */       response.setStatus(400);
/*  34 */       JSONObject jSONObject = new JSONObject(true);
/*  35 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  36 */       jSONObject.put("error", "Bad Request");
/*  37 */       jSONObject.put("message", "server is close");
/*  38 */       return jSONObject;
/*     */     } 
/*     */     
/*  41 */     String stageId = JsonBody.getString("stageId");
/*  42 */     int isReplay = JsonBody.getIntValue("isReplay");
/*  43 */     int startTs = JsonBody.getIntValue("startTs");
/*  44 */     int usePracticeTicket = JsonBody.getIntValue("usePracticeTicket");
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
/*  66 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*  67 */     JSONObject stage_table = ArknightsApplication.stageTable.getJSONObject(stageId);
/*     */     
/*  69 */     if (!UserSyncData.getJSONObject("dungeon").getJSONObject("stages").containsKey(stageId)) {
/*  70 */       JSONObject stagesData = new JSONObject(true);
/*  71 */       stagesData.put("completeTimes", Integer.valueOf(0));
/*  72 */       stagesData.put("hasBattleReplay", Integer.valueOf(0));
/*  73 */       stagesData.put("noCostCnt", Integer.valueOf(1));
/*  74 */       stagesData.put("practiceTimes", Integer.valueOf(0));
/*  75 */       stagesData.put("stageId", stageId);
/*  76 */       stagesData.put("startTimes", Integer.valueOf(0));
/*  77 */       stagesData.put("state", Integer.valueOf(0));
/*     */       
/*  79 */       UserSyncData.getJSONObject("dungeon").getJSONObject("stages").put(stageId, stagesData);
/*     */     } 
/*     */     
/*  82 */     if (usePracticeTicket == 1) {
/*  83 */       UserSyncData.getJSONObject("status").put("practiceTicket", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("practiceTicket") - 1));
/*  84 */       UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).put("practiceTimes", Integer.valueOf(1));
/*     */     } 
/*     */     
/*  87 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/*  89 */     JSONObject result = new JSONObject(true);
/*  90 */     JSONObject playerDataDelta = new JSONObject(true);
/*  91 */     JSONObject modified = new JSONObject(true);
/*  92 */     JSONObject dungeon = new JSONObject(true);
/*  93 */     JSONObject stages = new JSONObject(true);
/*     */     
/*  95 */     stages.put(stageId, UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId));
/*     */ 
/*     */     
/*  98 */     dungeon.put("stages", stages);
/*  99 */     modified.put("dungeon", dungeon);
/* 100 */     modified.put("status", UserSyncData.getJSONObject("status"));
/* 101 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 102 */     playerDataDelta.put("modified", modified);
/* 103 */     result.put("playerDataDelta", playerDataDelta);
/* 104 */     result.put("result", Integer.valueOf(0));
/* 105 */     result.put("battleId", stageId);
/*     */     
/* 107 */     result.put("isApProtect", Integer.valueOf(0));
/* 108 */     result.put("apFailReturn", Integer.valueOf(stage_table.getIntValue("apFailReturn")));
/*     */     
/* 110 */     if (UserSyncData.getJSONObject("dungeon").getJSONObject("stages").getJSONObject(stageId).getIntValue("noCostCnt") == 1) {
/* 111 */       result.put("isApProtect", Integer.valueOf(1));
/* 112 */       result.put("apFailReturn", Integer.valueOf(stage_table.getIntValue("apCost")));
/*     */     } 
/*     */     
/* 115 */     if (stage_table.getIntValue("apCost") == 0) {
/* 116 */       result.put("isApProtect", Integer.valueOf(0));
/* 117 */       result.put("apFailReturn", Integer.valueOf(0));
/*     */     } 
/*     */     
/* 120 */     if (usePracticeTicket == 1) {
/* 121 */       result.put("isApProtect", Integer.valueOf(0));
/* 122 */       result.put("apFailReturn", Integer.valueOf(0));
/*     */     } 
/*     */     
/* 125 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/battleFinish"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject BattleFinish(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 132 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 133 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /campaignV2/battleFinish");
/*     */     
/* 135 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 136 */     if (Accounts.size() != 1) {
/* 137 */       JSONObject jSONObject = new JSONObject(true);
/* 138 */       jSONObject.put("result", Integer.valueOf(2));
/* 139 */       jSONObject.put("error", "无法查询到此账户");
/* 140 */       return jSONObject;
/*     */     } 
/*     */     
/* 143 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 145 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 146 */       response.setStatus(500);
/* 147 */       JSONObject jSONObject = new JSONObject(true);
/* 148 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 149 */       jSONObject.put("error", "Bad Request");
/* 150 */       jSONObject.put("message", "error");
/* 151 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 155 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 157 */     JSONObject BattleData = Utils.BattleData_decrypt(JsonBody.getString("data"), UserSyncData.getJSONObject("pushFlags").getString("status"));
/*     */     
/* 159 */     String stageId = BattleData.getString("battleId");
/*     */     
/* 161 */     int DropRate = ArknightsApplication.serverConfig.getJSONObject("battle").getIntValue("dropRate");
/*     */     
/* 163 */     int killCnt = BattleData.getIntValue("killCnt");
/* 164 */     int completeState = BattleData.getIntValue("completeState");
/*     */     
/* 166 */     if (ArknightsApplication.serverConfig.getJSONObject("battle").getBooleanValue("debug")) {
/* 167 */       killCnt = 400;
/*     */     }
/*     */     
/* 170 */     JSONArray firstRewards = new JSONArray();
/* 171 */     JSONObject diamondShard = new JSONObject(true);
/* 172 */     int count = 0;
/* 173 */     int apFailReturn = 0;
/*     */     
/* 175 */     if (killCnt < 100) {
/* 176 */       count = 0;
/* 177 */       apFailReturn = 25;
/* 178 */     } else if (killCnt < 200) {
/* 179 */       count = 80;
/* 180 */       apFailReturn = 17;
/* 181 */     } else if (killCnt < 250) {
/* 182 */       count = 155;
/* 183 */       apFailReturn = 10;
/* 184 */     } else if (killCnt < 300) {
/* 185 */       count = 200;
/* 186 */       apFailReturn = 7;
/* 187 */     } else if (killCnt < 325) {
/* 188 */       count = 235;
/* 189 */       apFailReturn = 5;
/* 190 */     } else if (killCnt < 350) {
/* 191 */       count = 275;
/* 192 */       apFailReturn = 3;
/* 193 */     } else if (killCnt < 375) {
/* 194 */       count = 300;
/* 195 */       apFailReturn = 2;
/* 196 */     } else if (killCnt < 400) {
/* 197 */       count = 330;
/* 198 */       apFailReturn = 1;
/* 199 */     } else if (killCnt == 400) {
/* 200 */       count = 365;
/* 201 */       apFailReturn = 0;
/*     */     } 
/*     */     
/* 204 */     int nowTime = (int)((new Date()).getTime() / 1000L);
/*     */     
/* 206 */     int addAp = (nowTime - UserSyncData.getJSONObject("status").getIntValue("lastApAddTime")) / 360;
/*     */     
/* 208 */     if (UserSyncData.getJSONObject("status").getIntValue("ap") < UserSyncData.getJSONObject("status").getIntValue("maxAp")) {
/* 209 */       if (UserSyncData.getJSONObject("status").getIntValue("ap") + addAp >= UserSyncData.getJSONObject("status").getIntValue("maxAp")) {
/* 210 */         UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("maxAp")));
/* 211 */         UserSyncData.getJSONObject("status").put("lastApAddTime", Integer.valueOf(nowTime));
/*     */       }
/* 213 */       else if (addAp != 0) {
/* 214 */         UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") + addAp));
/* 215 */         UserSyncData.getJSONObject("status").put("lastApAddTime", Integer.valueOf(nowTime));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 220 */     UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") - ArknightsApplication.stageTable.getJSONObject(stageId).getIntValue("apCost")));
/*     */     
/* 222 */     if (apFailReturn != 0) {
/* 223 */       UserSyncData.getJSONObject("status").put("ap", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("ap") + apFailReturn));
/* 224 */       UserSyncData.getJSONObject("status").put("lastApAddTime", Integer.valueOf(nowTime));
/*     */     } 
/*     */     
/* 227 */     diamondShard.put("count", Integer.valueOf(count * DropRate));
/* 228 */     diamondShard.put("id", "4003");
/* 229 */     diamondShard.put("type", "DIAMOND_SHD");
/*     */     
/* 231 */     firstRewards.add(diamondShard);
/*     */     
/* 233 */     UserSyncData.getJSONObject("status").put("diamondShard", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("diamondShard") + count * DropRate));
/*     */     
/* 235 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 237 */     JSONObject result = new JSONObject(true);
/* 238 */     JSONObject playerDataDelta = new JSONObject(true);
/* 239 */     JSONObject modified = new JSONObject(true);
/* 240 */     modified.put("status", UserSyncData.getJSONObject("status"));
/* 241 */     playerDataDelta.put("modified", modified);
/* 242 */     result.put("playerDataDelta", playerDataDelta);
/* 243 */     result.put("rewards", firstRewards);
/* 244 */     result.put("apFailReturn", Integer.valueOf(apFailReturn));
/* 245 */     result.put("result", Integer.valueOf(0));
/* 246 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\campaignV2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */