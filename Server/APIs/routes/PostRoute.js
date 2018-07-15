"use strict";

const PostController = require("../controllers/PostController");
const Multer = require("../../custom_modules/multer");

module.exports = function(app) {
    app.route('/post/api/get_posts_nearby/')
        .get(PostController.get_posts_nearby); 

    app.route('/post/api/get_posts_by_user/:user_id')
        .get(PostController.get_posts_by_user);

    app.route('/post/api/make_new_post/')
        .post(Multer.array("room_images", 3), PostController.make_a_new_post);

    app.route('/post/api/get_newest_posts/')
        .get(PostController.get_newest_posts);

    app.route('/post/api/search_post/')
        .get(PostController.search_post);

    app.route('/post/api/get_post_by_id/:post_id')
        .get(PostController.get_post_by_id);

    app.route('/post/api/edit_post_by_id/:post_id')
        .put(Multer.array("room_images", 3), PostController.edit_post_by_id);

    app.route('/post/api/delete_post_by_id/:post_id')
        .delete(PostController.delete_post_by_id);

};
