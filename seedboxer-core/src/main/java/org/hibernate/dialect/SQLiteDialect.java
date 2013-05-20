/*******************************************************************************
 * SQLiteDialect.java
 * 
 * Copyright (c) 2013 SeedBoxer Team.
 * 
 * This file is part of SeedBoxer.
 * 
 * SeedBoxer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SeedBoxer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SeedBoxer.  If not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/

package org.hibernate.dialect;

import java.sql.Types;

import org.hibernate.dialect.function.AbstractAnsiTrimEmulationFunction;
import org.hibernate.dialect.function.NoArgSQLFunction;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class SQLiteDialect extends Dialect {
  public SQLiteDialect() {
    registerColumnType(Types.BIT, "boolean");
    registerColumnType(Types.TINYINT, "tinyint");
    registerColumnType(Types.SMALLINT, "smallint");
    registerColumnType(Types.INTEGER, "integer");
    registerColumnType(Types.BIGINT, "bigint");
    registerColumnType(Types.FLOAT, "float");
    registerColumnType(Types.REAL, "real");
    registerColumnType(Types.DOUBLE, "double");
    registerColumnType(Types.NUMERIC, "numeric($p, $s)");
    registerColumnType(Types.DECIMAL, "decimal");
    registerColumnType(Types.CHAR, "char");
    registerColumnType(Types.VARCHAR, "varchar($l)");
    registerColumnType(Types.LONGVARCHAR, "longvarchar");
    registerColumnType(Types.DATE, "date");
    registerColumnType(Types.TIME, "time");
    registerColumnType(Types.TIMESTAMP, "datetime");
    registerColumnType(Types.BINARY, "blob");
    registerColumnType(Types.VARBINARY, "blob");
    registerColumnType(Types.LONGVARBINARY, "blob");
    registerColumnType(Types.BLOB, "blob");
    registerColumnType(Types.CLOB, "clob");
    registerColumnType(Types.BOOLEAN, "boolean");

    //registerFunction( "abs", new StandardSQLFunction("abs") );
    registerFunction( "concat", new VarArgsSQLFunction(StandardBasicTypes.STRING, "", "||", "") );
    //registerFunction( "length", new StandardSQLFunction("length", StandardBasicTypes.LONG) );
    //registerFunction( "lower", new StandardSQLFunction("lower") );
    registerFunction( "mod", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "?1 % ?2" ) );
    registerFunction( "quote", new StandardSQLFunction("quote", StandardBasicTypes.STRING) );
    registerFunction( "random", new NoArgSQLFunction("random", StandardBasicTypes.INTEGER) );
    registerFunction( "round", new StandardSQLFunction("round") );
    registerFunction( "substr", new StandardSQLFunction("substr", StandardBasicTypes.STRING) );
    registerFunction( "substring", new SQLFunctionTemplate( StandardBasicTypes.STRING, "substr(?1, ?2, ?3)" ) );
    registerFunction( "trim", new AbstractAnsiTrimEmulationFunction() {
        protected SQLFunction resolveBothSpaceTrimFunction() {
          return new SQLFunctionTemplate(StandardBasicTypes.STRING, "trim(?1)");
        }

        protected SQLFunction resolveBothSpaceTrimFromFunction() {
          return new SQLFunctionTemplate(StandardBasicTypes.STRING, "trim(?2)");
        }

        protected SQLFunction resolveLeadingSpaceTrimFunction() {
          return new SQLFunctionTemplate(StandardBasicTypes.STRING, "ltrim(?1)");
        }

        protected SQLFunction resolveTrailingSpaceTrimFunction() {
          return new SQLFunctionTemplate(StandardBasicTypes.STRING, "rtrim(?1)");
        }

        protected SQLFunction resolveBothTrimFunction() {
          return new SQLFunctionTemplate(StandardBasicTypes.STRING, "trim(?1, ?2)");
        }

        protected SQLFunction resolveLeadingTrimFunction() {
          return new SQLFunctionTemplate(StandardBasicTypes.STRING, "ltrim(?1, ?2)");
        }

        protected SQLFunction resolveTrailingTrimFunction() {
          return new SQLFunctionTemplate(StandardBasicTypes.STRING, "rtrim(?1, ?2)");
        }
    } );
    //registerFunction( "upper", new StandardSQLFunction("upper") );
  }

  public boolean supportsIdentityColumns() {
    return true;
  }

  /*
  public boolean supportsInsertSelectIdentity() {
    return true; // As specify in NHibernate dialect
  }
  */

  public boolean hasDataTypeInIdentityColumn() {
    return false; // As specify in NHibernate dialect
  }

  /*
  public String appendIdentitySelectToInsert(String insertString) {
    return new StringBuffer(insertString.length()+30). // As specify in NHibernate dialect
      append(insertString).
      append("; ").append(getIdentitySelectString()).
      toString();
  }
  */

  public String getIdentityColumnString() {
    // return "integer primary key autoincrement";
    return "integer";
  }

  public String getIdentitySelectString() {
    return "select last_insert_rowid()";
  }

  public boolean supportsLimit() {
    return true;
  }

  public boolean bindLimitParametersInReverseOrder() {
    return true;
  }

  protected String getLimitString(String query, boolean hasOffset) {
    return new StringBuffer(query.length()+20).
      append(query).
      append(hasOffset ? " limit ? offset ?" : " limit ?").
      toString();
  }

  public boolean supportsTemporaryTables() {
    return true;
  }

  public String getCreateTemporaryTableString() {
    return "create temporary table if not exists";
  }

  public boolean dropTemporaryTableAfterUse() {
    return true; // TODO Validate
  }

  public boolean supportsCurrentTimestampSelection() {
    return true;
  }

  public boolean isCurrentTimestampSelectStringCallable() {
    return false;
  }

  public String getCurrentTimestampSelectString() {
    return "select current_timestamp";
  }

  public boolean supportsUnionAll() {
    return true;
  }

  public boolean hasAlterTable() {
    return false; // As specify in NHibernate dialect
  }

  public boolean dropConstraints() {
    return false;
  }

  /*
  public String getAddColumnString() {
    return "add column";
  }
  */

  public String getForUpdateString() {
    return "";
  }

  public boolean supportsOuterJoinForUpdate() {
    return false;
  }

  public String getDropForeignKeyString() {
    throw new UnsupportedOperationException("No drop foreign key syntax supported by SQLiteDialect");
  }

  public String getAddForeignKeyConstraintString(String constraintName,
      String[] foreignKey, String referencedTable, String[] primaryKey,
      boolean referencesPrimaryKey) {
    throw new UnsupportedOperationException("No add foreign key syntax supported by SQLiteDialect");
  }

  public String getAddPrimaryKeyConstraintString(String constraintName) {
    throw new UnsupportedOperationException("No add primary key syntax supported by SQLiteDialect");
  }

  public boolean supportsIfExistsBeforeTableName() {
    return true;
  }

  public boolean supportsCascadeDelete() {
    return true;
  }

  /* not case insensitive for unicode characters by default (ICU extension needed)
  public boolean supportsCaseInsensitiveLike() {
    return true;
  }
  */

  public boolean supportsTupleDistinctCounts() {
    return false;
  }

  public String getSelectGUIDString() {
    return "select hex(randomblob(16))";
  }
}