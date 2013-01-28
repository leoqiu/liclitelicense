package com.leo.liclitelicense.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.leo.liclitelicense.beans.LicLiteServerInfoBean;
import com.leo.liclitelicense.beans.LicLiteUserInfoBean;
import com.leo.liclitelicense.staticdata.LicLiteData;

public class Parser {
	
	//for feature section and feature user section
	private static String regexFeature = "(Users\\s{1}of\\s{1}((?:\\w+|\\-|\\/|\\.)+?))" +
			"(:\\s*?\\(Total\\s{1}of\\s{1}(\\d+)\\s{1}license(?:s|)\\s{1}issued;)" +
			"(\\s*?Total\\s{1}of\\s{1}(\\d+)\\s{1}license(?:s|)\\s{1}in\\s{1}use\\))";
	private static Pattern patternFeature = Pattern.compile(regexFeature);
	private static Matcher matcherFeature = null;
	
	private static String regexUser = "^(\\w+?)\\s" +
			"((?:\\w+|\\s+|\\/|\\:|\\.|\\(|\\)|\\-)+?)" +
			"(,\\s{1}start\\s{1}((?:\\w+|\\s+|\\/|\\:|\\,)+))";
	private static Pattern patternUser = Pattern.compile(regexUser);
	private static Matcher matcherUser = null;	
	
	
	/**
	 * 
	 * @return
	 */
	public static List<LicLiteServerInfoBean> parseDownloadDataFile(String fileName){
		List<LicLiteServerInfoBean> licliteServerInfoList = new ArrayList<LicLiteServerInfoBean>();
		LicLiteData.startTime = (int) (System.currentTimeMillis() / 1000);
		//NetWorkUtil.executeCmd(conn, cmd4);
//NetWorkUtil.executeSCPCmd(conn, cmd6);
		
		//local sdcard data file
		String licliteData = LicLiteData.licLiteDataDir + File.separator + fileName;
		File licLocalFile = new File(licliteData);		
		FileReader reader = null;		
		String line = null;		
System.out.println("licliteData --> " + licliteData + " local file exist --> " + licLocalFile.exists());		
		LicLiteServerInfoBean licLiteServerInfoBean = null;
		LicLiteUserInfoBean licLiteUserInfoBean = null;
		int featureAccount = 0;
		int featureAccountAddOneConfirm = 0;
		try {
			
			if(licLocalFile.exists()){
				reader = new FileReader(licLocalFile);
				BufferedReader bf = new BufferedReader(reader);
				while((line = bf.readLine()) != null){
					line = line.trim();

					if(!line.isEmpty()){
	
						if(line.contains("Users of ")){
							featureAccount++;
							licLiteServerInfoBean = new LicLiteServerInfoBean(null, null, null);
							matcherFeature = patternFeature.matcher(line);
							String featureName = null;
							String featureNumIssued = null;
							String featureNumInUsed = null;
								while(matcherFeature.find()){
									featureName = matcherFeature.group(2);
									featureNumIssued = matcherFeature.group(4);
									featureNumInUsed = matcherFeature.group(6);
								}
							licLiteServerInfoBean.setFeatureName(featureName);
							licLiteServerInfoBean.setFeatureNumIssued(featureNumIssued);
							licLiteServerInfoBean.setFeatureNumInUsed(featureNumInUsed);

						}
						
						if(line.contains(", start ")){
							licLiteUserInfoBean = new LicLiteUserInfoBean(null, null, null, null);
								matcherUser = patternUser.matcher(line);
							String userName = null;
							String userServerName = null;
							String userStartTimeAndUserNumOfLicenses = null;
							String userStartTime = null;
							String userNumOfLicenses = null;
								while(matcherUser.find()){
									userName = matcherUser.group(1);
									userServerName = matcherUser.group(2);
									userStartTimeAndUserNumOfLicenses = matcherUser.group(4);					
								}
							licLiteUserInfoBean.setUserName(userName);
							licLiteUserInfoBean.setUserServerName(userServerName);
							/*
							 * check if there is more than 1 license that user in used
							 */
							
							if(userStartTimeAndUserNumOfLicenses.contains(", ")){
								String[] tmpArr = userStartTimeAndUserNumOfLicenses.split(", ");
								userStartTime = tmpArr[0];
								userNumOfLicenses = tmpArr[1];
							}else{
								userStartTime = userStartTimeAndUserNumOfLicenses;
								userNumOfLicenses = "1 license";
							}
							
							
							licLiteUserInfoBean.setUserStartTime(userStartTime);
							licLiteUserInfoBean.setUserNumOfLicenses(userNumOfLicenses);
							licLiteServerInfoBean.getLicLiteUserInfoBeanList().add(licLiteUserInfoBean);

						}	
						
						
						//check featureAccount add one
						if((featureAccountAddOneConfirm+1) == featureAccount){
							licliteServerInfoList.add(licLiteServerInfoBean);							
							featureAccountAddOneConfirm++;
						}		
						
					}
						
				}
				
				reader.close();
				bf.close();
			}
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			
		}
		
		return licliteServerInfoList;
	}
}
