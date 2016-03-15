package com.tableview.manager;

import java.util.Properties;
import java.util.logging.Logger;

import panix.panic.persistence.EntityManager;
import panix.panic.persistence.EntityManagerFactory;

public final class EntityFactoryHolder {

	public static EntityManagerFactory emf = null;
	public static EntityManager em = null;

	public static EntityManager getEntityManager() {

		try {
			if (em == null && emf != null)
				if (emf != null)
					em = emf.getEntityManager();
		} catch (Exception ex) {

		}
		return em;
	}

	/**
	 * @param p
	 */
	public static void initEntityFactoryHolder(Properties p) {

		if (emf == null) {
			emf = new EntityManagerFactory(p);
		}
	}

	/**
	 * @param p
	 * @param logger
	 */
	public static void initEntityFactoryHolder(Properties p, Logger logger) {

		if (emf == null) {
			emf = new EntityManagerFactory(p);
			if (emf != null)
				emf.setLogger(logger);
		}
	}
}
