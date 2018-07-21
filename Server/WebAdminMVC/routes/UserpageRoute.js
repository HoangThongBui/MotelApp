'use strict'

const UserpageController = require('../controllers/UserpageController');
const Multer = require('../../custom_modules/multer');

module.exports = (app) => {
    app.route('/admin/user')
        .get(UserpageController.go_to_user_page);

    app.route('/admin/user/ban/:user_id')
        .get(UserpageController.ban_user);
    
    app.route('/admin/user/unban/:user_id')
        .get(UserpageController.unban_user);

    app.route('/admin/user/add_more_account/:account_type')
        .get(UserpageController.add_more_account);

    app.route('/admin/user/add_new_account/:account_type')
        .post(Multer.single('avatar'), UserpageController.add_new_account);
}