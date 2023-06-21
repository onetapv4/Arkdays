/*    */ package BOOT-INF.classes.com.hypergryph.arknights.auth;
/*    */ 
/*    */ import com.alibaba.fastjson.JSONObject;
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
/*    */ 
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/online"})
/*    */ public class online
/*    */ {
/* 23 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PostMapping(value = {"/v1/ping"}, produces = {"application/json;charset=UTF-8"})
/*    */   public JSONObject Ping(HttpServletRequest request) {
/* 30 */     JSONObject result = new JSONObject(true);
/* 31 */     result.put("result", Integer.valueOf(0));
/* 32 */     result.put("message", "OK");
/* 33 */     result.put("interval", Integer.valueOf(2242));
/* 34 */     result.put("timeLeft", Integer.valueOf(-1));
/* 35 */     result.put("alertTime", Integer.valueOf(600));
/* 36 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PostMapping(value = {"/v1/loginout"}, produces = {"application/json;charset=UTF-8"})
/*    */   public JSONObject LoginOut(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 86 */     JSONObject jsonObject = new JSONObject(true);
/* 87 */     jsonObject.put("result", Integer.valueOf(0));
/* 88 */     return jsonObject;
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\auth\online.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */