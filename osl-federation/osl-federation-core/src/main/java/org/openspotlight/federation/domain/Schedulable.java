package org.openspotlight.federation.domain;

import java.util.List;

/**
 * The Interface Schedulable.
 */
public interface Schedulable {

    /**
     * The Interface SchedulableCommand.
     */
    public static interface SchedulableCommand<S extends Schedulable> {

        /**
         * Execute.
         * 
         * @param schedulable the schedulable
         */
        public void execute( S schedulable );
    }

    /**
     * Gets the command class.
     * 
     * @return the command class
     */
    public Class<? extends SchedulableCommand<? extends Schedulable>> getCommandClass();

    /**
     * Gets the cron information.
     * 
     * @return the cron information
     */
    public List<String> getCronInformation();

}