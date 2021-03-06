/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.json.controller;

import edu.uoc.dao.ChatDao;
import edu.uoc.dao.UserMeetingDao;
import edu.uoc.model.Chat;
import edu.uoc.model.JSONRequest;
import edu.uoc.model.JSONResponse;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.User;
import edu.uoc.model.UserMeeting;
import edu.uoc.util.Constants;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author antonibertranbellido
 */
@Controller
@RequestMapping("/chat")
public class ChatController {
    //get log4j handler
    private static final Logger logger = Logger.getLogger(ChatController.class);

    @Autowired
    private ChatDao chatDao;
    
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody JSONResponse currentUserAcceptConnection(@RequestBody JSONRequest message, HttpSession session) {
        JSONResponse response = new JSONResponse();
        MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);
        User user = (User) session.getAttribute(Constants.USER_SESSION);
        try {
            if (user!=null && meeting!=null) {
                Chat chat = new Chat();
                chat.setUser(user);
                logger.debug("Chat message from "+user.getFullname()+" to "+message.getRequest());
                chat.setChat_message(message.getRequest());
                chat.setMeeting_room(meeting);
                
                chatDao.save(chat);
                
                response.setOk(true);
            }
        } catch (Exception e) {
            logger.error("Error registering chat", e);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }
}
