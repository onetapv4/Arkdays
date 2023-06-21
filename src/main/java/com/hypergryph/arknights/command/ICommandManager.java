/*   */ package BOOT-INF.classes.com.hypergryph.arknights.command;
/*   */ import com.hypergryph.arknights.command.ICommand;
/*   */ import com.hypergryph.arknights.command.ICommandSender;
/*   */ import java.util.Map;
/*   */ 
/*   */ public interface ICommandManager {
/*   */   static int executeCommand(ICommandSender sender, String rawCommand) {
/* 8 */     return 0;
/*   */   }
/*   */   
/*   */   List<ICommand> getPossibleCommands(ICommandSender paramICommandSender);
/*   */   
/*   */   Map<String, ICommand> getCommands();
/*   */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\ICommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */