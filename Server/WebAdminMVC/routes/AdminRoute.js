'use strict'

const AdminController = require('../controllers/AdminController');

module.exports = (app) => {
    app.route('/admin/change_password')
        .get(AdminController.go_to_confirm_current_password);

    app.route('/admin/change_password/confirm_current_password')
        .post(AdminController.confirm_current_password);

    app.route('/admin/change_password/create_new_password')
        .post(AdminController.create_new_password);
}