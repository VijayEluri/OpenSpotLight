package org.openspotlight.bundle.context;

import org.openspotlight.bundle.annotation.ArtifactLoaderRegistry;
import org.openspotlight.federation.finder.OriginArtifactLoader;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class ExecutionContextModule extends AbstractModule {

	public ExecutionContextModule(
			final Iterable<Class<? extends OriginArtifactLoader>> loaderRegistry) {
		this.loaderRegistry = loaderRegistry;
	}

	private final Iterable<Class<? extends OriginArtifactLoader>> loaderRegistry;

	@Override
	protected void configure() {
		bind(ExecutionContext.class).to(DefaultExecutionContext.class);
		bind(
				new TypeLiteral<Iterable<Class<? extends OriginArtifactLoader>>>() {
				}).annotatedWith(ArtifactLoaderRegistry.class).toInstance(
				loaderRegistry);

	}

}
