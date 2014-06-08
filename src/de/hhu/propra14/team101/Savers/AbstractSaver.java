package de.hhu.propra14.team101.Savers;

import org.yaml.snakeyaml.Yaml;


/**
 * Abstract Saver that Initializes with a Yaml object
 */

public abstract class AbstractSaver {

    protected Yaml yaml;

    /**
     * Initializes with a yaml object
     */

    public AbstractSaver() {
        this.yaml = new Yaml();
    }
}
