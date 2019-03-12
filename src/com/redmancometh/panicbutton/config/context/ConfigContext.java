package com.redmancometh.panicbutton.config.context;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.redmancometh.panicbutton.config.ConfigManager;
import com.redmancometh.panicbutton.config.context.pojo.KeyContainer;
import com.redmancometh.panicbutton.config.pojo.KillConfig;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

@Configuration
/**
 * 
 * @author Brendan T CUrry
 *
 */
public class ConfigContext {
	@Bean(name = "kill-conf-man")
	public ConfigManager<KillConfig> killConf() {
		ConfigManager<KillConfig> killConfMan = new ConfigManager("killlist.json", KillConfig.class);
		killConfMan.init();
		return killConfMan;
	}

	@Bean("combo-killwl")
	public KeyContainer killWhitelist(@Qualifier("kill-conf-man") ConfigManager<KillConfig> killConf) {
		String combo = killConf.getConfig().getWhitelistKillCombo();
		KeyCombination chosenCombo;
		KeyCombination defaultCombo = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN,
				KeyCombination.ALT_DOWN);
		try {
			chosenCombo = combo == null ? defaultCombo : KeyCodeCombination.keyCombination(combo);
		} catch (Exception e) {
			return new KeyContainer(defaultCombo);
		}
		return new KeyContainer(chosenCombo);
	}

	@Bean("combo-killbl")
	public KeyContainer killBlacklist(@Qualifier("kill-conf-man") ConfigManager<KillConfig> killConf) {
		String combo = killConf.getConfig().getBlacklistKillCombo();
		KeyCombination chosenCombo;
		KeyCombination defaultCombo = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN,
				KeyCombination.ALT_DOWN);
		try {
			chosenCombo = combo == null ? defaultCombo : KeyCodeCombination.keyCombination(combo);
		} catch (Exception e) {
			return new KeyContainer(defaultCombo);
		}
		return new KeyContainer(chosenCombo);
	}
}
