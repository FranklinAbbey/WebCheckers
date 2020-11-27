package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

public class PostExitGameRoute implements Route {

    private GameCenter gameCenter;
    public PostExitGameRoute(GameCenter gameCenter) {
        this.gameCenter = gameCenter;
    }

    @Override
    public Object handle(Request request, Response response)
    {
        return new Gson().toJson(new Message("Exit Game", Message.Type.INFO));
    }
}
