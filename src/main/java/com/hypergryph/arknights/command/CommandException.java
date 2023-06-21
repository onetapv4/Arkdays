/*    */ package BOOT-INF.classes.com.hypergryph.arknights.command;
/*    */ 
/*    */ public class CommandException extends Exception {
/*    */   private final Object[] errorObjects;
/*    */   
/*    */   public CommandException(String message, Object... objects) {
/*  7 */     super(message);
/*  8 */     this.errorObjects = objects;
/*    */   }
/*    */   
/*    */   public Object[] getErrorObjects() {
/* 12 */     return this.errorObjects;
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\CommandException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */