'use strict'

const PostDetailController = require('../controllers/PostDetailController');

module.exports = function(app){
    app.route('/admin/post/:post_id')
        .get(PostDetailController.get_post_detail);
}