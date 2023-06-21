/*    */ package BOOT-INF.classes.com.hypergryph.arknights.auth;
/*    */ 
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/captcha"})
/*    */ public class captcha
/*    */ {
/*    */   @RequestMapping({"/v1/register"})
/*    */   public JSONObject RegisterCaptcha() {
/* 13 */     JSONObject result = new JSONObject(true);
/* 14 */     result.put("result", Integer.valueOf(1));
/* 15 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\auth\captcha.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */