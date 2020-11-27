package com.webcheckers.ui;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The GET /signin route handler
 *
 * @authors
 *      Summer DiStefano
 *      Frank Abbey
 *      Raisa Hossain
 *      Stephen Bosonac
 *
 * @version 2/18/2020
 *
 */
public class GetSignInRoute implements Route {

    static final String TITLE = "Player Sign In";
    static final String VIEW_NAME = "signin.ftl";

    private static final Message SIGNIN_MSG = Message.info("Please sign in below");

    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all GET /signin HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     *
     */
    public GetSignInRoute(final TemplateEngine templateEngine) {
        //validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
    }

    /**
     * Render the WebCheckers Sign In page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Sign In page
     */
    @Override
    public String handle(Request request, Response response) {
        // retrieve the game object and start one if no game is in progress
        final Session httpSession = request.session();

        // build the View-Model
        final Map<String, Object> vm = new HashMap<>();
        //validKey = httpSession.attribute(PostSignInRoute.INVALID_KEY);

        //if the user has entered an invalid username, display an invalid message
        if(httpSession.attribute(PostSignInRoute.INVALID_KEY) != null)
            vm.put(GetHomeRoute.MESSAGE_ATTR, httpSession.attribute(PostSignInRoute.INVALID_KEY));
        //else the user has entered a valid username or has not entered anything yet
        else
            vm.put(GetHomeRoute.MESSAGE_ATTR, SIGNIN_MSG);

        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);

        // render the Game Form view
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }

}
