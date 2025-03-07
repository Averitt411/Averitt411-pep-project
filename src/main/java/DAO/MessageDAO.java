package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "select * from message;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch"));
                    messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "select * from message where message_id=?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,message_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch"));
                    return message;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message insertMessage(Message message){
        if(message.getMessage_text()==null || message.getMessage_text().length()>255 || userExists(message.getPosted_by()) == false || message.message_text.isEmpty()){
            return null;
        }else{
            Connection connection = ConnectionUtil.getConnection();
            try{
                String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?);";
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                ps.setInt(1,message.getPosted_by());
                ps.setString(2,message.getMessage_text());
                ps.setLong(3,message.getTime_posted_epoch());

                ps.executeUpdate();
                ResultSet pkeyResultSet = ps.getGeneratedKeys();
                if(pkeyResultSet.next()){
                    int generated_message_id = (int) pkeyResultSet.getLong(1);
                    return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public Message deleteMessageById(int message_id){
        Message messageToDelete = getMessageById(message_id);
        if(messageToDelete==null || messageToDelete.getMessage_text().isEmpty()){
            return null;
        }else{
            Connection connection = ConnectionUtil.getConnection();
            try{
                String sql = "delete from message where message_id=?;";
                PreparedStatement ps = connection.prepareStatement(sql);

                ps.setInt(1, message_id);

                ps.executeUpdate();

            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
            return messageToDelete;
        }
        
    }

    public List<Message> getAllMessagesByUser(int posted_by){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "select * from message where posted_by=?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, posted_by);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch"));
                    messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message updateMessageById(int message_id, String message_text){
        if(message_text==null || message_text.length()>255 || getMessageById(message_id)==null || message_text.isEmpty() || message_text.isBlank()){
            return null;
        }else{
            Connection connection = ConnectionUtil.getConnection();
            try{
                String sql = "update message set message_text = ? where message_id = ?;";
                PreparedStatement ps = connection.prepareStatement(sql);
    
                ps.setString(1,message_text);
                ps.setInt(2,message_id);
    
                ps.executeUpdate();
    
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
            return getMessageById(message_id);
        }
    }

    public boolean userExists(int posted_by){
        Connection connection = ConnectionUtil.getConnection();
        Boolean userExists = false;
        try{
            String sql1 = "select * from message where posted_by=?;";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ps1.setInt(1,posted_by);
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
