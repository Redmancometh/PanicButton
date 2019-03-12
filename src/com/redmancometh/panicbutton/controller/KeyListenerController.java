package com.redmancometh.panicbutton.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Controller;

import com.redmancometh.panicbutton.adapter.NativeHookListener;
import com.redmancometh.panicbutton.config.context.pojo.KeyContainer;

import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

@Controller
/**
 * 
 * @author Brendan T CUrry
 *
 */
public class KeyListenerController implements InitializingBean {
	@Autowired
	private ProcessController processes;
	@Autowired
	private AutowireCapableBeanFactory factory;
	@Autowired
	@Qualifier("combo-killwl")
	private KeyContainer killWl;
	@Autowired
	@Qualifier("combo-killbl")
	private KeyContainer killBl;

	@Override
	public void afterPropertiesSet() throws Exception {
		GlobalScreen.registerNativeHook();
		Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.OFF);
		NativeHookListener listener = new NativeHookListener();
		factory.autowireBean(listener);
		GlobalScreen.addNativeKeyListener(listener);
	}

	public void passPressed(NativeKeyEvent nativeEvent) {
		String keyPressed = NativeKeyEvent.getKeyText(nativeEvent.getKeyCode());
		String keyModifiers = NativeKeyEvent.getModifiersText(nativeEvent.getModifiers());
		String comboString = keyModifiers + "+" + keyPressed;
		try {
			KeyCombination combo = KeyCodeCombination.keyCombination(comboString);
			if (combo.equals(killBl.getCombo()))
				processes.killBlacklist();
			else if (combo.equals(killWl.getCombo()))
				processes.killWhitelist();
		} catch (IllegalArgumentException e) {
			// There's a bunch of ridiculous combos we don't care about.
		} catch (Exception e) {
			// This exception we likely actually care about.
			e.printStackTrace();
		}
	}

	public void passReleased(NativeKeyEvent nativeEvent) {

	}

	public void passTyped(NativeKeyEvent event) {
	}

}
