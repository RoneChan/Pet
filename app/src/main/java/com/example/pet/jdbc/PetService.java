package com.example.pet.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PetService {
    JdbcUtil jdbcUtil;
    Connection conn;

    public String getRestNumber(String Id) {
        String restNumber = "";
        try {
            Connection c = jdbcUtil.createConnection();
            String sql = "select name from clothes where ID=20210230539810;";
            Statement stmt = c.createStatement();
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                restNumber = res.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restNumber;
    }
}
