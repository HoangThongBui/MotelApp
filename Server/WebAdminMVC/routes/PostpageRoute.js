'use strict'

const PostpageController = require('../controllers/PostpageController');

module.exports = (app) => {
    app.route('/admin/post')
        .get(PostpageController.go_to_post_page);
}