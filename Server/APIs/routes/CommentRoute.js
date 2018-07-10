'use strict'

const CommentController = require('../controllers/CommentController');

module.exports = function(app){
    app.route('/comment/api/get_comments/:post_id')
        .get(CommentController.get_comments);
}