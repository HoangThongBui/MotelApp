"use strict";

const PostController = require("../controllers/PostController");

module.exports = function(app) {
    app.route('/post/api/get_posts_by_city/')
        .get(PostController.get_posts_by_city);

    app.route('/post/api/get_posts_by_user/:user_id')
        .get(PostController.get_posts_by_user);

    app.route('/post/api/make_new_post')
        .post(PostController.make_a_new_post);
};
