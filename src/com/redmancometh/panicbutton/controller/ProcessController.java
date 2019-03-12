package com.redmancometh.panicbutton.controller;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.redmancometh.panicbutton.config.ConfigManager;
import com.redmancometh.panicbutton.config.pojo.KillConfig;

@Controller
public class ProcessController {
	@Autowired
	@Qualifier("kill-conf-man")
	private ConfigManager<KillConfig> killConfigMan;

	public void killBlacklist() {
		Logger.getLogger(this.getClass().getName()).info("KILL");
		List<String> killList = killConfigMan.getConfig().getKillList();
		ProcessHandle.allProcesses().forEach((process) -> {
			Optional<String> cmd = process.info().command();
			if (cmd.isPresent()) {
				String procName = new File(cmd.get()).getName().toLowerCase();
				if (killList.contains(procName) && (!process.equals(ProcessHandle.current()))) {
					process.destroyForcibly();
				}
			}
		});
	}

	public void killWhitelist() {
		List<String> whiteList = killConfigMan.getConfig().getWhiteList();
		ProcessHandle.allProcesses().forEach((process) -> {
			Optional<String> cmd = process.info().command();
			if (cmd.isPresent()) {
				String procName = new File(cmd.get()).getName().toLowerCase();
				if (!whiteList.contains(procName))
					// Probably re-add the try-catch with an Alert dialog
					process.destroy();
			}
		});

	}
}