/*    */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*    */ 
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import com.hypergryph.arknights.ArknightsApplication;
/*    */ import com.hypergryph.arknights.core.dao.userDao;
/*    */ import com.hypergryph.arknights.core.pojo.Account;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.web.bind.annotation.PostMapping;
/*    */ import org.springframework.web.bind.annotation.RequestBody;
/*    */ import org.springframework.web.bind.annotation.RequestHeader;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/story"})
/*    */ public class story
/*    */ {
/*    */   @PostMapping(value = {"/finishStory"}, produces = {"application/json;charset=UTF-8"})
/*    */   public JSONObject FinishStory(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 26 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 27 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /story/finishStory");
/*    */     
/* 29 */     if (!ArknightsApplication.enableServer) {
/* 30 */       response.setStatus(400);
/* 31 */       JSONObject jSONObject = new JSONObject(true);
/* 32 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 33 */       jSONObject.put("error", "Bad Request");
/* 34 */       jSONObject.put("message", "server is close");
/* 35 */       return jSONObject;
/*    */     } 
/*    */     
/* 38 */     String storyId = JsonBody.getString("storyId");
/*    */ 
/*    */ 
/*    */     
/* 42 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 43 */     if (Accounts.size() != 1) {
/* 44 */       JSONObject jSONObject = new JSONObject(true);
/* 45 */       jSONObject.put("result", Integer.valueOf(2));
/* 46 */       jSONObject.put("error", "无法查询到此账户");
/* 47 */       return jSONObject;
/*    */     } 
/*    */     
/* 50 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*    */     
/* 52 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 53 */       response.setStatus(500);
/* 54 */       JSONObject jSONObject = new JSONObject(true);
/* 55 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 56 */       jSONObject.put("error", "Bad Request");
/* 57 */       jSONObject.put("message", "error");
/* 58 */       return jSONObject;
/*    */     } 
/*    */ 
/*    */     
/* 62 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*    */     
/* 64 */     UserSyncData.getJSONObject("status").getJSONObject("flags").put(storyId, Integer.valueOf(1));
/*    */     
/* 66 */     userDao.setUserData(uid, UserSyncData);
/*    */     
/* 68 */     JSONObject result = new JSONObject(true);
/* 69 */     JSONObject playerDataDelta = new JSONObject(true);
/* 70 */     JSONObject modified = new JSONObject(true);
/* 71 */     modified.put("status", UserSyncData.getJSONObject("status"));
/* 72 */     playerDataDelta.put("modified", modified);
/* 73 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 74 */     result.put("playerDataDelta", playerDataDelta);
/* 75 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\story.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */