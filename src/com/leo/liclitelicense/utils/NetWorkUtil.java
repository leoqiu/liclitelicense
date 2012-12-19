package com.leo.liclitelicense.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.leo.liclitelicense.staticdata.LicLiteData;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * All the network related utilities
 * 
 * @author leo
 *
 */
public class NetWorkUtil {
	
	/**
	 * Take ssh connection and cmd as a parameters and return
	 * the result by executing this cmd on the server through ssh
	 * 
	 * 
	 * @param Connection connection
	 * @param String cmd
	 * 
	 * @return result string by ssh executing the cmd on the other machine 
	 */
	public static String[] getCmdOutputStrs(Connection connection, String cmd){
		String[] outputStrArr = new String[]{"", ""};
		Session session = null;
        try {
        	session = connection.openSession();
        	session.execCommand(cmd);
	        InputStream stdout = new StreamGobbler(session.getStdout());
	        InputStream stderr = new StreamGobbler(session.getStderr());

	        InputStreamReader insrout=new InputStreamReader(stdout);
	        InputStreamReader insrerr=new InputStreamReader(stderr);

	        BufferedReader stdoutReader = new BufferedReader(insrout);

	        BufferedReader stderrReader = new BufferedReader(insrerr);

	        while (true)
	        {
	            String line = stdoutReader.readLine();
	            if (line == null)
	            {
	                break;
	            }
	            outputStrArr[0] += line + "\n";
	        }

	        while (true)
	        {
	            String line = stderrReader.readLine();
	            if (line == null)
	            {    break;}
	            outputStrArr[1] += line;
	        }
	        
	        stdoutReader.close();
	        stderrReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        //close session and assign session null, for GC to collection it
        session.close();
        session = null;
        
		return outputStrArr;
	}
	
	public static void executeCmd(Connection connection, String cmd){
		Session session = null;
        try {
        	session = connection.openSession();
        	session.execCommand(cmd);
        	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        //close session and assign session null, for GC to collection it
        session.close();
        session = null;
	}
	
	/**
	 * save lmgrd log remotely and download it to local dir
	 * 
	 * @param connection
	 * @param cmd
	 */
	public static void executeSCPCmd(Connection connection, String cmd){
		Session session = null;
		SCPClient scp = null;
        try {
        	session = connection.openSession();
        	session.execCommand(cmd);
        	scp = connection.createSCPClient();
        	scp.get("/home/leo/Desktop/log/one_server_temp.log", LicLiteData.licLiteDataDir);
        	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //close session and assign session null, for GC to collection it
        session.close();
        session = null;
	}
}
