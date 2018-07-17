'use strict'

const UserpageController = require('../controllers/UserpageController');

module.exports = (app) => {
    app.route('/admin/user')
        .get(UserpageController.go_to_user_page);

    app.route('/admin/user/ban/:user_id')
        .get(UserpageController.ban_user);
    
    app.route('/admin/user/unban/:user_id')
        .get(UserpageController.unban_user);
}