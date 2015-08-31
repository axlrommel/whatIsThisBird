package com.villagomezdiaz.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryScanner {

	private String fileTypeToSearch = "*";
	
	private String directoryToSearch;
	
	private boolean loggit = false;
	
	private List<File> result = new ArrayList<File>();

	public String getDirectoryToSearch() {
		return directoryToSearch;
	}

	public void setDirectoryToSearch(String directoryToSearch) {
		this.directoryToSearch = directoryToSearch;
	}
	

	public String getFileTypeToSearch() {
		return fileTypeToSearch;
	}

	public void setFileTypeToSearch(String fileTypeToSearch) {
		this.fileTypeToSearch = fileTypeToSearch;
	}

	public List<File> getResult() {
		return result;
	}

	public static void main(String[] args) 
	{

		DirectoryScanner dScanner = new DirectoryScanner();
		
		dScanner.setDirectoryToSearch(args[0]);
		
		if(args.length == 2)
		{
			dScanner.setFileTypeToSearch(args[1]);
		}

		//try different directory and filename :)
		try
		{
			dScanner.searchDirectory();
	
			int count = dScanner.getResult().size();
			
			if(count == 0)
			{
				System.out.println("\nNo result found!");
			}
			else
			{
				System.out.println("\nFound " + count + " result!\n");
				for (File matched : dScanner.getResult())
				{
					System.out.println("Found : " + matched);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void searchDirectory() throws Exception{
		
		if(directoryToSearch != null)
		{
			File d = new File(directoryToSearch);
			if(!d.isDirectory())
			{
				Exception e = new Exception(d.getAbsolutePath() + " is not a directory");
				throw e;
			}
			
			search(d);
		}
		else
		{
			Exception e = new Exception("no directory has been defined");
			throw e;
		}
		
		
	}


	private void search(File d) throws Exception
	{
		
		if (d.isDirectory()) 
		{
			if(loggit)
			{
				System.out.println("Searching directory ... " + d.getAbsoluteFile());
			}

			//do you have permission to read this directory?	
			if (d.canRead()) 
			{
				for (File temp : d.listFiles()) 
				{
					if (temp.isDirectory()) 
					{
						search(temp);
					} 
					else 
					{
						if(getFileTypeToSearch().equals("*"))
						{
							result.add(temp);
						}
						else if(temp.getName().endsWith(getFileTypeToSearch()))
						{			
							result.add(temp);
						}

					}
				}

			} 
			else 
			{
				Exception e = new Exception(d + " don't have permission");
				throw e;
			}
		}
	}

	public boolean isLoggit() {
		return loggit;
	}

	public void setLoggit(boolean loggit) {
		this.loggit = loggit;
	}

}
