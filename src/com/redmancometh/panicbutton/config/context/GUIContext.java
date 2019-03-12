package com.redmancometh.panicbutton.config.context;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.redmancometh.panicbutton.config.ConfigManager;
import com.redmancometh.panicbutton.config.pojo.KillConfig;
import com.redmancometh.panicbutton.controller.ListController;
import com.redmancometh.panicbutton.controller.ProcessController;
import com.redmancometh.panicbutton.mediator.GUIManager;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

@Configuration
public class GUIContext {
	@Bean(name = "title-label")
	public Label titleLabel() {
		return new Label("Panic Button - Created by Brendan Curry");
	}

	@Bean(name = "kill-blacklist")
	public Button killBlacklist(ProcessController procMan) {
		Button killButton = new Button("Kill Blacklisted Processes");
		killButton.setOnMouseClicked((event) -> {
			procMan.killBlacklist();
		});
		GridPane.setMargin(killButton, new Insets(0, 0, 0, 120));
		return killButton;
	}

	@Bean(name = "kill-whitelist")
	public Button killWhitelist(ProcessController procMan) {
		Button killButton = new Button("Kill All but Whitelisted Processes");
		killButton.setOnMouseClicked((event) -> {
			procMan.killWhitelist();
		});
		return killButton;
	}

	@Bean(name = "remove-blacklist")
	public Button removeBlacklist(ProcessController procMan, ListController lists,
			@Qualifier("blacklist-box") ListView blacklistBox) {
		Button removeBlacklistButton = new Button("Remove from kill list");
		removeBlacklistButton.setOnMouseClicked(
				(event) -> lists.removeKillList(blacklistBox.getSelectionModel().getSelectedItem().toString()));
		GridPane.setMargin(removeBlacklistButton, new Insets(0, 0, 0, 120));
		return removeBlacklistButton;
	}

	@Bean(name = "remove-whitelist")
	public Button removeWhitelist(ProcessController procMan, ListController lists,
			@Qualifier("whitelist-box") ListView whitelistBox) {
		Button removeWhitelistButton = new Button("Remove from whitelist");
		removeWhitelistButton.setOnMouseClicked(
				(event) -> lists.removeWhitelist(whitelistBox.getSelectionModel().getSelectedItem().toString()));
		return removeWhitelistButton;
	}

	@Bean(name = "add-blacklist")
	public Button addBlacklist(ProcessController procMan, ListController lists) {
		Button removeBlacklistButton = new Button("Add to kill list");
		removeBlacklistButton.setOnMouseClicked((event) -> {
			TextInputDialog dialog = new TextInputDialog("New Kill List Item");
			dialog.setContentText(
					"Please input the process name you wish to kill list. Make sure it is *only in lowercase*!");
			Optional<String> item = dialog.showAndWait();
			if (!item.isPresent())
				popAlert();
			else
				lists.addKillList(item.get());
		});
		GridPane.setMargin(removeBlacklistButton, new Insets(0, 0, 0, 120));
		return removeBlacklistButton;
	}

	@Bean(name = "add-whitelist")
	public Button addWhitelist(ProcessController procMan, ListController lists) {
		Button removeWhitelistButton = new Button("Add to whitelist");
		removeWhitelistButton.setOnMouseClicked((event) -> {
			TextInputDialog dialog = new TextInputDialog("New Whitelist Item");
			dialog.setContentText(
					"Please input the process name you wish to whitelist. Make sure it is *only in lowercase*!");
			Optional<String> item = dialog.showAndWait();
			if (!item.isPresent())
				popAlert();
			else
				lists.addWhitelist(item.get());
		});
		return removeWhitelistButton;
	}

	@Bean(name = "whitelist-box")
	public ListView whitelistBox(@Qualifier("kill-conf-man") ConfigManager<KillConfig> killConf) {
		ListView whiteListBox = new ListView();
		whiteListBox.setItems(FXCollections.observableArrayList(killConf.getConfig().getWhiteList()));
		return whiteListBox;
	}

	@Bean(name = "blacklist-box")
	public ListView blacklistBox(@Qualifier("kill-conf-man") ConfigManager<KillConfig> killConf) {
		ListView blackListBox = new ListView();
		blackListBox.setItems(FXCollections.observableArrayList(killConf.getConfig().getKillList()));
		GridPane.setMargin(blackListBox, new Insets(0, 0, 0, 120));
		return blackListBox;
	}

	@Bean(name = "bind-wlkillkey")
	public TextField comboWL(@Qualifier("blacklist-box") ListView blacklistBox,
			@Qualifier("combo-killwl") KeyCombination killWl) {
		TextField bindField = new TextField(killWl.getDisplayText() + " (Click to Rebind)");
		return bindField;
	}

	@Bean(name = "bind-blkillkey")
	public TextField comboBL(@Qualifier("whitelist-box") ListView whitelistBox,
			@Qualifier("combo-killbl") KeyCombination killBl) {
		TextField bindField = new TextField(killBl.getDisplayText() + " (Click to Rebind)");
		GridPane.setMargin(bindField, new Insets(0, 0, 0, 120));
		return bindField;
	}

	@Bean
	public GridPane root() {
		GridPane pane = new GridPane();
		GridPane.setMargin(pane, new Insets(0, 0, 0, 30));
		return pane;
	}

	@Bean
	public GUIManager gui() {
		return new GUIManager();
	}

	@Bean
	public Scene mainScene(Pane root) {
		return new Scene(root, 800, 600);
	}

	private void popAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText("No input was detected. Please input text before hitting add!");
		alert.show();
	}
}
