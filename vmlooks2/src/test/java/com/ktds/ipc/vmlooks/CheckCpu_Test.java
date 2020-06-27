package com.ktds.ipc.vmlooks;

import org.junit.Test;

import module.Module;

public class CheckCpu_Test extends Module {
	
	@Test
	public void CheckCpu()
	{
		System.out.println();
		System.out.println("[RESULT] ------------------------------------------------------------------------");
		System.out.println("[SystemLog] - Check Cpu Usage");
		checkCpuUsage();
		System.out.println();
	}
}
