/*     */ package BOOT-INF.classes.com.hypergryph.arknights.command;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.command.CommandBase;
/*     */ import com.hypergryph.arknights.command.CommandException;
/*     */ import com.hypergryph.arknights.command.ICommandSender;
/*     */ import com.hypergryph.arknights.core.dao.mailDao;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import com.hypergryph.arknights.core.pojo.Mail;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class CommandMail
/*     */   extends CommandBase
/*     */ {
/*  21 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   public String getCommandName() {
/*  25 */     return "mail";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  30 */     return "[string]<邮件名|list> [string]<子命令>";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandDescription() {
/*  35 */     return "处理邮件";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandExample() {
/*  40 */     return "/mail 测试 create";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandExampleUsage() {
/*  45 */     return "创造邮件，其名为\"测试\"";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  50 */     if (args.length == 1) {
/*     */       
/*  52 */       LOGGER.error("需要指定邮件名或使用 /mail list");
/*  53 */     } else if (args.length == 2) {
/*  54 */       if (args[1].equals("list")) {
/*  55 */         mailDao.queryMails().forEach(mail -> LOGGER.info("ID: " + mail.getId() + ", 邮件名: " + mail.getName() + ", 主题: " + mail.getSubject()));
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*  60 */       LOGGER.error("可用的子命令: create<创建邮件> setFrom<设置来源> setSubject<设置主题> setContent<设置内容> items<物品管理> info<查看信息> send<发送邮件>");
/*     */     } else {
/*     */       
/*  63 */       List<Mail> mailList = mailDao.queryMailByName(args[1]);
/*  64 */       String[] subCommandArgs = Arrays.<String, String>copyOfRange(args, 3, args.length, String[].class);
/*     */       
/*  66 */       switch (args[2]) {
/*     */         case "create":
/*  68 */           processCreateCommand(sender, args[1], mailList, subCommandArgs);
/*     */           return;
/*     */         case "setFrom":
/*  71 */           processSetFromCommand(sender, args[1], mailList, subCommandArgs);
/*     */           return;
/*     */         case "setSubject":
/*  74 */           processSetSubjectCommand(sender, args[1], mailList, subCommandArgs);
/*     */           return;
/*     */         case "setContent":
/*  77 */           processSetContentCommand(sender, args[1], mailList, subCommandArgs);
/*     */           return;
/*     */         case "items":
/*  80 */           processItemsCommand(sender, args[1], mailList, subCommandArgs);
/*     */           return;
/*     */         case "send":
/*  83 */           processSendCommand(sender, args[1], mailList, subCommandArgs);
/*     */           return;
/*     */         case "info":
/*  86 */           processInfoCommand(sender, args[1], mailList, subCommandArgs);
/*     */           return;
/*     */       } 
/*  89 */       LOGGER.error("可用的子命令: create<创建邮件> setFrom<设置来源> setSubject<设置主题> setContent<设置内容> items<物品管理> send<发送邮件>");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processInfoCommand(ICommandSender sender, String arg, List<Mail> mailList, String[] subCommandArgs) {
/*  97 */     if (mailList.size() != 1) {
/*  98 */       LOGGER.error("指定的邮件名不存在");
/*     */       return;
/*     */     } 
/* 101 */     Mail mail = mailList.get(0);
/* 102 */     LOGGER.info("ID: " + mail.getId() + ", 邮件名: " + mail.getName());
/* 103 */     LOGGER.info("主题: " + mail.getSubject() + ", 来自: " + ArknightsApplication.characterJson
/* 104 */         .getJSONObject(mail.getFrom()).getString("name"));
/* 105 */     LOGGER.info("正文: ");
/* 106 */     LOGGER.info(mail.getContent());
/* 107 */     LOGGER.info("物品: ");
/* 108 */     JSONArray.parseArray(mail.getItems()).forEach(obj -> {
/*     */           if (!(obj instanceof JSONObject)) {
/*     */             return;
/*     */           }
/*     */           JSONObject item = (JSONObject)obj;
/*     */           if (item.getString("type").equals("CHAR")) {
/*     */             LOGGER.info(ArknightsApplication.characterJson.getJSONObject(item.getString("id")).getString("name") + " * " + item.getIntValue("count"));
/*     */           } else {
/*     */             LOGGER.info(ArknightsApplication.itemTable.getJSONObject(item.getString("id")).getString("name") + " * " + item.getIntValue("count"));
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void processCreateCommand(ICommandSender sender, String name, List<Mail> mailList, String[] args) {
/* 123 */     if (mailList.size() != 0) {
/* 124 */       LOGGER.error("指定的邮件名已存在");
/*     */       return;
/*     */     } 
/* 127 */     int result = mailDao.createMail(name);
/* 128 */     if (result == 1) { LOGGER.info("创建成功"); }
/* 129 */     else { LOGGER.error("出了点意外，为什么捏？"); }
/*     */   
/*     */   }
/*     */   private void processSetFromCommand(ICommandSender sender, String name, List<Mail> mailList, String[] args) {
/* 133 */     if (mailList.size() != 1) {
/* 134 */       LOGGER.error("指定的邮件名不存在");
/*     */       return;
/*     */     } 
/* 137 */     if (args.length == 0) {
/* 138 */       LOGGER.error("用法: /mail " + name + " setFrom [string]<来源>");
/*     */       return;
/*     */     } 
/* 141 */     String characterId = null;
/* 142 */     for (String id : ArknightsApplication.characterJson.keySet()) {
/* 143 */       JSONObject character = ArknightsApplication.characterJson.getJSONObject(id);
/* 144 */       if (character.getString("name").equals(args[0]) || id.equals(args[0]))
/* 145 */         characterId = id; 
/*     */     } 
/* 147 */     if (characterId == null) {
/* 148 */       characterId = "none";
/* 149 */       LOGGER.info("未查找到此干员，默认企鹅物流");
/*     */     } 
/* 151 */     int result = mailDao.setMailFrom(((Mail)mailList.get(0)).getId(), characterId);
/* 152 */     if (result == 1) { LOGGER.info("修改成功"); }
/* 153 */     else { LOGGER.error("出了点意外，为什么捏？"); }
/*     */   
/*     */   }
/*     */   private void processSetSubjectCommand(ICommandSender sender, String name, List<Mail> mailList, String[] args) {
/* 157 */     if (mailList.size() != 1) {
/* 158 */       LOGGER.error("指定的邮件名不存在");
/*     */       return;
/*     */     } 
/* 161 */     if (args.length == 0) {
/* 162 */       LOGGER.error("用法: /mail " + name + " setSubject [string]<主题>");
/*     */       return;
/*     */     } 
/* 165 */     int result = mailDao.setMailSubject(((Mail)mailList.get(0)).getId(), joinToString(args, " "));
/* 166 */     if (result == 1) { LOGGER.info("修改成功"); }
/* 167 */     else { LOGGER.error("出了点意外，为什么捏？"); }
/*     */   
/*     */   }
/*     */   private void processSetContentCommand(ICommandSender sender, String name, List<Mail> mailList, String[] args) {
/* 171 */     if (mailList.size() != 1) {
/* 172 */       LOGGER.error("指定的邮件名不存在");
/*     */       return;
/*     */     } 
/* 175 */     if (args.length == 0) {
/* 176 */       LOGGER.error("用法: /mail " + name + " setConent [string]<内容>");
/*     */       return;
/*     */     } 
/* 179 */     int result = mailDao.setMailContent(((Mail)mailList.get(0)).getId(), joinToString(args, " "));
/* 180 */     if (result == 1) { LOGGER.info("修改成功"); }
/* 181 */     else { LOGGER.error("出了点意外，为什么捏？"); }
/*     */   
/*     */   }
/*     */   private void processItemsCommand(ICommandSender sender, String name, List<Mail> mailList, String[] args) {
/* 185 */     if (mailList.size() != 1) {
/* 186 */       LOGGER.error("指定的邮件名不存在");
/*     */       return;
/*     */     } 
/* 189 */     Mail mail = mailList.get(0);
/* 190 */     if (args.length == 0)
/* 191 */     { LOGGER.error("可用的二级子命令: list<查看物品列表> add<添加物品> del<删除物品>"); }
/* 192 */     else if (args[0].equals("list"))
/* 193 */     { JSONArray.parseArray(mail.getItems()).forEach(obj -> {
/*     */             if (!(obj instanceof JSONObject)) {
/*     */               return;
/*     */             }
/*     */             JSONObject item = (JSONObject)obj;
/*     */             if (item.getString("type").equals("CHAR")) {
/*     */               LOGGER.info(ArknightsApplication.characterJson.getJSONObject(item.getString("id")).getString("name") + " * " + item.getIntValue("count"));
/*     */             } else {
/*     */               LOGGER.info(ArknightsApplication.itemTable.getJSONObject(item.getString("id")).getString("name") + " * " + item.getIntValue("count"));
/*     */             } 
/*     */           }); }
/* 204 */     else if (args[0].equals("add"))
/* 205 */     { if (args.length == 1) {
/* 206 */         LOGGER.error("用法: /mail " + name + " items add [String]<物品|干员> [int]<数量>");
/*     */         return;
/*     */       } 
/* 209 */       int itemCount = 0;
/*     */       try {
/* 211 */         itemCount = Integer.parseInt(args[2]);
/* 212 */       } catch (Exception e) {
/* 213 */         LOGGER.error("解析数量时出错, 原文: " + args[2]);
/*     */         return;
/*     */       } 
/* 216 */       if (itemCount <= 0 || itemCount > 9999999) {
/* 217 */         LOGGER.error("数量范围应在1-9999999");
/*     */         return;
/*     */       } 
/* 220 */       String itemId = null, itemType = null;
/* 221 */       for (String id : ArknightsApplication.itemTable.keySet()) {
/* 222 */         if (ArknightsApplication.itemTable.getJSONObject(id).getString("name").equals(args[1]) || id.equals(args[1])) {
/* 223 */           itemId = id;
/* 224 */           itemType = ArknightsApplication.itemTable.getJSONObject(id).getString("itemType");
/*     */         } 
/*     */       } 
/* 227 */       for (String id : ArknightsApplication.characterJson.keySet()) {
/* 228 */         if (ArknightsApplication.characterJson.getJSONObject(id).getString("name").equals(args[1]) || id.equals(args[1])) {
/* 229 */           itemId = id;
/* 230 */           itemType = "CHAR";
/*     */         } 
/*     */       } 
/* 233 */       if (itemId == null) {
/* 234 */         LOGGER.error("未查找到此物品或干员");
/*     */         return;
/*     */       } 
/* 237 */       JSONArray items = JSONArray.parseArray(mail.getItems());
/* 238 */       JSONObject item = new JSONObject(true);
/* 239 */       item.put("id", itemId);
/* 240 */       item.put("type", itemType);
/* 241 */       item.put("count", Integer.valueOf(itemCount));
/* 242 */       items.add(item);
/* 243 */       int result = mailDao.setMailItems(mail.getId(), items);
/* 244 */       if (result == 1) { LOGGER.info("修改成功"); }
/* 245 */       else { LOGGER.error("出了点意外，为什么捏？"); }  }
/* 246 */     else if (args[0].equals("del"))
/* 247 */     { if (args.length == 1) {
/* 248 */         LOGGER.error("用法: /mail " + name + " items del [int]<物品下标>");
/*     */         return;
/*     */       } 
/* 251 */       int index = -1;
/*     */       try {
/* 253 */         index = Integer.parseInt(args[1]);
/* 254 */       } catch (Exception e) {
/* 255 */         LOGGER.error("解析下标时出错, 原文: " + args[2]);
/*     */         return;
/*     */       } 
/* 258 */       JSONArray items = JSONArray.parseArray(mail.getItems());
/* 259 */       if (index < 0 || index >= items.size()) {
/* 260 */         LOGGER.error("下标越界");
/*     */         return;
/*     */       } 
/* 263 */       items.remove(index);
/* 264 */       int result = mailDao.setMailItems(mail.getId(), items);
/* 265 */       if (result == 1) { LOGGER.info("修改成功"); }
/* 266 */       else { LOGGER.error("出了点意外，为什么捏？"); }
/*     */        }
/* 268 */     else { LOGGER.error("可用的二级子命令: list<查看物品列表> add<添加物品> del<删除物品>"); }
/*     */   
/*     */   }
/*     */   
/*     */   private void processSendCommand(ICommandSender sender, String name, List<Mail> mailList, String[] args) {
/* 273 */     if (mailList.size() != 1) {
/* 274 */       LOGGER.error("指定的邮件名不存在");
/*     */       return;
/*     */     } 
/* 277 */     if (args.length < 2) {
/* 278 */       LOGGER.error("用法: /mail " + name + " send [string]<玩家UID|*> [int]<过期时长(天)>");
/*     */       
/*     */       return;
/*     */     } 
/* 282 */     int expireTime = 0;
/*     */     try {
/* 284 */       expireTime = Integer.parseInt(args[1]);
/* 285 */     } catch (Exception e) {
/* 286 */       LOGGER.error("解析过期时长时出错, 原文: " + args[1]);
/*     */       return;
/*     */     } 
/* 289 */     Long createAt = Long.valueOf(ArknightsApplication.getTimestamp());
/* 290 */     Long expireAt = Long.valueOf(createAt.longValue() + 86400L * expireTime);
/*     */     
/* 292 */     Mail mail = mailList.get(0);
/*     */     
/* 294 */     JSONObject mailObject = new JSONObject(true);
/* 295 */     mailObject.put("mailId", Integer.valueOf(mail.getId()));
/* 296 */     mailObject.put("createAt", createAt);
/* 297 */     mailObject.put("expireAt", expireAt);
/* 298 */     mailObject.put("state", Integer.valueOf(0));
/* 299 */     mailObject.put("type", Integer.valueOf(1));
/* 300 */     mailObject.put("hasItem", Integer.valueOf(1));
/*     */     
/* 302 */     if (!args[0].equals("*")) {
/*     */ 
/*     */       
/* 305 */       long UID = 0L;
/*     */       try {
/* 307 */         UID = Long.parseLong(args[0]);
/* 308 */       } catch (Exception e) {
/* 309 */         LOGGER.error("解析 UID 时出错, 原文: " + args[0]);
/*     */         return;
/*     */       } 
/* 312 */       List<Account> acounts = userDao.queryAccountByUid(UID);
/* 313 */       if (acounts.size() != 1) {
/* 314 */         LOGGER.error("无法找到该玩家");
/*     */         return;
/*     */       } 
/* 317 */       Account account = acounts.get(0);
/* 318 */       JSONArray mailBox = JSONArray.parseArray(account.getMails());
/* 319 */       if (!addMail(mailBox, mailObject)) {
/* 320 */         LOGGER.error("玩家已拥有此邮件");
/*     */         return;
/*     */       } 
/* 323 */       int result = userDao.setMailsData(Long.valueOf(account.getUid()), mailBox);
/* 324 */       if (result == 1) { LOGGER.info("修改成功"); }
/* 325 */       else { LOGGER.error("出了点意外，为什么捏？"); }
/*     */     
/*     */     } 
/*     */   }
/*     */   private static String joinToString(String[] args, String seperate) {
/* 330 */     if (args.length == 0) return ""; 
/* 331 */     StringBuilder builder = new StringBuilder(args[0]);
/* 332 */     for (int i = 1; i < args.length; ) { builder.append(seperate).append(args[i]); i++; }
/* 333 */      return builder.toString();
/*     */   }
/*     */   
/*     */   private static boolean addMail(JSONArray mailList, JSONObject mail) {
/* 337 */     for (Object object : mailList) {
/* 338 */       if (((JSONObject)object).getIntValue("mailId") == mail.getIntValue("mailId")) {
/* 339 */         return false;
/*     */       }
/*     */     } 
/* 342 */     mailList.add(mail);
/* 343 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\CommandMail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */