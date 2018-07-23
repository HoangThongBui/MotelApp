'use strict'

const PostDetailController = require('../controllers/PostDetailController');

module.exports = function(app){
    app.route('/admin/post/:post_id')
        .get(PostDetailController.get_post_detail);

    app.route('/admin/post/confirm/:post_id')
        .post(PostDetailController.confirm_post);

    app.route('/admin/post/reject/:post_id')
        .post(PostDetailController.reject_post);
        
    app.route('/admin/post/delete/:post_id')
        .post(PostDetailController.delete_post);

    app.route('/admin/post/comment/delete_comment/:comment_id/:post_id')
        .get(PostDetailController.delete_comment);
}