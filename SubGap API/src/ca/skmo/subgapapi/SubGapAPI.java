package ca.skmo.subgapapi;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jsoup.Jsoup;

/*
 * 
 * Created by - Ocean Sharma
 * Version 1.0.1
 * 
 */

public class SubGapAPI extends JavaPlugin {
	
	SubGapAPI pl = this;
	
	// Create strings for storing config strings
	String gap = "";
	String noPerm = "";
	
	// Import strings from Config
	@Override
	public void onEnable() {
		// Find Library
		try {
            final File[] libs = new File[] {
                    new File(getDataFolder(), "jsoup.jar")};
            for (final File lib : libs) {
                if (!lib.exists()) {
                    JarUtils.extractFromJar(lib.getName(),
                            lib.getAbsolutePath());
                }
            }
            // Import Library
            for (final File lib : libs) {
                if (!lib.exists()) {
                    getLogger().warning(
                            "There was a critical error loading My plugin! Could not find lib: "
                                    + lib.getName());
                    Bukkit.getServer().getPluginManager().disablePlugin(this);
                    return;
                }
                addClassPath(JarUtils.getJarUrl(lib));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
	}
	
	// Add Class Path
	private void addClassPath(final URL url) throws IOException {
        final URLClassLoader sysloader = (URLClassLoader) ClassLoader
                .getSystemClassLoader();
        final Class<URLClassLoader> sysclass = URLClassLoader.class;
        try {
            final Method method = sysclass.getDeclaredMethod("addURL",
                    new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { url });
        } catch (final Throwable t) {
            t.printStackTrace();
            throw new IOException("Error adding " + url
                    + " to system classloader");
        }
    }
		
	// Returns specified YouTuber's sub count
	public int getSubCount(String channelId, String apiKey) throws IOException {
		int subCount = 0;
		int start = 0;
		int end = 0;
		
		String doc = Jsoup.connect("https://www.googleapis.com/youtube/v3/channels?part=statistics&id=" + channelId + "&key=" + apiKey).userAgent("Mozilla").ignoreContentType(true).get().html();
		
		start = doc.indexOf("subscriberCount") + 19;
		end = doc.indexOf("\"", start + 1);
		
		int middle = end - start;
		
		subCount = Integer.parseInt(doc.substring(doc.indexOf("subscriberCount") + 19, doc.indexOf("subscriberCount") + (19 + middle)));
		
	    return subCount;
	}
	
}
