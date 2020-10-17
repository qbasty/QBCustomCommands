package QBCustomCommands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_16_R2.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.clip.placeholderapi.PlaceholderAPI;

public class Main extends JavaPlugin {
	
	private String name = "QBCustomCommands";
		
	public class CustomCommand extends BukkitCommand {
		
		private String text;
		
	    public CustomCommand(String name, String text) {
	        super(name);
	        
	        this.text = text;
	        
	        this.usageMessage = "/"+name;
	        this.setAliases(new ArrayList<String>());
	    }

	    @Override
	    public boolean execute(CommandSender sender, String alias, String[] args) {
	    		    	
	    	for(String textLine : text.split("§z")) {
    			if(textLine != "") sender.sendMessage(PlaceholderAPI.setPlaceholders((Player)sender, textLine));
    		}
	    	return true;
	    }
	}
	
	public FileConfiguration findConfig() {
		
		File customConfigFile = new File(getDataFolder(), "custom.yml");
		FileConfiguration customConfig = new YamlConfiguration();
		
		if (!customConfigFile.exists()) {
			getConfig().options().copyDefaults(true);
			customConfig = getConfig();
			saveDefaultConfig();
		} 
		else {
			try {
	            customConfig.load(customConfigFile);
	        } catch (IOException | InvalidConfigurationException e) {
	            e.printStackTrace();
	        }
		}
		return customConfig;
	}
	
	public void loadCustomCommands() {
		
		FileConfiguration customConfig = findConfig();
		
		for(String key : customConfig.getValues(true).keySet()) {

			if(key == name) continue;
						
			String commandText = "";
			
			for(String textLine : customConfig.getStringList(key)) {
				commandText += textLine+"§z";
			}
			
			String command = key.replace(name+".", "");
			
			((CraftServer) this.getServer()).getCommandMap().register
			(name, new CustomCommand(command, commandText.replaceAll("&", "§")));
		}
	}
	
	@Override
	public void onEnable() {
		
		loadCustomCommands();
	}
}
