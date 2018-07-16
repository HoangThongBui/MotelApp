'use strict'

const ReportpageController = require('../controllers/ReportpageController');

module.exports = (app) => {
    app.route('/admin/report')
        .get(ReportpageController.go_to_report_page);
}