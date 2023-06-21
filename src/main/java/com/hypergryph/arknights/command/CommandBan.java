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
/*    */ 
/*    */ public class CommandBan
/*    */   extends CommandBase
/*    */ {
/* 17 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */ 
/*    */   
/*    */   public String getCommandName() {
/* 21 */     return "ban";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 26 */     return "[int]<玩家UID>";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandDescription() {
/* 31 */     return "禁止某位玩家进入服务器";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandExample() {
/* 36 */     return "/ban 1";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandExampleUsage() {
/* 41 */     return "封禁UID为1的玩家";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 46 */     if (args.length >= 2) {
/*    */       
/* 48 */       int uid = 0;
/*    */ 
/*    */       
/*    */       try {
/* 52 */         uid = Integer.parseInt(args[1]);
/* 53 */       } catch (Exception e) {
/* 54 */         LOGGER.error("使用方式: /" + getCommandName() + " " + getCommandUsage(sender));
/*    */         
/*    */         return;
/*    */       } 
/* 58 */       if (userDao.setBanStatus(uid, 1) != 1) {
/* 59 */         LOGGER.error("封禁失败");
/*    */         return;
/*    */       } 
/* 62 */       LOGGER.info("已封禁该玩家");
/*    */       return;
/*    */     } 
/* 65 */     LOGGER.error("使用方式: /" + getCommandName() + " " + getCommandUsage(sender));
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\CommandBan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */