package com.redmancometh.panicbutton.config.context;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.redmancometh.panicbutton.adapter.NativeHookListener;
import com.redmancometh.panicbutton.config.context.pojo.StageHolder;

@Configuration
/**
 * 
 * @author Brendan T CUrry
 *
 */
public class SpringConfiguration {

	@Bean
	public NativeHookListener nativeHook() {
		return new NativeHookListener();
	}

	@Bean
	/**
	 * Only use half the CPU worth of threads. If this is used more later we'll
	 * increase to 8 for 4 core CPUs
	 * 
	 * @return
	 */
	public ScheduledExecutorService scheduler() {
		return Executors.newScheduledThreadPool(4);
	}

	@Bean
	public StageHolder stageHolder() {
		return new StageHolder();
	}
}
