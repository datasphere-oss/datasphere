package com.huahui.datasphere.utils;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PGSQLConnection {

	private String url = "jdbc:postgresql://xxx.xxx.xxx.xxx:5432/testdb";
    private String username = "postgres";
    private String password = "postgres";
    private Connection connection = null;

    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            connection = DriverManager.getConnection(url, username, password);
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }


    public ResultSet query(Connection conn, String sql) {
        PreparedStatement pStatement = null;
        ResultSet rs = null;
        try {
            pStatement = conn.prepareStatement(sql);
            rs = pStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return rs;
    }

    public boolean queryUpdate(Connection conn, String sql) {
        PreparedStatement pStatement = null;
        int rs = 0;
        try {
            pStatement = conn.prepareStatement(sql);
            rs = pStatement.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (rs > 0) {
            return true;
        }
        return false;
    }
    
    public int fillStatementParameters(final int begin, final PreparedStatement pstmt, final Object... params) throws SQLException {
        if (params == null) {
            return 0;
        }
        if (params.length == 1 && params[0] instanceof List) {
            final List paramList = (List)params[0];
            final int end = paramList.size();
            int pstmtIndex = 0;
            for (int i = 0; i < end; ++i) {
                final Object param = paramList.get(i);
                pstmtIndex = begin + i + 1;
                this.fillStatementParameter(pstmt, pstmtIndex, param);
            }
            return pstmtIndex;
        }
        final int end2 = params.length;
        int pstmtIndex2 = 0;
        for (int j = 0; j < end2; ++j) {
            final Object param2 = params[j];
            pstmtIndex2 = begin + j + 1;
            this.fillStatementParameter(pstmt, pstmtIndex2, param2);
        }
        return pstmtIndex2;
    }
    
    public void fillStatementParameter(final PreparedStatement pstmt, final int i, final Object param) throws SQLException {
        if (param == null) {
            pstmt.setNull(i, 0);
        }
        else if (param instanceof InputStream) {
            pstmt.setBinaryStream(i, (InputStream)param);
        }
        else if (param instanceof Reader) {
            pstmt.setCharacterStream(i, (Reader)param);
        }
        else {
            pstmt.setObject(i, param);
        }
    }

}
