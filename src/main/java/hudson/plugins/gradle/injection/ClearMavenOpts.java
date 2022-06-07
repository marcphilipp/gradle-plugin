package hudson.plugins.gradle.injection;

import hudson.EnvVars;
import jenkins.security.MasterToSlaveCallable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ClearMavenOpts {

    private final Set<String> keys;

    public ClearMavenOpts(String... keys) {
        this.keys = new HashSet<>(Arrays.asList(keys));
    }

    public String removeSystemProperties(String mavenOpts) throws RuntimeException {
        return Optional.ofNullable(mavenOpts)
                .map(this::filterMavenOpts)
                .orElse("");
    }

    /**
     * Splits MAVEN_OPTS at each space and then removes all key value pairs that contain
     * any of the keys we want to remove.
     */
    private String filterMavenOpts(String mavenOpts) {
        return Arrays.stream(mavenOpts.split(" "))
                .filter(this::shouldBeKept)
                .collect(Collectors.joining(" "))
                .trim();
    }

    /**
     * Checks for a MAVEN_OPTS key value pair whether it contains none of the keys we're looking for.
     * In other words if this segment none of the keys, this method returns true.
     */
    private boolean shouldBeKept(String seg) {
        return keys.stream().noneMatch(seg::contains);
    }
}
