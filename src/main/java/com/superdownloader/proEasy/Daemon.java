
package com.superdownloader.proEasy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author jdavison
 *
 */
public class Daemon implements Runnable {
	
	private static final String SPRING_DEFAULT_FILE = "application-context.xml";
	private static final Log LOGGER = LogFactory.getLog(Daemon.class);
	
	private boolean close;
	private Object closeLock;

	public static void main(String[] args) {
		new Daemon().start(args);
	}
	
	public Daemon() {
		close = false;
		closeLock = new Object();
	}
	
	private void start(String[] args){
		ApplicationContext context = new ClassPathXmlApplicationContext (SPRING_DEFAULT_FILE);
		Runtime.getRuntime().addShutdownHook(new DaemonShutdown());
	}
	
	private class DaemonShutdown extends Thread {
		
	    public void run() {
			synchronized(closeLock) {
				close = true;
			}
			LOGGER.info("Daemon stopped!");
	    }
	}

	@Override
	public void run() {
		LOGGER.debug("Starting daemon loop");
		boolean close = false;
		
		while (!close){
			synchronized(closeLock){
				close = this.close;
			}
		}
		LOGGER.debug("Ended daemon loop");
	}
}
