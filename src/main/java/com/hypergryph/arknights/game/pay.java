/*     */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.admin;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestHeader;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/pay"})
/*     */ public class pay {
/*     */   @PostMapping(value = {"/getUnconfirmedOrderIdList"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject getUnconfirmedOrderIdList() {
/*  22 */     JSONObject result = new JSONObject(true);
/*  23 */     JSONObject playerDataDelta = new JSONObject(true);
/*  24 */     playerDataDelta.put("modified", new JSONObject(true));
/*  25 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  26 */     result.put("playerDataDelta", playerDataDelta);
/*  27 */     result.put("orderIdList", new JSONArray());
/*  28 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/createOrder"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject createOrder(@RequestBody JSONObject json) {
/*  34 */     if (!ArknightsApplication.serverConfig.getJSONObject("shop").getBooleanValue("enableDiamondShop")) {
/*  35 */       JSONObject jSONObject = new JSONObject(true);
/*  36 */       jSONObject.put("result", Integer.valueOf(114514));
/*  37 */       return jSONObject;
/*     */     } 
/*     */     
/*  40 */     JSONObject result = new JSONObject(true);
/*  41 */     JSONObject playerDataDelta = new JSONObject(true);
/*  42 */     playerDataDelta.put("modified", new JSONObject(true));
/*  43 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  44 */     result.put("playerDataDelta", playerDataDelta);
/*  45 */     result.put("result", Integer.valueOf(0));
/*  46 */     result.put("orderId", json.getString("goodId"));
/*  47 */     result.put("orderIdList", new JSONArray());
/*  48 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/createOrderAlipay2"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject createOrderAlipay(@RequestBody JSONObject json, HttpServletResponse response) {
/*  55 */     JSONObject result = new JSONObject(true);
/*  56 */     JSONObject playerDataDelta = new JSONObject(true);
/*  57 */     playerDataDelta.put("modified", new JSONObject(true));
/*  58 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  59 */     result.put("playerDataDelta", playerDataDelta);
/*  60 */     result.put("result", Integer.valueOf(0));
/*  61 */     result.put("orderId", json.getString("goodId"));
/*  62 */     result.put("price", Integer.valueOf(600));
/*  63 */     result.put("qs", "");
/*  64 */     result.put("pagePay", Boolean.valueOf(true));
/*  65 */     result.put("returnUrl", "");
/*  66 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/confirmOrderAlipay"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject confirmOrderAlipay() {
/*  72 */     JSONObject result = new JSONObject(true);
/*  73 */     result.put("status", Integer.valueOf(0));
/*  74 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/confirmOrder"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject confirmOrder(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response) {
/*  82 */     if (!ArknightsApplication.enableServer) {
/*  83 */       response.setStatus(400);
/*  84 */       JSONObject jSONObject = new JSONObject(true);
/*  85 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  86 */       jSONObject.put("error", "Bad Request");
/*  87 */       jSONObject.put("message", "server is close");
/*  88 */       return jSONObject;
/*     */     } 
/*     */     
/*  91 */     String goodId = JsonBody.getString("orderId");
/*     */     
/*  93 */     JSONArray items = new JSONArray();
/*  94 */     JSONObject receiveItems = new JSONObject(true);
/*     */ 
/*     */ 
/*     */     
/*  98 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  99 */     if (Accounts.size() != 1) {
/* 100 */       JSONObject jSONObject = new JSONObject(true);
/* 101 */       jSONObject.put("result", Integer.valueOf(2));
/* 102 */       jSONObject.put("error", "无法查询到此账户");
/* 103 */       return jSONObject;
/*     */     } 
/*     */     
/* 106 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 108 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 109 */       response.setStatus(500);
/* 110 */       JSONObject jSONObject = new JSONObject(true);
/* 111 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 112 */       jSONObject.put("error", "Bad Request");
/* 113 */       jSONObject.put("message", "error");
/* 114 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 118 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */ 
/*     */     
/* 121 */     if (goodId.indexOf("CS") != -1) {
/*     */ 
/*     */       
/* 124 */       JSONObject CashGood = new JSONObject();
/* 125 */       for (int i = 0; i < ArknightsApplication.CashGoodList.getJSONArray("goodList").size(); i++) {
/* 126 */         if (ArknightsApplication.CashGoodList.getJSONArray("goodList").getJSONObject(i).getString("goodId").equals(goodId)) {
/* 127 */           CashGood = ArknightsApplication.CashGoodList.getJSONArray("goodList").getJSONObject(i);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 132 */       Boolean doubleCash = Boolean.valueOf(true);
/* 133 */       JSONArray info = UserSyncData.getJSONObject("shop").getJSONObject("CASH").getJSONArray("info");
/*     */       
/* 135 */       int diamondNum = 0;
/* 136 */       for (int j = 0; j < info.size(); j++) {
/* 137 */         String id = info.getJSONObject(j).getString("id");
/* 138 */         if (id.equals(goodId)) {
/* 139 */           doubleCash = Boolean.valueOf(false);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 144 */       if (doubleCash.booleanValue()) {
/* 145 */         JSONObject CS = new JSONObject(true);
/* 146 */         CS.put("id", goodId);
/* 147 */         CS.put("count", Integer.valueOf(1));
/* 148 */         info.add(CS);
/*     */         
/* 150 */         diamondNum = CashGood.getIntValue("doubleCount");
/*     */       } else {
/*     */         
/* 153 */         diamondNum = CashGood.getIntValue("diamondNum") + CashGood.getIntValue("plusNum");
/*     */       } 
/*     */       
/* 156 */       UserSyncData.getJSONObject("status").put("androidDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("androidDiamond") + diamondNum));
/* 157 */       UserSyncData.getJSONObject("status").put("iosDiamond", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("iosDiamond") + diamondNum));
/*     */       
/* 159 */       JSONObject item = new JSONObject(true);
/* 160 */       item.put("id", "4002");
/* 161 */       item.put("type", "DIAMOND");
/* 162 */       item.put("count", Integer.valueOf(diamondNum));
/* 163 */       items.add(item);
/*     */     } 
/*     */ 
/*     */     
/* 167 */     if (goodId.indexOf("GP_") != -1) {
/*     */       
/* 169 */       JSONArray GPItems = new JSONArray();
/*     */       
/* 171 */       if (goodId.indexOf("GP_gW") != -1) {
/* 172 */         GPItems = ArknightsApplication.GPGoodList.getJSONObject("weeklyGroup").getJSONObject("packages").getJSONObject(goodId).getJSONArray("items");
/*     */       }
/*     */       
/* 175 */       if (goodId.indexOf("GP_gM") != -1) {
/* 176 */         GPItems = ArknightsApplication.GPGoodList.getJSONObject("monthlyGroup").getJSONObject("packages").getJSONObject(goodId).getJSONArray("items");
/*     */       }
/*     */       
/* 179 */       if (goodId.indexOf("GP_Once") != -1)
/*     */       {
/* 181 */         for (int j = 0; j < ArknightsApplication.GPGoodList.getJSONArray("oneTimeGP").size(); j++) {
/* 182 */           if (ArknightsApplication.GPGoodList.getJSONArray("oneTimeGP").getJSONObject(j).getString("goodId").equals(goodId)) {
/* 183 */             GPItems = ArknightsApplication.GPGoodList.getJSONArray("oneTimeGP").getJSONObject(j).getJSONArray("items");
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/* 189 */       for (int i = 0; i < GPItems.size(); i++) {
/* 190 */         String reward_id = GPItems.getJSONObject(i).getString("id");
/* 191 */         String reward_type = GPItems.getJSONObject(i).getString("type");
/* 192 */         int reward_count = GPItems.getJSONObject(i).getIntValue("count");
/*     */         
/* 194 */         admin.GM_GiveItem(UserSyncData, reward_id, reward_type, reward_count, items);
/*     */       } 
/*     */     } 
/*     */     
/* 198 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 200 */     JSONObject result = new JSONObject(true);
/* 201 */     JSONObject playerDataDelta = new JSONObject(true);
/* 202 */     JSONObject modified = new JSONObject(true);
/*     */     
/* 204 */     receiveItems.put("items", items);
/*     */     
/* 206 */     modified.put("status", UserSyncData.getJSONObject("status"));
/* 207 */     modified.put("shop", UserSyncData.getJSONObject("shop"));
/* 208 */     modified.put("troop", UserSyncData.getJSONObject("troop"));
/* 209 */     modified.put("skin", UserSyncData.getJSONObject("skin"));
/* 210 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 211 */     playerDataDelta.put("modified", modified);
/* 212 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 213 */     result.put("playerDataDelta", playerDataDelta);
/* 214 */     result.put("result", Integer.valueOf(0));
/* 215 */     result.put("receiveItems", receiveItems);
/* 216 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\pay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */