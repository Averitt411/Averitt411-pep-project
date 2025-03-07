package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message){
        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message deleteMessageById(int message_id){
        return messageDAO.deleteMessageById(message_id);
    }

    public Message getMessageById(int message_id){
        return messageDAO.getMessageById(message_id);
    }

    public List<Message> getAllMessagesByUser(int posted_by){
        return messageDAO.getAllMessagesByUser(posted_by);
    }

    public Message updateMessageById(int message_id, String message_text){
        return messageDAO.updateMessageById(message_id, message_text);
    }
}
