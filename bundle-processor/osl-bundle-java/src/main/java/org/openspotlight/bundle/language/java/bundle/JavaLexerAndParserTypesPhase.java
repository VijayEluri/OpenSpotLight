package org.openspotlight.bundle.language.java.bundle;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.Tree;
import org.openspotlight.bundle.common.metrics.SourceLineInfoAggregator;
import org.openspotlight.bundle.common.parser.SLArtifactStream;
import org.openspotlight.bundle.common.parser.SLArtifactStreamBasicImpl;
import org.openspotlight.bundle.language.java.parser.JavaLexer;
import org.openspotlight.bundle.language.java.parser.JavaParser;
import org.openspotlight.bundle.language.java.parser.executor.JavaLexerExecutor;
import org.openspotlight.bundle.language.java.parser.executor.JavaParserExecutor;
import org.openspotlight.bundle.language.java.parser.executor.JavaParserNodeHelper;
import org.openspotlight.federation.context.ExecutionContext;
import org.openspotlight.federation.domain.artifact.LastProcessStatus;
import org.openspotlight.federation.domain.artifact.StringArtifact;
import org.openspotlight.federation.processing.BundleProcessorArtifactPhase;
import org.openspotlight.federation.processing.CurrentProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaLexerAndParserTypesPhase implements
		BundleProcessorArtifactPhase<StringArtifact> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public void beforeProcessArtifact(final StringArtifact artifact) {

	}

	public void didFinishToProcessArtifact(final StringArtifact artifact,
			final LastProcessStatus status) {

	}

	public Class<StringArtifact> getArtifactType() {
		return StringArtifact.class;
	}

	public LastProcessStatus processArtifact(final StringArtifact artifact,
			final CurrentProcessorContext currentContext,
			final ExecutionContext context) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug(" starting to process artifact " + artifact);
		}
		try {
			final SLArtifactStream stream = new SLArtifactStreamBasicImpl(
					artifact.getArtifactCompleteName(), artifact.getContent());
			final JavaLexer lexer = new JavaLexer(stream);
			final SourceLineInfoAggregator sourceLine = new SourceLineInfoAggregator();
			final JavaLexerExecutor lexerExecutor = new JavaLexerExecutor(
					artifact, sourceLine);
			lexer.setLexerExecutor(lexerExecutor);
			final CommonTokenStream commonTokenStream = new CommonTokenStream(
					lexer);
			commonTokenStream.getTokens();
			final JavaParser parser = new JavaParser(commonTokenStream);
			final JavaParserNodeHelper helper = new JavaParserNodeHelper(
					currentContext.getCurrentNodeGroup(), context
							.getGraphSession());
			final JavaParserExecutor parserExecutor = new JavaParserExecutor(
					sourceLine, helper);
			parser.setParserExecutor(parserExecutor);
			final Tree tree = (Tree) parser.compilationUnit().getTree();
			final JavaTransientDto dto = JavaTransientDto.fromParser()
					.withStream(stream).withLexer(lexer).withSourceline(
							sourceLine).withLexerExecutor(lexerExecutor)
					.withCommonTokenStream(commonTokenStream)
					.withParser(parser).withParserExecutor(parserExecutor)
					.withTree(tree).create();
			artifact.getTransientMap().put("DTO-Parser", dto);

			return LastProcessStatus.PROCESSED;
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("ending to process artifact " + artifact);
			}
		}
	}

}
