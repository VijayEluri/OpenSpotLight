package org.openspotlight.federation.finder.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openspotlight.federation.domain.Repository;
import org.openspotlight.federation.domain.artifact.ArtifactSource;
import org.openspotlight.federation.domain.artifact.StringArtifact;
import org.openspotlight.federation.finder.FileSystemOriginArtifactLoader;
import org.openspotlight.federation.finder.JcrPersistentArtifactManager;
import org.openspotlight.jcr.provider.DefaultJcrDescriptor;
import org.openspotlight.jcr.provider.JcrConnectionProvider;
import org.openspotlight.jcr.provider.SessionWithLock;

import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class JcrPersistentArtifactManagerTest {
	/** The provider. */
	private static JcrConnectionProvider provider;

	private static ArtifactSource artifactSource;

	private static Repository repository;

	private static JcrPersistentArtifactManager persistenArtifactManager;

	/**
	 * Setup.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@BeforeClass
	public static void setup() throws Exception {
		provider = JcrConnectionProvider
				.createFromData(DefaultJcrDescriptor.TEMP_DESCRIPTOR);
		final SessionWithLock session = provider.openSession();
		artifactSource = new ArtifactSource();
		artifactSource.setName("classpath");
		artifactSource.setInitialLookup("./src");
		repository = new Repository();
		repository.setName("name");
		artifactSource.setRepository(repository);
		final FileSystemOriginArtifactLoader fileSystemFinder = new FileSystemOriginArtifactLoader();
		final Set<StringArtifact> artifacts = fileSystemFinder.listByPath(
				StringArtifact.class, artifactSource, null);
		persistenArtifactManager = new JcrPersistentArtifactManager(session,
				repository);
		for (StringArtifact artifact : artifacts)
			persistenArtifactManager.addTransient(artifact);
		persistenArtifactManager.saveTransientData();

	}

	@AfterClass
	public static void closeResources() {
		persistenArtifactManager.closeResources();
	}

	@Test
	public void shouldFindArtifacts() throws Exception {
		final StringArtifact sa = persistenArtifactManager.findByPath(
				StringArtifact.class,
				"/test/resources/artifacts/included/folder/file_included2");
		assertThat(sa, is(notNullValue()));
		assertThat(sa.getContent(), is(notNullValue()));

	}

	@Test
	public void shouldListArtifactNames() throws Exception {
		final Set<String> artifacts = persistenArtifactManager
				.getInternalMethods().retrieveNames(StringArtifact.class, null);

		assertThat(artifacts, is(notNullValue()));
		assertThat(artifacts.size(), is(not(0)));
		for (final String s : artifacts) {
			assertThat(s, is(notNullValue()));
		}
	}

	@Test
	public void shouldListArtifacts() throws Exception {
		final Set<StringArtifact> artifacts = persistenArtifactManager
				.listByPath(StringArtifact.class,
						"/main/java/org/openspotlight/federation");

		assertThat(artifacts, is(notNullValue()));
		assertThat(artifacts.size(), is(not(0)));
		for (final StringArtifact sa : artifacts) {
			assertThat(sa, is(notNullValue()));
			assertThat(sa.getContent(), is(notNullValue()));
		}
	}

}
