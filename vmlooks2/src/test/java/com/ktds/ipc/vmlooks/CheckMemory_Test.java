package com.ktds.ipc.vmlooks;

import org.junit.Test;

import module.Module;

public class CheckMemory_Test extends Module {

	@Test
	public void CheckMemory() 
	{
		System.out.println("[SystemLog] - Check Memory Usage");
		checkMemoryUsage();
	}
}
