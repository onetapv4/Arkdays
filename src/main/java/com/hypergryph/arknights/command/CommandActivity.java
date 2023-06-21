/*     */ package BOOT-INF.classes.com.hypergryph.arknights.command;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.command.CommandBase;
/*     */ import com.hypergryph.arknights.command.CommandException;
/*     */ import com.hypergryph.arknights.command.ICommandSender;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandActivity
/*     */   extends CommandBase
/*     */ {
/*  21 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   public String getCommandName() {
/*  25 */     return "activity";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  30 */     return "[int]<玩家UID> [string]<活动ID>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandDescription() {
/*  35 */     return "解锁某位玩家的活动";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandExample() {
/*  40 */     return "/unlock 10000001 act10mini";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandExampleUsage() {
/*  45 */     return "为UID为10000001的玩家解锁 act10mini 活动 详细信息请查看 data/unlockActivity.json";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  50 */     if (args.length >= 3) {
/*     */       
/*  52 */       int uid = 0;
/*  53 */       String activityId = "";
/*     */ 
/*     */       
/*     */       try {
/*  57 */         uid = Integer.parseInt(args[1]);
/*  58 */         activityId = args[2];
/*  59 */       } catch (Exception e) {
/*  60 */         LOGGER.error("使用方式: /" + getCommandName() + " " + getCommandUsage(sender));
/*     */         
/*     */         return;
/*     */       } 
/*  64 */       List<Account> user = userDao.queryAccountByUid(uid);
/*     */       
/*  66 */       if (user.size() != 1) {
/*  67 */         LOGGER.error("无法找到该玩家");
/*     */         
/*     */         return;
/*     */       } 
/*  71 */       if (!ArknightsApplication.unlockActivity.containsKey(activityId)) {
/*  72 */         LOGGER.error("未知的活动ID，请检查并修改后重试");
/*     */         
/*     */         return;
/*     */       } 
/*  76 */       JSONObject UserSyncData = JSONObject.parseObject(((Account)user.get(0)).getUser());
/*     */       
/*  78 */       JSONObject activity = ArknightsApplication.unlockActivity.getJSONObject(activityId);
/*  79 */       String activityType = activity.getString("type");
/*     */       
/*  81 */       for (int i = 0; i < activity.getJSONObject("activity").getJSONArray("stage").size(); i++) {
/*  82 */         String stageId = activity.getJSONObject("activity").getJSONArray("stage").getString(i);
/*     */         
/*  84 */         JSONObject stageInfo = new JSONObject();
/*  85 */         stageInfo.put("stageId", stageId);
/*  86 */         stageInfo.put("completeTimes", Integer.valueOf(1));
/*  87 */         stageInfo.put("startTimes", Integer.valueOf(1));
/*  88 */         stageInfo.put("practiceTimes", Integer.valueOf(1));
/*  89 */         stageInfo.put("state", Integer.valueOf(3));
/*  90 */         stageInfo.put("hasBattleReplay", Integer.valueOf(0));
/*  91 */         stageInfo.put("noCostCnt", Integer.valueOf(0));
/*     */         
/*  93 */         UserSyncData.getJSONObject("dungeon").getJSONObject("stages").put(stageId, stageInfo);
/*     */       } 
/*     */       
/*  96 */       JSONObject activityStory = new JSONObject();
/*  97 */       JSONArray stories = new JSONArray();
/*     */       
/*  99 */       for (int j = 0; j < activity.getJSONObject("activity").getJSONArray("storyreview").size(); j++) {
/* 100 */         String storyReviewId = activity.getJSONObject("activity").getJSONArray("storyreview").getString(j);
/*     */         
/* 102 */         JSONObject storyReview = new JSONObject();
/* 103 */         storyReview.put("id", storyReviewId);
/* 104 */         storyReview.put("uts", Long.valueOf((new Date()).getTime() / 1000L));
/* 105 */         storyReview.put("rc", Integer.valueOf(0));
/*     */         
/* 107 */         stories.add(storyReview);
/*     */       } 
/*     */       
/* 110 */       activityStory.put("rts", Long.valueOf((new Date()).getTime() / 1000L));
/* 111 */       activityStory.put("stories", stories);
/*     */       
/* 113 */       if (!UserSyncData.getJSONObject("activity").containsKey("activityType")) {
/* 114 */         UserSyncData.getJSONObject("activity").put(activityType, new JSONObject());
/*     */       }
/*     */       
/* 117 */       UserSyncData.getJSONObject("activity").getJSONObject(activityType).put(activityId, activity.getJSONObject("activity").getJSONObject(activityType).getJSONObject(activityId));
/* 118 */       UserSyncData.getJSONObject("storyreview").getJSONObject("groups").put(activityId, activityStory);
/*     */       
/* 120 */       userDao.setUserData(Long.valueOf(uid), UserSyncData);
/* 121 */       LOGGER.info("已为该玩家解锁 " + activityId);
/*     */       return;
/*     */     } 
/* 124 */     LOGGER.error("使用方式: /" + getCommandName() + " " + getCommandUsage(sender));
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\CommandActivity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */