/*     */ package BOOT-INF.classes.com.hypergryph.arknights;
/*     */ 
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.file.IOTools;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/test"})
/*     */ public class test
/*     */ {
/*     */   @RequestMapping({"/set/map"})
/*     */   public String setMap() {
/*  45 */     long uid = 10000001L;
/*     */     
/*  47 */     List<Account> Accounts = userDao.queryAccountByUid(uid);
/*     */     
/*  49 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/*  51 */     JSONObject current = UserSyncData.getJSONObject("rlv2").getJSONObject("current");
/*     */     
/*  53 */     current.getJSONObject("map").put("zones", IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/map.json"));
/*     */     
/*  55 */     userDao.setUserData(Long.valueOf(uid), UserSyncData);
/*     */     
/*  57 */     return "ok";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/query"})
/*     */   public String query(@RequestParam String name) {
/*  65 */     ArknightsApplication.LOGGER.info(Integer.valueOf(userDao.queryNickName(name).size()));
/*     */     
/*  67 */     return "ok";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/set"})
/*     */   public String set(@RequestParam String str) {
/*  75 */     return "ok";
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/itemtype"})
/*     */   public JSONArray itemtype() {
/*  81 */     JSONObject itemtype = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/excel/item_table.json").getJSONObject("items");
/*  82 */     JSONArray type = new JSONArray();
/*  83 */     for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)itemtype.entrySet()) {
/*  84 */       String itemType = itemtype.getJSONObject(entry.getKey()).getString("itemType");
/*  85 */       if (!type.contains(itemType)) {
/*  86 */         type.add(itemType);
/*     */       }
/*     */     } 
/*  89 */     return type;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/sub"})
/*     */   public String sub() {
/*  95 */     String charId = "char_179_cgbird";
/*  96 */     String sub1 = charId.substring(charId.indexOf("_") + 1);
/*  97 */     String charName = sub1.substring(sub1.indexOf("_") + 1);
/*     */     
/*  99 */     return charName;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/pwd"})
/*     */   public String pwd(@RequestParam String GMKey) {
/* 105 */     return GMKey;
/*     */   }
/*     */   
/*     */   @RequestMapping({"/roguelike"})
/*     */   public String roguelike() {
/* 110 */     JSONObject jSONObject = new JSONObject();
/*     */     
/* 112 */     for (Map.Entry entry : ArknightsApplication.stageTable.entrySet()) {
/* 113 */       JSONObject stageData = ArknightsApplication.stageTable.getJSONObject(entry.getKey().toString());
/*     */       
/* 115 */       JSONObject stage = new JSONObject();
/* 116 */       stage.put("id", stageData.getString("stageId"));
/* 117 */       stage.put("linkedStageId", "");
/* 118 */       stage.put("levelId", stageData.getString("levelId"));
/* 119 */       stage.put("code", "ISW-" + stageData.getString("code"));
/* 120 */       stage.put("name", stageData.getString("name"));
/* 121 */       stage.put("loadingPicId", "loading_PCS");
/* 122 */       stage.put("isBoss", Integer.valueOf(0));
/* 123 */       stage.put("isElite", Integer.valueOf(0));
/* 124 */       stage.put("difficulty", stageData.getString("difficulty"));
/* 125 */       stage.put("enlargeId", null);
/* 126 */       stage.put("capsulePool", "pool_capsule_default");
/* 127 */       stage.put("capsuleProb", Double.valueOf(1.0D));
/* 128 */       stage.put("vutresProb", new JSONArray());
/* 129 */       stage.put("description", stageData.getString("description"));
/* 130 */       stage.put("eliteDesc", stageData.getString("description"));
/* 131 */       if (stageData.getString("difficulty").equals("NORMAL")) {
/* 132 */         stage.put("eliteDesc", null);
/*     */       }
/* 134 */       if (stageData.getString("difficulty").equals("FOUR_STAR")) {
/* 135 */         stage.put("isElite", Integer.valueOf(1));
/*     */       }
/* 137 */       if (stageData.getBooleanValue("bossMark")) {
/* 138 */         stage.put("isBoss", Integer.valueOf(1));
/*     */       }
/* 140 */       jSONObject.put(entry.getKey().toString(), stage);
/*     */     } 
/*     */     
/* 143 */     return JSON.toJSONString(jSONObject, new SerializerFeature[] { SerializerFeature.WriteMapNullValue });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/stage"})
/*     */   public JSONObject stage() {
/* 151 */     JSONArray jSONArray = new JSONArray();
/* 152 */     JSONObject a = new JSONObject(true);
/* 153 */     JSONObject b = new JSONObject(true);
/* 154 */     a.put("MainStage", new JSONObject(true));
/* 155 */     for (Map.Entry entry : ArknightsApplication.stageTable.entrySet()) {
/* 156 */       JSONObject stageData = ArknightsApplication.stageTable.getJSONObject(entry.getKey().toString());
/*     */       
/* 158 */       if (stageData.getString("stageId").indexOf("act15side") != -1) {
/* 159 */         JSONObject data = new JSONObject(true);
/* 160 */         data.put("next", null);
/* 161 */         data.put("star", null);
/* 162 */         data.put("sub", null);
/* 163 */         data.put("hard", null);
/*     */         
/* 165 */         a.getJSONObject("MainStage").put(stageData.getString("stageId"), data);
/*     */       } 
/*     */     } 
/*     */     
/* 169 */     b.put("stage", new JSONObject(true));
/* 170 */     for (Map.Entry entry : ArknightsApplication.stageTable.entrySet()) {
/* 171 */       JSONObject stageData = ArknightsApplication.stageTable.getJSONObject(entry.getKey().toString());
/*     */       
/* 173 */       if (stageData.getString("stageId").indexOf("act15side") != -1) {
/* 174 */         JSONObject data = new JSONObject(true);
/* 175 */         data.put("stageId", stageData.getString("stageId"));
/* 176 */         data.put("completeTimes", Integer.valueOf(1));
/* 177 */         data.put("startTimes", Integer.valueOf(1));
/* 178 */         data.put("practiceTimes", Integer.valueOf(0));
/* 179 */         data.put("state", Integer.valueOf(3));
/* 180 */         data.put("hasBattleReplay", Integer.valueOf(0));
/* 181 */         data.put("noCostCnt", Integer.valueOf(0));
/*     */         
/* 183 */         b.getJSONObject("stage").put(stageData.getString("stageId"), data);
/*     */       } 
/*     */     } 
/*     */     
/* 187 */     System.out.println(JSON.toJSONString(a, new SerializerFeature[] { SerializerFeature.WriteMapNullValue }));
/* 188 */     System.out.println(JSON.toJSONString(b, new SerializerFeature[] { SerializerFeature.WriteMapNullValue }));
/* 189 */     JSONObject stage = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/battle/stage.json");
/*     */     
/* 191 */     return stage;
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\test.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */