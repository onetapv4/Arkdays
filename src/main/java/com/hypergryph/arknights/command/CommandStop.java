/*    */ package BOOT-INF.classes.com.hypergryph.arknights.command;
/*    */ import com.hypergryph.arknights.command.CommandBase;
/*    */ import com.hypergryph.arknights.command.CommandException;
/*    */ import com.hypergryph.arknights.command.ICommandSender;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class CommandStop extends CommandBase {
/*  8 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */ 
/*    */   
/*    */   public String getCommandName() {
/* 12 */     return "stop";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 17 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandDescription() {
/* 22 */     return "关闭服务器";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandExample() {
/* 27 */     return "/stop";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandExampleUsage() {
/* 32 */     return "使用/stop停止服务器";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 37 */     LOGGER.warn("正在关闭服务器...");
/* 38 */     System.exit(0);
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\CommandStop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */