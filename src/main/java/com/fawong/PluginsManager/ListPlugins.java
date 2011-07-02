package com.fawong.PluginsManager;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.Plugin;

/**
* Handle events for all in-game commands
* @author fawong
*/
public class ListPlugins implements PluginsManagerSettings{
	private String output_file_name_value = "";
	private String output_folder_name_value = "";
	private String column_view_value = "";
	private String last_updated_value = "";
	private String plugin_name_branding_value = "";
	private String server_pretext_value = "";
	private String plugins_pretext_value = "";
	private String css_file_name_value = "";
	private final PluginsManager plugin;
	private Plugin[] listofplugins;
	private String[] nameofplugins;

	public ListPlugins(PluginsManager instance) {
		plugin = instance;
	}

	protected boolean loadListPluginSettings() {
		try {
			prop.load(new FileInputStream(config_file));
			output_folder_name_value = prop.getProperty(output_folder_name);
			output_file_name_value = prop.getProperty(output_file_name);
			column_view_value = prop.getProperty(column_view);
			last_updated_value = prop.getProperty(last_updated);
			plugin_name_branding_value = prop.getProperty(plugin_name_branding);
			server_pretext_value = prop.getProperty(server_pretext);
			plugins_pretext_value = prop.getProperty(plugins_pretext);
			css_file_name_value = prop.getProperty(css_file_name);
			if (output_folder_name_value == null || output_file_name_value == null ||
			column_view_value == null || last_updated_value == null || plugin_name_branding_value == null ||
			server_pretext_value == null || plugins_pretext_value == null || css_file_name_value == null) {
				mcl.log(Level.SEVERE, plugin.pluginMessageString(config_file_name + " file is not in the proper format"));
				return false;
			} else {
				mcl.log(Level.INFO, plugin.pluginMessageString(config_file_name + " file successfully loaded"));
				column_view_value = column_view_value.trim().toLowerCase();
				last_updated_value = last_updated_value.trim().toLowerCase();
				plugin_name_branding_value = plugin_name_branding_value.trim().toLowerCase();
				return true;
			}
		} catch (IOException ioe) {
			mcl.log(Level.SEVERE, plugin.pluginMessageString(config_file_name + " file could not be loaded"));
			prop.clear();
			return false;
		}
	}

	protected String[] getFullPluginNames() {
		listofplugins = plugin.getServer().getPluginManager().getPlugins();
		String[] returnstring = new String[listofplugins.length];
		PluginDescriptionFile tempPDFile;
		for (int i = 0; i < listofplugins.length; i++) {
			tempPDFile = listofplugins[i].getDescription();
			returnstring[i] = tempPDFile.getFullName();
		}
		return returnstring;
	}

	private String listFullPluginNames(String cvv) {
		String returnstring = "";
		nameofplugins = getFullPluginNames();
		for (int j = 0; j < nameofplugins.length; j++) {
			if (j < nameofplugins.length - 1) {
				if (cvv.equalsIgnoreCase("off")) {
					returnstring += nameofplugins[j] + ", ";
				} else {
					returnstring += nameofplugins[j] + "<br />\n";
				}
			} else {
				returnstring += nameofplugins[j] + "\n";
			}
		}
		return returnstring;
	}

	private String lastUpdatedDate() {
		String returnstring = "";
		if (last_updated_value.equalsIgnoreCase("on")) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd @ HH:mm:ss");		
			returnstring = "<br /><br /><br /><br />This page was generated on: " + sdf.format(date) + "\n";
		}
		return returnstring;
	}

	private String pluginNameBranding() {
		String returnstring = "";
		if (plugin_name_branding_value.equalsIgnoreCase("on")) {
			returnstring += "<br /><br /><br/>Using " + plugin.getDescription().getFullName() + "<br />\n";
		}
		return returnstring;
	}

	private void listPluginsToFile() {
		String printtofile = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n";
		printtofile += "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n";
		printtofile += "<head>\n";
		printtofile += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />\n";
		if (css_file_name_value.length() != 0) {
			printtofile += "<link rel=\"stylesheet\" href=\"" + css_file_name_value + "\" type=\"text/css\" />\n";
		}
		printtofile += "<title>" + plugin.getServer().getName() + "</title>\n";
		printtofile += "</head>\n";
		printtofile += "<body>\n";
		printtofile += "<strong>" + server_pretext_value + "</strong><br />\n" + plugin.getServer().getVersion() + "\n<br /><br />\n";
		printtofile += "<strong>" + plugins_pretext_value + "</strong><br />\n";
		printtofile += listFullPluginNames(column_view_value);
		printtofile += lastUpdatedDate();
		printtofile += pluginNameBranding();
		printtofile += "</body>\n";
		printtofile += "</html>\n";
		try {
			File file_to_output = new File(output_folder_name_value, output_file_name_value);
			FileWriter fw = new FileWriter(file_to_output);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(printtofile);
			out.close();
			mcl.log(Level.INFO, plugin.pluginMessageString(output_file_name_value + " successfully created"));
		} catch (IOException ioe) {
			mcl.log(Level.SEVERE, plugin.pluginMessageString(output_file_name_value + " could not be created"));
		}
	}
}