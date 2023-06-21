/*    */ package BOOT-INF.classes.com.hypergryph.arknights.command;
/*    */ import com.hypergryph.arknights.ArknightsApplication;
/*    */ import com.hypergryph.arknights.command.CommandBase;
/*    */ import com.hypergryph.arknights.command.CommandException;
/*    */ import com.hypergryph.arknights.command.ICommand;
/*    */ import com.hypergryph.arknights.command.ICommandSender;
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class CommandHelp extends CommandBase {
/* 12 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */ 
/*    */   
/*    */   public String getCommandName() {
/* 16 */     return "help";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 21 */     return "[string]<命令>";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandDescription() {
/* 26 */     return "打开帮助菜单";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandExample() {
/* 31 */     return "/help help";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandExampleUsage() {
/* 36 */     return "查看命令 help 的帮助";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 41 */     if (args.length == 1) {
/*    */       
/* 43 */       LOGGER.info("§e------------------- §f帮助菜单 §e-------------------");
/* 44 */       Map<String, ICommand> map1 = getCommands();
/*    */       
/* 46 */       for (Map.Entry<String, ICommand> entry : map1.entrySet()) {
/* 47 */         ICommand iCommand = map1.get(entry.getKey());
/* 48 */         LOGGER.info("§6/" + iCommand.getCommandName() + " §f" + iCommand.getCommandUsage(sender));
/*    */       } 
/*    */       
/*    */       return;
/*    */     } 
/* 53 */     Map<String, ICommand> map = getCommands();
/* 54 */     ICommand icommand = map.get(args[1]);
/*    */     
/* 56 */     if (icommand == null) {
/* 57 */       LOGGER.error("未知或不完整的命令 '" + args[1] + "'");
/*    */       return;
/*    */     } 
/* 60 */     LOGGER.info("§e------------------- §f命令帮助 §e-------------------");
/* 61 */     LOGGER.info("§6描述: §f" + icommand.getCommandDescription());
/* 62 */     LOGGER.info("§6使用方式: §f/" + icommand.getCommandName() + " " + icommand.getCommandUsage(sender));
/* 63 */     LOGGER.info("§6例子: §f" + icommand.getCommandExample());
/* 64 */     LOGGER.info("§6说明: §f" + icommand.getCommandExampleUsage());
/*    */   }
/*    */   
/*    */   protected Map<String, ICommand> getCommands() {
/* 68 */     return ArknightsApplication.ConsoleCommandManager.getCommands();
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\CommandHelp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */