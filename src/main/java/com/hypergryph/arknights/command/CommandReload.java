/*    */ package BOOT-INF.classes.com.hypergryph.arknights.command;
/*    */ 
/*    */ import com.hypergryph.arknights.ArknightsApplication;
/*    */ import com.hypergryph.arknights.command.CommandBase;
/*    */ import com.hypergryph.arknights.command.CommandException;
/*    */ import com.hypergryph.arknights.command.ICommandSender;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandReload
/*    */   extends CommandBase
/*    */ {
/* 16 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCommandName() {
/* 21 */     return "reload";
/*    */   }
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 25 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandDescription() {
/* 30 */     return "重载配置文件";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandExample() {
/* 35 */     return "/reload";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandExampleUsage() {
/* 40 */     return "使用/reload重新载入配置文件";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 48 */     if (args.length >= 1) {
/* 49 */       ArknightsApplication.reloadServerConfig();
/*    */       
/*    */       return;
/*    */     } 
/* 53 */     LOGGER.error("§6使用方式: §f/" + getCommandName() + " " + getCommandUsage(sender));
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\CommandReload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */