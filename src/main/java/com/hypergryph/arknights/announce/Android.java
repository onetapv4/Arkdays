/*    */ package BOOT-INF.classes.com.hypergryph.arknights.announce;
/*    */ 
/*    */ import com.hypergryph.arknights.core.file.IOTools;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/announce"})
/*    */ public class Android
/*    */ {
/*    */   @RequestMapping({"/Android/preannouncement/280_1618473718.html"})
/*    */   public String PreAnnouncement() {
/* 13 */     return IOTools.ReadNormalFile(System.getProperty("user.dir") + "/data/static/announcement/280_1618473718.html");
/*    */   }
/*    */   
/*    */   @RequestMapping({"/Android/css/preannouncement.v_0_1_2.css"})
/*    */   public String PreAnnouncementCss() {
/* 18 */     return IOTools.ReadNormalFile(System.getProperty("user.dir") + "/data/static/css/preannouncement.v_0_1_2.css");
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\announce\Android.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */