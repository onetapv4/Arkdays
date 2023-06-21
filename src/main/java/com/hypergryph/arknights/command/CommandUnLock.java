/*    */ package BOOT-INF.classes.com.hypergryph.arknights.command;
/*    */ 
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import com.hypergryph.arknights.ArknightsApplication;
/*    */ import com.hypergryph.arknights.command.CommandBase;
/*    */ import com.hypergryph.arknights.command.CommandException;
/*    */ import com.hypergryph.arknights.command.ICommandSender;
/*    */ import com.hypergryph.arknights.core.dao.userDao;
/*    */ import com.hypergryph.arknights.core.pojo.Account;
/*    */ import java.util.List;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandUnLock
/*    */   extends CommandBase
/*    */ {
/* 19 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */ 
/*    */   
/*    */   public String getCommandName() {
/* 23 */     return "unlock";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 28 */     return "[int]<玩家UID> [string]<关卡ID>";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandDescription() {
/* 33 */     return "解锁某位玩家的关卡";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandExample() {
/* 38 */     return "/unlock 10000001 main_03-08";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandExampleUsage() {
/* 43 */     return "为UID为10000001的玩家解锁 3-8 关卡 详细信息请查看 data/excel/stage_table.json";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 48 */     if (args.length >= 3) {
/*    */       
/* 50 */       int uid = 0;
/* 51 */       String stageId = "";
/*    */ 
/*    */       
/*    */       try {
/* 55 */         uid = Integer.parseInt(args[1]);
/* 56 */         stageId = args[2];
/* 57 */       } catch (Exception e) {
/* 58 */         LOGGER.error("使用方式: /" + getCommandName() + " " + getCommandUsage(sender));
/*    */         
/*    */         return;
/*    */       } 
/* 62 */       List<Account> user = userDao.queryAccountByUid(uid);
/*    */       
/* 64 */       if (user.size() != 1) {
/* 65 */         LOGGER.error("无法找到该玩家");
/*    */         
/*    */         return;
/*    */       } 
/* 69 */       if (!ArknightsApplication.stageTable.containsKey(stageId)) {
/* 70 */         LOGGER.error("未知的关卡ID，请检查并修改后重试");
/*    */         
/*    */         return;
/*    */       } 
/* 74 */       JSONObject UserSyncData = JSONObject.parseObject(((Account)user.get(0)).getUser());
/*    */       
/* 76 */       JSONObject stageInfo = new JSONObject();
/* 77 */       stageInfo.put("stageId", stageId);
/* 78 */       stageInfo.put("completeTimes", Integer.valueOf(1));
/* 79 */       stageInfo.put("startTimes", Integer.valueOf(1));
/* 80 */       stageInfo.put("practiceTimes", Integer.valueOf(1));
/* 81 */       stageInfo.put("state", Integer.valueOf(3));
/* 82 */       stageInfo.put("hasBattleReplay", Integer.valueOf(0));
/* 83 */       stageInfo.put("noCostCnt", Integer.valueOf(0));
/*    */       
/* 85 */       UserSyncData.getJSONObject("dungeon").getJSONObject("stages").put(stageId, stageInfo);
/*    */       
/* 87 */       userDao.setUserData(Long.valueOf(uid), UserSyncData);
/* 88 */       LOGGER.info("已为该玩家解锁 " + stageId);
/*    */       return;
/*    */     } 
/* 91 */     LOGGER.error("使用方式: /" + getCommandName() + " " + getCommandUsage(sender));
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\CommandUnLock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */