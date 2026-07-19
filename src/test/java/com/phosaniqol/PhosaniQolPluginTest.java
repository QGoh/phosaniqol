package com.phosaniqol;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class PhosaniQolPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(PhosaniQolPlugin.class);
		RuneLite.main(args);
	}
}