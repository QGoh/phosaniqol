package com.phosaniqol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TotemHudStyle
{
	DEFAULT("Default"),
	NONE("None"),
	CUSTOM("Custom");

	private final String totemHudStyle;
}
