package water.godcoder.command.commands;

import water.godcoder.command.Command;
import water.godcoder.command.syntax.ChunkBuilder;
import water.godcoder.command.syntax.parsers.ModuleParser;
import water.godcoder.module.Module;
import water.godcoder.module.ModuleManager;

public class RenameModuleCommand extends Command {

    public RenameModuleCommand() {
        super("renamemodule", new ChunkBuilder().append("module", true, new ModuleParser()).append("name").build());
    }

    @Override
    public void call(String[] args) {
        if (args.length == 0) {
            sendChatMessage("Please specify a module!");
            return;
        }

        Module module = ModuleManager.getModuleByName(args[0]);
        if (module == null) {
            sendChatMessage("Unknown module '" + args[0] + "'!");
            return;
        }

        String name = args.length == 1 ? module.getOriginalName() : args[1];

        if (!(name.matches("[a-zA-Z]+"))) {
            sendChatMessage("Name must be alphabetic!");
            return;
        }

        sendChatMessage("&b" + module.getName() + "&r renamed to &b" + name);
        module.setName(name);
    }

}
