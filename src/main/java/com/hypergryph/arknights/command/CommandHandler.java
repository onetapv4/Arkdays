/*    */ package BOOT-INF.classes.com.hypergryph.arknights.command;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.hypergryph.arknights.command.CommandException;
/*    */ import com.hypergryph.arknights.command.ICommand;
/*    */ import com.hypergryph.arknights.command.ICommandManager;
/*    */ import com.hypergryph.arknights.command.ICommandSender;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class CommandHandler implements ICommandManager {
/* 14 */   private static final Logger logger = LogManager.getLogger();
/* 15 */   private final Map<String, ICommand> commandMap = Maps.newHashMap();
/* 16 */   private final Set<ICommand> commandSet = Sets.newHashSet();
/*    */   
/*    */   public List<ICommand> getPossibleCommands(ICommandSender sender) {
/* 19 */     List<ICommand> list = Lists.newArrayList();
/*    */     
/* 21 */     for (ICommand icommand : this.commandSet) {
/* 22 */       list.add(icommand);
/*    */     }
/*    */     
/* 25 */     return list;
/*    */   }
/*    */   
/*    */   public ICommand registerCommand(ICommand command) {
/* 29 */     this.commandMap.put(command.getCommandName(), command);
/* 30 */     this.commandSet.add(command);
/*    */     
/* 32 */     for (String s : command.getCommandAliases()) {
/* 33 */       ICommand icommand = this.commandMap.get(s);
/*    */       
/* 35 */       if (icommand == null || !icommand.getCommandName().equals(s)) {
/* 36 */         this.commandMap.put(s, command);
/*    */       }
/*    */     } 
/*    */     
/* 40 */     return command;
/*    */   }
/*    */   
/*    */   public int executeCommand(ICommandSender sender, String rawCommand) {
/* 44 */     rawCommand = rawCommand.trim();
/*    */     
/* 46 */     String[] astring = rawCommand.split(" ");
/* 47 */     String s = astring[0];
/*    */     
/* 49 */     ICommand icommand = this.commandMap.get(s);
/*    */     
/* 51 */     if (icommand == null) {
/* 52 */       logger.error("未知或不完整的命令 '" + s + "'");
/* 53 */       return 0;
/*    */     } 
/*    */     
/*    */     try {
/* 57 */       icommand.processCommand(sender, astring);
/* 58 */     } catch (CommandException e) {
/* 59 */       e.printStackTrace();
/*    */     } 
/* 61 */     return 0;
/*    */   }
/*    */   
/*    */   public Map<String, ICommand> getCommands() {
/* 65 */     return this.commandMap;
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\CommandHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */