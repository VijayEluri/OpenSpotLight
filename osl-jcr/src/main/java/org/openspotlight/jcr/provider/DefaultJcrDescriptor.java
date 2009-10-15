package org.openspotlight.jcr.provider;

import javax.jcr.Credentials;
import javax.jcr.SimpleCredentials;

/**
 * The Enum DefaultJcrDescriptor contains the defaults {@link JcrConnectionDescriptor descriptors} used inside this project.
 */
public enum DefaultJcrDescriptor implements JcrConnectionDescriptor {

    /** The TEM p_ descriptor. */
    TEMP_DESCRIPTOR("/tmp/osl-temp-repository", new SimpleCredentials("username", "password".toCharArray()), JcrType.JACKRABBIT,
                    "temp-repository.xml"),

    /** The DEFAUL t_ descriptor. */
    DEFAULT_DESCRIPTOR("/osl/repository", new SimpleCredentials("username", "password".toCharArray()), JcrType.JACKRABBIT,
                       "postgres-repository.xml");

    /** The configuration directory. */
    private final String      configurationDirectory;

    /** The credentials. */
    private final Credentials credentials;

    /** The jcr type. */
    private final JcrType     jcrType;

    /** The xml classpath location. */
    private final String      xmlClasspathLocation;

    /**
     * Instantiates a new default jcr descriptor.
     * 
     * @param configurationDirectory the configuration directory
     * @param credentials the credentials
     * @param jcrType the jcr type
     * @param xmlClasspathLocation the xml classpath location
     */
    private DefaultJcrDescriptor(
                                  final String configurationDirectory, final Credentials credentials, final JcrType jcrType,
                                  final String xmlClasspathLocation ) {
        this.configurationDirectory = configurationDirectory;
        this.credentials = credentials;
        this.jcrType = jcrType;
        this.xmlClasspathLocation = xmlClasspathLocation;
    }

    /* (non-Javadoc)
     * @see org.openspotlight.jcr.provider.JcrConnectionDescriptor#getConfigurationDirectory()
     */
    public String getConfigurationDirectory() {
        return this.configurationDirectory;
    }

    /* (non-Javadoc)
     * @see org.openspotlight.jcr.provider.JcrConnectionDescriptor#getCredentials()
     */
    public Credentials getCredentials() {
        return this.credentials;
    }

    /* (non-Javadoc)
     * @see org.openspotlight.jcr.provider.JcrConnectionDescriptor#getJcrType()
     */
    public JcrType getJcrType() {
        return this.jcrType;
    }

    /* (non-Javadoc)
     * @see org.openspotlight.jcr.provider.JcrConnectionDescriptor#getXmlClasspathLocation()
     */
    public String getXmlClasspathLocation() {
        return this.xmlClasspathLocation;
    }

}