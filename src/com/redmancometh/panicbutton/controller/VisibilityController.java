package com.redmancometh.panicbutton.controller;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.redmancometh.panicbutton.config.context.pojo.StageHolder;

import javafx.application.Platform;

@Controller
/**
 * 
 * @author Brendan T CUrry
 *
 */
public class VisibilityController {
	@Autowired
	private StageHolder stageHolder;
	@Autowired
	private TrayIcon trayIcon;
	@Autowired
	private AtomicBoolean clickedOnce;
	@Autowired
	private ScheduledExecutorService scheduler;

	public void createTrayIcon() {
		try {
			Logger.getLogger(this.getClass().getName()).info("CREATING TRAY ICON");
			SystemTray tray = SystemTray.getSystemTray();
			trayIcon.addMouseListener(mouseListener());
			tray.add(trayIcon);
			Logger.getLogger(this.getClass().getName()).info("TRAY ICON CREATED!");
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private MouseAdapter mouseListener() {
		return new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Platform.runLater(() -> {
					if (clickedOnce.get())
						stageHolder.getStage().show();
					else {
						clickedOnce.set(true);
						scheduler.schedule(() -> clickedOnce.set(false), 1, TimeUnit.SECONDS);
					}
				});
			}
		};
	}

	public void hideToTray() {
		stageHolder.getStage().hide();
	}

	public void showFromTray() {
		stageHolder.getStage().show();
	}

	public AtomicBoolean isHidden() {
		return new AtomicBoolean(false);
	}
}
