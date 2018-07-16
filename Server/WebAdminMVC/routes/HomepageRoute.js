'use strict'

const HomepageController = require('../controllers/HomepageController');

module.exports = (app) => {
    app.route('/admin/home')
        .get(HomepageController.go_to_home_page);
}