package com.redmancometh.panicbutton.config.context;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.redmancometh.panicbutton.controller.ProcessController;

@Configuration
public class VisibilityContext {
	@Bean
	/**
	 * So this can easily be shared across the application this is necessary.
	 * Atomified for thread safety.
	 * 
	 * @return
	 */
	public AtomicBoolean clickedTrayOnce() {
		return new AtomicBoolean(false);
	}

	@Bean
	public TrayIcon trayIcon(PopupMenu menu) {
		try (InputStream in = getClass().getResourceAsStream("/button.png")) {
			Image image = ImageIO.read(in);
			Logger.getLogger(this.getClass().getName()).info("Image null: " + (image == null));
			TrayIcon icon = new TrayIcon(image, "Panic Button", menu);
			icon.setImageAutoSize(true);
			return icon;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Bean
	public PopupMenu popupMenu(@Qualifier("tray-killbl") MenuItem killBl, @Qualifier("tray-killwl") MenuItem killWl,
			@Qualifier("tray-exit") MenuItem close) {
		PopupMenu menu = new PopupMenu();
		menu.add(killWl);
		menu.add(killBl);
		menu.add(close);
		return menu;
	}

	@Bean("tray-killbl")
	public MenuItem killBlacklist(ProcessController processes) {
		MenuItem item = new MenuItem("Kill Blacklist");
		item.addActionListener((e) -> processes.killBlacklist());
		return item;
	}

	@Bean("tray-killwl")
	public MenuItem killWhitelist(ProcessController processes) {
		MenuItem item = new MenuItem("Kill Whitelist");
		item.addActionListener((e) -> processes.killWhitelist());
		return item;
	}

	@Bean("tray-exit")
	public MenuItem close() {
		MenuItem item = new MenuItem("Exit");
		item.addActionListener((e) -> System.exit(0));
		return item;

	}

}
