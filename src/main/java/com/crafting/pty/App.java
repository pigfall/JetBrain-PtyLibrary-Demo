package com.crafting.pty;
import com.pty4j.*;
import java.util.*;
import java.io.OutputStream;
import java.io.InputStream;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args )
	{
		try{
			System.out.println( "Hello World!" );
			String[] cmd = { "/bin/sh", "-l" };
			Map<String, String> env = new HashMap<>(System.getenv());
			env.put("TERM", "xterm");
			PtyProcess process = new PtyProcessBuilder().setCommand(cmd).setEnvironment(env).start();

			OutputStream os = process.getOutputStream();
			InputStream is = process.getInputStream();
			new Thread(() -> {
				try{
					is.transferTo(System.out);
				}catch(Exception e){
					System.out.println( "Catch exception" );
				}
			}).start();
			new Thread(() -> {
				try{
					System.in.transferTo(os);
				}catch(Exception e){
					System.out.println( "Catch exception" );
				}
			}).start();
			

			// ... work with the streams ...

			// wait until the PTY child process is terminated
			int result = process.waitFor();
		}catch(Exception e){
			System.out.println( "Catch exception" );
		}
	}
}
