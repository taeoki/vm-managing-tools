package com.ktds.ipc.vmlooks;

import org.junit.Test;

import module.Module;

public class CheckWeb_Test extends Module {

	@Test
	public void checkWebPage() 
	{
		System.out.println("[SystemLog] - Check WebPage");
		checkWebPage("https://www.kt.com/");
	}
}
