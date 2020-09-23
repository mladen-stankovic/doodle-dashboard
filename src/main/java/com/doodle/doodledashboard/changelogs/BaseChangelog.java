package com.doodle.doodledashboard.changelogs;

import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * Created by mladen.stankovic on 2020-09-23.
 */
public abstract class BaseChangelog {
    protected Boolean devOrTestProfile(Environment environment) {
        return Arrays.stream(environment.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase("dev")
                        || env.equalsIgnoreCase("test")) );
    }
}
