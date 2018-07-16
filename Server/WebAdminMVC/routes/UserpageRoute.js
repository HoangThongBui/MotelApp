'use strict'

const UserpageController = require('../controllers/UserpageController');

module.exports = (app) => {
    app.route('/admin/user')
        .get(UserpageController.go_to_user_page);
}