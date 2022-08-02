package com.floreantpos.add.service;

/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.derby.vti.VTITemplate;
import org.apache.derby.vti.RestrictedVTI;
import org.apache.derby.vti.Restriction;

/**
 * <p>
 * This class contains a table function which can be used to bulk-import data
 * from a foreign database. Because the table function is a RestrictedVTI, it
 * can also be used to periodically and efficiently integrate data streams from
 * a foreign database. More specifically, this class contains the following:
 * </p>
 *
 * <ul>
 * <li>A table function for use in reading data out of tables in foreign databases.</li>
 * <li>Procedures for registering and dropping table functions and convenience views against
 *  all of the tables in a schema in a foreign database.</li>
 * </ul>
 *
 * <p>
 * To use this machinery, first declare the helper procedures which create
 * and drop the foreign views:
 * </p>
 *
 * <pre><blockquote>
 * create procedure registerForeignTables
 * (
 *     in foreignSchemaName varchar( 32672 ),
 *     in foreignDriverName varchar( 32672 ),
 *     in connectionURL varchar( 32672 ),
 *     in debug boolean
 * )
 * language java parameter style java modifies sql data
 * external name 'ForeignTableVTI.registerForeignTables';
 * 
 * create procedure deregisterForeignTables
 * (
 *     in foreignSchemaName varchar( 32672 ),
 *     in foreignDriverName varchar( 32672 ),
 *     in connectionURL varchar( 32672 ),
 *     in debug boolean
 * )
 * language java parameter style java modifies sql data
 * external name 'ForeignTableVTI.deregisterForeignTables';
 * </blockquote></pre>
 *
 * <p>
 * Then create a schema for holding the foreign views and switch to
 * that schema:
 * </p>
 *
 * <pre><blockquote>
 * create schema foreignschema;
 * set schema foreignschema;
 * </blockquote></pre>
 *
 * <p>
 * Register views against all of the tables in a foreign schema. In this
 * example, the foreign database is a Cloudscape database and you register views
 * and table functions against all of the tables in its APP schema:
 * </p>
 *
 * <pre><blockquote>
 * call app.registerForeignTables
 * (
 *     'APP',
 *     'COM.cloudscape.core.JDBCDriver',
 *     'jdbc:cloudscape:olddbs/SalesDatabase',
 *     true
 * );
 * </blockquote></pre>
 *
 * <p>
 * Now bulk import data from the foreign views using INSERT
 * INTO...SELECT statements. The following example bulk-copies all rows from
 * a foreign table called "salesorders" into a local table by the same name:
 * </p>
 *
 * <pre><blockquote>
 * insert into app.salesorders select * from salesorders;
 * </blockquote></pre>
 *
 * <p>
 * If you need to siphon data out of the foreign database on an ongoing basis, you
 * can restrict the data you SELECT. Note that the local views are backed by
 * RestrictedVTIs. That means that the actual query sent to the foreign database
 * will only involve the columns you SELECT. In addition, the query will include the WHERE clause,
 * provided that it is simple enough (see the javadoc for RestrictedVTI):
 * </p>
 *
 * <pre><blockquote>
 * insert into app.neworderids select orderid from salesorders where orderid > 54321;
 * </blockquote></pre>
 *
 * <p>
 * When you are done with the foreign data, you can drop the views and table
 * functions:
 * </p>
 *
 * <pre><blockquote>
 * call app.deregisterForeignTables
 * (
 *     'APP',
 *     'COM.cloudscape.core.JDBCDriver',
 *     'jdbc:cloudscape:olddbs/SalesDatabase',
 *     true
 * );
 * </blockquote></pre>
 */
public	class   ForeignTableVTI extends VTITemplate implements  RestrictedVTI
{
    ////////////////////////////////////////////////////////////////////////
    //
    //	CONSTANTS
    //
    ////////////////////////////////////////////////////////////////////////

    private static  final   int XML_TYPE = 2009;    // needed if you aren't compiling against Java 6

    ////////////////////////////////////////////////////////////////////////
    //
    //	STATE
    //
    ////////////////////////////////////////////////////////////////////////

    private static  HashMap<String,Connection>  _connections = new HashMap<String,Connection>();

    private String  _foreignSchemaName;
    private String  _foreignTableName;
    private String  _foreignDriverName;
    private String  _connectionURL;
    private boolean _debug;

    private String[]    _columnNames;
    private Restriction _restriction;

    // this maps Derby columns (0-based) to foreign column numbers (1-based) in
    // the actual query
    private int[]               _columnNumberMap;
    private PreparedStatement   _foreignPreparedStatement;
    private ResultSet           _foreignResultSet;

    ////////////////////////////////////////////////////////////////////////
    //
    //	CONSTRUCTOR
    //
    ////////////////////////////////////////////////////////////////////////

    protected  ForeignTableVTI
        (
         String foreignSchemaName,
         String foreignTableName,
         String foreignDriverName,
         String connectionURL,
         boolean debug
         )
    {
        _foreignSchemaName = foreignSchemaName;
        _foreignTableName = foreignTableName;
        _foreignDriverName = foreignDriverName;
        _connectionURL = connectionURL;
        _debug = debug;
    }

    ////////////////////////////////////////////////////////////////////////
    //
    //	TABLE FUNCTION
    //
    ////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Table function to read a table in a foreign database.
     * </p>
     *
     * @param   foreignSchemaName   Case-sensitive name of foreign schema
     * @param   foreignTablename    Case-sensitive name of foreign table
     * @param   foreignDriverName   Class name of foreign JDBC driver
     * @param   connectionURL       URL for connecting to foreign database via DriverManager.getConnection()
     * @param   debug               True if query string should be printed to System.out
     */
    public  static  ForeignTableVTI readForeignTable
        (
         String foreignSchemaName,
         String foreignTableName,
         String foreignDriverName,
         String connectionURL,
         boolean debug
         )
    {
        return new ForeignTableVTI( foreignSchemaName, foreignTableName, foreignDriverName, connectionURL, debug );
    }

    ////////////////////////////////////////////////////////////////////////
    //
    //	REGISTRATION PROCEDURE
    //
    ////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Database procedure to register table functions and views against
     * all of the tables in a schema in a foreign database.
     * </p>
     *
     * @param   foreignSchemaName   Case-sensitive name of foreign schema
     * @param   foreignDriverName   Class name of foreign JDBC driver
     * @param   connectionURL       URL for connecting to foreign database via DriverManager.getConnection()
     * @param   debug               True if query string should be printed to System.out
     */
    public  static  void    registerForeignTables
        (
         String foreignSchemaName,
         String foreignDriverName,
         String connectionURL,
         boolean debug
         )
        throws SQLException
    {
        Connection          conn = getForeignConnection( foreignDriverName, connectionURL );
        DatabaseMetaData    dbmd = conn.getMetaData();
        ResultSet           tableCursor = dbmd.getTables( null, foreignSchemaName, "%", null );

        while( tableCursor.next() )
        {
            registerForeignTable
                (
                 dbmd,
                 foreignSchemaName,
                 tableCursor.getString( 3 ),
                 foreignDriverName,
                 connectionURL,
                 debug
                 );
        }

        tableCursor.close();
    }
    private static  void    registerForeignTable
        (
         DatabaseMetaData   dbmd,
         String             foreignSchemaName,
         String             foreignTableName,
         String             foreignDriverName,
         String             connectionURL,
         boolean            debug
        )
        throws SQLException
    {
        //
        // Create DDL string for registering the table function
        //
        StringBuilder       tfBuffer = new StringBuilder();

        tfBuffer.append( "create function " + doubleQuote( foreignTableName ) );
        tfBuffer.append( "\n(" );
        tfBuffer.append( "\n\tforeignSchemaName varchar( 32672 )," );
        tfBuffer.append( "\n\tforeignTableName varchar( 32672 )," );
        tfBuffer.append( "\n\tforeignDriverName varchar( 32672 )," );
        tfBuffer.append( "\n\tconnectionURL varchar( 32672 )," );
        tfBuffer.append( "\n\tdebug boolean" );
        tfBuffer.append( "\n)\nreturns table\n(" );
        
        ResultSet           columnCursor = dbmd.getColumns( null, foreignSchemaName, foreignTableName, "%" );
        int                 columnCount = 0;
        while( columnCursor.next() )
        {
            tfBuffer.append( "\n\t" );
            if ( columnCount > 0 ) { tfBuffer.append( ", " ); }
            columnCount++;

            tfBuffer.append( doubleQuote( columnCursor.getString( 4 ) ) );
            tfBuffer.append( " " );
            tfBuffer.append
                (
                 mapType
                 (
                  columnCursor.getInt( 5 ),
                  columnCursor.getInt( 7 ),
                  columnCursor.getInt( 9 ),
                  columnCursor.getString( 6 )
                  )
                 );
        }
        columnCursor.close();

        tfBuffer.append( "\n)" );
        tfBuffer.append( "\nlanguage java parameter style derby_jdbc_result_set no sql" );
        tfBuffer.append( "\nexternal name '" + ForeignTableVTI.class.getName() + ".readForeignTable'" );

        String          tfDDL = tfBuffer.toString();
        
        //
        // Create DDL string for registering the view
        //
        StringBuilder   viewBuffer = new StringBuilder();

        viewBuffer.append( "create view " + doubleQuote( foreignTableName ) );
        viewBuffer.append( "\nas select *" );
        viewBuffer.append( "\nfrom table" );
        viewBuffer.append( "\n(\n" );
        viewBuffer.append( "\t" + doubleQuote( foreignTableName ) );
        viewBuffer.append( "\n\t(" );
        viewBuffer.append( "\n\t\t" + singleQuote( foreignSchemaName ) + "," );
        viewBuffer.append( "\n\t\t" + singleQuote( foreignTableName ) + "," );
        viewBuffer.append( "\n\t\t" + singleQuote( foreignDriverName ) + "," );
        viewBuffer.append( "\n\t\t" + singleQuote( connectionURL ) + "," );
        viewBuffer.append( "\n\t\t" + debug );
        viewBuffer.append( "\n\t)" );
        viewBuffer.append( "\n) s" );

        String          viewDDL = viewBuffer.toString();

        //
        // Now create the table function and view.
        //
        Connection          conn = getDerbyConnection();
        PreparedStatement   ddl;

        ddl = prepareStatement( conn, tfDDL, debug );
        ddl.execute();
        ddl.close();

        ddl = prepareStatement( conn, viewDDL, debug );
        ddl.execute();
        ddl.close();
    }
    
    /**
     * <p>
     * Get the type of an external database's column as a Derby type name.
     * </p>
     *
     */
    private static  String    mapType( int jdbcType, int precision, int scale, String foreignTypeName )
        throws SQLException
    {
        switch( jdbcType )
        {
        case    Types.BIGINT:           return "bigint";
        case    Types.BINARY:           return "char " + precisionToLength( precision ) + "  for bit data";
        case    Types.BIT:              return "boolean";
        case    Types.BLOB:             return "blob";
        case    Types.BOOLEAN:          return "boolean";
        case    Types.CHAR:             return "char" + precisionToLength( precision );
        case    Types.CLOB:             return "clob";
        case    Types.DATE:             return "date";
        case    Types.DECIMAL:          return "decimal" + precisionAndScale( precision, scale );
        case    Types.DOUBLE:           return "double";
        case    Types.FLOAT:            return "float";
        case    Types.INTEGER:          return "integer";
        case    Types.LONGVARBINARY:    return "long varchar for bit data";
        case    Types.LONGVARCHAR:      return "long varchar";
        case    Types.NUMERIC:          return "numeric" + precisionAndScale( precision, scale );
        case    Types.REAL:             return "real";
        case    Types.SMALLINT:         return "smallint";
        case    Types.TIME:             return "time";
        case    Types.TIMESTAMP:        return "timestamp";
        case    Types.TINYINT:          return "smallint";
        case    Types.VARBINARY:        return "varchar " + precisionToLength( precision ) + "  for bit data";
        case    Types.VARCHAR:          return "varchar" + precisionToLength( precision );
        case    XML_TYPE:               return "xml";
 
        default:
            throw new SQLException
                ( "Unknown external data type. JDBC type = " + jdbcType + ", external type name = " + foreignTypeName );
        }
    }

    /**
     * <p>
     * Turns precision into a length designator.
     * </p>
     *
     */
    private static  String  precisionToLength( int precision )
    {
        return "( " + precision + " )";
    }

    /**
     * <p>
     * Build a precision and scale designator.
     * </p>
     *
     */
    private static  String  precisionAndScale( int precision, int scale )
    {
        return "( " + precision + ", " + scale + " )";
    }

    ////////////////////////////////////////////////////////////////////////
    //
    //	DE-REGISTRATION PROCEDURE
    //
    ////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Database procedure to de-register the table functions and views declared against
     * the tables in a schema in a foreign database. Silently swallows errors if
     * the objects don't exist.
     * </p>
     *
     * @param   foreignSchemaName   Case-sensitive name of foreign schema
     * @param   foreignDriverName   Class name of foreign JDBC driver
     * @param   connectionURL       URL for connecting to foreign database via DriverManager.getConnection()
     * @param   debug               True if query string should be printed to System.out
     */
    public  static  void    deregisterForeignTables
        (
         String foreignSchemaName,
         String foreignDriverName,
         String connectionURL,
         boolean debug
         )
        throws SQLException
    {
        Connection          foreignConn = getForeignConnection( foreignDriverName, connectionURL );
        Connection          derbyConn = getDerbyConnection();
        DatabaseMetaData    dbmd = foreignConn.getMetaData();
        ResultSet           tableCursor = dbmd.getTables( null, foreignSchemaName, "%", null );

        while( tableCursor.next() )
        {
            String          objectName = tableCursor.getString( 3 );
            
            dropObject( derbyConn, objectName, "view", debug );
            dropObject( derbyConn, objectName, "function", debug );
        }

        tableCursor.close();
    }
    /**
     * <p>
     * Drop a schema object. If the object does not exist, silently
     * swallow the error.
     * </p>
     */
    private static  void    dropObject
        (
         Connection         conn,
         String             objectName,
         String             objectType,
         boolean            debug
        )
        throws SQLException
    {
        try {
            PreparedStatement   ps = prepareStatement
                (
                 conn,
                 "drop " + objectType + " " + doubleQuote( objectName ),
                 debug
                 );
            ps.execute();
            ps.close();
        }
        catch (SQLException se)
        {
            String  actualSQLState = se.getSQLState();

            if ( "X0X05".equals( actualSQLState ) ) {}
            else if ( "42Y55".equals( actualSQLState ) ) {}
            else { throw se; }
        }
    }

    ////////////////////////////////////////////////////////////////////////
    //
    //	ResultSet BEHAVIOR
    //
    ////////////////////////////////////////////////////////////////////////

    public  void    close() throws SQLException
    {
        if ( !isClosed() )
        {
            _foreignSchemaName = null;
            _foreignTableName = null;
            _foreignDriverName = null;
            _connectionURL = null;
            _columnNames = null;
            _restriction = null;
            _columnNumberMap = null;

            if ( _foreignResultSet != null ) { _foreignResultSet.close(); }
            if ( _foreignPreparedStatement != null ) { _foreignPreparedStatement.close(); }

            _foreignResultSet = null;
            _foreignPreparedStatement = null;
        }
    }

    public  boolean next()  throws SQLException
    {
        if ( !isClosed() && (_foreignResultSet == null) )
        {
            _foreignPreparedStatement = prepareStatement
                ( getForeignConnection( _foreignDriverName, _connectionURL ), makeQuery(), _debug );
            _foreignResultSet = _foreignPreparedStatement.executeQuery();
        }

        return _foreignResultSet.next();
    }

    public boolean isClosed() { return (_connectionURL == null); }

    public  boolean wasNull()   throws SQLException
    { return _foreignResultSet.wasNull(); }

    public  ResultSetMetaData   getMetaData()   throws SQLException
    { return _foreignResultSet.getMetaData(); }

    public  InputStream 	getAsciiStream(int i) throws SQLException
    { return _foreignResultSet.getAsciiStream( mapColumnNumber( i ) ); }
    
    public  BigDecimal 	getBigDecimal(int i) throws SQLException
    { return _foreignResultSet.getBigDecimal( mapColumnNumber( i ) ); }
    
    public  BigDecimal 	getBigDecimal(int i, int scale) throws SQLException
    { return _foreignResultSet.getBigDecimal( mapColumnNumber( i ), scale ); }
    
    public  InputStream 	getBinaryStream(int i)  throws SQLException
    { return _foreignResultSet.getBinaryStream( mapColumnNumber( i ) ); }
    
    public  Blob 	getBlob(int i)  throws SQLException
    { return _foreignResultSet.getBlob( mapColumnNumber( i ) ); }
    
    public  boolean 	getBoolean(int i) throws SQLException
    { return _foreignResultSet.getBoolean( mapColumnNumber( i ) ); }
    
    public  byte 	getByte(int i)    throws SQLException
    { return _foreignResultSet.getByte( mapColumnNumber( i ) ); }
    
    public  byte[] 	getBytes(int i) throws SQLException
    { return _foreignResultSet.getBytes( mapColumnNumber( i ) ); }
    
    public  Reader 	getCharacterStream(int i) throws SQLException
    { return _foreignResultSet.getCharacterStream( mapColumnNumber( i ) ); }

    public  Clob 	getClob(int i)  throws SQLException
    { return _foreignResultSet.getClob( mapColumnNumber( i ) ); }

    public  Date 	getDate(int i)  throws SQLException
    { return _foreignResultSet.getDate( mapColumnNumber( i ) ); }

    public  Date 	getDate(int i, Calendar cal)    throws SQLException
    { return _foreignResultSet.getDate( mapColumnNumber( i ), cal ); }

    public  double 	getDouble(int i)    throws SQLException
    { return _foreignResultSet.getDouble( mapColumnNumber( i ) ); }

    public  float 	getFloat(int i) throws SQLException
    { return _foreignResultSet.getFloat( mapColumnNumber( i ) ); }

    public  int 	getInt(int i)   throws SQLException
    { return _foreignResultSet.getInt( mapColumnNumber( i ) ); }

    public  long 	getLong(int i)  throws SQLException
    { return _foreignResultSet.getLong( mapColumnNumber( i ) ); }

    public  Object 	getObject(int i)    throws SQLException
    { return _foreignResultSet.getObject( mapColumnNumber( i ) ); }

    public  short 	getShort(int i) throws SQLException
    { return _foreignResultSet.getShort( mapColumnNumber( i ) ); }

    public  String 	getString(int i)    throws SQLException
    { return _foreignResultSet.getString( mapColumnNumber( i ) ); }

    public  Time 	getTime(int i)  throws SQLException
    { return _foreignResultSet.getTime( mapColumnNumber( i ) ); }

    public  Time 	getTime(int i, Calendar cal)    throws SQLException
    { return _foreignResultSet.getTime( mapColumnNumber( i ), cal ); }

    public  Timestamp 	getTimestamp(int i) throws SQLException
    { return _foreignResultSet.getTimestamp( mapColumnNumber( i ) ); }

    public  Timestamp 	getTimestamp(int i, Calendar cal)   throws SQLException
    { return _foreignResultSet.getTimestamp( mapColumnNumber( i ), cal ); }

    ////////////////////////////////////////////////////////////////////////
    //
    //	RestrictedVTI BEHAVIOR
    //
    ////////////////////////////////////////////////////////////////////////

    public  void    initScan
        ( String[] columnNames, Restriction restriction )
        throws SQLException
    {
        _columnNames = columnNames;
        _restriction = restriction;

        int columnCount = _columnNames.length;

        _columnNumberMap = new int[ columnCount ];
        int foreignColumnID = 1;
        for ( int i = 0; i < columnCount; i++ )
        {
            if ( columnNames[ i ] != null ) { _columnNumberMap[ i ] = foreignColumnID++; }
        }
    }

    ////////////////////////////////////////////////////////////////////////
    //
    //	Connection MANAGEMENT
    //
    ////////////////////////////////////////////////////////////////////////

    private static  Connection  getForeignConnection
        ( String foreignDriverName, String connectionURL )
        throws SQLException
    {
        Connection  conn = _connections.get( connectionURL );

        if ( conn == null )
        {
            try { Class.forName( foreignDriverName ); }
            catch (ClassNotFoundException cnfe) { throw new SQLException( cnfe.getMessage() ); }
                    
            conn = DriverManager.getConnection( connectionURL );

            if ( conn != null ) { _connections.put( connectionURL, conn ); }
        }

        return conn;
    }

    private static  Connection  getDerbyConnection() throws SQLException
    {
        return DriverManager.getConnection( "jdbc:default:connection" );
    }



    ////////////////////////////////////////////////////////////////////////
    //
    //	QUERY FACTORY
    //
    ////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Build the query which will be sent to the foreign database.
     * </p>
     */
    private String  makeQuery()
    {
        StringBuilder   buffer = new StringBuilder();

        buffer.append( "select " );

        int possibleCount = _columnNames.length;
        int actualCount = 0;
        for ( int i = 0; i < possibleCount; i++ )
        {
            String  rawName = _columnNames[ i ];
            if ( rawName == null ) { continue; }

            if ( actualCount > 0 ) { buffer.append( ", " ); }
            actualCount++;
            
            buffer.append( doubleQuote( rawName ) );
        }

        buffer.append( "\nfrom " );
        buffer.append( doubleQuote( _foreignSchemaName ) );
        buffer.append( '.' );
        buffer.append( doubleQuote( _foreignTableName ) );

        if ( _restriction != null )
        {
            String  clause = _restriction.toSQL();

            if (clause != null)
            {
                clause = clause.trim();
                if ( clause.length() != 0 )
                {
                    buffer.append( "\nwhere " + clause );
                }
            }
        }

        return buffer.toString();
    }

    private static  String  doubleQuote( String text )  { return '"' + text + '"'; }
    private static  String  singleQuote( String text )  { return '\'' + text + '\''; }

    private static  PreparedStatement   prepareStatement
        ( Connection conn, String text, boolean debug )
        throws SQLException
    {
        if ( debug ) { System.out.println( text ); }

        return conn.prepareStatement( text );
    }

    ////////////////////////////////////////////////////////////////////////
    //
    //	UTILITY METHODS
    //
    ////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Map a 1-based Derby column number to a 1-based column number in the
     * foreign query.
     * </p>
     */
    private int mapColumnNumber( int derbyNumber )
    {
        return _columnNumberMap[ derbyNumber - 1 ];
    }
}
