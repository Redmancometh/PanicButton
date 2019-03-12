package com.redmancometh.panicbutton.config.pojo;

import java.util.List;

import lombok.Data;

@Data
public class KillConfig {
	private List<String> killList;
	private List<String> whiteList;
	private String whitelistKillCombo;
	private String blacklistKillCombo;
}
