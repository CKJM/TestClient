package water.godcoder.command.commands;

import water.godcoder.command.Command;
import water.godcoder.command.syntax.ChunkBuilder;
import water.godcoder.command.syntax.parsers.ModuleParser;
import water.godcoder.module.Module;
import water.godcoder.module.ModuleManager;
import water.godcoder.setting.ISettingUnknown;
import water.godcoder.setting.Setting;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by 086 on 18/11/2017.
 */
public class SetCommand extends Command {

    public SetCommand() {
        super("set", new ChunkBuilder()
                .append("module", true, new ModuleParser())
                .append("setting", true)
                .append("value", true)
                .build());
    }

    @Override
    public void call(String[] args) {
        if (args[0] == null) {
            Command.sendChatMessage("Please specify a module!");
            return;
        }

        Module m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            Command.sendChatMessage("Unknown module &b" + args[0] + "&r!");
            return;
        }

        if (args[1] == null) {
            String settings = String.join(", ",  m.settingList.stream().map(setting -> setting.getName()).collect(Collectors.toList()));
            if (settings.isEmpty())
                Command.sendChatMessage("Module &b" + m.getName() + "&r has no settings.");
            else {
                Command.sendStringChatMessage(new String[]{
                        "Please specify a setting! Choose one of the following:", settings
                });
            }
            return;
        }

        Optional<Setting> optionalSetting = m.settingList.stream().filter(setting1 -> setting1.getName().equalsIgnoreCase(args[1])).findFirst();
        if (!optionalSetting .isPresent()) {
            Command.sendChatMessage("Unknown setting &b" + args[1] + "&r in &b" + m.getName() + "&r!");
            return;
        }

        ISettingUnknown setting = optionalSetting.get();

        if (args[2] == null) {
            Command.sendChatMessage("&b" + setting.getName() + "&r is a &3" + setting.getValueClass().getSimpleName() + "&r. Its current value is &3" + setting.getValueAsString());
            return;
        }

        try {
            setting.setValueFromString(args[2]);
            Command.sendChatMessage("Set &b" + setting.getName() + "&r to &3" + args[2] + "&r.");
        } catch (Exception e) {
            e.printStackTrace();
            Command.sendChatMessage("Unable to set value! &6" + e.getMessage());
        }
    }
}
