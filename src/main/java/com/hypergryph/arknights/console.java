/*    */ package BOOT-INF.classes.com.hypergryph.arknights;
/*    */ 
/*    */ import com.hypergryph.arknights.ArknightsApplication;
/*    */ import net.minecrell.terminalconsole.SimpleTerminalConsole;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class console
/*    */   extends SimpleTerminalConsole
/*    */ {
/*    */   protected boolean isRunning() {
/* 12 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void runCommand(String s) {
/* 17 */     ArknightsApplication.ConsoleCommandManager.executeCommand(ArknightsApplication.Sender, s);
/*    */   }
/*    */   
/*    */   protected void shutdown() {}
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\console.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */