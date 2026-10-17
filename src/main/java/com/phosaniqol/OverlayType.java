package com.phosaniqol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OverlayType
{
	NONE("None"),
	VALUE("Value"),
	PERCENTAGE("Percentage");

	private final String overlayType;
}
