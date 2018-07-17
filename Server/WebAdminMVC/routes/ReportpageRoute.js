'use strict'

const ReportpageController = require('../controllers/ReportpageController');

module.exports = (app) => {
    app.route('/admin/post_report')
        .get(ReportpageController.go_to_post_report_page);
}