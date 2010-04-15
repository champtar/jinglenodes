/**
 * $Revision: $
 * $Date: $
 *
 * Copyright (C) 2006 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Lesser Public License (LGPL),
 * a copy of which is included in this distribution.
 */

package org.jivesoftware.sparkimpl.settings;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.ModelUtil;
import org.jivesoftware.spark.util.log.Log;

import java.util.Map;

public class UserSettings {
    public static final String NAMESPACE = "jive:user:settings";
    public static final String ELEMENT_NAME = "personal_settings";
    private PrivateDataManager privateDataManager;
    private SettingsData settingsData;
    private static UserSettings singleton;
    private static final Object LOCK = new Object();

    public static UserSettings getInstance() {
        synchronized (LOCK) {
            if (null == singleton) {
                UserSettings controller = new UserSettings();
                singleton = controller;
                return controller;
            }
        }
        return singleton;
    }

    private UserSettings() {
        privateDataManager = new PrivateDataManager(SparkManager.getConnection());
        PrivateDataManager.addPrivateDataProvider("personal_settings", "jive:user:settings", new SettingsDataProvider());

        try {
            settingsData = (SettingsData)privateDataManager.getPrivateData("personal_settings", "jive:user:settings");
        }
        catch (XMPPException e) {
            Log.error("Error in User Settings", e);
        }
    }

    public Map<String,String> getSettings() {
        try {
            return settingsData.getMap();
        }
        catch (Exception ex) {
            Log.error("Error in User Settings.", ex);
        }
        return null;
    }

    public void setProperty(String name, String value) {
        getSettings().put(name, value);
    }

    public void setProperty(String name, boolean showtime) {
        getSettings().put(name, Boolean.toString(showtime));
    }

    public void setProperty(String name, int value) {
        getSettings().put(name, Integer.toString(value));
    }

    public String getProperty(String name) {
        return getSettings().get(name);
    }

    public String getEmptyPropertyIfNull(String name) {
        return ModelUtil.nullifyIfEmpty(getSettings().get(name));
    }

    public void save() {
        try {
            privateDataManager.setPrivateData(settingsData);
        }
        catch (XMPPException e) {
            Log.error("Error in User Settings.", e);
        }
    }


}