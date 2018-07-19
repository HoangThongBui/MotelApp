'use strict'

const PostreportpageController = require('../controllers/PostreportpageController');

module.exports = (app) => {
    app.route('/admin/post_report')
        .get(PostreportpageController.go_to_post_report_page);
}