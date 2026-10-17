package com.phosaniqol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HighlightStyle
{
	NONE("None"),
	TILE("Tile"),
	HULL("Hull"),
	OUTLINE("Outline"),
	CLICKBOX("Clickbox");

	private final String highlightStyle;
}
