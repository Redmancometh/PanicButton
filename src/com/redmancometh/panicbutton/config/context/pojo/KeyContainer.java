package com.redmancometh.panicbutton.config.context.pojo;

import javafx.scene.input.KeyCombination;
import lombok.Data;
import lombok.NonNull;

@Data
/**
 * We need mutability so this class exists.
 * 
 * @author Brendan T CUrry
 *
 */
public class KeyContainer {
	@NonNull
	private KeyCombination combo;
}
