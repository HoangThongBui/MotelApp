'use strict'

const HomepageController = require('../controllers/HomepageController');
const SessionChecker = require('../../custom_modules/session');

module.exports = (app) => {
    app.route('/admin/home')
        .get(HomepageController.go_to_home_page);

    app.route('/admin')
        .get(HomepageController.home_redirect);
}