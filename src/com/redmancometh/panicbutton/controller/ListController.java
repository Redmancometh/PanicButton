package com.redmancometh.panicbutton.controller;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.redmancometh.panicbutton.config.ConfigManager;
import com.redmancometh.panicbutton.config.pojo.KillConfig;

import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

@Controller
public class ListController implements InitializingBean {
	@Autowired
	@Qualifier("kill-conf-man")
	private ConfigManager<KillConfig> killConfigMan;
	@Autowired
	@Qualifier("blacklist-box")
	private ListView blacklistList;
	@Autowired
	@Qualifier("whitelist-box")
	private ListView whitelistList;

	/**
	 * Remove item from kill ilst in config and save json update
	 * 
	 * @param remove
	 */
	public void removeKillList(String remove) {
		killConfigMan.getConfig().getKillList().remove(remove);
		killConfigMan.saveConfig();
		updateBox(blacklistList, killConfigMan.getConfig().getKillList());
	}

	/**
	 * Add item to kill list in config and save json update
	 * 
	 * @param add
	 */
	public void addKillList(String add) {
		killConfigMan.getConfig().getKillList().add(add);
		killConfigMan.saveConfig();
		updateBox(blacklistList, killConfigMan.getConfig().getKillList());
	}

	/**
	 * Remove item from whitelist in config and save json update
	 * 
	 * @param add
	 */
	public void removeWhitelist(String remove) {
		killConfigMan.getConfig().getWhiteList().remove(remove);
		killConfigMan.saveConfig();
		updateBox(whitelistList, killConfigMan.getConfig().getWhiteList());

	}

	/**
	 * Add item to whtielist in config and save json update
	 * 
	 * @param add
	 */
	public void addWhitelist(String add) {
		killConfigMan.getConfig().getWhiteList().add(add);
		killConfigMan.saveConfig();
		updateBox(whitelistList, killConfigMan.getConfig().getWhiteList());
	}

	/**
	 * This is to be called upon a configuration reloading callback via the
	 * FileWatcher callback system
	 */
	private void updateBoxes() {
		updateBox(whitelistList, killConfigMan.getConfig().getWhiteList());
		updateBox(blacklistList, killConfigMan.getConfig().getKillList());
	}

	/**
	 * Update a specific ListView box
	 * 
	 * @param box
	 * @param newContent
	 */
	private void updateBox(ListView box, List<String> newContent) {
		box.setItems(FXCollections.observableArrayList(newContent));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		killConfigMan.setOnReload(this::updateBoxes);
	}

}
