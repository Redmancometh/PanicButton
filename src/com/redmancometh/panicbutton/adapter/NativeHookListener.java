package com.redmancometh.panicbutton.adapter;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.redmancometh.panicbutton.controller.KeyListenerController;

/**
 * The way this is done is dumb but necessary unfortuantely.
 * 
 * @author Brendan T CUrry
 *
 */
public class NativeHookListener implements NativeKeyListener {
	@Autowired
	private KeyListenerController listenController;

	@Override
	public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
		listenController.passTyped(nativeEvent);
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
		listenController.passPressed(nativeEvent);
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
		listenController.passReleased(nativeEvent);
	}

}
