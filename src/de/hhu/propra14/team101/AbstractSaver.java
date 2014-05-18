package de.hhu.propra14.team101;

import org.yaml.snakeyaml.Yaml;

public class AbstractSaver {
    protected Yaml yaml;

    /**
     * Initializes with a Yaml object
     */
    public AbstractSaver() {
        this.yaml = new Yaml();
    }
}
