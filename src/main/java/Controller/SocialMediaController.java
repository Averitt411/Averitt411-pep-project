package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }
    
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::userRegistrationHandler);
        app.post("/login", this::userLoginHandler);
        app.post("/messages", this::messageCreationHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserHandler);
    //    app.get("example-endpoint", this::exampleHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    //private void exampleHandler(Context context) {
    //    context.json("sample text");
    //}

    private void userRegistrationHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account registeredUser = accountService.registerUser(account);
        if(registeredUser==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(registeredUser));
        }
    }

    private void userLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInUser = accountService.loginUser(account);
        if(loggedInUser!=null){
            ctx.json(mapper.writeValueAsString(loggedInUser));
        }else{
            ctx.status(401);
        }
    }

    private void messageCreationHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage!=null){
            ctx.json(mapper.writeValueAsString(addedMessage));
        }else{
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx){
        ctx.json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException{
        Message gottenMessage = messageService.getMessageById(Integer.parseInt(ctx.pathParam("message_id")));
        if(gottenMessage==null){
            ctx.json("");
        }else{
        ctx.json(messageService.getMessageById(Integer.parseInt(ctx.pathParam("message_id"))));
        }
    }

    private void deleteMessageByIdHandler(Context ctx){
        Message toBeDeleted = messageService.getMessageById(Integer.parseInt(ctx.pathParam("message_id")));
        if(toBeDeleted==null){
            ctx.json("");
        }else{
        ctx.json(messageService.deleteMessageById(Integer.parseInt(ctx.pathParam("message_id"))));
        }
    }

    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message updatedMessage = messageService.updateMessageById(Integer.parseInt(ctx.pathParam("message_id")), message.getMessage_text());
        if(updatedMessage!=null){
            ctx.json(messageService.updateMessageById(Integer.parseInt(ctx.pathParam("message_id")), message.getMessage_text()));
        }else{
            ctx.status(400);
        }
    }

    private void getAllMessagesByUserHandler(Context ctx){
        ctx.json(messageService.getAllMessagesByUser(Integer.parseInt(ctx.pathParam("account_id"))));
    }
}