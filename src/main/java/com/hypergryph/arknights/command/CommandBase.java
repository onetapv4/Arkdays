/*    */ package BOOT-INF.classes.com.hypergryph.arknights.command;
/*    */ 
/*    */ import com.hypergryph.arknights.command.ICommand;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public abstract class CommandBase implements ICommand {
/*    */   public List<String> getCommandAliases() {
/*  9 */     return Collections.emptyList();
/*    */   }
/*    */   
/*    */   public int compareTo(ICommand p_compareTo_1_) {
/* 13 */     return getCommandName().compareTo(p_compareTo_1_.getCommandName());
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\CommandBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */