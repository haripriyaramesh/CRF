package com.asher_stern.crf.smalltests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.asher_stern.crf.postagging.data.penn.PennFileContentsParser;
import com.asher_stern.crf.postagging.data.penn.PennParserTreeNode;
import com.asher_stern.crf.utilities.CrfException;
import com.asher_stern.crf.utilities.ExceptionUtil;
import com.asher_stern.crf.utilities.log4j.Log4jInit;


public class DemoPennFile
{

	public static void main(String[] args)
	{
		try
		{
			Log4jInit.init(Level.DEBUG);
			new DemoPennFile(args[0]).go();
		}
		catch(Throwable t)
		{
			ExceptionUtil.logException(t, logger);
		}

	}
	

	
	public DemoPennFile(String filename)
	{
		super();
		this.filename = filename;
	}




	public void go()
	{
		StringBuilder sb = new StringBuilder();
		try(BufferedReader reader = new BufferedReader(new FileReader(filename)))
		{
			String line = reader.readLine();
			while (line != null)
			{
				sb.append(line).append("\n");
				line = reader.readLine();
			}
			
		}
		catch (IOException e)
		{
			throw new CrfException("IO problem.",e);
		}
		
		char[] contents = sb.toString().toCharArray();
		PennFileContentsParser parser = new PennFileContentsParser(contents);
		parser.parse();
		
		System.out.println("Number of trees: "+parser.getTrees().size());
		for (PennParserTreeNode tree : parser.getTrees())
		{
			printIndent(tree,0);
		}
		
	}
	
	private void printIndent(PennParserTreeNode node, int indent)
	{
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<indent;++i)
		{
			sb.append(" ");
		}
		sb.append(node.getNodeString());
		if (node.getChildren().size()==0)
		{
			sb.append(" (leaf)");
		}
		System.out.println(sb.toString());
		
		for (PennParserTreeNode child : node.getChildren())
		{
			printIndent(child,indent+1);
		}
	}
	
	private final String filename;
	
	private static final Logger logger = Logger.getLogger(DemoPennFile.class);
}
