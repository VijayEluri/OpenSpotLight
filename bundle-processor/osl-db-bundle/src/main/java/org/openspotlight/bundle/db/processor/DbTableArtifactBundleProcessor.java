package org.openspotlight.bundle.db.processor;

import java.util.HashSet;
import java.util.Set;

import org.openspotlight.bundle.db.DBConstants;
import org.openspotlight.bundle.db.metamodel.link.AbstractTypeBind;
import org.openspotlight.bundle.db.metamodel.link.CatalogTableView;
import org.openspotlight.bundle.db.metamodel.link.ColumnDataType;
import org.openspotlight.bundle.db.metamodel.link.GroupDatabase;
import org.openspotlight.bundle.db.metamodel.link.SchemaCatalog;
import org.openspotlight.bundle.db.metamodel.link.SchemaTableView;
import org.openspotlight.bundle.db.metamodel.link.TableViewColumns;
import org.openspotlight.bundle.db.metamodel.node.Catalog;
import org.openspotlight.bundle.db.metamodel.node.Column;
import org.openspotlight.bundle.db.metamodel.node.DataType;
import org.openspotlight.bundle.db.metamodel.node.Schema;
import org.openspotlight.bundle.db.metamodel.node.Server;
import org.openspotlight.bundle.db.metamodel.node.TableView;
import org.openspotlight.bundle.db.metamodel.node.TableViewTable;
import org.openspotlight.bundle.db.metamodel.node.TableViewView;
import org.openspotlight.federation.context.ExecutionContext;
import org.openspotlight.federation.domain.Artifact;
import org.openspotlight.federation.domain.LastProcessStatus;
import org.openspotlight.federation.domain.TableArtifact;
import org.openspotlight.federation.domain.ViewArtifact;
import org.openspotlight.federation.processing.BundleProcessor;
import org.openspotlight.graph.SLContext;
import org.openspotlight.graph.SLLink;
import org.openspotlight.graph.SLNode;

public class DbTableArtifactBundleProcessor implements
		BundleProcessor<TableArtifact>, DBConstants {

	public <A extends Artifact> boolean acceptKindOfArtifact(
			final Class<A> kindOfArtifact) {
		return TableArtifact.class.isAssignableFrom(kindOfArtifact);
	}

	public void beforeProcessArtifact(final TableArtifact artifact) {

	}

	public void didFinishProcessing(final ArtifactChanges<TableArtifact> changes) {

	}

	public void didFinishToProcessArtifact(final TableArtifact artifact,
			final LastProcessStatus status) {

	}

	public Class<TableArtifact> getArtifactType() {
		return TableArtifact.class;
	}

	public SaveBehavior getSaveBehavior() {
		return SaveBehavior.PER_ARTIFACT;
	}

	public LastProcessStatus processArtifact(final TableArtifact artifact,
			final CurrentProcessorContext currentContext,
			final ExecutionContext context) throws Exception {
<<<<<<< Updated upstream
		logger.info("adding node " + artifact.getTableName());
		currentContext.getCurrentNodeGroup().addNode(artifact.getTableName());
=======
		final SLContext databaseContext = context.getGraphSession()
				.createContext(DB_ABSTRACT_CONTEXT);
		final SLNode databaseContextNode = databaseContext.getRootNode();
		final Server server = currentContext.getCurrentNodeGroup().addNode(
				Server.class, artifact.getServerName());
		final Schema schema = server.addNode(Schema.class, artifact
				.getSchemaName());
		context.getGraphSession().addLink(GroupDatabase.class,
				currentContext.getCurrentNodeGroup(), server, false);

		Catalog catalog = null;
		if (artifact.getCatalogName() != null) {
			catalog = schema.addNode(Catalog.class, artifact.getCatalogName());
			context.getGraphSession().addLink(SchemaCatalog.class, schema,
					catalog, false);
		}
		final TableView table;
		final boolean isView = artifact instanceof ViewArtifact;
		final Class<? extends TableView> tableType = isView ? TableViewView.class
				: TableViewTable.class;
		final Class<? extends SLLink> tableParentLink = catalog != null ? CatalogTableView.class
				: SchemaTableView.class;

		final SLNode tableParent = catalog != null ? catalog : schema;
		table = tableParent.addNode(tableType, artifact.getTableName());
		final TableView abstractTable = databaseContextNode.addNode(
				TableView.class, artifact.getTableName());
		context.getGraphSession().addLink(AbstractTypeBind.class, table,
				abstractTable, false);
		context.getGraphSession().addLink(tableParentLink, tableParent, table,
				false);

		final Set<String> addedColumns = new HashSet<String>();
		for (final org.openspotlight.federation.domain.Column c : artifact
				.getColumns()) {
			final Column column = table.addNode(Column.class, c.getName());
			final Column abstractColumn = abstractTable.addNode(Column.class, c
					.getName());
			context.getGraphSession().addLink(AbstractTypeBind.class, column,
					abstractColumn, false);
			context.getGraphSession().addLink(TableViewColumns.class, table,
					column, false);

			final DataType dataType = databaseContextNode.addNode(
					DataType.class, c.getType().name());
			context.getGraphSession().addLink(ColumnDataType.class, column,
					dataType, false);
			// FIXME set data type string as a property on column

			addedColumns.add(c.getName());
		}
		for (final SLNode n : new HashSet<SLNode>(table.getNodes())) {
			if (n instanceof Column) {
				if (!addedColumns.contains(n.getName())) {
					n.remove();
				}
			}
		}
>>>>>>> Stashed changes
		return LastProcessStatus.PROCESSED;
	}

	public void selectArtifactsToBeProcessed(
			final CurrentProcessorContext currentContext,
			final ExecutionContext context,
			final ArtifactChanges<TableArtifact> changes,
			final ArtifactsToBeProcessed<TableArtifact> toBeReturned) {

	}

}
