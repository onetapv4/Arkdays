/*    */ package BOOT-INF.classes.com.hypergryph.arknights.track;
/*    */ 
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ @RestController
/*    */ public class event
/*    */ {
/*    */   @RequestMapping({"/event"})
/*    */   public JSONObject Event() {
/* 12 */     JSONObject result = new JSONObject(true);
/* 13 */     result.put("code", Integer.valueOf(200));
/* 14 */     result.put("msg", "ok");
/* 15 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\track\event.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */