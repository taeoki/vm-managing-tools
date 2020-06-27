package setup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import controller.Command;



public class Setup {
	
	private static String currentPath;
	private static String playbookPath;
	private static String hostsPath;
	static Command cmd;
	/*
	 * config.json의 설정값들이 담길 변수
	 */
	static String username;
	static String passwd;
	static long timeout;
	static long diskUsageLimit;
	static long cpuUsageLimit;
	static long memoryUsageLimit;
	static ArrayList<String> serviceList;			// 확인해야할 서비스 리스트 
	static String ServiceList_String;
	static ArrayList<String> arrangeFolderList;		// 정리해야할 폴더 리스트 
	static ArrayList<String> triggerList;			// 확인해야할 시스템 로그
	static String backupFolder;						// 백업본이 저장될 변수 
	
	
	public Setup()
	{
		init();
		readConfiguration();
	}
	
	/**
	 * 현재 파일 경로와 플레이북 파일 경로 정
	 */
	public void init()
	{
		/*
		 * 빌드되고 있는 프로젝트의 현재위치 알아오기 
		 */
		File file = new File("");
		currentPath = file.getAbsolutePath();
		hostsPath = currentPath + "/setting/hosts";
		playbookPath = currentPath + "/playbook/";
		
		cmd = new Command();
	}
	/**
	 * ./setting/config.json 파일로 부터 설정 값을 읽어
	 */
	public static void readConfiguration()
	{
		JSONParser parser = new JSONParser();

		try {
			/*
			 * ../setting/config.json 에서 설정값을 불러옴 
			 */
			Object obj = parser.parse(new FileReader(getCurrentPath()+"/setting/config.json"));
			JSONObject jsonObject = (JSONObject) obj;
			
			username = (String) jsonObject.get("Username");
			passwd = (String) jsonObject.get("Password");
			timeout = (Long)jsonObject.get("Timeout");
			diskUsageLimit = (Long) jsonObject.get("DiskUsageLimit");
			cpuUsageLimit = (Long) jsonObject.get("CpuUsageLimit");
			memoryUsageLimit = (Long) jsonObject.get("MemoryUsageLimit");
			// arrangeFolderList 
			arrangeFolderList = new ArrayList<String>();
			JSONArray folder = (JSONArray) jsonObject.get("TargetFolder");
			Iterator<String> iter = folder.iterator();
			while ( iter.hasNext() )
			{
				arrangeFolderList.add(iter.next());
			}
			
			// Array ServiceList
			serviceList = new ArrayList<String>();
			JSONArray service = (JSONArray) jsonObject.get("Service");
			iter = service.iterator();
			ServiceList_String = service.toString();
			while ( iter.hasNext() )
			{
				serviceList.add(iter.next());
			}
			
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("[SystemError] '../setting/config.json' 파일이 존재하지 않습니다.");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ${workspace}/src/playbook에 있는 플레이북을 실행시키고 
	 * 결과값을 리턴함
	 * @param playbookName
	 * @return
	 */
	public static String RunPlaybook(String playbookName)
	{
		String command = "ansible-playbook -i "+hostsPath+" "+getPlaybookPath()+playbookName;
		String result = cmd.execute(command);
		return result;
	}
	
	public static String getCurrentPath()
	{
		return currentPath;
	}
	public static String getPlaybookPath() 
	{
		return playbookPath;
	}
	
	/**
	 * ansible 메인 서버에 command 를 입력 후, 
	 * 결과값을 리턴함.
	 * 
	 * @param command
	 * @return
	 */
	public static String shell(String command)
	{
		String result = cmd.execute(command);
		return result;
	}
	
	public static Object getService(String returnValue)
	{
		if( returnValue.contains("String") )
			return ServiceList_String;
		else if( returnValue.contains("ArrayList"))
			return serviceList;
		
		return null;
	}
	
	public static Long getCpuLimit()
	{
		return cpuUsageLimit;
	}
	
	public static Long getMemoryLimit()
	{
		return memoryUsageLimit;
	}
	public static Long getDiskLimit()
	{
		return diskUsageLimit;
	}
}
