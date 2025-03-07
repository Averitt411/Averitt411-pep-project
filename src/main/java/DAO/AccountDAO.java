package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try{
            String sql = "select * from account;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account insertAccount(Account account){
        if(account.getPassword().length()<4 || account.getUsername()==null || usernameExists(account.getUsername()) == true){
            return null;
        }else{
            Connection connection = ConnectionUtil.getConnection();
            try{
                String sql = "insert into account (username, password) values (?, ?);";
                PreparedStatement ps = connection.prepareStatement(sql);
                
                ps.setString(1,account.getUsername());
                ps.setString(2,account.getPassword());

                ps.executeUpdate();
                ResultSet pkeyResultSet = ps.getGeneratedKeys();
                if(pkeyResultSet.next()){
                    int generated_account_id = (int) pkeyResultSet.getLong(1);
                    return new Account(generated_account_id, account.getUsername(), account.getPassword());
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }  
        }
        return null;
    }

    public Account getAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "select * from account where username=? and password=?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Account account1  = new Account(rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password"));
                return account1;
                // WORK ON THIS //   
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean usernameExists(String username){
        Connection connection = ConnectionUtil.getConnection();
        Boolean userExists = false;
        try{
            String sql1 = "select * from account where username=?;";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ps1.setString(1,username);
            ResultSet rs1 = ps1.executeQuery();
            if(rs1!=null){
                userExists = true;
            }
        }catch(SQLException e){
        System.out.println(e.getMessage());
        }
        return userExists;
    }
} 
