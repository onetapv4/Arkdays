/*    */ package BOOT-INF.classes.com.hypergryph.arknights.command;
/*    */ 
/*    */ import com.hypergryph.arknights.command.CommandBase;
/*    */ import com.hypergryph.arknights.command.CommandException;
/*    */ import com.hypergryph.arknights.command.ICommandSender;
/*    */ import com.hypergryph.arknights.core.dao.userDao;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandUnBan
/*    */   extends CommandBase
/*    */ {
/* 16 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */ 
/*    */   
/*    */   public String getCommandName() {
/* 20 */     return "unban";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 25 */     return "[int]<玩家UID>";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandDescription() {
/* 30 */     return "解封某位玩家";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandExample() {
/* 35 */     return "/unban 1";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandExampleUsage() {
/* 40 */     return "解封UID为1的玩家";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 45 */     if (args.length >= 2) {
/*    */       
/* 47 */       int uid = 0;
/*    */       
/*    */       try {
/* 50 */         uid = Integer.parseInt(args[1]);
/* 51 */       } catch (Exception e) {
/* 52 */         LOGGER.error("使用方式: /" + getCommandName() + " " + getCommandUsage(sender));
/*    */         
/*    */         return;
/*    */       } 
/* 56 */       if (userDao.setBanStatus(uid, 0) != 1) {
/* 57 */         LOGGER.error("解封失败");
/*    */         return;
/*    */       } 
/* 60 */       LOGGER.info("已解封该玩家");
/*    */       
/*    */       return;
/*    */     } 
/* 64 */     LOGGER.error("使用方式: /" + getCommandName() + " " + getCommandUsage(sender));
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\CommandUnBan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */