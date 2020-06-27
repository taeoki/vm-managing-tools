package module;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;

import controller.Command;
import controller.StringController;
import controller.WebController;
import dto.HostVM;
import setup.Setup;

public class Module extends Setup {
	
	static Command cmd;
	static StringController resultAnalysis;
	static HashMap<String, HostVM> VMList;
	
	public Module()
	{
		cmd = new Command();
		VMList = new HashMap<String, HostVM>();
		resultAnalysis = new StringController(VMList);
	}
	
	/**
	 * 호스트VM과의 연결 확인 
	 */
	public static void checkPing() 
	{
		String result = RunPlaybook("checkPing.yml");
		
		StringBuffer buffer = new StringBuffer(result);
		
		System.out.println();
		System.out.println();
		System.out.println(result);
		System.out.println();
	}
	
	/**
	 * ${basedir}/setting/config.json 에 지정된 서비스의 상태를 확인 
	 * 
	 */
	public static void checkRunningService() 
	{
		String serviceList = (String) getService("String");
		
		/*
		 * --extra-vars '{"service_list": "1","2","3"}'
		 */
		String command = "checkService.yml --extra-vars '{\"service_list\": "+serviceList+"}'";
		String result = RunPlaybook(command);
		assertTrue(resultAnalysis.checkBuildSuccess(result));
		
		// 결과 
		
		System.out.println();
		System.out.println();
		System.out.println(result);
		System.out.println();
	}
	
	/**
	 * 디스크 사용량을 확인
	 * ${basedir}/setting/config.json 에 임계치 입력 시, 
	 * 디스크 사용 위험 알림 확인할 수 있음 
	 * 
	 */
	public static void checkDiskUsage()
	{
		String result = RunPlaybook("checkDiskUsage.yml");
		assertTrue(resultAnalysis.checkBuildSuccess(result));
		//System.out.println(result);
		
		// 디스크 사용량만 파싱 후, HostVM 에 사용량 정보 저장.
		System.out.println();
		System.out.println();
		resultAnalysis.subStringDebug(result);
		System.out.println();
		
		if(VMList.size() > 0 && getDiskLimit() != null)
		{
			Iterator<String> keys = VMList.keySet().iterator();
			while(keys.hasNext())
			{
				HostVM VM = VMList.get(keys.next());
				VM.checkDisk(getDiskLimit());
			}
		}
		
	}
	
	/**
	 * Cpu 사용량을 확인 
	 * ${basedir}/setting/config.json에 임계치 입력 시, 
	 * Cpu 사용 위험 알림을 확인할 수 있음
	 */
	public static void checkCpuUsage() 
	{
		String result = RunPlaybook("checkCpuUsage.yml");
		assertTrue(resultAnalysis.checkBuildSuccess(result));
		//System.out.println(result);
		
		System.out.println();
		System.out.println();
		resultAnalysis.subStringDebug(result);
		System.out.println();
		
		if(VMList.size() > 0 && getCpuLimit() != null)
		{
			Iterator<String> keys = VMList.keySet().iterator();
			while(keys.hasNext())
			{
				HostVM VM = VMList.get(keys.next());
				VM.checkCpu(getCpuLimit());
			}
		}
	}
	
	/**
	 * Memory 사용량을 확인 
	 * ${basedir}/setting/config.json에 임계치 입력 시,
	 * Memory 사용 위험 알림을 확인할 수 있음
	 */
	public static void checkMemoryUsage() 
	{
		String result = RunPlaybook("checkMemoryUsage.yml");
		assertTrue(resultAnalysis.checkBuildSuccess(result));
		//System.out.println(result);
		
		System.out.println();
		System.out.println();
		resultAnalysis.subStringDebug(result);
		System.out.println();
		
		//System.out.println("checkpoint - VMSIZE" + VMList.size() );
		if(VMList.size() > 0 && getMemoryLimit() != null)
		{
			Iterator<String> keys = VMList.keySet().iterator();
			while(keys.hasNext())
			{
				HostVM VM = VMList.get(keys.next());
				VM.checkMemory(getMemoryLimit());
			}
		}
	}
	
	/**
	 * 
	 * @param url
	 */
	public static void checkWebPage(String url)
	{
		WebController web = new WebController(url);	// 웹 컨트롤러 생성
		
		//web.setURL(url);		// 'url' 페이지의 정보를 받아옴 
		//web.checkConnection();
		//web.quitDriver();
	}
	
	/**
	 * 각 VM으로 Command 를 보내고 결과를 받음.
	 * 
	 * @param command
	 */
	public static void inputCommand(String command)
	{
		/*
		 * command 명령어 예외처리 
		 * rm -rf 
		 * wget
		 */
		String fatalCmd[] = {"rm -f", "rm", "reboot"};
		for(int i=0; i<fatalCmd.length; i++)
		{
			// 금지 단어가 포함되어 있을 때,
			if(command.contains(fatalCmd[i]))
			{
				System.out.println();
				System.out.println("[SystemLog-ERROR] 실행할 수 없는 키워가 포함되어 있습니다.");
				System.out.println("[SystemLog-ERROR] 실행한 명령어: "+command);
				System.out.println("[SystemLog-ERROR] 발견된 키워드: '"+fatalCmd[i]+"'");
				assertTrue(false);
			}
		}
		
		String result = RunPlaybook("inputCommand.yml --extra-vars '{\"command\": "+command+"}'");
		assertTrue(resultAnalysis.checkBuildSuccess(result));
		
		System.out.println();
		System.out.println();
		resultAnalysis.subStringDebug(result);
		System.out.println();
	}
	
	public static void cleanLogFile()
	{
		String result = RunPlaybook("copy_sh.yml");
	}
	
	
}
