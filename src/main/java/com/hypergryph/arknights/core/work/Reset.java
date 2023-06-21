/*    */ package BOOT-INF.classes.com.hypergryph.arknights.core.work;
/*    */ 
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.scheduling.annotation.EnableScheduling;
/*    */ import org.springframework.scheduling.annotation.Scheduled;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @EnableScheduling
/*    */ public class Reset
/*    */ {
/* 17 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   @Scheduled(cron = "0 0 4 * * ?")
/*    */   private void CheckIn() {}
/*    */   
/*    */   @Scheduled(cron = "0 0 4 * * ?")
/*    */   private void PracticeTicket() {}
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\core\work\Reset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */