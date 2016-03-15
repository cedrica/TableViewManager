package com.tableview.manager;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import panix.essentials.logger.PILogger;
import panix.essentials.utilities.file.FileUtils;
import panix.essentials.utilities.plaf.IApplicationControl;
import panix.essentials.utilities.screenmanager.ScreenManager;


/**
 * Class <code>AppControl</code> manages and starts up the basic
 * routines. Database gets instantiated and configured. SessionCache loads
 * selectable.
 *
 * @author a.horoz
 * @update by cedric Leumaleu
 */
public final class AppControl implements IApplicationControl {

	// *************************************************************************************
	// *************************************************************************************
	/*
	 * singleton instance object
	 */
	private static AppControl applicationControl = null;
	private ScreenManager SCREENMANAGER = null;
	public static Logger LOGGER = null;


	private AppControl() throws Exception {

	}

	// *************************************************************************************
	// *************************************************************************************

	/**
	 * instantiates Application control
	 */
	public static void initApplicationStartUpControl() {
		if (applicationControl == null) {
			try {
				applicationControl = new AppControl();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// *************************************************************************************

	/**
	 * returns the singleton instance of class <code>ApplicationStartUpControl</code>
	 *
	 * @return
	 */
	public static AppControl getInst() {
		return applicationControl;
	}

	// *************************************************************************************

	public synchronized ScreenManager getScrnManager() {
		return this.SCREENMANAGER;
	}

	// *************************************************************************************

	/*
	 * (non-Javadoc)
	 * @see com.piwag.prisma.startup.IApplicationControl#performUpdate()
	 */
	@Override
	public void performUpdate() throws Exception {
	}

	// *************************************************************************************

	/*
	 * (non-Javadoc)
	 * @see com.piwag.prisma.startup.IApplicationControl#initScreenManager()
	 */
	@Override
	public void initScreenManager() throws Exception {
		if (SCREENMANAGER == null)
			SCREENMANAGER = new ScreenManager();
	}

	// *************************************************************************************

	/*
	 * (non-Javadoc)
	 * @see com.piwag.prisma.startup.IApplicationControl#initDataBase()
	 */
	@Override
	public void initDataBase(String... propPath) throws Exception {
		Properties p = null;
		if (propPath != null && propPath.length > 0 && propPath[0] != null && !propPath[0].isEmpty()) {
			p = FileUtils.aquierePropertiesFile(propPath[0]);
		} else {
			p = FileUtils.aquierePropertiesFile("connection.properties");
		}
		EntityFactoryHolder.initEntityFactoryHolder(p, LOGGER);
	}

	// *************************************************************************************

	/*
	 * (non-Javadoc)
	 * @see com.piwag.prisma.startup.IApplicationControl#initAdditional()
	 */
	@Override
	public void initLastAdditional() throws Exception {

	}

	// *************************************************************************************

	@Override
	public void initLogger() {
		PILogger.initLogger(Level.INFO);
		LOGGER = PILogger.getlogger();
	}

	// *************************************************************************************

	@Override
	public boolean proofUserAuthentification(String username, String password) {
		return true;
	}

	// *************************************************************************************

	@Override
	public void initOnLoginSucess() throws Exception {
	}

	// *************************************************************************************

	/*
	 * (non-Javadoc)
	 * @see com.piwag.prisma.startup.IApplicationControl#resetApplicationControl()
	 */
	@Override
	public void resetApplicationControl() {
	}

	@Override
	public void closeApplicationAndSaveNecessaryFiles() {
		// TODO Auto-generated method stub

	}

	@Override
	public void performAfterInit() throws Exception {
		// TODO Auto-generated method stub

	}

}
