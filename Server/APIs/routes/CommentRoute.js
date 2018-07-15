'use strict'

const CommentController = require('../controllers/CommentController');

module.exports = function(app){
    app.route('/comment/api/get_comments/:post_id')
        .get(CommentController.get_comments);

    app.route('/comment/api/post_a_comment/')
        .post(CommentController.post_a_comment);
}