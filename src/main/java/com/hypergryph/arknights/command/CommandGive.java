/*     */ package BOOT-INF.classes.com.hypergryph.arknights.command;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.admin;
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
/*     */ public class CommandGive
/*     */   extends CommandBase
/*     */ {
/*  20 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   public String getCommandName() {
/*  24 */     return "give";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  29 */     return "[int]<玩家UID> [string]<物品ID> [int]<物品数量>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandDescription() {
/*  34 */     return "给予玩家物品或角色";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandExample() {
/*  39 */     return "/give 10000001 4002 64 | /give 10000001 char_002_amiya 1 | /give 10000001 allchar 1";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandExampleUsage() {
/*  44 */     return "给予UID为10000001的玩家64颗至纯源石 详细信息请查看 data/excel/item_table.json | 给予UID为10000001的玩家一个阿米娅 详细信息请查看 data/excel/character_table.json | 给予UID为10000001的玩家所有角色";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  50 */     if (args.length >= 3) {
/*     */       
/*  52 */       int uid = 0;
/*  53 */       int itemCount = 0;
/*  54 */       String itemId = "";
/*     */       
/*     */       try {
/*  57 */         uid = Integer.parseInt(args[1]);
/*  58 */         itemId = args[2];
/*  59 */         itemCount = Integer.parseInt(args[3]);
/*  60 */       } catch (Exception e) {
/*  61 */         LOGGER.error("使用方式: /" + getCommandName() + " " + getCommandUsage(sender));
/*     */         
/*     */         return;
/*     */       } 
/*  65 */       List<Account> user = userDao.queryAccountByUid(uid);
/*     */       
/*  67 */       if (user.size() != 1) {
/*  68 */         LOGGER.error("无法找到该玩家");
/*     */         return;
/*     */       } 
/*  71 */       JSONObject UserSyncData = JSONObject.parseObject(((Account)user.get(0)).getUser());
/*     */       
/*  73 */       JSONArray excludeCharList = new JSONArray();
/*  74 */       excludeCharList.add("char_504_rguard");
/*  75 */       excludeCharList.add("char_505_rcast");
/*  76 */       excludeCharList.add("char_506_rmedic");
/*  77 */       excludeCharList.add("char_507_rsnipe");
/*  78 */       excludeCharList.add("char_508_aguard");
/*  79 */       excludeCharList.add("char_509_acast");
/*  80 */       excludeCharList.add("char_510_amedic");
/*  81 */       excludeCharList.add("char_511_asnipe");
/*  82 */       excludeCharList.add("char_512_aprot");
/*     */       
/*  84 */       if (itemId.indexOf("char") != -1) {
/*  85 */         if (itemId.equals("allchar")) {
/*  86 */           JSONArray jSONArray1 = new JSONArray();
/*  87 */           for (Map.Entry entry : ArknightsApplication.characterJson.entrySet()) {
/*  88 */             String charId = entry.getKey().toString();
/*  89 */             if (charId.indexOf("char_") != -1 && 
/*  90 */               !excludeCharList.contains(charId)) {
/*  91 */               admin.GM_GiveItem(UserSyncData, charId, "CHAR", 1, jSONArray1);
/*     */             }
/*     */           } 
/*     */           
/*  95 */           userDao.setUserData(Long.valueOf(uid), UserSyncData);
/*  96 */           LOGGER.info("已发送所有干员至该玩家");
/*     */           return;
/*     */         } 
/*  99 */         if (!ArknightsApplication.characterJson.containsKey(itemId)) {
/* 100 */           LOGGER.error("未查找到此干员");
/*     */           
/*     */           return;
/*     */         } 
/* 104 */         JSONArray jSONArray = new JSONArray();
/* 105 */         admin.GM_GiveItem(UserSyncData, itemId, "CHAR", 1, jSONArray);
/*     */         
/* 107 */         userDao.setUserData(Long.valueOf(uid), UserSyncData);
/* 108 */         LOGGER.info("已把干员" + ArknightsApplication.characterJson.getJSONObject(itemId).getString("name") + "给予该玩家，§c玩家需重新登录");
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 113 */       if (!ArknightsApplication.itemTable.containsKey(itemId)) {
/* 114 */         LOGGER.error("未查找到此物品");
/*     */         
/*     */         return;
/*     */       } 
/* 118 */       if (itemCount <= 0 || itemCount > 9999999) {
/* 119 */         LOGGER.error("数量范围应在1-9999999");
/*     */         
/*     */         return;
/*     */       } 
/* 123 */       String itemType = ArknightsApplication.itemTable.getJSONObject(itemId).getString("itemType");
/*     */       
/* 125 */       JSONArray items = new JSONArray();
/* 126 */       admin.GM_GiveItem(UserSyncData, itemId, itemType, itemCount, items);
/*     */       
/* 128 */       userDao.setUserData(Long.valueOf(uid), UserSyncData);
/* 129 */       LOGGER.info("已给予该玩家 " + itemCount + " " + ArknightsApplication.itemTable.getJSONObject(itemId).getString("name"));
/*     */       
/*     */       return;
/*     */     } 
/* 133 */     LOGGER.error("使用方式: /" + getCommandName() + " " + getCommandUsage(sender));
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\CommandGive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */