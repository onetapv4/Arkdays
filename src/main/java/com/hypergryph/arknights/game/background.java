/*    */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*    */ 
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import com.hypergryph.arknights.ArknightsApplication;
/*    */ import com.hypergryph.arknights.core.dao.userDao;
/*    */ import com.hypergryph.arknights.core.pojo.Account;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.springframework.web.bind.annotation.PostMapping;
/*    */ import org.springframework.web.bind.annotation.RequestBody;
/*    */ import org.springframework.web.bind.annotation.RequestHeader;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/background"})
/*    */ public class background
/*    */ {
/* 25 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */ 
/*    */ 
/*    */   
/*    */   @PostMapping(value = {"/setBackground"}, produces = {"application/json;charset=UTF-8"})
/*    */   public JSONObject SetBackground(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 31 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 32 */     LOGGER.info("[/" + clientIp + "] /background/setBackground");
/*    */     
/* 34 */     if (!ArknightsApplication.enableServer) {
/* 35 */       response.setStatus(400);
/* 36 */       JSONObject jSONObject = new JSONObject(true);
/* 37 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 38 */       jSONObject.put("error", "Bad Request");
/* 39 */       jSONObject.put("message", "server is close");
/* 40 */       return jSONObject;
/*    */     } 
/*    */     
/* 43 */     String bgID = JsonBody.getString("bgID");
/*    */     
/* 45 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 46 */     if (Accounts.size() != 1) {
/* 47 */       JSONObject jSONObject = new JSONObject(true);
/* 48 */       jSONObject.put("result", Integer.valueOf(2));
/* 49 */       jSONObject.put("error", "无法查询到此账户");
/* 50 */       return jSONObject;
/*    */     } 
/*    */     
/* 53 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*    */     
/* 55 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 56 */       response.setStatus(500);
/* 57 */       JSONObject jSONObject = new JSONObject(true);
/* 58 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 59 */       jSONObject.put("error", "Bad Request");
/* 60 */       jSONObject.put("message", "error");
/* 61 */       return jSONObject;
/*    */     } 
/*    */ 
/*    */     
/* 65 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*    */     
/* 67 */     UserSyncData.getJSONObject("background").put("selected", bgID);
/*    */     
/* 69 */     userDao.setUserData(uid, UserSyncData);
/*    */     
/* 71 */     JSONObject result = new JSONObject(true);
/* 72 */     JSONObject playerDataDelta = new JSONObject(true);
/* 73 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 74 */     JSONObject modified = new JSONObject(true);
/* 75 */     JSONObject jSONObject1 = new JSONObject(true);
/* 76 */     jSONObject1.put("selected", bgID);
/* 77 */     modified.put("background", jSONObject1);
/* 78 */     playerDataDelta.put("modified", modified);
/* 79 */     result.put("playerDataDelta", playerDataDelta);
/* 80 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\background.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */