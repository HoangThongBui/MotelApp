"use strict";

const PostController = require("../controllers/PostController");

module.exports = function(app) {
    app.route('/post/api/get_posts_by_city/:city')
        .get(PostController.get_posts_by_city)
};
