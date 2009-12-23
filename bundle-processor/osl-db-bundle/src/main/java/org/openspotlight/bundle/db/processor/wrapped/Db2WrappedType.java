/*
 * OpenSpotLight - Open Source IT Governance Platform
 *
 * Copyright (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA
 * or third-party contributors as indicated by the @author tags or express
 * copyright attribution statements applied by the authors.  All third-party
 * contributions are distributed under license by CARAVELATECH CONSULTORIA E
 * TECNOLOGIA EM INFORMATICA LTDA.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License  for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 ***********************************************************************
 * OpenSpotLight - Plataforma de Governança de TI de Código Aberto
 *
 * Direitos Autorais Reservados (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA
 * EM INFORMATICA LTDA ou como contribuidores terceiros indicados pela etiqueta
 * @author ou por expressa atribuição de direito autoral declarada e atribuída pelo autor.
 * Todas as contribuições de terceiros estão distribuídas sob licença da
 * CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA.
 *
 * Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free Software
 * Foundation.
 *
 * Este programa é distribuído na expectativa de que seja útil, porém, SEM NENHUMA
 * GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU ADEQUAÇÃO A UMA
 * FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral Menor do GNU para mais detalhes.
 *
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto com este
 * programa; se não, escreva para:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.openspotlight.bundle.db.processor.wrapped;

import org.openspotlight.bundle.db.metamodel.node.Catalog;
import org.openspotlight.bundle.db.metamodel.node.Column;
import org.openspotlight.bundle.db.metamodel.node.DataType;
import org.openspotlight.bundle.db.metamodel.node.Database;
import org.openspotlight.bundle.db.metamodel.node.DatabaseConstraintForeignKey;
import org.openspotlight.bundle.db.metamodel.node.DatabaseConstraintPrimaryKey;
import org.openspotlight.bundle.db.metamodel.node.Schema;
import org.openspotlight.bundle.db.metamodel.node.Server;
import org.openspotlight.bundle.db.metamodel.node.TableView;
import org.openspotlight.bundle.db.metamodel.node.TableViewTable;
import org.openspotlight.bundle.db.metamodel.node.TableViewView;
import org.openspotlight.bundle.db.metamodel.node.impl.Db2Catalog;
import org.openspotlight.bundle.db.metamodel.node.impl.Db2Column;
import org.openspotlight.bundle.db.metamodel.node.impl.Db2DataType;
import org.openspotlight.bundle.db.metamodel.node.impl.Db2Database;
import org.openspotlight.bundle.db.metamodel.node.impl.Db2DatabaseConstraintForeignKey;
import org.openspotlight.bundle.db.metamodel.node.impl.Db2DatabaseConstraintPrimaryKey;
import org.openspotlight.bundle.db.metamodel.node.impl.Db2Schema;
import org.openspotlight.bundle.db.metamodel.node.impl.Db2Server;
import org.openspotlight.bundle.db.metamodel.node.impl.Db2TableView;
import org.openspotlight.bundle.db.metamodel.node.impl.Db2TableViewTable;
import org.openspotlight.bundle.db.metamodel.node.impl.Db2TableViewView;
import org.openspotlight.bundle.db.processor.DbWrappedType;

public enum Db2WrappedType implements DbWrappedType {

	INSTANCE;

	public Class<? extends Catalog> getCatalogType() {
		return Db2Catalog.class;
	}

	public Class<? extends Column> getColumnType() {

		return Db2Column.class;
	}

	public Class<? extends DatabaseConstraintForeignKey> getDatabaseConstraintForeignKeyType() {

		return Db2DatabaseConstraintForeignKey.class;
	}

	public Class<? extends DatabaseConstraintPrimaryKey> getDatabaseConstraintPrimaryKeyType() {

		return Db2DatabaseConstraintPrimaryKey.class;
	}

	public Class<? extends Database> getDatabaseType() {

		return Db2Database.class;
	}

	public Class<? extends DataType> getDataTypeType() {

		return Db2DataType.class;
	}

	public Class<? extends Schema> getSchemaType() {

		return Db2Schema.class;
	}

	public Class<? extends Server> getServerType() {

		return Db2Server.class;
	}

	public Class<? extends TableViewTable> getTableViewTableType() {

		return Db2TableViewTable.class;
	}

	public Class<? extends TableView> getTableViewType() {

		return Db2TableView.class;
	}

	public Class<? extends TableViewView> getTableViewViewType() {

		return Db2TableViewView.class;
	}

}