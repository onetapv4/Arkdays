package BOOT-INF.classes.com.hypergryph.arknights.command;

import com.hypergryph.arknights.command.CommandException;
import com.hypergryph.arknights.command.ICommand;
import com.hypergryph.arknights.command.ICommandSender;
import java.util.List;

public interface ICommand extends Comparable<ICommand> {
  String getCommandName();
  
  String getCommandUsage(ICommandSender paramICommandSender);
  
  String getCommandDescription();
  
  String getCommandExample();
  
  String getCommandExampleUsage();
  
  List<String> getCommandAliases();
  
  void processCommand(ICommandSender paramICommandSender, String[] paramArrayOfString) throws CommandException;
}


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\command\ICommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */