package se.fikaware.tarta.pages;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormDataParser;
import se.fikaware.tarta.models.Post;
import se.fikaware.tarta.models.School;
import se.fikaware.web.Request;
import se.fikaware.web.Response;

public class PostsPage {

    public static void feedGet(HttpServerExchange exchange) {
        School school = School.load("New School");
        Response.json(exchange, Post.getAll(school));
    }

    public static void create(HttpServerExchange exchange) {
        School school = School.load("New School");
        var form = exchange.getAttachment(FormDataParser.FORM_DATA);
        var title = Request.getString(form, "title", null);
        var content = Request.getString(form, "content", null);

        if (title == null || content == null) {
            exchange.setStatusCode(400);
        } else {
            Post.create(school, title, content);
            // TODO: Post a notification for all client.
        }
    }
}
