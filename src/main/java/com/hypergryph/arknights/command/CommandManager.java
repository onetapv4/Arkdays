/*    */ package BOOT-INF.classes.com.hypergryph.arknights.command;
/*    */ 
/*    */ public class CommandManager extends CommandHandler {
/*    */   public CommandManager() {
/*  5 */     registerCommand((ICommand)new CommandHelp());
/*  6 */     registerCommand((ICommand)new CommandReload());
/*  7 */     registerCommand((ICommand)new CommandBan());
/*  8 */     registerCommand((ICommand)new CommandUnBan());
/*  9 */     registerCommand((ICommand)new CommandStop());
/* 10 */     registerCommand((ICommand)new CommandGive());
/* 11 */     registerCommand((ICommand)new CommandMail());
/* 12 */     registerCommand((ICommand)new CommandUpgrade());
/* 13 */     registerCommand((ICommand)new CommandUnLock());
/* 14 */     registerCommand((ICommand)new CommandActivity());
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\CommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */