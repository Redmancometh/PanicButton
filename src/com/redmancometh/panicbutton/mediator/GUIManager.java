package com.redmancometh.panicbutton.mediator;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import com.redmancometh.panicbutton.config.context.pojo.StageHolder;
import com.redmancometh.panicbutton.controller.VisibilityController;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;

@Controller
/**
 * 
 * @author Brendan T CUrry
 *
 */
public class GUIManager implements InitializingBean {
	/**
	 * State
	 */
	@Autowired
	private GridPane rootPane;
	@Autowired
	private Scene mainScene;
	@Autowired
	private StageHolder stageHolder;
	@Getter
	private Stage stage;
	/**
	 * Buttons
	 */
	@Autowired
	@Qualifier("kill-blacklist")
	private Button killBlacklist;
	@Autowired
	@Qualifier("kill-whitelist")
	private Button killWhitelist;
	@Autowired
	@Qualifier("remove-blacklist")
	private Button removeBlacklist;
	@Autowired
	@Qualifier("remove-whitelist")
	private Button removeWhitelist;
	@Autowired
	@Qualifier("add-whitelist")
	private Button addWhitelist;
	@Autowired
	@Qualifier("add-blacklist")
	private Button addBlacklist;
	/**
	 * Bind controls (TextFields)
	 */
	@Autowired
	@Qualifier("bind-blkillkey")
	private TextField bindKillBL;
	@Autowired
	@Qualifier("bind-wlkillkey")
	private TextField bindKillWL;
	/**
	 * Process list views
	 */
	@Autowired
	@Qualifier("whitelist-box")
	private ListView whitelistBox;
	@Autowired
	@Qualifier("blacklist-box")
	private ListView blacklistBox;
	/**
	 * Controllers
	 */
	@Autowired
	private VisibilityController visibility;

	@Override
	/**
	 * Construct the GUI. Due to the way JavaFX works we can't use @PostConstruct
	 * here without breaking things.
	 */
	public void afterPropertiesSet() throws Exception {
		Platform.setImplicitExit(false);
		stageHolder.setStage(stage);
		rootPane.setHgap(10);
		rootPane.setVgap(10);
		// rootPane.add(titleLabel, 1, 0);
		rootPane.add(killBlacklist, 0, 1);
		rootPane.add(killWhitelist, 1, 1);
		rootPane.add(blacklistBox, 0, 2);
		rootPane.add(whitelistBox, 1, 2);
		rootPane.add(removeBlacklist, 0, 3);
		rootPane.add(removeWhitelist, 1, 3);
		rootPane.add(addBlacklist, 0, 4);
		rootPane.add(addWhitelist, 1, 4);
		rootPane.add(bindKillBL, 0, 5);
		rootPane.add(bindKillWL, 1, 5);
		// This has to be set here, as the boxes have no width until the template
		// figures out the widths
	}

	/**
	 * Start the JavaFX side of things
	 * 
	 * @param stage This is the stage created automatically by JavaFX
	 */
	public void start(Stage stage) {
		setStage(stage);
		stageHolder.setStage(stage);
		stage.setScene(mainScene);
		stage.show();
		try (InputStream in = getClass().getResourceAsStream("/button.png")) {
			Image icon = new Image(in);
			stage.getIcons().add(icon);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initializeTray();
	}

	public void initializeTray() {
		visibility.createTrayIcon();
		stage.setOnCloseRequest((closeEvent) -> {
			Logger.getLogger(this.getClass().getName()).info("Close event");
			visibility.hideToTray();
		});
	}

	private void setStage(Stage stage) {
		this.stage = stage;
	}

}
