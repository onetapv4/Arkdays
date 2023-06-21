/*    */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*    */ 
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import com.hypergryph.arknights.core.dao.userDao;
/*    */ import com.hypergryph.arknights.core.pojo.Account;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.web.bind.annotation.PostMapping;
/*    */ import org.springframework.web.bind.annotation.RequestHeader;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/storyreview"})
/*    */ public class storyreview
/*    */ {
/*    */   @RequestMapping({"/readStory"})
/*    */   public JSONObject readStory() {
/* 23 */     return JSONObject.parseObject("{\"result\":0,\"rewards\":[],\"unlockStages\":[],\"alert\":[],\"playerDataDelta\":{\"modified\":{},\"deleted\":{}}}");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PostMapping(value = {"/markStoryAcceKnown"}, produces = {"application/json;charset=UTF-8"})
/*    */   public JSONObject markStoryAcceKnown(@RequestHeader("secret") String secret, HttpServletResponse response) {
/* 31 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 32 */     if (Accounts.size() != 1) {
/* 33 */       JSONObject jSONObject = new JSONObject(true);
/* 34 */       jSONObject.put("result", Integer.valueOf(2));
/* 35 */       jSONObject.put("error", "无法查询到此账户");
/* 36 */       return jSONObject;
/*    */     } 
/*    */     
/* 39 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*    */     
/* 41 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 42 */       response.setStatus(500);
/* 43 */       JSONObject jSONObject = new JSONObject(true);
/* 44 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 45 */       jSONObject.put("error", "Bad Request");
/* 46 */       jSONObject.put("message", "error");
/* 47 */       return jSONObject;
/*    */     } 
/*    */ 
/*    */     
/* 51 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*    */     
/* 53 */     UserSyncData.getJSONObject("storyreview").getJSONObject("tags").put("knownStoryAcceleration", Integer.valueOf(1));
/*    */     
/* 55 */     userDao.setUserData(uid, UserSyncData);
/*    */     
/* 57 */     JSONObject result = new JSONObject(true);
/* 58 */     JSONObject playerDataDelta = new JSONObject(true);
/* 59 */     JSONObject modified = new JSONObject(true);
/* 60 */     JSONObject dungeon = new JSONObject(true);
/* 61 */     JSONObject stages = new JSONObject(true);
/*    */     
/* 63 */     dungeon.put("stages", stages);
/* 64 */     modified.put("storyreview", UserSyncData.getJSONObject("storyreview"));
/* 65 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 66 */     playerDataDelta.put("modified", modified);
/* 67 */     result.put("playerDataDelta", playerDataDelta);
/* 68 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\storyreview.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */