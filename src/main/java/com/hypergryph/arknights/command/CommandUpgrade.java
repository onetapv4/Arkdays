/*     */ package BOOT-INF.classes.com.hypergryph.arknights.command;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.command.CommandBase;
/*     */ import com.hypergryph.arknights.command.CommandException;
/*     */ import com.hypergryph.arknights.command.ICommandSender;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class CommandUpgrade
/*     */   extends CommandBase {
/*  17 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   public String getCommandName() {
/*  21 */     return "upgrade";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  26 */     return "[int]<玩家UID>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandDescription() {
/*  31 */     return "让某位玩家的所有干员升级至满级";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandExample() {
/*  36 */     return "/upgrade 10000001";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandExampleUsage() {
/*  41 */     return "把UID为10000001的玩家的所有干员升级至满级";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  46 */     if (args.length >= 2) {
/*     */       
/*  48 */       int uid = 0;
/*     */ 
/*     */       
/*     */       try {
/*  52 */         uid = Integer.parseInt(args[1]);
/*  53 */       } catch (Exception e) {
/*  54 */         LOGGER.error("使用方式: /" + getCommandName() + " " + getCommandUsage(sender));
/*     */         return;
/*     */       } 
/*  57 */       List<Account> user = userDao.queryAccountByUid(uid);
/*     */       
/*  59 */       if (user.size() != 1) {
/*  60 */         LOGGER.error("无法找到该玩家");
/*     */         return;
/*     */       } 
/*  63 */       JSONObject UserSyncData = JSONObject.parseObject(((Account)user.get(0)).getUser());
/*     */       
/*  65 */       JSONObject chars = UserSyncData.getJSONObject("troop").getJSONObject("chars");
/*  66 */       for (Map.Entry entry : chars.entrySet()) {
/*     */         
/*  68 */         JSONObject userChar = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(entry.getKey().toString());
/*  69 */         String charId = userChar.getString("charId");
/*  70 */         int charRarity = ArknightsApplication.characterJson.getJSONObject(charId).getIntValue("rarity");
/*  71 */         int level = 0;
/*  72 */         int evolvePhase = 0;
/*  73 */         userChar.put("favorPoint", Integer.valueOf(25570));
/*     */         
/*  75 */         if (charRarity != 0 && charRarity != 1) {
/*  76 */           userChar.put("mainSkillLvl", Integer.valueOf(7));
/*     */         }
/*     */         
/*  79 */         if (charRarity == 0 || charRarity == 1) {
/*  80 */           level = 30;
/*  81 */           evolvePhase = 0;
/*     */         } 
/*  83 */         if (charRarity == 2) {
/*  84 */           level = 55;
/*  85 */           evolvePhase = 1;
/*     */         } 
/*  87 */         if (charRarity == 3) {
/*  88 */           level = 70;
/*  89 */           evolvePhase = 2;
/*     */         } 
/*  91 */         if (charRarity == 4) {
/*  92 */           level = 80;
/*  93 */           evolvePhase = 2;
/*     */         } 
/*  95 */         if (charRarity == 5) {
/*  96 */           level = 90;
/*  97 */           evolvePhase = 2;
/*     */         } 
/*  99 */         userChar.put("level", Integer.valueOf(level));
/* 100 */         userChar.put("evolvePhase", Integer.valueOf(evolvePhase));
/*     */         
/* 102 */         for (int i = 0; i < userChar.getJSONArray("skills").size(); i++) {
/* 103 */           userChar.getJSONArray("skills").getJSONObject(i).put("unlock", Integer.valueOf(1));
/* 104 */           userChar.getJSONArray("skills").getJSONObject(i).put("specializeLevel", Integer.valueOf(3));
/*     */         } 
/*     */       } 
/*     */       
/* 108 */       userDao.setUserData(Long.valueOf(uid), UserSyncData);
/* 109 */       LOGGER.info("已把该玩家所有干员升至满级");
/*     */       return;
/*     */     } 
/* 112 */     LOGGER.error("使用方式: /" + getCommandName() + " " + getCommandUsage(sender));
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\CommandUpgrade.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */