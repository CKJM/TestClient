package water.godcoder.command.commands;

import water.godcoder.command.Command;
import water.godcoder.command.syntax.ChunkBuilder;
import water.godcoder.command.syntax.parsers.ModuleParser;
import water.godcoder.module.Module;
import water.godcoder.module.ModuleManager;
import water.godcoder.setting.Setting;
import water.godcoder.setting.Settings;
import water.godcoder.setting.builder.SettingBuilder;
import water.godcoder.util.Wrapper;

/**
 * Created by 086 on 12/11/2017.
 */
public class BindCommand extends Command {

    public static Setting<Boolean> modifiersEnabled = SettingBuilder.register(Settings.b("modifiersEnabled", false), "binds");

    public BindCommand() {
        super("bind", new ChunkBuilder()
                .append("[module]|modifiers", true, new ModuleParser())
                .append("[key]|[on|off]", true)
                .build()
        );
    }

    @Override
    public void call(String[] args) {
        if (args.length == 1) {
            Command.sendChatMessage("Please specify a module.");
            return;
        }

        String module = args[0];
        String rkey = args[1];

        if (module.equalsIgnoreCase("modifiers")) {
            if (rkey == null) {
                sendChatMessage("Expected: on or off");
                return;
            }

            if (rkey.equalsIgnoreCase("on")) {
                modifiersEnabled.setValue(true);
                sendChatMessage("Turned modifiers on.");
            } else if (rkey.equalsIgnoreCase("off")) {
                modifiersEnabled.setValue(false);
                sendChatMessage("Turned modifiers off.");
            } else {
                sendChatMessage("Expected: on or off");
            }
            return;
        }

        Module m = ModuleManager.getModuleByName(module);

        if (m == null){
            sendChatMessage("Unknown module '" + module + "'!");
            return;
        }

        if (rkey == null){
            sendChatMessage(m.getName() + " is bound to &b" + m.getBindName());
            return;
        }

        int key = Wrapper.getKey(rkey);

        if (rkey.equalsIgnoreCase("none")){
            key = -1;
        }

        if (key == 0){
            sendChatMessage("Unknown key '" + rkey + "'!");
            return;
        }

        m.getBind().setKey(key);
        sendChatMessage("Bind for &b" + m.getName() + "&r set to &b" + rkey.toUpperCase());
    }
}
