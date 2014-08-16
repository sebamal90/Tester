/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 *
 * @author SebaTab
 */
public final class Config {

    public static String DEVICE_ADDRESS;
    public static String DEVICE_NAME;
    public static String DEVICE_TYPE;
    public static Locale LOCALE;
    private static final String CONFIG_DIR = "config.properties";
    public static ResourceBundle labels;

    public static void loadConfig() {
        Properties prop = new Properties();
	InputStream input = null;

	try {

		input = new FileInputStream(CONFIG_DIR);

		// load a properties file
		prop.load(input);

		DEVICE_ADDRESS = prop.getProperty("deviceAddress");
		DEVICE_NAME = prop.getProperty("deviceName");
		DEVICE_TYPE = prop.getProperty("deviceType");
                LOCALE = new Locale(prop.getProperty("localeLanguage"),
                                    prop.getProperty("localeCountry"));

                labels = ResourceBundle.getBundle("ibm.eti.pg.gda.pl.magisterka.malecki.test.config.config", LOCALE);

	} catch (IOException ex) {
		createConfig();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    }

    public static void saveConfig() {
        Properties prop = new Properties();
	OutputStream output = null;

	try {

		output = new FileOutputStream("config.properties");

		// set the properties value
		prop.setProperty("deviceAddress", "0022D000F0A7");
		prop.setProperty("deviceName", "Polar iWL");
		prop.setProperty("deviceType", "Polar Wear-Link");
                prop.setProperty("localeLanguage", "pl");
                prop.setProperty("localeCountry", "PL");

		// save properties to project root folder
		prop.store(output, null);

	} catch (IOException io) {
		io.printStackTrace();
	} finally {
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
    }

    public static void createConfig() {
        Properties prop = new Properties();
	OutputStream output = null;

	try {

		output = new FileOutputStream(CONFIG_DIR);

                prop.setProperty("localeLanguage", "pl");
                prop.setProperty("localeCountry", "PL");

		// save properties to project root folder
		prop.store(output, null);

                LOCALE = new Locale(prop.getProperty("localeLanguage"),
                                    prop.getProperty("localeCountry"));

	} catch (IOException io) {
		io.printStackTrace();
	} finally {
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
    }
}
