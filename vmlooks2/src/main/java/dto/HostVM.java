package dto;

import static org.junit.Assert.assertTrue;

public class HostVM {
	static String hostName;
	static String cpuUsage;
	static String memUsage;
	static String diskUsage;
	
	public void setName(String name) {
		this.hostName = name;
	}
	
	public void setCpuUsage(String usage)
	{
		this.cpuUsage = usage;
	}
	
	public void setMemUsage(String usage)
	{
		this.memUsage = usage;
	}
	
	public void setdiskUsage(String usage)
	{
		this.diskUsage = usage;
	}
	
	public void checkCpu(Long cpuUsageLimit)
	{
		//System.out.println("CPUUSAGELIMIT: "+cpuUsageLimit);
		//System.out.println("CPUUSAGE: "+cpuUsage);
		
		if( (double)cpuUsageLimit <= Double.parseDouble(cpuUsage))
		{
			//System.out.println("CheckPoint!!");
			assertTrue("[SystemLog-ERROR]("+hostName+")-CpuUsage: "+cpuUsage+"%"
					,false);
		}
	}
	
	public void checkDisk(Long diskUsageLimit)
	{
		//System.out.println("DiskUSAGELIMIT: "+diskUsageLimit);
		//System.out.println("DiskUSAGE: "+diskUsage);
		if( (double)diskUsageLimit <= Double.parseDouble(diskUsage) )
		{
			assertTrue("[SystemLog-ERROR]("+hostName+")-DiskUsage: "+diskUsage+"%"
					,false);
		}
	}
	
	public void checkMemory(Long memoryUsageLimit)
	{
		if( (double)memoryUsageLimit <= Double.parseDouble(memUsage) )
		{
			assertTrue("[SystemLog-ERROR]("+hostName+")-MemoryUsage: "+memUsage+"%"
					,false);
		}
	}
}
